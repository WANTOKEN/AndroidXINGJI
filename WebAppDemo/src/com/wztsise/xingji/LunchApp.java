package com.wztsise.xingji;

import com.example.webappdemo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class LunchApp extends Activity {
	private TextView lunchtext;
	private int time = 3;
	Runnable runnable1;
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lunch_app);
		lunchtext = (TextView) findViewById(R.id.lunchtext);
		lunchtext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LunchApp.this, login.class);
				startActivity(intent);
				finish();
				time = 0;

			}
		});
		lunchtext.setText(time + "s");
		handler.postDelayed(runnable, 3000);
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			time--;
			if (time >= 1) {
				lunchtext.setText(time + "s");
			}
			handler.postDelayed(this, 1000);
			if (time == 1) {
				Intent intent = new Intent(LunchApp.this, login.class);
				startActivity(intent);
				finish();
			} else if (time == 0) {

			}
		}
	};
}
