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
		//��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext  
        //ע��÷���Ҫ��setContentView����֮ǰʵ��  
        SDKInitializer.initialize(getApplicationContext());  
		setContentView(R.layout.activity_main);
		
		
		/*************************************************************************/
		//2015��8��14��09:52:52 ����GET��ȡJSON����
		new Thread(){
			public void run() {
				try {
					String testStr = ParkData.getParkData("LOCAL","" ,0 , "����ʦ����ѧ��ɽУ��", "parkingPrice", "ASC");
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
