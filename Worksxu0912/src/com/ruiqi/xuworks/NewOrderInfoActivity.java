package com.ruiqi.xuworks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.ruiqi.adapter.BottleToShopAdapter;
import com.ruiqi.bean.Orderdeail;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.fragment.OrderInfoFragment;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;
import com.ruiqi.utils.XuDialog;
import com.ruiqi.utils.XuDialog.No;
import com.ruiqi.utils.XuDialog.Yes;
import com.ruiqi.view.popupwindow.SelectOrderInfoPopupWindow.PopDismiss;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.CommintOrder;
import com.ruiqi.works.DownOrderActivity;
import com.ruiqi.works.HomePageActivity;
import com.ruiqi.works.NfcActivity;
import com.ruiqi.works.OrderInfoActivity;
import com.ruiqi.works.R;
import com.ruiqi.works.WeightActivity;

public class NewOrderInfoActivity extends BaseActivity implements ParserData,Yes,No,
		OnGetGeoCoderResultListener, BDLocationListener {
	String order_number, status, address, total, name, mobile, yunfei, ordersn,
			kid;// 订单号
	private TextView tv_number, tv_status, tv_name, tv_phone, tv_address,
			tv_goodMoney, tv_yunfei, tv_total, tv_orderStatus, tv_beizhu,
			tv_next, tv_deduction;
	int judge;//判断配件库存
	public static List<Orderdeail> list;// 扫重瓶时需要
	public static List<Orderdeail> mList;// 为确认订单准备
	public static List<Orderdeail> peijianList;// 确认订单的 配件data串
	private List<Orderdeail> peijianjudge;
	private List<TableInfo> mDatas;// 配件库存
	private LinearLayout ll_change;
	double deduction, youhui;
	private ProgressDialog pd;
	private LinearLayout ll_daohang;
	public LocationClient mLocationClient = null;
	Bundle bundle;
	GeoCoder mSearch = null;
	// 起始坐标
	double mLat1;
	double mLon1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.neworder_info_layout);
		pd = new ProgressDialog(NewOrderInfoActivity.this);
		pd.setMessage("正在加载");
		pd.show();
		order_number = getIntent().getStringExtra("ordersn");
		kid = getIntent().getStringExtra("kid");
		SPUtils.put(NewOrderInfoActivity.this, "kid", kid);
		// 初始化搜索模块，注册事件监听
		SDKInitializer.initialize(getApplicationContext());
		mSearch = GeoCoder.newInstance();
		mLocationClient = new LocationClient(getApplicationContext());// 初始化定位
		mLocationClient.registerLocationListener(this); // 注册监听函数
		mLocationClient.start();

		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("订单详情");
		ll_daohang  = (LinearLayout) findViewById(R.id.ll_newOrderInfo_daohang);
		ll_daohang.setOnClickListener(this);
		tv_next = (TextView) findViewById(R.id.orderInfo_nextStep);
		tv_next.setOnClickListener(this);
		ll_change = (LinearLayout) findViewById(R.id.ll_newOrderInfo_change);
		ll_change.setOnClickListener(this);
		tv_number = (TextView) findViewById(R.id.textView_orderInfo_number);
		tv_status = (TextView) findViewById(R.id.tv_newOrderInfo_status);
		tv_total = (TextView) findViewById(R.id.textView_orderInfo_TotalMoney);
		tv_orderStatus = (TextView) findViewById(R.id.textView_orderInfo_status);
		tv_yunfei = (TextView) findViewById(R.id.textView_orderInfo_songqiMoney);// 优惠券
		tv_deduction = (TextView) findViewById(R.id.textView_orderInfo_deductionMoney);// 抵扣券
		tv_phone = (TextView) findViewById(R.id.textView_orderInfo_phone);
		tv_phone.setOnClickListener(this);
		tv_name = (TextView) findViewById(R.id.textView_orderInfo_name);
		tv_goodMoney = (TextView) findViewById(R.id.textView_orderInfo_goodsMoney);
		tv_beizhu = (TextView) findViewById(R.id.textView_orderInfo_beizhu);
		tv_address = (TextView) findViewById(R.id.textView_orderInfo_address);
		tv_address.setOnClickListener(this);
		initData();
		getPeijian();
	}

	private HttpUtil httpUtil;

	private void initData() {
		
		// TODO Auto-generated method stub
		httpUtil = new HttpUtil();
		httpUtil.setParserData((ParserData) this);
		RequestParams params = new RequestParams(RequestUrl.ORDER_INFO);
		params.addBodyParameter("ordersn", order_number);
		Log.e("lll", "订单详情" + params.getStringParams().toString());
		httpUtil.PostHttp(params, 0);
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

	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("lll", result);
		if(what == 0){
		list = new ArrayList<Orderdeail>();
		mList = new ArrayList<Orderdeail>();
		peijianList = new ArrayList<Orderdeail>();
		try {
			JSONObject object = new JSONObject(result);
			int code = object.getInt("resultCode");
			pd.dismiss();
			if (code == 1) {
				JSONObject obj = object.getJSONObject("resultInfo");
				ordersn = obj.getString("ordersn");// 订单号
				status = obj.getString("status"); // 送气状态
				address = obj.getString("address");// 地址
				mobile = obj.getString("mobile"); // 电话
				total = obj.getString("total"); // 商品金额
				yunfei = obj.getString("yunfei"); // 运费
				name = obj.getString("username");
				String use_type = obj.getString("ktype");
				SPUtils.put(NewOrderInfoActivity.this, "user_type", use_type);
				String comment = obj.getString("comment");
				String deduction1 = obj.getString("yh_money");// 抵扣券
				String youhui1 = obj.getString("yhq_money");// 优惠券
				if(SPUtils.get(NewOrderInfoActivity.this, "oldUserName", "").equals(name)&&SPUtils.get(NewOrderInfoActivity.this, "old_new", "").equals("1")){
					SPUtils.put(NewOrderInfoActivity.this, "old_new", "1");// 新客户
				}else{
					SPUtils.put(NewOrderInfoActivity.this, "old_new", "2");// 老客户
				}
				Log.e("llllgfghg", SPUtils.get(NewOrderInfoActivity.this, "old_new", "")+"");
				if (!TextUtils.isEmpty(deduction1)) {
					deduction = Double.parseDouble(deduction1);
				} else {
					deduction = 0;
				}
				if (!TextUtils.isEmpty(youhui1)) {
					youhui = Double.parseDouble(youhui1);
				} else {
					youhui = 0;
				}
				if(!TextUtils.isEmpty(comment)){
					tv_beizhu.setText(comment);
				}
//				tv_beizhu.setText(text);
				tv_yunfei.setText(-youhui + "");
				tv_deduction.setText(-deduction + "");
				tv_address.setText(address);
				tv_goodMoney.setText(total);
				tv_name.setText(name);
				tv_phone.setText(mobile);
//				if((Double.parseDouble(total) - deduction - youhui)<0){
//					
//				}
				tv_total.setText(((Double.parseDouble(total) - deduction - youhui) + ""));// 待改

				tv_number.setText(ordersn);
				SPUtils.put(NewOrderInfoActivity.this, "goodsMoney", total+"");
				SPUtils.put(NewOrderInfoActivity.this, "goodsyouhui", youhui+"");
				SPUtils.put(NewOrderInfoActivity.this, "goodsdeduction", deduction+"");
				SPUtils.put(NewOrderInfoActivity.this, "goodsNumber", ordersn);
				SPUtils.put(NewOrderInfoActivity.this, "goodsName", name);
				SPUtils.put(NewOrderInfoActivity.this, "goodsPhone", mobile);
				SPUtils.put(NewOrderInfoActivity.this, "goodsAddress", address);
				if (status.equals("1")) {
					tv_status.setText("未派发");
					tv_orderStatus.setText("正常订单");
				} else if (status.equals("2")) {
					tv_status.setText("配送中");
					tv_orderStatus.setText("正常订单");
				} else if (status.equals("4")) {
					tv_status.setText("已送达");
					tv_orderStatus.setText("正常订单");
				} else if (status.equals("5")) {
					tv_status.setText("问题订单");
					tv_orderStatus.setText("问题订单");
				}

				JSONArray array = obj.getJSONArray("type");
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj1 = array.getJSONObject(i);
					Orderdeail order = new Orderdeail();
					if (obj1.getString("type").equals("1")) {// 订单瓶的list
						order.setTitle(obj1.getString("title"));
						order.setNum(obj1.getString("num"));
						order.setGoods_price(obj1.getString("money"));
						order.setGoods_kind(obj1.getString("kind_name"));//规格
						order.setGoods_id(obj1.getString("goods_id"));
						order.setNorm_id(obj1.getString("kind"));//瓶规格id

						order.setGoods_type(obj1.getString("type"));
						mList.add(order);
					}else{
						order.setNum(obj1.getString("num"));
						order.setGoods_id(obj1.getString("goods_id"));
						order.setNorm_id(obj1.getString("kind"));
						order.setTitle(obj1.getString("title"));
						peijianList.add(order);
					}
					order.setTitle(obj1.getString("title"));
					order.setNum(obj1.getString("num"));
					order.setGoods_price(obj1.getString("money"));
					order.setGoods_kind(obj1.getString("kind_name"));
					order.setGoods_id(obj1.getString("goods_id"));
					order.setNorm_id(obj1.getString("kind"));
					list.add(order);
				}
				OrderInfoFragment oif = new OrderInfoFragment();
				Bundle bundle = new Bundle();
				bundle.putSerializable("mData", (Serializable) list);
				oif.setArguments(bundle);
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.ll_orderInfo_toast, oif).commit();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}else{
			Log.e("lllll_peijiankucun", result);
			mDatas = new ArrayList<TableInfo>();
			try {
				JSONObject object = new JSONObject(result);
				int code = object.getInt("resultCode");
				if(code == 1){
				pd.dismiss();
				JSONArray array = object.getJSONArray("resultInfo");
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					String name  = obj.getString("name");
					String num = obj.getString("goods_num");
					String type = obj.getString("goods_kind");// 配件id
//					String kid = "0";
//					if(array.length()>0){
//					total_num += Integer.parseInt(num);
//					
//					}
					
					mDatas.add(new TableInfo(name,num,type,"0","0"));
				}
				
				
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_newOrderInfo_change:
			Intent change = new Intent(NewOrderInfoActivity.this,
					ChangeOrderActivity.class);
			change.putExtra("kid", kid);
			change.putExtra("orderson", order_number);
			startActivityForResult(change, 2);
			break;
		case R.id.orderInfo_nextStep:
			judge();
			if (status.equals("5")) {
				T.show(NewOrderInfoActivity.this, "问题订单不能结算", 1);
			} else {
				if(judge == 2){
					XuDialog.getInstance().setno(this);
					XuDialog.getInstance().setyes(this);
					XuDialog.getInstance().show(NewOrderInfoActivity.this, "选择类型","预支付结算","正常结算", 1);
					
				}else{
					T.show(NewOrderInfoActivity.this, "请检查配件库存", 1);
				}
			}
			break;
		case R.id.ll_newOrderInfo_daohang:
			//

			mSearch.setOnGetGeoCodeResultListener(this);
			mSearch.geocode(new GeoCodeOption().city("").address(
					tv_address.getText().toString()));
			break;
		case R.id.textView_orderInfo_phone:
			Log.e("kdsgksdg", "jinlaile ");
			XuDialog.getInstance().setno(this);
			XuDialog.getInstance().setyes(this);
			XuDialog.getInstance().show(NewOrderInfoActivity.this, "是否拨打电话","是","否", 2);
			
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg1 == 2) {
			bundle = new Bundle();
			bundle = arg2.getBundleExtra("change");

			status = bundle.getString("status");
			Log.e("lll", status);
			if (status.equals("1")) {
				tv_status.setText("未派发");
				tv_orderStatus.setText("正常订单");
			} else if (status.equals("2")) {
				tv_status.setText("配送中");
				tv_orderStatus.setText("正常订单");
			} else if (status.equals("4")) {
				tv_status.setText("已送达");
				tv_orderStatus.setText("正常订单");
			} else if (status.equals("5")) {
				tv_status.setText("问题订单");
				tv_orderStatus.setText("问题订单");
			}
			if (!bundle.getString("beizhu").isEmpty()) {
				tv_beizhu.setText(bundle.getString("beizhu"));
			}else{
				tv_beizhu.setText("暂无");
			}

		}

	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub
		if (arg0 == null || arg0.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(NewOrderInfoActivity.this, "抱歉，未能找到结果",
					Toast.LENGTH_LONG).show();
			return;
		}
		String strInfo = String.format("纬度：%f 经度：%f",
				arg0.getLocation().latitude, arg0.getLocation().longitude);
		Log.e("llll_dfdsfd", arg0.getLocation().latitude + "");
		LatLng ptStart = new LatLng(mLat1, mLon1);
		LatLng ptEnd = new LatLng(arg0.getLocation().latitude,
				arg0.getLocation().longitude);// 终点坐标

		// 构建 route搜索参数
		RouteParaOption para = new RouteParaOption().startPoint(ptStart)
				.startName("你的位置").endPoint(ptEnd);
		// .endName("大雁塔")
		// .cityName("西安");

		// RouteParaOption para = new RouteParaOption()
		// .startName("天安门").endName("百度大厦");

		// RouteParaOption para = new RouteParaOption()
		// .startPoint(pt_start).endPoint(pt_end);

		try {

			BaiduMapRoutePlan.setSupportWebRoute(false);
			BaiduMapRoutePlan.openBaiduMapDrivingRoute(para, this);
		} catch (Exception e) {
			e.printStackTrace();
			showDialog();
		}

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub

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

	/**
	 * 提示未安装百度地图app或app版本过低
	 */
	public void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				OpenClientUtil.getLatestBaiduMapApp(NewOrderInfoActivity.this);
			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		mSearch.destroy();// 释放资源
	}

	@Override
	public void XuNo(int i) {
		// TODO Auto-generated method stub
		if(i == 1){
		if(NewOrderInfoActivity.mList.size()>0){
			if(!TextUtils.isEmpty(DownOrderActivity.changebottle)){
				DownOrderActivity.changebottle = null;
			}
			Intent intent = new Intent(NewOrderInfoActivity.this,
					WeightActivity.class);
			intent.putExtra("show", "OrderInfoActivity");
		
			startActivity(intent);
		}else{
			Intent intent = new Intent(NewOrderInfoActivity.this,
					PayMoneyActivity.class);
			
			startActivity(intent);
		}
			
		}
		
	}

	@Override
	public void XuCallback(int i) {
		// TODO Auto-generated method stub
//		if(i == 1){
//			XuDialog.getInstance().show(NewOrderInfoActivity.this, "选择支付类型","现金支付","网上支付", 2);
//		}else{
		if(i == 2){
			Intent intent = new Intent(Intent.ACTION_CALL, Uri
					.parse("tel:" + tv_phone.getText().toString()));
			startActivity(intent);
		}else{
		
		Intent intent = new Intent(NewOrderInfoActivity.this,
				AdvanceCommintActivity.class);
		intent.putExtra("youhui", youhui);//活动优惠
		intent.putExtra("deduction", deduction);//用户优惠
		intent.putExtra("goods", Double.parseDouble(total));
		startActivity(intent);
		
		}
		
	}
	public void getPeijian() {
		
		mDatas = new ArrayList<TableInfo>();
		httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(NewOrderInfoActivity.this, SPutilsKey.TOKEN, 0);
		String shipid = (String) SPUtils.get(NewOrderInfoActivity.this,
				SPutilsKey.SHIP_ID, "error");
		RequestParams params = new RequestParams(RequestUrl.PJ_STOCK);
		params.addBodyParameter("shipper_id", shipid);
		params.addBodyParameter("Token", token + "");
		httpUtil.PostHttp(params, 1);
	}
	void judge(){
		
		int judge_num = 0;
		if( peijianList.size()!=0){
			peijianjudge = peijianList;
			Log.e("peijianjudge", peijianjudge.size()+"");
		if(mDatas.size()!=0){
			
		
		
			
			for (int j = 0; j < peijianList.size(); j++) {
				String name_id = peijianList.get(j).getGoods_id();
				int num = Integer.parseInt(peijianList.get(j).getNum());
				for (int i = 0; i < mDatas.size(); i++) {
				Log.e("llll_peijian", mDatas.get(i).getOrderStatus());
//				Log.e("llll_订单", peijianList.get(j).getGoods_id());
				if(mDatas.get(i).getOrderStatus().equals(peijianList.get(j).getGoods_id())&&!(Integer.parseInt(mDatas.get(i).getOrderMoney())<Integer.parseInt(peijianList.get(j).getNum()))){
					
					Log.e("llll_订单_十多个方便", "相同进入");
					judge_num ++;
					
					Log.e("peijian_shuliang ",judge_num+"");
					if(peijianList.size()==judge_num){
						judge = 2;
						
					}else{
						judge = 1;
					}
				}
//				else if(mDatas.get(i).getOrderStatus().equals(peijianList.get(j).getGoods_id())&&Integer.parseInt(mDatas.get(i).getOrderMoney())<Integer.parseInt(peijianList.get(j).getNum())){
//					judge = 1;
//					return;
//				}
			}
		}
			Log.e("llll_订单_shuliang ", judge_num+"");
		}else{
			judge = 1;
		}
		}else{
			judge = 2;
		}
	}
	
}
