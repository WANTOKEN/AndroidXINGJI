package com.wztsise.xingji;

import java.util.ArrayList;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.example.webappdemo.R;

public class localcateActivity extends Activity {
	private ImageButton actionbar_returnbt_map, mapselectbtn;
	private EditText actionbar_maptext;
	private Button actionbar_ensurebtn_map;
	private ListView list_mapselect;
	private ArrayAdapter<String> adapter;
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private LocationClient mLocationClient;
	private MyLocationListener mLocationListener;
	private boolean isFirstIn = true;
	private double mLatitude;
	private double mLongtitude;
	GeoCoder geoCoder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(this.getApplicationContext());
		setContentView(R.layout.maplocalcatelist);
		initUI();
		initView();
		geoCoder = GeoCoder.newInstance();
		initLocation();
	}

	private void initUI() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(android.app.ActionBar.DISPLAY_SHOW_CUSTOM,
				android.app.ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setCustomView(R.layout.map_actionbar);
		list_mapselect = (ListView) findViewById(R.id.list_mapselect);
		actionbar_returnbt_map = (ImageButton) findViewById(R.id.actionbar_returnbt_map);
		actionbar_maptext = (EditText) findViewById(R.id.actionbar_maptext);
		actionbar_ensurebtn_map = (Button) findViewById(R.id.actionbar_ensurebtn_map);
		mapselectbtn = (ImageButton) findViewById(R.id.mapselectbtn);
		mapselectbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final String[] item_list = { "普通地图", "卫星地图", "我的位置" };
				AlertDialog.Builder builder = new AlertDialog.Builder(
						localcateActivity.this);
				builder.setItems(item_list,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String typetext = item_list[which];
								if (typetext.equals("普通地图")) {
									mBaiduMap
											.setMapType(BaiduMap.MAP_TYPE_NORMAL);
								} else if (typetext.equals("卫星地图")) {
									mBaiduMap
											.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
								} else if (typetext.equals("我的位置")) {
									centerToMyLocation();
								}
							}
						});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});
		actionbar_returnbt_map.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		actionbar_ensurebtn_map.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String result = actionbar_maptext.getText().toString().trim();
				Intent intent = getIntent();
				Bundle bundle = new Bundle();
				bundle.putString("maptext", result);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				finish();

			}
		});
	}

	private void initLocation() {
		mLocationClient = new LocationClient(this);
		mLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 高精度
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);
	}

	private void initView() {
		mMapView = (MapView) findViewById(R.id.id_bmapView2);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
		mBaiduMap.setMapStatus(msu);
		mBaiduMap.setOnMapLongClickListener(new OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng point) {
				addDestInfoOverlay(point);
				latlngToAddress(point);
			}
		});
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public boolean onMapPoiClick(MapPoi poi) {
				return false;
			}

			@Override
			public void onMapClick(LatLng point) {
				addDestInfoOverlay(point);
				latlngToAddress(point);
			}
		});
	}

	/*
	 * 根据经纬度反地理编码获取位置文本并显示加载到列表
	 */
	private void latlngToAddress(LatLng latlng) {
		geoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latlng));
		geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
				if (result == null
						|| result.error != SearchResult.ERRORNO.NO_ERROR) {
					Toast.makeText(localcateActivity.this, "找不到位置！请检查网络~",
							Toast.LENGTH_SHORT).show();
				}
				addMapList(result);// 添加列表
				// Log.i("INFO", "getAddressDetail:"+result.getAddressDetail());
				Log.i("INFO",
						"getAddressDetail:"
								+ result.getAddressDetail().province
								+ result.getAddressDetail().city
								+ result.getAddressDetail().street
								+ result.getAddressDetail().streetNumber
								+ result.getAddressDetail().describeContents());
				Log.i("INFO", "getBusinessCircle:" + result.getBusinessCircle());
				Log.i("INFO",
						"getSematicDescription:"
								+ result.getSematicDescription());
				Log.i("INFO", "describeContents:" + result.describeContents());
				Log.i("INFO", "getCityCode:" + result.getCityCode());
				Log.i("INFO", "toString:" + result.toString());
				actionbar_maptext.setText(result.getSematicDescription());
				// setTitle(result.getAddress());
				Toast.makeText(localcateActivity.this,
						result.getSematicDescription(), 500).show();
			}

			@Override
			public void onGetGeoCodeResult(GeoCodeResult result) {
				String address = result.getAddress();
			}
		});
	}

	/*
	 * 添加到列表
	 */
	private void addMapList(ReverseGeoCodeResult result) {
		final String[] arr_data = {
				result.getAddressDetail().countryName
						+ result.getAddressDetail().city, // 国家+城市
				result.getAddressDetail().province
						+ result.getAddressDetail().city
						+ result.getAddressDetail().street
						+ result.getAddressDetail().streetNumber, // 省市街街道号
				result.getSematicDescription(), // 详细位置
		};
		adapter = new ArrayAdapter<String>(localcateActivity.this,
				android.R.layout.simple_list_item_1, arr_data);
		list_mapselect.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String text = list_mapselect.getItemAtPosition(position) + ""; // 获取列表位置的文本
				actionbar_maptext.setText(text); // 将文本设置到标题输入框
			}
		});
		list_mapselect.setVisibility(View.VISIBLE); // 设置列表可见
		list_mapselect.setAdapter(adapter); // 加载到适配器中
	}

	private void addDestInfoOverlay(LatLng destInfo) {
		mBaiduMap.clear();
		OverlayOptions options = new MarkerOptions().position(destInfo)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.maker))
				.zIndex(5);
		mBaiduMap.addOverlay(options);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
		mBaiduMap.setMyLocationEnabled(true);
		if (!mLocationClient.isStarted())
			mLocationClient.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mBaiduMap.setMyLocationEnabled(false);
		mLocationClient.stop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}

	private class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			MyLocationData data = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(data);
			mLatitude = location.getLatitude();
			mLongtitude = location.getLongitude();
			if (isFirstIn) {
				LatLng latLng = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				isFirstIn = false;
				latlngToAddress(latLng);
			}
		}

	}

	/*
	 * 定位到我的位置
	 */
	private void centerToMyLocation() {
		LatLng latLng = new LatLng(mLatitude, mLongtitude);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.animateMapStatus(msu);
		latlngToAddress(latLng);
		addDestInfoOverlay(latLng);
	}
}
