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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.Table.OrderTable;
import com.ruiqi.Table.TabAdapter;
import com.ruiqi.Table.TabRow;
import com.ruiqi.bean.OutToShop;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.MyMoneyActivity;
import com.ruiqi.works.OutMoneyActivity;
import com.ruiqi.works.R;


public class TurnOverActivity extends BaseActivity implements ParserData {
	
	private List<OutToShop> list;
	private ListView lv_content;
	private TextView tv_commint;
	public List<TabRow> table;
	
	public List<TableInfo> mDatas;
	
	public TabAdapter adapter;
	String yutotal ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.turnoverlayout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("上缴支出");
		getData();
		lv_content = (ListView) findViewById(R.id.lv_content);
		tv_commint = (TextView) findViewById(R.id.tv_out);
		tv_commint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TurnOverActivity.this, OutMoneyActivity.class);
				intent.putExtra("mData", yutotal);
				startActivity(intent);
			}
		});
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
	private HttpUtil httpUtil;
	private void getData() {
		// TODO Auto-generated method stub
		mDatas = new ArrayList<TableInfo>();
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		String shipper_id = (String) SPUtils.get(TurnOverActivity.this, SPutilsKey.SHIP_ID, "error");
		int token = (Integer) SPUtils.get(TurnOverActivity.this, SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.OUT);
		params.addBodyParameter("shipper_id", shipper_id);
		params.addBodyParameter("goodtime", CurrencyUtils.getNowTime());
		
//		params.addBodyParameter("Token",  token+"");
		httpUtil.PostHttp(params, 0);
	}

	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		list = new ArrayList<OutToShop>();
		System.out.println("result=上缴支出"+result);
		//解析
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				System.out.println("mDatas="+mDatas);
//				
				JSONObject object = obj.getJSONObject("resultInfo");
				yutotal = object.getString("ytotal");
				Log.e("dsgldsg_dfdsf", "jinlai");
				 Log.e("sadsadasdsad_余额", yutotal);
				 
				JSONArray array = object.getJSONArray("datalist");
				
			 
			
				Log.e("上缴支出数量", array.length()+"");
				if(array.length()>0){
					
				
				for (int i = 0; i < array.length(); i++) {
					
					JSONObject obj1 = array.getJSONObject(i);
					String money = obj1.getString("money");
					String shop_id = obj1.getString("shop_id");
					String shop_name = obj1.getString("shop_name");
					String time = obj1.getString("time");
					mDatas.add(new TableInfo(money, shop_name, time));
				}
				setAdapterToListView(mDatas, initTitles());
				}else{
					Toast.makeText(TurnOverActivity.this, "暂时没有支出记录", 1).show();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	//上缴给门店的表头
		private String[] initTitles() {
			String [] titles = {"金额","收款人","日期"};
			return titles;
		}
		private void setAdapterToListView(List<TableInfo> mData,String [] titles){
			table = new OrderTable().addData( mData, titles);
			
			adapter = new TabAdapter(TurnOverActivity.this, table);
			
			lv_content.setAdapter(adapter);
			
		}

}
