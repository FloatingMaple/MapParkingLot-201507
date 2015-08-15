package com.nd.ql.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.ui.routeguide.subview.r;
import com.nd.ql.bdmap.MyOrientationListener;
import com.nd.ql.bdmap.MyOrientationListener.OnOrientationListener;
import com.nd.ql.login.R;
import com.nd.ql.public_class.ThreadClass;
import com.nd.ql.public_class.GetNetConnection;
import com.nd.ql.public_class.PublicPath;
import com.nd.ql.public_class.PullResponseServletData;

public class MapFragment extends Fragment {
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    private BitmapDescriptor mIconLocation;
    private boolean isFirstIn = true;
    private double mLatitude;
    private double mLongitude;
    private BaiduMap baiduMap;
    MapView mMapView = null;
    private static String LOCATION = "";
    private MyOrientationListener myOrientationListener;
    private float mCurrentX;
    
	protected static final String Tag = "MyTag";
	View mView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstancemap) {
		SDKInitializer.initialize(getActivity().getApplicationContext());
		super.onCreateView(inflater, container, savedInstancemap);
		
		View mapLayout = inflater.inflate(R.layout.home_map_tag, container,
				false);
		
		mMapView = (MapView) mapLayout.findViewById(R.id.bmapview);
		
		initview();
		//ʵ�ֶ�λ
		initlocation();
		
		return mapLayout;
	}
	

	private void initview() {
		baiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		baiduMap.setMapStatus(msu);
	}


	private void initlocation() {
		mLocationClient = new LocationClient(getActivity());
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);
		
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");	//bd09ll
		option.setIsNeedAddress(true);	//�����ȡ��ַ
		option.setOpenGps(true);		//����GPS
		option.setScanSpan(1000);		//��λ���ʱ��1s
		mLocationClient.setLocOption(option);
		
		mIconLocation = BitmapDescriptorFactory
				.fromResource(R.drawable.navi_map_gps_locked);
		
		myOrientationListener = new MyOrientationListener(this.getActivity().getApplicationContext());
		
		myOrientationListener.setOnOrientationListener(new OnOrientationListener() {
			
			@Override
			public void onOrientationChanged(float x) {
				mCurrentX = x;
			}
		});
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ImageButton imageButton = (ImageButton) getActivity().findViewById(R.id.ib_mylocation);
		imageButton.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				local_to_mLocation();
			}
		});
	}
	
	//��λ���ҵ�λ��
	private void local_to_mLocation(){
		LatLng latLng = new LatLng(mLatitude, mLongitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		baiduMap.animateMapStatus(msu);
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
			
			//�����Զ���ͼ��
			MyLocationConfiguration configuration = new MyLocationConfiguration(
					com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL, true, mIconLocation);
			baiduMap.setMyLocationConfigeration(configuration); 
			
			//����λ��
			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
			
			//�Ѷ�λ���õ�λ�ô洢��LOCATION
			LOCATION = Double.toString(mLongitude) + "," + Double.toString(mLatitude);
			
			//��һ�ν���
			if (isFirstIn) {
				LatLng latLng = new LatLng(location.getLatitude(), 
						location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				baiduMap.animateMapStatus(msu);
				isFirstIn = false;
				
				Toast.makeText(getActivity().getApplicationContext(), location.getAddrStr(),Toast.LENGTH_SHORT).show();
			}
		}	
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();
		baiduMap.setMyLocationEnabled(true);
		if(!mLocationClient.isStarted())
			mLocationClient.start();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		myOrientationListener.start();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		myOrientationListener.stop();
	}

}
