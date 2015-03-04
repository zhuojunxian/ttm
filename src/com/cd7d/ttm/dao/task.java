package com.cd7d.ttm.dao;

import com.cd7d.ttm.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class task extends BaseAdapter {

	// stringHolder[] holders;
	Cursor cursor;
	Context context;
	Db mdb;
	public String vtid = "0";
	public String vServerid="0";
	private LayoutInflater layoutInflater; // 得到一个LayoutInfalter对象用来导入布局

	taskmod[] mlist; 
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int ccount = 0;
		if (mlist != null) {
			ccount = mlist.length;
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
		return arg0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			arg1 = layoutInflater.inflate(R.layout.itemtask, null);
		}
		
		TextView mid = (TextView) arg1.findViewById(R.id.tid);
		mid.setText(mlist[arg0].listID);
		
		TextView mpercent = (TextView) arg1.findViewById(R.id.tpercent);
	
if(mlist[arg0].listType.trim().equals("5")){	//进度显示百分比
	mpercent.setText(mlist[arg0].listPercent + "%");
}else{
	mpercent.setVisibility(View.GONE);
	ImageView mmubiao = (ImageView) arg1.findViewById(R.id.mubiao);
	mmubiao.setImageResource(R.drawable.ticno);
	System.out.println(mlist[arg0].listType);
}
 


		TextView mname = (TextView) arg1.findViewById(R.id.name);
		mname.setText(mlist[arg0].listName);
		TextView mdesc = (TextView) arg1.findViewById(R.id.description);
		mdesc.setText(mlist[arg0].listDesc);
		return arg1;
	};

	public task(Context context) {
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
	}

	public void GetData(String vid, String vtype) {
		vtid = vid;
		if (vtid != null) {
			// 编辑处理
			System.out.println("Bundle:" + vtid);

			String vsql = "";
			mdb = new Db(this.context);
			if (vtid.length() > 0 && vtid != "0") {
				vsql = "select  * from task where _id=" + vtid + " ";
			} else {
				if (vtype.length() > 0) {
					if(vtype.equals("1")){
					vsql = "select * from task where projectid=1 order by torder desc, _id desc";
					}else{
						vsql = "select * from task where projectid<>1 order by torder desc, _id desc";	
					}
				} else {
					vsql = "select * from task order by torder desc, _id desc";
				}
			}
			System.out.println(vsql);
			cursor = mdb.getCursor(vsql);
			int ccount = 0;

			mlist = new taskmod[cursor.getCount()];

			while (cursor.moveToNext()) {
				mlist[ccount]=new taskmod();
				mlist[ccount].listID = cursor.getString(0);
				mlist[ccount].listName = cursor.getString(1);				
				mlist[ccount].listDesc = cursor.getString(2);
				mlist[ccount].listType = cursor.getString(5);
				mlist[ccount].listProjectID = cursor.getString(12);
				mlist[ccount].listPercent = cursor.getString(6);
				mlist[ccount].listServerid = cursor.getString(13);
				if (vtid.length() > 0 && vtid != "0") {
					vServerid=cursor.getString(13);
				}
				ccount = ccount + 1;
			}
			mdb.CloseDb();

		}
	}

	public static class ViewHolder {
		TextView title;
		TextView content;
	}

	public static class stringHolder {
		String title;
		String content;
	}
}
