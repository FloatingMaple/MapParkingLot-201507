package com.example.parkingsystem;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.security.auth.PrivateCredentialPermission;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

private MapView mMapView;
private BaiduMap baiduMap;
private Handler handler;

private LocationClient mLocationClient;
private MyLocationListener mLocationListener;
private boolean isFirstIn = true;
private Context context;
private double mLatitude;
private double mLongitude;

private BitmapDescriptor mMarker;

private static String PATH = "http://api.map.baidu.com/geosearch/v3/nearby?";
private static String AK = "hTOEZILzGv4YmsrlZek5AZkA";
private static String GEOTABLE_ID = "114798";
private static String LOCATION = "";
private static String MCODE = 
	"5B:D0:78:FA:FE:7E:F3:53:45:1D:2B:32:4D:43:79:FB:96:88:BF:3F;"
	+ "com.example.parkingsystem";
private static String PATH1 = "";

private BitmapDescriptor mIconLocation;

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
	
	/**
	 * 
	 */
	private void initview() {
		mMapView = (MapView) findViewById(R.id.bmapview);
		baiduMap = mMapView.getMap();
		
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		//设置初始比例尺为500m
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		baiduMap.setMapStatus(msu);
	}
	
	private void initlocation() {
		mLocationClient = new LocationClient(this);
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);
		
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");	//bd09ll
		option.setIsNeedAddress(true);	//允许获取地址
		option.setOpenGps(true);		//开启GPS
		option.setScanSpan(1000);		//定位间隔时间1s
		mLocationClient.setLocOption(option);
		
		mIconLocation = BitmapDescriptorFactory
				.fromResource(R.drawable.navi_map_gps_locked);
			
	}
	
	public void mylocation_click(View view){
		local_to_mLocation();
	}

	public void show_overlays(View view){
		
		//获取数据的链接地址
		PATH1 = PATH + 
				"ak=" + AK + 
				"&geotable_id=" + GEOTABLE_ID +
				"&location=" + LOCATION + 
				"&mcode=" + MCODE +
				"&radius=" + "5000"		//搜索半径5000m
				;
		//测试连接地址
		Log.i("abc", PATH1);
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				Log.i("handler", "receive msg,read msg. msg:"+msg);
				
				//以覆盖物的形式显示
				baiduMap.clear();
				
				mMarker = BitmapDescriptorFactory.fromResource(R.drawable.maker);
				LatLng latLng = null;
				Marker marker = null;
				
				List<Map<String, String>> list1 = 
						(List<Map<String, String>>) msg.obj;
				
				Log.i("list1", "list1.length:" + list1.size());
				
				OverlayOptions options;
				for(Map<String, String> list2 : list1){
					String title = list2.get("title");
					String distance = list2.get("distance");
					String price = list2.get("price");
					String tag = list2.get("tag");
					String address = list2.get("address");
					String location = list2.get("location");
					String latitude = list2.get("latitude");
					String longitude = list2.get("longitude");
					
					Log.i("list", "LIST2 中的测试数据  :  title:" + title + " | distance:" + distance + " | price:"
							+ price + " | tag:" + tag + " | location:" + location + " | address:"
							+ address + " | latitude:" + latitude + " | longitude:" + longitude); 
					
					latLng = new LatLng(Double.valueOf(longitude),Double.valueOf(latitude));
					Log.i("list", "latitude:"+Double.valueOf(latitude)+"longitude"+Double.valueOf(longitude));
					options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
					marker = (Marker) baiduMap.addOverlay(options);
//					Bundle arg0 = new Bundle();
//					arg0.putSerializable("list", (Serializable) list2);
//					marker.setExtraInfo(arg0);
					Log.i("list", "overlay finish");
				}
				
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				baiduMap.setMapStatus(msu);
			}
		};
		
		

		//读取数据
		new Thread(){
			public void run() {
				List<Map<String, String>> list = new ArrayList<Map<String,String>>();
				Log.i("abc", "test2");			
				try {
					list = JSON.getJSONObject(PATH1);
					Log.i("json", "json finish. list:"+list);
					
					Message message = Message.obtain();
					message.obj = list;
					handler.sendMessage(message);
					
					Log.i("handler", "send a message to main thread");
					
				} catch (Exception e) {
					Log.i("abc", "数据存在异常");
					e.printStackTrace();
				}
				Log.i("abc", "list.size:"+list.size());				
			};
		}.start();
		
		Log.i("abc", "test3");			
		

		
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
			
			//设置自定义图标
			MyLocationConfiguration configuration = new MyLocationConfiguration(
					com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL, true, mIconLocation);
			baiduMap.setMyLocationConfigeration(configuration); 
			
			//更新位置
			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
			
			//把定位所得的位置存储到LOCATION
			LOCATION = Double.toString(mLongitude) + "," + Double.toString(mLatitude);
			
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
	
	
	//定位到我的位置
	private void local_to_mLocation(){
		LatLng latLng = new LatLng(mLatitude, mLongitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		baiduMap.animateMapStatus(msu);
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
		switch(id){
		case R.id.id_map_traffic:
			if (baiduMap.isTrafficEnabled()) {
				baiduMap.setTrafficEnabled(false);
				item.setTitle("实时交通(off)");
			}else {
				baiduMap.setTrafficEnabled(true);
				item.setTitle("实时交通(on)");
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
