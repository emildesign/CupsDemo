package com.emildesign.cupsdemo.listeners;

import java.util.ArrayList;

import com.emildesign.cupsdemo.CupsDemoApplication;
import com.emildesign.cupsdemo.objects.CoffeeShop;

import android.app.Activity;
import android.app.Application;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocationListener implements LocationListener 
{
	static final String TAG = MyLocationListener.class.getSimpleName();
	CupsDemoApplication context;

	public MyLocationListener(CupsDemoApplication context)
	{
		this.context = context;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		context.setCurrentUserLocation(location);
		Log.d(TAG, "New location was set to currentUserLocation: "+location.toString());	
		context.stopListenToLocationUpdates();
		Log.d(TAG, "Removed Updates");
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

}
