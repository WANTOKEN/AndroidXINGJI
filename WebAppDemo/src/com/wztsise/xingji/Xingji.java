package com.wztsise.xingji;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.example.webappdemo.R;
import com.wztsise.util.ImageSet;
import com.wztsise.util.setTime;

public class Xingji extends Fragment {
	View view;
	private ListView listView;
	private TextView addrecord1;
	private TextView xingji_flag, xingji_settime, xingji_setflag;
	private List<Map<String, Object>> list;
	private DBHelper helper;
	SimpleAdapter adapter;
	String qflag;
	String userid;
	final static String setflag1 ="全部";
	final static String setflag2 ="全部记录";
	String re_flag = "全部";// 标签
	String[] from = { "re_name", "re_time", "re_content", "re_locate",
			"re_userimg", "re_pic1", "re_pic2", "re_pic3", "re_flag" };
	int[] to = { R.id.xingjiuesrName, R.id.xingjitime, R.id.xingjitext,
			R.id.xingji_locate, R.id.xingjiuserImg, R.id.xingji_pic1,
			R.id.xingji_pic2, R.id.xingji_pic3, R.id.re_flag };
	int qflagnum;
	String settime;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.xingji, container, false);

		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		Intent intent = getActivity().getIntent();
		userid = intent.getStringExtra("userid");
		listView = (ListView) view.findViewById(R.id.xingjilist);
		addrecord1 = (TextView) view.findViewById(R.id.addrecord1);
		xingji_flag = (TextView) view.findViewById(R.id.xingji_flag);
		xingji_settime = (TextView) view.findViewById(R.id.xingji_settime);
		xingji_settime.setText(setTime.getTime().replace(setTime.getTime(),
				"今天"));
		xingji_setflag = (TextView) view.findViewById(R.id.xingji_setflag);
		xingji_flag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				helper = new DBHelper(getActivity());
				Cursor cursor = helper.query_Flag_Table(userid);
				int num = cursor.getCount();// falg的个数
				final String item_list[] = new String[num + 2];
				item_list[0] = "全部";
				item_list[1] = "未标注";
				int count = 1;
				while (cursor.moveToNext()) {
					count++;
					item_list[count] = cursor.getString(cursor
							.getColumnIndex("flag_name"));

				}
				cursor.close();
				helper.close();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("选择标签");// 设置标题
				builder.setItems(item_list,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String typetext = item_list[which];
								xingji_flag.setText(typetext);
								re_flag = typetext.toString().trim();
								initDataList();
							}
						});
				AlertDialog dialog = builder.create();// 获取dialog
				dialog.show();// 显示对话框

			}
		});
		xingji_settime.setOnClickListener(new OnClickListener() {// 设置时间
					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						String []item={"全部记录","选择日期"};
						builder.setItems(item,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if(which==0){	//全部记录
											xingji_settime.setText(setflag2);
											initDataList();
										}else{
											showDateDialog();	//日期对话框
										}
											
										
									}
								});
						AlertDialog dialog = builder.create();// 获取dialog
						dialog.show();// 显示对话框
					
					}
					/*
					 * 日期选择
					 */
					private void showDateDialog() {
						Calendar cal = Calendar.getInstance();
						new DatePickerDialog(getActivity(),
								new OnDateSetListener() {

									@Override
									public void onDateSet(DatePicker view,
											int year, int monthOfYear,
											int dayOfMonth) {
										xingji_settime
												.setText((year + "-"
														+ (monthOfYear + 1)
														+ "-" + dayOfMonth)
														.replace(setTime
																.getTime(),
																"今天"));
										initDataList();
									}
								}, cal.get(Calendar.YEAR), cal
										.get(Calendar.MONTH), cal
										.get(Calendar.DAY_OF_MONTH)).show();
					}
				});
		xingji_setflag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), addflag.class);
				intent.putExtra("userid", userid);
				startActivity(intent);
			}
		});
		final View bottomView = LayoutInflater.from(getActivity()).inflate(
				R.layout.indexfoot, null);
		bottomView.setVisibility(View.GONE);
		addrecord1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent1 = new Intent(getActivity(), addrecord.class);
				intent1.putExtra("userid", userid);
				startActivity(intent1);

			}
		});
		listView.addFooterView(bottomView);
		listView.setOnScrollListener(new OnScrollListener() {
			private int totalItemCount;// 页面显示的总条数
			private int lastVisibleItem;// 最后一条可见记录的数值

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 当页面总条数等于最后一条可见记录的数值，说明以及滑动到了底部,并判断滑动是否停止
				if (totalItemCount == lastVisibleItem
						&& scrollState == SCROLL_STATE_IDLE) {
					bottomView.setVisibility(View.VISIBLE);
					Handler handler = new Handler();
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							setdateTime();// 设置时间
							qflag = String.valueOf(qflagnum);// 数据量-1=位置,查下一位
							// Log.i("INFO","视图已经停止滑动"+qflag);
							addDatalist(userid, qflag, settime, re_flag);// 添加数据
							adapter.notifyDataSetChanged();
							bottomView.setVisibility(View.GONE);
						}
					}, 500);

				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// Log.i("INFO",""+firstVisibleItem+visibleItemCount+totalItemCount);
				this.lastVisibleItem = firstVisibleItem + visibleItemCount;
				this.totalItemCount = totalItemCount;
			}
		});
		initDataList();
	}

	private void initDataList() {
	
		setdateTime();
		qflagnum = 0;// 条数
		qflag = "0";// 从第一行读起
		use_cursor(userid, qflag, settime, re_flag);// 按用户id,记录位置，时间，标注查询
	}

	private void setdateTime() {
		settime = xingji_settime.getText().toString().trim();
		if (settime.equals("今天")) {
			settime = setTime.getTime();
		} else if(settime.equals(setflag2)){
			settime =setflag2;
		}else{
			settime = xingji_settime.getText().toString().trim();
		}
	}

	private void use_cursor(String userid, String qflag, String settime2,
			String re_flag1) {
		list = new ArrayList<Map<String, Object>>();
		addDatalist(userid, qflag, settime2, re_flag1);
		adapter = new SimpleAdapter(getActivity(), list,
				R.layout.xingji_listitem, from, to) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final int p = position;
				final View view = super.getView(position, convertView, parent);
				ImageView pic1 = (ImageView) view
						.findViewById(R.id.xingji_pic1);
				ImageView pic2 = (ImageView) view
						.findViewById(R.id.xingji_pic2);
				ImageView pic3 = (ImageView) view
						.findViewById(R.id.xingji_pic3);
				TextView del = (TextView) view.findViewById(R.id.xingji_del);
				Button sharebtn = (Button) view.findViewById(R.id.sharebtn);
				TextView view_flag = (TextView) view.findViewById(R.id.re_flag);

				LinearLayout xingjil = (LinearLayout) view
						.findViewById(R.id.xingjil); // 外层布局
				final Map<String, Object> text = list.get(p);// 获取
				final String re_content = (String) text.get("re_content");
				final Bitmap pic1s = (Bitmap) text.get("re_pic1");
				final Bitmap pic2s = (Bitmap) text.get("re_pic2");
				final Bitmap pic3s = (Bitmap) text.get("re_pic3");
				final String re_id = (String) text.get("re_id");
				final String re_pic1s = (String) text.get("pic1");
				final String re_pic2s = (String) text.get("pic2");
				final String re_pic3s = (String) text.get("pic3");
				final String imgflag;
				if (pic1s == null) {// 根据有无图片来显示
					pic1.setVisibility(View.GONE);
					pic2.setVisibility(View.GONE);
					pic3.setVisibility(View.GONE);
					imgflag = "0";
				} else if (pic2s == null) {
					pic1.setVisibility(View.VISIBLE);
					pic2.setVisibility(View.INVISIBLE);
					pic3.setVisibility(View.INVISIBLE);
					imgflag = "1";
				} else if (pic3s == null) {
					pic1.setVisibility(View.VISIBLE);
					pic2.setVisibility(View.VISIBLE);
					pic3.setVisibility(View.INVISIBLE);
					imgflag = "2";
				} else {
					pic1.setVisibility(View.VISIBLE);
					pic2.setVisibility(View.VISIBLE);
					pic3.setVisibility(View.VISIBLE);
					imgflag = "3";
				}

				itemclick(p, pic1, pic2, pic3, del, pic1s, pic2s, pic3s, re_id,
						imgflag, re_pic1s, re_pic2s, re_pic3s, xingjil,
						sharebtn, re_content, view_flag);
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
		listView.setAdapter(adapter);
	}

	/*
	 * 添加数据是实现 qflag指定位置
	 */
	public void addDatalist(String userid, String index, String settime2,
			String re_flag1) {
		helper = new DBHelper(getActivity());
		Cursor cursor = helper.query_Jilu_Table1(userid, index, settime2,
				re_flag1);
		while (cursor.moveToNext()) {
			qflagnum++;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("re_id", cursor.getString(cursor.getColumnIndex("re_id")));
			map.put("re_name",
					cursor.getString(cursor.getColumnIndex("us_name")));
			map.put("re_time",
					cursor.getString(cursor.getColumnIndex("re_time")).replace(
							setTime.getTime(), "今天"));
			map.put("re_content",
					cursor.getString(cursor.getColumnIndex("re_content")));
			map.put("re_locate",
					cursor.getString(cursor.getColumnIndex("re_locate")));
			map.put("re_userimg",
					cursor.getString(cursor.getColumnIndex("us_img")));
			map.put("pic1", cursor.getString(cursor.getColumnIndex("re_pic1")));
			map.put("pic2", cursor.getString(cursor.getColumnIndex("re_pic2")));
			map.put("pic3", cursor.getString(cursor.getColumnIndex("re_pic3")));
			map.put("re_flag",
					cursor.getString(cursor.getColumnIndex("re_flag")));
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
				pic1bit = ImageSet.getBitmapFormUri(getActivity(), pic1uri);
				pic2bit = ImageSet.getBitmapFormUri(getActivity(), pic2uri);
				pic3bit = ImageSet.getBitmapFormUri(getActivity(), pic3uri);
			} catch (FileNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			if (pic1bit != null) {

				map.put("re_pic1", pic1bit);
			}
			if (pic2bit != null) {

				map.put("re_pic2", pic2bit);
			}
			if (pic3bit != null) {

				map.put("re_pic3", pic3bit);
			}
			list.add(map);
		}
		helper.close();
	}

	/*
	 * item点击事件
	 */
	public void itemclick(final int p, ImageView pic1, ImageView pic2,
			ImageView pic3, TextView del, final Bitmap pic1s,
			final Bitmap pic2s, final Bitmap pic3s, final String re_id,
			final String imgflag, final String re_pic1s, final String re_pic2s,
			final String re_pic3s, LinearLayout xingjil, Button sharebtn,
			final String re_content, final TextView view_flag) {
		/*
		 * 更改flag
		 */
		view_flag.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				helper = new DBHelper(getActivity());
				Cursor cursor = helper.query_Flag_Table(userid);
				int num = cursor.getCount();// falg的个数
				final String item_list[] = new String[num + 1];
				item_list[0] = "添加自定义标签";
				// Toast.makeText(getActivity(), "添加成功"+num,
				// Toast.LENGTH_SHORT).show();
				int count = 0;
				while (cursor.moveToNext()) {
					count++;
					item_list[count] = cursor.getString(cursor
							.getColumnIndex("flag_name"));
				}
				cursor.close();
				helper.close();
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("选择标签");// 设置标题
				builder.setItems(item_list,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String typetext = item_list[which];
								if (typetext.equals(item_list[0])) { // 判断是不是第一个默认
									Intent intent = new Intent(getActivity(),
											addflag.class);
									intent.putExtra("userid", userid);
									startActivity(intent);
								} else { // 不是则更新记录标注
									re_flag = typetext;
									ContentValues values = new ContentValues();
									values.put("re_flag", typetext);
									helper = new DBHelper(getActivity()
											.getApplicationContext());
									helper.update_Jiluflag_Table(values, re_id);
									helper.close();
									// Toast.makeText(getActivity(), "修改成功！",
									// Toast.LENGTH_SHORT).show();
									view_flag.setText(typetext);
								}

							}
						});
				AlertDialog dialog = builder.create();// 获取dialog
				dialog.show();// 显示对话框

			}
		});
		/*
		 * 分享
		 */
		sharebtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				OnekeyShare oneKeyShare = new OnekeyShare();// 一键分享
				// 设置标题
				oneKeyShare.setTitle("行记");
				oneKeyShare.setText(re_content); // 获取所在记录的内容并设置文本信息
				if (pic1s != null) {
					oneKeyShare.setImageData(pic1s);// 获取图片(Bitmap类型),并设置到分享图片
				}
				oneKeyShare.show(getActivity());
			}
		});
		xingjil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent showxingji = new Intent(getActivity(), xingjishow.class);
				showxingji.putExtra("userid", userid);// 必须要转成字符串
				showxingji.putExtra("re_id", re_id);
				startActivity(showxingji);
			}
		});
		pic1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent showimg = new Intent(getActivity(), imageshow.class);
				showimg.putExtra("picid", re_id);
				showimg.putExtra("re_pic1s", re_pic1s);
				showimg.putExtra("re_pic2s", re_pic2s);
				showimg.putExtra("re_pic3s", re_pic3s);
				showimg.putExtra("piccount", imgflag);
				startActivity(showimg);
			}
		});
		pic2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent showimg = new Intent(getActivity(), imageshow.class);
				showimg.putExtra("picid", re_id);
				showimg.putExtra("re_pic1s", re_pic1s);
				showimg.putExtra("re_pic2s", re_pic2s);
				showimg.putExtra("re_pic3s", re_pic3s);
				showimg.putExtra("piccount", imgflag);// 图片数量
				startActivity(showimg);
			}
		});
		pic3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent showimg = new Intent(getActivity(), imageshow.class);
				showimg.putExtra("picid", re_id);
				showimg.putExtra("re_pic1s", re_pic1s);
				showimg.putExtra("re_pic2s", re_pic2s);
				showimg.putExtra("re_pic3s", re_pic3s);
				showimg.putExtra("piccount", imgflag);
				startActivity(showimg);
			}
		});
		del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setMessage("真的要删除该记录吗？");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								list.remove(p);// 视图中删除
								adapter.notifyDataSetChanged();
								helper = new DBHelper(getActivity());// 数据库中删除
								helper.del_Jilu_Table(re_id);
								Intent intent = new Intent(getActivity(),
										MainActivity.class);
								intent.putExtra("id", 2);// 发送信号
								intent.putExtra("userid", userid);// 发送信号
								startActivity(intent);
								getActivity().finish();
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();
				helper.close();
			}
		});
	}

}
