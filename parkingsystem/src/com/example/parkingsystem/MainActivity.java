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

//定位相关
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
		
		//实现定位
		initlocation();
	}


	private void initview() {
		mMapView = (MapView) findViewById(R.id.bmapview);
		baiduMap = mMapView.getMap();
		//普通模式地图
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		//设置默认比例尺500m
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		baiduMap.setMapStatus(msu);
	}
	
	private void initlocation() {
		mLocationClient = new LocationClient(this);
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);
		
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");	//bd09ll类型坐标
		option.setIsNeedAddress(true);	//获取地址
		option.setOpenGps(true);		//开启GPS
		option.setScanSpan(1000);		//间隔1s
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
			
			//第一次进入
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
