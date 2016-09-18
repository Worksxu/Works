package com.ruiqi.works;


import java.util.ArrayList;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruiqi.adapter.ZheKouAdapter;
import com.ruiqi.adapter.ZheKouAdapter.Send;
import com.ruiqi.adapter.ZheKouAdapter.ViewHolder;
import com.ruiqi.bean.OldUserInfo;
import com.ruiqi.bean.Type;
import com.ruiqi.bean.YouHuiInfo;
import com.ruiqi.bean.YouHuiZheKouInfo;
import com.ruiqi.bean.ZheKouContent;
import com.ruiqi.bean.ZheKouInfo;
import com.ruiqi.bean.ZheKouInfo.Data;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.works.R;

public class AddZhekou extends BaseActivity implements ParserData,Send{
	private ListView lv_zhekou;
	private TextView tv_sure;
	private ZheKouAdapter adapter;
	private int a = 0;// 用来判定是否是第一次点击，0代表是，1代表不是
	private int cliPos;// 记录第一次点击时的位置
	private Boolean flag = true;
	private String title, money_zhekou,id;
	private ArrayList<Data> data;
	private ArrayList<Type> list;
	Bundle zhekou;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_zhekou_layout);
		init();
		
	}
	
	private void init() {
		// TODO Auto-generated method stub
		setTitle("折扣商品");
		lv_zhekou = (ListView) findViewById(R.id.listView_zhekou);
		
		adapter = new ZheKouAdapter(AddZhekou.this, youhHuiZheKou);
		tv_sure = (TextView) findViewById(R.id.tv_zhekou_sure);
		tv_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				zhekou = new Bundle();
				zhekou.putSerializable("zhekou_list", list);
//				intent.putExtra("zhekou", zhekou);
				intent.putExtra("zhekou", zhekou);
				setResult(2, intent);
				finish();
			}
		});
		adapter.Setsend(this);// 调用接口回调
		initData();
	}
	private HttpUtil httpUtil;
	private void initData() {
		// TODO Auto-generated method stub
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		RequestParams params=new RequestParams(RequestUrl.NewYOUHUI);
		params.addBodyParameter(SPutilsKey.TOKEN,(Integer)SPUtils.get(this,  SPutilsKey.TOKEN, 0)+"");
		Log.e("lll", "请求优惠"+params.getStringParams().toString());
		httpUtil.PostHttp(params, 0);
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
	private ArrayList<YouHuiZheKouInfo> youhHuiZheKou=new ArrayList<YouHuiZheKouInfo>();
	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("lll", result);
		Gson gson=new Gson();
		
		try {
			JSONObject jsob=new JSONObject(result);
			if(jsob.getInt("resultCode")==1){
				youhHuiZheKou.clear();
				JSONArray jsoa=jsob.getJSONArray("resultInfo");
				for (int i = 0; i < jsoa.length(); i++) {
					JSONObject jsb=jsoa.getJSONObject(i);
					String str=jsoa.getJSONObject(i).toString();
//					if(jsb.getString("type").equals("1")){//优惠券
//						youhHuiZheKou.add(gson.fromJson(str, YouHuiInfo.class));
//					}else 
					
						if(jsb.getString("type").equals("2")){//折扣商品
							ZheKouInfo zheKouInfo =	gson.fromJson(str, ZheKouInfo.class);
							zheKouInfo.setImage(R.drawable.unchecked);
						youhHuiZheKou.add(zheKouInfo);
						
					}
				}
				lv_zhekou.setAdapter(adapter);

			}else{
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		
	}

}

	@SuppressWarnings("unchecked")
	@Override
	public Bundle bunndle(Bundle bunndle) {
		// TODO Auto-generated method stub
		list = new ArrayList<Type>();
		title = bunndle.getString("zhekou_name");
//		Log.e("lll", title);
		zhekou = bunndle;
		data =  (ArrayList<Data>) zhekou.getSerializable("zhekou");
//		Log.e("lll", data.toString());
//		title = bundle.getString("zhekou_name");
//		id_zhekou = bundle.getString("zhekou_id");
		money_zhekou = zhekou.getString("zhekou_money");
		Type type = new Type();
		type.setName(data.get(0).getGood_name());
		type.setId(data.get(0).getGood_id());
		type.setNum(money_zhekou);// 折扣商品对应的钱数
		type.setYj_price(data.get(0).getGood_price());
		type.setType(data.get(0).getGood_type());
		type.setWeight(data.get(0).getGood_kind());// 把折扣的data串放到list里
		type.setPrice(money_zhekou);
		
		list.add(type);
		return null;
	}
	}
