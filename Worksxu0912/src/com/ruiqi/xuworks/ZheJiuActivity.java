package com.ruiqi.xuworks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.a.a.ad;

import com.ruiqi.adapter.ResidueGassAdapter;
import com.ruiqi.adapter.ResidueGassLvAdapter;
import com.ruiqi.adapter.TypePopupAdapter;
import com.ruiqi.adapter.ZheJiuLvAdapter;
import com.ruiqi.bean.Type;
import com.ruiqi.bean.Weight;
import com.ruiqi.bean.ZheiJiu;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.view.SwipeMenu;
import com.ruiqi.view.SwipeMenuCreator;
import com.ruiqi.view.SwipeMenuItem;
import com.ruiqi.view.SwipeMenuListView;
import com.ruiqi.view.SwipeMenuListView.OnMenuItemClickListener;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.HomePageActivity;
import com.ruiqi.works.R;

public class ZheJiuActivity extends BaseActivity implements ParserData {
	private ArrayList<Weight> yearTypeList;// 年份选择的list
	private ArrayList<Weight> weightTypeList;// 瓶规格的list
	private ArrayList<Type> mTypeList;// 网络上获取的全部
	private ZheJiuLvAdapter adapter1;// 选择后生成的list的适配器
	private ResidueGassAdapter adapter;// 下拉框适配器
	private List<Type> mData = new ArrayList<Type>();// 选择后生成的list
	double zhejiu_money, canye_price, total;
	private Spinner sp_weight, sp_date;
	private ImageView img_soup, img_add;
	private TextView tv_canyeMoney, tv_totalMoney, tv_commint, tv_sure, tv_num;
	private EditText et_weight;
	private SwipeMenuListView lv_zhejiu;
	String weight, year, select_price, weight_name, year_name;
	int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_zhejiu_layout);
		type = getIntent().getExtras().getInt("type");
		Log.e("llll_type", type+"");
		init();
	}

	private void init() {
		initData();
		setTitle("折旧瓶");
		lv_zhejiu = (SwipeMenuListView) findViewById(R.id.lv_yuqi_Toast);
		img_soup = (ImageView) findViewById(R.id.iv_olderSup);
		img_add = (ImageView) findViewById(R.id.iv_olderAdd);
		img_add.setOnClickListener(this);
		img_soup.setOnClickListener(this);
		tv_num = (TextView) findViewById(R.id.tv_zhejiu_num);
		tv_sure = (TextView) findViewById(R.id.textView_yuqi_sure);
		tv_sure.setOnClickListener(this);
		tv_commint = (TextView) findViewById(R.id.textView_yuqi_commint);
		tv_commint.setOnClickListener(this);
		tv_canyeMoney = (TextView) findViewById(R.id.textView_zhejiu_canyemoney);// 残液钱
		tv_totalMoney = (TextView) findViewById(R.id.textView_yuqiToatal);// 总钱数
		et_weight = (EditText) findViewById(R.id.editText_zhejiu_weight);
		et_weight.addTextChangedListener(watcher);
		sp_weight = (Spinner) findViewById(R.id.spinner_zhejiu_type);
		sp_date = (Spinner) findViewById(R.id.spinner_zhejiu_date);
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
		lv_zhejiu.setMenuCreator(creator);
		sp_weight.setPrompt("选择钢瓶规格");
		sp_weight.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) ZheJiuActivity.this
						.getSystemService(ZheJiuActivity.this.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return false;
			}
		});

		sp_weight.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				weight = weightTypeList.get(position).getType();// 瓶的id
				weight_name = weightTypeList.get(position).getXinpian();
				Log.e("llll_zhejiu", weight_name);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		sp_date.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				year = yearTypeList.get(position).getType();// 年份id
				year_name = yearTypeList.get(position).getXinpian();// 年份
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	}

	int num = 0;

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.textView_yuqi_commint:
			if(mData.size() == 0){
				Toast.makeText(ZheJiuActivity.this, "请选择钢瓶", 1).show();
			}else{
			sure();}
			break;
		case R.id.textView_yuqi_sure:
			select_price = "0";
			for (int i = 0; i < mTypeList.size(); i++) {
				if (weight.equals(mTypeList.get(i).getNum())
						&& year.equals(mTypeList.get(i).getNorm_id())) {
					select_price = mTypeList.get(i).getPrice();// 价钱

				}
			}
			if (select_price.equals("0")) {
				Toast.makeText(ZheJiuActivity.this, "没有这个规格", 1).show();
			} else if (tv_num.getText().toString().equals("0")) {
				Toast.makeText(ZheJiuActivity.this, "数量不能为0", 1).show();
			} else {
				getSelect();
			}
			break;
		case R.id.iv_olderSup:
			if (num == 0) {
				Toast.makeText(ZheJiuActivity.this, "数量已达到最低", 1).show();
			} else {
				num--;
				tv_num.setText(num + "");
			}

			break;
		case R.id.iv_olderAdd:
			num++;
			tv_num.setText(num + "");
			break;

		default:
			break;
		}
	}

	private void getSelect() {
		// TODO Auto-generated method stub

		Type type = new Type();
		if (mData != null && mData.size() > 0) {
			type.setBottle_name(weight_name);
			type.setWeight(weight);// 瓶id
			type.setNorm_id(year);// 年份id
			type.setName(year_name);// 年份
			type.setPrice(select_price);// 金额
			type.setNum(tv_num.getText().toString());// 数量
			mData.add(type);
			for (int i = 0; i < mData.size() - 1; i++) {
				Log.e("lll_fsaf", "进来");
				for (int j = mData.size() - 1; j > i; j--) {
					Log.e("lll_fsaf", "进来me ");
					if (mData.get(j).getName().equals(mData.get(i).getName())
							&& mData.get(j).getBottle_name()
									.equals(mData.get(i).getBottle_name())) {
						Log.e("lll_fsaf", "进来zheg ");
						Toast.makeText(ZheJiuActivity.this, "添加过了", 1).show();
						mData.remove(j);

					}
				}
			}
			adapter1 = new ZheJiuLvAdapter(ZheJiuActivity.this, mData);
//			lv_zhejiu.setAdapter(adapter1);
//			getMoney();
			

		} else {
			Log.e("lll_sf", "queren3");
			type.setBottle_name(weight_name);
			type.setWeight(weight);// 瓶id
			type.setNorm_id(year);// 年份id
			type.setName(year_name);// 年份
			type.setPrice(select_price);// 金额
			type.setNum(tv_num.getText().toString());// 数量
			mData.add(type);
			adapter1 = new ZheJiuLvAdapter(ZheJiuActivity.this, mData);
			
		}
		lv_zhejiu.setAdapter(adapter1);
		getMoney();
		lv_zhejiu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public void onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				// TODO Auto-generated method stub
				switch (index) {
				case 0:
					mData.remove(position);
					adapter1.notifyDataSetChanged();
					getMoney();
					break;

				default:
					break;
				}
			}
		});

	}

	/**
	 * 统计总额
	 */
	public double getMoney() {
		if (mData != null && mData.size() > 0) {
			zhejiu_money = 0;
			total = 0;
			for (int i = 0; i < mData.size(); i++) {
				zhejiu_money += Double.parseDouble(mData.get(i).getPrice())
						* Integer.parseInt(mData.get(i).getNum());// 瓶的钱
			}
		} else {
			zhejiu_money = 0;
		}
		total = zhejiu_money
				+ Double.parseDouble(tv_canyeMoney.getText().toString().trim());
		tv_totalMoney.setText(total + "");
		return total;
	}

	private void sure() {
		// TODO Auto-generated method stub

		 zhejiu_money = 0;
		 for (int i = 0; i < mData.size(); i++) {
		 zhejiu_money += Double.parseDouble(mData.get(i).getPrice())
		 * Integer.parseInt(mData.get(i).getNum());// 瓶的钱
		 }
		String zheijiu_ping = getJsonDataZheiJiu(mData);
		SPUtils.put(ZheJiuActivity.this, "zheijiu_ping", zheijiu_ping);
		if (type == 3) {// 折现来的
			Log.e("llll_tast", type+"");
			Intent intent = new Intent(ZheJiuActivity.this,PayMoneyActivity.class);
			intent.putExtra("zhejiu_money", zhejiu_money);// 折旧的钱
			intent.putExtra("canye_money", tv_canyeMoney.getText().toString());//残液的钱
			if(!TextUtils.isEmpty(et_weight.getText().toString().trim())){
				
				SPUtils.put(ZheJiuActivity.this, "canye_weight", et_weight.getText().toString());//重
			}else{
				SPUtils.put(ZheJiuActivity.this, "canye_weight", "0");//重
			}
			SPUtils.put(ZheJiuActivity.this, "zhejiu", zhejiu_money);
			SPUtils.put(ZheJiuActivity.this, "canye_money", tv_canyeMoney.getText().toString());// 钱
			
//			setResult(3, intent);
//			finish();
			startActivity(intent);
			Log.e("llll", "重瓶来的");
		} else if(type == 1) {
			Log.e("llll", "订单来的");
			commintData();
		}

	}

	private HttpUtil httpUtil;

	// 获取折旧瓶列表
	private void initData() {
		// TODO Auto-generated method stub
		httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(ZheJiuActivity.this,
				SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.ZHEJIU);
		params.addBodyParameter("token ", token + "");
		httpUtil.PostHttp(params, 0);
	}

	/**
	 * 提交订单 参数:shipper_id shipper_name shipper_mobile kid //客户id mobile //客户联系方式
	 * sheng shi qu cun address bottle_data //备注data串 money //当前金额 comment //备注
	 */
	private void commintData() {
		// TODO Auto-generated method stub
		httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(ZheJiuActivity.this,
				SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.ZHEJIUORDER);
		String shop_id = (String) SPUtils
				.get(ZheJiuActivity.this, SPutilsKey.SHOP_ID, "error");
		params.addBodyParameter("token", token + "");
		params.addBodyParameter("shipper_id", (String) SPUtils.get(
				ZheJiuActivity.this, SPutilsKey.SHIP_ID, "error"));
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("shipper_name", (String) SPUtils.get(
				ZheJiuActivity.this, "shipper_name", "error"));
		params.addBodyParameter("shipper_mobile", (String) SPUtils.get(
				ZheJiuActivity.this, SPutilsKey.MOBILLE, "error"));
		params.addBodyParameter("kid",
				(String) SPUtils.get(ZheJiuActivity.this, "kid", "error"));
		params.addBodyParameter("mobile", (String) SPUtils.get(
				ZheJiuActivity.this, "oldUserMobile", "error"));
		params.addBodyParameter("sheng",
				(String) SPUtils.get(ZheJiuActivity.this, "sheng", "error"));
		params.addBodyParameter("shi",
				(String) SPUtils.get(ZheJiuActivity.this, "shi", "error"));
		params.addBodyParameter("qu",
				(String) SPUtils.get(ZheJiuActivity.this, "qu", "error"));
		params.addBodyParameter("cun",
				(String) SPUtils.get(ZheJiuActivity.this, "cun", "error"));
		params.addBodyParameter("address", (String) SPUtils.get(
				ZheJiuActivity.this, "oldUserAddress", "error"));
		params.addBodyParameter("zhejiu_data", getJsonDataZheiJiu(mData));
		params.addBodyParameter("money", getMoney() + "");// 待改
		if(!TextUtils.isEmpty(et_weight.getText().toString().trim())){
			params.addBodyParameter("weight", et_weight.getText().toString());//
		}
		 
		params.addBodyParameter("comment", "");// 备注
		Log.e("lll_commintparams", params.getStringParams().toString());
		httpUtil.PostHttp(params, 1);
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
	 * { "id": "1", "bottle_name": "5KG", "bottle_type": "2", "year": "1",
	 * "price": "2", "time_created": "1471234022", "year_name": "15年后" },
	 */
	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		if (what == 0) {

			mTypeList = new ArrayList<Type>();
			yearTypeList = new ArrayList<Weight>();
			weightTypeList = new ArrayList<Weight>();
			Log.e("lll", result);
			try {
				JSONObject object = new JSONObject(result);
				int code = object.getInt("resultCode");
				if (code == 1) {
					JSONObject obj = object.getJSONObject("resultInfo");
					JSONArray array = obj.getJSONArray("raffinate");
					canye_price = obj.getDouble("price");
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj1 = array.getJSONObject(i);
						String price = obj1.getString("price");// 折旧瓶价格
						String bottle_name = obj1.getString("bottle_name");// 瓶的名称
						String bottle_type = obj1.getString("bottle_type");// 瓶的id
						String year_name = obj1.getString("year_name");// 年份名
						String year = obj1.getString("year");// 年份id
						mTypeList.add(new Type(price, bottle_name, bottle_type,
								year_name, year, "0", "0"));
						yearTypeList.add(new Weight(year_name, year, "0"));
						weightTypeList.add(new Weight(bottle_name, bottle_type,
								"0"));
						Log.e("lll_size", weightTypeList.size() + "");
					}
					if (removeDuplicate(weightTypeList).size() > 0
							&& removeDuplicate(weightTypeList) != null) {
						Log.e("lll_size", removeDuplicate(weightTypeList)
								.size() + "");
						adapter = new ResidueGassAdapter(ZheJiuActivity.this,
								removeDuplicate(weightTypeList));
						sp_weight.setAdapter(adapter);
					}
					if (removeDuplicate(yearTypeList).size() > 0
							&& removeDuplicate(yearTypeList) != null) {
						adapter = new ResidueGassAdapter(ZheJiuActivity.this,
								removeDuplicate(yearTypeList));
						sp_date.setAdapter(adapter);
					}

				}
			} catch (JSONException e) {

				e.printStackTrace();
			}
		} else {
			Log.e("lll_commint", result);
			try {
				JSONObject object = new JSONObject(result);
				int code = object.getInt("resultCode");
				String info = object.getString("resultInfo");
				if(code == 1){
					Toast.makeText(ZheJiuActivity.this, info, 1).show();
					Intent home = new Intent(ZheJiuActivity.this,HomePageActivity.class);
					startActivity(home);
					finish();
				}else{
					Toast.makeText(ZheJiuActivity.this, info, 1).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// 组成折旧瓶data串{type_id,type_name,year,money,weight,price}

	private String getJsonDataZheiJiu(List<Type> popupList) {
		JSONArray arr = new JSONArray();
		// 订单
		for (int j = 0; j < popupList.size(); j++) {
			JSONObject obj1 = new JSONObject();
			try {
				obj1.put("good_id", popupList.get(j).getWeight());// 规格id
				obj1.put("good_name", popupList.get(j).getBottle_name());// 规格
				obj1.put("good_num", popupList.get(j).getNum());// 数量
				obj1.put("year", popupList.get(j).getNorm_id());// 年限id
				obj1.put("money", popupList.get(j).getPrice());// 瓶钱
				// obj1.put("weight", popupList.get(j).getId());//残液重
				// obj1.put("price", popupList.get(j).getId());// 残液钱
				// obj1.put("wb", weightBottle);
				arr.put(obj1);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return arr.toString();
	}

	
	

	// 去重
	public ArrayList removeDuplicate(List<Weight> list) {
		Log.e("lll_sizeo", list.size() + "");
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).getXinpian().equals(list.get(i).getXinpian())) {
					list.remove(j);
				}
			}
		}
		System.out.println(list);
		return (ArrayList) list;
	}

	private TextWatcher watcher = new TextWatcher() {
		//
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
			if (!TextUtils.isEmpty(s.toString())) {
				tv_canyeMoney.setText(Double.parseDouble(s.toString())
						* canye_price + "");
				getMoney();
			}else{
				tv_canyeMoney.setText("0");
			}
		}
	};
	// 滑动删除用的类
		private int dp2px(int dp) {
			return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
					getResources().getDisplayMetrics());
		}

}
