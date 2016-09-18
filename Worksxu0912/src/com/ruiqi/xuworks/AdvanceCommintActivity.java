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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.adapter.ZheJiuLvAdapter;
import com.ruiqi.bean.Orderdeail;
import com.ruiqi.bean.Type;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.XuDialog;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.XuDialog.No;
import com.ruiqi.utils.XuDialog.Yes;
import com.ruiqi.view.CustomDownView;
import com.ruiqi.view.CustomListView;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.CommintOrder;
import com.ruiqi.works.HomePageActivity;
import com.ruiqi.works.R;
import com.ruiqi.works.SelectYouhuiActivity;
import com.ruiqi.works.WorksYouhuiActivity;

public class AdvanceCommintActivity extends BaseActivity implements ParserData,
		Yes, No {
	private CustomListView lv_toast;
	private ZheJiuLvAdapter adapter;
	private List<Type> mData;
	double youhui = 0;
	double total = 0;
	double pay = 0;
	double work_youhui = 0;
	double use_youhui = 0;
	double peijian_money = 0;
	private TextView tv_sure, tv_goodsmoney, tv_userYouhui, tv_workerYouhui,tv_manjian,
			tv_pay, tv_toast;
	private CustomDownView cdv_youhui;
	private EditText et_num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advance_commint_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("预支付订单结算");
		youhui = getIntent().getExtras().getDouble("youhui");// 活动优惠
		use_youhui = getIntent().getExtras().getDouble("deduction");// 用户优惠
		total = getIntent().getExtras().getDouble("goods");
		Log.e("llll_adsadsad", total+"");
		lv_toast = (CustomListView) findViewById(R.id.listView_commintTost);
		tv_sure = (TextView) findViewById(R.id.textView_commint_order);
		tv_manjian = (TextView) findViewById(R.id.textView_manjianMoney);
		tv_manjian.setText(youhui+"");
		tv_sure.setOnClickListener(this);
		tv_goodsmoney = (TextView) findViewById(R.id.textView_goodsMoney);
		tv_userYouhui = (TextView) findViewById(R.id.textView_deductionMoney);
		tv_workerYouhui = (TextView) findViewById(R.id.textView_youhuiMoney);
		tv_pay = (TextView) findViewById(R.id.textView_commint_Toast);
		tv_toast = (TextView) findViewById(R.id.textView_ToastYouhui);
		tv_goodsmoney.setText(total + "");
		tv_userYouhui.setText(use_youhui + "");
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
					if(work_youhui*Integer.parseInt(et_num.getText().toString().trim())>(total - youhui -use_youhui)){
						Toast.makeText(AdvanceCommintActivity.this, "优惠已大于商品金额", 1).show();
						et_num.setText("1");
						
					}else{
					getMoney(work_youhui*Integer.parseInt(et_num.getText().toString().trim()));
					tv_workerYouhui.setText(work_youhui*Integer.parseInt(et_num.getText().toString().trim())+"");}
				}
				
				
			}
		});
		cdv_youhui = (CustomDownView) findViewById(R.id.commint_youhui);
		cdv_youhui.setOnClickListener(this);
		cdv_youhui.setString("送气工优惠");
		cdv_youhui.setView(View.VISIBLE);
		getMoney(0);
		getData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.commint_youhui:
			if(tv_pay.getText().toString().equals("0")){
				Toast.makeText(AdvanceCommintActivity.this, "优惠不可用", 1).show();
			}else{
			Intent intent = new Intent(AdvanceCommintActivity.this,
					WorksYouhuiActivity.class);
			startActivityForResult(intent, 1);
			}
			break;
		case R.id.textView_commint_order:
