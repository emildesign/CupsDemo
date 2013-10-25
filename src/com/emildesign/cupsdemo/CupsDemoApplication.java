package com.emildesign.cupsdemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



import com.emildesign.cupsdemo.listeners.MyLocationListener;
import com.emildesign.cupsdemo.objects.CoffeeShop;
import com.emildesign.cupsdemo.utils.CupsRESTClient;
import com.emildesign.cupsdemo.utils.JSONParsing;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Application;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class CupsDemoApplication extends Application 
{
	private static final String TAG = CupsDemoApplication.class.getSimpleName();
	
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	private List<CoffeeShop> coffeeShopsList = new ArrayList<CoffeeShop>();
	private Location currentUserLocation;
	private static CupsDemoApplication appObjInstance;
	private MyLocationListener locationListener;
	private LocationManager locationManager;
	private Location location;

	@Override
	public void onCreate() {
		super.onCreate();
		
		appObjInstance = this;
		locationListener = new MyLocationListener(this);  
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		Log.d(TAG, "Getting json from internet");
		CupsRESTClient.get("", null, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String response) 
			{
				coffeeShopsList = JSONParsing.NativeJsonParseMethodForCoffeeShops(response);
				sortCoffeeShopByDistanceFromCurrentLocation();
			}

			@Override
			public void onFailure(Throwable e, String response) {
				Log.d(TAG, "failed Getting: " + response);
			}

			@Override
			public void onFinish() {
				Log.d(TAG, "finish Getting ");
			}
		});
		Log.d(TAG, "coffeeShopsList: " + coffeeShopsList.toString());
		startListenToLocationUpdates();
				
	}
	
	/** Determines whether one Location reading is better than the current Location fix
	  * @param location  The new Location that you want to evaluate
	  * @param currentBestLocation  The current Location fix, to which you want to compare the new one
	  */
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
	
	public void sortCoffeeShopByDistanceFromCurrentLocation() {
		Log.d(TAG, "Sorting Coffee Shops By Distance From Current Location");
		if (location != null &&  currentUserLocation != null) 
	 	{
	 		if (isBetterLocation(location, currentUserLocation))
	 		{
	 			currentUserLocation = location;
	 		}
	 	}
	 	else if (location != null)
	 	{
	 		currentUserLocation = location;
	 	}
	 	if(currentUserLocation != null)	
	 	{
	 		Location tempLocation;
	 		for ( CoffeeShop tempCoffeeShop: coffeeShopsList)
	 		{
	 			tempLocation = new Location("");
	 			tempLocation.setLatitude(Double.parseDouble(tempCoffeeShop.getCoffeeShopLat()));
	 			tempLocation.setLongitude(Double.parseDouble(tempCoffeeShop.getCoffeeShopLng()));
	 			float distance = currentUserLocation.distanceTo(tempLocation);
	 			distance = distance/1000;
	 			tempCoffeeShop.setCoffeeShopDistance(distance);
	 			//Log.d(TAG, "distnace: " + String.valueOf(tempCoffeeShop.getCoffeeShopDistance()));
	 		}
	 		
	 		Collections.sort(coffeeShopsList, new Comparator<CoffeeShop>() {
	 		    @Override
	 		    public int compare(CoffeeShop c1, CoffeeShop c2) {
	 		        return new Float(c1.getCoffeeShopDistance()).compareTo(new Float(c2.getCoffeeShopDistance()));
	 		    }
	 		});
	 	} 
	 	else 
	 	{
	 		Toast.makeText(CupsDemoApplication.this, "Your current location could not be found",  Toast.LENGTH_SHORT).show();
	 	} 
	}
	
	public void startListenToLocationUpdates()
	{
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	    boolean enabledGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	    //boolean enabledWiFi = service.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

	        // Check if enabled and if not send user to the GSP settings
	        // Better solution would be to display a dialog and suggesting to 
	        // go to the settings
	        if (!enabledGPS) {
	            Toast.makeText(this, "GPS signal not found, turn on gps sensor", Toast.LENGTH_LONG).show();
	            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);
	        }

	    // Define the criteria how to select the locatioin provider -> use default
	    Criteria criteria = new Criteria();
	    String provider = locationManager.getBestProvider(criteria, false);
	    location = locationManager.getLastKnownLocation(provider);
	}
	
	public void stopListenToLocationUpdates()
	{
		locationManager.removeUpdates(locationListener);
	}
	
	public List<CoffeeShop> getCoffeeShopsList() {
		return coffeeShopsList;
	}
	
	public Location getCurrentUserLocation() {
		return currentUserLocation;
	}
	
	public void setCurrentUserLocation(Location location) {
		if (isBetterLocation(location, currentUserLocation))
 		{
 			currentUserLocation = location;
 			stopListenToLocationUpdates();
 		}
	}
	
	public static CupsDemoApplication getInstance() { 
		return appObjInstance; 
	}
	
	public LocationManager getLocationManager() {
		return locationManager;
	}
}
