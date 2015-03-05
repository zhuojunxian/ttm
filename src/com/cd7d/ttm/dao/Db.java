package com.cd7d.ttm.dao;

import com.cd7d.ttm.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//数据库操作类
public class Db {
	public SQLiteDatabase db;
	public Context context;
	private Cursor cursor;
	public String UserName = "";
	public String UserKey = "";

	public Db(Context main) {
		context = main;
		// TODO Auto-generated constructor stub
	}

	public Cursor getCursor(String vsql) {
		// TODO Auto-generated method stub

		OpenDb();
		cursor = db.rawQuery(vsql, null);
		// System.out.println(cursor.getCount());
		return cursor;
	}

	public void RunSql(String vsql) {
		OpenDb();
		db.execSQL(vsql);
	}

	// 打开数据库
	public void OpenDb() {
		if (db == null || !db.isOpen()) {
			db = SQLiteDatabase.openOrCreateDatabase(
					this.context.getFilesDir().toString() + "/"
							+ this.context.getString(R.string.dbname), null);
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

	// 初始化数据库
	public void init() {
		//用户信息
		if (!exits("userinfo")) {
			RunSql("create table userinfo"
					+ "(_id integer primary key autoincrement,"// 自动编号
					+ "username varchar(50),"// 用户名
					+ "userpass varchar(50),"// 密码
					+ "userkey varchar(50),"// 临时码
					+ "mobile varchar(50),"// 手机号
					+ "truename varchar(50));  ");// 真实姓名

			RunSql("insert into userinfo(username) values('');");// 真实姓名
		}
		//任务表
		if (!exits("task")) {
			RunSql("create table task"
					+ "(_id integer primary key autoincrement,"// 自动编号
					+ "name varchar(50),"
					+ "description varchar(250),"// 任务名称
					+ "edittime datetime,"// 编辑时间
					+ "begintime datetime,"// 开始时间
					+ "ttype integer,"// 类型 1单次，2每日，3每周，4每月，5进度
					+ "percent integer,"// 完成百分比
					+ "ttime datetime,"// 相关时间
					+ "torder integer,"// 排度值，暂时没用
					+ "tpic varchar(250),"// 相关图片
					+ "isring integer,"// 是否提醒
					+ "overtime datetime,"// 完成时间
					+ "projectid integer,"// 项目ID
					+ "serverid integer);  ");// 服务器ID
		}
		//工作记录表
		if (!exits("twork")) {
			RunSql("  create table twork("
					+ "_id integer primary key autoincrement," + "tid integer,"
					+ "wtime datetime," + "wnote varchar(250),"
					+ "wpoint integer," + "wtype integer,"
					+ "serverid integer); ");
		}
	}

	public boolean exits(String table) {
		boolean exits = false;
		String sql = "select * from sqlite_master where name=" + "'" + table
				+ "'";
		Cursor cursor = getCursor(sql);

		if (cursor.getCount() != 0) {
			exits = true;
		}
		return exits;
	}

	public boolean Islogin() {
		// TODO Auto-generated method stub
		boolean exits = false;
		String sql = "select  * from userinfo where length(username)>0 and length(userkey)>30 limit  1;";
		Cursor cursor = getCursor(sql);

		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			UserName = cursor.getString(1);
			UserKey = cursor.getString(3);
			exits = true;
		}
		return exits;
	}

	public String NoInjSql(String var) {
		// TODO Auto-generated method stub
		String revar = "";
		if (var != null && var.length() > 0) {
			revar = var.replace("'", "");
		}
		return revar;
	}
}
