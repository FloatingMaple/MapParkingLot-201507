package com.nd.ql.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Geocoder;
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
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;

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
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.map.Text;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption.DrivingPolicy;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.navisdk.ui.routeguide.subview.r;
import com.nd.ql.bdmap.MapDataInfo;
import com.nd.ql.bdmap.MyOrientationListener;
import com.nd.ql.bdmap.MyOrientationListener.OnOrientationListener;
import com.nd.ql.login.R;
import com.nd.ql.public_class.ThreadClass;
import com.nd.ql.public_class.GetNetConnection;
import com.nd.ql.public_class.PublicPath;
import com.nd.ql.public_class.PullResponseServletData;

public class MapFragment extends Fragment implements OnGetRoutePlanResultListener{
    private LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    private BitmapDescriptor mIconLocation;
    private BitmapDescriptor mMarker;
    private RelativeLayout mMarkerLy;
    private boolean isFirstIn = true;
    private double mLatitude;
    private double mLongitude;
    private BaiduMap baiduMap;
    private com.baidu.mapapi.map.MyLocationConfiguration.LocationMode mLocationMode;
    MapView mMapView = null;
    private static String LOCATION = "";
    private MyOrientationListener myOrientationListener;
    private float mCurrentX;
    private LatLng enLatLng;
    RouteLine route = null;
    private RoutePlanSearch mSearch;// 路径规划搜索接口
    private int drivintResultIndex = 0;	//驾车路线方案index
    private int totalLine = 0;		//总路线数量
    
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
		//实现定位
		initlocation();
		
		initMarker();
		
		mMarkerLy = (RelativeLayout) mapLayout.findViewById(R.id.rl_maker_ly);

		
		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@SuppressLint("NewApi")
			@Override
			public boolean onMarkerClick(Marker marker) {
				Bundle extraInfo = marker.getExtraInfo();
				MapDataInfo info = (MapDataInfo) extraInfo.getSerializable("info");
				TextView distance = (TextView) mMarkerLy.findViewById(R.id.tv_info_distance);
				TextView price = (TextView) mMarkerLy.findViewById(R.id.tv_info_price);
				TextView name = (TextView) mMarkerLy.findViewById(R.id.tv_info_name);
				TextView lastnum = (TextView) mMarkerLy.findViewById(R.id.tv_info_lastnum);
				
				distance.setText("距离:" + info.getDistance() + "米");
				price.setText("价格:" + String.valueOf(info.getParkingprice()) + "元/小时");
				name.setText(info.getName());
				lastnum.setText("剩余车位:" + String.valueOf(info.getLastparknum()));
				enLatLng = new LatLng(info.getLongitude(), info.getLatitude());
				System.out.println("This latlng : " + enLatLng);
				
				InfoWindow infoWindow;
				TextView tv = new TextView(getActivity());
				tv.setBackgroundResource(R.drawable.location_tips);
				tv.setPadding(30, 20, 30, 50);
				tv.setTextColor(Color.parseColor("#ffffff"));
				tv.setText(info.getName());
				
				final LatLng latLng = marker.getPosition();
				Point p = baiduMap.getProjection().toScreenLocation(latLng);
				p.y -= 47;
				LatLng ll = baiduMap.getProjection().fromScreenLocation(p);
				
				BitmapDescriptor tvBD = BitmapDescriptorFactory.fromView(tv);
				infoWindow = new InfoWindow(tvBD, ll, 0, new OnInfoWindowClickListener() {
					
					@Override
					public void onInfoWindowClick() {
						baiduMap.hideInfoWindow();
					}
				});
				
				baiduMap.showInfoWindow(infoWindow);
				
				mMarkerLy.setVisibility(View.VISIBLE);
				
				return true;
			}
		});
		
		baiduMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}
			
			@Override
			public void onMapClick(LatLng arg0) {
				mMarkerLy.setVisibility(View.GONE);
				baiduMap.hideInfoWindow();
			}
		});
		
		TextView navi = (TextView) mMarkerLy.findViewById(R.id.tv_navi_click);
		navi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LatLng stLatLng = new LatLng(mLatitude,mLongitude);
				System.out.println("start latlng : " + stLatLng);
				navigation_click(stLatLng,enLatLng);
			}
		});
				
		
		return mapLayout;
	}
	
	private void navigation_click(LatLng stLatLng,LatLng enLatLng){

		mSearch.setOnGetRoutePlanResultListener(this);
		PlanNode stNode = PlanNode.withLocation(stLatLng);
		PlanNode enNode = PlanNode.withLocation(enLatLng);
		mSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(enNode));
		System.out.println("drivingrouteplanoption:" + stNode.toString() + "   " + enNode.toString());
	}
	
		
	private void initMarker() {
		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.maker);
		addOverlays(MapDataInfo.infos);
	}

	
	private void addOverlays(List<MapDataInfo> infos) {
		baiduMap.clear();
		LatLng latLng = null;
		Marker marker = null;
		OverlayOptions options;
		for(MapDataInfo info : infos){
			latLng = new LatLng(info.getLongitude(),info.getLatitude());
			options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
			marker = (Marker) baiduMap.addOverlay(options);
			Bundle bundle = new Bundle();
			bundle.putSerializable("info", info);
			marker.setExtraInfo(bundle);
		}
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		baiduMap.setMapStatus(msu);
	}


	private void initview() {
		baiduMap = mMapView.getMap();
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		baiduMap.setTrafficEnabled(true);
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		baiduMap.setMapStatus(msu);
		
		mSearch = RoutePlanSearch.newInstance();
	}


	private void initlocation() {
		mLocationMode = com.baidu.mapapi.map.MyLocationConfiguration.LocationMode.NORMAL;
		mLocationClient = new LocationClient(getActivity());
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);
		
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");	//bd09ll
		option.setIsNeedAddress(true);	//允许获取地址
		option.setOpenGps(true);		//开启GPS
		option.setScanSpan(1000);		//定位间隔时间1s
		mLocationClient.setLocOption(option);
		
		mIconLocation = BitmapDescriptorFactory
				.fromResource(R.drawable.bullet_blue);
		
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
	
	//定位到我的位置
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
				
				Toast.makeText(getActivity().getApplicationContext(), location.getAddrStr(),Toast.LENGTH_SHORT).show();
			}
		}	
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		mSearch.destroy();
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

	
	
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
		//baiduMap.clear();
		if(drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR){
			Log.i("resultError", "drivingrouteresulterror:"+drivingRouteResult.error);
			Toast.makeText(getActivity().getApplicationContext(), "抱歉，未找到结果！", Toast.LENGTH_SHORT).show();
		}
		if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			//drivingRouteResult.getSuggestAddrInfo();
			return;
		}
		if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR){
			DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(baiduMap);
			drivingRouteOverlay.setData(drivingRouteResult.getRouteLines().get(drivintResultIndex));
			drivingRouteOverlay.addToMap();
			drivingRouteOverlay.zoomToSpan();
			totalLine = drivingRouteResult.getRouteLines().size();
			Log.i("result size", "result size :　" + totalLine);
			if(drivingRouteResult.getTaxiInfo() != null){
				Toast.makeText(getActivity().getApplicationContext(), "总路程:" + drivingRouteResult.getTaxiInfo().getDistance(), 1000).show();
			}
		}
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
		// TODO Auto-generated method stub
		
	}

}
