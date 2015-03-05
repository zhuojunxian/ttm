package com.cd7d.ttm.ui;


import org.json.JSONObject;

import com.cd7d.ttm.R;
import com.cd7d.ttm.dao.Db;
import com.cd7d.ttm.dao.HttpUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {
	Db mdb = new Db(this);
 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		Refresh();

	}

	private void Refresh() {

		EditText lgusername = (EditText) findViewById(R.id.loginUsername);
		TextView lguserpasslb = (TextView) findViewById(R.id.login_userpass_lb);
		EditText lguserpass = (EditText) findViewById(R.id.loginUserpass);
		Button lgloginbt = (Button) findViewById(R.id.login_login);

		
		
		
		TextView lgshowusername = (TextView) findViewById(R.id.login_showusername);
		Button lglogoutbt = (Button) findViewById(R.id.login_logout);
		Button lgupdatebt = (Button) findViewById(R.id.login_update);
		Button lgbackebt = (Button) findViewById(R.id.login_back);
		if (mdb.Islogin()) {

			lgshowusername.setText(mdb.UserName);
			
			lgshowusername.setVisibility(View.VISIBLE);
			lglogoutbt.setVisibility(View.VISIBLE);
			lgupdatebt.setVisibility(View.VISIBLE);
			lgbackebt.setVisibility(View.VISIBLE);

			lgusername.setVisibility(View.GONE);
			lguserpasslb.setVisibility(View.GONE);
			lguserpass.setVisibility(View.GONE);
			lgloginbt.setVisibility(View.GONE);
			
		} else {
			lgusername.setVisibility(View.VISIBLE);
			lguserpasslb.setVisibility(View.VISIBLE);
			lguserpass.setVisibility(View.VISIBLE);
			lgloginbt.setVisibility(View.VISIBLE);
			
			
			lgshowusername.setText("");
			lgshowusername.setVisibility(View.GONE);
			lglogoutbt.setVisibility(View.GONE);
			lgupdatebt.setVisibility(View.GONE);
			lgbackebt.setVisibility(View.GONE);
		}

	}

	public void ToLogin(View vw) {
 
		EditText lgusername = (EditText) findViewById(R.id.loginUsername);
		EditText lguserpass = (EditText) findViewById(R.id.loginUserpass);
		String mvar = "1";
		String vusername = mdb.NoInjSql(lgusername.getText().toString());
		String vpassword = mdb.NoInjSql(lguserpass.getText().toString());
		try {
			mvar = HttpUtil
					.getRequest(getResources().getString(R.string.ServerUrl)+"/ajax/login.ashx?username="
							+ vusername + "&password=" + vpassword);
			JSONObject mjson = new JSONObject(mvar);
			if (mjson.getString("msg").equals("login")) {

				mdb.RunSql("update userinfo set username='" + vusername
						+ "',userpass='" + vpassword + "',userkey='"
						+ mjson.getString("loginkey") + "' ");
				System.out.println("登录成功！");
				Refresh();
			} else {
				System.out.println("帐号或密码不正确！");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void ToLogout(View vw) {
		mdb.RunSql("update userinfo set username='',userpass='',userkey='' ");
		Refresh();
	}
	
	public void ToClose(View vw) {
		finish();
	}

	
	public void ToUpdate(View vw) {
		Intent myint = new Intent(Login.this, GetServerData.class);

		startActivityForResult(myint, 0);
	}
	
}
