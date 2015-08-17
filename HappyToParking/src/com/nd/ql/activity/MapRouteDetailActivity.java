/**
 * 
 */
package com.nd.ql.activity;

import com.baidu.mapapi.map.Text;
import com.nd.ql.login.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * @author Cerulean
 *
 */
public class MapRouteDetailActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.nd.ql.login.R.layout.map_navi_detail_info);
		
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		String detail_info = bundle.getString("detailinfo");
		
		TextView tv_show_detail_info = 
				(TextView) findViewById(R.id.tv_show_detail_info);
		tv_show_detail_info.setText(detail_info);
		
		TextView tv_return_from_detail = 
				(TextView) findViewById(R.id.tv_return_from_detail);
		tv_return_from_detail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClassName(MapRouteDetailActivity.this, "com.nd.ql.activity.HomePageActivity");
				startActivity(intent);
			}
		});
		
	}
	
	
}
