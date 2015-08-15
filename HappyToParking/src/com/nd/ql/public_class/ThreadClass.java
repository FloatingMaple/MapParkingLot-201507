package com.nd.ql.public_class;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class ThreadClass extends Thread {
	private static final String Tag = "MyTag";
	Handler handler;
	String path;
	static public String result;

	public ThreadClass(Handler handler, String path) {
		this.handler = handler;
		this.path = path;
	}
    public String getResult() {
		return result;
	}
    public void setResult(String result) {
		this.result = result;
	}
	public void run() {
		try {
			URL url = new URL(path);
			HttpURLConnection coon = (HttpURLConnection) url.openConnection();
			coon.setReadTimeout(5000);
			coon.setRequestMethod("GET");
			int code = coon.getResponseCode();
			System.out.println("code:" + code);
			if (code == 200) {
				InputStream is = coon.getInputStream();
				result = PullResponseServletData.readInpuString(is);
				System.out.println(result);
				Log.v(Tag, result);
				Message msg = new Message();
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.v(Tag, "¡¨Ω” ß∞‹");
		};
	}

}
