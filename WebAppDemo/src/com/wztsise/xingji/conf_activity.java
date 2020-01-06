package com.wztsise.xingji;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webappdemo.R;

public class conf_activity extends Activity {
	private LinearLayout conf_1, conf_2;
	private String userid;
	private DBHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_activity);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(android.app.ActionBar.DISPLAY_SHOW_CUSTOM,
				android.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setCustomView(R.layout.my_actionbar3);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("设置");
		ImageButton actionbar_returnbt3 = (ImageButton) findViewById(R.id.actionbar_returnbt3);
		actionbar_returnbt3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Intent intent = getIntent();
		userid = intent.getStringExtra("userid");
		conf_1 = (LinearLayout) findViewById(R.id.conf_1);
		conf_2 = (LinearLayout) findViewById(R.id.conf_2);
		helper = new DBHelper(this);
		conf_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				helper.delTable_JL(userid);
				Toast.makeText(conf_activity.this, "清空数据成功！",
						Toast.LENGTH_SHORT).show();

			}
		});
		conf_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				helper.delTable_ZB(userid);
				Toast.makeText(conf_activity.this, "清空数据成功！",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onDestroy() {
		back();
		Log.i("info", "onDestroy()");
		super.onDestroy();
	}

	public void back() {
		Intent intent = new Intent(conf_activity.this, MainActivity.class);
		intent.putExtra("id", 4);
		intent.putExtra("userid", userid);
		startActivity(intent);
		finish();
	}
}
