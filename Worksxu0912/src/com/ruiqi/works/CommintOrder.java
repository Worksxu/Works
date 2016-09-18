package com.ruiqi.works;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.adapter.DeletItemAdapter;
import com.ruiqi.adapter.MoneyAdapter;


import com.ruiqi.adapter.MoneyAdapter.addMoney;
import com.ruiqi.adapter.MoneyAdapter.reduceMoney;
import com.ruiqi.bean.Type;
import com.ruiqi.bean.ZheKouContent;
import com.ruiqi.utils.ChaiMyDialog.ChaiCallBack;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.XuDialog;
import com.ruiqi.utils.XuDialog.No;
import com.ruiqi.utils.XuDialog.Yes;
import com.ruiqi.view.CustomDownView;
import com.ruiqi.view.SwipeMenu;
import com.ruiqi.view.SwipeMenuCreator;
import com.ruiqi.view.SwipeMenuItem;
import com.ruiqi.view.SwipeMenuListView;
import com.ruiqi.view.SwipeMenuListView.OnMenuItemClickListener;
import com.ruiqi.xuworks.CreateGassActivity;
import com.ruiqi.xuworks.NewOrderInfoActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommintOrder extends BaseActivity implements addMoney,reduceMoney,ParserData,Yes,No{
	private TextView tv_money,tv_toast,tv_order,tv_goodsMoney,tv_manjian,tv_youhui,tv_deduction;
	private LinearLayout ll_manjian,ll_youhui,ll_zhekou,ll_deduction;
	private CustomDownView cdv_youhui;
	private List<Type> goods;
	private List<Type> zhekou = new ArrayList<Type>();
	private ArrayList<Integer> deduction  = new ArrayList<Integer>();
	private MoneyAdapter goodsAdapter;
	private DeletItemAdapter delete;
	String order_number,kid;
	private SwipeMenuListView lv_zhekou,lv_youhui,lv_goods;
//	XuDialog dialog = new XuDialog();
	String youhui_money,youhui_id;
	Bundle bundle;
	ZheKouContent data;
	double totail_money ,goods_money,zhekou_money,youhui_money1,deduction_money,peijian_noney;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commint_order_layout);
		bundle = getIntent().getBundleExtra("gass");
		goods = (List<Type>) bundle.getSerializable("mDatas");
//		zhekou = (List<Type>) bundle.getSerializable("mzhekou");
		deduction = (ArrayList<Integer>) bundle.getSerializable("mdeduction");
		deduction_money = bundle.getDouble("deduction_money");
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("确认订单信息");
		ll_zhekou = (LinearLayout) findViewById(R.id.ll_commint_zhekou);
		ll_deduction = (LinearLayout) findViewById(R.id.ll_commint_deduction);
		ll_zhekou.setOnClickListener(this);
		tv_money = (TextView) findViewById(R.id.textView_commint_Toast);
		tv_toast = (TextView) findViewById(R.id.textView_ToastYouhui);
		tv_order = (TextView) findViewById(R.id.textView_commint_order);
		tv_goodsMoney = (TextView) findViewById(R.id.textView_goodsMoney);
		tv_manjian = (TextView) findViewById(R.id.textView_manjianMoney);
		tv_youhui = (TextView) findViewById(R.id.textView_youhuiMoney);
		tv_deduction = (TextView) findViewById(R.id.textView_deductionMoney);
		ll_manjian = (LinearLayout) findViewById(R.id.ll_commint_manjian);
		ll_youhui = (LinearLayout) findViewById(R.id.ll_commint_youhui);
		tv_order.setOnClickListener(this);
		lv_goods = (SwipeMenuListView) findViewById(R.id.listView_commintTost);
		goodsAdapter = new MoneyAdapter(goods, CommintOrder.this);
		goodsAdapter.setAdd((com.ruiqi.adapter.MoneyAdapter.addMoney) this);
		goodsAdapter.setReduce((com.ruiqi.adapter.MoneyAdapter.reduceMoney) this);
		lv_goods.setAdapter(goodsAdapter);
		lv_youhui = (SwipeMenuListView) findViewById(R.id.lv_commint_youhui);
		lv_zhekou = (SwipeMenuListView) findViewById(R.id.lv_commint_zhekou);
