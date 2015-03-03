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
 

public class work extends BaseAdapter  {
	
//	stringHolder[] holders;
	Cursor cursor; 
   Context context;
    Db mdb;
    public String vtid="0";
   private LayoutInflater layoutInflater; //得到一个LayoutInfalter对象用来导入布局
//    
    
	String[] listID;
	String[] listNum;
	String[] listDate;
	String[] listDesc;
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int ccount = 0;
		if(listID!=null){
			ccount = listID.length;
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

	@SuppressLint("InflateParams") @Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			arg1 = layoutInflater.inflate( R.layout.itemwork,
					null);
		}
		// ImageView miv = (ImageView) arg1.findViewById(R.id.imageView1);
		// miv.setImageResource(imgItem[arg0]);
		TextView mid = (TextView) arg1.findViewById(R.id.wid);
		mid.setText(listID[arg0]+"%");
		TextView mpercent = (TextView) arg1.findViewById(R.id.workno);
		mpercent.setText(listNum[arg0]+"%");
		
		TextView mname = (TextView) arg1.findViewById(R.id.workDate);
		mname.setText(listDate[arg0]);
		TextView mdesc = (TextView) arg1.findViewById(R.id.workdesc);
		mdesc.setText(listDesc[arg0]);
		return arg1;
	};
	

    public work(Context context) {  
        this.context = context;  
        this.layoutInflater = LayoutInflater.from(context);  
    } 
    
    public void GetData(String vid){
    	vtid=vid;
		if(vtid!=null){
		//编辑处理
	System.out.println("Bundle:"+vtid);
	

	mdb=new Db(this.context);
	if(vtid.length()>0&&vtid!="0"){
	cursor=mdb.getCursor("select  * from twork where tid="+vtid+" order by _id desc");
	}else{
		//cursor = mdb.getCursor(context.getString(R.string.select_Table_Task));
	}
	int ccount = 0;

	listID = new String[cursor.getCount()];
	listNum = new String[cursor.getCount()];
	listDesc = new String[cursor.getCount()];
	listDate = new String[cursor.getCount()];
	while (cursor.moveToNext()) {
		listID[ccount] = cursor.getString(0);
		listNum[ccount] = cursor.getString(4);
		listDesc[ccount] = cursor.getString(3);
		listDate[ccount] = cursor.getString(2);
		ccount = ccount + 1;
	}
mdb.CloseDb();
	
	
	}
    }


}


