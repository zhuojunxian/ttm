package com.cd7d.ttm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.cd7d.ttm.R;
import com.cd7d.ttm.dao.Db;
import com.cd7d.ttm.ui.Newtask;
import com.cd7d.ttm.ui.Viewtask;
import com.cd7d.ttm.ui.Login;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class main extends Activity {
	public float currentX = 40;
	public float currentY = 50;
	public Intent myint;
	ListView mainlout;
	Cursor cursor;
	Db mdb;
	public int IsPopMenuShow = 0;
	List<Map<String, String>> moreList;
	private PopupWindow pwMyPopWindow;// popupwindow
	private ListView lvPopupList;// popupwindow中的ListView

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		iniPopupWindow();

		mdb = new Db(this);
		mdb.init();
 
		mainlout = (ListView) findViewById(R.id.mylist);
		ReFreshData();

		mainlout.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				TextView mname = (TextView) view.findViewById(R.id.tid);
				myint = new Intent(main.this, Newtask.class);
				Bundle data = new Bundle();
				data.putSerializable("tid", mname.getText().toString());
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
				TextView mname = (TextView) view.findViewById(R.id.tid);
				// cursor.moveToPosition(position);
				myint = new Intent(main.this, Viewtask.class);
				Bundle data = new Bundle();
				data.putSerializable("tid", mname.getText().toString());
				myint.putExtras(data);
				startActivityForResult(myint, 0);

				System.out.println(position + "被单击了" + mname.getText());
			}

		});

	}

	private void iniPopupWindow() {
		moreList = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		map = new HashMap<String, String>();
		map.put("share_key", "全部任务");
		moreList.add(map);
		map = new HashMap<String, String>();
		map.put("share_key", "个人任务");
		moreList.add(map);
		map = new HashMap<String, String>();
		map.put("share_key", "项目任务");
		moreList.add(map);
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.menu, null);
		lvPopupList = (ListView) layout.findViewById(R.id.lv_popup_list);
		pwMyPopWindow = new PopupWindow(layout, 200, 200);
		;
		pwMyPopWindow.setFocusable(true);// 加上这个popupwindow中的ListView才可以接收点击事件

		lvPopupList.setAdapter(new SimpleAdapter(main.this, moreList,
				R.layout.list_item_popupwindow, new String[] { "share_key" },
				new int[] { R.id.tv_list_item }));
		lvPopupList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (moreList.get(position).get("share_key") == "个人任务") {
					// Toast.makeText(main.this,
					// moreList.get(position).get("share_key"),
					// Toast.LENGTH_LONG).show();
					ReFreshData("", "1");
				} else if (moreList.get(position).get("share_key") == "项目任务") {
					ReFreshData("", "2");
				} else {
					ReFreshData("", "");
				}

				pwMyPopWindow.dismiss(); // ①

			}

		});

		// 控制popupwindow的宽度和高度自适应
		lvPopupList.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);
		// 控制popupwindow点击屏幕其他地方消失
		pwMyPopWindow.setBackgroundDrawable(this.getResources().getDrawable(
				R.drawable.cornerbg));// 设置背景图片，不能在布局中设置，要通过代码来设置
		pwMyPopWindow.setOutsideTouchable(true);// 触摸popupwindow外部，popupwindow消失。这个要求你的popupwindow要有背景图片才可以成功，如上
	}

	public void showPopMenu(View vi) {
		pwMyPopWindow.showAsDropDown(vi);
		// 将PopupWindow显示在指定位置
		pwMyPopWindow.showAtLocation(findViewById(R.id.popmenubt),
				Gravity.CENTER, 20, 20);
		IsPopMenuShow = 1;
	}

	// 同步数据库数据线程启动
	public void RefreshTask(View vw) {

	}



	// 刷新数据
	@SuppressLint("HandlerLeak")
	public void ReFreshData() {
		ReFreshData("", "");

	}

	public void ReFreshData(String vid, String vtype) {
		// TODO Auto-generated method stub
		com.cd7d.ttm.dao.task mtask = new com.cd7d.ttm.dao.task(this);
		if (mtask != null) {
			mtask.GetData(vid, vtype);

			mainlout.setAdapter(mtask);
		}

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

	public void Login(View vw) {
		myint = new Intent(main.this, Login.class);

		startActivityForResult(myint, 0);
	}
}