//		if(zhekou.size()>0){
//			delete = new DeletItemAdapter(CommintOrder.this, (ArrayList<Type>) zhekou);
//			lv_zhekou.setAdapter(delete);
//		}else{
//			
//		}
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.lajitong_xhdpi);
				// add to menu
				menu.addMenuItem(deleteItem);
				
			}
		};
		lv_zhekou.setMenuCreator(creator);
		lv_zhekou.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				// TODO Auto-generated method stub
				switch (index) {
				case 0:
					zhekou.remove(position);
					delete.notifyDataSetChanged();
					getMoney(youhui_money1,deduction_money);
					break;

				default:
					break;
				}
			}
		});
		
		cdv_youhui = (CustomDownView) findViewById(R.id.commint_youhui);
		cdv_youhui.setOnClickListener(this);
		cdv_youhui.setString("优惠活动");
		cdv_youhui.setView(View.VISIBLE);
		getMoney(0,deduction_money);
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.commint_youhui:
			if(getMoney(0,deduction_money) == 0){
				Toast.makeText(CommintOrder.this, "优惠不可用", 1).show();
			}else{
			Intent intent = new Intent(CommintOrder.this,SelectYouhuiActivity.class);
			startActivityForResult(intent, 1);}
			break;
		case R.id.textView_commint_order:
//			if(tv_money.getText().toString().equals("0.0")){
//				Toast.makeText(CommintOrder.this, "请选择商品", 1).show();
//			}else{
			XuDialog.getInstance().setno(this);
			XuDialog.getInstance().setyes(this);
			XuDialog.getInstance().show(CommintOrder.this, "是否提交", 1);
