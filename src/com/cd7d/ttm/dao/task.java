package com.cd7d.ttm.dao;

 

import com.cd7d.ttm.Db;
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
	stringHolder[] holders;
	Cursor cursor; 
    Context context;
    Db mdb;
    public String vtid="0";
    private LayoutInflater layoutInflater; //得到一个LayoutInfalter对象用来导入布局
    
    public task(Context context) {  
        this.context = context;  
        this.layoutInflater = LayoutInflater.from(context);  
    } 
    
    public void GetData(String vid){
    	vtid=vid;
		if(vtid!=null){
		//编辑处理
	System.out.println("Bundle:"+vtid);
	

	mdb=new Db(this.context);
	
	cursor=mdb.getCursor("select  * from task where _id="+vtid+" ");
 
	holders=new stringHolder[cursor.getCount()];
	int holderi=0;
while(cursor.moveToNext()){
	holders[holderi]=new stringHolder();
	holders[holderi].title=cursor.getString(1);
	holders[holderi].content=cursor.getString(2);
 
	holderi++;
}
mdb.CloseDb();
	
	
	}
    }
    
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return holders.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// Log.v("BaseAdapterTest", "getView " + position + " " + convertView);
		ViewHolder holder=new ViewHolder();
		//组装数据
		if(convertView==null){
			convertView=layoutInflater.inflate(R.layout.task, null);
			holder.title=(TextView) convertView.findViewById(R.id.name);
			holder.content=(TextView) convertView.findViewById(R.id.description);
			if(holders.length>0){
				holder.title.setText(holders[position].title);
				holder.content.setText(holders[position].content);

			}else{
				holder.title.setText("test");
				holder.content.setText("test"+position);
			
			}
			//使用tag存储数据
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}


		return convertView;  
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


