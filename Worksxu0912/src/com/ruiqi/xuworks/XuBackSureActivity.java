package com.ruiqi.xuworks;

import java.io.Serializable;
import java.util.ArrayList;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ruiqi.bean.Type;
import com.ruiqi.bean.Weight;
import com.ruiqi.fragment.BackSureFragment;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.HomePageActivity;
import com.ruiqi.works.NfcActivity;
import com.ruiqi.works.R;

@SuppressLint("NewApi")
public class XuBackSureActivity extends BaseActivity implements ParserData {
	private TextView tv_yajin, tv_yuqi, tv_pay, tv_next, tv_weight,tv_gass,tv_total;
	private EditText et_yajia;
	private LinearLayout ll_toast, ll_yuqi, ll_gass;
	private CheckBox cb_gass;
	private List<Weight> list;
	private List<Type> list_price;
	private List<Type> select_list;
	private double pay;
	private double et = 0;
	String yuqiweight, yuqimoney,type;//type用来判断瓶的状态1空,2重,4故障
	Bundle bundle;
	boolean flag = false;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xuback_layout);
		initData();
		init();
	}

	private void initData() {
		// TODO Auto-generated method stub
		httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(XuBackSureActivity.this,
				SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.BOTTLE_TYPE);
		params.addBodyParameter(SPutilsKey.SHOP_ID, (String) SPUtils.get(
				XuBackSureActivity.this, SPutilsKey.SHOP_ID, ""));
		params.addBodyParameter("user_type",
				(String) SPUtils.get(XuBackSureActivity.this, "user_type", ""));
		Log.e("lll", params.getStringParams().toString());
		httpUtil.PostHttp(params, 1);
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("退瓶确认");
//		if(BackBottleOrderInfo.back_flag.equals("2")){
//			type ="1";
//		}else if(BackBottleOrderInfo.back_flag.equals("1")&&
//				BackBottleOrderInfo.bottle_flag.equals("1")){
//			type = "2";
//		}else if(BackBottleOrderInfo.back_flag.equals("1")&&
//				BackBottleOrderInfo.bottle_flag.equals("2")){
//			type = "4";
//		}
		
		
		list = new ArrayList<Weight>();
		cb_gass = (CheckBox) findViewById(R.id.imageView_xuback_gass);
//		cb_gass.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@SuppressLint("NewApi")
//			@SuppressWarnings("unused")
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//			if(isChecked){
//				ll_yuqi.setAlpha(0.5f);
//				ll_gass.setAlpha(1.0f);
//				flag = true;
//				getMoney(et);
//				Log.e("lll_check", "true");
//			}else {
//				ll_yuqi.setAlpha(1.0f);
//				ll_gass.setAlpha(0.5f);
//				flag = false;
//				getMoney(et);
//				Log.e("lll_check", "false");
//			}
//			}
//		});
		tv_total = (TextView) findViewById(R.id.textView_xuback_Toatal);
		tv_pay = (TextView) findViewById(R.id.textView_xuback_pay);
		tv_yuqi = (TextView) findViewById(R.id.textView_xuback_yuqi);
		tv_gass = (TextView) findViewById(R.id.textView_xuback_gassMoney);
		tv_weight = (TextView) findViewById(R.id.textView_xuback_yuqiweight);
		ll_gass = (LinearLayout) findViewById(R.id.ll_xuback_gassToast);
		ll_yuqi = (LinearLayout) findViewById(R.id.ll_xuback_yuqitoast);
		if(BackBottleOrderInfo.back_flag.equals("2")){
			yuqimoney = getIntent().getBundleExtra("bundle")
					.getString("yuqi_money");
			yuqiweight = getIntent().getBundleExtra("bundle").getString(
					"yuqi_weight");
			tv_yuqi.setText(yuqimoney);
			tv_weight.setText(yuqiweight);
		}else{
			ll_yuqi.setVisibility(View.GONE);
		}
		
		ll_gass.setAlpha(0.5f);
		et_yajia = (EditText) findViewById(R.id.textView_xuback_yajin);
		et_yajia.addTextChangedListener(watcher);
		tv_next = (TextView) findViewById(R.id.tv_xuback_next);
		tv_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if (tv_pay.equals("0.0")) {
//					Toast.makeText(XuBackSureActivity.this, "押金不能为空", 1).show();
//				} else {
					commintData();
//				}
			}

		});
		ll_toast = (LinearLayout) findViewById(R.id.ll_xuback_toast);
		BackSureFragment bsf = new BackSureFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("mData", (Serializable) NfcActivity.mDataBottle);
		bsf.setArguments(bundle);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.ll_xuback_toast, bsf).commit();
		getMoney(0);
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

	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		list_price = new ArrayList<Type>();
		select_list = new ArrayList<Type>();
		Log.e("lll", result);
		if (what == 0) {
			if (true) {

				try {
					JSONObject object = new JSONObject(result);
					int code = object.getInt("resultCode");
					String info = object.getString("resultInfo");
					if (code == 1) {
						Toast.makeText(XuBackSureActivity.this, info, 1).show();
						Intent intent = new Intent(XuBackSureActivity.this,
								HomePageActivity.class);
						startActivity(intent);
						if(NfcActivity.mDataBottle != null){
							NfcActivity.mDataBottle = null;
						}
						finish();
					} else {
						Toast.makeText(XuBackSureActivity.this, info, 1).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			try {
				JSONObject obj = new JSONObject(result);
				int resultCode = obj.getInt("resultCode");
				if (resultCode == 1) {

					JSONArray array = obj.getJSONArray("resultInfo");
					for (int j = 0; j < array.length(); j++) {
						JSONObject object = array.getJSONObject(j);
						String bottle_name = object.getString("typename");
						String price = object.getString("price");
					
						String yj_price = object.getString("yj_price");
						 list_price.add(new Type(price, 
						 bottle_name, yj_price));
					}

				}
				for (int i = 0; i < NfcActivity.mDataBottle.size(); i++) {
					String type_name = NfcActivity.mDataBottle.get(i).getType();
					for (int j = 0; j < list_price.size(); j++) {
						if(list_price.get(j).getWeight().equals(type_name)){
							String select_name = list_price.get(j).getWeight();
							String select_price = list_price.get(j).getPrice();
							String select_yj = list_price.get(j).getNum();
							select_list.add(new Type(select_price, select_name, select_yj));
						}
					}
				}
				double gass = 0;
				for (int i = 0; i < select_list.size(); i++) {
					 gass+= Double.parseDouble(select_list.get(i).getPrice());
				}
				tv_gass.setText(gass+"");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			Log.e("lll", "on");
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			Log.e("lll", "before");

		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			et = Double.parseDouble(et_yajia.getText().toString());
			getMoney(et);

			//
		}
	};

	public void getMoney(double et) {
//		if(flag == true){
//			pay = et + Double.parseDouble(tv_gass.getText().toString());
//		}else{
			pay = et + Double.parseDouble(tv_yuqi.getText().toString());
	
		
		tv_total.setText(pay + "");
	}

	/**
	 * 拼接json串
	 */
	private String getJsonStr(List<Weight> sList) {
		JSONArray array = new JSONArray();
		for (int i = 0; i < sList.size(); i++) {
			array.put(sList.get(i).getXinpian());
		}
		return array.toString();
	}

	private HttpUtil httpUtil;

	private void commintData() {
		// TODO Auto-generated method stub
		httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(XuBackSureActivity.this,
				SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.CONFIRM_DEPOSIT);
		params.addBodyParameter("token", token + "");
		params.addBodyParameter("depositsn",
				(String) SPUtils.get(XuBackSureActivity.this, "tpnumber", ""));// 退瓶订单号
		params.addBodyParameter("shop_Id", (String) SPUtils.get(
				XuBackSureActivity.this, SPutilsKey.SHOP_ID, ""));
		params.addBodyParameter("shipper_id",
				(String) SPUtils.get(XuBackSureActivity.this, "shipper_id", ""));
		params.addBodyParameter("shipper_mobile",
				(String) SPUtils.get(XuBackSureActivity.this, "SPutilsKey.MOBILLE", ""));
		params.addBodyParameter("shipper_name",
				(String) SPUtils.get(XuBackSureActivity.this, "shipper_name", ""));
		params.addBodyParameter("bottle", getJsonStr(NfcActivity.mDataBottle));
		params.addBodyParameter("kid",
				(String) SPUtils.get(XuBackSureActivity.this, "kid", ""));
		params.addBodyParameter("money", tv_total.getText().toString());
		if(!tv_weight.getText().equals("0")){// 判断是否为0,用以判断是否有余气
			 params.addBodyParameter("canye_money",tv_yuqi.getText().toString());
			 params.addBodyParameter("canye_weight", tv_weight.getText().toString());
		}
		
		Log.e("lll", params.getStringParams().toString());
		httpUtil.PostHttp(params, 0);

	}
	

}
