package com.cd7d.ttm.ui;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cd7d.ttm.R;
import com.cd7d.ttm.dao.Db;
import com.cd7d.ttm.dao.HttpUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {
	Db mdb = new Db(this);
	private Thread mThread;

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
		if (mdb.Islogin()) {

			lgshowusername.setText(mdb.UserName);
			
			lgshowusername.setVisibility(View.VISIBLE);
			lglogoutbt.setVisibility(View.VISIBLE);
			lgupdatebt.setVisibility(View.VISIBLE);

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
		}

	}

	public void ToLogin(View vw) {
		System.out.println(R.string.ServerUrl+"/ajax/login.ashx?username=");
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
		if (mThread == null) {
			mThread = new Thread(runnable);
			mThread.start();
		}
	}
	
	// 线程完成后处理UI
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {// 此方法在ui线程运行
			switch (msg.what) {
			case 0:
//				ReFreshData();
				System.out.println("同步成功");
				break;
			case 1:
				System.out.println("线程数据读取失败");
				break;
			}
		}
	};

	// 同步数据库数据线程
	Runnable runnable = new Runnable() {
		@Override
		public void run() {// run()在新的线程中运行
			try {

				String mvar = "1";
				System.out.println(getResources().getString(R.string.ServerUrl)+"/ajax/tasklist.ashx?username="+mdb.UserName+"&loginkey="+mdb.UserKey);
				// http://lz.86mt.com/ajax/login.ashx?username=zjx&password=831214
				mvar = HttpUtil
						.getRequest(getResources().getString(R.string.ServerUrl)+"/ajax/tasklist.ashx?username="+mdb.UserName+"&loginkey="+mdb.UserKey);
				JSONObject mjson = new JSONObject(mvar);
				System.out.println(mjson.getString("usertname"));
				System.out.println(mjson.getInt("userid"));
				if (mjson.getInt("userid") > 0) {

					JSONArray jsonArray = mjson.getJSONArray("taskdata");
					mdb.OpenDb();
					mdb.db.beginTransaction();  //手动设置开始事务
				        try{
					for (int i = 0; i < jsonArray.length(); i++) {

						
						 Cursor ckcursor = mdb.db.rawQuery("select  * from task where serverid="+jsonArray.optJSONObject(i).getString("no"), null);

						if (ckcursor.getCount() != 0) {
							mdb.db.execSQL("update task set "
									+ "name='"+ jsonArray.optJSONObject(i).getString("name")+ "',"
									+ "description='"+ jsonArray.optJSONObject(i).getString("note")+ "',"
									+ "edittime='"+ jsonArray.optJSONObject(i).getString("time")+ "',"
									+ "ttype="+ jsonArray.optJSONObject(i).getString("type")+ ","
									+ "percent="+ jsonArray.optJSONObject(i).getString("percent")+ ","
									+ "ttime='"+ jsonArray.optJSONObject(i).getString("time")+ "',"
									//+ "torder='"+ jsonArray.optJSONObject(i).getString("name")+ "',"
									//+ "tpic='"+ jsonArray.optJSONObject(i).getString("name")+ "',"
									+ "isring="+ ((jsonArray.optJSONObject(i).getString("isring") == "43") ? "1" : "0")+ ","
									+ "projectid="+ jsonArray.optJSONObject(i).getString("projectid")+ ""
									+ " where serverid="+jsonArray.optJSONObject(i).getString("no")+";");
						}else{
						
						mdb.db.execSQL("insert into task(name,description,edittime,ttype,percent,ttime,torder,tpic,isring,projectid,serverid)  values('"
								+ jsonArray.optJSONObject(i).getString("name")
								+ "','"
								+ jsonArray.optJSONObject(i).getString("note")
								+ "',datetime('"
								+ (jsonArray.optJSONObject(i).getString("time")
										.length() > 0 ? jsonArray
										.optJSONObject(i).getString("time")
										: "now','+8 hours")
								+ "'),"
								+ jsonArray.optJSONObject(i).getString("type")
								+ ","
								+ jsonArray.optJSONObject(i).getString(
										"percent")
								+ ",datetime('"
								+ (jsonArray.optJSONObject(i).getString("time")
										.length() > 0 ? jsonArray
										.optJSONObject(i).getString("time")
										: "now','+8 hours")
								+ "'),0,'',"
								+ ((jsonArray.optJSONObject(i).getString("isring") == "43") ? "1" : "0")
								+ ","
								+ jsonArray.optJSONObject(i).getString(
										"projectid")
								+ ","
								+ jsonArray.optJSONObject(i).getString("no")
								+ ");");
						}

					}
					mdb.db.setTransactionSuccessful();  
				        }catch(Exception e){
				        	e.printStackTrace();
				           }finally{
				        	   mdb.db.endTransaction(); //处理完成
				           }
				        mdb.CloseDb();
				} else {
					System.out.println(mjson.toString());
				}
				mHandler.obtainMessage(0).sendToTarget();// 获取图片成功，向ui线程发送MSG_SUCCESS标识和bitmap对象

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mHandler.obtainMessage(1).sendToTarget();// 获取图片失败
			}

		}
	};

}
