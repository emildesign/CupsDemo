package com.emildesign.cupsdemo.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.emildesign.cupsdemo.CupsDemoApplication;
import com.emildesign.cupsdemo.R;
import com.emildesign.cupsdemo.activities.MainActivity;
import com.emildesign.cupsdemo.objects.CoffeeShop;
import com.emildesign.cupsdemo.utils.GMapV2Direction;
import com.emildesign.cupsdemo.utils.GetDirectionsAsyncTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

public class MyMapFragment extends Fragment {
	
	private static final LatLng TELAVIV = new LatLng(32.073229,34.773231);
	private GoogleMap map;
	private SupportMapFragment fragment;
	private MainActivity activity;
	ArrayList<CoffeeShop> coffeeShopsList;
	private LatLng clickMarkerLatLng;

	public View onCreateView(final LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.map_fragment, null, false);	
		fragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
		map = fragment.getMap(); 
		map.setMyLocationEnabled(true);
		
		coffeeShopsList = (ArrayList<CoffeeShop>) ((CupsDemoApplication)getActivity().getApplication()).getCoffeeShopsList();
		
		for (CoffeeShop tempCoffeeShop: coffeeShopsList)
		{
			 Marker marker = map.addMarker(new MarkerOptions()
			.position(new LatLng(Double.parseDouble(tempCoffeeShop.getCoffeeShopLat()), Double.parseDouble(tempCoffeeShop.getCoffeeShopLng())))
			.title(tempCoffeeShop.getCoffeeShopName())
			.snippet(tempCoffeeShop.getCoffeeShopAddress())
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.pinplace_turkiz)));
		}	
		
		// Setting a custom info window adapter for the google map
		if (map != null)
		{
	        map.setInfoWindowAdapter(new InfoWindowAdapter() {
	 
	            // Use default InfoWindow frame
	            @Override
	            public View getInfoWindow(Marker args) {
	                return null;
	            }
	 
	            // Defines the contents of the InfoWindow
	            @Override
	            public View getInfoContents(Marker args) {
	 
	                // Getting view from the layout file info_window_layout
	                View v = inflater.inflate(R.layout.info_window_layout, null);
	 
	                // Getting the position from the marker
	                clickMarkerLatLng = args.getPosition();

	                TextView title = (TextView) v.findViewById(R.id.tvCoffeeShopName);
	                TextView address = (TextView) v.findViewById(R.id.tvCoffeeShopAddress);
	                
	                title.setText(args.getTitle());
	                address.setText(args.getSnippet());
	                
	    	        map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {          
	    	            public void onInfoWindowClick(Marker marker) 
	    	            {
	    	            	findDirections(CupsDemoApplication.getInstance().getCurrentUserLocation().getLatitude(),
	    	            			CupsDemoApplication.getInstance().getCurrentUserLocation().getLongitude(),
 	        					   clickMarkerLatLng.latitude, clickMarkerLatLng.longitude, GMapV2Direction.MODE_DRIVING );
	    	            } 
	    	        }); 
	 
	                // Returning the view containing InfoWindow contents
	                return v;
	 
	            }
	        });	
		}

		// Move the camera instantly to hamburg with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(TELAVIV, 2));

		// Zoom in, animating the camera.
		map.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
		
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (map == null) {
			map = fragment.getMap();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);	
		this.activity = (MainActivity) activity;
	}

	@Override
	public void onStart() {
		super.onStart();
		activity.setMap(map);
	}
	
	public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
		map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
		map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
		map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
		map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);
		
		GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask((MainActivity)getActivity());
		asyncTask.execute(map);	
	}
	
	
}
