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
public class Infos implements Serializable{

	private double latitude;
	private double longitude;
	private String name;
	private String distance;
	private String price;
	private String tag;
	
	public void setInfo(List<Map<String, String>> info){
		List<Infos> infos = new ArrayList<Infos>();
//		Array latlng = info.getArray("location");
//		infos.add(new Infos(info.))
	}
	
	
}
