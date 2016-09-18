package com.ruiqi.works;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.adapter.TypePopupAdapter;
import com.ruiqi.bean.PeiJian;
import com.ruiqi.bean.PeiJianTypeMoney;
import com.ruiqi.bean.Type;
import com.ruiqi.db.Pj;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.works.ApplyActivity.MyAdapter;
import com.ruiqi.works.ApplyActivity.ItemAdapter.ViewHolder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class AddPeijianActivity extends BaseActivity{
	private ListView lv_peijian;
	private TextView tv_sure;
	private TypePopupAdapter adapter;
	private List<Type> mTypeList = new ArrayList<Type>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_peijian_layout);
		init();
	}
	

	private void init() {
		// TODO Auto-generated method stub
		setTitle("添加配件");
		adapter = new TypePopupAdapter(mTypeList, AddPeijianActivity.this);
		lv_peijian = (ListView) findViewById(R.id.listView_Peijian);
		tv_sure = (TextView) findViewById(R.id.tv_peijian_sure);
		tv_sure.setOnClickListener(this);
		initData();
	}


	private void initData() {
		// TODO Auto-generated method stub
		String shipper_id = (String) SPUtils.get(AddPeijianActivity.this, SPutilsKey.SHIP_ID, "error");
		String shop_id = (String) SPUtils.get(AddPeijianActivity.this, SPutilsKey.SHOP_ID, "error");
		RequestParams params = new RequestParams(RequestUrl.PJ);
		params.addBodyParameter("shipper_id", shipper_id);
		params.addBodyParameter("shop_id", shop_id);
		HttpUtil.PostHttp(params, handler, new ProgressDialog(AddPeijianActivity.this));
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}
	};
	private void paraseData(String result) {
		System.out.println("result="+result);
		Log.e("lll", result);
		try {
			JSONObject obj1 = new JSONObject(result);
			int resultCode = obj1.getInt("resultCode");
			if(resultCode==1){
				
				JSONArray array = obj1.getJSONArray("resultInfo");
				for(int i=0;i<array.length();i++){
					JSONObject obj2 = array.getJSONObject(i);
					
					String id = obj2.getString("id");
					String name = obj2.getString("name");
					String type = obj2.getString("type");
					String norm_id = obj2.getString("norm_id");
					String typename = obj2.getString("typename");
					String price = obj2.getString("price");
					
					mTypeList.add(new Type(price,  typename, "0", id, norm_id, type,"0", name, "0"));
//				
				}

				lv_peijian.setAdapter(adapter);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_peijian_sure:
			typeSure();
			Log.e("lll", "点击");
			break;

		default:
			break;
		}
	}

	@Override
	public void jumpPage() {
		// TODO Auto-generated method stub
		super.jumpPage();
		finish();
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
	public static List<Type> list;
	private double money = 0;
	private void typeSure() {
		
		list = new ArrayList<Type>();
		for (int i = 0; i < mTypeList.size(); i++) {
			int num = Integer.parseInt(mTypeList.get(i).getNum());
			if (num > 0) {
//				list.add(new Type(Double.parseDouble(mTypeList.get(i).getPrice()) * num + "", mTypeList.get(i).getWeight(), mTypeList.get(i).getNum(), mTypeList.get(i).getId(), mTypeList.get(i).getNorm_id(),mTypeList.get(i).getType(), mTypeList.get(i).getBottle_name(), mTypeList.get(i).getName(), mTypeList.get(i).getYj_price()));
			Type type = new Type();
			type.setPrice(mTypeList.get(i).getPrice());// 商品单价
			type.setWeight(mTypeList.get(i).getWeight());// 商品规格
			type.setName(mTypeList.get(i).getName());// 商品名
			type.setNum(mTypeList.get(i).getNum());// 商品数量
			type.setId(mTypeList.get(i).getId());// 商品Id;
			type.setType(mTypeList.get(i).getType());// 商品type
			type.setNorm_id(mTypeList.get(i).getNorm_id());// 商品kind;
			type.setYj_price(Double.parseDouble(mTypeList.get(i).getPrice()) * num+"");// 单类商品总价
			list.add(type);
			}
			
		}
		if (list.size() > 0) {
			money = 0;
			for (int i = 0; i < list.size(); i++) {
				money += Double.parseDouble(list.get(i).getPrice());
			}
		
		}
		Bundle bundle = new Bundle();
		bundle.putSerializable("mDatas", (Serializable) list);
		bundle.putString("money", money+"");
		Intent intent = new Intent();
		intent.putExtra("peijian", bundle);
		AddPeijianActivity.this.setResult(1, intent);
		finish();

}
		public String getPeijianJSON(){
			JSONArray array = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				JSONObject object = new JSONObject();
				try {
					object.put("good_id", list.get(i).getId());
					object.put("good_num", list.get(i).getNum());
					object.put("good_name", list.get(i).getName());
					object.put("good_price", list.get(i).getPrice());
					object.put("good_type", list.get(i).getType());
					object.put("good_kind", list.get(i).getNorm_id());
					object.put("iszhekou", "0");
					// obj1.put("wb", weightBottle);
					array.put(object);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return array.toString();
		}
	}
