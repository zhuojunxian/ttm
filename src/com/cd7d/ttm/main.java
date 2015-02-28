package com.cd7d.ttm;


import org.json.JSONArray;
import org.json.JSONObject;

import com.cd7d.ttm.R;
import com.cd7d.ttm.dao.Db;
import com.cd7d.ttm.dao.HttpUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class main extends Activity {
	public float currentX = 40;
	public float currentY = 50;
	public Intent myint;
	private Thread mThread;
	ListView mainlout;
	Cursor cursor;
	Db mdb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mdb = new Db(this);
		
		//判断无数据时初始化数据库
		try {
			cursor = mdb.getCursor(getString(R.string.select_Table_Task));
		} catch (SQLiteException se) {
			mdb.RunSql(this.getString(R.string.create_Table_Task));
		}
		
		mainlout = (ListView) findViewById(R.id.mylist);
		ReFreshData();

		// 为ListView的列表项单击事件绑定事件监听器
		// mainlout.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View arg0, MotionEvent arg1) {
		// // TODO Auto-generated method stub
		// // 当前组件的currentX、currentY两个属性
		// currentX = arg1.getX();
		// System.out.println(arg1.getX() + "=." + arg1.getY() + "被单击了");
		//
		// // 返回true表明处理方法已经处理该事件
		// return false;
		// }
		//
		// });
		// 进入编辑页面
		mainlout.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				cursor.moveToPosition(position);
				myint = new Intent(main.this, Newtask.class);
				Bundle data = new Bundle();
				data.putSerializable("tid", cursor.getString(0));
				myint.putExtras(data);
				startActivityForResult(myint, 0);

				return true;
			}

		});
		//
		// 为ListView的列表项单击事件绑定事件监听器

		mainlout.setOnItemClickListener(new OnItemClickListener() {
			// 第position项被单击时激发该方法。
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				cursor.moveToPosition(position);
				myint = new Intent(main.this, Viewtask.class);
				Bundle data = new Bundle();
				data.putSerializable("tid", cursor.getString(0));
				myint.putExtras(data);
				startActivityForResult(myint, 0);

				System.out.println(this.toString() + "被单击了");
			}

		});

	}

	// 同步数据库数据线程
	Runnable runnable = new Runnable() {
		@Override
		public void run() {// run()在新的线程中运行
			try {

				String mvar = "1";
				// http://lz.86mt.com/ajax/login.ashx?username=zjx&password=831214
				mvar = HttpUtil.getRequest("http://lz.86mt.com/ajax/tasklist.ashx?username=zjx&loginkey=6B71B9B7-B2D5-4F96-B6C1-8790903A2435");
				JSONObject mjson = new JSONObject(mvar);
				System.out.println(mjson.getString("usertname"));
				System.out.println(mjson.getInt("userid"));
				if (mjson.getInt("userid") > 0) {

					JSONArray jsonArray = mjson.getJSONArray("taskdata");

					for (int i = 0; i < jsonArray.length(); i++) {

						mdb.execSQL("insert into task(name,description,edittime,ttype,percent,ttime,torder,tpic,isring)  values('"
								+ jsonArray.optJSONObject(i).getString("name")
								+ "','"
								+ jsonArray.optJSONObject(i).getString("note")
								+ "',datetime('"
								+ (jsonArray.optJSONObject(i).getString("time").length()>0?jsonArray.optJSONObject(i).getString("time"):"now','+8 hours")
								+ "'),1,"
								+ jsonArray.optJSONObject(i).getString("percent")
								+ ",datetime('"
								+ (jsonArray.optJSONObject(i).getString("time").length()>0?jsonArray.optJSONObject(i).getString("time"):"now','+8 hours")
								+ "'),0,'',1);");

					}

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

	// 同步数据库数据线程启动
	public void RefreshTask(View vw) {

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
				ReFreshData();
				break;
			case 1:
				System.out.println("线程数据读取失败");
				break;
			}
		}
	};

	// 刷新数据
	@SuppressLint("HandlerLeak")
	public void ReFreshData() {

		mainlout.setAdapter(new myadt());

	}

	// 进入当前页
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		ReFreshData();
	}

	// 释放内存
	@Override
	public void onDestroy() {
		super.onDestroy();
		mdb.CloseDb();
	}

	public void AddNewTask(View vw) {
		myint = new Intent(main.this, Newtask.class);

		startActivityForResult(myint, 0);
	}

	class myadt extends BaseAdapter {
		String[] listID;
		String[] listName;
		String[] listDesc;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			int ccount = 0;
			try {
	
				cursor = mdb.getCursor(getString(R.string.select_Table_Task));

				listID = new String[cursor.getCount()];
				listName = new String[cursor.getCount()];
				listDesc = new String[cursor.getCount()];
				while (cursor.moveToNext()) {
					listID[ccount] = cursor.getString(0);
					listName[ccount] = cursor.getString(1);
					listDesc[ccount] = cursor.getString(2);
					ccount = ccount + 1;
				}
			} catch (SQLiteException se) {

			}

			return ccount;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			if (arg1 == null) {
				arg1 = View.inflate(getApplicationContext(), R.layout.itemtask,
						null);
			}
			// ImageView miv = (ImageView) arg1.findViewById(R.id.imageView1);
			// miv.setImageResource(imgItem[arg0]);
			TextView mid = (TextView) arg1.findViewById(R.id.tid);
			mid.setText(listID[arg0]+"%");
			TextView mname = (TextView) arg1.findViewById(R.id.name);
			mname.setText(listName[arg0]);
			TextView mdesc = (TextView) arg1.findViewById(R.id.description);
			mdesc.setText(listDesc[arg0]);
			return arg1;
		};
	}
}
