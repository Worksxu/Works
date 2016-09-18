package com.ruiqi.xuworks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.adapter.CommonAdapter;
import com.ruiqi.adapter.ResidueGassAdapter;
import com.ruiqi.adapter.ResidueGassLvAdapter;
import com.ruiqi.addreselector.MyListItem;
import com.ruiqi.bean.Type;
import com.ruiqi.bean.Weight;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.view.SwipeMenu;
import com.ruiqi.view.SwipeMenuCreator;
import com.ruiqi.view.SwipeMenuItem;
import com.ruiqi.view.SwipeMenuListView;
import com.ruiqi.view.SwipeMenuListView.OnMenuItemClickListener;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.HomePageActivity;
import com.ruiqi.works.NfcActivity;
import com.ruiqi.works.NullActivity;
import com.ruiqi.works.R;

public class ResidueGassActivity extends BaseActivity {
	private Spinner sp_number = null;
	private ResidueGassAdapter adapter;
	private EditText et_weight,et_price;
	private TextView tv_price, tv_money, tv_total, tv_commint, tv_sure;
	private SwipeMenuListView lv_toast;
	String number;
	private ResidueGassLvAdapter adapter1;
	private List<Weight> mData = new ArrayList<Weight>();
	int type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yuqi_layout);
		type = getIntent().getExtras().getInt("type");
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("余气折现");
		et_weight = (EditText) findViewById(R.id.edit_yuqi_weight);
		et_price = (EditText) findViewById(R.id.edit_yuqi_price);
		et_weight.addTextChangedListener(watcher);
		et_price.addTextChangedListener(watcher);
		lv_toast = (SwipeMenuListView) findViewById(R.id.lv_yuqi_Toast);
//		tv_price = (TextView) findViewById(R.id.textView_yuqi_price);
		tv_total = (TextView) findViewById(R.id.textView_yuqiToatal);
		tv_commint = (TextView) findViewById(R.id.textView_yuqi_commint);
		tv_sure = (TextView) findViewById(R.id.textView_yuqi_sure);
		tv_sure.setOnClickListener(this);
		tv_commint.setOnClickListener(this);
		tv_money = (TextView) findViewById(R.id.textView_yuqi_money);// 折现钱
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
		lv_toast.setMenuCreator(creator);
		sp_number = (Spinner) findViewById(R.id.spinner_null_number);

		// Log.e("lll", et_weight.getText().toString());
		sp_number.setPrompt("选择钢瓶号");
		sp_number.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) ResidueGassActivity.this
						.getSystemService(ResidueGassActivity.this.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return false;
			}
		});
		if (NfcActivity.mDataNull == null) {
			adapter = new ResidueGassAdapter(ResidueGassActivity.this,
					NfcActivity.mDataBottle);
		} else {
			adapter = new ResidueGassAdapter(ResidueGassActivity.this,
					NfcActivity.mDataNull);
		}

		sp_number.setAdapter(adapter);
		sp_number.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (NfcActivity.mDataNull == null) {
					number = NfcActivity.mDataBottle.get(position).getXinpian();
				} else {
					number = NfcActivity.mDataNull.get(position).getXinpian();
				}

				Log.e("lll", number);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.textView_yuqi_sure:
			Log.e("lll_sf", "queren");
			Weight wt = new Weight();
			if (TextUtils.isEmpty(et_weight.getText().toString())||TextUtils.isEmpty(et_weight.getText().toString())) {
				Toast.makeText(ResidueGassActivity.this, "请填写重量或气价", 1).show();
			} else {
				if (mData != null&&mData.size()>0) {
					
					
							wt.setXinpian(number);
							wt.setType(et_weight.getText().toString());
							wt.setType_name(tv_money.getText().toString());
							mData.add(wt);
							Log.e("lll_sf", "queren2");
							for (int i = 0; i < mData.size() - 1; i++) {
								Log.e("lll_fsaf", "进来");
								for (int j = mData.size() - 1; j > i; j--) {
									Log.e("lll_fsaf", "进来me ");
									if (mData.get(j).getXinpian().equals(mData.get(i).getXinpian())) {
										Log.e("lll_fsaf", "进来zheg ");
										Toast.makeText(ResidueGassActivity.this, "添加过了", 1).show();
										mData.remove(j);

									}
								}
						
					}
				} else {
					Log.e("lll_sf", "queren3");
					wt.setXinpian(number);
					wt.setType(et_weight.getText().toString());
					wt.setType_name(tv_money.getText().toString());
					mData.add(wt);
				}
				adapter1 = new ResidueGassLvAdapter(ResidueGassActivity.this,
						mData);
				lv_toast.setAdapter(adapter1);
				getMOney();
				lv_toast.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public void onMenuItemClick(int position, SwipeMenu menu,
							int index) {
						// TODO Auto-generated method stub
						switch (index) {
						case 0:
							mData.remove(position);
							adapter1.notifyDataSetChanged();
							getMOney();
							break;

						default:
							break;
						}
					}
				});
			}
			break;
		case R.id.textView_yuqi_commint:
			SPUtils.put(ResidueGassActivity.this, "null_data",
					getJsonData(mData));
			if(type == 5){// 余气
				Intent intent = new Intent(ResidueGassActivity.this,PayMoneyActivity.class);
				NullActivity.null_bottle = "1";//代表有余气
				SPUtils.put(ResidueGassActivity.this, "yuqi", tv_total.getText().toString());
				startActivity(intent);
			
				
			}else if(type == 6) {//退瓶
				Intent xu = new Intent(ResidueGassActivity.this,XuBackSureActivity.class);
				Bundle  bundle = new Bundle();
//				bundle.putSerializable("select_list", (Serializable) mData);
				bundle.putString("yuqi_money", tv_total.getText().toString());
				bundle.putString("yuqi_weight", getWeight()+"");
				xu.putExtra("bundle", bundle);
				startActivity(xu);
				finish();
			}else {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("yuqi_list", (Serializable) mData);
				bundle.putString("yuqi_money", tv_total.getText().toString());
				bundle.putString("yuqi_weight", getWeight()+"");
				intent.putExtra("bundle", bundle);
				setResult(9, intent);
				finish();
			}
