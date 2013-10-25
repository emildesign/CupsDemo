package com.emildesign.cupsdemo.objects;

import java.util.ArrayList;
import java.util.List;

import com.emildesign.cupsdemo.utils.JSONParsing;
import com.google.gson.annotations.SerializedName;

public class CoffeeShop
{
	@SerializedName(JSONParsing.COFFEE_SHOP_NAME_ELEMENT)
	private String coffeeShopName;
	
	@SerializedName(JSONParsing.COFFEE_SHOP_ADDRESS_ELEMENT)
    private String coffeeShopAddress;
	
	@SerializedName(JSONParsing.COFFEE_SHOP_LAT_ELEMENT)
    private String coffeeShopLat;
	
	@SerializedName(JSONParsing.COFFEE_SHOP_LNG_ELEMENT)
    private String coffeeShopLng;
	
	@SerializedName(JSONParsing.COFFEE_SHOP_LOGO_ELEMENT)
    private String coffeeShopLogo;
	
	private float coffeeShopDistance;

	public String getCoffeeShopName() {
		return coffeeShopName;
	}

	public void setCoffeeShopName(String coffeeShopName) {
		this.coffeeShopName = coffeeShopName;
	}

	public String getCoffeeShopAddress() {
		return coffeeShopAddress;
	}

	public void setCoffeeShopAddress(String coffeeShopAddress) {
		this.coffeeShopAddress = coffeeShopAddress;
	}

	public String getCoffeeShopLat() {
		return coffeeShopLat;
	}

	public void setCoffeeShopLat(String coffeeShopLat) {
		this.coffeeShopLat = coffeeShopLat;
	}

	public String getCoffeeShopLng() {
		return coffeeShopLng;
	}

	public void setCoffeeShopLng(String coffeeShopLng) {
		this.coffeeShopLng = coffeeShopLng;
	}

	public String getCoffeeShopLogo() {
		return coffeeShopLogo;
	}

	public void setCoffeeShopLogo(String coffeeShopLogo) {
		this.coffeeShopLogo = coffeeShopLogo;
	}

	public float getCoffeeShopDistance() {
		return coffeeShopDistance;
	}

	public void setCoffeeShopDistance(float coffeeShopDistance) {
		this.coffeeShopDistance = coffeeShopDistance;
	}

	@Override
	public String toString() {
		return "CoffeeShop [coffeeShopName=" + coffeeShopName
				+ ", coffeeShopAddress=" + coffeeShopAddress
				+ ", coffeeShopLat=" + coffeeShopLat + ", coffeeShopLng="
				+ coffeeShopLng + ", coffeeShopLogo=" + coffeeShopLogo + "]";
	}
}
