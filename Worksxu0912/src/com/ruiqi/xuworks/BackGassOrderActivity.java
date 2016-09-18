package com.ruiqi.xuworks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.Table.OrderTable;
import com.ruiqi.Table.TabAdapter;
import com.ruiqi.Table.TabRow;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.bean.Type;
import com.ruiqi.bean.Weight;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.XuDialog;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.XuDialog.No;
import com.ruiqi.utils.XuDialog.Yes;
import com.ruiqi.view.CustomListView;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.DownOrderActivity;
import com.ruiqi.works.HomePageActivity;
import com.ruiqi.works.NfcActivity;
import com.ruiqi.works.R;

public class BackGassOrderActivity extends BaseActivity implements ParserData,Yes,No {
	private CustomListView lv_bad, lv_good;
	private TextView tv_sure;
	public TabAdapter adapter;
	public List<TabRow> table;
	private List<TableInfo> weightDatas;// 重瓶列表list
	private List<TableInfo> badDatas;// 故障瓶列表list

	// int old_num;
	int tag;//判定重瓶和钢瓶芯片号和数目是否一致:1,不能提交,其他可以
	String title_weight[] = { "芯片号", "规格", "钢印号" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.back_gass_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("置换瓶确认");
		tv_sure = (TextView) findViewById(R.id.tv_changebottle_sure);
		XuDialog.getInstance().setno(this);
		XuDialog.getInstance().setyes(this);
		tv_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				judge();
				if(tag == 1){
					Toast.makeText(BackGassOrderActivity.this, "规格类型或数量不符", Toast.LENGTH_SHORT).show();
				}else{
					
					XuDialog.getInstance().show(BackGassOrderActivity.this, "选择置换类型","正常瓶","故障瓶", 1);
				
				}
			}

		});
		lv_bad = (CustomListView) findViewById(R.id.listView_backgass_bad);
		lv_good = (CustomListView) findViewById(R.id.listView_backgass_good);
		weightDatas = new ArrayList<TableInfo>();
		badDatas = new ArrayList<TableInfo>();
		if (NfcActivity.mDataWeight != null
				&& NfcActivity.mDataWeight.size() > 0) {
			for (int i = 0; i < NfcActivity.mDataWeight.size(); i++) {
				String xinpian = NfcActivity.mDataWeight.get(i).getXinpian();
				String type = NfcActivity.mDataWeight.get(i).getType();
				String status = NfcActivity.mDataWeight.get(i).getStatus();// 规格
				weightDatas.add(new TableInfo(xinpian, type, status));
				
			}
			table = new OrderTable().addData(weightDatas, title_weight);
			adapter = new TabAdapter(BackGassOrderActivity.this, table);
			lv_good.setAdapter(adapter);
		}
		if (NfcActivity.mDataNull != null && NfcActivity.mDataNull.size() > 0) {
			for (int i = 0; i < NfcActivity.mDataNull.size(); i++) {
				String xinpian = NfcActivity.mDataNull.get(i).getXinpian();
				String type = NfcActivity.mDataNull.get(i).getType();
				String status = NfcActivity.mDataNull.get(i).getStatus();
				badDatas.add(new TableInfo(xinpian, type, status));
				
			}
			table = new OrderTable().addData(badDatas, title_weight);
			adapter = new TabAdapter(BackGassOrderActivity.this, table);
			lv_bad.setAdapter(adapter);
		}
		// table = new OrderTable().
		// adapter = new TabAdapter(BackGassOrderActivity.this, table);
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

	// {"resultCode":1,"resultInfo":"\u521b\u5efa\u6210\u529f"}

	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("lll_result", result);
		try {
			JSONObject object = new JSONObject(result);
			int code = object.getInt("resultCode");
			String info = object.getString("resultInfo");
			if (code == 1) {
				Toast.makeText(BackGassOrderActivity.this, info, 1).show();
				Intent home = new Intent(BackGassOrderActivity.this,
						HomePageActivity.class);
				startActivity(home);
				if(NfcActivity.mDataNull != null){
					NfcActivity.mDataNull = null;
				}
				if(NfcActivity.mDataWeight != null){
					NfcActivity.mDataWeight = null;
				}
				if(!TextUtils.isEmpty(DownOrderActivity.changebottle)){
					DownOrderActivity.changebottle = null;
				}
				finish();
			} else {
				Toast.makeText(BackGassOrderActivity.this, info, 1).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private HttpUtil httpUtil;

	private void commintData(String bottle_type) {
		// TODO Auto-generated method stub
		httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(BackGassOrderActivity.this,
				SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.CHANGEORDER);

		String shop_id = (String) SPUtils.get(BackGassOrderActivity.this,
				SPutilsKey.SHOP_ID, "error");
		params.addBodyParameter("token", token + "");
		params.addBodyParameter("money", "0");
		params.addBodyParameter("weight", "0");
		params.addBodyParameter("deposit", "0");
		params.addBodyParameter("comment", "");
		params.addBodyParameter("depositsn", (String) SPUtils.get(BackGassOrderActivity.this, "tpnumber", ""));// 单号
//		params.addBodyParameter("username", (String) SPUtils.get(BackGassOrderActivity.this,
//				"oldUserName", "error"));
		params.addBodyParameter("ya_money", "0");
		params.addBodyParameter("bottle_text", getJsonDataChange(badDatas));
		params.addBodyParameter("type", "2");// 2dai表置换
		params.addBodyParameter("bottle_type", bottle_type);// 2dai表置换
		params.addBodyParameter("bottle_data", getJsonDataBad(badDatas));// 故障瓶data串
		params.addBodyParameter("change_data", getJsonDatagood(weightDatas));// 新换瓶data串
//		params.addBodyParameter("shipper_id", (String) SPUtils.get(
//				BackGassOrderActivity.this, SPutilsKey.SHIP_ID, "error"));
//		params.addBodyParameter("shop_id", shop_id);
//		params.addBodyParameter("shipper_name", (String) SPUtils.get(
//				BackGassOrderActivity.this, "shipper_name", "error"));
//		params.addBodyParameter("shipper_mobile", (String) SPUtils.get(
//				BackGassOrderActivity.this, SPutilsKey.MOBILLE, "error"));
//		params.addBodyParameter("kid", (String) SPUtils.get(
//				BackGassOrderActivity.this, "kid", "error"));
//		params.addBodyParameter("mobile", (String) SPUtils.get(
//				BackGassOrderActivity.this, "oldUserMobile", "error"));
//		params.addBodyParameter("sheng", (String) SPUtils.get(
//				BackGassOrderActivity.this, "sheng", "error"));
//		params.addBodyParameter("shi", (String) SPUtils.get(
//				BackGassOrderActivity.this, "shi", "error"));
//		params.addBodyParameter("qu",
//				(String) SPUtils.get(BackGassOrderActivity.this, "qu", "error"));
//		params.addBodyParameter("cun", (String) SPUtils.get(
//				BackGassOrderActivity.this, "cun", "error"));
//		params.addBodyParameter("address", (String) SPUtils.get(
//				BackGassOrderActivity.this, "oldUserAddress", "error"));
		Log.e("llll_置换", params.getStringParams().toString());
		httpUtil.PostHttp(params, 0);
	}

	private String getJsonDataBad(List<TableInfo> popupList) {// 故障瓶data串
		JSONArray arr = new JSONArray();
		// 订单
		for (int j = 0; j < popupList.size(); j++) {
			JSONObject obj1 = new JSONObject();
			try {
				obj1.put("number", popupList.get(j).getOrderStatus());// 钢印号
				obj1.put("xinpian", popupList.get(j).getOrderNum());// 芯片号
				obj1.put("type", "4");
				obj1.put("weight", "0");
				obj1.put("price", "0");
				obj1.put("deposit", "0");
				obj1.put("good_name", popupList.get(j).getOrderMoney());
				arr.put(obj1);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return arr.toString();
	}
	private String getJsonDatagood(List<TableInfo> popupList) {//钢瓶data串
		JSONArray arr = new JSONArray();
		// 订单
		for (int j = 0; j < popupList.size(); j++) {
			JSONObject obj1 = new JSONObject();
			try {
				obj1.put("number", popupList.get(j).getOrderStatus());// 钢印号
				obj1.put("xinpian", popupList.get(j).getOrderNum());// 芯片号
				obj1.put("type", "2");
				obj1.put("weight", "0");
				obj1.put("price", "0");
				obj1.put("deposit", "0");
				obj1.put("good_name", popupList.get(j).getOrderMoney());
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

			arr.put(popupList.get(j).getOrderNum());// 芯片号

		}
		return arr.toString();
	}
	/**
	 * 判断钢瓶
	 */
	public void judge(){
		if(weightDatas != null&&badDatas != null&&weightDatas.size()>0&&badDatas.size()>0){
			int g = 0;
			 if(weightDatas.size() != badDatas.size()){
				Log.e("fsdfsafasd", "进入");
				tag = 1;
			}
		for (int i = 0; i < weightDatas.size(); i++) {
			String xinpian  = weightDatas.get(i).getOrderNum();
//			Log.e("llllllllllll", xinpian);
			for (int j = 0; j < badDatas.size(); j++) {
				if(xinpian.equals(badDatas.get(j).getOrderNum())){
					tag = 1;
					
				}else if(weightDatas.get(i).getOrderMoney().equals(badDatas.get(j).getOrderMoney())){
					g++;
				}

			}
			
		}
		if(weightDatas.size() == badDatas.size()&&g<weightDatas.size()){
			tag = 1;
		}
		}else{
			tag = 1;
		}
	}

	@Override
	public void XuNo(int i) {
		// TODO Auto-generated method stub
		commintData("1");
	}

	@Override
	public void XuCallback(int i) {
		// TODO Auto-generated method stub
		commintData("2");
	}

}
