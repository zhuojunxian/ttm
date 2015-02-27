package com.cd7d.ttm;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.widget.ListView;

public class Viewtask extends Activity {


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewtask);
		
		Intent intent=getIntent();
		String vtid=(String)intent.getSerializableExtra("tid");
		ListView mtsk=(ListView)findViewById(R.id.listView1); 
 
	com.cd7d.ttm.dao.task mtask=new  com.cd7d.ttm.dao.task(this);
	if(mtask!=null){
	mtask.GetData(vtid);
		mtsk.setAdapter(mtask);
	}
		

		
		
	}

 
}
