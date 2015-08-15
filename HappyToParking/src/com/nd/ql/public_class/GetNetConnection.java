package com.nd.ql.public_class;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetNetConnection {
	public static InputStream getConnection(String path) {
		try {
			HttpURLConnection connection;
			int code;
			URL url = new URL(path);
			connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			code = connection.getResponseCode();
			if (code == 200) {
				InputStream is = connection.getInputStream();
				return is;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
