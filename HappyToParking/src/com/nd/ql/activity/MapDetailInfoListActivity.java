/**
 * 
 */
package com.nd.ql.activity;

import java.net.ContentHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.mapapi.map.Text;
import com.baidu.navisdk.R.color;
import com.nd.ql.bdmap.MapDataInfo;
import com.nd.ql.fragment.MapFragment;
import com.nd.ql.login.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Cerulean
 *
 */
public class MapDetailInfoListActivity extends ListActivity {

	private List<MapDataInfo> tempinfolist;
	
    public static double[] chooselatlng;
    
    public double[] getchooselatlng(){
    	return chooselatlng;
    }
    
    public void setchooselatlng(double[] latlng){
    	this.chooselatlng = latlng;
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		tempinfolist = (List<MapDataInfo>) bundle.getSerializable("info");
		MyAdapter adapter = new MyAdapter(this);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Log.i("list item click ", tempinfolist.get(position).getName());
	}
	
	public final class ViewHolder{
		public TextView tv_map_detail_info_list_name;
		public TextView tv_map_detail_info_list_distance;
		public TextView tv_map_detail_info_list_address;
		public TextView tv_map_detail_info_list_price;
		public TextView tv_map_detail_info_list_lastnum;
		public Button bt_map_detail_info_list_navi;
	}
	
	public class MyAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		
		public MyAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount() {
			return tempinfolist.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View converView, ViewGroup parent) {
			
			ViewHolder holder = null;
			if(converView == null){
				holder = new ViewHolder();
				converView = mInflater.inflate(R.layout.map_detail_info_list, null);
				holder.tv_map_detail_info_list_address = 
						(TextView) converView.findViewById(R.id.tv_map_detail_info_list_address);
				holder.tv_map_detail_info_list_distance = 
						(TextView) converView.findViewById(R.id.tv_map_detail_info_list_distance);
				holder.tv_map_detail_info_list_lastnum = 
						(TextView) converView.findViewById(R.id.tv_map_detail_info_list_lastnum);
				holder.tv_map_detail_info_list_name = 
						(TextView) converView.findViewById(R.id.tv_map_detail_info_list_name);
				holder.tv_map_detail_info_list_price = 
						(TextView) converView.findViewById(R.id.tv_map_detail_info_list_price);
				holder.bt_map_detail_info_list_navi = 
						(Button) converView.findViewById(R.id.bt_map_detail_info_list_navi);
				converView.setTag(holder);
			}else {
				holder = (ViewHolder) converView.getTag();
			}
			holder.tv_map_detail_info_list_address.setText(tempinfolist.get(position).getAddress());
			holder.tv_map_detail_info_list_distance.setText(tempinfolist.get(position).getDistance());
			holder.tv_map_detail_info_list_lastnum.setText(String.valueOf(tempinfolist.get(position).getLastparknum()));
			holder.tv_map_detail_info_list_name.setText(tempinfolist.get(position).getName());
			holder.tv_map_detail_info_list_price.setText(String.valueOf(tempinfolist.get(position).getParkingprice()));
			
			holder.bt_map_detail_info_list_navi.setOnClickListener(new OnClickListener() {
			
			double[] chooselatlng = {tempinfolist.get(position).getLatitude(),tempinfolist.get(position).getLongitude()}; 
				@Override
				public void onClick(View view) {
					setchooselatlng(chooselatlng);
					Log.i("chooselatlng", chooselatlng[0] + " " + chooselatlng[1]);
					Intent intent = new Intent();
					intent.setClassName(MapDetailInfoListActivity.this, "com.nd.ql.activity.HomePageActivity");
					startActivity(intent);
				}
			});
			return converView;
		}
		
	}
}
