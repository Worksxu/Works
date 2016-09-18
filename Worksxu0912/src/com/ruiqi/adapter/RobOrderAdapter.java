package com.ruiqi.adapter;

import java.text.DecimalFormat;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.ruiqi.bean.Order;
import com.ruiqi.bean.Pensoral;


import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.XuDialog;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.XuDialog.No;
import com.ruiqi.utils.XuDialog.Yes;
import com.ruiqi.works.R;
import com.ruiqi.xuworks.NewOrderInfoActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class RobOrderAdapter extends BaseAdapter implements OnGetGeoCoderResultListener, BDLocationListener,ParserData,No,Yes{
	LayoutInflater inflater;
	List<Order>list;
	Context context;
	String ordeson,kid;
	double distance;
	float distance_li;
	int click;
	public LocationClient mLocationClient = null;
	
	GeoCoder mSearch = null;
	// 起始坐标
	double mLat1;
	double mLon1;
	public RobOrderAdapter(Context context,List<Order>list) {
		// TODO Auto-generated constructor stub
		Log.e("llllllllll_rorb", "jinlaile");
//		SDKInitializer.initialize(getApplicationContext());
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		mLocationClient = new LocationClient(context);// 初始化定位
		mLocationClient.registerLocationListener(this); // 注册监听函数
		mLocationClient.start();
		this.list  = list;
		this.context = context;
		this.inflater = inflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.roborder_itemlayout, null);
//			viewHolder.tv_content = (TextView) convertView.findViewById(R.id.textView_user_type);
			viewHolder.tv_address = (TextView) convertView.findViewById(R.id.textView_roborder_address);
			viewHolder.tv_type = (TextView) convertView.findViewById(R.id.textView_roborder_usertype);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.textView_roborder_time);
			viewHolder.tv_money = (TextView) convertView.findViewById(R.id.textView_roborder_money);
			viewHolder.tv_distance = (TextView) convertView.findViewById(R.id.textView_roborder_distance);
			viewHolder.tv_order = (TextView) convertView.findViewById(R.id.rob_order);
			
//			convertView.setTag(viewHolder);
//		}else{
//			viewHolder = (ViewHolder) convertView.getTag();
//		}
		viewHolder.tv_address.setText(list.get(position).getAddress());
		
		
		Log.e("fdfdsfdsf", "ffdsfd");
		viewHolder.tv_type.setText(list.get(position).getStatus());
		viewHolder.tv_money.setText(list.get(position).getPay_money());
		viewHolder.tv_time.setText(list.get(position).getTime());
		viewHolder.tv_distance.setText(distance_li+""+"公里");
		viewHolder.tv_order.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mSearch.geocode(new GeoCodeOption().city("").address(
						list.get(position).getAddress()));
				click = position;
			}
		});
		}
		return convertView;
	}
	class ViewHolder{
		TextView tv_address,tv_distance,tv_money,tv_type,tv_time,tv_order;
		
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
		mLat1 = arg0.getLatitude();
		sb.append("\nlontitude : ");
		sb.append(arg0.getLongitude());
		mLon1 = arg0.getLongitude();
		sb.append("\nradius : ");
		sb.append(arg0.getRadius());
		Log.e("lllfldfdfl", mLat1 + "");
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub
		if (arg0 == null || arg0.error != SearchResult.ERRORNO.NO_ERROR) {
//			Toast.makeText(context, "抱歉，未能找到结果",
//					Toast.LENGTH_LONG).show();
			return;
		}
		String strInfo = String.format("纬度：%f 经度：%f",
				arg0.getLocation().latitude, arg0.getLocation().longitude);
		LatLng ptStart = new LatLng(mLat1, mLon1);
		LatLng ptEnd = new LatLng(arg0.getLocation().latitude,
				arg0.getLocation().longitude);// 终点坐标
		 distance = DistanceUtil.getDistance(ptStart, ptEnd);
//		 float percent = (float)currentPosition/(float)mDuration;
	        DecimalFormat fnum = new DecimalFormat("##0.0");
	        distance_li =Float.parseFloat(fnum.format(distance/2000));
	        Log.e("llll_jl",  distance_li+"");
	        XuDialog.getInstance().setno(this);
			XuDialog.getInstance().setyes(this);
			XuDialog.getInstance().show(context, "距离:"+ distance_li+""+"公里","抢单","不抢", click);
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("llllll", result);
		try {
			JSONObject object = new JSONObject(result);
			int code = object.getInt("resultCode");
			String info = object.getString("resultInfo");
			if(code == 1){
				Toast.makeText(context, info, 1).show();
				Intent order = new Intent(context,NewOrderInfoActivity.class);
				order.putExtra("ordersn",ordeson);
				order.putExtra("kid", kid);
				context.startActivity(order);
			}else{
				Toast.makeText(context, info, 1).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void XuCallback(int i) {
		// TODO Auto-generated method stub
		Log.e("fdsfdsfdsf_谓之", i+"");
		ordeson = list.get(i).getOrdersn();
		kid = list.get(i).getKid();
		getData(ordeson);
	}

	@Override
	public void XuNo(int i) {
		// TODO Auto-generated method stub
		
	}
	private HttpUtil httpUtil;
	private void getData(String ordeson) {
		// TODO Auto-generated method stub
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
//		int token = (Integer) SPUtils.get(SelfCheckActivity.this, SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.ROBORDERSURE);
//		params.addBodyParameter("type", (String) SPUtils.get(SelfCheckActivity.this, "self_type", ""));
		params.addBodyParameter("shipper_id",  (String) SPUtils.get(context,
				SPutilsKey.SHIP_ID, "error"));
		params.addBodyParameter("shipper_mobile",   (String) SPUtils.get(context,
				SPutilsKey.MOBILLE, "error"));
		params.addBodyParameter("shipper_name",  (String) SPUtils.get(context,
				"shipper_name", "error"));
		params.addBodyParameter("ordersn",  ordeson);
		httpUtil.PostHttp(params, 0);
	}

}
