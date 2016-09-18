package com.ruiqi.works;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.google.gson.Gson;
import com.ruiqi.adapter.YouHuiAdapter;
import com.ruiqi.adapter.YouHuiAdapter.Send;
import com.ruiqi.bean.Type;
import com.ruiqi.bean.YouHuiInfo;
import com.ruiqi.bean.YouHuiZheKouInfo;
import com.ruiqi.bean.ZheKouInfo;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.HttpUtil.ParserData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectYouhuiActivity extends BaseActivity implements ParserData,Send{
	private ListView lv_Toast;
	private TextView tv_sure;
	private YouHuiAdapter adapter;
	private List<Type> youhui;
	String youhui_title,youhui_money;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_youhui_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("使用优惠券");
		tv_sure = (TextView) findViewById(R.id.tv_youhui_next);
		tv_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(youhui_money)){
					Toast.makeText(SelectYouhuiActivity.this, "请选择优惠券", 1).show();
				}else{
				Bundle bundle = new Bundle();
				bundle.putString("youhui_name", youhui_title);
				bundle.putDouble("youhui_money", Double.parseDouble(youhui_money));
				Intent intent = new Intent();
				intent.putExtra("youhui", bundle);
				SelectYouhuiActivity.this.setResult(1, intent);
				finish();}
			}
		});
		lv_Toast = (ListView) findViewById(R.id.listView_Tost_youhui);
		adapter = new YouHuiAdapter(SelectYouhuiActivity.this, youhHuiZheKou);
		adapter.Setsend(this);
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
		Gson gson=new Gson();
		Log.e("lll", result);
		try {
			JSONObject jsob=new JSONObject(result);
			if(jsob.getInt("resultCode")==1){
				youhHuiZheKou.clear();
				JSONArray jsoa=jsob.getJSONArray("resultInfo");
				for (int i = 0; i < jsoa.length(); i++) {
					JSONObject jsb=jsoa.getJSONObject(i);
					String str=jsoa.getJSONObject(i).toString();
					if(jsb.getString("type").equals("1")){//优惠券
						youhHuiZheKou.add(gson.fromJson(str, YouHuiInfo.class));
					}
//						
				}
				lv_Toast.setAdapter(adapter);
//				lv_Toast.setOnItemClickListener(new OnItemClickListener() {
//
//					@Override
//					public void onItemClick(AdapterView<?> parent, View view,
//							int position, long id) {
//						// TODO Auto-generated method stub
////						youhui = new  ArrayList<Type>();
////						Type  type = new Type();
////						type.setName(youhHuiZheKou.get(position).getTitle());
////						type.setPrice(youhHuiZheKou.get(position).getMoney()+"");
//						
//					}
//				});

			}else{
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		
	}
		
	}

	@Override
	public Bundle bunndle(Bundle bunndle) {
		// TODO Auto-generated method stub
		youhui_money = bunndle.getString("youhui_money");
		youhui_title = bunndle.getString("youhui_name");
		return null;
	}

}
