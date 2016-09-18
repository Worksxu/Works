package com.ruiqi.xuworks;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.ruiqi.adapter.UserTypeAdapter;
import com.ruiqi.bean.Orderdeail;
import com.ruiqi.bean.Type;
import com.ruiqi.bean.Weight;
import com.ruiqi.fragment.OrderInfoFragment;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.XuDialog.No;
import com.ruiqi.utils.XuDialog.Yes;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.XuDialog;
import com.ruiqi.view.popupwindow.SelectOrderInfoPopupWindow;
import com.ruiqi.view.popupwindow.SelectOrderInfoPopupWindow.PopDismiss;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.CommintOrder;
import com.ruiqi.works.HomePageActivity;
import com.ruiqi.works.NfcActivity;
import com.ruiqi.works.NullActivity;
import com.ruiqi.works.R;
import com.ruiqi.works.RegisterActivity;
import com.ruiqi.works.SelectYouhuiActivity;
import com.ruiqi.works.WorksYouhuiActivity;

public class PayMoneyActivity extends BaseActivity implements ParserData,PopDismiss,Yes,No{
	private TextView tv_yunfei_title, tv_money, tv_songqi_title, tv_pay_title,
	tv_back_modify, tv_order_commit,tv_userYouhui;
	private EditText et_num;// 优惠数量
	private TextView tv_name, tv_phone, tv_address, tv_time, tv_yunfei_money,
	tv_songqi_money, tv_pay, tv_yajin;
	private TextView tv_zheijiu_money, tv_canye_money, tv_pj_money, tv_content,tv_youhui_select,tv_order_money,tv_youhuiMoney,tv_yuqiMoney,tv_peijian; // 押金，残液，折旧,余气,优惠
	private LinearLayout ll_content ,ll_youhui,ll_payway,ll_typemoney;
	private TextView tv_youhui,tv_moneyType,tv_payway,tv_number;
	private double shangPinMoney,cash,youhuiOrder,zhejiu,canye,yuqi,total;// 订单产生的各种金额
	double  youhuimoney = 0; 
	double  debt_cash = 0; 
	double  debt_goods = 0; 
	List<String> sList;// 支付类型
	private ListView lv_select_address;
	private SelectOrderInfoPopupWindow old;// 类型弹出框
	String goodsNumber,goodsMoney,cashNumber,cashMoney,isyouhui ;// 欠款合同押金及状态1代表有这个状态
	String ispay="0";
	int numberWeight;// 0现金1网上
	int is_cash = 0;
	String data,nodata,nodatajson;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paymoney_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("确认收款");
		if (NfcActivity.mDataWeight != null) {
			if (NfcActivity.mDataWeight.size() > 0) {
				 data = getJsonStr(NfcActivity.mDataWeight);
				 numberWeight = NfcActivity.mDataWeight.size();
			}
		}
		if (NfcActivity.mDataNull != null) {
			if (NfcActivity.mDataNull.size() > 0) {
				nodatajson = (String) SPUtils.get(PayMoneyActivity.this, "null_data", "");
				nodata = getJsonStr(NfcActivity.mDataNull);//空瓶码
			}
		}
		tv_userYouhui = (TextView) findViewById(R.id.tv_user_money);
		tv_userYouhui.setText((CharSequence) SPUtils.get(PayMoneyActivity.this, "goodsyouhui", ""));
		lv_select_address = (ListView) findViewById(R.id.lv_select_address);
		ll_youhui = (LinearLayout) findViewById(R.id.rl_youhui);
		ll_youhui.setOnClickListener(this);
		ll_payway = (LinearLayout) findViewById(R.id.ll_payMoney_way);
		ll_payway.setOnClickListener(this);
		ll_typemoney = (LinearLayout) findViewById(R.id.ll_payMoney_type);
		tv_youhui = (TextView) findViewById(R.id.textView_payMoney_youhui);
		tv_number = (TextView) findViewById(R.id.textView_payMoney_orderNumber);
		tv_number.setText((CharSequence) SPUtils.get(PayMoneyActivity.this, "goodsNumber", ""));
		tv_moneyType = (TextView) findViewById(R.id.textView_type_money);
		tv_payway = (TextView) findViewById(R.id.textView_select_Pay_way);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_name.setText((CharSequence) SPUtils.get(PayMoneyActivity.this, "goodsName", ""));
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_phone.setText((CharSequence) SPUtils.get(PayMoneyActivity.this, "goodsPhone", ""));
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_address.setText((CharSequence) SPUtils.get(PayMoneyActivity.this, "goodsAddress", ""));
		tv_peijian = (TextView) findViewById(R.id.tv_peijian_money);
		tv_peijian.setText((CharSequence) SPUtils.get(PayMoneyActivity.this, "peijian_total", ""));
		tv_order_money = (TextView) findViewById(R.id.tv_order_money);//订单应付款
		tv_order_money.setText((CharSequence) SPUtils.get(PayMoneyActivity.this, "goodsMoney", ""));
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_time.setText(getNowTime());

