package com.ruiqi.xuworks;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.bean.TableInfo;
import com.ruiqi.fragment.BackSureFragment;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.HomePageActivity;
import com.ruiqi.works.NfcActivity;
import com.ruiqi.works.R;

public class XuBottleGassCommint extends BaseActivity implements ParserData{
	private TextView tv_yuqi,tv_yj,tv_total,tv_sure;
	private List<TableInfo> mList;
	double yuqi,weight;
	double yuqi_weight = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bottlegass_commint_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("退瓶结算");
		mList = (List<TableInfo>) getIntent().getBundleExtra("bundle").getSerializable("mList");
		yuqi = getIntent().getBundleExtra("bundle").getDouble("yuqi");
		weight = getIntent().getBundleExtra("bundle").getDouble("weight");
		tv_total = (TextView) findViewById(R.id.textView_xuback_Toatal);
		tv_yuqi = (TextView) findViewById(R.id.textView_xuback_yuqi);
		tv_yuqi.setText(yuqi+"");
		tv_yj = (TextView) findViewById(R.id.textView_xuback_yj);
		tv_yj.setText(weight+"");
		tv_total.setText((yuqi+weight)+"");
		tv_sure = (TextView) findViewById(R.id.tv_xuback_sure);
		tv_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				commintData();
			}
		});
		BackSureFragment bsf = new BackSureFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("mData", (Serializable) mList);
		bsf.setArguments(bundle);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.ll_xuback_toast, bsf).commit();
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
		Log.e("lll_result", result);
		try {
			JSONObject object = new JSONObject(result);
			int code = object.getInt("resultCode");
			String info = object.getString("resultInfo");
			if (code == 1) {
				Toast.makeText(XuBottleGassCommint.this, info, 1).show();
				Intent home = new Intent(XuBottleGassCommint.this,
						HomePageActivity.class);
				startActivity(home);
				if(NfcActivity.mDataBottle != null){
					NfcActivity.mDataBottle = null;
				}
				
				finish();
			} else {
				Toast.makeText(XuBottleGassCommint.this, info, 1).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private HttpUtil httpUtil;

	private void commintData() {
		// TODO Auto-generated method stub
		httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(XuBottleGassCommint.this,
				SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.CHANGEORDER);

		String shop_id = (String) SPUtils.get(XuBottleGassCommint.this,
				SPutilsKey.SHOP_ID, "error");
//		params.addBodyParameter("username", (String) SPUtils.get(XuBottleGassCommint.this,
//				"oldUserName", "error"));// 客户姓名
		params.addBodyParameter("yq_money", tv_yuqi.getText().toString());
		params.addBodyParameter("bottle_text", getJsonDataChange(mList));
		params.addBodyParameter("token", token + "");
		params.addBodyParameter("money", tv_total.getText().toString());
		
		params.addBodyParameter("deposit", tv_yj.getText().toString());
		params.addBodyParameter("depositsn", (String) SPUtils.get(XuBottleGassCommint.this, "tpnumber", ""));// 单号
		params.addBodyParameter("comment", "");
		params.addBodyParameter("type", "1");// 2dai表置换
		params.addBodyParameter("bottle_data", getJsonDataBad(mList));// 故障瓶data串
//		params.addBodyParameter("change_data", getJsonDataChange(weightDatas));// 新换瓶data串
		params.addBodyParameter("weight", yuqi_weight+"");
//		params.addBodyParameter("shipper_id", (String) SPUtils.get(
//				XuBottleGassCommint.this, SPutilsKey.SHIP_ID, "error"));
//		params.addBodyParameter("shop_id", shop_id);
//		params.addBodyParameter("shipper_name", (String) SPUtils.get(
//				XuBottleGassCommint.this, "shipper_name", "error"));
//		params.addBodyParameter("shipper_mobile", (String) SPUtils.get(
//				XuBottleGassCommint.this, SPutilsKey.MOBILLE, "error"));
//		params.addBodyParameter("kid", (String) SPUtils.get(
//				XuBottleGassCommint.this, "kid", "error"));
//		params.addBodyParameter("mobile", (String) SPUtils.get(
//				XuBottleGassCommint.this, "oldUserMobile", "error"));
//		params.addBodyParameter("sheng", (String) SPUtils.get(
//				XuBottleGassCommint.this, "sheng", "error"));
//		params.addBodyParameter("shi", (String) SPUtils.get(
//				XuBottleGassCommint.this, "shi", "error"));
//		params.addBodyParameter("qu",
//				(String) SPUtils.get(XuBottleGassCommint.this, "qu", "error"));
//		params.addBodyParameter("cun", (String) SPUtils.get(
//				XuBottleGassCommint.this, "cun", "error"));
//		params.addBodyParameter("address", (String) SPUtils.get(
//				XuBottleGassCommint.this, "oldUserAddress", "error"));
		Log.e("llll_tui瓶", params.getStringParams().toString());
		httpUtil.PostHttp(params, 0);
	}

	private String getJsonDataBad(List<TableInfo> popupList) {// 故障瓶data串
		JSONArray arr = new JSONArray();
		// 订单
		for (int j = 0; j < popupList.size(); j++) {
			yuqi_weight += Double.parseDouble(popupList.get(j).getYuqi());
			JSONObject obj1 = new JSONObject();
			try {
				obj1.put("number", popupList.get(j).getOrderStatus());// 钢印号
				obj1.put("xinpian", popupList.get(j).getXinpian());// 芯片号
				obj1.put("type", "1");
				obj1.put("weight", popupList.get(j).getYuqi());
				obj1.put("price", popupList.get(j).getKid());
				obj1.put("deposit", popupList.get(j).getOrderNum());//yj
				obj1.put("good_name", popupList.get(j).getOrderTime());//规格
				
				arr.put(obj1);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return arr.toString();
	}
	private String getJsonDataBad() {// 故障瓶data串
		JSONArray arr = new JSONArray();
		// 订单
		for (int j = 0; j < 3; j++) {
//			yuqi_weight += Double.parseDouble(popupList.get(j).getYuqi());
			JSONObject obj1 = new JSONObject();
			try {
				obj1.put("number", "xxxx"+j);// 钢印号
				obj1.put("xinpian", "yyyy"+j);// 芯片号
				obj1.put("type", "1");
				obj1.put("weight", j+"");
				obj1.put("price", j+"");
				obj1.put("deposit", j+"");//yj
				
				arr.put(obj1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		return arr.toString();
	}
	private String getJsonDataChange(List<TableInfo> popupList) {// 换瓶data串
		JSONArray arr = new JSONArray();
		// 订单
		for (int j = 0; j < popupList.size(); j++) {

			arr.put(popupList.get(j).getXinpian());// 芯片号

		}
		return arr.toString();
	}

}
