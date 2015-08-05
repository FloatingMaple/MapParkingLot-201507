/**
 * 
 */
package com.example.parkingsystem;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.media.TimedText;
import android.util.Log;

/**
 * @author Cerulean
 *
 */
public class JSON {
	public static List<Map<String, String>> getJSONObject(String path) throws Exception{
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		Map<String, String> map = null;
		URL url = new URL(path);
		//获取网页数据
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		//超时时间5s
		conn.setConnectTimeout(5000);
		//GET方式
		conn.setRequestMethod("GET");
		
		if(conn.getResponseCode() == 200){	//200  成功
			//获取输入流
			InputStream is = conn.getInputStream();
			//输入流转化成字符数组
			byte[] data = readStream(is);
			//字符数组转化成字符串
			String json = new String(data);
			
			
			JSONObject jsonObject = new JSONObject(json);
			int total = jsonObject.getInt("total");
			//保存status编号
			int status = jsonObject.getInt("status");
			//判断是否成功
			Boolean success = (jsonObject.getInt("status") == 0)?true:false;
			//测试数据 
			Log.i("abc", 
					"total:" + total + " | success:" + success + " | status:" + status);  
			
			//获取数组数据
			JSONArray jsonArray = jsonObject.getJSONArray("contents");
			for(int i = 0;i<jsonArray.length();i++){
				//得到数据
				JSONObject item = jsonArray.getJSONObject(i);
				String title = item.optString("title");
				float distance = (float) item.optDouble("distance");
				float price = (float) item.optDouble("parkingPrice");
				String tag= item.optString("tags");
				String address = item.optString("address");
				JSONArray location = item.optJSONArray("location");
				
				//存放到map中
				map = new HashMap<String,String>();
				map.put("title", title);				
				map.put("distance", Float.toString(distance));					
				map.put("price", Float.toString(price));
				map.put("tag", tag);
				map.put("address", address);
				map.put("location", location.toString());
				list.add(map);
			}
		}
		
		//测试数据
		for(Map<String, String> list2 : list){
			String title = list2.get("title");
			String distance = list2.get("distance");
			String price = list2.get("price");
			String tag = list2.get("tag");
			String address = list2.get("address");
			String location = list2.get("location");
			
			Log.i("abc", "title:" + title + " | distance:" + distance + " | price:"
					+ price + " | tag:" + tag + " | location:" + location + " | address:" + address); 
		}
		
		
		return list;
	}
	
	
	/**
	 * 把输入流转化成字符数组
	 * @param inputStream 输入流
	 * @return 字符数组
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inputStream) throws Exception{
		ByteArrayOutputStream bOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			bOutputStream.write(buffer,0,len);
		}
		
		bOutputStream.close();
		inputStream.close();
		
		return bOutputStream.toByteArray();
	}
}
