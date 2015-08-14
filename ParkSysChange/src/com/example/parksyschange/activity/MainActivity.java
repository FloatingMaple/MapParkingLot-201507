package com.example.parksyschange.activity;

import com.baidu.mapapi.SDKInitializer;
import com.example.parksyschange.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.parksyschange.utils.ParkData;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getApplicationContext());  
		setContentView(R.layout.activity_main);
		
		
		/*************************************************************************/
		//2015年8月14日09:52:52 测试GET获取JSON数据
		new Thread(){
			public void run() {
				try {
					String testStr = ParkData.getParkData("LOCAL","" ,0 , "福建师范大学旗山校区", "parkingPrice", "ASC");
					System.out.println(testStr);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("error!");
				}	
			};
		}.start();
		
		/*************************************************************************/
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
