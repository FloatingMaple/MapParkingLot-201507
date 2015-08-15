package com.nd.ql.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.R.array;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.nd.ql.login.R;
import com.nd.ql.public_class.GetBitmap;
import com.nd.ql.public_class.ThreadClass;
import com.nd.ql.public_class.GetNetConnection;
import com.nd.ql.public_class.PublicPath;
import com.nd.ql.public_class.PullResponseServletData;

public class OrderFragment extends Fragment {
	protected static final String Tag = "MyTag";
	ListView lv_order;
	View mView;
	List<Map<String, Object>> data;// 用来存放listview的数据
	String[] string = null;// 用来存服务器返回的字符串
	Button btn_privi_hot;
	Button btn_privi_friend;
	Button btn_privi_remind;
	JSONArray array;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View orderLayout = inflater.inflate(R.layout.home_order_tag, container,
				false);
		lv_order = (ListView) orderLayout.findViewById(R.id.lv_order);
		String path = "http://" + PublicPath.coonPath
				+ ":80/QuickLife/home_privilege_list";
		new ThreadClass(handler, path).start();
		return orderLayout;
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				array = new JSONArray(ThreadClass.result);
			} catch (Exception e) {
				e.printStackTrace();
			}
			List<HashMap<String, Object>> mListData = getListData();
			SimpleAdapter adapter = new SimpleAdapter(getActivity(), mListData,
					R.layout.home_order_list_item, new String[] {
							"iv_order_logo", "tv_order_date", "tv_order_name",
							"tv_order_price" }, new int[] { R.id.iv_order_logo,
							R.id.tv_order_date, R.id.tv_order_name,
							R.id.tv_order_price });
			lv_order.setAdapter(adapter);
		};
	};

	public List<HashMap<String, Object>> getListData() {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map = null;
		try {
			for (int i = 0; i < 10; i++) {
				map = new HashMap<String, Object>();
				map.put("iv_order_logo",
						GetBitmap.getBitmap(array.getJSONObject(i).getString(
								"buss_name")));
				map.put("tv_order_date",
						array.getJSONObject(i).getString("coupons_name"));
				map.put("tv_order_name",
						array.getJSONObject(i).getString("coupons_name"));
				map.put("tv_order_price",
						array.getJSONObject(i).getString("coupons_name"));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}