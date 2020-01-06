package com.wztsise.xingji;

import com.example.webappdemo.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent.OnFinished;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnCheckedChangeListener,
		OnClickListener {
	private RadioGroup bottom_gruop;
	// fragment
	private Index indexf;
	private User_center user_center;
	private Xingji xingjif;
	private Jizhang jizhangf;
	// Button
	private RadioButton indexbt;
	private RadioButton userbt;
	private RadioButton xingjibt;
	private RadioButton jizhangbt;
	// actionbar
	private TextView actionbar_title;
	private ImageButton actionbar_return;
	private TextView actionbar_return1;
	private ImageButton actionbar_search;
	FragmentManager fragmentManager;
	public String userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bottom_tab);
		Intent intent = getIntent();
		userid = intent.getStringExtra("userid");
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(android.app.ActionBar.DISPLAY_SHOW_CUSTOM,
				android.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		actionBar.setCustomView(R.layout.my_actionbar);

		initUI();

	}

	private void initUI() {
		bottom_gruop = (RadioGroup) findViewById(R.id.radiogroup);
		bottom_gruop.setOnCheckedChangeListener(this);
		indexbt = (RadioButton) findViewById(R.id.radio1);
		xingjibt = (RadioButton) findViewById(R.id.radio2);
		jizhangbt = (RadioButton) findViewById(R.id.radio3);
		userbt = (RadioButton) findViewById(R.id.radio4);
		actionbar_title = (TextView) findViewById(R.id.actionbar_title);
		actionbar_return = (ImageButton) findViewById(R.id.actionbar_return);
		actionbar_return1 = (TextView) findViewById(R.id.actionbar_return1);
		actionbar_search = (ImageButton) findViewById(R.id.actionbar_search);
		actionbar_return1.setText("返回");
		actionbar_return.setOnClickListener(this);
		actionbar_return1.setOnClickListener(this);
		actionbar_search.setOnClickListener(this);
		int id = getIntent().getIntExtra("id", 0);
		if (id == 2) {
			bottom_gruop.check(R.id.radio2);
		} else if (id == 3) {
			bottom_gruop.check(R.id.radio3);
		} else if (id == 4) {
			bottom_gruop.check(R.id.radio4);
		} else {
			bottom_gruop.check(R.id.radio1);
		}

	}

	private void pressradio(String settile, RadioButton radiobtn,
			Object fragment) {
		actionbar_title.setText(settile);
		reTxtSelect();
		radiobtn.setSelected(true);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		fragmentManager = getFragmentManager();
		FragmentTransaction bTransaction = fragmentManager.beginTransaction();

		hideAllFragment(bTransaction);
		switch (checkedId) {
		case R.id.radio1: {
			pressradio("首页", indexbt, indexf);
			if (indexf == null) {
				indexf = new Index();
				bTransaction.add(R.id.frame, indexf);
			} else {
				bTransaction.show(indexf);
			}
			break;

		}

		case R.id.radio2: {
			Log.i("Tag", "点了行记");
			pressradio("行记", xingjibt, xingjif);
			if (xingjif == null) {
				xingjif = new Xingji();
				bTransaction.add(R.id.frame, xingjif);
			} else {
				bTransaction.show(xingjif);

			}
			break;

		}
		case R.id.radio3: {
			Log.i("Tag", "点了记账");
			pressradio("记账", jizhangbt, jizhangf);
			if (jizhangf == null) {
				jizhangf = new Jizhang();
				bTransaction.add(R.id.frame, jizhangf);
			} else {
				bTransaction.show(jizhangf);
			}
			break;
		}
		case R.id.radio4: {
			Log.i("Tag", "点了我");
			pressradio("我", userbt, user_center);
			if (user_center == null) {
				user_center = new User_center();
				bTransaction.add(R.id.frame, user_center);
			} else {
				bTransaction.show(user_center);
			}
			break;

		}

		}
		bTransaction.commit();
	}

	// 初始化底部菜单选择状态״̬
	private void reTxtSelect() {
		indexbt.setSelected(false);
		xingjibt.setSelected(false);
		userbt.setSelected(false);
		jizhangbt.setSelected(false);
	}

	private void hideAllFragment(FragmentTransaction bTransaction) {
		if (indexf != null) {
			bTransaction.hide(indexf);
		}
		if (xingjif != null) {
			bTransaction.hide(xingjif);
		}
		if (jizhangf != null) {
			bTransaction.hide(jizhangf);
		}
		if (user_center != null) {
			bTransaction.hide(user_center);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(1, 100, 1, "添加记录");
		menu.add(1, 101, 1, "记一笔");
		menu.add(1, 102, 1, "地图");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 100:
			Intent intent1 = new Intent(MainActivity.this, addrecord.class);
			intent1.putExtra("userid", userid);
			item.setIntent(intent1);
			break;
		case 101:
			Intent intent2 = new Intent(MainActivity.this, addcount.class);
			intent2.putExtra("userid", userid);
			item.setIntent(intent2);
			break;
		case 102:
			Intent intent4 = new Intent(MainActivity.this, mapActivity.class);
			startActivity(intent4);
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.actionbar_return:
			finish();
			break;
		case R.id.actionbar_return1:
			finish();
			break;
		case R.id.actionbar_search:
			Intent intent = new Intent(MainActivity.this, searchactivity.class);
			intent.putExtra("userid", userid);
			MainActivity.this.startActivity(intent);
			break;
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}

}
