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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ruiqi.adapter.TypePopupAdapter;
import com.ruiqi.bean.Type;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.view.CustomListView;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.CommintOrder;
import com.ruiqi.works.R;

public class AdvanceActivity extends BaseActivity implements ParserData{
	private TextView tv_next;
	private CustomListView lv_advance;
	private TypePopupAdapter adapter;
	private List<Type> mTypeList ;
	public static List<Type> advance_gass = new ArrayList<Type>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advance_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("创建预购订单");
		lv_advance = (CustomListView) findViewById(R.id.listView_gassTost);
		tv_next = (TextView) findViewById(R.id.tv_gass_next);
		tv_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectgass();
				if(advance_gass.size()>0){
					Commint();
				}else{
					T.show(AdvanceActivity.this, "请选择商品", 1);
				}
			}
		});
		initData();
	}
	
	private HttpUtil httpUtil;
	private void initData() {
		// TODO Auto-generated method stub
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		RequestParams params = new RequestParams(RequestUrl.BOTTLE_TYPE);
		params.addBodyParameter(SPutilsKey.SHOP_ID, (String) SPUtils.get(AdvanceActivity.this,SPutilsKey.SHOP_ID, ""));
		params.addBodyParameter("user_type", (String)SPUtils.get(AdvanceActivity.this, "user_type", ""));
		int token = (Integer) SPUtils.get(AdvanceActivity.this, SPutilsKey.TOKEN, 0);
		params.addBodyParameter("Token",  token+"");
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
		mTypeList = new ArrayList<Type>();
		if(what == 0){
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if (resultCode == 1) {
				
					JSONArray array = obj.getJSONArray("resultInfo");
					for (int j = 0; j < array.length(); j++) {
						
						JSONObject object = array.getJSONObject(j);
						if(object.getString("type").equals("1")){
						String bottle_name = object.getString("typename");
						String price = object.getString("price");
						String id = object.getString("id");
						String norm_id = object.getString("norm_id");
						String type = object.getString("type");
						String name = object.getString("name");
						String yj_price = object.getString("yj_price");
						mTypeList.add(new Type(price,  bottle_name, "0", id, norm_id, type,bottle_name, name, yj_price));
						}
						}
					adapter = new TypePopupAdapter(mTypeList, AdvanceActivity.this);
//					CurrencyUtils.setListViewHeightBasedOnChildren(lv_gass);
					lv_advance.setAdapter(adapter);
					
				
			}
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
		}else{
			Log.e("lll_fdfds", result);
		}
	}
	
	public void selectgass(){
		advance_gass = new ArrayList<Type>();
		for (int i = 0; i < mTypeList.size(); i++) {
			int num = Integer.parseInt(mTypeList.get(i).getNum());
			if(num > 0){
				Type type = new Type();
				type.setPrice(mTypeList.get(i).getPrice());// 商品单价
				type.setWeight(mTypeList.get(i).getWeight());// 商品规格
				type.setName(mTypeList.get(i).getName());// 商品名
				type.setNum(mTypeList.get(i).getNum());// 商品数量
				type.setId(mTypeList.get(i).getId());// 商品Id;
				type.setType(mTypeList.get(i).getType());// 商品Id;
				type.setNorm_id(mTypeList.get(i).getNorm_id());// 商品的Norm_id
				type.setYj_price(Double.parseDouble(mTypeList.get(i).getPrice()) * num+"");// 单类商品总价
				advance_gass.add(type);
				
			}
			
		}
	}
	void Commint(){
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		String shipper_id = (String) SPUtils.get(AdvanceActivity.this, SPutilsKey.SHIP_ID, "error");
		String shop_id = (String) SPUtils.get(AdvanceActivity.this, SPutilsKey.SHOP_ID, "error");
		
		
		RequestParams params = new RequestParams(RequestUrl.NEWCREATEORDER);
		params.addBodyParameter("mobile", (String) SPUtils.get(AdvanceActivity.this, "oldUserMobile", ""));
		params.addBodyParameter("sheng", (String) SPUtils.get(AdvanceActivity.this, "sheng", ""));
		params.addBodyParameter("shi", (String) SPUtils.get(AdvanceActivity.this, "shi", ""));
		params.addBodyParameter("qu", (String) SPUtils.get(AdvanceActivity.this, "qu", ""));
		params.addBodyParameter("cun", (String) SPUtils.get(AdvanceActivity.this, "cun", ""));
		params.addBodyParameter("address", (String) SPUtils.get(AdvanceActivity.this, "oldUserAddress", ""));
		params.addBodyParameter("card_sn", (String) SPUtils.get(AdvanceActivity.this, "card_sn", ""));
		params.addBodyParameter("user_type", (String) SPUtils.get(AdvanceActivity.this, "user_type", ""));
		params.addBodyParameter("shipper_id", shipper_id);
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("shipper_name", (String) SPUtils.get(AdvanceActivity.this, "shipper_name", ""));
		params.addBodyParameter("shipper_mobile", (String) SPUtils.get(AdvanceActivity.this,  SPutilsKey.MOBILLE, ""));
//		params.addBodyParameter("money", tv_goodsMoney.getText().toString());// 商品金额
//		params.addBodyParameter("count_money", youhui_money1+"");// 优惠金额
//		params.addBodyParameter("yh_money", deduction_money+"");// 抵扣券
		params.addBodyParameter("urgent", "0");
		params.addBodyParameter("goodtime", "0");
		params.addBodyParameter("isold", "0");
		params.addBodyParameter("ismore", "0");
		params.addBodyParameter("tc_type", "0");
		params.addBodyParameter("tc_type", "0");
		int token = (Integer) SPUtils.get(AdvanceActivity.this, SPutilsKey.TOKEN, 0);
		params.addBodyParameter("Token",  token+"");
		params.addBodyParameter("username", (String) SPUtils.get(AdvanceActivity.this, "oldUserName", ""));
//		params.addBodyParameter("youhuijson", getdeductionData(deduction));
		params.addBodyParameter("data", getJsonData(advance_gass));// 商品的data串
		
//		Log.e("lll", getJsonData(goods));
//		params.addBodyParameter("peijian", getzhekouJsonData(zhekou));// 折旧物品data串
		httpUtil.PostHttp(params, 1);
//		HttpUtil.PostHttp(params, handler, what);
		Log.e("lll", params.getBodyParams().toString());
	}
	private String getJsonData(List<Type> popupList) {
		JSONArray arr = new JSONArray();
		// 订单
		for (int j = 0; j < popupList.size(); j++) {
			JSONObject obj1 = new JSONObject();
			try {
				obj1.put("good_id", popupList.get(j).getId());
				obj1.put("good_num", popupList.get(j).getNum());
				obj1.put("good_name", popupList.get(j).getName());
				
				obj1.put("good_price",Double.parseDouble(popupList.get(j).getPrice()));
				
				obj1.put("good_type", popupList.get(j).getType());
				obj1.put("good_kind", popupList.get(j).getNorm_id());
				// obj1.put("wb", weightBottle);
				arr.put(obj1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return arr.toString();
	}

	}


