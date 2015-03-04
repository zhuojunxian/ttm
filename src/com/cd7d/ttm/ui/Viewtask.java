package com.cd7d.ttm.ui;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cd7d.ttm.R;
import com.cd7d.ttm.dao.Db;
import com.cd7d.ttm.dao.HttpUtil;
import com.cd7d.ttm.dao.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class Viewtask extends Activity {
	public String vtid="0" ;
	public ListView mwok;
	Db mdb = new Db(this);
	private Thread mThread;
	public task mtask;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewtask);

		Intent intent = getIntent();
		 vtid = (String) intent.getSerializableExtra("tid");
		ListView mtsk = (ListView) findViewById(R.id.listView1);

		  mtask = new task(this);
		if (mtask != null) {
			mtask.GetData(vtid,"");
			mtsk.setAdapter(mtask);
		}
		 mwok = (ListView) findViewById(R.id.listVieww);

			ReFresh();
			if (mdb.Islogin()) {
			if (mThread == null) {
				mThread = new Thread(runnable);
				mThread.start();
			}
			}
		
		 
	}
	public void AddWork(View vi){
		

		EditText vanote = (EditText) findViewById(R.id.viewtaskAddworkNote);
			Spinner vapercent = (Spinner) findViewById(R.id.viewtaskAddworkPercent);
			AddWork(vtid,vanote.getText().toString(),vapercent.getSelectedItem().toString());
			
			
			
			
	}
	
	public void AddWork(String tid, String wnote, String wpercent) {
		// TODO Auto-generated method stub
		mdb.RunSql("insert into twork(tid,wtime,wnote,wpoint,wtype,serverid) values("+tid+",datetime('now','+8 hours'),'"+wnote.replace("'", "")+"',"+wpercent+",1,0)");
		System.out.println(tid+wpercent+wnote);
		ReFresh() ;
	}
	
	public void ReFresh() {
		// TODO Auto-generated method stub
		com.cd7d.ttm.dao.work mwork = new com.cd7d.ttm.dao.work(this);
		if (mwork != null) {
			mwork.GetData(vtid);
			mwok.setAdapter(mwork);
		}
	}
	
	// 线程完成后处理UI
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {// 此方法在ui线程运行
			switch (msg.what) {
			case 0:
				ReFresh();
				
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
			 	mvar = HttpUtil
						.getRequest(getResources().getString(R.string.ServerUrl)+"/ajax/tasklist.ashx?username="+mdb.UserName+"&taskid="+mtask.vServerid+"&loginkey="+mdb.UserKey);
				JSONObject mjson = new JSONObject(mvar);
				System.out.println(mjson.getString("usertname"));
				System.out.println(mjson.getInt("userid"));
				if (mjson.getInt("userid") > 0) {

					JSONArray jsonArray = mjson.getJSONArray("taskdata");
					mdb.OpenDb();
					mdb.db.beginTransaction();  //手动设置开始事务
				        try{
					for (int i = 0; i < jsonArray.length(); i++) {

						
						 Cursor ckcursor = mdb.db.rawQuery("select  * from twork where serverid="+jsonArray.optJSONObject(i).getString("no"), null);

						if (ckcursor.getCount() != 0) {
							mdb.db.execSQL("update twork set "
									+ "wtime='"+ jsonArray.optJSONObject(i).getString("time")+ "',"
									+ "wnote='"+ jsonArray.optJSONObject(i).getString("note")+ "',"
									+ "wpoint='"+ jsonArray.optJSONObject(i).getString("percent")+ "',"
									+ "wtype="+ jsonArray.optJSONObject(i).getString("type")+ " "
									+ " where tid="+vtid+" and serverid="+jsonArray.optJSONObject(i).getString("no")+";");
						}else{
						
						mdb.db.execSQL("insert into twork(tid,wtime,wnote,wpoint,wtype,serverid)  values('"
								+ vtid
								+ "','"
								+ jsonArray.optJSONObject(i).getString("time")
								+ "','"
								+ jsonArray.optJSONObject(i).getString("note")
								+ "',"
								+ jsonArray.optJSONObject(i).getString(
										"percent")
								+ ","
								+ jsonArray.optJSONObject(i).getString(
										"type")
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
