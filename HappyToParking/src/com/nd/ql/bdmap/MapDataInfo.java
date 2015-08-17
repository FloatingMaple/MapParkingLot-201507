/**
 * 
 */
package com.nd.ql.bdmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cerulean
 *
 */
public class MapDataInfo implements Serializable{
	private double latitude;
	private double longitude;
	private String name;
	private String distance;
	private String address;
	private double parkingprice;
	private int lastparknum;
	
	public static List<MapDataInfo> infos = new ArrayList<MapDataInfo>();
	
	static{
		infos.add(new MapDataInfo(119.218677, 26.03178, "测试6", "475", "福建省福州市闽侯县致广南路", 4.8,20));
		infos.add(new MapDataInfo(119.225504, 26.026585000000001, "测试5", "523", "福建省福州市闽侯县旗山大道", 10,40));
		infos.add(new MapDataInfo(119.227804,26.022978,  "测试1", "0", "", 8.8, 3));
	}
	
	public MapDataInfo(double latitude, double longitude, String name,
			String distance,String address, double parkingprice,int lastparknum) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.distance = distance;
		this.address = address;
		this.parkingprice = parkingprice;
		this.lastparknum = lastparknum;
	}
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	public double getParkingprice() {
		return parkingprice;
	}
	public void setParkingprice(double parkingprice) {
		this.parkingprice = parkingprice;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getLastparknum() {
		return lastparknum;
	}
	public void setLastparknum(int lastparknum) {
		this.lastparknum = lastparknum;
	}
	
	
}
