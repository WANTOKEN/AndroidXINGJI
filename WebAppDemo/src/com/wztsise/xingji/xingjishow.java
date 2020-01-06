package com.wztsise.xingji;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.webappdemo.R;
import com.wztsise.util.ImageSet;
import com.wztsise.util.setTime;

public class xingjishow extends Activity {
	private String userid, re_id;
	private DBHelper helper;
	private TextView wxingjiuesrName, wxingjitime, wxingjitext, wxingji_locate,
			wxingji_del;
	private ImageView wxingjiuserImg, wxingji_pic1, wxingji_pic2, wxingji_pic3;
	String imgflag, pic1, pic2, pic3;
	String re_content;
	private Button sharebtn2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xingjishow);
		Intent intent = getIntent();
		userid = intent.getStringExtra("userid");
		re_id = intent.getStringExtra("re_id");
		initUI();
		initData();
	}

	private void initUI() {
		wxingjiuesrName = (TextView) findViewById(R.id.wxingjiuesrName);
		wxingjitime = (TextView) findViewById(R.id.wxingjitime);
		wxingjitext = (TextView) findViewById(R.id.wxingjitext);
		wxingji_locate = (TextView) findViewById(R.id.wxingji_locate);
		wxingji_del = (TextView) findViewById(R.id.wxingji_del);
		wxingjiuserImg = (ImageView) findViewById(R.id.wxingjiuserImg);
		wxingji_pic1 = (ImageView) findViewById(R.id.wxingji_pic1);
		wxingji_pic2 = (ImageView) findViewById(R.id.wxingji_pic2);
		wxingji_pic3 = (ImageView) findViewById(R.id.wxingji_pic3);
		sharebtn2 = (Button) findViewById(R.id.sharebtn2);
		sharebtn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OnekeyShare oneKeyShare = new OnekeyShare();
				// 设置标题
				oneKeyShare.setTitle("行记");
				oneKeyShare.setText(re_content);
				if (!pic1.equals("")) {
					Bitmap pic1bit = null;
					Uri pic1uri = Uri.parse(pic1);
					try {
						pic1bit = ImageSet.getBitmapFormUri(xingjishow.this,
								pic1uri);
					} catch (Exception e) {
						e.printStackTrace();
					}
					oneKeyShare.setImageData(pic1bit);
				}

				oneKeyShare.show(xingjishow.this);
			}
		});

		wxingji_pic1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showimg();
			}
		});
		wxingji_pic2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showimg();
			}
		});
		wxingji_pic3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showimg();
			}
		});
		wxingji_del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(
						xingjishow.this);
				builder.setMessage("真的要删除该记录吗？");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								helper = new DBHelper(xingjishow.this);// 数据库中删除
								helper.del_Jilu_Table(re_id);
								Toast.makeText(xingjishow.this, "删除成功",
										Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(xingjishow.this,
										MainActivity.class);
								intent.putExtra("id", 1);// 发送信号
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
		re_actionbar();
	}

	/*
	 * 自定义actionbar
	 */
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
		title.setText("行记");
		ImageButton actionbar_returnbt3 = (ImageButton) findViewById(R.id.actionbar_returnbt3);
		actionbar_returnbt3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void showimg() {
		Intent showimg = new Intent(xingjishow.this, imageshow.class);
		showimg.putExtra("re_pic1s", pic1);
		showimg.putExtra("re_pic2s", pic2);
		showimg.putExtra("re_pic3s", pic3);
		showimg.putExtra("piccount", imgflag);
		startActivity(showimg);
	}

	private void initData() {
		helper = new DBHelper(this);
		Cursor cursor = helper.query_Jilu_Table2(userid, re_id);

		while (cursor.moveToNext()) {
			String re_name = cursor.getString(cursor.getColumnIndex("us_name"));
			String re_time = cursor.getString(cursor.getColumnIndex("re_time"))
					.replace(setTime.getTime(), "今天");
			re_content = cursor.getString(cursor.getColumnIndex("re_content"));
			String re_locate = cursor.getString(cursor
					.getColumnIndex("re_locate"));
			String re_userimg = cursor.getString(cursor
					.getColumnIndex("us_img"));
			pic1 = cursor.getString(cursor.getColumnIndex("re_pic1"));
			pic2 = cursor.getString(cursor.getColumnIndex("re_pic2"));
			pic3 = cursor.getString(cursor.getColumnIndex("re_pic3"));
			wxingjiuesrName.setText(re_name);
			wxingjitime.setText(re_time);
			wxingjitext.setText(re_content);
			wxingji_locate.setText(re_locate);
			wxingjiuserImg.setImageURI(Uri.parse(re_userimg));
			if (pic1.equals("")) {
				wxingji_pic1.setVisibility(View.GONE);
				wxingji_pic2.setVisibility(View.GONE);
				wxingji_pic3.setVisibility(View.GONE);
				imgflag = "0";
			} else if (pic2.equals("")) {
				wxingji_pic1.setVisibility(View.VISIBLE);
				wxingji_pic2.setVisibility(View.GONE);
				wxingji_pic3.setVisibility(View.GONE);
				imgflag = "1";
			} else if (pic3.equals("")) {
				wxingji_pic1.setVisibility(View.VISIBLE);
				wxingji_pic2.setVisibility(View.VISIBLE);
				wxingji_pic3.setVisibility(View.GONE);
				imgflag = "2";
			} else {
				wxingji_pic1.setVisibility(View.VISIBLE);
				wxingji_pic2.setVisibility(View.VISIBLE);
				wxingji_pic3.setVisibility(View.VISIBLE);
				imgflag = "3";
			}
			wxingji_pic1.setImageURI(Uri.parse(pic1));
			wxingji_pic2.setImageURI(Uri.parse(pic2));
			wxingji_pic3.setImageURI(Uri.parse(pic3));

		}
		helper.close();
	}

}
