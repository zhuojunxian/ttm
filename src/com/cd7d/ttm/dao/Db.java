package com.cd7d.ttm.dao;
import com.cd7d.ttm.R;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
//数据库操作类
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
	// 打开数据库
	public void OpenDb() {
		if (db == null || !db.isOpen()) {
			db = SQLiteDatabase.openOrCreateDatabase(this.context.getFilesDir()
					.toString() + "/"+this.context.getString(R.string.dbname), null);
		}
		
	}

	// 关闭数据库
	public void CloseDb() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

	public void execSQL(String vsql) {
		RunSql(vsql);
		
	}
	
   public void init(){
    	if(!exits("task")){
    		RunSql("create table task"
    				+ "(_id integer primary key autoincrement,"//自动编号
    				+ "name varchar(50),description varchar(250),"//任务名称
    				+ "edittime datetime,"//
    				+ "begintime datetime,"//
    				+ "ttype integer,"//
    				+ "percent integer,"//
    				+ "ttime datetime,"
    				+ "torder integer,"
    				+ "tpic varchar(250),"
    				+ "isring integer,"
    				+ "overtime datetime,"
    				+ "sync integer);  ");
    	}
if(!exits("twork")){
    		RunSql("  create table twork(_id integer primary key autoincrement,tid integer,wtime datetime,wnote varchar(250),wpoint integer,wtype integer); ");
    	}
    }
    public boolean exits(String table){
    	boolean exits = false;
    	String sql = "select * from sqlite_master where name="+"'"+table+"'";
    	Cursor cursor = getCursor(sql);

    	if(cursor.getCount()!=0){
    	exits = true;
    	}
    	return exits;
    	}
}
