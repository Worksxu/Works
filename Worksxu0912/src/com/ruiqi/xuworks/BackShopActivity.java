package com.ruiqi.xuworks;

import java.io.Serializable;
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

import com.ruiqi.bean.TableInfo;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.view.CustomDownView;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.R;

public class BackShopActivity extends BaseActivity implements ParserData {

	private CustomDownView cdv_weight, cdv_null, cdv_old, cdv_peijian,cdv_bad,
			cdv_weightNotes, cdv_nullNotes, cdv_oldNotes, cdv_peijianNotes,cdv_badNotes;
	private List<TableInfo> list_null;
	private List<TableInfo> list_weight;
	private List<TableInfo> list_old;
	private List<TableInfo> list_peijian;
	private List<TableInfo> list_guzhang;
	public String judge_guzhang;
	public String  judge_weight;
	public String  judge_null;
	public String  judge_old;
	public String  judge_peijian;
	String type,status;
	Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.back_shop_layput);
		init();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getData();
	}
	private void init() {
		// TODO Auto-generated method stub
		setTitle("回库");
		cdv_weight = (CustomDownView) findViewById(R.id.CustomDownView_backshop_weight);
		cdv_weight.setString("重瓶回库申请");
		cdv_weight.setOnClickListener(this);
		cdv_bad = (CustomDownView) findViewById(R.id.CustomDownView_backshop_guzhang);
		cdv_bad.setString("故障瓶回库申请");
		cdv_bad.setOnClickListener(this);
		cdv_badNotes = (CustomDownView) findViewById(R.id.CustomDownView_backshop_guzhangNotes);
		cdv_badNotes.setString("故障瓶回库申请记录");
		cdv_badNotes.setOnClickListener(this);
		cdv_null = (CustomDownView) findViewById(R.id.CustomDownView_backshop_null);
		cdv_null.setString("空瓶回库申请");
		cdv_null.setOnClickListener(this);
		
		cdv_old = (CustomDownView) findViewById(R.id.CustomDownView_backshop_old);
		cdv_old.setString("折旧瓶回库申请");
		cdv_old.setOnClickListener(this);
		cdv_peijian = (CustomDownView) findViewById(R.id.CustomDownView_backshop_peijian);
		cdv_peijian.setString("配件回库申请");
		cdv_peijian.setOnClickListener(this);
		cdv_weightNotes = (CustomDownView) findViewById(R.id.CustomDownView_backshop_weightNotes);
		cdv_weightNotes.setString("重瓶回库申请记录");
		cdv_weightNotes.setOnClickListener(this);
		cdv_nullNotes = (CustomDownView) findViewById(R.id.CustomDownView_backshop_nullNotes);
		cdv_nullNotes.setString("空瓶回库申请记录");
		cdv_nullNotes.setOnClickListener(this);
		cdv_oldNotes = (CustomDownView) findViewById(R.id.CustomDownView_backshop_oldNotes);
		cdv_oldNotes.setString("折旧瓶瓶回库申请记录");
		cdv_oldNotes.setOnClickListener(this);
		cdv_peijianNotes = (CustomDownView) findViewById(R.id.CustomDownView_backshop_peijianNotes);
		cdv_peijianNotes.setString("配件回库申请记录");
		cdv_peijianNotes.setOnClickListener(this);
		getData();
	}

	private HttpUtil httpUtil;

	private void getData() {
		// TODO Auto-generated method stub
		httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(BackShopActivity.this,
				SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.BACKLIST);

		params.addBodyParameter("Token", token + "");
		params.addBodyParameter("shop_id",
				(String) SPUtils.get(this, SPutilsKey.SHOP_ID, "error"));
		params.addBodyParameter("shipper_id",
				(String) SPUtils.get(this, SPutilsKey.SHIP_ID, "error"));
		Log.e("llll", params.getStringParams().toString());
		httpUtil.PostHttp(params, 0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.CustomDownView_backshop_peijian:
			Intent peijian = new Intent(BackShopActivity.this,
					PeijianBackShopActvity.class);
			peijian.putExtra("judge", judge_peijian);
			startActivity(peijian);
			break;
		case R.id.CustomDownView_backshop_weightNotes:
			Intent weightnote = new Intent(BackShopActivity.this,
					BackShopNotesActivity.class);
			bundle = new Bundle();
			bundle.putSerializable("mdatas", (Serializable) list_weight);
			bundle.putString("type", "2");// 区分空重
			weightnote.putExtra("bundle", bundle);
			startActivity(weightnote);
			break;
		case R.id.CustomDownView_backshop_oldNotes:
			Intent old = new Intent(BackShopActivity.this,
					BackShopNotesActivity.class);
			bundle = new Bundle();
			bundle.putSerializable("mdatas", (Serializable) list_old);
			bundle.putString("type", "3");
			old.putExtra("bundle", bundle);
			startActivity(old);
			break;
		case R.id.CustomDownView_backshop_nullNotes:
			Intent null_bo = new Intent(BackShopActivity.this,
					BackShopNotesActivity.class);
			bundle = new Bundle();
			bundle.putSerializable("mdatas", (Serializable) list_null);
			bundle.putString("type", "1");
			null_bo.putExtra("bundle", bundle);
			startActivity(null_bo);
			break;
		case R.id.CustomDownView_backshop_weight:
			Intent weight = new Intent(BackShopActivity.this,BackShopApplyActivity.class);
			weight.putExtra("is_open", "1");
			weight.putExtra("judge", judge_weight);
			startActivity(weight);
			break;
		case R.id.CustomDownView_backshop_null:
			Intent null_apply = new Intent(BackShopActivity.this,BackShopApplyActivity.class);
			null_apply.putExtra("is_open", "0");
			null_apply.putExtra("judge", judge_null);
			startActivity(null_apply);
			break;
		case R.id.CustomDownView_backshop_guzhang:
			Intent bad_apply = new Intent(BackShopActivity.this,BackShopApplyActivity.class);
			bad_apply.putExtra("is_open", "2");
			bad_apply.putExtra("judge", judge_guzhang);
			startActivity(bad_apply);
			break;
		case R.id.CustomDownView_backshop_old:
			Intent old_apply = new Intent(BackShopActivity.this,BackShopOldApply.class);
			old_apply.putExtra("judge", judge_old);
			startActivity(old_apply);
			break;
		case R.id.CustomDownView_backshop_peijianNotes:
			Intent peijian_apply = new Intent(BackShopActivity.this,BackShopNotesActivity.class);
			bundle = new Bundle();
			bundle.putSerializable("mdatas", (Serializable) list_peijian);
			bundle.putString("type", "0");
			peijian_apply.putExtra("bundle", bundle);
			startActivity(peijian_apply);
			break;
		case R.id.CustomDownView_backshop_guzhangNotes:
			Intent guzhang_apply = new Intent(BackShopActivity.this,BackShopNotesActivity.class);
			bundle = new Bundle();
			bundle.putSerializable("mdatas", (Serializable) list_guzhang);
			bundle.putString("type", "4");
			guzhang_apply.putExtra("bundle", bundle);
			startActivity(guzhang_apply);
			break;

		default:
			break;
		}
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

	/**
	 * { "confirme_no": "tp16080851525554", "shop_id": "17", "shipper_id": "53",
	 * "shipper_name": "门店1送气工", "bottle": [ { "type_name": "5KG", "type_id":
	 * "2" } ], "bottle_data": [ { "type_num": "1", "number": "3CD4ECB9",
	 * "xinpian": "3CD4ECB9" } ], "time": "2016-08-08", "status": "0" },
	 */
	String title;
	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("llll", result);
		try {
			JSONObject object = new JSONObject(result);
			int code = object.getInt("resultCode");
			JSONArray array = object.getJSONArray("resultInfo");
			list_null = new ArrayList<TableInfo>();
			list_weight = new ArrayList<TableInfo>();
			list_old = new ArrayList<TableInfo>();
			list_peijian = new ArrayList<TableInfo>();
			list_guzhang = new ArrayList<TableInfo>();
			if(code == 1){
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				 type = obj.getString("ftype");// 1:空,2:重,3:折旧,0配件,4guzhang
				String ordeson = obj.getString("confirme_no");// 订单编号
				 status = obj.getString("status");// 订单状态
				if(status.equals("0")){
					 title = "未确认";
				}else{
					 title = "已确认";
				}
				String time = obj.getString("time");//订单时间
				String bottle = obj.getString("bottle");// 带有bottle的字符串
				String bottle_data = obj.getString("bottle_data");// 带有bottle_data的字符串
				if(type.equals("1")){
					list_null.add(new TableInfo(ordeson,title,time,bottle_data,type));
					if(status.equals("0")){
						SPUtils.put(BackShopActivity.this, "judge_null", bottle_data);
						judge_null = bottle_data;
					}
				}else if(type.equals("2")){
					if(status.equals("0")){
						SPUtils.put(BackShopActivity.this, "judge_weight", bottle_data);
						judge_weight = bottle_data;
					}
					list_weight.add(new TableInfo(ordeson,title,time,bottle_data,type));
				}else if(type.equals("3")){
					if(status.equals("0")){
						SPUtils.put(BackShopActivity.this, "judge_old", bottle);
						judge_old = bottle;
					}
					list_old.add(new TableInfo(ordeson,title,time,bottle,type));
				}else if(type.equals("0")){
					if(status.equals("0")){
						SPUtils.put(BackShopActivity.this, "judge_peijian", bottle);
						judge_peijian  = bottle;
					}
					list_peijian.add(new TableInfo(ordeson,title,time,bottle,type));
				}else {
					if(status.equals("0")){
						SPUtils.put(BackShopActivity.this, "judge_guzhang", bottle_data);
						judge_guzhang = bottle_data;
					}
					list_guzhang.add(new TableInfo(ordeson,title,time,bottle_data,type));
				}
			}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//	public void getList(String bota){
//		try {
//			JSONArray array = new JSONArray(bota);
//			for (int i = 0; i < array.length(); i++) {
//				JSONObject  object = array.getJSONObject(i);
//				String number = object.getString("number");
//				String xinpian = object.getString("xinpian");
////				if(type.equals("1")&&status.equals("0")){
////					judge_guzhang
////				}
//			}
//			
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
