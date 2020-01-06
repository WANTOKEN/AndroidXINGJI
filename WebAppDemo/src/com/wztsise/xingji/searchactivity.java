package com.wztsise.xingji;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.webappdemo.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class searchactivity extends Activity implements OnClickListener {
	private ImageButton actionbat_returnbt2;
	private EditText actionbar_searchtext;
	private ImageButton actionbar_serachdel;
	private String userid;
	LinearLayout search1, search2;
	private DBHelper helper;
	private TextView t1, t2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		Intent intent = getIntent();
		userid = intent.getStringExtra("userid");
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(android.app.ActionBar.DISPLAY_SHOW_CUSTOM,
				android.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setCustomView(R.layout.my_actionbar2);
		search1 = (LinearLayout) findViewById(R.id.search1);
		search2 = (LinearLayout) findViewById(R.id.search2);
		initUI();
	}

	private void initUI() {
		actionbat_returnbt2 = (ImageButton) findViewById(R.id.actionbar_returnbt2);
		actionbar_searchtext = (EditText) findViewById(R.id.actionbar_searchtext);
		actionbar_serachdel = (ImageButton) findViewById(R.id.actionbar_searchdel);
		t1 = (TextView) findViewById(R.id.t1);
		t2 = (TextView) findViewById(R.id.t2);
		actionbat_returnbt2.setOnClickListener(this);
		actionbar_serachdel.setOnClickListener(this);
		actionbar_searchtext.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				search(); // 查询操作
			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.actionbar_returnbt2:
			finish();
			break;
		case R.id.actionbar_searchdel:
			actionbar_searchtext.setText("");
			t1.setText("");
			t2.setText("");
			break;
		}

	}

	/*
	 * 查询
	 */

	private void search() {
		search1.removeAllViews();// 清除所有子view
		search2.removeAllViews();
		String serachtext = actionbar_searchtext.getText().toString();
		int flag1 = 0, flag2 = 0;
		LayoutInflater inflater = getLayoutInflater();
		if (!serachtext.equals("")) {// 判断文本内容是否为空
			helper = new DBHelper(this);
			Cursor cursor1 = helper.query_SearchJl(userid, serachtext);
			while (cursor1.moveToNext()) {
				flag1++;
				Map<String, Object> map = new HashMap<String, Object>();
				final String re_id = cursor1.getString(cursor1
						.getColumnIndex("re_id"));
				map.put("re_content",
						cursor1.getString(cursor1.getColumnIndex("re_content")));
				map.put("re_time",
						cursor1.getString(cursor1.getColumnIndex("re_time")));
				View view1 = inflater.inflate(R.layout.search_item1, null);
				LinearLayout searchitem1 = (LinearLayout) view1
						.findViewById(R.id.searchitem1);
				searchitem1.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent showxingji = new Intent(searchactivity.this,
								xingjishow.class);
						showxingji.putExtra("userid", userid);
						showxingji.putExtra("re_id", re_id);
						startActivity(showxingji);
					}
				});
				TextView r_content = (TextView) view1
						.findViewById(R.id.jilucontent);
				TextView r_time = (TextView) view1.findViewById(R.id.jilutime);
				String j1 = (String) map.get("re_time");
				String j2 = (String) map.get("re_content");
				r_content.setText(j2);
				r_time.setText(j1);
				search1.addView(view1);

			}
			if (flag1 != 0) { // 判断是否有记录，0表示没有记录，那么就不要把标题显示出来
				t1.setVisibility(View.VISIBLE);// 将标题文本显示出来
				t1.setText("记录");// 设置文本标题
				flag1 = 0;
			} else {
				t1.setVisibility(View.GONE);
				flag1 = 0;
			}
			Cursor cursor2 = helper.query_SearchZb(userid, serachtext);
			while (cursor2.moveToNext()) {
				flag2++;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("zb_id",
						cursor2.getString(cursor2.getColumnIndex("zb_id")));
				map.put("zb_datetime",
						cursor2.getString(cursor2.getColumnIndex("zb_time")));
				map.put("zb_type",
						cursor2.getString(cursor2.getColumnIndex("zb_type")));
				map.put("zb_content",
						cursor2.getString(cursor2.getColumnIndex("zb_content")));
				map.put("zb_flag",
						cursor2.getString(cursor2.getColumnIndex("zb_flag")));
				if (cursor2.getString(cursor2.getColumnIndex("zb_flag"))
						.equals("收入")) {
					map.put("zb_count",
							"+"
									+ cursor2.getString(cursor2
											.getColumnIndex("zb_count")));
				} else if (cursor2.getString(cursor2.getColumnIndex("zb_flag"))
						.equals("支出")) {
					map.put("zb_count",
							"-"
									+ cursor2.getString(cursor2
											.getColumnIndex("zb_count")));
				}

				View view2 = inflater.inflate(R.layout.search_item2, null);
				LinearLayout searchitem2 = (LinearLayout) view2
						.findViewById(R.id.searchitem2);
				ImageView item2flag = (ImageView) view2
						.findViewById(R.id.item2flag);
				TextView z_content = (TextView) view2
						.findViewById(R.id.jizhangcontent);
				TextView z_type = (TextView) view2
						.findViewById(R.id.jizhangtype);
				TextView z_datetime = (TextView) view2
						.findViewById(R.id.jizhangdatetime);
				TextView z_count = (TextView) view2
						.findViewById(R.id.jizhangcount);
				final String zb_id = (String) map.get("zb_id");
				String z1 = (String) map.get("zb_content");
				String z2 = (String) map.get("zb_datetime");
				String z3 = (String) map.get("zb_type");
				String z4 = (String) map.get("zb_count");
				String zb_flag = (String) map.get("zb_flag");
				if (zb_flag.equals("收入")) {
					item2flag.setImageResource(R.drawable.zb_s);
				} else if (zb_flag.equals("支出")) {
					item2flag.setImageResource(R.drawable.zb_z);
				}
				searchitem2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent showzhangben = new Intent(searchactivity.this,
								zhangbenshow.class);
						showzhangben.putExtra("userid", userid);
						showzhangben.putExtra("zb_id", zb_id);
						startActivity(showzhangben);

					}
				});
				z_content.setText(z1);
				z_datetime.setText(z2);
				z_type.setText(z3);
				z_count.setText(z4);
				search2.addView(view2);
			}
			if (flag2 != 0) {
				t2.setVisibility(View.VISIBLE);
				t2.setText("账本");
				flag2 = 0;
			} else {
				t2.setVisibility(View.GONE);
				flag2 = 0;
			}
			helper.close();
		} else {
			t1.setText("");
			t2.setText("");
		}
	}

}
