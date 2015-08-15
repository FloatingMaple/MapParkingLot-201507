package com.nd.ql.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.ContentHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.R.array;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.nd.ql.activity.LoginActivity;
import com.nd.ql.activity.RegisterActivity;
import com.nd.ql.assist_class.MyScrollView;
import com.nd.ql.login.R;
import com.nd.ql.public_class.ThreadClass;
import com.nd.ql.public_class.GetNetConnection;
import com.nd.ql.public_class.PublicPath;
import com.nd.ql.public_class.PullResponseServletData;

public class MyselfFragment extends Fragment implements OnGestureListener,
		OnTouchListener {
	protected static final String Tag = "MyTag";
	JSONArray array;
	private TextView date_TextView;
	private ImageButton set_ImageButton, regist_ImageButton, login_ImageButton;
	private ViewFlipper viewFlipper;
	private boolean showNext = true;
	private boolean isRun = true;
	private int currentPage = 0;
	private final int SHOW_NEXT = 0011;
	private static final int FLING_MIN_DISTANCE = 50;
	private static final int FLING_MIN_VELOCITY = 0;
	private GestureDetector mGestureDetector;
	private View myselfLayout;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myselfLayout = inflater.inflate(R.layout.home_myself_tag, container,
				false);
		date_TextView = (TextView) myselfLayout.findViewById(R.id.home_date_tv);
		date_TextView.setText(getDate());

		set_ImageButton = (ImageButton) myselfLayout
				.findViewById(R.id.title_set_bn);
		set_ImageButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				toastInfo("设置属性");
			}
		});
		regist_ImageButton = (ImageButton) myselfLayout
				.findViewById(R.id.home_bn_regist);
		regist_ImageButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), RegisterActivity.class);
				startActivity(intent);
			}
		});
		login_ImageButton = (ImageButton) myselfLayout
				.findViewById(R.id.home_bn_login);
		login_ImageButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
		});

		viewFlipper = (ViewFlipper) myselfLayout
				.findViewById(R.id.mViewFliper_vf);
		mGestureDetector = new GestureDetector(this);
		viewFlipper.setOnTouchListener(this);
		viewFlipper.setLongClickable(true);
		viewFlipper.setOnClickListener(clickListener);
		displayRatio_selelct(currentPage);

		MyScrollView myScrollView = (MyScrollView) myselfLayout
				.findViewById(R.id.viewflipper_scrollview);
		myScrollView.setOnTouchListener(onTouchListener);
		myScrollView.setGestureDetector(mGestureDetector);

		thread.start();
		return myselfLayout;
	}

	private OnClickListener clickListener = new OnClickListener() {

		public void onClick(View v) {
			
		}
	};
	private OnTouchListener onTouchListener = new OnTouchListener() {

		public boolean onTouch(View v, MotionEvent event) {
			return mGestureDetector.onTouchEvent(event);
		}
	};

	Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_NEXT:
				if (showNext) {
					showNextView();
				} else {
					showPreviousView();
				}
				break;

			default:
				break;
			}
		}

	};

	private String getDate() {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		int w = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		String mDate = c.get(Calendar.YEAR) + "年" + c.get(Calendar.MONTH) + "月"
				+ c.get(Calendar.DATE) + "日  " + weekDays[w];
		return mDate;
	}

	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

	public boolean onDown(MotionEvent e) {
		return false;
	}

	public void onShowPress(MotionEvent e) {

	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		return false;
	}

	public void onLongPress(MotionEvent e) {

	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.e("view", "onFling");
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			Log.e("fling", "left");
			showNextView();
			showNext = true;
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			Log.e("fling", "right");
			showPreviousView();
			showNext = false;
		}
		return false;
	}

	Thread thread = new Thread() {

		public void run() {
			while (isRun) {
				try {
					Thread.sleep(1000 * 8);
					Message msg = new Message();
					msg.what = SHOW_NEXT;
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	};

	private void showNextView() {

		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.push_left_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.push_left_out));
		viewFlipper.showNext();
		currentPage++;
		if (currentPage == viewFlipper.getChildCount()) {
			displayRatio_normal(currentPage - 1);
			currentPage = 0;
			displayRatio_selelct(currentPage);
		} else {
			displayRatio_selelct(currentPage);
			displayRatio_normal(currentPage - 1);
		}
		Log.e("currentPage", currentPage + "");

	}

	private void showPreviousView() {
		displayRatio_selelct(currentPage);
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.push_right_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),
				R.anim.push_right_out));
		viewFlipper.showPrevious();
		currentPage--;
		if (currentPage == -1) {
			displayRatio_normal(currentPage + 1);
			currentPage = viewFlipper.getChildCount() - 1;
			displayRatio_selelct(currentPage);
		} else {
			displayRatio_selelct(currentPage);
			displayRatio_normal(currentPage + 1);
		}
		Log.e("currentPage", currentPage + "");
	}

	private void displayRatio_selelct(int id) {
		int[] ratioId = { R.id.home_ratio_img_04, R.id.home_ratio_img_03,
				R.id.home_ratio_img_02, R.id.home_ratio_img_01 };
		ImageView img = (ImageView) myselfLayout.findViewById(ratioId[id]);
		img.setSelected(true);
	}

	private void displayRatio_normal(int id) {
		int[] ratioId = { R.id.home_ratio_img_04, R.id.home_ratio_img_03,
				R.id.home_ratio_img_02, R.id.home_ratio_img_01 };
		ImageView img = (ImageView) myselfLayout.findViewById(ratioId[id]);
		img.setSelected(false);
	}

	private void toastInfo(String string) {
		Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
	}

}