package com.example.parkingsystem;

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
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;

import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

private MapView mMapView;
private BaiduMap baiduMap;


private LocationClient mLocationClient;
private MyLocationListener mLocationListener;
private boolean isFirstIn = true;
private Context context;
private double mLatitude;
private double mLongitude;

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
		
		PATH1 = PATH + 
				"ak=" + AK + 
				"&geotable_id=" + GEOTABLE_ID +
				"&location=" + LOCATION + 
				"&mcode=" + MCODE +
				"&radius=" + "5000"		//搜索半径5000m
				;

		Log.i("abc", PATH1);
		
//		Looper.prepare();
		new Thread(){
			public void run() {
				try {
					JSON.getJSONObject(PATH1);
				} catch (Exception e) {
					Log.i("abc", "数据存在异常");
					e.printStackTrace();
//					Toast.makeText(context, "数据存在异常", Toast.LENGTH_SHORT).show()
				}				
			};
		}.start();
//		Looper.loop();
		
	}
	

	
//	public void onGetSearchResult(CloudSearchResult result,int type,int iError) {
//		if(result != null && result.poiList != null 
//				&& result.poiList.size()>0 ){
//			LocalSearchInfo info = new LocalSearchInfo();
//			info.ak = "hTOEZILzGv4YmsrlZek5AZkA";
//			info.geoTableId = 114798;
//			info.tags = "";		//查询标签
//			info.q = "";		//q 检索关键字
//			info.region = "";	//检索区域的名称  e.g.市/区 默认全国范围
//			CloudManager.getInstance().localSearch(info);
//		}
//	}
	
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