//			XuDialog.getInstance().setno(this);
//			XuDialog.getInstance().setyes(this);
//			XuDialog.getInstance().show(AdvanceCommintActivity.this, "选择支付类型",
//					"现金支付", "网上支付", 1);
			commint("0");
			break;

		default:
			break;
		}
	}

	private void getData() {
		// TODO Auto-generated method stub
		if (NewOrderInfoActivity.list != null
				&& NewOrderInfoActivity.list.size() > 0) {
			Log.e("LLLL_shulkiang", NewOrderInfoActivity.list.size()+"");
			mData = new ArrayList<Type>();
			
			for (int i = 0; i < NewOrderInfoActivity.list.size(); i++) {
				Type type = new Type();
				type.setBottle_name(NewOrderInfoActivity.list.get(i)
						.getTitle());
				type.setName(NewOrderInfoActivity.list.get(i).getGoods_kind());
				type.setNum(NewOrderInfoActivity.list.get(i).getGoods_price());
				type.setPrice(NewOrderInfoActivity.list.get(i).getNum());
				Log.e("LLLL_sce", NewOrderInfoActivity.list.get(i).getGoods_kind());
				mData.add(type);
			}
			adapter = new ZheJiuLvAdapter(AdvanceCommintActivity.this, mData);
			lv_toast.setAdapter(adapter);
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

	void getMoney(double work) {
		pay = total - youhui -use_youhui- work;
		if(pay<0){
			tv_pay.setText("0");
		}else{
		tv_pay.setText(pay + "");
		}
	}

	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("lllllllllll_fsafas", result);
		try {
			JSONObject object = new JSONObject(result);
			int code = object.getInt("resultCode");
			String info = object.getString("resultInfo");
			if(code == 1){
				Toast.makeText(AdvanceCommintActivity.this, info, 1).show();
				Intent home = new Intent(AdvanceCommintActivity.this,HomePageActivity.class);
				startActivity(home);
				finish();
			}else{
				Toast.makeText(AdvanceCommintActivity.this, info, 1).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
	
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg1 == 1) {
			Bundle bundle = new Bundle();
			bundle = arg2.getBundleExtra("youhui");
			work_youhui = bundle.getDouble("youhui_money");
			if(!TextUtils.isEmpty(et_num.getText().toString().trim())){
				
			
			
			if(work_youhui*Integer.parseInt(et_num.getText().toString().trim())<=(total - youhui -use_youhui)){
				tv_toast.setVisibility(View.VISIBLE);
				tv_toast.setText(bundle.getString("youhui_name") + " "
						+ work_youhui+ "");
				tv_toast.setText(bundle.getString("youhui_name") + " "
						+ work_youhui*Integer.parseInt(et_num.getText().toString().trim()) + "");
				tv_workerYouhui.setText(bundle.getString("youhui_name") + " "
						+ work_youhui*Integer.parseInt(et_num.getText().toString().trim()) + "");
				getMoney(work_youhui*Integer.parseInt(et_num.getText().toString().trim()));
			}else{
				Toast.makeText(AdvanceCommintActivity.this, "此优惠券不可用", 1).show();
				work_youhui = 0;
				tv_toast.setText(bundle.getString("youhui_name") + " "
						+ work_youhui*Integer.parseInt(et_num.getText().toString().trim()) + "");
				tv_workerYouhui.setText(bundle.getString("youhui_name") + " "
						+ work_youhui*Integer.parseInt(et_num.getText().toString().trim()) + "");
				getMoney(work_youhui*Integer.parseInt(et_num.getText().toString().trim()));
			}
			
			}else{
				Toast.makeText(AdvanceCommintActivity.this, "请填写数量", 1).show();
			}
		}
	}

	@Override
	public void XuNo(int i) {
		// TODO Auto-generated method stub
		commint("1");
	}

	@Override
	public void XuCallback(int i) {
		// TODO Auto-generated method stub
		commint("0");
	}

	private HttpUtil httpUtil;

	private void commint(String is_cash) {

		// TODO Auto-generated method stub
		httpUtil = new HttpUtil();
		httpUtil.setParserData((ParserData) this);
		RequestParams params = new RequestParams(RequestUrl.ADVANCE);
		params.addBodyParameter("ordersn", (String) SPUtils.get(AdvanceCommintActivity.this, "goodsNumber", ""));
		params.addBodyParameter("kid",(String) SPUtils.get(AdvanceCommintActivity.this, "kid", ""));
		params.addBodyParameter("pay_money", tv_pay.getText().toString());
		params.addBodyParameter("is_cash", "0");// 0代表现金
		params.addBodyParameter("comment", "0");
		params.addBodyParameter("shipper_money", work_youhui+"");
		params.addBodyParameter("bottle_data",
				getJsonData(NewOrderInfoActivity.mList));
		params.addBodyParameter("peijian",
				getJsonData(NewOrderInfoActivity.peijianList));
		
		Log.e("lll_预支付", params.getStringParams().toString());
		httpUtil.PostHttp(params, 1);
	}

	private String getJsonData(List<Orderdeail> popupList) {
		JSONArray arr = new JSONArray();
		// 订单
		// Log.e("lllpopupList", popupList.toString());
		// Log.e("lllmDataWeight", NfcActivity.mDataWeight.toString());
		for (int j = 0; j < popupList.size(); j++) {
			JSONObject obj1 = new JSONObject();
			try {

				obj1.put("good_id", popupList.get(j).getGoods_id());
				obj1.put("good_name", popupList.get(j).getTitle());

				obj1.put("good_kind", popupList.get(j).getNorm_id());
				obj1.put("good_num", popupList.get(j).getNum());
				obj1.put("good_price", popupList.get(j).getGoods_price());
				obj1.put("good_type", popupList.get(j).getGoods_type());
				obj1.put("good_deposit", popupList.get(j).getGoods_price());
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

}
