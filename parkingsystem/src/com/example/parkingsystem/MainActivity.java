package com.example.parkingsystem;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

private MapView mMapView;
private BaiduMap baiduMap;

//��λ���
private LocationClient mLocationClient;
private MyLocationListener mLocationListener;
private boolean isFirstIn = true;
private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		this.context = this;
		initview();
		
		//��ʼ����λ
		initlocation();
	}


	private void initview() {
		mMapView = (MapView) findViewById(R.id.bmapview);
		baiduMap = mMapView.getMap();
		//��ͨģʽ��ͼ
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		//���ó�ʼ�ı�����ԼΪ500m
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		baiduMap.setMapStatus(msu);
	}
	
	private void initlocation() {
		mLocationClient = new LocationClient(this);
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);
		
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");	//��������
		option.setIsNeedAddress(true);	//������Ҫ��ַ
		option.setOpenGps(true);		//ʹ��GPS
		option.setScanSpan(1000);		//��λ������
	
		/*  ���ڶ�λģʽ�뾫��:
		option.setLocationMode(LocationMode.Hight_Accuracy);
		Ĭ�ϸ߾���
		Hight_Accuracy �߾��ȶ�λģʽ�����ֶ�λģʽ�£���ͬʱʹ�����綨λ��GPS��λ�����ȷ�����߾��ȵĶ�λ�����
		Battery_Saving �͹��Ķ�λģʽ�����ֶ�λģʽ�£�����ʹ��GPS��ֻ��ʹ�����綨λ��Wi-Fi�ͻ�վ��λ����
		Device_Sensors �����豸��λģʽ�����ֶ�λģʽ�£�����Ҫ�������磬ֻʹ��GPS���ж�λ������ģʽ�²�֧�����ڻ����Ķ�λ��
		*/
		mLocationClient.setLocOption(option);
	}

	private class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			MyLocationData data = new MyLocationData.Builder()//
					.accuracy(location.getRadius())//
					.latitude(location.getLatitude())//
					.longitude(location.getLongitude())//
					.build();
			baiduMap.setMyLocationData(data);
			
			//����ǵ�һ��ʹ�ã����ж�λ
			if (isFirstIn) {
				LatLng latLng = new LatLng(location.getLatitude(), 
						location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				baiduMap.animateMapStatus(msu);
				isFirstIn = false;
				
				Toast.makeText(context, location.getAddrStr(),Toast.LENGTH_SHORT).show();
			}
		}	
	}
	
	@Override
		protected void onStart() {
			super.onStart();
			baiduMap.setMyLocationEnabled(true);
			mLocationClient.start();
		}
	
	@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			mMapView.onResume();
		}
	
	@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			mMapView.onPause();
		}
	
	@Override
		protected void onStop() {
			super.onStop();
			baiduMap.setMyLocationEnabled(false);
			mLocationClient.stop();
		}
	
	@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			mMapView.onDestroy();
			mMapView = null;
			super.onDestroy();
		}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
