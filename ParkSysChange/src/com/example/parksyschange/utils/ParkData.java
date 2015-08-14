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
	 * GET��ʽ��ȡJSON����
	 * @param type ��ѯ���� LOCAL�ܱ߼�������� NEARBY���ؼ���������
	 * @param location nearby����ʱ�ļ������ĵ�����
	 * @param radius nearby����ʱ�ļ����뾶 ��λ��
	 * @param region local����ʱ�ļ����ص� e.g. ������/����ʦ����ѧ/����ʡ
	 * @param sortbyKey local/nearby����ʱ����������
	 * @param sortbyType local/nearby����ʱ����ķ�ʽ ASC����/DESC����
	 * @return jsonStr ����JSON���ݵ�Stringֵ
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
	

	//����JSON����
	public void changeParkData(){
		
	}
	
	/**
	 * ��������ת�����ַ�����
	 * @param is ������
	 * @return �ַ�����
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
