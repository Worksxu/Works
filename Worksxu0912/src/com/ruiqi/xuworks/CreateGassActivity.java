package com.ruiqi.xuworks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import cn.jpush.a.a.ad;

import com.ruiqi.adapter.DeletItemAdapter;
import com.ruiqi.adapter.TypePopupAdapter;
import com.ruiqi.bean.Type;
import com.ruiqi.bean.ZheKouContent;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;
import com.ruiqi.view.CustomDownView;
import com.ruiqi.view.CustomListView;
import com.ruiqi.view.SwipeMenu;
import com.ruiqi.view.SwipeMenuCreator;
import com.ruiqi.view.SwipeMenuItem;
import com.ruiqi.view.SwipeMenuListView;
import com.ruiqi.view.SwipeMenuListView.OnMenuItemClickListener;
import com.ruiqi.works.AddPeijianActivity;
import com.ruiqi.works.AddZhekou;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.CommintOrder;
import com.ruiqi.works.R;
import com.ruiqi.works.R.drawable;
import com.ruiqi.works.R.id;
import com.ruiqi.works.R.layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateGassActivity extends BaseActivity{
//	private ListView lv_gass;
	private ScrollView sv_gass;
	private SwipeMenuListView lv_peijian,lv_zhekou;
	private CustomListView lv_gass;
	private CustomDownView cdv_peijian,cdv_zhekou;
	private TextView tv_next,tv_delete;
	private LinearLayout ll_delete;
	private ImageView img_delete;
	private ProgressDialog pd;
	private ArrayList<Integer> idList = new ArrayList<Integer>();
	public static List<Type> gass = new ArrayList<Type>();
	private List<Type> mTypeList = new ArrayList<Type>();
	private List<Type> mList = new ArrayList<Type>();
	private List<Type> mList_youhui = new ArrayList<Type>();// 客户优惠
	private TypePopupAdapter adapter;
	private DeletItemAdapter delete;
	private double deduction_money = 0;// 抵扣金额
	String title, id_zhekou,money_zhekou,money_peijian;
	Bundle bundle;
	int select_gass_num = 0;
	int deducyion_num = 0;
	ZheKouContent data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_gass_layout);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		setTitle("创建订气订单");
		if(SPUtils.get(CreateGassActivity.this, "old_new", "").equals("1")){// 新用户
			SPUtils.remove(CreateGassActivity.this, "deduction");
		}
		getyh();// 获取优惠的list
		pd = new ProgressDialog(this);
		pd.setTitle("正在加载");
		sv_gass = (ScrollView) findViewById(R.id.scrollView_crateGass);
		sv_gass.smoothScrollBy(0, 0);
		adapter = new TypePopupAdapter(mTypeList, CreateGassActivity.this);
		cdv_peijian = (CustomDownView) findViewById(R.id.down_addPeijian);
		cdv_peijian.setString("添加配件");
		cdv_peijian.setView(View.VISIBLE);
		cdv_zhekou = (CustomDownView) findViewById(R.id.down_addzhekou);
		cdv_zhekou.setString("用户优惠和余瓶券");
		cdv_zhekou.setView(View.VISIBLE);
		cdv_zhekou.settvView(View.VISIBLE);
		cdv_zhekou.settvString("共计"+mList.size()+""+"张余气券");
		lv_gass = (CustomListView) findViewById(R.id.listView_gassTost);
		
		lv_zhekou = (SwipeMenuListView) findViewById(R.id.listView_addzhekou);
		lv_peijian = (SwipeMenuListView) findViewById(R.id.listView_addpeijian);
		tv_next = (TextView) findViewById(R.id.tv_gass_next);
		tv_delete = (TextView) findViewById(R.id.textView_delete_zhekou);
		ll_delete = (LinearLayout) findViewById(R.id.ll_delete_zhekou);
		img_delete  = (ImageView) findViewById(R.id.imageView_delete_zhekou);
		tv_next.setOnClickListener(this);
		cdv_peijian.setOnClickListener(this);
		cdv_zhekou.setOnClickListener(this);

		getGass();
	}
	private void getGass() {
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams(RequestUrl.BOTTLE_TYPE);
		params.addBodyParameter(SPutilsKey.SHOP_ID, (String) SPUtils.get(CreateGassActivity.this,SPutilsKey.SHOP_ID, ""));
		params.addBodyParameter("user_type", (String)SPUtils.get(CreateGassActivity.this, "user_type", ""));
		Log.e("lll_获取订气列表", params.getStringParams().toString());
		HttpUtil.PostHttp(params, handler, pd);
	}
	// 获得气罐数据
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			if (result != null) {
				parseData(result);
			}
		}

		private void parseData(String result) {
			// TODO Auto-generated method stub
			Log.e("lll", result);
			try {
				JSONObject obj = new JSONObject(result);
				int resultCode = obj.getInt("resultCode");
				if (resultCode == 1) {
					
						JSONArray array = obj.getJSONArray("resultInfo");
						for (int j = 0; j < array.length(); j++) {
							JSONObject object = array.getJSONObject(j);
							String bottle_name = object.getString("typename");
							String price = object.getString("price");
							String id = object.getString("id");
							String norm_id = object.getString("norm_id");
							String type = object.getString("type");
							String name = object.getString("name");
							String yj_price = object.getString("yj_price");
							mTypeList.add(new Type(price,  bottle_name, "0", id, norm_id, type,bottle_name, name, yj_price));
							}
//						CurrencyUtils.setListViewHeightBasedOnChildren(lv_gass);
						lv_gass.setAdapter(adapter);
						
					
				}
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
		}
	};
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		selectgass();
		switch (v.getId()) {
		case R.id.tv_gass_next:
			Log.e("llll_商品", select_gass_num+"");
			Log.e("llll_优惠", deducyion_num+"");
			if((select_gass_num-deducyion_num)<0){
				Toast.makeText(CreateGassActivity.this, "商品有变动,请重新选择余瓶券", 1).show();
				
			}else{
				nextSteap();
				
			}
			break;
		case R.id.down_addPeijian:
			Intent intent = new Intent(CreateGassActivity.this,AddPeijianActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.down_addzhekou:
//			if(gass.size()>0){
				getSelect();
//			}else{
//				Toast.makeText(CreateGassActivity.this, "请选择液化气", 1).show();
//			}
			
			
			break;
		case R.id.rl_back:
			finish();
			break;
		default:
			break;
		}
	}
	
	private double gass_money = 0;
	private void nextSteap() {
		// TODO Auto-generated method stub
		

		
		Bundle bundle = new Bundle();
		bundle.putSerializable("mDatas", (Serializable) gass);
		bundle.putSerializable("mdeduction", (Serializable) idList);
		bundle.putDouble("deduction_money", deduction_money);
		if(gass.size()>0){
			Intent intent = new Intent(CreateGassActivity.this,CommintOrder.class);
			intent.putExtra("gass", bundle);
			startActivity(intent);
		}else{
			T.show(CreateGassActivity.this, "请选择商品", 1);
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
	
	private List<Type> peijian = new ArrayList<Type>();
	private List<Type> deduction = new ArrayList<Type>();
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		switch (arg1) {

		case 2:
			Log.e("lll", "2");
			deduction = new ArrayList<Type>();
			bundle = new Bundle();
			bundle = arg2.getBundleExtra("deduction");
			deduction = (List<Type>) bundle.getSerializable("deduction");
			idList = (ArrayList<Integer>) bundle.getSerializable("idList");
			deduction_money = bundle.getDouble("deduction_money");
//			data = (ZheKouContent) bundle.getSerializable("zhekou");
//			Log.e("lll", data.getGood_name());
//			title = bundle.getString("zhekou_name");
//			id_zhekou = bundle.getString("zhekou_id");
//			money_zhekou = bundle.getString("zhekou_money");
//			Type type = new Type();
//			type.setName(data.getGood_name());
//			type.setId(data.getGood_id());
//			type.setNum(data.getGood_num());
//			type.setYj_price(data.getGood_price());
//			type.setType(data.getGood_type());
//			type.setWeight(data.getGood_kind());// 把折扣的data串放到list里
//			type.setPrice(money_zhekou);
//			
//			zhekou.add(type);
			deducyion_num = 0;
			for (int i = 0; i < deduction.size(); i++) {
				deducyion_num += Integer.parseInt(deduction.get(i).getNum());
			}
			cdv_zhekou.settvString("已选"+deducyion_num+""+"张余气券"+"共计:"+deduction_money+""+"元");
			delete = new DeletItemAdapter(CreateGassActivity.this,(ArrayList<Type>) deduction);
			
			lv_zhekou.setAdapter(delete);
			lv_zhekou.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				
				@Override
				public void onMenuItemClick(int position, SwipeMenu menu, int index) {
					// TODO Auto-generated method stub
					switch (index) {
					case 0:
						deduction.remove(position);
						delete.notifyDataSetChanged();
						if(deduction.size() == 0){
							lv_zhekou.setVisibility(View.GONE);
						}
						break;

					default:
						break;
					}
				}
			});
			break;

		default:
			break;
		}
		
	}
	
	// 滑动删除用的类
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	/**
	 * 获取用户的余气券
	 */
	public void getyh(){
		try {
			JSONArray array = new JSONArray((String)SPUtils.get(CreateGassActivity.this, "deduction", ""));
			for (int i = 0; i < array.length(); i++) {
				
				JSONObject object = array.getJSONObject(i);
				Type type = new Type();
//				type.setName(object.getString("type"));//1客户优惠,2余瓶券
				if(object.getString("type").equals("1")){
					type.setPrice(object.getString("money"));
					type.setWeight(object.getString("title"));
					type.setType(object.getString("good_type"));
					type.setName(object.getString("type"));//1客户优惠,2余瓶券
					type.setId(object.getString("id"));
					mList_youhui.add(type);
				}
				type.setPrice(object.getString("money"));
				type.setWeight(object.getString("title"));
				type.setType(object.getString("good_type"));
//				
				type.setId(object.getString("id"));
				
				type.setNum("1");
				mList.add(type);
				
			}
			
			Log.e("lll", mList.toString());
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 获取选中的gass
	public void selectgass(){
		gass = new ArrayList<Type>();
		select_gass_num = 0;
		for (int i = 0; i < mTypeList.size(); i++) {
			int num = Integer.parseInt(mTypeList.get(i).getNum());
			if(num > 0){
				Type type = new Type();
				type.setPrice(mTypeList.get(i).getPrice());// 商品单价
				type.setWeight(mTypeList.get(i).getWeight());// 商品规格
				type.setName(mTypeList.get(i).getName());// 商品名
				type.setNum(mTypeList.get(i).getNum());// 商品数量
				type.setId(mTypeList.get(i).getId());// 商品Id;
				type.setType(mTypeList.get(i).getType());// 商品Id;
				type.setNorm_id(mTypeList.get(i).getNorm_id());// 商品的Norm_id
				type.setYj_price(Double.parseDouble(mTypeList.get(i).getPrice()) * num+"");// 单类商品总价
				gass.add(type);
				
			}
			select_gass_num+=Integer.parseInt(mTypeList.get(i).getNum());
		}
	}
	
	public void getSelect(){
		ArrayList<Type> select = new ArrayList<Type>();
		for (int i = 0; i < gass.size(); i++) {
			int number = Integer.parseInt(gass.get(i).getNum());
			String type = gass.get(i).getNorm_id();
			Log.e("lll", type);
			for (int j = 0, k = 0; j < mList.size()&& number>k; j++) {
				if(type.equals(mList.get(j).getType())){
					select.add(mList.get(j));
					k++;
				}
			}
			
		}
		select.addAll(mList_youhui);
			Intent zhekou = new Intent(CreateGassActivity.this,DeductionActivity.class);
			Bundle bun = new Bundle();
			bun.putSerializable("select", (Serializable) select);
			bun.putSerializable("all", (Serializable) mList);
			zhekou.putExtra("select", bun);
			startActivityForResult(zhekou, 2);;
		
		
	}

}
