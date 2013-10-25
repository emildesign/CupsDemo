package com.emildesign.cupsdemo.activities;

import java.util.ArrayList;
import java.util.Locale;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import com.emildesign.cupsdemo.R;
import com.emildesign.cupsdemo.adapters.CoffeeShopsListAdapter.OnCoffeeShopSelectedListener;
import com.emildesign.cupsdemo.fragments.MyMapFragment;
import com.emildesign.cupsdemo.fragments.ShopsListFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, OnCoffeeShopSelectedListener
{
	private static final String TAG = MainActivity.class.getSimpleName();
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private static final LatLng TELAVIV = new LatLng(32.073229,34.773231);
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;	
	private ActionBar actionBar;
	private GoogleMap map;
	private boolean currentShowingAllMap = true;
	private Polyline newPolyline;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		// Set up the action bar.
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		initComponents();
	}

	public void initComponents()
	{
		setContentView(R.layout.activity_main);
    
		// Create the adapter that will return a fragment for each of the 2 primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
						moveCameraToTelAviv();
					}
				});
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment;
			Bundle args = new Bundle();
			args.putInt(ShopsListFragment.ARG_QUESTION_NUMBER, position);
			switch (position) {
			case 0:
				fragment = new ShopsListFragment();
				fragment.setArguments(args);
				return fragment;
			case 1:
				fragment = new MyMapFragment();
				fragment.setArguments(args);
				return fragment;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.coffee_shops_section).toUpperCase(l);
			case 1:
				return getString(R.string.map_section).toUpperCase(l);
			}
			return null;
		}
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
		moveCameraToTelAviv();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	}
	
	public ViewPager getViewPager()
	{
		return mViewPager;
	}
	
	public void setMap(GoogleMap map)
	{
		this.map = map;
	}

	@Override
	public void onCoffeeShopSelected(LatLng latlng) 
	{
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14));
		map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);	
		currentShowingAllMap = false;
		if (newPolyline != null)
		{
			newPolyline.remove();
		}
	}
	
	public void moveCameraToTelAviv() 
	{
		if (map != null && !currentShowingAllMap)
		{
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(TELAVIV, 12));
			map.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
			currentShowingAllMap = true;
			if (newPolyline != null)
			{
				newPolyline.remove();
			}
		}
	}
	
	public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints)
	{
		PolylineOptions rectLine = new PolylineOptions().width(3).color(getResources().getColor(R.color.cups_color));

		for(int i = 0 ; i < directionPoints.size() ; i++) 
		{          
			rectLine.add(directionPoints.get(i));
		}
		if (newPolyline != null)
		{
			newPolyline.remove();
		}
		newPolyline = map.addPolyline(rectLine);
	}

	@Override
	public void onBackPressed() {
		if (mViewPager.getCurrentItem() == 1)
		{
			mViewPager.setCurrentItem(0);
		}
		else
		{
			super.onBackPressed();
		}
	}
	
	
}