//			else {// 置换
//				Log.e("lll_ss", type+"");
//				Intent home = new Intent(ResidueGassActivity.this,BackGassOrderActivity.class);
//				startActivity(home);
//				finish();
//			}
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
			if(!TextUtils.isEmpty(et_price.getText().toString())&&!TextUtils.isEmpty(et_weight.getText().toString())){
				
					tv_money.setText(Double.parseDouble(et_weight.getText()
							.toString().trim())
							* Double.parseDouble(et_price.getText().toString().trim())
							+ "");
				
			}
//			else{
//				Toast.makeText(ResidueGassActivity.this, "请输入上次气价", 1).show();
//			}
			

			//
		}
	};

	// 滑动删除用的类
	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	/**
	 * 获得钱数
	 */
	void getMOney() {
		double total = 0;
		if (mData.size() != 0) {
			for (int i = 0; i < mData.size(); i++) {
				total += Double.parseDouble(mData.get(i).getType_name());
				tv_total.setText(total + "");
			}
		}
	}
	// 获得重量
  public  double getWeight(){
	  double weight = 0;
	  if (mData.size() != 0) {
			for (int i = 0; i < mData.size(); i++) {
				weight += Double.parseDouble(mData.get(i).getType());
				
			}
			}
	return weight;
  }
	/**
	 * 拼接字符串
	 * 
	 * @param popupList
	 * @return String
	 */
	private String getJsonData(List<Weight> popupList) {
		JSONArray arr = new JSONArray();
		// 订单
		for (int j = 0; j < popupList.size(); j++) {
			JSONObject obj1 = new JSONObject();
			try {
				obj1.put("xinpian", popupList.get(j).getXinpian());
				obj1.put("weight", popupList.get(j).getType());
				obj1.put("price", popupList.get(j).getType_name());
				// obj1.put("wb", weightBottle);
				arr.put(obj1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return arr.toString();
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case 8://dingdan
			type = 8;
			Log.e("lll_ss", type+"");
			break;
		case 5:// zhexian
			type = 5;
			Log.e("lll_ss", type+"");
			break;

		default:
			break;
		}
	}

}
