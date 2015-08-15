package com.nd.ql.activity;

import com.nd.ql.login.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText etUsername, etPassword;
	private Button btnLogin;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		etUsername = (EditText) findViewById(R.id.et_login_username);
		etPassword = (EditText) findViewById(R.id.et_login_password);
		btnLogin = (Button) findViewById(R.id.btn_signin);
		btnLogin.setOnClickListener(listen);
	}

	OnClickListener listen = new OnClickListener() {
		public void onClick(View v) {
			String username = etUsername.getText().toString().trim();
			String password = etPassword.getText().toString().trim();
			if (username.equals("") || password.equals("")) {
				Toast.makeText(LoginActivity.this, "��������˺Ż������벻��Ϊ�գ�����������", 0)
						.show();
			} else {
				if (username.equals("zhangsan") && password.equals("123")) {
					Toast.makeText(LoginActivity.this, "��¼�ɹ�", 0).show();
				} else {
					Toast.makeText(LoginActivity.this, "��¼ʧ��", 0).show();
				}
			}
		}
	};
}
