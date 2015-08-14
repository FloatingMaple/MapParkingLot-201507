/**
 * 
 */
package com.example.parksyschange.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

/**
 * @author Cerulean
 *
 */
public class ParkData {

	private static final String SEARCH_URL_NEARBY = "http://api.map.baidu.com/geosearch/v3/nearby";
	private static final String SEARCH_URL_LOCAL = "http://api.map.baidu.com/geosearch/v3/local";
	private static final String API_KEY = "xnK3x2TNBw8sI4oVl6FN55RC";
	private static final String GEOTABLE_ID = "114798";
	private static final String MCODE = "0C:13:BB:0E:5B:77:43:AA:7B:28:EF:BC:AD:E9:73:B6:4A:D4:2A:A5;com.example.parksyschange";
	
	public static final int TIMEOUTMILL = 5000;
	public static final int RESPONSESUCCESS = 200;
	
	/**
	 * GET方式获取JSON数据
	 * @param type 查询类型 LOCAL周边检索输地名 NEARBY本地检索输坐标
	 * @param location nearby检索时的检索中心点坐标
	 * @param radius nearby检索时的检索半径 单位米
	 * @param region local检索时的检索地点 e.g. 北京市/福建师范大学/安徽省
	 * @param sortbyKey local/nearby检索时的排序依照
	 * @param sortbyType local/nearby检索时排序的方式 ASC升序/DESC降序
	 * @return jsonStr 返回JSON数据的String值
	 * @throws Exception 
	 */
	public static String getParkData(String type,String location,int radius,String region,String sortbyKey,String sortbyType) throws Exception{
		String path = "";
		if (type.equals("NEARBY")) {
			path = SEARCH_URL_NEARBY + "?ak=" + API_KEY + "&geotable_id="
					+ GEOTABLE_ID + "&mcode=" + MCODE + "&location=" + location + "&radius="
					+ radius + "&sortby=" + sortbyKey + ":";
			if(sortbyType.equals("ASC")){
				path = path + "1";
			}else if(sortbyType.equals("DESC")){
				path = path + "-1";
			}
		}else if(type.equals("LOCAL")){
			path = SEARCH_URL_LOCAL + "?ak=" + API_KEY + "&geotable_id="
					+ GEOTABLE_ID + "&mcode=" + MCODE + "&region=" + region + "&sortby=" 
					+ sortbyKey + ":";
			if(sortbyType.equals("ASC")){
				path = path + "1";
			}else if(sortbyType.equals("DESC")){
				path = path + "-1";
			}
		}else{
			return null;
		}
		
		Log.i("url", path);
		
		URL url = new URL(path);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(TIMEOUTMILL);
		connection.setRequestMethod("GET");
		
		if(connection.getResponseCode() == RESPONSESUCCESS){
			InputStream is = connection.getInputStream();
			byte[] data = readStream(is);
			String jsonStr = new String(data);
			return jsonStr;
		}else{
			return null;
		}
	}
	

	//解析JSON数据
	public void changeParkData(){
		
	}
	
	/**
	 * 把输入流转化成字符数组
	 * @param is 输入流
	 * @return 字符数组
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream is) throws Exception {
		ByteArrayOutputStream bOutputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = is.read(buffer)) != -1){
			bOutputStream.write(buffer,0,len);
		}
		
		bOutputStream.close();
		is.close();
		
		return bOutputStream.toByteArray();
	}
	
	
}
