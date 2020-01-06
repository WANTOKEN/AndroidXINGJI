package com.wztsise.xingji;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.webappdemo.R;
import com.wztsise.util.setTime;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Jizhang extends Fragment {
	private ListView jizhanglistView;
	private List<Map<String, Object>> jizhanglist;
	private DBHelper helper;
	private TextView addrecord2, jiazhang_income, jiazhang_outcome,
			jiazhang_time;
	String userid;
	View view;
	String qflag;// 位置
	SimpleAdapter jizhangadapter;
	int total_outcome = 0; // 总支出
	int total_income = 0; // 总收入
	String from[] = { "datetime", "type", "content", "count" };
	int to[] = { R.id.jizhangdatetime, R.id.jizhangtype, R.id.jizhangcontent,
			R.id.jizhangcount };
	int qflagnum;// 记录条数
	String settime;
	final static String setflag1 ="全部";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.jizhang, null);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Intent intent = getActivity().getIntent();
		userid = intent.getStringExtra("userid");
		initUI();
		initDataList();

	}

	private void initUI() {
		jizhanglistView = (ListView) view.findViewById(R.id.jizhanglist);
		jiazhang_time = (TextView) view.findViewById(R.id.jiazhang_time);
		jiazhang_time.setText(setTime.getTime()
				.replace(setTime.getTime(), "今天"));
		jiazhang_outcome = (TextView) view.findViewById(R.id.jiazhang_outcome);
		jiazhang_income = (TextView) view.findViewById(R.id.jiazhang_income);
		addrecord2 = (TextView) view.findViewById(R.id.addrecord2);
		jiazhang_time.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				String []item={"全部","选择日期"};
				builder.setItems(item,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if(which==0){	//全部记录
									jiazhang_time.setText(setflag1);
									initDataList();
								}else{
									showDateDialog();
								}
									
								
							}
						});
				AlertDialog dialog = builder.create();// 获取dialog
				dialog.show();// 显示对话框
			
			}

			private void showDateDialog() {
				Calendar cal = Calendar.getInstance();
				new DatePickerDialog(getActivity(), new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						jiazhang_time.setText((year + "-" + (monthOfYear + 1)
								+ "-" + dayOfMonth).replace(setTime.getTime(),
								"今天"));
						initDataList();
					}
				}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal
						.get(Calendar.DAY_OF_MONTH)).show();
			}
		});
		addrecord2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(getActivity(), addcount.class);
				intent1.putExtra("userid", userid);
				startActivity(intent1);

			}
		});
		final View bottomView = LayoutInflater.from(getActivity()).inflate(
				R.layout.indexfoot, null);
		bottomView.setVisibility(View.GONE);
		jizhanglistView.addFooterView(bottomView);
		jizhanglistView.setOnScrollListener(new OnScrollListener() {
			private int lastVisibleItem;
			private int totalItemCount;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (totalItemCount == lastVisibleItem
						&& scrollState == SCROLL_STATE_IDLE) {
					bottomView.setVisibility(View.VISIBLE);
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							setdateTime();// 设置时间
							qflag = String.valueOf(qflagnum);// 数据量-1=位置,查下一位
							addDatalist(userid, qflag, settime);
							jizhangadapter.notifyDataSetChanged();
							bottomView.setVisibility(View.GONE);
						}
					}, 500);

				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				this.lastVisibleItem = firstVisibleItem + visibleItemCount;
				this.totalItemCount = totalItemCount;
			}
		});
	}

	private void initDataList() {
		total_outcome = 0;
		total_income = 0;
		setdateTime();
		qflag = "0";// 位置
		qflagnum = 0;// 条数
		use_cursor(userid, qflag, settime);

	}

	private void setdateTime() {
		settime = jiazhang_time.getText().toString().trim();
		if (settime.equals("今天")) {
			settime = setTime.getTime();
		}else if(settime.equals(setflag1)){
			settime=setflag1;
		} 
		else {
			settime = jiazhang_time.getText().toString().trim();
		}
	}

	private void use_cursor(final String userid, String qflag, String settime2) {
		jizhanglist = new ArrayList<Map<String, Object>>();
		addDatalist(userid, qflag, settime2);
		jizhangadapter = new SimpleAdapter(getActivity(), jizhanglist,
				R.layout.jizhang_listitem, from, to) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final View view = super.getView(position, convertView, parent);
				final Map<String, Object> text = jizhanglist.get(position);
				final String zb_id = (String) text.get("zb_id");
				final String zb_flag = (String) text.get("zb_flag");

				LinearLayout jizhangitem1 = (LinearLayout) view
						.findViewById(R.id.jizhangitem1);
				ImageView jizhangflag = (ImageView) view
						.findViewById(R.id.jizhangflag);
				if (zb_flag.equals("收入")) {
					jizhangflag.setImageResource(R.drawable.zb_s);
				} else if (zb_flag.equals("支出")) {
					jizhangflag.setImageResource(R.drawable.zb_z);
				}
				jizhangitem1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent showzhangben = new Intent(getActivity(),
								zhangbenshow.class);
						showzhangben.putExtra("userid", userid);
						showzhangben.putExtra("zb_id", zb_id);
						startActivity(showzhangben);
					}
				});
				return view;
			}
		};
		jizhanglistView.setAdapter(jizhangadapter);

	}

	private void addDatalist(String userid, String index, String settime2) {
		helper = new DBHelper(getActivity());
		Cursor cursor = helper.query_ZhBen_Table(userid, index, settime2);
		while (cursor.moveToNext()) {
			qflagnum++;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("zb_id", cursor.getString(cursor.getColumnIndex("zb_id")));
			map.put("datetime",
					cursor.getString(cursor.getColumnIndex("zb_time")).replace(
							setTime.getTime(), "今天"));
			map.put("type", cursor.getString(cursor.getColumnIndex("zb_type")));
			map.put("content",
					cursor.getString(cursor.getColumnIndex("zb_content")));
			map.put("zb_flag",
					cursor.getString(cursor.getColumnIndex("zb_flag")));
			if (cursor.getString(cursor.getColumnIndex("zb_flag")).equals("收入")) {
				map.put("count",
						"+"
								+ cursor.getString(cursor
										.getColumnIndex("zb_count")));
				total_income += Integer.valueOf(cursor.getString(cursor
						.getColumnIndex("zb_count")));

			} else if (cursor.getString(cursor.getColumnIndex("zb_flag"))
					.equals("支出")) {
				map.put("count",
						"-"
								+ cursor.getString(cursor
										.getColumnIndex("zb_count")));
				total_outcome += Integer.valueOf(cursor.getString(cursor
						.getColumnIndex("zb_count")));

			}
			jizhanglist.add(map);
		}
		helper.close();
		jiazhang_outcome.setText("-" + String.valueOf(total_outcome));
		jiazhang_income.setText("+" + String.valueOf(total_income));
	}
}
