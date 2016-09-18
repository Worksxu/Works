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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.adapter.OldBottleBackAdapter;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.bean.Weight;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.HomePageActivity;
import com.ruiqi.works.R;

public class BackShopOldApply extends BaseActivity implements ParserData{
	private TextView tv_yuqi, tv_select_num, tv_totalnum, tv_weight,
	tv_commint;
	private ListView lv_check;
	private LinearLayout ll_weight,ll_old;
	private List<Weight> list;// 折旧list
	private List<Weight> judge_list;// 判断的list
	private OldBottleBackAdapter adapter;
	String bottle_data;
	int total_num;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backshop_apply_layout);
		
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("折旧瓶回库申请");
//		getIntent().getStringExtra("judge")
		if(TextUtils.isEmpty(getIntent().getStringExtra("judge"))){
			judge_list = new ArrayList<Weight>();
		}else{
			getList(getIntent().getStringExtra("judge"));
		}
		tv_select_num = (TextView) findViewById(R.id.tv_backshop_apply_selectnum);
		tv_totalnum = (TextView) findViewById(R.id.tv_backshop_apply_totalnum);
		tv_weight = (TextView) findViewById(R.id.tv_backshop_apply_selectweight);
		tv_commint = (TextView) findViewById(R.id.tv_backshop_apply_commint);
		lv_check = (ListView) findViewById(R.id.listView_backshop_apply);
		ll_weight = (LinearLayout) findViewById(R.id.ll_backshop_apply_weight);
		
		ll_old = (LinearLayout) findViewById(R.id.ll_backshop_apply_old);
		ll_old.setVisibility(View.VISIBLE);
		ll_weight.setVisibility(View.GONE);
		tv_select_num.setVisibility(View.GONE);
		tv_commint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(total_num==0){
					Toast.makeText(BackShopOldApply.this, "没有可回的瓶", Toast.LENGTH_SHORT).show();
				}else{
					if(judge_list.size()>0){
						Toast.makeText(BackShopOldApply.this, "您还有未确认的回库记录,请联系门店确认", 1).show();
					}else{
				commintData();
					}
				}
			}
		});
		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub
		list = new ArrayList<Weight>();
		httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		String shipid = (String) SPUtils.get(BackShopOldApply.this, SPutilsKey.SHIP_ID,
				"error");
		RequestParams params = new RequestParams(RequestUrl.STOCK_LIST);
		params.addBodyParameter("shipper_id", shipid);
		params.addBodyParameter("Token", (Integer) SPUtils.get(BackShopOldApply.this, SPutilsKey.TOKEN, 0)+"");
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
		if(what == 0){
			
			try {
				JSONObject obj = new JSONObject(result);
				int resultCode = obj.getInt("resultCode");
				if(resultCode == 1){
				JSONObject obj1 = obj.getJSONObject("resultInfo");
//				String total = obj1.getString("total");
				JSONArray array = obj1.getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					System.out.println("==================");
					JSONObject object = array.getJSONObject(i);
					if(object.getString("typeid").equals("3")){// 折旧
						
				
					String title = object.getString("title");
					String typename = object.getString("typename");
					String typeid = object.getString("typeid");
					int num = object.getInt("num");
					total_num += num;
					String kind = object.getString("is_open");// 规格id
					list.add(new Weight(title, num+"", kind));
					}
					
					tv_totalnum.setText("统计:"+total_num+"");
					tv_weight.setText("共计:"+total_num+""+"瓶");
					adapter = new OldBottleBackAdapter(BackShopOldApply.this, list);
					lv_check.setAdapter(adapter);
				}
			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}else{
			try {
				JSONObject object = new JSONObject(result);
				int code = object.getInt("resultCode");
				String info = object.getString("resultInfo");
				if (code == 1) {
					Toast.makeText(BackShopOldApply.this, info, 1).show();
					Intent home = new Intent(BackShopOldApply.this, BackShopActivity.class);
					startActivity(home);
					finish();
				} else {
					Toast.makeText(BackShopOldApply.this, info, 1).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	// 钢瓶回库接口
		private HttpUtil httpUtil;

		private void commintData() {
			// TODO Auto-generated method stub
			httpUtil = new HttpUtil();
			httpUtil.setParserData(this);
			int token = (Integer) SPUtils.get(BackShopOldApply.this, SPutilsKey.TOKEN, 0);
			String shop_id = (String) SPUtils.get(BackShopOldApply.this,
					SPutilsKey.SHOP_ID, "error");
			String shiper_id = (String) SPUtils.get(BackShopOldApply.this,
					SPutilsKey.SHIP_ID, "error");
			RequestParams params = new RequestParams(RequestUrl.BACKSHOP);

			// params.addBodyParameter("shop_id", shop_id);
			params.addBodyParameter("shipper_name",
					(String) SPUtils.get(BackShopOldApply.this, "shipper_name", ""));
			params.addBodyParameter("shop_id", shop_id);
			params.addBodyParameter("shipper_id", shiper_id);
			params.addBodyParameter("Token", token + "");

			params.addBodyParameter("qbottle", getGsonStringInfo());
			Log.e("lll", params.getStringParams().toString());
			httpUtil.PostHttp(params, 1);
		}
		public String getGsonStringInfo() {
			JSONArray jsoa = new JSONArray();
			for (int i = 0; i < list.size(); i++) {

				JSONObject jsob = new JSONObject();
				try {
					jsob.put("type_id", list.get(i).getStatus());// 规格id
					jsob.put("type_num", list.get(i).getType());// 数量
					jsob.put("type_name", list.get(i).getXinpian());//规格名 
				} catch (JSONException e) {
					e.printStackTrace();
				}
				jsoa.put(jsob);
			}
			return jsoa.toString();
		}
		public void getList(String bota){
			Log.e("llll_sfdsjkfgd", bota);
			judge_list = new ArrayList<Weight>();
			try {
				JSONArray array = new JSONArray(bota);
				for (int i = 0; i < array.length(); i++) {
					JSONObject  object = array.getJSONObject(i);
					String number = object.getString("type_name");
					String xinpian = object.getString("type_num");
					judge_list.add(new Weight(xinpian, number, "0"));
				}
				Log.e("llll_sfdsjkfgd", judge_list.size()+"");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

}