		tv_yunfei_money = (TextView) findViewById(R.id.tv_yunfei_money);//押金应付金额
		et_num = (EditText) findViewById(R.id.edit_pay);
		et_num.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Log.e("dsfds_youhui", s.toString());
				
				if(!TextUtils.isEmpty(s.toString())){
					if(youhuimoney*Integer.parseInt(et_num.getText().toString().trim())>(shangPinMoney - youhuiOrder  -Double.parseDouble((String) SPUtils.get(PayMoneyActivity.this, "goodsyouhui", "")) - debt_goods - debt_cash)){
						Toast.makeText(PayMoneyActivity.this, "优惠已大于商品金额", 1).show();
						et_num.setText("1");
					}else{
					getMoney(youhuimoney*Integer.parseInt(et_num.getText().toString().trim()), debt_goods, debt_cash);
					tv_youhuiMoney.setText(youhuimoney*Integer.parseInt(et_num.getText().toString().trim())+"");}
				}
				
				
			}
		});
		tv_pj_money = (TextView) findViewById(R.id.tv_pj_money);// 优惠订单
		tv_pj_money.setText((CharSequence) SPUtils.get(PayMoneyActivity.this, "goodsdeduction", ""));// 余气券
		tv_youhuiMoney = (TextView) findViewById(R.id.tv_youhui_money);// 优惠券送气工

		tv_pay = (TextView) findViewById(R.id.tv_pay);
		
	

		tv_pay_title = (TextView) findViewById(R.id.tv_pay_title);
		tv_pay_title.setText("应收款");

		tv_back_modify = (TextView) findViewById(R.id.tv_back_modify);
		tv_back_modify.setText("欠款");
		tv_back_modify.setOnClickListener(this);

		tv_order_commit = (TextView) findViewById(R.id.tv_order_commit);
		tv_order_commit.setText("确定收款");
		tv_order_commit.setOnClickListener(this);

		tv_yuqiMoney = (TextView) findViewById(R.id.tv_yuqi_money);// 余气money
	
		tv_zheijiu_money = (TextView) findViewById(R.id.tv_zheijiu_money);// 折旧金额
	
		tv_canye_money = (TextView) findViewById(R.id.tv_canye_money);// 残液金额
		if(NewOrderInfoActivity.mList.size()>0){
			tv_yunfei_money.setText((CharSequence) SPUtils.get(PayMoneyActivity.this, "yajin", ""));//押金
			
			if(SPUtils.get(PayMoneyActivity.this, "old_new", "").equals("1")&&!TextUtils.isEmpty((CharSequence) SPUtils.get(PayMoneyActivity.this, "zhejiu", ""))){
				tv_zheijiu_money.setText((CharSequence) SPUtils.get(PayMoneyActivity.this, "zhejiu", ""));//折旧钱
			}
			
		}
