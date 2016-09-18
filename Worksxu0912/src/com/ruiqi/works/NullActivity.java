package com.ruiqi.works;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.adapter.TypePopupAdapter;
import com.ruiqi.bean.Orderdeail;
import com.ruiqi.bean.Type;
import com.ruiqi.bean.Weight;
import com.ruiqi.db.GpDao;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.InPutIsBottle;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.XuDialog;
import com.ruiqi.utils.XuDialog.No;
import com.ruiqi.utils.XuDialog.Yes;
import com.ruiqi.view.popupwindow.SelectOrderInfoPopupWindow;
import com.ruiqi.xuworks.BackGassOrderActivity;
import com.ruiqi.xuworks.CashAndDiscountActivity;
import com.ruiqi.xuworks.NewZhejiuActivity;
import com.ruiqi.xuworks.PayMoneyActivity;
import com.ruiqi.xuworks.ResidueGassActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

//扫空瓶界面
public class NullActivity extends SaoMiaoActivity implements Yes, No {

	public static List<Weight> list;
	private List<Type> numList;
	public static String null_bottle = "2";//判断空瓶是否有余气
	private List<Type> weightList;
	HashMap<String, Integer> info;
	private ArrayList<Type> mTypeList;
	
	double total_money = 0;
	int tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!TextUtils.isEmpty(DownOrderActivity.changebottle)
				&& DownOrderActivity.changebottle.equals("1")) {
			setTitle("扫描故障瓶");
		} else {
			setTitle("扫描空瓶");
		}
	}

	@Override
	public int initFlag() {
		return 2;
	}

	@Override
	protected void onResume() {
		super.onResume();
		SPUtils.put(NullActivity.this, "UI", "null");
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = null;
		judgetype();
		switch (v.getId()) {
		
		case R.id.next: // 跳转到安全报告界面或者残气，折旧
			if (!TextUtils.isEmpty(DownOrderActivity.changebottle)
					&& DownOrderActivity.changebottle.equals("1")) {
				if(tag==1){
					Toast.makeText(NullActivity.this, "不能为同一个瓶",
							Toast.LENGTH_SHORT).show();
				}else{
				Intent change = new Intent(NullActivity.this,
						BackGassOrderActivity.class);
				startActivity(change);
				}
			} else {
				SPUtils.put(NullActivity.this, "deposit", "0");
				SPUtils.put(NullActivity.this, "ping_total", "0");
				SPUtils.put(NullActivity.this, "canye", "0");
				if (NfcActivity.mData != null) {
					NfcActivity.mData = null;
				}
				SPUtils.remove(NullActivity.this, "UI");
				if (NfcActivity.mDataNull == null) {
					NfcActivity.mDataNull = new ArrayList<Weight>();
					// getNum(NfcActivity.mDataNull);

				}

				/*
				 * if(SPutilsKey.type==1){ intent = new
				 * Intent(NullActivity.this, SelfActivity.class); }else
				 * if(SPutilsKey.type==2){
				 */
				
				if (tag == 1) {
					Toast.makeText(NullActivity.this, "不能为同一个瓶",
							Toast.LENGTH_SHORT).show();
				} else {
					total_money = 0;
					changeNum();
					if(tag == 1){
						Toast.makeText(NullActivity.this, "非法操作",
								Toast.LENGTH_SHORT).show();
					}else{
						initType();//获取押金
					}
					
					
				}
			}
			// }
			break;

		case R.id.tv_input:
			if (NfcActivity.mDataNull == null) {
				NfcActivity.mDataNull = new ArrayList<Weight>();
			}
			str = et_input.getText().toString();
			new InPutIsBottle(str, NullActivity.this, mData, adapter, pd,
					GpDao.getInstances(NullActivity.this),NfcActivity.mDataNull,3).addDataToList();
			/*
			 * new InPutIsBottle(str, WeightActivity.this, mData, adapter, pd,
					GpDao.getInstances(WeightActivity.this),
					NfcActivity.mDataWeight, 1).addDataToList();
			 */
			break;

		default:
			break;
		}
		if (intent != null) {
			startActivity(intent);
		}
	}

	@Override
	public Handler[] initHandler() {
		return null;
	}

	@Override
	public void jumpPage() {
		super.jumpPage();
		if (NfcActivity.mData != null) {
			NfcActivity.mData = null;
		}
		if (NfcActivity.mDataNull != null) {
			NfcActivity.mDataNull = null;
		}
		SPUtils.remove(NullActivity.this, "UI");
		Intent intent = new Intent(NullActivity.this, WeightActivity.class);
		intent.putExtra("show", getIntent().getStringExtra("show"));
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (NfcActivity.mData != null) {
				NfcActivity.mData = null;
			}
			if (NfcActivity.mDataNull != null) {
				NfcActivity.mDataNull = null;
			}
			SPUtils.remove(NullActivity.this, "UI");
			Intent intent = new Intent(NullActivity.this, WeightActivity.class);
			intent.putExtra("show", getIntent().getStringExtra("show"));
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	private List<Weight> mData;

	@Override
	public List<Weight> initList() {
		mData = new ArrayList<Weight>();
		if (NfcActivity.mDataNull != null) {
			for (int i = 0; i < NfcActivity.mDataNull.size(); i++) {
				Weight w = NfcActivity.mDataNull.get(i);

				mData.add(new Weight(w.getXinpian(), w.getType(), w.getStatus()));
				Log.e("lll_null", w.getType());
			}
		}
		return mData;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		CurrencyUtils.onLongClickDelete(position, NullActivity.this, adapter,
				mData, NfcActivity.mDataNull, NfcActivity.mData);
		return false;
	}

	public List<Type> getNum(List<Weight> mlist) {
		numList = new ArrayList<Type>();
		// nullList = new ArrayList<Type>();
		// weightList = new ArrayList<Type>();
		info = new HashMap<String, Integer>();

		for (int i = 0; i < mlist.size(); i++) {
			int num = 1;
			if (info.containsKey(mlist.get(i).getYj())) {
				info.put(mlist.get(i).getYj(), info.get(mlist.get(i).getYj())+1);
			} else {
				info.put(mlist.get(i).getYj(), num);
			}
		}
		for (Map.Entry<String, Integer> entry : info.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			Type type = new Type();
			type.setName(key);
			type.setNum(value + "");
			numList.add(type);
			
		}
		for (int i = 0; i < numList.size(); i++) {
			String nn = numList.get(i).getNum();
			Log.e("safasfas_kong", numList.get(i).getName());
		}
		
		return numList;
	}

	void judgetype() {// 判断钢瓶是否为同一个瓶
		tag = 0;
		for (int i = 0; i < NfcActivity.mDataWeight.size(); i++) {
			String xinpian = NfcActivity.mDataWeight.get(i).getXinpian();
			Log.e("safafsafsaf", xinpian);
			if(NfcActivity.mDataNull != null){
				for (int j = 0; j < NfcActivity.mDataNull.size(); j++) {
					if (xinpian.equals(NfcActivity.mDataNull.get(j).getXinpian())) {
						tag = 1;
					}
				}
			}
			
		}
	}

	// Intent intent = new Intent(NullActivity.this,
	// CashAndDiscountActivity.class);
	// Bundle bundle = new Bundle();
	// intent.putExtra("flag", "nullbottle");
	// intent.putExtra("show", getIntent().getStringExtra("show"));
	//
	// bundle.putSerializable("numList", (Serializable) weightList);
	// intent.putExtra("nullBundle", bundle);
	// startActivity(intent);

	@Override
	public void XuNo(int i) {
		// TODO Auto-generated method stub
		if (i == 1) {
			
			Intent intent = new Intent(NullActivity.this,
					PayMoneyActivity.class);// 结算
			null_bottle = "2";
			startActivity(intent);
		}
	}

	@Override
	public void XuCallback(int i) {
		// TODO Auto-generated method stub
		if (i == 1) {
			if(NfcActivity.mDataNull.size() == 0){
				Toast.makeText(NullActivity.this, "没扫空瓶", 1).show();
			}else{
//				null_bottle = "1";
			Intent yuqi = new Intent(NullActivity.this,ResidueGassActivity.class);
			yuqi.putExtra("type", 5);
			startActivityForResult(yuqi, 5);
			}
		}
	}

	void changeNum() {
		int g =0;
		weightList = new ArrayList<Type>();
		for (int i = 0; i < getNum(NfcActivity.mDataWeight).size(); i++) {
			Type te = new Type();
			int num = 0;
			int num1 = Integer.parseInt(getNum(NfcActivity.mDataWeight).get(i)
					.getNum());
			String type_name = getNum(NfcActivity.mDataWeight).get(i).getName();// 规格id
			if (NfcActivity.mDataNull != null
					&& NfcActivity.mDataNull.size() > 0) {

				for (int j = 0; j < getNum(NfcActivity.mDataNull).size(); j++) {

					if (type_name.equals(getNum(NfcActivity.mDataNull).get(j)
							.getName())) {
						g++;
						if (num1 != 0) {
							num1 = num1 - Integer.parseInt(getNum(NfcActivity.mDataNull).get(i)
									.getNum());
							 Log.e("lll_bull", num1+"");
						}
					} else {
						//
					}
				}
				if(g == 0){
					tag = 1;
				}
			}
			te.setNum(num1 + "");
			te.setName(getNum(NfcActivity.mDataWeight).get(i).getName());
			weightList.add(te);
			SPUtils.put(NullActivity.this, "yjjson", getYjData(weightList));
			Log.e("dfdsfd_押金串", getYjData(weightList));
		}

	}

	private void initType() {

		pd.show();
		String shop_id = (String) SPUtils.get(NullActivity.this,
				SPutilsKey.SHOP_ID, "");
		int token = (Integer) SPUtils.get(NullActivity.this, SPutilsKey.TOKEN,
				0);
		// 请求网络
		RequestParams params = new RequestParams(RequestUrl.BOTTLE_TYPE);
		params.addBodyParameter(SPutilsKey.SHOP_ID, shop_id);
		params.addBodyParameter(SPutilsKey.TOKEN, token + "");
		params.addBodyParameter("user_type", (String)SPUtils.get(NullActivity.this, "user_type", ""));
		HttpUtil.PostHttp(params, typeHandler, pd);

	}

	private Handler typeHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			if (result != null) {

				parseData(result, 2);

			}
		}

	};
	private SelectOrderInfoPopupWindow old;
	private TypePopupAdapter typeAdapter;

	private void parseData(String result, int i) {
		mTypeList = new ArrayList<Type>();
		Log.e("lll", result);
		if (i == 2) {
			try {
				JSONObject obj = new JSONObject(result);
				int resultCode = obj.getInt("resultCode");
				String info = obj.getString("resultInfo");
				if (resultCode == 1) {
					// 继续解析
					JSONArray array = obj.getJSONArray("resultInfo");
					for (int j = 0; j < array.length(); j++) {
						int num = 0;
						JSONObject object = array.getJSONObject(j);
						if (object.getString("type").equals("1")) {
							String bottle_name = object.getString("norm_id");
							String price = object.getString("yj_price");
//							Log.e("LLLL", weightList.size()+"");
							for (int k = 0; k < weightList.size(); k++) {
								if (bottle_name.equals(weightList.get(k)
										.getName())) {
									num = Integer.parseInt(weightList.get(k)
											.getNum());
								}
							}
							mTypeList.add(new Type(price, bottle_name, num + ""));
						}

					}
					for (int j = 0; j < mTypeList.size(); j++) {
						total_money += Double.parseDouble(mTypeList.get(j)
								.getPrice())
								* Integer.parseInt(mTypeList.get(j).getNum());

					}
					XuDialog.getInstance().setno(this);
					XuDialog.getInstance().setyes(this);
					XuDialog.getInstance().show(NullActivity.this, "是否有余气(本次押金:"+total_money+")", 1);
					SPUtils.put(NullActivity.this, "yajin", total_money);
					Log.e("LLLLL_yj", total_money + "");
					
				}else{
					Toast.makeText(NullActivity.this, info, 1).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	private String getYjData(List<Type> popupList) {
		JSONArray arr = new JSONArray();
		// 订单
		Log.e("lllpopupList", popupList.toString());
//		Log.e("lllmDataWeight", NfcActivity.mDataWeight.toString());
		for (int j = 0; j < popupList.size(); j++) {
			JSONObject obj1 = new JSONObject();
			try {
				obj1.put("type", popupList.get(j).getName());
				obj1.put("num", popupList.get(j).getNum());
				// obj1.put("wb", weightBottle);
				
				arr.put(obj1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
		for (int i = 0; i < arr.length(); i++) {
//			
			try {
				JSONObject obj = arr.getJSONObject(i);
				if(obj.get("num").equals("0")){
					arr.remove(i);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
//		return arr.toString();
		
		return arr.toString();
	}

}
