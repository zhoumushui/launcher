package com.zms.launcher;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DbOperation {
	AppDbHelper dbhelper = null;
	SQLiteDatabase db = null;

	public DbOperation(Context context) {

		dbhelper = new AppDbHelper(context);

	}
	

	public void close() {

		dbhelper.close();
	}

	public void insertApp(App a) {
		db = dbhelper.getWritableDatabase();

		String sql = "insert into appTable(appName,appPack)values('"
				+ a.getName() + "','" + a.getPack() + "')";
		// String
		// sql="insert into appTable(appName,appPack)values('"+a.getName()+"','"+a.getTime()+"','"+a.getPack()+"')";
		db.execSQL(sql);
		db.close();

	}
//	public void deleteTable() {
//		db = dbhelper.getWritableDatabase();
//		String sql = "DROP TABLE IF EXISTS "+ "appTable";
//		db.execSQL(sql);
//
//		db.close();
//	}
	public void deleteTable(){
		db = dbhelper.getWritableDatabase();
		String sql = "delete from appTable";
		String sql2 = "DELETE FROM sqlite_sequence";
		db.execSQL(sql);
		db.execSQL(sql2);

		db.close();
	}
	public Long getCount() {
		  SQLiteDatabase db = dbhelper.getReadableDatabase();
		  Cursor cursor = db.rawQuery("select count(*)from appTable",null);
		  cursor.moveToFirst();
		  Long count = cursor.getLong(0);
		  cursor.close();
		  return count;
		 }

	public void changeName(String newName, String oldName) {
		db = dbhelper.getWritableDatabase();
		String sql = "update appTable set appName=? where appName=?";
		db.execSQL(sql, new String[] { newName, oldName });

		db.close();
	}

	public void insertTime(String appTime, String appPack) {
		db = dbhelper.getWritableDatabase();
		String sql = "update appTable set appTime=? where appPack=?";
		db.execSQL(sql, new String[] { appTime, appPack });
		db.close();
	}

	//调用queryAll()
	public List<App> query(String table) {

		List<App> appInfos = new ArrayList<App>();
		Cursor cursor = this.queryAll(table);
		while (cursor.moveToNext()) {
			App appinfo = new App();
			appinfo.setName(cursor.getString(1));

			appinfo.setPack(cursor.getString(2));
			appInfos.add(appinfo);
		}
		cursor.close();
		db.close();
		return appInfos;
	}
	
	//调用queryAllByName()
	public List<App> queryByName(String table) {

		List<App> appInfos = new ArrayList<App>();
		Cursor cursor = this.queryAllByName(table);
		while (cursor.moveToNext()) {
			App appinfo = new App();
			appinfo.setName(cursor.getString(1));

			appinfo.setPack(cursor.getString(2));
			appInfos.add(appinfo);
		}
		cursor.close();
		db.close();
		return appInfos;
	}
	
	//调用queryAllByTime
	public List<App> queryByTime(String table) {

		List<App> appInfos = new ArrayList<App>();
		Cursor cursor = this.queryAllByTime(table);
		while (cursor.moveToNext()) {
			App appinfo = new App();
			appinfo.setName(cursor.getString(1));

			appinfo.setPack(cursor.getString(2));
			appInfos.add(appinfo);
		}
		cursor.close();
		db.close();
		return appInfos;
	}
	

	
	

	// 根据数据库表名获取行
	public Cursor queryAll(String table) {
		db = dbhelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from " + table, null);
		return cursor;
	}
	

	//按应用名称排序
	public Cursor queryAllByName(String table) {
		db = dbhelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from " + table
				+ " order by appName", null);
		return cursor;
	}

	//按安装时间排序
	public Cursor queryAllByTime(String table) {
		db = dbhelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from " + table
				+ " order by appTime", null);
		return cursor;
	}

	public void appPackQuery(int i) {
		db = dbhelper.getWritableDatabase();
		//String sql ="select appPack from appTable where id =?";
		db.execSQL("select appPack from appTable where id = "+i);


	}
	
	
	// 将bitmap转换为byte[]

	public void deleteApp(String appPack) {
		db = dbhelper.getWritableDatabase();
		String sql = "delete from appTable where appPack=?";
		db.execSQL(sql, new String[] { appPack });
		db.close();

	}

}