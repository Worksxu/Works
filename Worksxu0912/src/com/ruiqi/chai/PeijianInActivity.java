package com.ruiqi.chai;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruiqi.bean.TableInfo;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.view.AutoListView;
import com.ruiqi.view.AutoListView.OnLoadListener;
import com.ruiqi.view.AutoListView.OnRefreshListener;
import com.ruiqi.works.R;

public class PeijianInActivity extends Activity implements OnClickListener,OnRefreshListener,OnLoadListener,ParserData{

	
	private TextView tv_title,tv_back;
	private ImageView ivBack;
	
	private AutoListView alv;
	private  ArrayList<TableInfo> arrayList=new ArrayList<TableInfo>();
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			List<TableInfo> result = (List<TableInfo>) msg.obj;
			switch (msg.what) {
			case AutoListView.REFRESH:
				alv.onRefreshComplete();
				arrayList.clear();
				TableInfo tableInfo=new TableInfo("订单号","配件名称","数量","日期");
				arrayList.add(tableInfo);
				arrayList.addAll(result);
				break;
			case AutoListView.LOAD:
				alv.onLoadComplete();
				arrayList.addAll(result);
				break;
			}
			alv.setResultSize(result.size());
			adapter.notifyDataSetChanged();
		};
	};
	
	
	private ListViewAdapterText4 adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.chai_ping_in_activity);
		ivBack=(ImageView) findViewById(R.id.ivBack);
		tv_back=(TextView) findViewById(R.id.tv_back);
		tv_title=(TextView) findViewById(R.id.tvTitle);
		tv_title.setText("配件入库记录");
		
		alv=(AutoListView) findViewById(R.id.alv);
		adapter=new ListViewAdapterText4(this, arrayList);
		alv.setAdapter(adapter);
		
		alv.setOnRefreshListener(this);
		alv.setOnLoadListener(this);
		
		ivBack.setOnClickListener(this);
		tv_back.setOnClickListener(this);
		initData();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.ivBack:
		case R.id.tv_back:
			finish();
			break;
		default:
			break;
		}
		
	}
	
	private HttpUtil httpUtil;
	private int page=1;
	private void initData(){
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		loadData(AutoListView.REFRESH);
	}

	private void loadData(int what){
		String shop_id = (String) SPUtils.get(PeijianInActivity.this, SPutilsKey.SHOP_ID, "error");
		String ship_id = (String) SPUtils.get(PeijianInActivity.this, SPutilsKey.SHIP_ID, "error");
		RequestParams params = new RequestParams(RequestUrl.IN_PING);
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("shipper_id", ship_id);
		params.addBodyParameter("type", "2");
		params.addBodyParameter("page", page+"");
		httpUtil.PostHttp(params, what);
	}
	@Override
	public void onLoad() {
		page++;
		loadData(AutoListView.LOAD);
	}
	@Override
	public void onRefresh() {
		page=1;
		loadData(AutoListView.REFRESH);
	}
	
	private ArrayList<TableInfo> aList=new ArrayList<TableInfo>();
	@Override
	public void sendResult(String result, int what) {
		System.out.println("配件入库result="+result);
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			aList.clear();
			if(resultCode==1){
				JSONObject jsob = obj.getJSONObject("resultInfo");
				JSONArray array=jsob.getJSONArray("product");
				aList = new ArrayList<TableInfo>();
				for(int j=0;j<array.length();j++){
					JSONObject object = array.getJSONObject(j);
					String documentsn = object.getString("documentsn");
					String time = object.getString("time");
					String goods_num = object.getString("goods_num");
					String goods_name = object.getString("goods_name");
					aList.add(new TableInfo(documentsn,goods_name,goods_num,time));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Message msg=Message.obtain();
		msg.what=what;
		msg.obj=aList;
		handler.sendMessage(msg);
	}

}
