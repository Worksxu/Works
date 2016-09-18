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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.adapter.BottleInfoAdapter;
import com.ruiqi.adapter.CheckBottleAdapter;
import com.ruiqi.adapter.ReceideailAdapter;
import com.ruiqi.adapter.CheckBottleAdapter.ViewHolder;
import com.ruiqi.bean.NopayDetail;
import com.ruiqi.bean.Order;
import com.ruiqi.bean.Weight;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.XuDialog;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.XuDialog.No;
import com.ruiqi.utils.XuDialog.Yes;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.HomePageActivity;
import com.ruiqi.works.R;

public class CheckBottleInfoActivity extends BaseActivity implements ParserData {
	private ListView lv_check;
	private TextView tv_next;
	private List<Weight> list;// 全部的list
	private List<Weight> select_list;// 选中的list
	private BottleInfoAdapter adapter;
	String typeid, typename,num_bottle;// 判定空重瓶,规格及数量
	String is_open;
	 
	int num = 0;
	HashMap<Weight, Integer> info = new HashMap<Weight, Integer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_bottle_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("钢瓶详情");
		
		typeid = getIntent().getExtras().getString("typeid");
		Log.e("lll", typeid);
		typename = getIntent().getExtras().getString("typename");
		num_bottle = getIntent().getExtras().getString("num");
		if (typeid.equals("1")) {
			is_open = "0";
		} else if(typeid.equals("4")){// 待修
			is_open = "2";
		}
		else {
			is_open = "1";
		}
		Log.e("lll", is_open);
		lv_check = (ListView) findViewById(R.id.listView_checkbottle);
	
		getData();
	}

	private HttpUtil httpUtil;

	private void getData() {
		// TODO Auto-generated method stub
		httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(CheckBottleInfoActivity.this,
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

	/**
	 * { "resultCode": 1, "resultInfo": [ { "number": "3CD4ECB9", "xinpian":
	 * "3CD4ECB9", "type": "2", "is_open": "1", "type_name": "5KG" }, {
	 * "number": "5EB2D631", "xinpian": "5EB2D631", "type": "7", "is_open": "1",
	 * "type_name": "50KG" } ]
	 */
	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		if(what == 0){
			
		
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
					if (obj.getString("is_open").equals(is_open)
							&& obj.getString("type_name").equals(typename)) {

						wt.setXinpian(obj.getString("xinpian"));
						wt.setType(obj.getString("type"));
						wt.setType_name(obj.getString("type_name"));
						wt.setStatus(obj.getString("is_open"));// 瓶的状态
						wt.setXuliehao(obj.getString("number"));// 钢瓶码
						list.add(wt);
					}
				}
				Log.e("llll", list.size()+"");
				adapter = new BottleInfoAdapter(CheckBottleInfoActivity.this,
						list);
				lv_check.setAdapter(adapter);
				

			} else {
				Toast.makeText(CheckBottleInfoActivity.this, info, 1).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
	}

	
	
}
