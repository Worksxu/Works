package com.ruiqi.works;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

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
import com.ruiqi.utils.T;
import com.ruiqi.utils.XuDialog;
import com.ruiqi.utils.XuDialog.No;
import com.ruiqi.utils.XuDialog.Yes;
import com.ruiqi.utils.XuIntent;
import com.ruiqi.utils.XuIntent.NoIntwnt;
import com.ruiqi.utils.XuIntent.YesIntwnt;
import com.ruiqi.xuworks.CashAndDiscountActivity;
import com.ruiqi.xuworks.CreateGassActivity;
import com.ruiqi.xuworks.NewOrderInfoActivity;
import com.ruiqi.xuworks.PayMoneyActivity;
import com.ruiqi.xuworks.PeijianBackShopActvity;
import com.ruiqi.xuworks.ZheJiuActivity;

//扫描重瓶界面
public class WeightActivity extends SaoMiaoActivity implements Yes, No,
		YesIntwnt, NoIntwnt {

	private String show;

	private List<String> mList;

	public static List<Weight> list;

	private WeightActivity wa;

	private List<Weight> mData;
	private List<Type> numList;

	private List<Type> weightList;
	private ArrayList<Type> mTypeList;
	HashMap<String, Integer> info;
	double total_money = 0;
	int judgeold;// 判断新老
	private int num;
	int judge;// 判断扫描瓶与订单瓶是否符合

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("扫描重瓶");
		System.out.println("+++" + SPutilsKey.type);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SPUtils.put(WeightActivity.this, "UI", "weight");
		show = getIntent().getStringExtra("show");// 来自于CreateOrderActivity和NFCActiivty传的值
		num = setNum();
		System.out.println("num=" + num);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	// 根据不同的界面，设置需要扫描的个数
	private int setNum() {
		int num = 0;
		if ("CreateOrderActivity".equals(show)) {// 创建订单过来的
			for (int i = 0; i < CreateOrderActivity.list.size(); i++) {
				Type t = CreateOrderActivity.list.get(i);
				num += Integer.parseInt(t.getNum());
			}
		} else if ("OrderInfoActivity".equals(show)) {// 订单详情过来的
			for (int i = 0; i < NewOrderInfoActivity.list.size(); i++) {
				Orderdeail o = NewOrderInfoActivity.list.get(i);
				num += Integer.parseInt(o.getNum());
			}
		} else if ("DownOrderActivity".equals(show)) {
			num = 10000000;
		}
		return num;
	}

	@Override
	public int initFlag() {
		show = getIntent().getStringExtra("show");
		if (show.equals("CreateOrderActivity")) {
			SPUtils.put(WeightActivity.this, "NFC", "createOrder");
			return 2;// 隐藏
		} else if (show.equals("OrderInfoActivity")) {
			SPUtils.put(WeightActivity.this, "NFC", "orderInfo");
			return 2;
		} else {
			SPUtils.put(WeightActivity.this, "NFC", "downorder");
			return 2;//
		}
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = null;
		switch (v.getId()) {
		case R.id.next:// 下一步
			 judge();
			initType();// 改动
			// if (judge == 1) {
			// Toast.makeText(WeightActivity.this, "扫描重瓶与订单不匹配", 1).show();
			// } else {
//			intent = initIntent(intent);
			// }
			break;
		case R.id.tv_input:// 手动输入
			if (NfcActivity.mDataWeight == null) {
				NfcActivity.mDataWeight = new ArrayList<Weight>();
			}
			str = et_input.getText().toString().trim();
			// 柴钢瓶判断
			new InPutIsBottle(str, WeightActivity.this, mData, adapter, pd,
					GpDao.getInstances(WeightActivity.this),
					NfcActivity.mDataWeight, 1).addDataToList();
			break;

		default:
			break;
		}
		// if(CreateOrderActivity.OrderKind==1){
		if (intent != null) {
			if (mData.size() < num || mData.size() == num) {
				if (NfcActivity.mData != null) {
					NfcActivity.mData = null;
				}
				if (mData.size() != 0) {
					SPUtils.remove(WeightActivity.this, "UI");
					startActivity(intent);
					finish();
				} else {
					T.showShort(WeightActivity.this, "瓶的数量小于用户实际的需求");
				}

			} else if (mData.size() > num) {
				T.showShort(WeightActivity.this, "瓶的数量大于用户实际的需求");
			}
		}
		// }else
		// if(CreateOrderActivity.OrderKind==2||CreateOrderActivity.OrderKind==3||CreateOrderActivity.OrderKind==4){
		// if(intent!=null){
		// if(mData.size()>0){
		// if(NfcActivity.mData!=null){
		// NfcActivity.mData=null;
		// }
		// SPUtils.remove(WeightActivity.this, "UI");
		// startActivity(intent);
		// finish();
		// }else if(mData.size()==0){
		// T.showShort(WeightActivity.this, "请扫描钢瓶");
		// }else if(mData.size()>num){
		// T.showShort(WeightActivity.this, "瓶的数量大于用户实际的需求");
		// }
		// }
	}

	// }
	@Override
	public void jumpPage() {
		super.jumpPage();

		SPutilsKey.type = 0;
		if (NfcActivity.mData != null) {
			NfcActivity.mData = null;
		}
		if (NfcActivity.mDataWeight != null) {
			NfcActivity.mDataWeight = null;
		}
		if (list != null) {
			list = null;
		}
		SPUtils.remove(WeightActivity.this, "UI");

		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == event.KEYCODE_BACK) {
			SPutilsKey.type = 0;
			if (NfcActivity.mData != null) {
				NfcActivity.mData = null;
			}
			if (NfcActivity.mDataWeight != null) {
				NfcActivity.mDataWeight = null;
			}
			if (list != null) {
				list = null;
			}
			SPUtils.remove(WeightActivity.this, "UI");
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private Intent initIntent(Intent intent) {
		// Log.e("lll", (String) SPUtils.get(WeightActivity.this, "old_new",
		// ""));

		switch (Integer.parseInt((String) SPUtils.get(WeightActivity.this,
				"old_new", ""))) {
		// 创建订单
		case 1:// 新用户
				// judgeold = 1;
			if (judgeold == 2) {
			XuDialog.getInstance().setno(this);
			XuDialog.getInstance().setyes(this);

			XuDialog.getInstance().show(WeightActivity.this,
					"是否有折旧瓶(本次押金:" + total_money + ")", 1);// 待改
			}
			// intent = new Intent(WeightActivity.this,
			// CashAndDiscountActivity.class);//押金和折现
			// intent.putExtra("flag", "weightbottle");
			// intent.putExtra("show", show);
			break;
		case 2:// 老用户
			 judge();
			// judgeold = 2;
			if (!TextUtils.isEmpty(DownOrderActivity.changebottle)
					&& DownOrderActivity.changebottle.equals("1")) {
				intent = new Intent(WeightActivity.this, NullActivity.class);// 空瓶
				intent.putExtra("show", show);
			} else {
				Log.e("llllllllllfds", total_money + "");

				 if (judge == 1) {
				 Toast.makeText(WeightActivity.this, "扫描重瓶与订单不匹配", 1).show();
				 } else {
				if (judgeold == 2) {
					XuIntent.getInstance().setnointent(
							(com.ruiqi.utils.XuIntent.NoIntwnt) this);
					XuIntent.getInstance().setyesintent(
							(com.ruiqi.utils.XuIntent.YesIntwnt) this);
					XuIntent.getInstance().show(WeightActivity.this,
							"是否有空瓶(本次押金:" + total_money + ")", 2, intent);
					 }
				}
			}
			// Intent intent1 = new Intent(WeightActivity.this,
			// NullActivity.class);// 空瓶
			// intent1.putExtra("show", show);
			// startActivity(intent1);
			break;
		default:
			T.showShort(WeightActivity.this, "请选择用户身份");
			break;
		}
		return intent;
	}

	@Override
	public Handler[] initHandler() {
		return null;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	public List<Weight> initList() {
		mData = new ArrayList<Weight>();
		if (NfcActivity.mDataWeight != null) {
			for (int i = 0; i < NfcActivity.mDataWeight.size(); i++) {
				Weight w = NfcActivity.mDataWeight.get(i);
				mData.add(new Weight(w.getXinpian(), w.getType(), w.getStatus()));
				Log.e("lll", w.getStatus());
			}
		}
		System.out.println("mDataWeight====" + NfcActivity.mDataWeight);
		return mData;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		System.out.println("NfcActivity.mData=" + NfcActivity.mData);
		CurrencyUtils.onLongClickDelete(position, WeightActivity.this, adapter,
				mData, NfcActivity.mDataWeight, NfcActivity.mData);
		return false;
	}

	@Override
	public void XuNo(int i) {
		// TODO Auto-generated method stub
		if (i == 1) {
			Intent intent = new Intent(WeightActivity.this,
					PayMoneyActivity.class);// 结算
			// Log.e("gsdgdsgdsg", "进来了gdsgdsg");
			if (intent != null) {
				if (mData.size() < num || mData.size() == num) {
					if (NfcActivity.mData != null) {
						NfcActivity.mData = null;
					}
					if (mData.size() != 0) {
						SPUtils.remove(WeightActivity.this, "UI");
						startActivity(intent);
						finish();
					} else {
						T.showShort(WeightActivity.this, "瓶的数量小于用户实际的需求");
					}

				} else if (mData.size() > num) {
					T.showShort(WeightActivity.this, "瓶的数量大于用户实际的需求");
				}
			}

		}
	}

	@Override
	public void XuCallback(int i) {
		// TODO Auto-generated method stub
		switch (i) {
		case 1:
			Intent old = new Intent(WeightActivity.this, ZheJiuActivity.class);
			old.putExtra("type", 3);

			if (old != null) {
				if (mData.size() < num || mData.size() == num) {
					if (NfcActivity.mData != null) {
						NfcActivity.mData = null;
					}
					if (mData.size() != 0) {
						SPUtils.remove(WeightActivity.this, "UI");
						startActivityForResult(old, 3);//
						finish();
					} else {
						T.showShort(WeightActivity.this, "瓶的数量小于用户实际的需求");
					}

				} else if (mData.size() > num) {
					T.showShort(WeightActivity.this, "瓶的数量大于用户实际的需求");
				}
			}
			break;
		// case 2:
		// Intent intent = new Intent(WeightActivity.this,
		// NullActivity.class);// 空瓶
		// intent.putExtra("show", show);
		// startActivity(intent);
		// break;

		default:
			break;
		}
	}

	private void initType() {

		pd.show();
		String shop_id = (String) SPUtils.get(WeightActivity.this,
				SPutilsKey.SHOP_ID, "");
		int token = (Integer) SPUtils.get(WeightActivity.this,
				SPutilsKey.TOKEN, 0);
		// 请求网络
		RequestParams params = new RequestParams(RequestUrl.BOTTLE_TYPE);
		params.addBodyParameter(SPutilsKey.SHOP_ID, shop_id);
		params.addBodyParameter(SPutilsKey.TOKEN, token + "");
		params.addBodyParameter("user_type", (String)SPUtils.get(WeightActivity.this, "user_type", ""));
		Log.e("fdsfsdfdsf", params.getStringParams().toString());
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

	private void parseData(String result, int i) {
		total_money = 0;
		mTypeList = new ArrayList<Type>();
		Log.e("lll", result);
		if (i == 2) {
			try {
				JSONObject obj = new JSONObject(result);
				int resultCode = obj.getInt("resultCode");
				if (resultCode == 1) {
					// 继续解析
					JSONArray array = obj.getJSONArray("resultInfo");
					for (int j = 0; j < array.length(); j++) {
						int num = 0;
						JSONObject object = array.getJSONObject(j);
						if (object.getString("type").equals("1")) {
							String bottle_name = object.getString("norm_id");
							String price = object.getString("yj_price");
							for (int k = 0; k < getNum(NfcActivity.mDataWeight)
									.size(); k++) {
								if (bottle_name.equals(getNum(
										NfcActivity.mDataWeight).get(k)
										.getName())) {
									num = Integer.parseInt(getNum(
											NfcActivity.mDataWeight).get(k)
											.getNum());
								}
							}
							mTypeList
									.add(new Type(price, bottle_name, num + ""));
						}

					}
					for (int j = 0; j < mTypeList.size(); j++) {
						total_money += Double.parseDouble(mTypeList.get(j)
								.getPrice())
								* Integer.parseInt(mTypeList.get(j).getNum());
						// total_num +=
						// Integer.parseInt(mTypeList.get(j).getNum());
					}
					judgeold = 2;
					SPUtils.put(WeightActivity.this, "yajin", total_money);
					Log.e("LLLLL_yj", total_money + "");
					Intent intent = null ;
					switch (Integer.parseInt((String) SPUtils.get(WeightActivity.this,
							"old_new", ""))) {
					case 1:
						XuDialog.getInstance().setno(this);
						XuDialog.getInstance().setyes(this);

						XuDialog.getInstance().show(WeightActivity.this,
								"是否有折旧瓶(本次押金:" + total_money + ")", 1);
						break;
					case 2:
						if (!TextUtils.isEmpty(DownOrderActivity.changebottle)
								&& DownOrderActivity.changebottle.equals("1")) {
							Log.e("lllll_置换", "走置换");
							 Intent null_a = new Intent(WeightActivity.this, NullActivity.class);// 空瓶
							 null_a.putExtra("show", show);
							 if(NfcActivity.mDataWeight != null&&NfcActivity.mDataWeight.size()>0){
								 startActivity(null_a);
							 }else{
								 Toast.makeText(WeightActivity.this, "请扫描重瓶", 1).show();
							 }
							
						} else {
							Log.e("llllllllllfds", total_money + "");

							 if (judge == 1) {
							 Toast.makeText(WeightActivity.this, "扫描重瓶与订单不匹配", 1).show();
							 } else {
							if (judgeold == 2) {
								XuIntent.getInstance().setnointent(
										(com.ruiqi.utils.XuIntent.NoIntwnt) this);
								XuIntent.getInstance().setyesintent(
										(com.ruiqi.utils.XuIntent.YesIntwnt) this);
								XuIntent.getInstance().show(WeightActivity.this,
										"是否有空瓶(本次押金:" + total_money + ")", 2, intent);
								 }
							}
						}
						break;

					default:
						break;
					}
					

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public List<Type> getNum(List<Weight> list) {
		numList = new ArrayList<Type>();
		// nullList = new ArrayList<Type>();
		// weightList = new ArrayList<Type>();
		info = new HashMap<String, Integer>();
		if (list != null && list.size() > 0) {
			
			for (int i = 0; i < list.size(); i++) {
				Log.e("sdsadas_重瓶", list.get(i).getYj());
				int num = 1;
				if (info.containsKey(list.get(i).getYj())) {
					info.put(list.get(i).getYj(), info.get(list.get(i).getYj())+1);
				} else {
					info.put(list.get(i).getYj(), num);
				}
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
		Log.e("lll", numList.toString());
		for (int i = 0; i < numList.size(); i++) {
			String nn = numList.get(i).getNum();
			Log.e("safasfas", numList.get(i).getName());
		}
		SPUtils.put(WeightActivity.this, "yjjson", getYjData(numList));
		return numList;
	}

	@Override
	public void XuNoIntent(int i, Intent intent) {
		// TODO Auto-generated method stub
		if (i == 2) {
			intent = new Intent(WeightActivity.this, PayMoneyActivity.class);// 结算
			Log.e("gsdgdsgdsg", "进来了gdsgdsg");
			// startActivity(intent);
			if (intent != null) {
				if (mData.size() < num || mData.size() == num) {
					if (NfcActivity.mData != null) {
						NfcActivity.mData = null;
					}
					if (mData.size() != 0) {
						SPUtils.remove(WeightActivity.this, "UI");
						startActivity(intent);
						finish();
					} else {
						T.showShort(WeightActivity.this, "瓶的数量小于用户实际的需求");
					}

				} else if (mData.size() > num) {
					T.showShort(WeightActivity.this, "瓶的数量大于用户实际的需求");
				}
			}
		}
	}

	@Override
	public void XuCallbackIntent(int i, Intent intent) {
		// TODO Auto-generated method stub
		if (i == 2) {
			intent = new Intent(WeightActivity.this, NullActivity.class);// 空瓶
			intent.putExtra("show", show);
			if (intent != null) {
				if (mData.size() < num || mData.size() == num) {
					if (NfcActivity.mData != null) {
						NfcActivity.mData = null;
					}
					if (mData.size() != 0) {
						SPUtils.remove(WeightActivity.this, "UI");
						startActivity(intent);
						finish();
					} else {
						T.showShort(WeightActivity.this, "瓶的数量小于用户实际的需求");
					}

				} else if (mData.size() > num) {
					T.showShort(WeightActivity.this, "瓶的数量大于用户实际的需求");
				}
			}
			Log.e("gsdgdsgdsg", "进来了gdsgdsg");
		}
	}

	void judge() {
		judge = 0;
		int g = 0;
		if (NewOrderInfoActivity.mList != null
				&& NewOrderInfoActivity.mList.size() != 0) {
			for (int i = 0; i < NewOrderInfoActivity.mList.size(); i++) {
				if (NfcActivity.mDataWeight != null
						&& NfcActivity.mDataWeight.size() != 0) {
					for (int j = 0; j < NfcActivity.mDataWeight.size(); j++) {
						if (NewOrderInfoActivity.mList
								.get(i)
								.getGoods_kind()
								.equals(NfcActivity.mDataWeight.get(j)
										.getType())) {
							g++;
							Log.e("lll_sadsa", NfcActivity.mDataWeight.size()+"");
							
							
							
						}
					}
				}
			}
			if(NfcActivity.mDataWeight != null&&g<NfcActivity.mDataWeight.size()){
				Log.e("lll_yiyangdecishu", g+"");
				judge = 1;
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
		
		return arr.toString();
	}

}
