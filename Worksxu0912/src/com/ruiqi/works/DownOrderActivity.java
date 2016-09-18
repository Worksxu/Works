package com.ruiqi.works;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.utils.DateUtils;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.XuDialog;
import com.ruiqi.utils.XuDialog.No;
import com.ruiqi.utils.XuDialog.Yes;
import com.ruiqi.view.CustomDownView;
import com.ruiqi.xuworks.AdvanceActivity;
import com.ruiqi.xuworks.BackBottleOrderInfo;
import com.ruiqi.xuworks.ChangeMessageActivity;
import com.ruiqi.xuworks.CreateGassActivity;
import com.ruiqi.xuworks.NewZhejiuActivity;
import com.ruiqi.xuworks.XuBottleGassCommint;
import com.ruiqi.xuworks.ZheJiuActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DownOrderActivity extends BaseActivity implements Yes,No,ParserData{
	

	private CustomDownView cdv_gass,cdv_test,cdv_peijian,cdv_backBottle,cdv_backgass,cdv_advance;
	private TextView tv_name,tv_phone,tv_card,tv_address,tv_time,tv_type;
	private ImageView img_change;
	String type;// 区分1置换2退瓶
	public static String changebottle = null;//置换瓶

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.down_order_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("客户下单");
		img_change = (ImageView) findViewById(R.id.imageView_downorder_change);
		img_change.setOnClickListener(this);
		cdv_gass = (CustomDownView) findViewById(R.id.down_gass);
		
		cdv_backBottle = (CustomDownView) findViewById(R.id.down_backBottle);
		cdv_test = (CustomDownView) findViewById(R.id.down_test);
		cdv_peijian = (CustomDownView) findViewById(R.id.down_peijian);
		cdv_backgass = (CustomDownView) findViewById(R.id.down_backgass);
		cdv_advance = (CustomDownView) findViewById(R.id.down_advance);
		cdv_advance.setString("预购订单");
		cdv_backBottle.setString("退瓶订单");
		cdv_gass.setString("订气订单");
		cdv_test.setString("体验套餐");
		cdv_peijian.setString("折旧订单");
		cdv_backgass.setString("置换订单");
		cdv_gass.setOnClickListener(this);
		cdv_test.setOnClickListener(this);
		cdv_peijian.setOnClickListener(this);
		cdv_backBottle.setOnClickListener(this);
		cdv_backgass.setOnClickListener(this);
		cdv_advance.setOnClickListener(this);
		tv_address = (TextView) findViewById(R.id.textView_down_address);
		tv_card = (TextView) findViewById(R.id.textView_down_card);
		tv_phone = (TextView) findViewById(R.id.textView_down_phone);
		tv_name = (TextView) findViewById(R.id.textView_down_name);
		tv_time = (TextView) findViewById(R.id.textView_down_time);
		tv_type = (TextView) findViewById(R.id.textView_down_type);
		tv_name.setText((CharSequence) SPUtils.get(DownOrderActivity.this, "oldUserName", ""));
		tv_phone.setText((CharSequence) SPUtils.get(DownOrderActivity.this, "oldUserMobile", ""));
		tv_address.setText((CharSequence) SPUtils.get(DownOrderActivity.this, "oldUserAddress", ""));
		tv_card.setText((CharSequence) SPUtils.get(DownOrderActivity.this, "card_sn", ""));
//		String Date = DateUtils.TimeStampToDate(((String) SPUtils.get(DownOrderActivity.this, "safe_time", ""),"yyyy-MM-dd hh:mm");
//		String Date = DateUtils.TimeStampToDate((String) SPUtils.get(DownOrderActivity.this, "safe_time", ""), "yyyy-MM-dd HH:mm");
		if(SPUtils.get(DownOrderActivity.this, "old_new", "").equals("1")){//新课户
			tv_time.setVisibility(View.GONE);
		}else{
		tv_time.setText((String) SPUtils.get(DownOrderActivity.this, "safe_time", ""));}
		if(SPUtils.get(DownOrderActivity.this, "user_type", "").equals("1")){
//			Log.e("llll_type", SPUtils.get(DownOrderActivity.this, "use_type", ""));
			tv_type.setText("居民用户");
		}else{
			tv_type.setText("商业用户");
		}
	}

	

	@Override
	public Activity getActivity() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public Handler[] initHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.down_gass:
			Intent gass = new Intent(DownOrderActivity.this,CreateGassActivity.class);
			startActivity(gass);
			break;
		case R.id.down_backBottle:
			XuDialog.getInstance().setno(this);
			XuDialog.getInstance().setyes(this);
			XuDialog.getInstance().show(DownOrderActivity.this, "是否确认退瓶", 1);
			break;
		
		case R.id.imageView_downorder_change:
			Intent change = new Intent(DownOrderActivity.this,ChangeMessageActivity.class);
			change.putExtra("kid", (String) SPUtils.get(DownOrderActivity.this, "kid", ""));
			startActivity(change);
			finish();
			
			break;
		case R.id.down_backgass:
			XuDialog.getInstance().setno(this);
			XuDialog.getInstance().setyes(this);
			XuDialog.getInstance().show(DownOrderActivity.this, "是否确认置换", 2);
			
