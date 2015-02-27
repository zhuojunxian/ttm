package com.cd7d.ttm;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TimePicker;

public class Newtask extends Activity {
	EditText tname, tDesc;
	Spinner ttype;
	CheckBox tisRing;
	SQLiteDatabase db;
	Cursor cursor;
	TableLayout mtl;
	LinearLayout mt2;
	EditText mydatetime;
	DatePicker tdate;
	TimePicker ttime;
	public String vtid = "0";
	int year;
	int monthOfYear;
	int dayOfMonth;
	int hour;
	int minute;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newtask);

		tdate = (DatePicker) findViewById(R.id.datePicker1);
		ttime = (TimePicker) findViewById(R.id.timePicker1);
		mtl = (TableLayout) findViewById(R.id.contentEr);
		mt2 = (LinearLayout) findViewById(R.id.myDatePicker);
		mydatetime = (EditText) findViewById(R.id.tdatetime);
		tname = (EditText) findViewById(R.id.newtaskname);
		tDesc = (EditText) findViewById(R.id.newtaskdesc);
		ttype = (Spinner) findViewById(R.id.spinner1);
		tisRing = (CheckBox) findViewById(R.id.checkBox1);

		Intent intent = getIntent();
		vtid = (String) intent.getSerializableExtra("tid");
		if (vtid != null) {
			// 编辑处理
			OpenDb();
			cursor = db.rawQuery("select  * from task where _id=" + vtid, null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();

				tname.setText(cursor.getString(1));
				tDesc.setText(cursor.getString(2));

				mydatetime.setText(cursor.getString(7));
				if (cursor.getString(7) != null
						&& cursor.getString(7).length() > 7) {

			
					year = strToDateLong(cursor.getString(7)).getYear() + 1900;
					monthOfYear = strToDateLong(cursor.getString(7)).getMonth();
					dayOfMonth = strToDateLong(cursor.getString(7)).getDay() + 1;
					hour = strToDateLong(cursor.getString(7)).getHours();
					minute = strToDateLong(cursor.getString(7)).getMinutes();
					tdate.init(year, monthOfYear, dayOfMonth, null);
					ttime.setCurrentHour(hour);
					ttime.setCurrentMinute(minute);
				}
				System.out.println("o1:"+cursor.getInt(1));
				System.out.println("o5:"+cursor.getInt(5));
				if(cursor.getString(5) != null&&cursor.getInt(5)>0){
					System.out.println("6:"+cursor.getInt(5));
				ttype.setSelection(cursor.getInt(5)-1);
				}
				System.out.println("o10:"+cursor.getInt(10));
				if(cursor.getString(10) != null&&cursor.getInt(10)==1){
					System.out.println("10:"+cursor.getInt(10));
					tisRing.setChecked(true);
				}
			}

			CloseDb();
		}

		mydatetime.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// 此处为得到焦点时的处理内容
					System.out.println("被ShowDatePicker");

					Newtask.this.year = tdate.getYear();
					Newtask.this.monthOfYear = tdate.getMonth();
					Newtask.this.dayOfMonth = tdate.getDayOfMonth();
					Newtask.this.hour = ttime.getCurrentHour();
					Newtask.this.minute = ttime.getCurrentMinute();
					mtl.setVisibility(View.VISIBLE);
					mt2.setVisibility(View.GONE);
					mydatetime.setText(Newtask.this.year + "-"
							+ (Newtask.this.monthOfYear + 1) + "-"
							+ Newtask.this.dayOfMonth + " " + Newtask.this.hour
							+ ":" + Newtask.this.minute + ":00");

					mtl.setVisibility(View.GONE);
					mt2.setVisibility(View.VISIBLE);
				} else {
					// 此处为失去焦点时的处理内容
				}
			}
		});

	}

	@SuppressLint("SimpleDateFormat")
	public static Date strToDateLong(String strDate) {
		Date strtodate = null;
		strtodate = new Date();
		if (strDate.length() > 0) {
			SimpleDateFormat formatter = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			ParsePosition pos = new ParsePosition(0);
			strtodate = formatter.parse(strDate, pos);
		}
		return strtodate;
	}

	public void Picked(View vw) {

		Newtask.this.year = tdate.getYear();
		Newtask.this.monthOfYear = tdate.getMonth();
		Newtask.this.dayOfMonth = tdate.getDayOfMonth();
		Newtask.this.hour = ttime.getCurrentHour();
		Newtask.this.minute = ttime.getCurrentMinute();
		mtl.setVisibility(View.VISIBLE);
		mt2.setVisibility(View.GONE);
		mydatetime.setText(Newtask.this.year + "-"
				+ (Newtask.this.monthOfYear + 1) + "-"
				+ Newtask.this.dayOfMonth + " " + Newtask.this.hour + ":"
				+ Newtask.this.minute + ":00");

	}

	public void Save(View vw) {

		db = SQLiteDatabase.openOrCreateDatabase(Newtask.this.getFilesDir()
				.toString() + "/ttm.db3", null);
		tname = (EditText) findViewById(R.id.newtaskname);
		tDesc = (EditText) findViewById(R.id.newtaskdesc);
		ttype = (Spinner) findViewById(R.id.spinner1);
		tisRing = (CheckBox) findViewById(R.id.checkBox1);

		// TODO Auto-generated method stub
		String vname = tname.getText().toString();
		String vdesc = tDesc.getText().toString();
		String vdatetime = mydatetime.getText().toString();
		System.out.print(vdatetime);
		String tisRingvar = "0";

		int avail = 1;
		switch (ttype.getSelectedItemPosition()) {
		case 0:
			avail = 1;
			break;
		case 1:
			avail = 2;
			break;
		case 2:
			avail = 3;
			break;
		case 3:
			avail = 4;
			break;
		case 4:
			avail = 5;
			break;
		default:
			avail = 1;
			break;
		}

		if (tisRing.isChecked()) {
			tisRingvar = "1";
		} else {
			tisRingvar = "0";
		}
		if (vtid != null) {
			// 编辑处理	
			try {
				// System.out.println("insert into task(name,description,edittime,ttype,percent,ttime,torder,tpic,isring) values('"+vname+"','"+vdesc+"',datetime('now','+8 hours'),"+avail+",0,datetime('"+vdatetime+"'),0,'',"+tisRingvar+");");
				db.execSQL("update task set "
						+ " name='"+ vname+ "',"
						+ "description='"+ vdesc+ "',"
						+ "edittime=datetime('now','+8 hours'),"
						+"ttype="+avail+","
						+ "ttime='"+ vdatetime+ "',"
						+ "isring="+ tisRingvar
						+ "  where _id=" + vtid);

				finish();
			} catch (Exception e) {
				System.out.println(e.toString() );
			}
			
		} else {
			try {
				// System.out.println("insert into task(name,description,edittime,ttype,percent,ttime,torder,tpic,isring) values('"+vname+"','"+vdesc+"',datetime('now','+8 hours'),"+avail+",0,datetime('"+vdatetime+"'),0,'',"+tisRingvar+");");
				db.execSQL("insert into task(name,description,edittime,ttype,percent,ttime,torder,tpic,isring) values('"
						+ vname
						+ "','"
						+ vdesc
						+ "',datetime('now','+8 hours'),"
						+ avail
						+ ",0,'"
						+ vdatetime
						+ "',0,'',"
						+ tisRingvar
						+ ");");
				finish();
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
	}

	public void GoBack(View vw) {

		finish();
	}

	// 打开数据库
	private void OpenDb() {
		if (db == null || !db.isOpen()) {
			db = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir()
					.toString() + "/" + this.getString(R.string.dbname), null);
		}

	}

	// 关闭数据库
	private void CloseDb() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}
}
