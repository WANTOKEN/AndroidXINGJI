package com.wztsise.xingji;

import com.example.webappdemo.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnShowListener;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class login extends Activity {
	private EditText uid, upwd;// 账号、密码
	private Button loginbtn;// 登录按钮
	private TextView fpwd, register;// 忘记密码、注册
	private DBHelper helper;
	private String defaultimg;
	private Uri defaultimguri;
	SharedPreferences pref; // 保存用id
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initUI();
	}

	private void initUI() {
		uid = (EditText) findViewById(R.id.uid);
		upwd = (EditText) findViewById(R.id.pwd);
		loginbtn = (Button) findViewById(R.id.loginbtn);
		fpwd = (TextView) findViewById(R.id.fpwd);
		register = (TextView) findViewById(R.id.register);
		pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
		editor = pref.edit();
		String name = pref.getString("userName", "");
		uid.setText(name);
		register.setOnClickListener(listener);
		loginbtn.setOnClickListener(listener);
		fpwd.setOnClickListener(listener);
		defaultimguri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
				+ "://"
				+ getResources().getResourcePackageName(R.drawable.pic1) + "/"
				+ getResources().getResourceTypeName(R.drawable.pic1) + "/"
				+ getResources().getResourceEntryName(R.drawable.pic1));
		defaultimg = String.valueOf(defaultimguri);
	}

	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.loginbtn:
				login();
				break;
			case R.id.fpwd:
				Toast.makeText(login.this, "请联系下管理员哦！tel：18318565453",
						Toast.LENGTH_SHORT).show();
				break;
			case R.id.register:
				register();
				break;
			}
		}

	};

	/**
	 * 注册
	 */
	public void register() {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view = inflater.inflate(R.layout.registerdialog_layout, null);
		final EditText repwd = (EditText) view.findViewById(R.id.registerpwd);
		final EditText reid = (EditText) view.findViewById(R.id.registernum);
		double rand = Math.random();
		String str = String.valueOf(rand).replace("0.", "");
		String reids = str.substring(0, 11);// 11位随机数
		reid.setText(reids);
		reid.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				reid.setFocusable(true);
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
		builder.setTitle("您的新账号");// 设置标题
		builder.setView(view);
		builder.setPositiveButton("注册", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String newpwd = repwd.getText().toString();
				final String newid = reid.getText().toString();
				String usid = null;
				helper = new DBHelper(getApplicationContext());
				Cursor cursor = helper.query_User_Table(newid);
				while (cursor.moveToNext()) {
					usid = cursor.getString(cursor.getColumnIndex("user_id"));
				}
				if (!newid.equals(usid)) {
					if (!newpwd.equals("") && !newid.equals("")) {
						ContentValues values = new ContentValues();
						values.put("user_id", newid.toString());
						values.put("us_passwd", newpwd.toString());
						values.put("us_name", "uxv482");
						values.put("us_img", defaultimg);

						helper = new DBHelper(getApplicationContext());
						helper.insert_User_Table(values);
						helper.close();
						Toast.makeText(login.this, "您已成功注册！",
								Toast.LENGTH_SHORT).show();
						uid.setText(newid);
						upwd.setText(newpwd);
					} else {
						Toast.makeText(login.this, "要输账号和密码哦！",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(login.this, "该账号已经被注册了哦！",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(login.this, "您已取消！", Toast.LENGTH_SHORT).show();
			}
		});
		AlertDialog dialog = builder.create();// 获取dialog
		dialog.setOnShowListener(new OnShowListener() {
			public void onShow(DialogInterface dialog) {
				InputMethodManager inputManager = (InputMethodManager) repwd
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(repwd, 0);
			}
		});
		dialog.show();// 显示对话框
	}

	/**
	 * 登录
	 */
	private void login() {
		helper = new DBHelper(getApplicationContext());
		String userid = uid.getText().toString();
		String uesrpwd = upwd.getText().toString();
		String userpwd2 = null;
		String username = null;
		if (!userid.equals("") && !uesrpwd.equals("")) {
			Cursor cursor = helper.query_User_Table(userid);
			while (cursor.moveToNext()) {
				userpwd2 = cursor.getString(cursor.getColumnIndex("us_passwd"));
				username = cursor.getString(cursor.getColumnIndex("us_name"));
			}
			if (!uesrpwd.equals(userpwd2)) {
				Toast.makeText(login.this, "请检查账号密码哦！", Toast.LENGTH_SHORT)
						.show();
			} else {
				Intent intent = new Intent(login.this, MainActivity.class);
				intent.putExtra("userid", userid);
				intent.putExtra("username", username);
				editor.putString("userName", userid);// 记住userid
				editor.commit();
				startActivity(intent);
				finish();
			}
		} else {
			Toast.makeText(login.this, "请输入账号密码哦！", Toast.LENGTH_SHORT).show();
		}
		helper.close();
	}
}
