package com.nd.ql.activity;

import java.security.PublicKey;

import com.nd.ql.fragment.MapFragment;
import com.nd.ql.fragment.MyselfFragment;
import com.nd.ql.fragment.OrderFragment;
import com.nd.ql.login.R;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class HomePageActivity extends Activity implements OnClickListener {

	/*
	 * 定义动态、优惠、签到的Fragment
	 */
	private MapFragment mapFragment;
	private OrderFragment orderFragment;
	private MyselfFragment myselfFragment;

	/*
	 * 定义动态、优惠、签到的fragment的布局
	 */
	private View mapLayout;
	private View orderLayout;
	private View myselfLayout;

	/*
	 * 定义动态、优惠、签到的tab的图标
	 */
	private ImageView iv_map;
	private ImageView iv_order;
	private ImageView iv_myself;

	/*
	 * 定义消息、联系人、动态的tab的文本
	 */
	private TextView tv_map;
	private TextView tv_order;
	private TextView tv_myself;

	private FragmentManager fragmentManager; // 对Fragment进行管理
	private DrawerLayout mDrawerLayout = null;
	private ViewFlipper flipper;// ViewFlipper实例
	private GestureDetector detector;// 触摸监听实例
	private String user_id = null;

	protected void onCreate(Bundle savedInstancemap) {
		super.onCreate(savedInstancemap);
		Intent intent = getIntent();
		user_id = intent.getStringExtra("user_id");
		setContentView(R.layout.home_main);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		initViews(); // 初始化界面，并设置四个tab的监听		
		fragmentManager = getFragmentManager();
		setTabSelection(0); // 第一次启动时开启第0个tab
	}

	public String setUser_id() {
		return user_id;
	}

	/*
	 * 根据传入的index，来设置开启的tab页面index代表对应的下标，0对应动态，1对应优惠，2对应签到
	 */

	private void setTabSelection(int i) {
		// 清理之前的所有状态
		clearSelection();
		// 开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 隐藏所有的fragment，防止有多个界面显示在界面上
		hideFragments(transaction);
		switch (i) {
		case 0:
			// 当点击消息tab时，改变控件的图片和文字颜色
			iv_map.setImageResource(R.drawable.iv_tag_map);
			tv_map.setTextColor(Color.RED);
			// 如果mapFragment为空，则创建一个添加到界面上
			if (mapFragment == null) {
				mapFragment = new MapFragment();
				transaction.add(R.id.content, mapFragment);
			} else {
				// 如果mapFragment不为空，则直接将它显示出来
				transaction.show(mapFragment);
			}
			break;
		case 1:
			// 当点击联系人tab时，改变控件的图片和文字颜色
			iv_order.setImageResource(R.drawable.iv_tag_order);
			tv_order.setTextColor(Color.YELLOW);
			if (orderFragment == null) {
				orderFragment = new OrderFragment();
				transaction.add(R.id.content, orderFragment);
			} else {
				transaction.show(orderFragment);
			}
			break;
		case 2:
			iv_myself.setImageResource(R.drawable.iv_tag_myself);
			tv_myself.setTextColor(Color.BLUE);
			if (myselfFragment == null) {
				myselfFragment = new MyselfFragment();
				transaction.add(R.id.content, myselfFragment);
			} else {
				transaction.show(myselfFragment);
			}
			break;
		default:
			break;
		}
		transaction.commit();
	}

	private void hideFragments(FragmentTransaction transaction) {
		if (mapFragment != null) {
			transaction.hide(mapFragment);
		}
		if (orderFragment != null) {
			transaction.hide(orderFragment);
		}
		if (myselfFragment != null) {
			transaction.hide(myselfFragment);
		}

	}

	private void clearSelection() {
		iv_map.setImageResource(R.drawable.iv_tag_map);
		tv_map.setTextColor(Color.parseColor("#82858b"));
		iv_order.setImageResource(R.drawable.iv_tag_order);
		tv_order.setTextColor(Color.parseColor("#82858b"));
		iv_myself.setImageResource(R.drawable.iv_tag_myself);
		tv_myself.setTextColor(Color.parseColor("#82858b"));
	}

	private void initViews() {
		mapLayout = findViewById(R.id.layout_map);
		orderLayout = findViewById(R.id.layout_order);
		myselfLayout = findViewById(R.id.layout_myself);

		iv_map = (ImageView) findViewById(R.id.iv_map);
		iv_order = (ImageView) findViewById(R.id.iv_order);
		iv_myself = (ImageView) findViewById(R.id.iv_myself);

		tv_map = (TextView) findViewById(R.id.tv_map);
		tv_order = (TextView) findViewById(R.id.tv_order);
		tv_myself = (TextView) findViewById(R.id.tv_myself);

		mapLayout.setOnClickListener(this);
		orderLayout.setOnClickListener(this);
		myselfLayout.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_map:
			// 点击消息tab，选中第一个tab
			setTabSelection(0);
			break;
		case R.id.layout_order:
			setTabSelection(1);
			break;
		case R.id.layout_myself:
			setTabSelection(2);
			break;
		default:
			break;
		}
	}
}
