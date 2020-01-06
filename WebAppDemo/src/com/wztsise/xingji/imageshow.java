package com.wztsise.xingji;

import java.util.ArrayList;

import com.example.webappdemo.R;
import com.wztsise.util.ImageSet;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

public class imageshow extends Activity {
	private ViewPager viewPager;
	private ArrayList<View> pageview;
	private ImageView imageView, viewpic1, viewpic2, viewpic3;
	private ImageView[] imageViews;
	// Bitmap bitmap;
	String imgflag, picid, re_pic1s, re_pic2s, re_pic3s, re_pic4s, re_pic5s;
	private ImageButton actionbar_returnbt3;
	private TextView title;
	private ViewGroup group;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageshow);
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
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		Intent intent = getIntent();
		imgflag = intent.getStringExtra("piccount");
		pageview = new ArrayList<View>();
		if (imgflag.equals("1")) { // 当传递图片为1
			re_pic1s = intent.getStringExtra("re_pic1s");
			Uri pic1uri = Uri.parse(re_pic1s);
			viewpic1 = new ImageView(imageshow.this);// 动态创建ImageView
			viewpic1.setBackgroundColor(Color.BLACK);
			viewpic1.setScaleType(ScaleType.FIT_CENTER);
			viewpic1.setImageURI(pic1uri); //
			pageview.add(viewpic1); // 加入到可滑动的ViewPager中
		} else if (imgflag.equals("2")) {
			re_pic1s = intent.getStringExtra("re_pic1s");
			re_pic2s = intent.getStringExtra("re_pic2s");
			Uri pic1uri = Uri.parse(re_pic1s);
			Uri pic2uri = Uri.parse(re_pic2s);
			// Toast.makeText(imageshow.this, ""+picid+re_pic1s+re_pic2s,
			// Toast.LENGTH_SHORT).show();
			viewpic1 = new ImageView(imageshow.this);
			viewpic1.setBackgroundColor(Color.BLACK);
			viewpic1.setScaleType(ScaleType.FIT_CENTER);
			// viewpic1.setScaleType(ScaleType.CENTER_CROP);
			viewpic1.setImageURI(pic1uri);
			viewpic2 = new ImageView(imageshow.this);
			viewpic2.setBackgroundColor(Color.BLACK);
			viewpic2.setScaleType(ScaleType.FIT_CENTER);
			viewpic2.setImageURI(pic2uri);
			pageview.add(viewpic1);
			pageview.add(viewpic2);
		} else if (imgflag.equals("3")) {
			re_pic1s = intent.getStringExtra("re_pic1s");
			re_pic2s = intent.getStringExtra("re_pic2s");
			re_pic3s = intent.getStringExtra("re_pic3s");
			Uri pic1uri = Uri.parse(re_pic1s);
			Uri pic2uri = Uri.parse(re_pic2s);
			Uri pic3uri = Uri.parse(re_pic3s);
			viewpic1 = new ImageView(imageshow.this);
			viewpic1.setBackgroundColor(Color.BLACK);
			viewpic1.setScaleType(ScaleType.FIT_CENTER);
			viewpic1.setImageURI(pic1uri);
			viewpic2 = new ImageView(imageshow.this);
			viewpic2.setBackgroundColor(Color.BLACK);
			viewpic2.setScaleType(ScaleType.FIT_CENTER);
			viewpic2.setImageURI(pic2uri);
			viewpic3 = new ImageView(imageshow.this);
			viewpic3.setBackgroundColor(Color.BLACK);
			viewpic3.setScaleType(ScaleType.FIT_CENTER);
			viewpic3.setImageURI(pic3uri);
			pageview.add(viewpic1);
			pageview.add(viewpic2);
			pageview.add(viewpic3);
		}
		group = (ViewGroup) findViewById(R.id.viewGroup);
		// 有多少张图就有多少个点点
		imageViews = new ImageView[pageview.size()];
		for (int i = 0; !imgflag.equals("1") && i < pageview.size(); i++) {
			imageView = new ImageView(imageshow.this);
			imageView.setLayoutParams(new LayoutParams(20, 20));
			imageView.setPadding(20, 0, 20, 0);
			imageViews[i] = imageView;
			// 默认第一张图显示为选中状态
			if (i == 0) {
				imageViews[i]
						.setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				imageViews[i]
						.setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
			group.addView(imageViews[i]);
		}
		viewPager.setAdapter(mPagerAdapter);
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}

	PagerAdapter mPagerAdapter = new PagerAdapter() {
		// 获取当前窗体界面数
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pageview.size();
		}

		// 断是否由对象生成界面
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		// 是从ViewGroup中移出当前View
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageview.get(arg1));
		}

		// 返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageview.get(arg1));
			return pageview.get(arg1);
		}

	};

	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		// 如果切换了，就把当前的点点设置为选中背景，其他设置未选中背景
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			for (int i = 0; i < imageViews.length; i++) {
				imageViews[arg0]
						.setBackgroundResource(R.drawable.page_indicator_focused);
				if (arg0 != i) {
					imageViews[i]
							.setBackgroundResource(R.drawable.page_indicator_unfocused);
				}
			}

		}

	}
}
