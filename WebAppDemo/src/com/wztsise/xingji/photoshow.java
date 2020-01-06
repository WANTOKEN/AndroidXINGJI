package com.wztsise.xingji;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.example.webappdemo.R;
import com.wztsise.util.ImageSet;

/*
 * 相册
 */
public class photoshow extends Activity {
	private DBHelper helper;
	private GridView gridView;
	private List<Map<String, Object>> dataList;
	private SimpleAdapter adapter;
	private TextView id_dir_count;
	private ImageButton actionbar_returnbt3;
	private TextView title;
	int imgcount;
	String userid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_show);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(android.app.ActionBar.DISPLAY_SHOW_CUSTOM,
				android.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setCustomView(R.layout.my_actionbar3);
		title = (TextView) findViewById(R.id.title);
		title.setText("相册");
		actionbar_returnbt3 = (ImageButton) findViewById(R.id.actionbar_returnbt3);
		actionbar_returnbt3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		Intent intent = getIntent();
		userid = intent.getStringExtra("userid");
		gridView = (GridView) findViewById(R.id.photos_gridView);
		id_dir_count = (TextView) findViewById(R.id.id_dir_count);
		dataList = new ArrayList<Map<String, Object>>();
		adapter = new SimpleAdapter(this, getData(), R.layout.photoitem,
				new String[] { "image" }, new int[] { R.id.image }) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final int p = position;
				final View view = super.getView(position, convertView, parent);
				ImageView pic = (ImageView) view.findViewById(R.id.image);
				final Map<String, Object> text = dataList.get(p);
				final String imagestr = String.valueOf(text.get("imageuri"));
				pic.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent showimg = new Intent(photoshow.this,
								imageshow.class);
						showimg.putExtra("piccount", "1");
						showimg.putExtra("re_pic1s", imagestr);
						startActivity(showimg);

					}
				});
				return view;
			}
		};
		adapter.setViewBinder(new ViewBinder() {
			public boolean setViewValue(View view, Object data,
					String textRepresentation) {
				if (view instanceof ImageView && data instanceof Bitmap) {
					ImageView iv = (ImageView) view;
					iv.setImageBitmap((Bitmap) data);
					return true;
				} else {
					return false;
				}
			}
		});
		gridView.setAdapter(adapter);
		helper.close();
		id_dir_count.setText("共" + imgcount + "张");
	}

	private List<Map<String, Object>> getData() {
		helper = new DBHelper(this);
		Cursor cursor = helper.query_Jilu_Table_Photos(userid);
		Cursor cursor2 = helper.query_ZhBen_Table_Photos(userid);
		imgcount = 0;
		while (cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			Uri pic1uri = Uri.parse(cursor.getString(cursor
					.getColumnIndex("re_pic1")));
			Uri pic2uri = Uri.parse(cursor.getString(cursor
					.getColumnIndex("re_pic2")));
			Uri pic3uri = Uri.parse(cursor.getString(cursor
					.getColumnIndex("re_pic3")));
			Bitmap pic1bit = null;
			Bitmap pic2bit = null;
			Bitmap pic3bit = null;
			try {
				pic1bit = ImageSet.getBitmapFormUri(this, pic1uri);
				pic2bit = ImageSet.getBitmapFormUri(this, pic2uri);
				pic3bit = ImageSet.getBitmapFormUri(this, pic3uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (pic1bit != null) {
				map.put("image", pic1bit);
				map.put("imageuri", pic1uri);
				imgcount++;
				dataList.add(map);
			}
			if (pic2bit != null) {
				map.put("image", pic2bit);
				map.put("imageuri", pic2uri);
				dataList.add(map);
				imgcount++;
			}
			if (pic3bit != null) {
				map.put("image", pic3bit);
				map.put("imageuri", pic3uri);
				dataList.add(map);
				imgcount++;
			}
		}
		while (cursor2.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			Uri pic4uri = Uri.parse(cursor2.getString(cursor2
					.getColumnIndex("zb_pic1")));
			Uri pic5uri = Uri.parse(cursor2.getString(cursor2
					.getColumnIndex("zb_pic2")));
			Uri pic6uri = Uri.parse(cursor2.getString(cursor2
					.getColumnIndex("zb_pic3")));
			Bitmap pic4bit = null;
			Bitmap pic5bit = null;
			Bitmap pic6bit = null;
			try {
				pic4bit = ImageSet.getBitmapFormUri(this, pic4uri);
				pic5bit = ImageSet.getBitmapFormUri(this, pic5uri);
				pic6bit = ImageSet.getBitmapFormUri(this, pic6uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (pic4bit != null) {
				map.put("image", pic4bit);
				map.put("imageuri", pic4uri);
				dataList.add(map);
				imgcount++;
			}
			if (pic5bit != null) {
				map.put("image", pic5bit);
				map.put("imageuri", pic5uri);
				dataList.add(map);
				imgcount++;
			}
			if (pic6bit != null) {
				map.put("image", pic6bit);
				map.put("imageuri", pic6uri);
				dataList.add(map);
				imgcount++;
			}
		}
		return dataList;
	}

}
