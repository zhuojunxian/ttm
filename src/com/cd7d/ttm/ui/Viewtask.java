package com.cd7d.ttm.ui;

import com.cd7d.ttm.R;
import com.cd7d.ttm.dao.Db;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

public class Viewtask extends Activity {
	public String vtid="0" ;
	public ListView mwok;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewtask);

		Intent intent = getIntent();
		 vtid = (String) intent.getSerializableExtra("tid");
		ListView mtsk = (ListView) findViewById(R.id.listView1);

		com.cd7d.ttm.dao.task mtask = new com.cd7d.ttm.dao.task(this);
		if (mtask != null) {
			mtask.GetData(vtid,"");
			mtsk.setAdapter(mtask);
		}
		 mwok = (ListView) findViewById(R.id.listVieww);

		com.cd7d.ttm.dao.work mwork = new com.cd7d.ttm.dao.work(this);
		if (mwork != null) {
			mwork.GetData(vtid);
			mwok.setAdapter(mwork);
		}
	}
	public void AddWork(View vi){
		

		EditText vanote = (EditText) findViewById(R.id.viewtaskAddworkNote);
			Spinner vapercent = (Spinner) findViewById(R.id.viewtaskAddworkPercent);
			AddWork(vtid,vanote.getText().toString(),vapercent.getSelectedItem().toString());
			
			com.cd7d.ttm.dao.work mwork = new com.cd7d.ttm.dao.work(this);
			if (mwork != null) {
				mwork.GetData(vtid);
				mwok.setAdapter(mwork);
			}
	}
	
	public void AddWork(String tid, String wnote, String wpercent) {
		// TODO Auto-generated method stub
		Db mdb=new Db(this);
		mdb.RunSql("insert into twork(tid,wtime,wnote,wpoint) values("+tid+",datetime('now','+8 hours'),'"+wnote.replace("'", "")+"',"+wpercent+")");
		System.out.println(tid+wpercent+wnote);
	}

}
