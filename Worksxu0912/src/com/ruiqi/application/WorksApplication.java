package com.ruiqi.application;

import org.xutils.x;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.ruiqi.service.TimerService;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;

public class WorksApplication extends Application{
	public static int versionCode;
	public LocationClient mLocationClient = null;
	@Override
	public void onCreate() {
		super.onCreate();
		x.Ext.init(this);//xutils的初始化
		//初始化自定义扑捉异常
		//CustomCrash mCustomCrash = CustomCrash.getInstance();
	//	mCustomCrash.setCustomCrashInfo(this);
//		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		 JPushInterface.setDebugMode(false);
		 JPushInterface.init(this);
		 SDKInitializer.initialize(getApplicationContext());
	}
}
