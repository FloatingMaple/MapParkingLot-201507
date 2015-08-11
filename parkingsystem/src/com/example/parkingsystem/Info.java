/**
 * 
 */
package com.example.parkingsystem;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.text.StaticLayout;

/**
 * @author Cerulean
 *
 */
public class Info implements Serializable{

	private double latitude;
	private double longitude;
	private String title;
	private String distance;
	private String parkingPrice;
	private String tags;

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getParkingPrice() {
		return parkingPrice;
	}

	public void setParkingPrice(String parkingPrice) {
		this.parkingPrice = parkingPrice;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
	
	
}
