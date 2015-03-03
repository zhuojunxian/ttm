package com.cd7d.ttm.dao;

 

import com.cd7d.ttm.R;



import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 

public class task extends BaseAdapter  {
	
//	stringHolder[] holders;
	Cursor cursor; 
   Context context;
    Db mdb;
    public String vtid="0";
   private LayoutInflater layoutInflater; //得到一个LayoutInfalter对象用来导入布局
//    
    
	String[] listID;
	String[] listName;
	String[] listDesc;
	String[] listPercent;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int ccount = 0;
		if(listID!=null){
			ccount = listID.length;
		}
//		try {
//
//			cursor = mdb.getCursor(context.getString(R.string.select_Table_Task));
//
//			listID = new String[cursor.getCount()];
//			listName = new String[cursor.getCount()];
//			listDesc = new String[cursor.getCount()];
//			listPercent = new String[cursor.getCount()];
//			while (cursor.moveToNext()) {
//				listID[ccount] = cursor.getString(0);
//				listName[ccount] = cursor.getString(1);
//				listDesc[ccount] = cursor.getString(2);
//				listPercent[ccount] = cursor.getString(5);
//				ccount = ccount + 1;
//			}
//		} catch (SQLiteException se) {
//
//		}

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

	@SuppressLint("InflateParams") @Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			arg1 = layoutInflater.inflate( R.layout.itemtask,
					null);
		}
		// ImageView miv = (ImageView) arg1.findViewById(R.id.imageView1);
		// miv.setImageResource(imgItem[arg0]);
		TextView mid = (TextView) arg1.findViewById(R.id.tid);
		mid.setText(listID[arg0]);
		TextView mpercent = (TextView) arg1.findViewById(R.id.tpercent);
		mpercent.setText(listPercent[arg0]+"%");
		
		TextView mname = (TextView) arg1.findViewById(R.id.name);
		mname.setText(listName[arg0]);
		TextView mdesc = (TextView) arg1.findViewById(R.id.description);
		mdesc.setText(listDesc[arg0]);
		return arg1;
	};
	

    public task(Context context) {  
        this.context = context;  
        this.layoutInflater = LayoutInflater.from(context);  
    } 
    
    public void GetData(String vid,String vtype){
    	vtid=vid;
		if(vtid!=null){
		//编辑处理
	System.out.println("Bundle:"+vtid);
	
String vsql="";
	mdb=new Db(this.context);
	if(vtid.length()>0&&vtid!="0"){
		vsql="select  * from task where _id="+vtid+" ";
	}else{
		if(vtype.length()>0){
			vsql="select * from task where ttype="+vtype+" order by torder desc, _id desc";
	 	}else{
	 		vsql="select * from task order by torder desc, _id desc";
		}
	}
	System.out.println(vsql);
	cursor=mdb.getCursor(vsql);
	int ccount = 0;

	listID = new String[cursor.getCount()];
	listName = new String[cursor.getCount()];
	listDesc = new String[cursor.getCount()];
	listPercent = new String[cursor.getCount()];
	while (cursor.moveToNext()) {
		listID[ccount] = cursor.getString(0);
		listName[ccount] = cursor.getString(1);
		listDesc[ccount] = cursor.getString(2);
		listPercent[ccount] = cursor.getString(6);
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


