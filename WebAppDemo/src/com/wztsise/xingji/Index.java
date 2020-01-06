package com.wztsise.xingji;

import java.util.HashMap;
import java.util.Map;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.webappdemo.R;
import com.wztsise.util.ImageSet;
import com.wztsise.util.setTime;

public class Index extends Fragment {
	private View view;
	private String userid;
	private LinearLayout indexl, indexr;// 左右布局
	private DBHelper helper;
	private ViewFlipper picViewFliper;
	int[] ids = { R.drawable.indextop, R.drawable.indextop2,
			R.drawable.indextop, R.drawable.indextop2 };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.index_fragment, container, false);
		return view;

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Intent intent = getActivity().getIntent();
		userid = intent.getStringExtra("userid");
		initUI();
		initData();
	}

	private void initUI() {
		indexl = (LinearLayout) view.findViewById(R.id.indexl);
		indexr = (LinearLayout) view.findViewById(R.id.indexr);
		picViewFliper = (ViewFlipper) view.findViewById(R.id.picViewFliper);
		for (int i = 0; i < ids.length; i++) {
			ImageView iview = getImageView(ids[i]);
			picViewFliper.addView(iview);
		}
	}

	private ImageView getImageView(int id) {
		ImageView imageView = new ImageView(getActivity());
		imageView.setBackgroundResource(id);
		return imageView;
	}

	/*
	 * 初始化数据
	 */
	private void initData() {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		helper = new DBHelper(getActivity());
		Cursor cursor = helper.query_Index_JL(userid, "0");
		int flag = 0;
		while (cursor.moveToNext()) {
			flag++;
			Map<String, Object> map = new HashMap<String, Object>();
			// 记录
			map.put("re_id", cursor.getString(cursor.getColumnIndex("re_id")));
			map.put("re_content",
					cursor.getString(cursor.getColumnIndex("re_content")));
			map.put("re_time",
					cursor.getString(cursor.getColumnIndex("re_time")).replace(
							setTime.getTime(), "今天"));
			map.put("re_locate",
					cursor.getString(cursor.getColumnIndex("re_locate")));
			map.put("re_pic1",
					cursor.getString(cursor.getColumnIndex("re_pic1")));
			map.put("re_pic2",
					cursor.getString(cursor.getColumnIndex("re_pic2")));
			map.put("re_pic3",
					cursor.getString(cursor.getColumnIndex("re_pic3")));
			final String re_id = (String) map.get("re_id");
			String j1 = (String) map.get("re_time");
			String j2 = (String) map.get("re_content");
			String j3 = (String) map.get("re_locate");
			final Uri pic1uri = Uri.parse((String) map.get("re_pic1"));
			final Uri pic2uri = Uri.parse((String) map.get("re_pic2"));
			final Uri pic3uri = Uri.parse((String) map.get("re_pic3"));
			Bitmap pic1bit = null;
			Bitmap pic2bit = null;
			Bitmap pic3bit = null;
			try {
				pic1bit = ImageSet.getBitmapFormUri(getActivity(), pic1uri);
				pic2bit = ImageSet.getBitmapFormUri(getActivity(), pic2uri);
				pic3bit = ImageSet.getBitmapFormUri(getActivity(), pic3uri);
			} catch (Exception e) {

			}

			View view1 = inflater.inflate(R.layout.indexitem_xing, null);
			TextView indextime1 = (TextView) view1
					.findViewById(R.id.indextime1);
			TextView indexcontext1 = (TextView) view1
					.findViewById(R.id.indexcontext1);
			TextView index_locate = (TextView) view1
					.findViewById(R.id.index_locate);
			ImageView img1 = (ImageView) view1.findViewById(R.id.index_pic1);
			view1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent showxingji = new Intent(getActivity(),
							xingjishow.class);
					showxingji.putExtra("userid", userid);
					showxingji.putExtra("re_id", re_id);
					startActivity(showxingji);
				}
			});
			final String imgflag;
			indextime1.setText(j1);
			indexcontext1.setText(j2);
			index_locate.setText(j3);
			imgflag = imgSetCount(pic1bit, pic2bit, pic3bit, img1);// 计算图片个数
			if (flag % 2 != 0) { // 根据奇数偶数判断制作流式布局
				indexl.addView(view1);// 放置左边
			} else {
				indexr.addView(view1);// 放置右边
			}
			img1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					actionToImg(pic1uri, pic2uri, pic3uri, imgflag);// 跳转到图片显示
				}
			});

		}
		Cursor cursor2 = helper.query_Index_ZB(userid, "0");
