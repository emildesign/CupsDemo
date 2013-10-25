package com.emildesign.cupsdemo.adapters;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.emildesign.cupsdemo.R;
import com.emildesign.cupsdemo.activities.MainActivity;
import com.emildesign.cupsdemo.objects.CoffeeShop;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CoffeeShopsListAdapter extends ArrayAdapter<CoffeeShop>
{	
	private static final String TAG = CoffeeShopsListAdapter.class.getSimpleName();
	private ArrayList<CoffeeShop> coffeeShopsList;
	private Context context;
	private MainActivity activity;
    OnCoffeeShopSelectedListener mCallback;

	public CoffeeShopsListAdapter(Context context, int textViewResourceId, ArrayList<CoffeeShop> coffeeShopsList) 
	{
		super(context, textViewResourceId,coffeeShopsList);
		Log.d(TAG, "CoffeeShopsListAdapter called");
		 this.coffeeShopsList = new ArrayList<CoffeeShop>();
		 this.coffeeShopsList.addAll(coffeeShopsList);
		 this.context = context;
		 this.activity = (MainActivity)context;
		 
	     try {
	            mCallback = (OnCoffeeShopSelectedListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement OnHeadlineSelectedListener");
	        }
		 Log.d(TAG, "coffeeShopsList: "+ coffeeShopsList.size());
		 
	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
    	ViewHolder holder = new ViewHolder();
	    
    	LayoutInflater inflator = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = inflator.inflate(R.layout.listview_item_layout, null);
	    
        holder.name = (TextView) convertView.findViewById(R.id.tvShopName);
        holder.address = (TextView) convertView.findViewById(R.id.tvShopAddress);
        holder.distance = (TextView) convertView.findViewById(R.id.tvDistance);
        holder.layout = (RelativeLayout) convertView.findViewById(R.id.rlShopInfoLayout);

		holder.name.setText(coffeeShopsList.get(position).getCoffeeShopName());
		holder.address.setText(coffeeShopsList.get(position).getCoffeeShopAddress());
		holder.distance.setText(new DecimalFormat("##.##").format(coffeeShopsList.get(position).getCoffeeShopDistance()) + "km");
		holder.layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				activity.getViewPager().setCurrentItem(2, true);
				mCallback.onCoffeeShopSelected(new LatLng(Double.parseDouble(coffeeShopsList.get(position).getCoffeeShopLat()), Double.parseDouble(coffeeShopsList.get(position).getCoffeeShopLng())));
			}
		});
		
		return convertView;
	}
	
	static class ViewHolder 
	{
		 TextView name;
		 TextView address;
		 TextView distance;
		 RelativeLayout layout;
	}

	// Container Activity must implement this interface
	public interface OnCoffeeShopSelectedListener {
	   public void  onCoffeeShopSelected(LatLng latlng);
	}	
}