//			}
			break;
		case R.id.ll_commint_zhekou:
			Intent zhekou = new Intent(CommintOrder.this,AddZhekou.class);
			startActivityForResult(zhekou, 2);
			break;
		case R.id.rl_back:
			finish();
			break;
		default:
			break;
		}
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
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		 bundle = new Bundle();
//		 if(arg1 == 1){
//			
//		 }
		 switch (arg1) {
		case 1:
			 bundle = arg2.getBundleExtra("youhui");
				youhui_money1 = bundle.getDouble("youhui_money");
				tv_toast.setVisibility(View.VISIBLE);
				tv_toast.setText(bundle.getString("youhui_name")+" "+youhui_money1+"");
				getMoney(youhui_money1,deduction_money);
			break;
		case 2:
			zhekou = new ArrayList<Type>();
			bundle = new Bundle();
			bundle = arg2.getBundleExtra("zhekou");
			zhekou = (List<Type>) bundle.getSerializable("zhekou_list");
			
//			zhekou.add(type);
			delete = new DeletItemAdapter(CommintOrder.this,(ArrayList<Type>) zhekou);
			lv_zhekou.setVisibility(View.VISIBLE);
			lv_zhekou.setAdapter(delete);
			getMoney(youhui_money1,deduction_money);
			lv_zhekou.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				
				@Override
				public void onMenuItemClick(int position, SwipeMenu menu, int index) {
					// TODO Auto-generated method stub
					switch (index) {
					case 0:
						zhekou.remove(position);
						delete.notifyDataSetChanged();
						getMoney(youhui_money1,deduction_money);
						if(zhekou.size() == 0){
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
	// 计算钱的方法
	
	public double getMoney(double preferential,double deduction){
		goods_money = 0;
		zhekou_money = 0;
		peijian_noney = 0;//配件的钱
		for (int i = 0; i < goods.size(); i++) {
			goods_money += Double.parseDouble(goods.get(i).getPrice()) * Integer.parseInt(goods.get(i).getNum());
			Log.e("sdfdsfd_选中气的类型", goods.get(i).getType());
			if(goods.get(i).getType().equals("2")){
				peijian_noney +=Double.parseDouble(goods.get(i).getPrice()) * Integer.parseInt(goods.get(i).getNum());
				Log.e("sdfdsfd_选中配件的钱", peijian_noney+"");
			}
		}
		if(zhekou.size()>0){
			for (int i = 0; i < zhekou.size(); i++) {
				zhekou_money +=Double.parseDouble(zhekou.get(i).getPrice()) * 1;
			}
		}else{
			
		}
		
		totail_money = goods_money + zhekou_money - preferential - deduction;
		if(totail_money < 0){
			tv_money.setText("0.0");
		}else{
		tv_money.setText(totail_money+"");}
		tv_goodsMoney.setText(goods_money + zhekou_money+"");
		if(preferential > 0){
			ll_youhui.setVisibility(View.VISIBLE);
			tv_youhui.setText(-preferential+"");
		}else{
			ll_youhui.setVisibility(View.GONE);
		}
		if(deduction > 0){
			ll_deduction.setVisibility(View.VISIBLE);
			tv_deduction.setText(-deduction+"");
		}else{
			ll_deduction.setVisibility(View.GONE);
		}
		return totail_money;
	}

	@Override
	public void reduce() {
		// TODO Auto-generated method stub
		
		getMoney(youhui_money1,deduction_money);
	}

	@Override
	public void add() {
		// TODO Auto-generated method stub
		
		getMoney(youhui_money1,deduction_money);
	}
	//滑动删除用的类
		private int dp2px(int dp) {
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
					getResources().getDisplayMetrics());
		}
		/**
		 * 拼接json字符串
		 * 
		 * @return
		 */
		private String getJsonData(List<Type> popupList) {
			JSONArray arr = new JSONArray();
			// 订单
			for (int j = 0; j < popupList.size(); j++) {
				JSONObject obj1 = new JSONObject();
				try {
					obj1.put("good_id", popupList.get(j).getId());
					obj1.put("good_num", popupList.get(j).getNum());
					obj1.put("good_name", popupList.get(j).getName());
					
					obj1.put("good_price",Double.parseDouble(popupList.get(j).getPrice()));
					
					obj1.put("good_type", popupList.get(j).getType());
					obj1.put("good_kind", popupList.get(j).getNorm_id());
					// obj1.put("wb", weightBottle);
					arr.put(obj1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return arr.toString();
		}
		private String getzhekouJsonData(List<Type> popupList) {
			JSONArray arr = new JSONArray();
			
			// 订单
			for (int j = 0; j < popupList.size(); j++) {
				JSONObject obj1 = new JSONObject();
				try {
					obj1.put("good_id", popupList.get(j).getId());
					obj1.put("good_num", "1");
					obj1.put("good_name", popupList.get(j).getName());
					
					obj1.put("good_price",Double.parseDouble(popupList.get(j).getPrice()));
					
					obj1.put("good_type", popupList.get(j).getType());
					obj1.put("good_kind", popupList.get(j).getWeight());
					obj1.put("is_zhekou", "1");
					// obj1.put("wb", weightBottle);
					arr.put(obj1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return arr.toString();
		}
		private String getdeductionData(ArrayList<Integer> popupList) {
			JSONArray arr = new JSONArray();
			for (int i = 0; i < popupList.size(); i++) {
				arr.put(popupList.get(i));
			}
			
			return arr.toString();
		}
		private HttpUtil httpUtil;
		void Commint(){
			httpUtil=new HttpUtil();
			httpUtil.setParserData(this);
			String shipper_id = (String) SPUtils.get(CommintOrder.this, SPutilsKey.SHIP_ID, "error");
			String shop_id = (String) SPUtils.get(CommintOrder.this, SPutilsKey.SHOP_ID, "error");
			
			
			RequestParams params = new RequestParams(RequestUrl.NEWCREATEORDER);
			params.addBodyParameter("mobile", (String) SPUtils.get(CommintOrder.this, "oldUserMobile", ""));
			params.addBodyParameter("sheng", (String) SPUtils.get(CommintOrder.this, "sheng", ""));
			params.addBodyParameter("shi", (String) SPUtils.get(CommintOrder.this, "shi", ""));
			params.addBodyParameter("qu", (String) SPUtils.get(CommintOrder.this, "qu", ""));
			params.addBodyParameter("cun", (String) SPUtils.get(CommintOrder.this, "cun", ""));
			params.addBodyParameter("address", (String) SPUtils.get(CommintOrder.this, "oldUserAddress", ""));
			params.addBodyParameter("card_sn", (String) SPUtils.get(CommintOrder.this, "card_sn", ""));
			params.addBodyParameter("user_type", (String) SPUtils.get(CommintOrder.this, "user_type", ""));
			params.addBodyParameter("shipper_id", shipper_id);
			params.addBodyParameter("shop_id", shop_id);
			params.addBodyParameter("shipper_name", (String) SPUtils.get(CommintOrder.this, "shipper_name", ""));
			params.addBodyParameter("shipper_mobile", (String) SPUtils.get(CommintOrder.this,  SPutilsKey.MOBILLE, ""));
			params.addBodyParameter("money", tv_goodsMoney.getText().toString());// 商品金额
			params.addBodyParameter("count_money", youhui_money1+"");// 优惠金额
			params.addBodyParameter("yh_money", deduction_money+"");// 抵扣券
			params.addBodyParameter("urgent", "0");
			params.addBodyParameter("goodtime", "0");
			params.addBodyParameter("isold", "0");
			params.addBodyParameter("ismore", "0");
			params.addBodyParameter("tc_type", "0");
			params.addBodyParameter("tc_type", "0");
			params.addBodyParameter("token", "1234");
			params.addBodyParameter("username", (String) SPUtils.get(CommintOrder.this, "oldUserName", ""));
			params.addBodyParameter("youhuijson", getdeductionData(deduction));
			params.addBodyParameter("data", getJsonData(goods));// 商品的data串
			
			Log.e("lll", zhekou_money+peijian_noney+"");
			params.addBodyParameter("peijian", getzhekouJsonData(zhekou));// 折旧物品data串
			params.addBodyParameter("peijian_money", zhekou_money+peijian_noney+"");// 
			httpUtil.PostHttp(params, 0);
//			HttpUtil.PostHttp(params, handler, what);
			Log.e("lll", params.getBodyParams().toString());
		}

		@Override
		public void sendResult(String result, int what) {
			// TODO Auto-generated method stub
			Log.e("lll", result);
			try {
				JSONObject object = new JSONObject(result);
				String info = object.getString("resultInfo");
				if(object.getInt("resultCode") == 1){
					JSONObject obj = object.getJSONObject("resultInfo");
				order_number = obj.getString("ordersn");// 订单号
				 kid = obj.getString("kid");
					
					SPUtils.put(CommintOrder.this, "kid", kid);
					SPUtils.put(CommintOrder.this, "goodsData",  getJsonData(goods));
					XuDialog.getInstance().show(CommintOrder.this, "提交成功是否开始配送", 2);
					
				}else{
					Toast.makeText(CommintOrder.this, info, 1).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void XuNo(int i) {
			// TODO Auto-generated method stub
			if(i == 1){
				Log.e("lll", "tijiao");
				// 
			}else{
				Intent intent = new Intent(CommintOrder.this,HomePageActivity.class);
				startActivity(intent);
				finish();
			}
		}

		@Override
		public void XuCallback(int i) {
			// TODO Auto-generated method stub
			if(i == 1){
				Log.e("lll", "yesTi");
				Commint();
				
			}else{
				Intent intent = new Intent(CommintOrder.this,NewOrderInfoActivity.class);
				intent.putExtra("ordersn", 	order_number);
				intent.putExtra("kid", 	kid);
				startActivity(intent);
				finish();
			}
		}

		

}

