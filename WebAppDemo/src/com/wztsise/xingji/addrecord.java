package com.wztsise.xingji;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.webappdemo.R;
import com.wztsise.util.setTime;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class addrecord extends Activity implements OnClickListener {
	private static final int MAP_CODE = 3; // 地图
	private static final int REQUEST_CODE_PICK_IMAGE = 1; // 相册
	private static final int REQUEST_IMAGE_CAPTURE_FULL = 2;// 相机
	private ImageButton actionbar_returnbt_addrecord, weizhiimgbtn;
	private TextView actionbar_return_addrecord, weizhitext;
	private Button actionbar_ensurebtn_addrecord;
	private EditText content_addrecord;
	private ImageView addpicture, addpicture1, addpicture2, addpicture3,
			addpicture4;
	private Map<String, Object> urimap;
	private int addpiccount = 0;
	private String path = Environment.getExternalStorageDirectory()
			+ File.separator + Environment.DIRECTORY_DCIM + File.separator;
	public static String DIRECTORY_DCIM = "DCIM";
	File photoFile;// 相机文件
	Uri photoUri;// 相机
	private String userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addrecord);
		Intent intent = getIntent();
		userid = intent.getStringExtra("userid");
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(android.app.ActionBar.DISPLAY_SHOW_CUSTOM,
				android.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setCustomView(R.layout.addrecord_actionbar);
		initUI();
		initDefaultUri();
	}

	private void initDefaultUri() {
		urimap = new HashMap<String, Object>();
		urimap.put("addpicture1", "");
		urimap.put("addpicture2", "");
		urimap.put("addpicture3", "");
	}

	private void initUI() {
		actionbar_returnbt_addrecord = (ImageButton) findViewById(R.id.actionbar_returnbt_addrecord);
		actionbar_return_addrecord = (TextView) findViewById(R.id.actionbar_return_addrecord);
		actionbar_ensurebtn_addrecord = (Button) findViewById(R.id.actionbar_ensurebtn_addrecord);
		weizhiimgbtn = (ImageButton) findViewById(R.id.weizhiimgbtn);
		weizhitext = (TextView) findViewById(R.id.weizhitext);

		content_addrecord = (EditText) findViewById(R.id.content_addrecord);
		addpicture1 = (ImageView) findViewById(R.id.addpicture1);
		addpicture2 = (ImageView) findViewById(R.id.addpicture2);
		addpicture3 = (ImageView) findViewById(R.id.addpicture3);
		addpicture4 = (ImageView) findViewById(R.id.addpicture4);
		actionbar_returnbt_addrecord.setOnClickListener(this);
		actionbar_return_addrecord.setOnClickListener(this);
		actionbar_ensurebtn_addrecord.setOnClickListener(this);
		weizhiimgbtn.setOnClickListener(this);
		weizhitext.setOnClickListener(this);
		addpicture1.setOnClickListener(this);
		addpicture2.setOnClickListener(this);
		addpicture3.setOnClickListener(this);
		addpicture4.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.actionbar_returnbt_addrecord:
			finish();
			break;
		case R.id.actionbar_return_addrecord:
			finish();
			break;
		case R.id.actionbar_ensurebtn_addrecord:
			addRecord();
			break;
		case R.id.weizhiimgbtn:
			Intent intent4 = new Intent(addrecord.this, localcateActivity.class);
			startActivity(intent4);
			finish();
			break;
		case R.id.weizhitext:
			Intent intent5 = new Intent(addrecord.this, localcateActivity.class);
			startActivityForResult(intent5, MAP_CODE);
			break;
		case R.id.addpicture4:// 图片添加
			showChoosePicDialog1();
			break;
		case R.id.addpicture1:// 图片添加
			addpicture = addpicture1;
			showChoosePicDialog();
			break;
		case R.id.addpicture2:// 图片添加
			addpicture = addpicture2;
			showChoosePicDialog();
			break;
		case R.id.addpicture3:// 图片添加
			addpicture = addpicture3;
			showChoosePicDialog();
			break;
		}

	}

	@Override
	public void onActivityResult(int req, int res, Intent data) {
		super.onActivityResult(req, res, data);
		if (req == MAP_CODE && res == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String text = null;
			if (bundle != null)
				text = bundle.getString("maptext");
			weizhitext.setText(text);
		}
		if (req == REQUEST_CODE_PICK_IMAGE && res == RESULT_OK) {
			Uri uri = data.getData();
			try {
				selectImgLoader(data, uri);
			} catch (Exception e) {

			}
		}
		if (req == REQUEST_IMAGE_CAPTURE_FULL && res == RESULT_OK) {
			try {
				selectImgLoader(data, photoUri);
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
	}

	/*
	 * 选择图片加载
	 */
	private void selectImgLoader(Intent data, Uri uri)
			throws FileNotFoundException {
		setAddpiccount(addpiccount + 1);// 图片+1
		Log.i("INFO", "" + "图片数量" + getAddpiccount());
		Bitmap bit = BitmapFactory.decodeStream(getContentResolver()
				.openInputStream(uri));
		if (getAddpiccount() == 1) {
			addpicture1.setVisibility(View.VISIBLE);
			addpicture1.setImageBitmap(bit);
			urimap.put("addpicture1", uri);
			addpicture4.setVisibility(View.VISIBLE);
		} else if (getAddpiccount() == 2) {
			addpicture1.setVisibility(View.VISIBLE);
			addpicture2.setVisibility(View.VISIBLE);
			addpicture2.setImageBitmap(bit);
			urimap.put("addpicture2", uri);
			addpicture4.setVisibility(View.VISIBLE);
		} else if (getAddpiccount() == 3) {
			addpicture1.setVisibility(View.VISIBLE);
			addpicture2.setVisibility(View.VISIBLE);
			addpicture3.setVisibility(View.VISIBLE);
			addpicture3.setImageBitmap(bit);
			urimap.put("addpicture3", uri);
			addpicture4.setVisibility(View.INVISIBLE);
		} else if (getAddpiccount() == 0) {
			addpicture4.setVisibility(View.VISIBLE);
			addpicture1.setVisibility(View.GONE);
			addpicture2.setVisibility(View.GONE);
			addpicture3.setVisibility(View.GONE);
		}
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

	/*
	 * 查看大图，删除图片
	 */
	private void showChoosePicDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String[] items = { "查看大图", "删除该图片" };
		builder.setNegativeButton("取消", null);
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:// 查看大图
					Log.i("Dialog", "WATCH picture");
					watchBigimg();
					break;
				case 1:// 删除图片
					Log.e("Dialog", "DELETE picture");
					del_Photos();
					break;
				}
			}
		});
		builder.show();
	}

	/*
	 * 查看大图
	 */
	private void watchBigimg() {
		String re_pic1s = null;
		String re_pic2s = null;
		String re_pic3s = null;
		String imgcount = "0";
		if (addpicture == addpicture1) {
			re_pic1s = String.valueOf(urimap.get("addpicture1"));
			imgcount = "1";
		} else if (addpicture == addpicture2) {
			re_pic1s = String.valueOf(urimap.get("addpicture1"));
			re_pic2s = String.valueOf(urimap.get("addpicture2"));
			imgcount = "2";
		} else if (addpicture == addpicture3) {
			re_pic1s = String.valueOf(urimap.get("addpicture1"));
			re_pic2s = String.valueOf(urimap.get("addpicture2"));
			re_pic3s = String.valueOf(urimap.get("addpicture3"));
			imgcount = "3";
		}
		Intent showimg = new Intent(addrecord.this, imageshow.class);
		showimg.putExtra("piccount", imgcount);
		showimg.putExtra("re_pic1s", re_pic1s);
		showimg.putExtra("re_pic2s", re_pic2s);
		showimg.putExtra("re_pic3s", re_pic3s);
		startActivity(showimg);
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

	/*
	 * 删除图片
	 */
	private void del_Photos() {// 删除图片，并去掉对应的imageView
		// TODO 自动生成的方法存根
		if (addpicture == addpicture1) {
			Log.i("INFO", "删除：" + getAddpiccount());
			if (getAddpiccount() == 3) {
				urimap.put("addpicture1", urimap.get("addpicture2"));
				urimap.put("addpicture2", urimap.get("addpicture3"));
				urimap.put("addpicture3", "");
				addpicture3.setVisibility(View.GONE);
				addpicture2.setVisibility(View.VISIBLE);
				try {
					Bitmap bit1 = BitmapFactory
							.decodeStream(getContentResolver().openInputStream(
									(Uri) urimap.get("addpicture1")));
					addpicture1.setImageBitmap(bit1);
					Bitmap bit2 = BitmapFactory
							.decodeStream(getContentResolver().openInputStream(
									(Uri) urimap.get("addpicture2")));
					addpicture2.setImageBitmap(bit2);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} else if (getAddpiccount() == 2) {
				urimap.put("addpicture1", urimap.get("addpicture2"));
				urimap.put("addpicture2", "");
				urimap.put("addpicture3", "");
				addpicture2.setVisibility(View.GONE);
				try {
					Bitmap bit1 = BitmapFactory
							.decodeStream(getContentResolver().openInputStream(
									(Uri) urimap.get("addpicture1")));
					addpicture1.setImageBitmap(bit1);
				} catch (FileNotFoundException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}

			} else if (getAddpiccount() == 1) {
				urimap.put("addpicture1", "");
				addpicture1.setVisibility(View.GONE);

			} else {
				Log.i("INFO", "没有删除的：" + getAddpiccount());
			}

			setAddpiccount(addpiccount - 1);// 图片-1
			addpicture4.setVisibility(View.VISIBLE);
		} else if (addpicture == addpicture2) {
			Log.i("INFO", "删除：" + getAddpiccount());
			if (getAddpiccount() == 3) {
				urimap.put("addpicture2", "");
				urimap.put("addpicture2", urimap.get("addpicture3"));
				urimap.put("addpicture3", "");
				addpicture3.setVisibility(View.GONE);
				addpicture2.setVisibility(View.VISIBLE);
				try {
					Bitmap bit1 = BitmapFactory
							.decodeStream(getContentResolver().openInputStream(
									(Uri) urimap.get("addpicture1")));
					addpicture1.setImageBitmap(bit1);
					Bitmap bit2 = BitmapFactory
							.decodeStream(getContentResolver().openInputStream(
									(Uri) urimap.get("addpicture2")));
					addpicture2.setImageBitmap(bit2);
				} catch (FileNotFoundException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				setAddpiccount(addpiccount - 1);// 图片-1
			} else if (getAddpiccount() == 2) {
				urimap.put("addpicture2", "");
				urimap.put("addpicture3", "");
				addpicture2.setVisibility(View.GONE);
				try {
					Bitmap bit1 = BitmapFactory
							.decodeStream(getContentResolver().openInputStream(
									(Uri) urimap.get("addpicture1")));
					addpicture1.setImageBitmap(bit1);
				} catch (FileNotFoundException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				setAddpiccount(addpiccount - 1);// 图片-1
			}
			addpicture4.setVisibility(View.VISIBLE);

		} else if (addpicture == addpicture3) {
			urimap.put("addpicture3", "");
			if (getAddpiccount() == 3) {
				setAddpiccount(addpiccount - 1);// 图片-1
				addpicture3.setVisibility(View.GONE);
			}
			Log.i("INFO", "删除：" + getAddpiccount());
			addpicture4.setVisibility(View.VISIBLE);
		}

	}

	private void addRecord() {// 添加记录信息
		String re_id = new SimpleDateFormat("yyyyMMddhhmmss")
				.format(new Date());
		String re_userid = userid;
		String re_content = content_addrecord.getText().toString();// 获取文本内容信息
		String re_time = setTime.initTime();
		String re_locate = "";
		String re_flag = "未标注";
		if (!weizhitext.getText().toString().equals("所在位置")
				&& !weizhitext.getText().toString().equals("")) {
			re_locate = weizhitext.getText().toString();
		} else {
			re_locate = "未知";
		}
		String re_pic1 = String.valueOf(urimap.get("addpicture1"));
		String re_pic2 = String.valueOf(urimap.get("addpicture2"));
		String re_pic3 = String.valueOf(urimap.get("addpicture3"));
		ContentValues values = new ContentValues();
		values.put("re_id", re_id);
		values.put("re_userid", re_userid);
		values.put("re_time", re_time);
		values.put("re_content", re_content);
		values.put("re_locate", re_locate);
		values.put("re_pic1", re_pic1);
		values.put("re_pic2", re_pic2);
		values.put("re_pic3", re_pic3);
		values.put("re_flag", re_flag);

		// 创建数据库工具类DBHelper
		DBHelper helper = new DBHelper(getApplicationContext());
		helper.insert_Jilu_Table(values);
		helper.close();
		Intent intent = new Intent(addrecord.this, MainActivity.class);
		intent.putExtra("id", 2);// 发送信号
		intent.putExtra("userid", userid);// 发送用户信号
		startActivity(intent);
		finish();
		Toast.makeText(addrecord.this, "添加成功", Toast.LENGTH_SHORT).show();

	}

	public int getAddpiccount() {
		return addpiccount;
	}

	public void setAddpiccount(int addpiccount) {
		if (addpiccount <= 0) {
			this.addpiccount = 0;
		} else {
			this.addpiccount = addpiccount;
		}

	}

}
