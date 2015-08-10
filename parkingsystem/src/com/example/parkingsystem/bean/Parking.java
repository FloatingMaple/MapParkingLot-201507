/**
 * 
 */
package com.example.parkingsystem.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cerulean
 *
 */
public class Parking {
	
	private int status;
	private int total;
	private int size;
	private List content;
	
	private String title;
	private String address;
	private String tags;
	private String type;
	private int distance;
	private float parkingPrice;
	private Double[] location = new Double[2];
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public List getContent() {
		return content;
	}
	public void setContent(List content) {
		this.content = content;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public Double[] getLocation() {
		return location;
	}
	public void setLocation(Double[] location) {
		this.location = location;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public float getParkingPrice() {
		return parkingPrice;
	}
	public void setParkingPrice(float parkingPrice) {
		this.parkingPrice = parkingPrice;
	}
	
	
	
}
