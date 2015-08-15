package com.nd.ql.activity;

import com.nd.ql.login.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private EditText etRegUsername, etRegPassword, etRegPasswordAgain;
	private Button btnRegister;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		etRegUsername = (EditText) findViewById(R.id.et_reg_username);
		etRegPassword = (EditText) findViewById(R.id.et_reg_password);
		etRegPasswordAgain = (EditText) findViewById(R.id.et_reg_password_again);
		btnRegister = (Button) findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(listen);
	}

	OnClickListener listen = new OnClickListener() {

		public void onClick(View v) {
			String username = etRegUsername.getText().toString().trim();
			String password = etRegPassword.getText().toString().trim();
			String passwordAgain = etRegPasswordAgain.getText().toString()
					.trim();
			if (username.equals("") || password.equals("")) {
				Toast.makeText(RegisterActivity.this, "您输入的账号或者密码不能为空，请重新输入", 0)
						.show();
			} else {
				if (!password.equals(passwordAgain)) {
					Toast.makeText(RegisterActivity.this, "确认密码有误，请重新输入", 0)
							.show();
				} else {
					Intent intent = new Intent();
					intent.setClass(RegisterActivity.this, LoginActivity.class);
					startActivity(intent);
				}
			}
		}
	};
}
