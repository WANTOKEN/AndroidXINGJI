package com.wztsise.xingji;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.webappdemo.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnShowListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class userinfo extends Activity {
	private static final int REQUEST_CODE_PICK_IMAGE = 1; // 相册
	private static final int REQUEST_IMAGE_CAPTURE_FULL = 2;// 相机
	private String path = Environment.getExternalStorageDirectory()
			+ File.separator + Environment.DIRECTORY_DCIM + File.separator;
	public static String DIRECTORY_DCIM = "DCIM";
	File photoFile;// 相机文件
	Uri photoUri;// 相机
	private String updateImguri;// 要存入数据库中的URI
	private String uspwd;// 密码
	LinearLayout info_1, info_2, info_3, info_4;
	private ImageView info_img;
	private TextView info_name, info_id;
	private DBHelper helper;
	private String userid;
	private TextView actionbar_return_info;
	private Button actionbar_ensurebtn_info;
	private ImageButton actionbar_returnbt_info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info);
		Intent intent = getIntent();
		userid = intent.getStringExtra("userid");
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(android.app.ActionBar.DISPLAY_SHOW_CUSTOM,
				android.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setCustomView(R.layout.userinfo_actionbar);

		initUI();
		initDate();
	}

	private void initDate() {
		use_query();

	}

	void use_query() {
		helper = new DBHelper(this);
		Cursor cursor = helper.query_User_Table(userid);
		while (cursor.moveToNext()) {
			updateImguri = cursor.getString(cursor.getColumnIndex("us_img"));
			uspwd = cursor.getString(cursor.getColumnIndex("us_passwd"));
			info_name
					.setText(cursor.getString(cursor.getColumnIndex("us_name")));
			info_id.setText(cursor.getString(cursor.getColumnIndex("user_id")));
			info_img.setImageURI(Uri.parse(updateImguri));

		}
		helper.close();
	}

	private void initUI() {
		actionbar_returnbt_info = (ImageButton) findViewById(R.id.actionbar_returnbt_info);
		actionbar_return_info = (TextView) findViewById(R.id.actionbar_return_info);
		actionbar_ensurebtn_info = (Button) findViewById(R.id.actionbar_ensurebtn_info);
		info_1 = (LinearLayout) findViewById(R.id.info_1);
		info_2 = (LinearLayout) findViewById(R.id.info_2);
		info_3 = (LinearLayout) findViewById(R.id.info_3);
		info_4 = (LinearLayout) findViewById(R.id.info_4);
		info_img = (ImageView) findViewById(R.id.info_img);
		info_name = (TextView) findViewById(R.id.info_name);
		info_id = (TextView) findViewById(R.id.info_id);
		info_1.setOnClickListener(listener);
		info_2.setOnClickListener(listener);
		info_3.setOnClickListener(listener);
		info_4.setOnClickListener(listener);
		info_img.setOnClickListener(listener);
		info_name.setOnClickListener(listener);
		actionbar_returnbt_info.setOnClickListener(listener);
		actionbar_return_info.setOnClickListener(listener);
		actionbar_ensurebtn_info.setOnClickListener(listener);

	}

	public OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.info_img:
				showChoosePicDialog1();
				break;
			case R.id.info_1:
				showChoosePicDialog1();
				break;
			case R.id.info_2:
				changename();
				break;
			case R.id.info_name:
				changename();
				break;
			case R.id.info_4:
				changepwd();// 修改密码
				break;
			case R.id.actionbar_returnbt_info:
				back();
				finish();
				break;
			case R.id.actionbar_return_info:
				back();
				finish();
				break;
			case R.id.actionbar_ensurebtn_info:
				updateinfo();
				back();
				break;
			}

		}
	};

	public void back() {
		Intent intent = new Intent(userinfo.this, MainActivity.class);
		intent.putExtra("id", 4);
		intent.putExtra("userid", userid);
		startActivity(intent);
		finish();
	}

	/*
	 * 更改用户名
	 */
	public void changename() {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view = inflater.inflate(R.layout.changename, null);
		final EditText changename = (EditText) view
				.findViewById(R.id.changename);
		AlertDialog.Builder builder = new AlertDialog.Builder(userinfo.this);
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String name1 = changename.getText().toString();
				ContentValues values = new ContentValues();
				values.put("us_name", name1);
				helper = new DBHelper(getApplicationContext());
				helper.update_User_Table(values, userid);
				helper.close();
				Toast.makeText(userinfo.this, "修改成功！", Toast.LENGTH_SHORT)
						.show();
				info_name.setText(name1);
			}
		});
		AlertDialog dialog = builder.create();// 获取dialog
		dialog.setOnShowListener(new OnShowListener() {
			public void onShow(DialogInterface dialog) {
				InputMethodManager inputManager = (InputMethodManager) changename
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(changename, 0);
			}
		});
		dialog.show();// 显示对话框
	}

	/*
	 * 更改密码
	 */
	public void changepwd() {
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view = inflater.inflate(R.layout.pwddialog_layout, null);
		final EditText oldpwd = (EditText) view.findViewById(R.id.oldpwd);
		final EditText newpwd = (EditText) view.findViewById(R.id.newpwd);
		AlertDialog.Builder builder = new AlertDialog.Builder(userinfo.this);
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String pwd1 = oldpwd.getText().toString();
				final String pwd2 = newpwd.getText().toString();

				String uspwd = null;
				helper = new DBHelper(getApplicationContext());
				Cursor cursor = helper.query_User_Table(userid);
				while (cursor.moveToNext()) {
					uspwd = cursor.getString(cursor.getColumnIndex("us_passwd"));
				}
				if (pwd1.equals(uspwd)) {
					if (!pwd1.equals("") && !pwd2.equals("")) {
						ContentValues values = new ContentValues();
						values.put("us_passwd", newpwd.toString());
						helper = new DBHelper(getApplicationContext());
						helper.update_User_Table(values, userid);
						helper.close();
						Toast.makeText(userinfo.this, "密码修改成功！",
								Toast.LENGTH_SHORT).show();
						uspwd = pwd2;
					} else {
						Toast.makeText(userinfo.this, "请输入密码哦！",
								Toast.LENGTH_SHORT).show();
						changepwd();
					}
				} else {
					Toast.makeText(userinfo.this, "密码不正确哦！", Toast.LENGTH_SHORT)
							.show();
					changepwd();
				}

			}
		});
		AlertDialog dialog = builder.create();// 获取dialog
		dialog.setOnShowListener(new OnShowListener() {
			public void onShow(DialogInterface dialog) {
				InputMethodManager inputManager = (InputMethodManager) newpwd
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(newpwd, 0);
			}
		});
		dialog.show();// 显示对话框
	}

	/*
	 * 更新到数据库
	 */
	protected void updateinfo() {
		helper = new DBHelper(this);
		ContentValues values = new ContentValues();
		values.put("us_name", info_name.getText().toString());
		values.put("us_img", String.valueOf(updateImguri));
		values.put("us_passwd", uspwd);
		helper.update_User_Table(values, userid);
		helper.close();
	}

	@Override
	public void onActivityResult(int req, int res, Intent data) {
		switch (req) {
		/**
		 * 从相册中选取图片的请求标志
		 */
		case REQUEST_CODE_PICK_IMAGE:
			if (res == RESULT_OK) {
				try {
					/**
					 * 该uri是上一个Activity返回的
					 */
					Uri uri = data.getData();
					selectImgLoader(data, uri);
				} catch (Exception e) {
					Toast.makeText(this, "打开相册失败~", Toast.LENGTH_SHORT).show();
				}
			} else {
				Log.i("liang", "失败");
			}
			break;
		case REQUEST_IMAGE_CAPTURE_FULL:
			if (res == RESULT_OK) {
				try {
					selectImgLoader(data, photoUri);
				} catch (FileNotFoundException e) {
					Toast.makeText(this, "打开相机失败~", Toast.LENGTH_SHORT).show();
				}

			}

			break;
		}
	}

	/*
	 * 封装相册、相机加载图片
	 */
	private void selectImgLoader(Intent data, Uri uri)
			throws FileNotFoundException {
		Bitmap bit = BitmapFactory.decodeStream(getContentResolver()
				.openInputStream(uri));
		info_img.setImageBitmap(bit);
		updateImguri = String.valueOf(uri);
	}

	/**
	 * 弹出选择框 1、拍照 2、选择本地相片
	 */
	private void showChoosePicDialog1() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String[] items = { "拍照", "选择本地图片" };
		builder.setNegativeButton("取消", null);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:// 拍摄照片
					takephoto();
					Log.e("Dialog", "TAKE picture");
					break;
				case 1:// 选择本地图片
					Log.e("Dialog", "CHOOSE picture");
					choose_Photos();
					break;
				}
			}

		});
		builder.show();

	}

	/*
	 * 调用相机
	 */
	private void takephoto() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdir();
			}
			String fileName = getPhotoFileName() + ".jpg";
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			photoUri = Uri.fromFile(new File(path + fileName));
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(intent, REQUEST_IMAGE_CAPTURE_FULL);
		}

	}

	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return "IMG_" + dateFormat.format(date);
	}

	/**
	 * 从相册选取图片
	 */
	void choose_Photos() {
		/**
		 * 打开选择图片的界面
		 */
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");// 相片类型
		startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);

	}
}
