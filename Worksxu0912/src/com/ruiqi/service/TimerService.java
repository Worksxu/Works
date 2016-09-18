package com.ruiqi.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingDeque;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.ruiqi.bean.Weight;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.HttpUtil.ParserData;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class TimerService extends Service implements ParserData,
		BDLocationListener {
	public LocationClient mLocationClient = null;
	// public BDLocationListener myListener = new MyLocationListener();
	double lat, lon;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.e("llll_service", "服务create");
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		initLocation();
//		timer.schedule(task, 1000, 2000 * 60); // 1s后执行task,经过2分再次执行
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span =5000*60;// 每5分钟请求一次
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		mLocationClient.setLocOption(option);
		
		mLocationClient.registerLocationListener(this); // 注册监听函数
		mLocationClient.start();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		Log.e("llll_start", "服务start");

	}

//	Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			if (msg.what == 1) {
//				//
//				Log.e("lll", "服务");// 进行网络请求
//				
//			}
//			super.handleMessage(msg);
//		};
//	};
//	Timer timer = new Timer();
//	TimerTask task = new TimerTask() {
//
//		@Override
//		public void run() {
//			// 需要做的事:发送消息
//			Message message = new Message();
//			message.what = 1;
//			handler.sendMessage(message);
//		}
//	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("llll", result);
	}

	private HttpUtil httpUtil;

	private void getData() {
		// TODO Auto-generated method stub
		httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(TimerService.this, SPutilsKey.TOKEN,
				0);
		RequestParams params = new RequestParams(RequestUrl.LOCATION);

		params.addBodyParameter("Token", token + "");
		params.addBodyParameter("position", getJsonData());
		params.addBodyParameter("shipper_mobile", (String) SPUtils.get(
				TimerService.this, SPutilsKey.MOBILLE, "error"));// 送气工联系方式
		params.addBodyParameter("shipper_name", (String) SPUtils.get(
				TimerService.this, "shipper_name", "error"));// 送气工姓名
		params.addBodyParameter("shipper_id", (String) SPUtils.get(
				TimerService.this, SPutilsKey.SHIP_ID, "error"));// 送气工id
		params.addBodyParameter("shop_id", (String) SPUtils.get(
				TimerService.this, SPutilsKey.SHOP_ID, "error"));// 门店id
		Log.e("lll", params.getStringParams().toString());
		httpUtil.PostHttp(params, 0);
	}

	
	private String getJsonData() {
		JSONArray arr = new JSONArray();
		// 订单

		JSONObject obj1 = new JSONObject();
		try {
			obj1.put("lat", lat+"");
			obj1.put("lon", lon+"");

			// obj1.put("wb", weightBottle);
			arr.put(obj1);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return arr.toString();
	}

	@Override
	public void onReceiveLocation(BDLocation arg0) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer(256);
		sb.append("time : ");
		sb.append(arg0.getTime());
		sb.append("\nerror code : ");
		sb.append(arg0.getLocType());
		sb.append("\nlatitude : ");
		sb.append(arg0.getLatitude());
		lat = arg0.getLatitude();
		sb.append("\nlontitude : ");
		sb.append(arg0.getLongitude());
		lon = arg0.getLongitude();
		sb.append("\nradius : ");
		sb.append(arg0.getRadius());
		getData();
		Log.e("BaiduLocationApiDem", sb.toString());
	}

}
