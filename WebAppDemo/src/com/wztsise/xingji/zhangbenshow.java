package com.wztsise.xingji;

import com.example.webappdemo.R;
import com.wztsise.util.setTime;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class zhangbenshow extends Activity {
	private String userid, zb_id;
	private DBHelper helper;
	private TextView zhangbenuesrName, zhangbentime, zhangbentext,
			zhangben_type, zhangben_count, zhangben_del;
	private ImageView zhangbenflag, wzhangben_pic1, wzhangben_pic2,
			wzhangben_pic3;
	String imgflag, pic1, pic2, pic3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhangbenshow);
		Intent intent = getIntent();
		userid = intent.getStringExtra("userid");
		zb_id = intent.getStringExtra("zb_id");
		initUI();
		initData();
	}

	private void initUI() {
		re_actionbar();
		zhangbenuesrName = (TextView) findViewById(R.id.zhangbenuesrName);
		zhangbentime = (TextView) findViewById(R.id.zhangbentime);
		zhangbentext = (TextView) findViewById(R.id.zhangbentext);
		zhangben_type = (TextView) findViewById(R.id.zhangben_type);
		zhangben_count = (TextView) findViewById(R.id.zhangben_count);
		zhangben_del = (TextView) findViewById(R.id.zhangben_del);
		zhangbenflag = (ImageView) findViewById(R.id.zhangbenflag);
		wzhangben_pic1 = (ImageView) findViewById(R.id.wzhangben_pic1);
		wzhangben_pic2 = (ImageView) findViewById(R.id.wzhangben_pic2);
		wzhangben_pic3 = (ImageView) findViewById(R.id.wzhangben_pic3);
		wzhangben_pic1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showimg();
			}
		});
		wzhangben_pic2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showimg();
			}
		});
		wzhangben_pic3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showimg();
			}
		});
		zhangben_del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(
						zhangbenshow.this);
				builder.setMessage("真的要删除该记录吗？");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								helper = new DBHelper(zhangbenshow.this);// 数据库中删除
								helper.del_ZhBen_Table(zb_id);
								Toast.makeText(zhangbenshow.this, "删除成功",
										Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(zhangbenshow.this,
										MainActivity.class);
								intent.putExtra("id", 3);// 发送信号
								intent.putExtra("userid", userid);// 发送信号
								startActivity(intent);
								finish();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();
				helper.close();
			}
		});
	}

	private void re_actionbar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(android.app.ActionBar.DISPLAY_SHOW_CUSTOM,
				android.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setCustomView(R.layout.my_actionbar3);
		TextView title = (TextView) findViewById(R.id.title);
		title.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		title.setText("账本");
		ImageButton actionbar_returnbt3 = (ImageButton) findViewById(R.id.actionbar_returnbt3);
		actionbar_returnbt3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void showimg() {
		Intent showimg = new Intent(zhangbenshow.this, imageshow.class);
		showimg.putExtra("re_pic1s", pic1);
		showimg.putExtra("re_pic2s", pic2);
		showimg.putExtra("re_pic3s", pic3);
		showimg.putExtra("piccount", imgflag);
		startActivity(showimg);
	}

	private void initData() {
		helper = new DBHelper(this);
		Cursor cursor = helper.query_ZhBen_Table2(userid, zb_id);

		while (cursor.moveToNext()) {
			// String re_id = cursor.getString(cursor.getColumnIndex("re_id"));
			String zb_name = cursor.getString(cursor.getColumnIndex("us_name"));
			String zb_time = cursor.getString(cursor.getColumnIndex("zb_time"))
					.replace(setTime.getTime(), "今天");
			String zb_content = cursor.getString(cursor
					.getColumnIndex("zb_content"));
			String zb_type = cursor.getString(cursor.getColumnIndex("zb_type"));
			String zb_count = "";
			pic1 = cursor.getString(cursor.getColumnIndex("zb_pic1"));
			pic2 = cursor.getString(cursor.getColumnIndex("zb_pic2"));
			pic3 = cursor.getString(cursor.getColumnIndex("zb_pic3"));
			if (cursor.getString(cursor.getColumnIndex("zb_flag")).equals("收入")) {
				zb_count = "+"
						+ cursor.getString(cursor.getColumnIndex("zb_count"));
				zhangbenflag.setImageResource(R.drawable.zb_s);
			} else if (cursor.getString(cursor.getColumnIndex("zb_flag"))
					.equals("支出")) {
				zb_count = "-"
						+ cursor.getString(cursor.getColumnIndex("zb_count"));
				zhangbenflag.setImageResource(R.drawable.zb_z);
			}
			zhangbenuesrName.setText(zb_name);
			zhangbentime.setText(zb_time);
			zhangbentext.setText(zb_content);
			zhangben_type.setText(zb_type + ":");
			zhangben_count.setText(zb_count);
			if (pic1.equals("")) {
				wzhangben_pic1.setVisibility(View.GONE);
				wzhangben_pic2.setVisibility(View.GONE);
				wzhangben_pic3.setVisibility(View.GONE);
				imgflag = "0";
			} else if (pic2.equals("")) {
				wzhangben_pic1.setVisibility(View.VISIBLE);
				wzhangben_pic2.setVisibility(View.GONE);
				wzhangben_pic3.setVisibility(View.GONE);
				imgflag = "1";
			} else if (pic3.equals("")) {
				wzhangben_pic1.setVisibility(View.VISIBLE);
				wzhangben_pic2.setVisibility(View.VISIBLE);
				wzhangben_pic3.setVisibility(View.GONE);
				imgflag = "2";
			} else {
				wzhangben_pic1.setVisibility(View.VISIBLE);
				wzhangben_pic2.setVisibility(View.VISIBLE);
				wzhangben_pic3.setVisibility(View.VISIBLE);
				imgflag = "3";
			}
			wzhangben_pic1.setImageURI(Uri.parse(pic1));
			wzhangben_pic2.setImageURI(Uri.parse(pic2));
			wzhangben_pic3.setImageURI(Uri.parse(pic3));

		}
		helper.close();
	}

}