//		if
//		tv_canye_money.setText((CharSequence) SPUtils.get(PayMoneyActivity.this, "canye_money", ""));
		// 给订单产生的款项明细赋值
		if(NewOrderInfoActivity.mList.size()>0&&!TextUtils.isEmpty((String) SPUtils.get(PayMoneyActivity.this, "yuqi", ""))&&!TextUtils.isEmpty(nodata)&&NullActivity.null_bottle.equals("1")){
			yuqi = Double.parseDouble((String) SPUtils.get(PayMoneyActivity.this, "yuqi", ""));
			tv_yuqiMoney.setText((CharSequence) SPUtils.get(PayMoneyActivity.this, "yuqi", ""));
		}
		
		if(NewOrderInfoActivity.mList.size()>0&&!TextUtils.isEmpty((String) SPUtils.get(PayMoneyActivity.this, "yajin", ""))){
			cash = Double.parseDouble((String) SPUtils.get(PayMoneyActivity.this, "yajin", ""));
		}
		if(NewOrderInfoActivity.mList.size()>0&&!TextUtils.isEmpty((String) SPUtils.get(PayMoneyActivity.this, "zhejiu", ""))){
			zhejiu = Double.parseDouble((String) SPUtils.get(PayMoneyActivity.this, "zhejiu", ""));
		}
		
		if(SPUtils.get(PayMoneyActivity.this, "old_new", "").equals("1")){
			if(NewOrderInfoActivity.mList.size()>0&&!TextUtils.isEmpty((String) SPUtils.get(PayMoneyActivity.this, "canye_money", ""))){
				canye = Double.parseDouble((String) SPUtils.get(PayMoneyActivity.this, "canye_money", ""));
				tv_canye_money.setText(canye+"");
			}
		}
		
		
		
//		peijian = Double.parseDouble((String) SPUtils.get(PayMoneyActivity.this, "peijian_total", ""));
		OrderInfoFragment oif = new OrderInfoFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("mData", (Serializable) NewOrderInfoActivity.list);
		oif.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, oif).commit();
		Log.e("gsgagag", "lai a ");
		getMoney(youhuimoney*Integer.parseInt(et_num.getText().toString().trim()),0,0);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("gsgagag", "lai a ");
		if(!TextUtils.isEmpty(et_num.getText().toString().trim())){
			getMoney(youhuimoney*Integer.parseInt(et_num.getText().toString().trim()), debt_goods, debt_cash);
		}
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_back_modify:// 欠款
			if(tv_pay.getText().toString().equals("0.0")){
				Toast.makeText(PayMoneyActivity.this, "当前金额为0", 1).show();
			}else{
			SPUtils.put(PayMoneyActivity.this, "payTotal", getMoney(youhuimoney*Integer.parseInt(et_num.getText().toString().trim()), 0, 0));
			Intent debt = new Intent(PayMoneyActivity.this,DebtActivity.class);
			startActivityForResult(debt, 2);
			}
			break;
		case R.id.tv_order_commit:// 确认收款
//			if(is_cash == 5){
//				Toast.makeText(PayMoneyActivity.this, "请选择支付方式", 1).show();
//			}else{
			initData();
//			}
			break;
