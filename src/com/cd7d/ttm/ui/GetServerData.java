package com.cd7d.ttm.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import android.widget.TextView;

public class GetServerData extends Activity {
	Db mdb = new Db(this);
	private Thread mThread;
	TextView getlb;
	Button getbt;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.getserverdata);
		getlb = (TextView) findViewById(R.id.getServerDatalb);
		getbt = (Button) findViewById(R.id.getserverdata_back);
		getlb.setText("数据上行中，请稍候...");
		getbt.setText("取消");
		if (mdb.Islogin()) {
			if (mThread == null) {// runsendwork rungettask
				mThread = new Thread(runsendwork);
				mThread.start();
			}
		}
	}

	public void ToClose(View vw) {
		finish();
	}

	// 向下同步任务数据线程完成后处理UI
	@SuppressLint("HandlerLeak")
	private Handler getTaskHandler = new Handler() {
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {// 此方法在ui线程运行
			switch (msg.what) {
			case 0:
				// ReFreshData();
				System.out.println("同步成功");
				getlb.setText("数据下行中，请稍候...");

				mThread = new Thread(rungettask);
				mThread.start();

				break;
			case 1:
				System.out.println("上行同步数据失败");
				getlb.setText("上行同步同步失败了，返回稍后再试！");
				break;
			case 2:
				// ReFreshData();
				System.out.println("同步成功");
				getlb.setText("数据同步完成");

				break;
			case 3:
				System.out.println("下行同步数据失败");
				getlb.setText("下行同步失败了，返回稍后再试！");
				break;
			}
			getbt.setText("返回");
		}
	};

	// 向上同步任务数据线程
	Runnable runsendwork = new Runnable() {
		@Override
		public void run() {// run()在新的线程中运行
			try {

				Cursor ckcursor = mdb
						.getCursor("select * from task limit 0,100;");
				int ckccount = 0;
				if (ckcursor.getCount() > 0) {
					String mvar = "[";
					while (ckcursor.moveToNext()) {
						if (ckccount != 0) {
							mvar = mvar + ",";
						}

						mvar = mvar + "{ID:'" + ckcursor.getString(0) + "',"
								+ "Name:'" + ckcursor.getString(1) + "',"
								+ "Desc:'" + ckcursor.getString(2) + "',"
								+ "Edittime:'" + ckcursor.getString(3) + "',"
								+ "Begintime:'" + ckcursor.getString(4) + "',"
								+ "Type:'" + ckcursor.getString(5) + "',"
								+ "Percent:'" + ckcursor.getString(6) + "',"
								+ "Ttime:'" + ckcursor.getString(7) + "',"
								+ "Torder:'" + ckcursor.getString(8) + "',"	
								+ "Isring:'" + ckcursor.getString(10) + "',"																
								+ "Overtime:'" + ckcursor.getString(11) + "',"									
								+ "ProjectID:'" + ckcursor.getString(12) + "',"
								+ "Guid:'" + ckcursor.getString(13) + "'}";
						ckccount++;
					}
					mvar = mvar + "]";
					System.out.println(mvar);
					final Map<String, String> mmap = new HashMap<String, String>();

					mmap.put("data", mvar);

					// http://lz.86mt.com/ajax/login.ashx?username=zjx&password=831214
					mvar = HttpUtil
							.postRequest(
									getResources()
											.getString(R.string.ServerUrl)
											+ "/ajax/taskpost.ashx?username="
											+ mdb.UserName
											+ "&loginkey="
											+ mdb.UserKey, mmap);

				}
				getTaskHandler.obtainMessage(0).sendToTarget();// 成功

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				getTaskHandler.obtainMessage(1).sendToTarget();// 失败
			}

		}
	};
	// 向下同步任务数据线程
	@SuppressLint("SimpleDateFormat")
	Runnable rungettask = new Runnable() {
		@Override
		public void run() {// run()在新的线程中运行

			try {
				System.out.println(getResources().getString(R.string.ServerUrl)
						+ "/ajax/tasklist.ashx?username=" + mdb.UserName
						+ "&loginkey=" + mdb.UserKey + "&time=");

				String mvar = "[]";
				mvar = HttpUtil.getRequest(getResources().getString(
						R.string.ServerUrl)
						+ "/ajax/tasklist.ashx?username="
						+ mdb.UserName
						+ "&loginkey=" + mdb.UserKey + "&time=");

				// System.out.println(mvar);
				if (mvar.length() > 3) {
					JSONObject mjson = new JSONObject(mvar);
//					System.out.println(mjson.getString("usertname"));
//					System.out.println(mjson.getInt("userid"));
					if (mjson.getInt("userid") > 0) {

						JSONArray jsonArray = mjson.getJSONArray("taskdata");
						mdb.OpenDb();
						mdb.db.beginTransaction(); // 手动设置开始事务
						try {
							for (int i = 0; i < jsonArray.length(); i++) {

								Cursor ckcursor = mdb.db.rawQuery(
					"select  * from task where   guid='"+ jsonArray.optJSONObject(i)
	.getString("guid")+"' ", null);

								if (ckcursor.getCount() != 0) {
									
									ckcursor.moveToFirst();
									try{
									SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
								    Date date1 = sdf.parse(ckcursor.getString(3));  
								    Date date2 = sdf.parse(jsonArray.optJSONObject(i).getString("edittime"));  
									if(date1.getTime()<date2.getTime()){
									
									mdb.db.execSQL("update task set "
											+ "name='"
											+ jsonArray.optJSONObject(i)
													.getString("name")
											+ "',"
											+ "description='"
											+ jsonArray.optJSONObject(i)
													.getString("note")
											+ "',"
											+ "edittime='"
											+ jsonArray.optJSONObject(i)
													.getString("edittime")
											+ "',"
											+ "ttype="
											+ jsonArray.optJSONObject(i)
													.getString("type")
											+ ","
											+ "percent="
											+ jsonArray.optJSONObject(i)
													.getString("percent")
											+ ","
											+ "ttime='"
											+ jsonArray.optJSONObject(i)
													.getString("time")
											+ "',"
											// + "torder='"+
											// jsonArray.optJSONObject(i).getString("name")+
											// "',"
											// + "tpic='"+
											// jsonArray.optJSONObject(i).getString("name")+
											// "',"
											+ "isring="
											+ ((jsonArray.optJSONObject(i)
													.getString("isring") == "43") ? "1"
													: "0")
											+ ","
											+ "projectid="
											+ jsonArray.optJSONObject(i)
													.getString("projectid")
											+ ""
											+ " where guid='"
											+ jsonArray.optJSONObject(i)
													.getString("guid") + "';");
									}
									 } catch (Exception exception) {
								            exception.printStackTrace();
								        }
								} else {

									mdb.db.execSQL("insert into task(name,description,edittime,ttype,percent,ttime,torder,tpic,isring,projectid,guid)  values('"
											+ jsonArray.optJSONObject(i)
													.getString("name")
											+ "','"
											+ jsonArray.optJSONObject(i)
													.getString("note")
											+ "',datetime('"
											+ (jsonArray.optJSONObject(i)
													.getString("time").length() > 0 ? jsonArray
													.optJSONObject(i)
													.getString("time")
													: "now','+8 hours")
											+ "'),"
											+ jsonArray.optJSONObject(i)
													.getString("type")
											+ ","
											+ jsonArray.optJSONObject(i)
													.getString("percent")
											+ ",datetime('"
											+ (jsonArray.optJSONObject(i)
													.getString("time").length() > 0 ? jsonArray
													.optJSONObject(i)
													.getString("time")
													: "now','+8 hours")
											+ "'),0,'',"
											+ ((jsonArray.optJSONObject(i)
													.getString("isring") == "43") ? "1"
													: "0")
											+ ","
											+ jsonArray.optJSONObject(i)
													.getString("projectid")
											+ ",'"
											+ jsonArray.optJSONObject(i)
													.getString("guid") + "');");
								}

							}
							mdb.db.setTransactionSuccessful();
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							mdb.db.endTransaction(); // 处理完成
						}
						mdb.CloseDb();
					} else {
						System.out.println(mjson.toString());
					}

					getTaskHandler.obtainMessage(2).sendToTarget();// 成功
				} else {
					System.out.println("未返回数据");

					getTaskHandler.obtainMessage(3).sendToTarget();// 失败
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				getTaskHandler.obtainMessage(3).sendToTarget();// 失败
			}

		}
	};
}
