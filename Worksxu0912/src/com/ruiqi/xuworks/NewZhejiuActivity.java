package com.ruiqi.xuworks;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import com.ruiqi.view.SwipeMenuListView;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.R;

public class NewZhejiuActivity extends BaseActivity{
//	public LocationClient mLocationClient = null;
//	public BDLocationListener myListener = new MyLocationListener();
	private Spinner sp_weight,sp_date;
	private ImageView img_soup,img_add;
	private TextView tv_canyeMoney,tv_totalMoney,tv_commint,tv_sure;
	private EditText et_weight;
	private SwipeMenuListView lv_zhejiu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_zhejiu_layout);
		
//		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
//		mLocationClient.registerLocationListener(myListener); // 注册监听函数
//		mLocationClient.start();
	}

	@Override
	public Activity getActivity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Handler[] initHandler() {
		// TODO Auto-generated method stub
		return null;
	}
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// Receive Location
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
		
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
		
			sb.append("\nradius : ");
			sb.append(location.getRadius());
		
			Log.e("BaiduLocationApiDem", sb.toString());
		}

	}

}
