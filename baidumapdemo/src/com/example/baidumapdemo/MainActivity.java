package com.example.baidumapdemo;

import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.ui.routeguide.subview.r;
import com.example.baidumapdemo.MyOrientationListener.OnOrientationListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
MapView mapView = null;
private BaiduMap mBaiduMap;

//定位相关
private LocationClient mLocationClient;
private MyLocationListener mLocationListener;
private boolean isFirstIn = true;
private Context context;
private double mLatitude;
private double mLongitude;

//自定义定位图标
private BitmapDescriptor mIconLocation;

private MyOrientationListener myOrientationListener;
private float mCurrentX;

private com.baidu.mapapi.map.MyLocationConfiguration.LocationMode mLocationMode;


//覆盖物相关
private BitmapDescriptor mMarker;
private RelativeLayout mMarkerLy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.activity_main);
		this.context = this;
		mapView = (MapView) findViewById(R.id.bmapView);
		//设置初始的地图比例  500米
		mBaiduMap = mapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
		//初始化定位
		initLocation();
		
		initMarker();
		
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				Bundle extraInfo = marker.getExtraInfo();
				Info info = (Info) extraInfo.getSerializable("info");
				ImageView iv = (ImageView) mMarkerLy.findViewById(R.id.id_info_img);
				TextView distance = (TextView) mMarkerLy.findViewById(R.id.id_info_distance);
				TextView name = (TextView) mMarkerLy.findViewById(R.id.id_info_name);
				TextView zan = (TextView) mMarkerLy.findViewById(R.id.id_info_zan);
				
				iv.setImageResource(info.getImgId());
				distance.setText(info.getDistance());
				name.setText(info.getName());
				zan.setText(info.getZan() + "");
				
				mMarkerLy.setVisibility(View.VISIBLE);
				return true;
			}
		});
		
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
				mMarkerLy.setVisibility(View.GONE);
			}
		});
	}
	
	private void initMarker() {
		// TODO Auto-generated method stub
		mMarker = BitmapDescriptorFactory
				.fromResource(R.drawable.maker);
		mMarkerLy = (RelativeLayout) findViewById(R.id.id_maker_ly);
	}

	private void initLocation() {
		
		mLocationMode = com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL;
		mLocationClient = new LocationClient(this);
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);
		
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);
		
		mLocationClient.setLocOption(option);
		//初始化图标 	
		mIconLocation = BitmapDescriptorFactory
				.fromResource(R.drawable.navi_map_gps_locked);
		
		myOrientationListener = new MyOrientationListener(context);
		
		myOrientationListener.setOnOrientationListener(new OnOrientationListener() {
			
			@Override
			public void onOrientationChanged(float x) {
				mCurrentX = x;
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.onDestroy();
	}
	
	@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			mapView.onResume();
		}

	@Override
		protected void onStart() {
			super.onStart();
			mBaiduMap.setMyLocationEnabled(true);
			mLocationClient.start();
			myOrientationListener.start();
		}
	
	@Override
		protected void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			mapView.onPause();
		}
	
	@Override
		protected void onStop() {
			super.onStop();
			mBaiduMap.setMyLocationEnabled(false);
			mLocationClient.stop();
			myOrientationListener.stop();
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.id_map_common:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			break;
		case R.id.id_map_site:
			mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			break;
		case R.id.id_map_traffic:
			if (mBaiduMap.isTrafficEnabled()) {
				mBaiduMap.setTrafficEnabled(false);
				item.setTitle("实时交通(off)");
			}else {
				mBaiduMap.setTrafficEnabled(true);
				item.setTitle("实时交通(on)");
			}
			break;
		case R.id.id_map_location:
			centerToMylocation();
			break;
		case R.id.id_map_mode_common:
			mLocationMode = com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL;
			break;
		case R.id.id_map_mode_following:
			mLocationMode = com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.FOLLOWING;
			break;
		case R.id.id_map_mode_compass:
			mLocationMode = com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.COMPASS;
			break;
		case R.id.id_map_add_overlay:
			addOverlays(Info.infos);
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void addOverlays(List<Info> infos) {
		// 添加覆盖物
		mBaiduMap.clear();
		LatLng latLng = null;
		Marker marker = null;
		OverlayOptions options;
		for (Info info:infos) {
			//经纬度
			latLng = new LatLng(info.getLatitude(),info.getLongitude());
			//图标
			options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
			marker = (Marker) mBaiduMap.addOverlay(options);
			Bundle bundle = new Bundle();
			bundle.putSerializable("info", info);
			marker.setExtraInfo(bundle);
		}
		
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.setMapStatus(msu);
	}

	/**
	 * 定位到我的位置
	 */
	private void centerToMylocation() {
		LatLng latLng = new LatLng(mLatitude, mLongitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
	}
	
	private class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			MyLocationData data = new MyLocationData.Builder()//
				.direction(mCurrentX)//更新方向
				.accuracy(location.getRadius())//
				.latitude(location.getLatitude())//
				.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(data);
			//设置自定义图标
			MyLocationConfiguration config = new 
					MyLocationConfiguration(mLocationMode, true, mIconLocation);
			mBaiduMap.setMyLocationConfigeration(config);
			//更新经纬度			
			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
			
			
			if (isFirstIn) {
				LatLng latLng = new LatLng(location.getLatitude(), 
						location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				isFirstIn = false;
				
				Toast.makeText(context, location.getAddrStr(),
						Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
}
