package com.ruiqi.xuworks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.ruiqi.adapter.CheckBottleAdapter;
import com.ruiqi.adapter.CheckBottleAdapter.ViewHolder;
import com.ruiqi.bean.Weight;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.XuDialog.No;
import com.ruiqi.utils.XuDialog.Yes;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.XuDialog;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.HomePageActivity;
import com.ruiqi.works.R;

public class BackShopApplyActivity extends BaseActivity implements ParserData {
	private List<Weight> list;// 全部的list
	private List<Weight> select_list;// 选中的list
	private List<Weight> judge_list;// 判断的list
	private CheckBottleAdapter adapter;
	private TextView tv_yuqi, tv_select_num, tv_totalnum, tv_weight,
			tv_commint;
	private ListView lv_check;
	int num = 0;
	int tag;
	String is_open;// 判断空重0空1重,2guzhang
	String bottle_data;
	HashMap<Weight, Integer> info = new HashMap<Weight, Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backshop_apply_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		is_open = getIntent().getStringExtra("is_open");
//		if(TextUtils.isEmpty(getIntent().getStringExtra("judge"))){
//			judge_list = new ArrayList<Weight>();
//		}
//		bottle_data = getIntent().getStringExtra("judge");
		tv_yuqi = (TextView) findViewById(R.id.tv_backshop_apply_yuqi);
		tv_select_num = (TextView) findViewById(R.id.tv_backshop_apply_selectnum);
		tv_totalnum = (TextView) findViewById(R.id.tv_backshop_apply_totalnum);
		tv_weight = (TextView) findViewById(R.id.tv_backshop_apply_selectweight);
		tv_commint = (TextView) findViewById(R.id.tv_backshop_apply_commint);
		if (is_open.equals("1")) {
			tv_yuqi.setVisibility(View.GONE);
			tv_weight.setVisibility(View.GONE);
			setTitle("重瓶回库申请");
			if(TextUtils.isEmpty(getIntent().getStringExtra("judge"))){
				judge_list = new ArrayList<Weight>();
			}else{
				getList(getIntent().getStringExtra("judge"));
			}
		}else if(is_open.equals("0")){
			setTitle("空瓶回库申请");
			if(TextUtils.isEmpty(getIntent().getStringExtra("judge"))){
				judge_list = new ArrayList<Weight>();
			}else{
				getList(getIntent().getStringExtra("judge"));
			}
			
		}else{
			if(TextUtils.isEmpty(getIntent().getStringExtra("judge"))){
				judge_list = new ArrayList<Weight>();
			}else{
				getList(getIntent().getStringExtra("judge"));
			}
			
			setTitle("故障瓶回库申请");
		}
		tv_commint.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				judge();//判定方法
				if(select_list == null){
					Toast.makeText(BackShopApplyActivity.this, "请选择钢瓶", 1).show();
				}else{
					if(judge_list.size() > 0){
						Toast.makeText(BackShopApplyActivity.this, "您还有未确认的回库记录,请联系门店确认", Toast.LENGTH_SHORT).show();
					}else{
				commintData();
				}
				}
			}
		});
		lv_check = (ListView) findViewById(R.id.listView_backshop_apply);
		getData();
	}

	/**
	 * 获取库存空重瓶的信息
	 */
	private HttpUtil httpUtil;

	private void getData() {
		// TODO Auto-generated method stub
		httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(BackShopApplyActivity.this,
				SPutilsKey.TOKEN, 0);
		String shop_id = (String) SPUtils
				.get(this, SPutilsKey.SHOP_ID, "error");
		String shiper_id = (String) SPUtils.get(this, SPutilsKey.SHIP_ID,
				"error");
		RequestParams params = new RequestParams(RequestUrl.LOGIN_BOTTLELIST);

		SPUtils.get(this, SPutilsKey.SHIP_ID, "");

		// params.addBodyParameter("shop_id", shop_id);

		params.addBodyParameter("shipper_id", shiper_id);
		params.addBodyParameter("Token", token + "");
		Log.e("lllparams", params.getStringParams().toString());
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

		if (what == 0) {
			try {
				JSONObject object = new JSONObject(result);
				int code = object.getInt("resultCode");
				String info = object.getString("resultInfo");
				Log.e("lll", result);
				if (code == 1) {
					list = new ArrayList<Weight>();
					JSONArray arr = object.getJSONArray("resultInfo");
					
					for (int i = 0; i < arr.length(); i++) {
						JSONObject obj = arr.getJSONObject(i);
						Weight wt = new Weight();
						if (obj.getString("is_open").equals(is_open)) {
							wt.setXinpian(obj.getString("xinpian"));
							wt.setType(obj.getString("type"));
							wt.setType_name(obj.getString("type_name"));
							wt.setStatus(obj.getString("is_open"));// 瓶的状态0空1重
							wt.setXuliehao(obj.getString("number"));// 钢瓶码
							list.add(wt);
						

						}

					}

					adapter = new CheckBottleAdapter(
							BackShopApplyActivity.this, list);
					tv_totalnum.setText("统计:" + list.size() + "");
				
					lv_check.setAdapter(adapter);
					
					lv_check.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							ViewHolder holder = (ViewHolder) view.getTag();
							holder.cb_select.toggle();
							// 将CheckBox的选中状况记录下来
							adapter.getIsSelected().put(position,
									holder.cb_select.isChecked());
							getSelectData();
						}
					});

				} else {
					Toast.makeText(BackShopApplyActivity.this, info, 1).show();
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
				if(code == 1){
					Toast.makeText(BackShopApplyActivity.this, info, 1).show();
					Intent home = new Intent(BackShopApplyActivity.this,BackShopActivity.class);
					startActivity(home);
					finish();
				}else{
					Toast.makeText(BackShopApplyActivity.this, info, 1).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	void getSelectData() {
		select_list = new ArrayList<Weight>();
		HashMap<Integer, Boolean> map = CheckBottleAdapter.getIsSelected();
		info = new HashMap<Weight, Integer>();
		if (map.size() > 0) {
			num = 0;
			for (int i = 0; i < map.size(); i++) {
				if (map.get(i)) {
					if (map.size() > 0) {
						num += 1;
					}
					info.put(list.get(i), i);

				}

			}
			tv_select_num.setText("已选" + num + "" + "瓶");
		}
		for (Entry<Weight, Integer> entry : info.entrySet()) {
			Weight key = entry.getKey();
			select_list.add(key);

		}

	}

	// 钢瓶回库接口
	private void commintData() {
		// TODO Auto-generated method stub
		Log.e("lll", "1");
		httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(BackShopApplyActivity.this,
				SPutilsKey.TOKEN, 0);
		String shop_id = (String) SPUtils
				.get(this, SPutilsKey.SHOP_ID, "error");
		String shiper_id = (String) SPUtils.get(this, SPutilsKey.SHIP_ID,
				"error");
		RequestParams params = new RequestParams(RequestUrl.BACKSHOP);

		SPUtils.get(this, SPutilsKey.SHIP_ID, "");

		params.addBodyParameter("shipper_name", (String) SPUtils.get(
				BackShopApplyActivity.this, "shipper_name", ""));
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("shipper_id", shiper_id);
		params.addBodyParameter("Token", token + "");

		if (is_open.equals("1")) {// 重瓶
			params.addBodyParameter("bottle", getGsonStringInfo());

			params.addBodyParameter("bottle_data",
					getGsonStringInfo(select_list));

		} else if (is_open.equals("0")) {// 空瓶
			Log.e("lll", "4");
			params.addBodyParameter("kbottle", getGsonStringInfo());
			params.addBodyParameter("kbottle_data",
					getGsonStringInfo(select_list));

			//

		}else if(is_open.equals("2")){// 故障
			params.addBodyParameter("xbottle", getGsonStringInfo());
			params.addBodyParameter("xbottle_data",
					getGsonStringInfo(select_list));
		}
		Log.e("lll", params.getStringParams().toString());
		httpUtil.PostHttp(params, 1);
	}

	/**
	 * 封装data串 钢瓶汇总
	 * 
	 * @return
	 */
	public String getGsonStringInfo() {
		JSONArray jsoa = new JSONArray();
		for (int i = 0; i < select_list.size(); i++) {
			JSONObject jsob = new JSONObject();
			try {
				jsob.put("type_id", select_list.get(i).getType());
				jsob.put("type_name", select_list.get(i).getType_name());
				jsob.put("type_num", 1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			jsoa.put(jsob);
		}
		return jsoa.toString();
	}

	/**
	 * 封装芯片号,码
	 * 
	 * @return
	 */
	public String getGsonStringInfo(List<Weight> select_list) {
		JSONArray jsoa = new JSONArray();
		for (int i = 0; i < select_list.size(); i++) {
			JSONObject jsob = new JSONObject();
			try {
				jsob.put("xinpian", select_list.get(i).getXinpian());
				jsob.put("number", select_list.get(i).getXuliehao());

			} catch (JSONException e) {
				e.printStackTrace();
			}
			jsoa.put(jsob);
		}
		return jsoa.toString();
	}
	public void getList(String bota){
//		Log.e("llll", bota);
		judge_list = new ArrayList<Weight>();
		try {
			JSONArray array = new JSONArray(bota);
			for (int i = 0; i < array.length(); i++) {
				JSONObject  object = array.getJSONObject(i);
				String number = object.getString("number");
				String xinpian = object.getString("xinpian");
				judge_list.add(new Weight(xinpian, number, "0"));
			}
			Log.e("llll", judge_list.size()+"");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void judge(){
		for (int i = 0; i < judge_list.size(); i++) {
			if(select_list != null &&select_list.size()>0){
				
			
			for (int j = 0; j < select_list.size(); j++) {
				if(judge_list.get(i).getXinpian().equals(select_list.get(j).getXinpian())){
					Log.e("llllbfdhdf", judge_list.get(i).getXinpian()+"uiuyuih"+select_list.get(j).getXinpian());
					tag = 1;
				}
			}
			}
		}
	}
}
