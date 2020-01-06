package com.wztsise.xingji;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "xingji.db";// 数据库名称
	private static final String Jilu_Table = "JiLuTb";// 记录表名
	private static final String ZhBen_Table = "ZhBeTb";// 记录表名
	private static final String User_Table = "UserTb";// 用户表名
	private static final String Flag_Table = "FlagTb";// 定义标签表名
	private static final String Flag = "全部";// 定义标签名
	private static final String Flag2 = "全部记录";// 定义标签名
	private SQLiteDatabase db;

	public DBHelper(Context c) {
		super(c, DB_NAME, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db; // 获取SQLiteDatabase对象
		// 创建表
		// 记录表
		String CREATE_TAL = "create table "
				+ Jilu_Table
				+ "(re_id text primary key,re_time text,"
				+ "re_content text,re_locate text,re_pic1 text,re_pic2 text,re_pic3 text,re_flag text,re_userid text,"
				+ "foreign key(re_userid) references UserTb(user_id) on delete cascade on update cascade)";
		// 账本表
		String CREATE_TAL1 = "create table "
				+ ZhBen_Table
				+ "(zb_id text primary key,zb_type text,zb_time text,"
				+ "zb_content text,zb_count text,zb_flag text,zb_userid text,zb_pic1 text,zb_pic2 text,zb_pic3 text,"
				+ "foreign key(zb_userid) references UserTb(user_id) on delete cascade on update cascade)";
		// 用户表
		String CREATE_TAL2 = "create table " + User_Table
				+ "(user_id text primary key,us_name text,us_passwd text,"
				+ "us_img text)";
		// 标签表
		String CREATE_TAL3 = "create table "
				+ Flag_Table
				+ "(flag_id text primary key,flag_name text,flag_userid text,"
				+ "foreign key(flag_userid) references UserTb(user_id) on delete cascade on update cascade)";
		Log.i("INFO", "表创建");
		db.execSQL(CREATE_TAL);
		db.execSQL(CREATE_TAL1);
		db.execSQL(CREATE_TAL2);
		db.execSQL(CREATE_TAL3);
	}

	// 标签表插入
	public void insert_Flag_Table(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		db.insert(Flag_Table, null, values);
		Log.i("INFO", "标签表插入");
		db.close();
	}

	// 用户插入
	public void insert_User_Table(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		db.insert(User_Table, null, values);
		Log.i("INFO", "用户表插入");
		db.close();
	}

	// 记录插入
	public void insert_Jilu_Table(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		db.insert(Jilu_Table, null, values);
		Log.i("INFO", "记录表插入");
		db.close();
	}

	// 账本插入
	public void insert_ZhBen_Table(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		db.insert(ZhBen_Table, null, values);
		Log.i("INFO", "账本表插入");
		db.close();
	}

	// 用户校验
	public Cursor query_User_Table(String userid) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from UserTb where user_id=?",
				new String[] { String.valueOf(userid) });
		return cursor;
	}

	// 查询标签
	public Cursor query_Flag_Table(String userid) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + Flag_Table
				+ " where flag_userid=?",
				new String[] { String.valueOf(userid) });
		return cursor;
	}

	/*
	 * 主页查询记录
	 */
	public Cursor query_Index_JL(String userid, String offset) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select * from JiLuTb,UserTb where (JiLuTb.re_userid=UserTb.user_id) and JiLuTb.re_userid="
								+ userid
								+ " order by re_id desc limit 4 offset "
								+ offset, null);
		return cursor;
	}

	// 记录表查询
	public Cursor query_Jilu_Table1(String userid, String offset,
			String settime2, String flag) {
		Log.i("INFO", flag + "" + settime2);
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = null;
		if ((settime2.equals(Flag2)) && (flag.equals(Flag))) {
			Log.i("INFO", flag + "and" + settime2);
			cursor = db.rawQuery(
					"select * from JiLuTb,UserTb where (JiLuTb.re_userid=UserTb.user_id) "
							+ "and JiLuTb.re_userid=" + userid
							+ " order by re_id desc limit 5 offset " + offset,
					null);

		} else {
			if (flag.equals(Flag)) { // 全部记录
				cursor = db
						.rawQuery(
								"select * from JiLuTb,UserTb where (JiLuTb.re_userid=UserTb.user_id) and (JiLuTb.re_time like '%"
										+ settime2
										+ "%')"
										+ "and JiLuTb.re_userid="
										+ userid
										+ " order by re_id desc limit 5 offset "
										+ offset, null);

			} else if (settime2.equals(Flag2)) {
				cursor = db.rawQuery(
						"select * from JiLuTb,UserTb where (JiLuTb.re_userid=UserTb.user_id) "
								+ "and (JiLuTb.re_flag like '%" + flag + "%') "
								+ "and JiLuTb.re_userid=" + userid
								+ " order by re_id desc limit 5 offset "
								+ offset, null);

			} else {
				Log.i("INFO", flag);
				cursor = db
						.rawQuery(
								"select * from JiLuTb,UserTb where (JiLuTb.re_userid=UserTb.user_id) and (JiLuTb.re_time like '%"
										+ settime2
										+ "%') and (JiLuTb.re_flag like '%"
										+ flag
										+ "%') "
										+ "and JiLuTb.re_userid="
										+ userid
										+ " order by re_id desc limit 5 offset "
										+ offset, null);
			}
		}

		return cursor;
	}

	// 账本表查询 记账页
	public Cursor query_ZhBen_Table(String userid, String offset,
			String settime2) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = null;
		if (settime2.equals(Flag)) {
			cursor = db.rawQuery(
					"select * from ZhBeTb,UserTb where (ZhBeTb.zb_userid=UserTb.user_id) "
							+ "and ZhBeTb.zb_userid=" + userid
							+ " order by zb_id desc limit 10 offset " + offset,
					null);
		} else {
			cursor = db
					.rawQuery(
							"select * from ZhBeTb,UserTb where (ZhBeTb.zb_userid=UserTb.user_id) and (ZhBeTb.zb_time like '%"
									+ settime2
									+ "%') and ZhBeTb.zb_userid="
									+ userid
									+ " order by zb_id desc limit 10 offset "
									+ offset, null);
		}

		Log.i("INFO", "账本查询");
		return cursor;
	}

	/*
	 * 根据记录id查询单独
	 */
	public Cursor query_Jilu_Table2(String userid, String re_id) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select * from JiLuTb,UserTb where (JiLuTb.re_userid=UserTb.user_id) and JiLuTb.re_userid="
								+ userid + " and JiLuTb.re_id=" + re_id, null);
		return cursor;
	}

	/*
	 * 根据账本id查询单独
	 */
	public Cursor query_ZhBen_Table2(String userid, String zb_id) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select * from ZhBeTb,UserTb where (ZhBeTb.zb_userid=UserTb.user_id) and ZhBeTb.zb_userid="
								+ userid + " and ZhBeTb.zb_id=" + zb_id, null);
		return cursor;
	}

	/*
	 * 相册图片查询
	 */
	public Cursor query_Jilu_Table_Photos(String userid) {
		SQLiteDatabase db = getReadableDatabase();
		// Cursor cursor = db.query(Jilu_Table, null,null, null, null,null,
		// null);
		Cursor cursor = db.rawQuery("select * from JiLuTb,UserTb where "
				+ "(JiLuTb.re_userid=UserTb.user_id) and JiLuTb.re_userid="
				+ userid + " order by JiLuTb.re_userid desc", null);

		return cursor;
	}

	/*
	 * 账本相册图片查询
	 */
	public Cursor query_ZhBen_Table_Photos(String userid) {
		SQLiteDatabase db = getReadableDatabase();
		// Cursor cursor = db.query(Jilu_Table, null,null, null, null,null,
		// null);
		Cursor cursor = db.rawQuery("select * from " + ZhBen_Table
				+ ",UserTb where " + "(" + ZhBen_Table
				+ ".zb_userid=UserTb.user_id) and " + ZhBen_Table
				+ ".zb_userid=" + userid + " order by " + ZhBen_Table
				+ ".zb_userid desc", null);

		return cursor;
	}

	/*
	 * 主页显示
	 */
	public Cursor query_Index_ZB(String userid, String offset) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db
				.rawQuery(
						"select * from ZhBeTb,UserTb where (ZhBeTb.zb_userid=UserTb.user_id) and ZhBeTb.zb_userid="
								+ userid
								+ " order by zb_id desc limit 4 offset "
								+ offset, null);
		Log.i("INFO", "账本查询");
		return cursor;
	}

	// 搜索页账本
	public Cursor query_SearchZb(String userid, String msg) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from ZhBeTb,UserTb where "
				+ "(ZhBeTb.zb_userid=UserTb.user_id )"
				+ " and (ZhBeTb.zb_userid=" + userid + ") "
				+ "and (ZhBeTb.zb_type like '%" + msg
				+ "%' or ZhBeTb.zb_content like '%" + msg
				+ "%' or ZhBeTb.zb_time like '%" + msg + "%' )"
				+ " order by zb_id desc limit 10 ", null);
		Log.i("INFO", "账本查询");
		return cursor;
	}

	// 搜索页记录
	public Cursor query_SearchJl(String userid, String msg) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select re_userid,re_content,re_time,re_id from JiLuTb,UserTb where "
						+ "(JiLuTb.re_userid=UserTb.user_id )"
						+ " and (JiLuTb.re_userid=" + userid + ") "
						+ "and (JiLuTb.re_content like '%" + msg
						+ "%' or JiLuTb.re_time like '%" + msg + "%')"
						+ " order by re_userid desc limit 10 ", null);
		return cursor;
	}

	public void del_Jilu_Table(String re_id) {
		if (db == null) {
			db = getWritableDatabase();
			db.delete(Jilu_Table, "re_id=?",
					new String[] { String.valueOf(re_id) });
		}
	}

	public void del_ZhBen_Table(String zb_id) {
		if (db == null) {
			Log.i("INFO", "账本删除");
			db = getWritableDatabase();
			db.delete(ZhBen_Table, "zb_id=?",
					new String[] { String.valueOf(zb_id) });
		}
	}

	// 更新用户数据库
	public void update_User_Table(ContentValues values, String user_id) {
		SQLiteDatabase db = getWritableDatabase();
		db.update(User_Table, values, "user_id=?",
				new String[] { String.valueOf(user_id) });
		db.close();
	}

	// 更记录表的flag
	public void update_Jiluflag_Table(ContentValues values, String re_id) {
		SQLiteDatabase db = getWritableDatabase();
		db.update(Jilu_Table, values, "re_id=?",
				new String[] { String.valueOf(re_id) });
		db.close();
	}

	public void close() {
		if (db != null)
			db.close();
	}

	/*
	 * 删除所有记录
	 */
	public void delTable_JL(String user_id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(Jilu_Table, "re_userid=?",
				new String[] { String.valueOf(user_id) });
		db.close();
	}

	/*
	 * 删除所有记录
	 */
	public void delTable_ZB(String user_id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(ZhBen_Table, "zb_userid=?",
				new String[] { String.valueOf(user_id) });
		db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
