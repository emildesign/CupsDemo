package com.emildesign.cupsdemo.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.emildesign.cupsdemo.objects.CoffeeShop;
import com.google.gson.Gson;

import android.util.Log;

public class JSONParsing 
{
	public static final String TAG = JSONParsing.class.getSimpleName();
	
	//static final Strings for parsed elements
	//================================================================
	
	//Coffee Shop elements
	public static final String COFFEE_SHOP_NAME_ELEMENT = "name";
	public static final String COFFEE_SHOP_ADDRESS_ELEMENT = "address";
	public static final String COFFEE_SHOP_LAT_ELEMENT = "lat";
	public static final String COFFEE_SHOP_LNG_ELEMENT = "lng";
	public static final String COFFEE_SHOP_LOGO_ELEMENT = "logo";
	
	//Native method for parsing json file
	public static List<CoffeeShop> NativeJsonParseMethodForCoffeeShops(String jsonString) 
	{
		try
		{
			//Creating list object for storing the parsed shops
			List<CoffeeShop> coffeeShopsList = new ArrayList<CoffeeShop>();
			
			//Creating JSON object from string
			JSONObject json = new JSONObject(jsonString);

			JSONArray shopsIDArray = json.names();

			//Creating empty json object for Shops loop
			JSONObject currentCoffeeShop;
								
			//looping all the Shops and adding them parsed one by one to the list
			for (int i = 0 ; i < shopsIDArray.length() ; i ++)
			{
				currentCoffeeShop = json.getJSONObject(shopsIDArray.get(i).toString());
				addCurrentCoffeeShopToCoffeeShopsListUsingGson(currentCoffeeShop, coffeeShopsList);
			}
				
			//Returning parsed students list
			return coffeeShopsList;	
		} 
		catch (Exception e) 
		{
		   	Log.e("EXC", "Error", e);
		   	return null;
		}
	}

	private static void addCurrentCoffeeShopToCoffeeShopsListUsingGson(JSONObject currentCoffeeShop, List<CoffeeShop> coffeeShopList) 
	{
		Gson gson = new Gson();
		CoffeeShop coffeeShop = gson.fromJson(currentCoffeeShop.toString(), CoffeeShop.class);
		coffeeShopList.add(coffeeShop);
	}
}
