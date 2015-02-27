package com.cd7d.ttm;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//���ݿ������
public class Db{
	public 	SQLiteDatabase db;
	public Context context;
	private Cursor cursor;
	public Db(Context main) {
		context=main;
		// TODO Auto-generated constructor stub
	}

	public Cursor getCursor(String vsql) {
		// TODO Auto-generated method stub
	
		OpenDb();
		cursor=db.rawQuery(vsql,null);
		//System.out.println(cursor.getCount());
		return cursor;
	}

	public void RunSql(String vsql) {
		OpenDb();
		db.execSQL(vsql);
	}
	// �����ݿ�
	public void OpenDb() {
		if (db == null || !db.isOpen()) {
			db = SQLiteDatabase.openOrCreateDatabase(this.context.getFilesDir()
					.toString() + "/"+this.context.getString(R.string.dbname), null);
		}
		
	}

	// �ر����ݿ�
	public void CloseDb() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

	public void execSQL(String vsql) {
		RunSql(vsql);
		
	}

}
