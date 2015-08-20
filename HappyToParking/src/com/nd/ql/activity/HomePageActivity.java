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
	 * ���嶯̬���Żݡ�ǩ����Fragment
	 */
	private MapFragment mapFragment;
	private OrderFragment orderFragment;
	private MyselfFragment myselfFragment;

	/*
	 * ���嶯̬���Żݡ�ǩ����fragment�Ĳ���
	 */
	private View mapLayout;
	private View orderLayout;
	private View myselfLayout;

	/*
	 * ���嶯̬���Żݡ�ǩ����tab��ͼ��
	 */
	private ImageView iv_map;
	private ImageView iv_order;
	private ImageView iv_myself;

	/*
	 * ������Ϣ����ϵ�ˡ���̬��tab���ı�
	 */
	private TextView tv_map;
	private TextView tv_order;
	private TextView tv_myself;

	private FragmentManager fragmentManager; // ��Fragment���й���
	private DrawerLayout mDrawerLayout = null;
	private ViewFlipper flipper;// ViewFlipperʵ��
	private GestureDetector detector;// ��������ʵ��
	private String user_id = null;

	protected void onCreate(Bundle savedInstancemap) {
		super.onCreate(savedInstancemap);
		Intent intent = getIntent();
		user_id = intent.getStringExtra("user_id");
		setContentView(R.layout.home_main);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		initViews(); // ��ʼ�����棬�������ĸ�tab�ļ���		
		fragmentManager = getFragmentManager();
		setTabSelection(0); // ��һ������ʱ������0��tab
	}

	public String setUser_id() {
		return user_id;
	}

	/*
	 * ���ݴ����index�������ÿ�����tabҳ��index�����Ӧ���±꣬0��Ӧ��̬��1��Ӧ�Żݣ�2��Ӧǩ��
	 */

	private void setTabSelection(int i) {
		// ����֮ǰ������״̬
		clearSelection();
		// ����һ��Fragment����
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// �������е�fragment����ֹ�ж��������ʾ�ڽ�����
		hideFragments(transaction);
		switch (i) {
		case 0:
			// �������Ϣtabʱ���ı�ؼ���ͼƬ��������ɫ
			iv_map.setImageResource(R.drawable.iv_tag_map);
			tv_map.setTextColor(Color.RED);
			// ���mapFragmentΪ�գ��򴴽�һ����ӵ�������
			if (mapFragment == null) {
				mapFragment = new MapFragment();
				transaction.add(R.id.content, mapFragment);
			} else {
				// ���mapFragment��Ϊ�գ���ֱ�ӽ�����ʾ����
				transaction.show(mapFragment);
			}
			break;
		case 1:
			// �������ϵ��tabʱ���ı�ؼ���ͼƬ��������ɫ
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
			// �����Ϣtab��ѡ�е�һ��tab
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
