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
		infos.add(new MapDataInfo(119.218677, 26.03178, "²âÊÔ6", "475", "¸£½¨Ê¡¸£ÖİÊĞÃöºîÏØÖÂ¹ãÄÏÂ·", 4.8,20));
		infos.add(new MapDataInfo(119.225504, 26.026585000000001, "²âÊÔ5", "523", "¸£½¨Ê¡¸£ÖİÊĞÃöºîÏØÆìÉ½´óµÀ", 10,40));
		infos.add(new MapDataInfo(119.227804, 26.022978,  "²âÊÔ1", "0", "", 8.8, 3));
		infos.add(new MapDataInfo(119.280229, 26.056106 , "³¬Ô¶¾àÀë²âÊÔ", "0", "", 12, 100));
	}
	
	public static List<MapDataInfo> infos2 = new ArrayList<MapDataInfo>();
	
	static{
		infos2.add(new MapDataInfo(119.218377, 26.03178, "ËÑË÷²âÊÔ1", "475", "", 4.8,20));
		infos2.add(new MapDataInfo(119.225004, 26.026585, "ËÑË÷²âÊÔ2", "523", "", 10,40));
		infos2.add(new MapDataInfo(119.227104, 26.022978,  "ËÑË÷²âÊÔ3", "0", "", 8.8, 3));
		infos2.add(new MapDataInfo(119.280229, 26.050106 , "ËÑË÷²âÊÔ4", "0", "", 12, 100));
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