//			Toast.makeText(getActivity(), "此功能未开通", 1).show();
			break;
		case R.id.down_peijian:
			Intent zhejiu = new Intent(DownOrderActivity.this,ZheJiuActivity.class);
			zhejiu.putExtra("type", 1);
			startActivityForResult(zhejiu, 1);
//			startActivityForResult(zhejiu, 1);
			break;
		case R.id.down_advance:// 预购
			Intent advance = new Intent(DownOrderActivity.this,AdvanceActivity.class);
//			advance.putExtra("type", 1);
			startActivity(advance);
			break;
		default:
			break;
		}
	}

	@Override
	public void XuNo(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void XuCallback(int i) {
		// TODO Auto-generated method stub
		if(i == 1){
			type = "1";
			getNumber(type);
		}else{
			type = "2";
			getNumber(type);
		}
		
	}
	
	private HttpUtil httpUtil;
	private void getNumber(String type) {
		// TODO Auto-generated method stub
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		RequestParams params = new RequestParams(RequestUrl.NUllBOTTLE);
		if(!TextUtils.isEmpty((String) SPUtils.get(DownOrderActivity.this, "comment", ""))){
			params.addBodyParameter("comment", (String) SPUtils.get(DownOrderActivity.this, "comment", ""));
		}
		params.addBodyParameter("comment", "0");
		params.addBodyParameter("kid", (String) SPUtils.get(DownOrderActivity.this, "kid", ""));
		params.addBodyParameter("username", (String) SPUtils.get(DownOrderActivity.this, "oldUserName", ""));
		params.addBodyParameter("mobile", (String) SPUtils.get(DownOrderActivity.this, "oldUserMobile", ""));
		params.addBodyParameter("shop_id", (String) SPUtils.get(DownOrderActivity.this, SPutilsKey.SHOP_ID, ""));// 门店id
		params.addBodyParameter("shipper_id", (String) SPUtils.get(DownOrderActivity.this,SPutilsKey.SHIP_ID, ""));// 送气工id
		params.addBodyParameter("shipper_name", (String) SPUtils.get(DownOrderActivity.this, "shipper_name", ""));// 送气工名字
		params.addBodyParameter("shipper_mobile", (String) SPUtils.get(DownOrderActivity.this, SPutilsKey.MOBILLE, ""));// 送气手机号
		params.addBodyParameter("sheng", (String) SPUtils.get(
				DownOrderActivity.this, "sheng", "error"));
		params.addBodyParameter("shi", (String) SPUtils.get(
				DownOrderActivity.this, "shi", "error"));
		params.addBodyParameter("qu",
				(String) SPUtils.get(DownOrderActivity.this, "qu", "error"));
		params.addBodyParameter("cun", (String) SPUtils.get(
				DownOrderActivity.this, "cun", "error"));
		params.addBodyParameter("address", (String) SPUtils.get(DownOrderActivity.this, "oldUserAddress", ""));// 地址
		params.addBodyParameter("Token", SPUtils.get(DownOrderActivity.this, SPutilsKey.TOKEN, 0)+"");
		params.addBodyParameter("type", type);
//		params.addBodyParameter("Token", SPUtils.get(DownOrderActivity.this, SPutilsKey.TOKEN, 0)+"");
		Log.e("lll", params.getStringParams().toString());
		httpUtil.PostHttp(params, 0);
	}

	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("lll", result);
		if(true){
			try {
				JSONObject object = new JSONObject(result);
				int code = object.getInt("resultCode");
				String info = object.getString("resultInfo");
				if(code == 1){
					SPUtils.put(DownOrderActivity.this, "tpnumber", info);
					if(type.equals("1")){
						Intent intent = new Intent(DownOrderActivity.this,BackBottleOrderInfo.class);
						intent.putExtra("depositsn", info);
						intent.putExtra("from", "DownOrder");
						startActivity(intent);
					}else if(type.equals("2")){
						Intent backgass = new Intent(DownOrderActivity.this,WeightActivity.class);
						backgass.putExtra("show", "DownOrderActivity");
						changebottle = "1";
						startActivity(backgass);
					}
					
					
				}else{
					Toast.makeText(DownOrderActivity.this, info, 1).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