//		case R.id.ll_payMoney_way:// 支付方式
//			initType();
//			break;
		case R.id.rl_youhui:// 选择优惠券
			if(tv_pay.getText().toString().equals("0.0")||(shangPinMoney-youhuiOrder) == 0){
				Toast.makeText(PayMoneyActivity.this, "优惠不可用", 1).show();
			}else{
			Intent youhui = new Intent(PayMoneyActivity.this,WorksYouhuiActivity.class);
			startActivityForResult(youhui, 1);
			}
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
	
	private String getNowTime() {
		Date nowDate = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String now = df.format(nowDate);
		return now;
	}
	Bundle bundle ;
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		switch (arg1) {
		case 1:
			bundle = new Bundle();
			bundle = arg2.getBundleExtra("youhui");
			youhuimoney = bundle.getDouble("youhui_money");
//			tv_youhui.setText(bundle.getString("youhui_name")+youhuimoney+"");
//			tv_youhuiMoney.setText(youhuimoney*Integer.parseInt(et_num.getText().toString().trim())+"");
			if(!(youhuimoney ==0)){
				isyouhui = "1";
			}
			if(TextUtils.isEmpty(et_num.getText().toString())){
				Toast.makeText(PayMoneyActivity.this, "请选择数量", 1).show();
			}else{
			if(youhuimoney*Integer.parseInt(et_num.getText().toString().trim())>(shangPinMoney   - youhuiOrder  -Double.parseDouble((String) SPUtils.get(PayMoneyActivity.this, "goodsyouhui", "")) - debt_goods - debt_cash)){
				Toast.makeText(PayMoneyActivity.this, "优惠已大于商品金额", 1).show();
				youhuimoney = 0;
				tv_youhui.setText(bundle.getString("youhui_name")+youhuimoney+"");
				tv_youhuiMoney.setText(youhuimoney*Integer.parseInt(et_num.getText().toString().trim())+"");
				et_num.setText("1");
			}else{
				if(!TextUtils.isEmpty(et_num.getText().toString().trim())){
					tv_youhui.setText(bundle.getString("youhui_name")+youhuimoney+"");
					tv_youhuiMoney.setText(youhuimoney*Integer.parseInt(et_num.getText().toString().trim())+"");
					getMoney(youhuimoney*Integer.parseInt(et_num.getText().toString().trim()), debt_goods ,debt_cash);
				}
			}
			}
			
			break;
		case 2:
			ll_typemoney.setVisibility(View.VISIBLE);
			bundle = new Bundle();
			bundle = arg2.getBundleExtra("debt");
			goodsMoney = bundle.getString("goodsMoney");
			goodsNumber = bundle.getString("goodsNumber");
			cashMoney = bundle.getString("cashMoney");
			cashNumber = bundle.getString("cashNumber");
			if(!TextUtils.isEmpty(goodsMoney)){
				tv_moneyType.setText("商品欠款"+bundle.getString("goodsMoney"));
				debt_goods = Double.parseDouble(goodsMoney);
			}
			if(!TextUtils.isEmpty(cashMoney)){
				tv_moneyType.setText("押金欠款"+bundle.getString("cashMoney"));
				debt_cash = Double.parseDouble(cashMoney);
			}
			if(!TextUtils.isEmpty(goodsMoney)&&!TextUtils.isEmpty(cashMoney)){
				tv_moneyType.setText("商品欠款"+bundle.getString("goodsMoney")+"押金欠款"+bundle.getString("cashMoney"));
			}
			if(!TextUtils.isEmpty(goodsMoney)||!TextUtils.isEmpty(cashMoney)){
				ispay = "1";
			}
			getMoney(youhuimoney*Integer.parseInt(et_num.getText().toString().trim()), debt_goods, debt_cash);
			break;

		default:
			break;
		}
	}
	private void initType() {
		// TODO Auto-generated method stub
		 sList = new ArrayList<String>();
		sList.add("现金支付");
		sList.add("网上支付");
		
//		MyAdapter myAdapter = new MyAdapter(RegisterActivity.this, list);
		UserTypeAdapter adapter = new UserTypeAdapter(PayMoneyActivity.this, sList);
		old = new SelectOrderInfoPopupWindow(PayMoneyActivity.this, itemsOnClickType, lv_select_address, adapter);
		Log.e("lll", "进类");
		old.setPopDismiss(this);
		old.showAtLocation(PayMoneyActivity.this.findViewById(R.id.ll_main),Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	// listview子项的点击 用户
			private OnItemClickListener itemsOnClickType = new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					String type = sList.get(position);
					tv_payway.setText(type);
					if(type.equals("现金支付")){
						is_cash = 0;
						Log.e("lll", "1");
					}else if(type.equals("网上支付")){
						is_cash = 1;
						
					}
					old.dismiss();
				}
			};

	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("lll", result);
		try {
			JSONObject object = new JSONObject(result);
			int code = object.getInt("resultCode");
			
			if(code == 1){
				if(NfcActivity.mDataWeight != null){
					NfcActivity.mDataWeight = null;
				}
				if(NfcActivity.mDataNull != null){
					NfcActivity.mDataNull = null;
				}
				if(!TextUtils.isEmpty((CharSequence) SPUtils.get(PayMoneyActivity.this, "debtgoods", ""))){
				SPUtils.remove(PayMoneyActivity.this, "debtgoods");	
				}
				if(!TextUtils.isEmpty((CharSequence) SPUtils.get(PayMoneyActivity.this, "debtyj", ""))){
					SPUtils.remove(PayMoneyActivity.this, "debtyj");
				}
				
				SPUtils.remove(PayMoneyActivity.this, "yajin");
				SPUtils.remove(PayMoneyActivity.this, "yuqi");
				SPUtils.remove(PayMoneyActivity.this, "canye_money");
				SPUtils.remove(PayMoneyActivity.this, "canye_weight");
				SPUtils.remove(PayMoneyActivity.this, "zhejiu");
				SPUtils.remove(PayMoneyActivity.this, "zheijiu_ping");
				Toast.makeText(PayMoneyActivity.this, object.getString("resultInfo"), 1).show();
				XuDialog.getInstance().setno(this);
				XuDialog.getInstance().setyes(this);
				XuDialog.getInstance().show(PayMoneyActivity.this, "是否进行安全检查", 1);
			}else{
				Toast.makeText(PayMoneyActivity.this, object.getString("resultInfo"), 1).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void popDismissCalBack() {
		// TODO Auto-generated method stub
		
		
	}
	/*
	 * 计算应收款项
	 */
	double getMoney(double select,double goods,double debtcash){
		shangPinMoney = Double.parseDouble((String) SPUtils.get(PayMoneyActivity.this, "goodsMoney", ""));
		youhuiOrder = Double.parseDouble((String) SPUtils.get(PayMoneyActivity.this, "goodsdeduction", ""));// 余瓶券
		if(NewOrderInfoActivity.mList.size()>0){
			total = shangPinMoney + cash  - youhuiOrder - zhejiu - canye - yuqi -Double.parseDouble((String) SPUtils.get(PayMoneyActivity.this, "goodsyouhui", "")) - select -goods-debtcash;
		}else{
			total = shangPinMoney   - youhuiOrder  -Double.parseDouble((String) SPUtils.get(PayMoneyActivity.this, "goodsyouhui", "")) - select -goods-debtcash;
		}
		if(total<0){
			total = 0.0;
		}
		tv_pay.setText(total+"");
		
		return total;
	}
	private HttpUtil httpUtil;
	
	private void initData() {
		// TODO Auto-generated method stub
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		RequestParams params = new RequestParams(RequestUrl.CONFIRMORDER);
		params.addBodyParameter("ordersn", tv_number.getText().toString());// 订单编号
		params.addBodyParameter("kid", (String) SPUtils.get(PayMoneyActivity.this, "kid", ""));// 客户id
		Log.e("lll", (String) SPUtils.get(PayMoneyActivity.this, "kid", "error"));
		params.addBodyParameter("pay_money", tv_pay.getText().toString());
//		params.addBodyParameter("deposit_num", (String) SPUtils.get(PayMoneyActivity.this, "total_num", ""));// 押金数量
		if(NewOrderInfoActivity.mList.size()>0){
			params.addBodyParameter("deposit", tv_yunfei_money.getText().toString());// 押金钱数
			if(SPUtils.get(PayMoneyActivity.this, "old_new", "").equals("1")&&!tv_zheijiu_money.getText().toString().equals("0")){
				params.addBodyParameter("raffinat", (String) SPUtils.get(PayMoneyActivity.this, "canye_money", ""));// 残液金额
				params.addBodyParameter("raffinat_weight", (String) SPUtils.get(PayMoneyActivity.this, "canye_weight", ""));// 残液重量
				params.addBodyParameter("depreciation", (String) SPUtils.get(PayMoneyActivity.this, "zhejiu", ""));// 折旧
			}
			
		}
		
		
		
		
		if(SPUtils.get(PayMoneyActivity.this, "old_new", "").equals("1")&&!SPUtils.get(PayMoneyActivity.this, "zhejiu", "").equals("0")){
			params.addBodyParameter("zheijiu_ping", (String) SPUtils.get(PayMoneyActivity.this, "zheijiu_ping", ""));// 折旧瓶data串
		}
		
		params.addBodyParameter("data", data);// 重瓶串
		if(NfcActivity.mDataNull != null&&NfcActivity.mDataNull.size()>0){
			
			params.addBodyParameter("nodata", nodata);// 空瓶码
			if(NullActivity.null_bottle.equals("1")){
				params.addBodyParameter("is_more", "1");// 0无,1有
				params.addBodyParameter("nodatajson", nodatajson);// 空瓶串详情
				
			}else{
				params.addBodyParameter("is_more", "0");// 0无,1有
				
			}
			
		}
//		params.addBodyParameter("shop_id", (String) SPUtils.get(PayMoneyActivity.this,
//				SPutilsKey.SHOP_ID, "error"));// 门店id
		if(!TextUtils.isEmpty(ispay)){
			
			params.addBodyParameter("is_pay", ispay);// 是否为欠款
		}
		if(!TextUtils.isEmpty(cashNumber)){
			params.addBodyParameter("deposit_contractno", cashNumber);// 押金合同编号
		}
		if(!TextUtils.isEmpty(cashMoney)){
			params.addBodyParameter("deposit_money", cashMoney);// 押金欠款金额
		}
		if(!TextUtils.isEmpty(goodsNumber)){
			params.addBodyParameter("bottle_contractno", goodsNumber);// 商品欠款合同编号
		}
		if(!TextUtils.isEmpty(goodsMoney)){
			params.addBodyParameter("bottle_money", goodsMoney);// 商品欠款金额
		}
		
		params.addBodyParameter("is_cash", "0");// 支付方式默认现金
		if(NewOrderInfoActivity.peijianList.size()>0){
			params.addBodyParameter("peijian", getPeijianData(NewOrderInfoActivity.peijianList));// 配件的data串折扣的data串也在里面用来减少库存
		}
//		if(!TextUtils.isEmpty((CharSequence) SPUtils.get(PayMoneyActivity.this, "peijian_total", ""))){
//			params.addBodyParameter("peijian_money", (String) SPUtils.get(PayMoneyActivity.this, "peijian_total", ""));// 配件金额
//		}
		
//		params.addBodyParameter("shipper_id", (String) SPUtils.get(PayMoneyActivity.this,
//				SPutilsKey.SHIP_ID, "error"));// 送气工id
//		params.addBodyParameter("shipper_name", (String) SPUtils.get(PayMoneyActivity.this, "shipper_name",
//				"error"));// 送气工姓名
//		Log.e("lll", (String) SPUtils.get(PayMoneyActivity.this, "shipper_name", "error"));
//		params.addBodyParameter("shipper_mobile", (String) SPUtils.get(PayMoneyActivity.this,
//				SPutilsKey.MOBILLE, "error"));// 送气工电话
//		params.addBodyParameter("isyouhui", isyouhui);// 是否优惠
		if(!tv_youhuiMoney.getText().toString().equals("0")){
			params.addBodyParameter("shipper_money", tv_youhuiMoney.getText().toString());// 优惠金额送气工的
		}
		
//		params.addBodyParameter("selfjson", "");// 安全报告
//		params.addBodyParameter("is_safe", "0");// 是否安全报告
		if(NewOrderInfoActivity.mList.size()>0){
			params.addBodyParameter("bottle_data", getJsonData(NewOrderInfoActivity.mList));// 余额data串(余瓶)
		}
		params.addBodyParameter("yjdata", (String) SPUtils.get(PayMoneyActivity.this, "yjjson", ""));
		httpUtil.PostHttp(params, 0);
		Log.e("lll", params.getBodyParams().toString());
	}
	/**
	 * 重瓶空瓶串
	 * @param sList
	 * @return String
	 */
	private String getJsonStr(List<Weight> sList) {
		JSONArray array = new JSONArray();
		for (int i = 0; i < sList.size(); i++) {
			array.put(sList.get(i).getXinpian());
		}
		return array.toString();
	}
	/**
	 * 客户剩余的钢瓶Data(余额串)
	 * @param popupList
	 * @param number
	 * @return
	 */
	@SuppressLint("NewApi")
	private String getJsonData(List<Orderdeail> popupList) {
		JSONArray arr = new JSONArray();
		// 订单
//		Log.e("lllpopupList", popupList.toString());
//		Log.e("lllmDataWeight", NfcActivity.mDataWeight.toString());
		for (int j = 0; j < popupList.size(); j++) {
			JSONObject obj1 = new JSONObject();
			try {
				
				obj1.put("good_id", popupList.get(j).getGoods_id());
				obj1.put("good_name", popupList.get(j).getTitle());
				String good_kind = popupList.get(j).getGoods_kind();
				int num = Integer.parseInt(popupList.get(j).getNum());
				if(NfcActivity.mDataWeight != null){
				for (int k = 0; k < NfcActivity.mDataWeight.size(); k++) {
					Log.e("lllgood_kind", good_kind);
					Log.e("lll", NfcActivity.mDataWeight.get(k).getType());
					if (good_kind.equals(NfcActivity.mDataWeight.get(k)
							.getType())) {
						if (num != 0) {
							num--;
						}
					}
				}}
				obj1.put("good_kind", popupList.get(j).getNorm_id());
				obj1.put("good_num", num + "");
				obj1.put("good_price", popupList.get(j).getGoods_price());
				obj1.put("good_type",  popupList.get(j).getGoods_type());
				obj1.put("good_deposit", popupList.get(j).getGoods_price());
				// obj1.put("wb", weightBottle);
				
				arr.put(obj1);
				
				
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		for (int i = 0; i < arr.length(); i++) {
			
			try {
				JSONObject obj = arr.getJSONObject(i);
				if(obj.get("good_num").equals("0")){
					arr.remove(i);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
		return arr.toString();
	}
	/**
	 * 客户剩余的钢瓶Data(余额串)
	 * @param popupList
	 * @param number
	 * @return
	 */
	@SuppressLint("NewApi")
//	private String getYajinData(List<Weight> popupList) {
//		JSONArray arr = new JSONArray();
//		// 订单
////		Log.e("lllpopupList", popupList.toString());
////		Log.e("lllmDataWeight", NfcActivity.mDataWeight.toString());
//		for (int j = 0; j < popupList.size(); j++) {
//			JSONObject obj1 = new JSONObject();
//			try {
//				
//				obj1.put("good_id", popupList.get(j).getGoods_id());
//				obj1.put("good_name", popupList.get(j).getTitle());
//				String good_kind = popupList.get(j).getGoods_kind();
//				int num = Integer.parseInt(popupList.get(j).getNum());
//				for (int k = 0; k < NfcActivity.mDataWeight.size(); k++) {
//					Log.e("lllgood_kind", good_kind);
//					Log.e("lll", NfcActivity.mDataWeight.get(k).getType());
//					if (good_kind.equals(NfcActivity.mDataWeight.get(k)
//							.getType())) {
//						if (num != 0) {
//							num--;
//						}
//					}
//				}
//				obj1.put("good_kind", popupList.get(j).getNorm_id());
//				obj1.put("good_num", num + "");
//				obj1.put("good_price", popupList.get(j).getGoods_price());
//				obj1.put("good_type",  popupList.get(j).getGoods_type());
//				obj1.put("good_deposit", popupList.get(j).getGoods_price());
//				// obj1.put("wb", weightBottle);
//				
//				arr.put(obj1);
//				
//				
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//
//		}
//		for (int i = 0; i < arr.length(); i++) {
//			
//			try {
//				JSONObject obj = arr.getJSONObject(i);
//				if(obj.get("good_num").equals("0")){
//					arr.remove(i);
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			}
//		return arr.toString();
//	}
	/**
	 * 
	 * @param popupList
	 * @return
	 */
	private String getPeijianData(List<Orderdeail> popupList) {
		JSONArray arr = new JSONArray();
		// 订单
		Log.e("lllpopupList", popupList.toString());
//		Log.e("lllmDataWeight", NfcActivity.mDataWeight.toString());
		for (int j = 0; j < popupList.size(); j++) {
			JSONObject obj1 = new JSONObject();
			try {
//				if(popupList.get(j).getGoods_id()){
//					
//				}
				obj1.put("good_id", popupList.get(j).getGoods_id());// id
				obj1.put("good_name", popupList.get(j).getTitle());// 名称
				
				
				obj1.put("good_kind", popupList.get(j).getNorm_id());//种类
				obj1.put("good_num", popupList.get(j).getNum());// 数量
				
				// obj1.put("wb", weightBottle);
				
				arr.put(obj1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		return arr.toString();
	}

	@Override
	public void XuNo(int i) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(PayMoneyActivity.this,HomePageActivity.class);
		startActivity(intent);
	}

	@Override
	public void XuCallback(int i) {
		// TODO Auto-generated method stub
		Intent self = new Intent(PayMoneyActivity.this,SelfCheckActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("orderson", tv_number.getText().toString());
		bundle.putString("kid", (String) SPUtils.get(PayMoneyActivity.this, "kid", ""));
		bundle.putString("type", (String) SPUtils.get(PayMoneyActivity.this, "user_type", ""));
		if(SPUtils.get(PayMoneyActivity.this, "old_new", "").equals("2")){// 老用户
			bundle.putString("flag", (String) SPUtils.get(PayMoneyActivity.this, "safe_detials", ""));
		}
		
		self.putExtra("flag", bundle);
		startActivity(self);
	}
	

}