//		flag = 0;
		while (cursor2.moveToNext()) {
			View view2 = inflater.inflate(R.layout.indexitem_zhang, null);
			ImageView index_flag = (ImageView) view2
					.findViewById(R.id.index_flag);
			flag++;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("zb_id", cursor2.getString(cursor2.getColumnIndex("zb_id")));
			map.put("zb_datetime",
					cursor2.getString(cursor2.getColumnIndex("zb_time"))
							.replace(setTime.getTime(), "今天"));
			map.put("zb_type",
					cursor2.getString(cursor2.getColumnIndex("zb_type")));
			map.put("zb_content",
					cursor2.getString(cursor2.getColumnIndex("zb_content")));
			map.put("zb_pic1",
					cursor2.getString(cursor2.getColumnIndex("zb_pic1")));
			map.put("zb_pic2",
					cursor2.getString(cursor2.getColumnIndex("zb_pic2")));
			map.put("zb_pic3",
					cursor2.getString(cursor2.getColumnIndex("zb_pic3")));
			final Uri pic1uri = Uri.parse((String) map.get("zb_pic1"));
			final Uri pic2uri = Uri.parse((String) map.get("zb_pic2"));
			final Uri pic3uri = Uri.parse((String) map.get("zb_pic3"));
			Bitmap pic1bit = null;
			Bitmap pic2bit = null;
			Bitmap pic3bit = null;
			try {
				pic1bit = ImageSet.getBitmapFormUri(getActivity(), pic1uri);
				pic2bit = ImageSet.getBitmapFormUri(getActivity(), pic2uri);
				pic3bit = ImageSet.getBitmapFormUri(getActivity(), pic3uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (cursor2.getString(cursor2.getColumnIndex("zb_flag")).equals(
					"收入")) {
				map.put("zb_count",
						"+"
								+ cursor2.getString(cursor2
										.getColumnIndex("zb_count")));
				index_flag.setImageResource(R.drawable.zb_s);
			} else if (cursor2.getString(cursor2.getColumnIndex("zb_flag"))
					.equals("支出")) {
				map.put("zb_count",
						"-"
								+ cursor2.getString(cursor2
										.getColumnIndex("zb_count")));
				index_flag.setImageResource(R.drawable.zb_z);
			}
			final String zb_id = (String) map.get("zb_id");
			String z1 = (String) map.get("zb_content");
			String z2 = (String) map.get("zb_datetime");
			String z3 = (String) map.get("zb_type");
			String z4 = (String) map.get("zb_count");

			TextView indextime2 = (TextView) view2
					.findViewById(R.id.indextime2);
			TextView indexcontext2 = (TextView) view2
					.findViewById(R.id.indexcontext2);
			TextView index_typecount = (TextView) view2
					.findViewById(R.id.index_typecount);
			ImageView img2 = (ImageView) view2.findViewById(R.id.index_pic2);

			view2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent showzhangben = new Intent(getActivity(),
							zhangbenshow.class);
					showzhangben.putExtra("userid", userid);
					showzhangben.putExtra("zb_id", zb_id);
					startActivity(showzhangben);
				}
			});
			final String imgflag;
			indextime2.setText(z2);
			indexcontext2.setText("备注：" + z1);
			index_typecount.setText(z3 + ":" + z4);
			imgflag = imgSetCount(pic1bit, pic2bit, pic3bit, img2);
			if (flag % 2 == 0) {
				indexr.addView(view2);
			} else {
				indexl.addView(view2);
			}
			img2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					actionToImg(pic1uri, pic2uri, pic3uri, imgflag);
				}

			});

		}
		helper.close();
	}

	/*
	 * 显示图片
	 */
	private void actionToImg(final Uri pic1uri, final Uri pic2uri,
			final Uri pic3uri, final String imgflag) {
		Intent showimg = new Intent(getActivity(), imageshow.class);
		showimg.putExtra("re_pic1s", String.valueOf(pic1uri));
		showimg.putExtra("re_pic2s", String.valueOf(pic2uri));
		showimg.putExtra("re_pic3s", String.valueOf(pic3uri));
		showimg.putExtra("piccount", imgflag);
		startActivity(showimg);
	}

	/*
	 * 设置图片数量
	 */
	private String imgSetCount(Bitmap pic1bit, Bitmap pic2bit, Bitmap pic3bit,
			ImageView img1) {
		final String imgflag;
		if (pic1bit == null) { // 如果第一张为空，第二第三也是为空
			img1.setVisibility(View.GONE);
			imgflag = "0";
		} else if (pic2bit == null) { // 第一张不为空，第二为空
			img1.setVisibility(View.VISIBLE);
			img1.setImageBitmap(pic1bit);// 放置第一张图片
			imgflag = "1";
		} else if (pic3bit == null) { // 第二张不为空，第三为空
			img1.setVisibility(View.VISIBLE);
			img1.setImageBitmap(pic1bit);// 放置第一张图片
			imgflag = "2";
		} else {
			img1.setVisibility(View.VISIBLE);
			img1.setImageBitmap(pic1bit);// 放置第一张图片
			imgflag = "3"; // 声明有三张图片
		}
		return imgflag;
	}

}
