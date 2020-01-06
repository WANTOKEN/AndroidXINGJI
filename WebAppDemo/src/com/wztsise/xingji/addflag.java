package com.wztsise.xingji;

import com.example.webappdemo.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class addflag extends Activity {
	private EditText addflag;
	private ImageButton actionbar_returnbt_addrecord;
	private TextView actionbar_return_addrecord;
	private Button actionbar_ensurebtn_addrecord;
	private String userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addflag);
		Intent intent = getIntent();
		userid = intent.getStringExtra("userid");
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(android.app.ActionBar.DISPLAY_SHOW_CUSTOM,
				android.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setCustomView(R.layout.addrecord_actionbar);
		addflag = (EditText) findViewById(R.id.addflag);
		actionbar_returnbt_addrecord = (ImageButton) findViewById(R.id.actionbar_returnbt_addrecord);
		actionbar_return_addrecord = (TextView) findViewById(R.id.actionbar_return_addrecord);
		actionbar_ensurebtn_addrecord = (Button) findViewById(R.id.actionbar_ensurebtn_addrecord);
		actionbar_returnbt_addrecord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		actionbar_return_addrecord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		actionbar_ensurebtn_addrecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String flagtext = addflag.getText().toString().trim();
				if (!flagtext.equals("")) {
					DBHelper helper = new DBHelper(getApplicationContext());
					ContentValues values = new ContentValues();
					values.put("flag_userid", userid);
					values.put("flag_name", flagtext.toString().trim());
					helper.insert_Flag_Table(values);
					helper.close();
					Intent intent = new Intent(addflag.this, MainActivity.class);
					intent.putExtra("id", 2);// 发送信号
					intent.putExtra("userid", userid);// 发送用户信号
					startActivity(intent);
					finish();
					Toast.makeText(addflag.this, "添加成功", Toast.LENGTH_SHORT)
							.show();

				} else {
					Toast.makeText(addflag.this, "请填写标签内容哦~",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}
}
