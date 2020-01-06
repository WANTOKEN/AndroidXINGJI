package com.wztsise.xingji;

import com.example.webappdemo.R;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class User_center extends Fragment implements OnClickListener {
	LinearLayout user_info, user_jilu, user_jizhang, user_xiangce, user_shezhi,
			user_tuichu;
	private ImageView user_img;
	private TextView user_name, user_id;
	View view;
	String userid;
	private DBHelper helper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.user_center, container, false);
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

	private void initData() {
		use_query();

	}

	private void initUI() {
		user_info = (LinearLayout) view.findViewById(R.id.user_info);
		user_jilu = (LinearLayout) view.findViewById(R.id.user_jilu);
		user_jizhang = (LinearLayout) view.findViewById(R.id.user_jizhang);
		user_xiangce = (LinearLayout) view.findViewById(R.id.user_xiangce);
		user_shezhi = (LinearLayout) view.findViewById(R.id.user_shezhi);
		user_tuichu = (LinearLayout) view.findViewById(R.id.user_tuichu);
		user_img = (ImageView) view.findViewById(R.id.user_img);
		user_name = (TextView) view.findViewById(R.id.user_name);
		user_id = (TextView) view.findViewById(R.id.user_id);
		user_info.setOnClickListener(this);
		user_jilu.setOnClickListener(this);
		user_jizhang.setOnClickListener(this);
		user_xiangce.setOnClickListener(this);
		user_shezhi.setOnClickListener(this);
		user_tuichu.setOnClickListener(this);
	}

	void use_query() {
		helper = new DBHelper(getActivity());
		Cursor cursor = helper.query_User_Table(userid);
		while (cursor.moveToNext()) {
			user_img.setImageURI(Uri.parse(cursor.getString(cursor
					.getColumnIndex("us_img"))));
			user_name
					.setText(cursor.getString(cursor.getColumnIndex("us_name")));
			user_id.setText(cursor.getString(cursor.getColumnIndex("user_id")));
		}
		helper.close();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_info:
			Intent intent0 = new Intent(getActivity(), userinfo.class);
			intent0.putExtra("userid", userid);
			startActivity(intent0);
			break;
		case R.id.user_jilu:
			Intent intent1 = new Intent(getActivity(), addrecord.class);
			intent1.putExtra("userid", userid);
			startActivity(intent1);
			break;
		case R.id.user_jizhang:
			Intent intent2 = new Intent(getActivity(), addcount.class);
			intent2.putExtra("userid", userid);
			startActivity(intent2);
			break;
		case R.id.user_xiangce:
			Intent intent3 = new Intent(getActivity(), photoshow.class);
			intent3.putExtra("userid", userid);
			startActivity(intent3);
			break;
		case R.id.user_shezhi:
			Intent intent4 = new Intent(getActivity(), conf_activity.class);
			intent4.putExtra("userid", userid);
			startActivity(intent4);
			break;
		case R.id.user_tuichu:
			Intent intent = new Intent(getActivity(), login.class);
			intent.putExtra("userid", userid);
			startActivity(intent);
			getActivity().finish();
			break;
		default:
			break;
		}

	}

}
