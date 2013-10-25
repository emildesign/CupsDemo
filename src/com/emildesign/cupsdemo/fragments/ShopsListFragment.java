package com.emildesign.cupsdemo.fragments;

import java.util.ArrayList;
import com.emildesign.cupsdemo.CupsDemoApplication;
import com.emildesign.cupsdemo.R;
import com.emildesign.cupsdemo.adapters.CoffeeShopsListAdapter;
import com.emildesign.cupsdemo.objects.CoffeeShop;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShopsListFragment extends Fragment 
{
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_QUESTION_NUMBER = "section_number";
	private CoffeeShopsListAdapter coffeeShopsListAdapter;
	ArrayList<CoffeeShop> coffeeShopsList;
	ListView lvCoffeeShopsList;
	
	public ShopsListFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView = inflater.inflate(R.layout.fragment_coffee_shops_list, container, false);
		
		lvCoffeeShopsList = (ListView) rootView.findViewById(R.id.lvCoffeeShopsList);
	
		coffeeShopsList = (ArrayList<CoffeeShop>) ((CupsDemoApplication)getActivity().getApplication()).getCoffeeShopsList();

		coffeeShopsListAdapter = new CoffeeShopsListAdapter(getActivity(), R.id.tvShopName, coffeeShopsList);
		lvCoffeeShopsList.setAdapter(coffeeShopsListAdapter);

		return rootView;
	}
}