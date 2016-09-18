package com.ruiqi.xuworks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Loader.ForceLoadContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.adapter.BottleToShopAdapter;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.bean.Weight;
import com.ruiqi.fragment.BottleToShopFragment;
import com.ruiqi.fragment.PingStockFragment;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.XuDialog;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.XuDialog.No;
import com.ruiqi.utils.XuDialog.Yes;
import com.ruiqi.view.CustomListView;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.HomePageActivity;
import com.ruiqi.works.R;

/**
 * xtl 配件回库界面
 */
public class PeijianBackShopActvity extends BaseActivity implements ParserData {
	private ListView lv_peijian;
	private TextView tv_sure;
	private List<TableInfo> mDatas;// 
	private BottleToShopAdapter adapter;
	private List<Weight> judge_list;// 判断的list
	int total_num;
	int tag,flag;
	private ProgressDialog pd;
	LinearLayout ll_toast,ll_content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backshop_layout);
		getList((String) SPUtils.get(PeijianBackShopActvity.this, "judge_peijian", ""));
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("配件回库");
		if(TextUtils.isEmpty(getIntent().getStringExtra("judge"))){
			judge_list = new ArrayList<Weight>();
		}else{
			getList(getIntent().getStringExtra("judge"));
		}
		pd = new ProgressDialog(PeijianBackShopActvity.this);
		pd.setMessage("正在加载");
		ll_content = (LinearLayout) findViewById(R.id.ll_backShop);
		ll_toast = (LinearLayout) findViewById(R.id.ll_backshop_toast);
		lv_peijian = (CustomListView) findViewById(R.id.listView_backpeijian);
		tv_sure = (TextView) findViewById(R.id.tv_peijianshop_sure);
		tv_sure.setOnClickListener(this);
		initData();
		tv_sure.setOnClickListener(new OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getNum();
				if(total_num>0){
//					if(tag==1&&flag!=1){
						XuDialog.getInstance().show(PeijianBackShopActvity.this, "是否提交", 1);// 待改
//					}else{
//						
//						Toast.makeText(PeijianBackShopActvity.this, "提交数量大于实际数量", 1).show();
//					}
					
				}else{
					Toast.makeText(PeijianBackShopActvity.this, "请输入数量", 1).show();
				}
				
				XuDialog.getInstance().setno(new No() {
					
					@Override
					public void XuNo(int i) {
						// TODO Auto-generated method stub
						
					}
				});
				XuDialog.getInstance().setyes(new Yes() {
					
					@Override
					public void XuCallback(int i) {
						// TODO Auto-generated method stub
						if(judge_list.size()>0){
							Toast.makeText(PeijianBackShopActvity.this, "您还有未确认的回库记录,请联系门店确认", 1).show();
						}else{
							commintData();
//					
//							
						}
						
					}

					
				});
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

	public void initData() {
		pd.show();
		mDatas = new ArrayList<TableInfo>();
		httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(PeijianBackShopActvity.this, SPutilsKey.TOKEN, 0);
		String shipid = (String) SPUtils.get(PeijianBackShopActvity.this,
				SPutilsKey.SHIP_ID, "error");
		RequestParams params = new RequestParams(RequestUrl.PJ_STOCK);
		params.addBodyParameter("shipper_id", shipid);
		params.addBodyParameter("Token", token + "");
		httpUtil.PostHttp(params, 0);
	}

	/**
	 * "resultCode": 1, "resultInfo": [ { "id": "", "shipper_id": "18",
	 * "goods_id": "23", "goods_kind": "35", "goods_num": "-33", "goods_type":
	 * "23", "name": "双眼灶具-台", "type": "2" },
	 * 
	 * @param result
	 * @param what
	 */
	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("lll", result);
		if(what == 0){
		try {
			JSONObject object = new JSONObject(result);
			int code = object.getInt("resultCode");
			if(code == 1){
			pd.dismiss();
			JSONArray array = object.getJSONArray("resultInfo");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				String name  = obj.getString("name");
				String num = obj.getString("goods_num");
				String type = obj.getString("goods_kind");// 配件id
				String id = obj.getString("goods_type");//xinjia
//				String kid = "0";
//				if(array.length()>0){
//				total_num += Integer.parseInt(num);
//				
//				}
				Log.e("llll", total_num+"");
				mDatas.add(new TableInfo(name,num,type,id,"0"));
			}
			if(mDatas != null && mDatas.size()>0){
				ll_content.setVisibility(View.VISIBLE);
				adapter = new BottleToShopAdapter(PeijianBackShopActvity.this, mDatas);
				lv_peijian.setAdapter(adapter);
			}else{
				
				ll_toast.setVisibility(View.VISIBLE);
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
			if(code == 1){
				Log.e("llll_sfafas", info);
				Toast.makeText(PeijianBackShopActvity.this, info, 1).show();
				Intent home = new Intent(PeijianBackShopActvity.this,HomePageActivity.class);
				startActivity(home);
			}else{
				Toast.makeText(PeijianBackShopActvity.this, info, 1).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}
	private void commintData() {
		// TODO Auto-generated method stub
		httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(PeijianBackShopActvity.this,
				SPutilsKey.TOKEN, 0);
		String shop_id = (String) SPUtils
				.get(PeijianBackShopActvity.this, SPutilsKey.SHOP_ID, "error");
		String shiper_id = (String) SPUtils.get(PeijianBackShopActvity.this, SPutilsKey.SHIP_ID,
				"error");
		RequestParams params = new RequestParams(RequestUrl.BACKSHOP);
		
		

		// params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("shipper_name", (String) SPUtils.get(PeijianBackShopActvity.this, "shipper_name", ""));
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("shipper_id", shiper_id);
		params.addBodyParameter("Token", token + "");
		
		params.addBodyParameter("pbottle", getGsonStringInfo());
			Log.e("lll", params.getStringParams().toString());
		httpUtil.PostHttp(params, 1);
	}
	public String getGsonStringInfo() {
		JSONArray jsoa = new JSONArray();
		for (int i = 0; i < mDatas.size(); i++) {
			if(!TextUtils.isEmpty(mDatas.get(i).getKid())){
			
			
			JSONObject jsob = new JSONObject();
			try {
				jsob.put("type_id", mDatas.get(i).getOrderStatus());// 物品id
				jsob.put("type_name", mDatas.get(i).getOrderNum());// 规格名
				jsob.put("type_num", mDatas.get(i).getKid());// 数量
				jsob.put("type_kind", mDatas.get(i).getOrderTime());// type
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
			
			try {
				if(!jsob.get("type_num").equals("0")){
					Log.e("sadsadsad", "jin这个方法");
					jsoa.put(jsob);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			}
		
		return jsoa.toString();
	}
	int getNum(){
		
		for (int i = 0; i < mDatas.size(); i++) {
			
			total_num += Integer.parseInt(mDatas.get(i).getKid());
			Log.e("llll_allkid", mDatas.get(i).getKid());
//			if(Integer.parseInt(mDatas.get(i).getKid())<Integer.parseInt(mDatas.get(i).getOrderMoney())){
//				tag = 1;// 判断输入的数量与实际的数量是否匹配
//			}else{
//				flag = 1;
//			}
		}
	
		return total_num;
		
	}
	public void getList(String bota){
		Log.e("llll", bota);
		judge_list = new ArrayList<Weight>();
		try {
			JSONArray array = new JSONArray(bota);
			for (int i = 0; i < array.length(); i++) {
				JSONObject  object = array.getJSONObject(i);
//				String number = object.getString("number");
//				String xinpian = object.getString("xinpian");
				judge_list.add(new Weight("0", "0", "0"));
			}
			Log.e("llll", judge_list.size()+"");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
