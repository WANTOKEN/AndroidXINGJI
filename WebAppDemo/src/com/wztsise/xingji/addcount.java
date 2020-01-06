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
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

public class addcount extends Activity implements OnClickListener {
	private static final int REQUEST_CODE_PICK_IMAGE = 1; // 相册
	private static final int REQUEST_IMAGE_CAPTURE_FULL = 2;// 相机
	private ImageView addpicture, addpicture1, addpicture2, addpicture3,
			addpicture4;
	private Map<String, Object> urimap;
	private int addpiccount = 0;
	private String path = Environment.getExternalStorageDirectory()
			+ File.separator + Environment.DIRECTORY_DCIM + File.separator;
	public static String DIRECTORY_DCIM = "DCIM";
	File photoFile;// 相机文件
	Uri photoUri;// 相机
	private ImageButton actionbar_returnbt_addrecord;
	private TextView actionbar_return_addrecord, zb_type, zb_typetext, paytype,
			moneycount;
	private Button actionbar_ensurebtn_addrecord;
	private EditText zb_content;
	private String payflag;// 收支标志־
	private String userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		userid = intent.getStringExtra("userid");
		setContentView(R.layout.addcount);
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
		zb_type = (TextView) findViewById(R.id.zb_type);
		zb_typetext = (TextView) findViewById(R.id.zb_typetext);
		paytype = (TextView) findViewById(R.id.paytype);
		moneycount = (TextView) findViewById(R.id.moneycount);
		zb_content = (EditText) findViewById(R.id.zb_content);
		addpicture1 = (ImageView) findViewById(R.id.addpicture1);
		addpicture2 = (ImageView) findViewById(R.id.addpicture2);
		addpicture3 = (ImageView) findViewById(R.id.addpicture3);
		addpicture4 = (ImageView) findViewById(R.id.addpicture4);
		addpicture1.setOnClickListener(this);
		addpicture2.setOnClickListener(this);
		addpicture3.setOnClickListener(this);
		addpicture4.setOnClickListener(this);
		actionbar_returnbt_addrecord.setOnClickListener(this);
		actionbar_return_addrecord.setOnClickListener(this);
		actionbar_ensurebtn_addrecord.setOnClickListener(this);
		zb_type.setOnClickListener(this);
		zb_typetext.setOnClickListener(this);
		paytype.setOnClickListener(this);
		moneycount.setOnClickListener(this);
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
			addCount();
			break;
		case R.id.zb_type:
			selecttype();
			break;
		case R.id.zb_typetext:
			selecttype();
			break;
		case R.id.paytype:
			paycount("");
			break;
		case R.id.moneycount:// 支付金额
			paycount("");
			break;
		case R.id.addpicture4:// 图片添加
			showChoosePicDialog1();
			break;
		case R.id.addpicture1:// 图片添加1
			addpicture = addpicture1;
			showChoosePicDialog();
			break;
		case R.id.addpicture2:
			addpicture = addpicture2;
			showChoosePicDialog();
			break;
		case R.id.addpicture3:
			addpicture = addpicture3;
			showChoosePicDialog();
			break;
		}

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
					Toast.makeText(this, "打开相册失败", Toast.LENGTH_SHORT).show();
				}
			} else {

			}
			break;
		case REQUEST_IMAGE_CAPTURE_FULL:
			if (res == RESULT_OK) {
				try {
					selectImgLoader(data, photoUri);
				} catch (FileNotFoundException e) {
					Toast.makeText(this, "打开相机失败", Toast.LENGTH_SHORT).show();
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
		setAddpiccount(addpiccount + 1);// 图片+1
		Log.i("INFO", "" + "图片+1" + getAddpiccount());
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
		Intent showimg = new Intent(addcount.this, imageshow.class);
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
		if (addpicture == addpicture1) {
			Log.i("INFO", "删除" + getAddpiccount());
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
			addpicture4.setVisibility(View.VISIBLE);
		}

	}

	private void addCount() {// 添加记录信息

		if (!moneycount.getText().equals("")) {
			String zb_id = new SimpleDateFormat("yyyyMMddhhmmss")
					.format(new Date());
			String zb_type = zb_typetext.getText().toString();
			String zb_count = moneycount.getText().toString();
			String zb_time = setTime.initTime();
			String zb_text = zb_content.getText().toString();
			String zb_userid = userid;
			String zb_pic1 = String.valueOf(urimap.get("addpicture1"));
			String zb_pic2 = String.valueOf(urimap.get("addpicture2"));
			String zb_pic3 = String.valueOf(urimap.get("addpicture3"));
			ContentValues values = new ContentValues();
			values.put("zb_id", zb_id.toString());
			values.put("zb_type", zb_type.toString());
			values.put("zb_time", zb_time.toString());
			values.put("zb_content", zb_text.toString());
			values.put("zb_count", zb_count.toString());
			values.put("zb_flag", payflag.toString());
			values.put("zb_userid", zb_userid);
			values.put("zb_pic1", zb_pic1);
			values.put("zb_pic2", zb_pic2);
			values.put("zb_pic3", zb_pic3);
			DBHelper helper = new DBHelper(getApplicationContext());
			helper.insert_ZhBen_Table(values);
			helper.close();
			Toast.makeText(addcount.this, "添加账本成功", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(addcount.this, MainActivity.class);
			intent.putExtra("id", 3);
			intent.putExtra("userid", userid);
			startActivity(intent);
			finish();
		} else {
			Toast.makeText(addcount.this, "请点击输入金额哦", Toast.LENGTH_SHORT)
					.show();
			selecttype();
		}

	}

	/*
	 * 选择类型
	 */
	private void selecttype() {
		final String[] item_list = { "现金支出", "现金收入", "微信支出", "微信收入", "支付宝支出",
				"支付宝收入", "银行卡支出", "银行卡收入", "其他方式支出", "其他方式收入" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择类型");// 设置标题
		builder.setItems(item_list, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String typetext = item_list[which];
				zb_typetext.setText(typetext);
				if (typetext.endsWith("支出")) {
					paytype.setText("支出：");
					paycount("支出");
					payflag = "支出";// 1代表支出
				} else if (typetext.endsWith("收入")) {
					paytype.setText("收入：");
					paycount("收入");
					payflag = "收入";// 代表收入
				} else {
					paytype.setText("");
					payflag = "无";// 代表无
				}

			}
		});
		AlertDialog dialog = builder.create();// 获取dialog
		dialog.show();// 显示对话框
	}

	/*
	 * 支付类型金额输入
	 */
	private void paycount(String payways) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.paycountdialog_layout, null);
		final EditText dialogtext = (EditText) view.findViewById(R.id.paydtext);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请输入" + payways + "金额");// 设置标题
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String paycount = dialogtext.getText().toString();
				moneycount.setText(paycount);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO 自动生成的方法存根
				Toast.makeText(addcount.this, "取消！", Toast.LENGTH_SHORT).show();
			}
		});
		AlertDialog dialog = builder.create();// 获取dialog
		dialog.setOnShowListener(new OnShowListener() {

			public void onShow(DialogInterface dialog) {
				InputMethodManager inputManager = (InputMethodManager) dialogtext
						.getContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(dialogtext, 0);
			}
		});
		dialog.show();
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
