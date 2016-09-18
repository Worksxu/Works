package com.ruiqi.xuworks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.ruiqi.adapter.CashAdapter;
import com.ruiqi.adapter.DeletItemAdapter;
import com.ruiqi.adapter.TypePopupAdapter;
import com.ruiqi.bean.PeiJian;
import com.ruiqi.bean.PeiJianTypeMoney;
import com.ruiqi.bean.Type;
import com.ruiqi.bean.Weight;
import com.ruiqi.bean.ZheKouInfo;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.view.CustomDownView;
import com.ruiqi.view.SwipeMenuListView;
import com.ruiqi.view.popupwindow.SelectOrderInfoPopupWindow;
import com.ruiqi.view.popupwindow.SelectOrderInfoPopupWindow.PopDismiss;
import com.ruiqi.works.AddPeijianActivity;
import com.ruiqi.works.AddZhekou;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.NfcActivity;
import com.ruiqi.works.NullActivity;
import com.ruiqi.works.R;

/**
 * 
 * @author xtl
 * 押金折现界面
 */

public class CashAndDiscountActivity extends BaseActivity implements PopDismiss {
	private CustomDownView cdv_total,cdv_zhejiu,cdv_yuqi,cdv_canye,cdv_peijian,cdv_dikou;
	private TextView tv_total,tv_zhejiu,tv_yuqi,tv_canye,tv_next;
	private ListView lv_select_address;
	private SwipeMenuListView lv_peijian,lv_dikou;
	private ProgressDialog pd;
	private ArrayList<Type> mTypeList;
	private ArrayList<Type> peijianList ;
	private ArrayList<Type> zhekouList ;
//	private ArrayList<Type> numList = new ArrayList<Type>();
	private DeletItemAdapter adapter;
	private CashAdapter adapter1;
	private double total_money,zhejiu_money,total_peijian;
	String canye_money = "0";
	String yuqi_money = "0";
	String canye_weight = "0";
	int total_num = 0;
	Bundle bunlde;
	public static List<Weight> list;
	private List<Type> numList;
	private List<Type> nullList;
	private List<Type> weightList;
	HashMap<String, Integer> info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashanddiscount_layout);
		
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("押金折现");
		
		pd = new ProgressDialog(this);
		pd.setMessage("正在加载......");
		bunlde = new Bundle();
		bunlde  = getIntent().getBundleExtra("nullBundle");
//		if(SPUtils.get(CashAndDiscountActivity.this, "oldnew", "").equals("2")){
//			numList = (ArrayList<Type>) bunlde.getSerializable("numList");
//			Log.e("lll_numlist", numList.size()+"");
//		}
		changeNum();
		initType();
		lv_select_address = (ListView) findViewById(R.id.lv_select_address);
		lv_peijian = (SwipeMenuListView) findViewById(R.id.listView_cash_peijian);
		lv_dikou = (SwipeMenuListView) findViewById(R.id.listView_cash_zhekou);
		tv_canye = (TextView) findViewById(R.id.tv_cash_canye);
		tv_yuqi = (TextView) findViewById(R.id.tv_cash_yuqi);
		tv_total = (TextView) findViewById(R.id.tv_cash_total);
		tv_zhejiu = (TextView) findViewById(R.id.tv_cash_oldBottle);
		tv_next = (TextView) findViewById(R.id.tv_cash_next);
		tv_next.setOnClickListener(this);
		cdv_canye = (CustomDownView) findViewById(R.id.CustomDownView_cash_canye);
		cdv_yuqi = (CustomDownView) findViewById(R.id.CustomDownView_cash_yuqi);
		cdv_total = (CustomDownView) findViewById(R.id.CustomDownView_cash_total);
		cdv_peijian = (CustomDownView) findViewById(R.id.CustomDownView_cash_peijian);
		cdv_dikou = (CustomDownView) findViewById(R.id.CustomDownView_cash_zhekou);
		cdv_zhejiu = (CustomDownView) findViewById(R.id.CustomDownView_cash_oldBottle);
		cdv_zhejiu.setOnClickListener(this);
		cdv_total.setOnClickListener(this);
		cdv_yuqi.setOnClickListener(this);
		cdv_peijian.setOnClickListener(this);
		cdv_dikou.setOnClickListener(this);// 折扣商品
		cdv_canye.setOnClickListener(this);
		cdv_peijian.setString("添加配件");
		cdv_peijian.setView(View.VISIBLE);
		cdv_total.setString("选择钢瓶规格");
		cdv_total.setView(View.VISIBLE);
		cdv_zhejiu.setString("选择折旧瓶规格");
		cdv_zhejiu.setView(View.VISIBLE);
		cdv_yuqi.setString("余气折现");
		cdv_yuqi.setView(View.VISIBLE);
		cdv_canye.setString("残液折现");
		cdv_canye.setView(View.VISIBLE);
		cdv_dikou.setString("折扣商品");
		cdv_dikou.setView(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_cash_next:
			
//			total_peijian = Double.parseDouble(peijian_money)+Double.parseDouble(zhekou_money);
			SPUtils.put(CashAndDiscountActivity.this, "yajin", tv_total.getText().toString());
			SPUtils.put(CashAndDiscountActivity.this, "zhejiu", tv_zhejiu.getText().toString());
			SPUtils.put(CashAndDiscountActivity.this, "canye_money", canye_money);// 钱
			SPUtils.put(CashAndDiscountActivity.this, "canye_weight", canye_weight);//重
			SPUtils.put(CashAndDiscountActivity.this, "yuqi", tv_yuqi.getText().toString());
			SPUtils.put(CashAndDiscountActivity.this, "total_num", total_num+"");
			double numList_num = 0;
//			SPUtils.put(CashAndDiscountActivity.this, "peijian_total", total_peijian+"");
			for (int i = 0; i < weightList.size(); i++) {
				 numList_num += Integer.parseInt(weightList.get(i).getNum());
			}
			
			if(weightList != null&&numList_num != 0&&tv_total.getText().toString().equals("0.0")){
				Toast.makeText(CashAndDiscountActivity.this, "请选择押金", 1).show();
			}else{
				Intent pay = new Intent(CashAndDiscountActivity.this,PayMoneyActivity.class);
				startActivity(pay);
			}
			
			
			
			
			break;
		case R.id.CustomDownView_cash_canye:
			Intent canye = new Intent(CashAndDiscountActivity.this,CanyeActivity.class);
			startActivityForResult(canye, 4);
			break;
		case R.id.CustomDownView_cash_oldBottle:
			Intent old = new Intent(CashAndDiscountActivity.this,ZheJiuActivity.class);
			old.putExtra("type", 3);
			startActivityForResult(old, 3);
			break;
		case R.id.CustomDownView_cash_peijian:
			Intent intent = new Intent(CashAndDiscountActivity.this,AddPeijianActivity.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.CustomDownView_cash_total:
//			initType(v);
			break;
		case R.id.CustomDownView_cash_yuqi:
			if(NfcActivity.mDataNull.size() == 0){
				Toast.makeText(CashAndDiscountActivity.this, "没扫空瓶", 1).show();
			}else{
			Intent yuqi = new Intent(CashAndDiscountActivity.this,ResidueGassActivity.class);
			yuqi.putExtra("type", 5);
			startActivityForResult(yuqi, 5);}
			break;
		case R.id.CustomDownView_cash_zhekou:
			Intent zhekou = new Intent(CashAndDiscountActivity.this,AddZhekou.class);
			startActivityForResult(zhekou, 2);
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		switch (arg1) {
		case 3:
			zhejiu_money = arg2.getExtras().getDouble("zhejiu_money");
			canye_money = arg2.getExtras().getString("canye_money");
			canye_weight = arg2.getExtras().getString("canye_weight");
			
			tv_zhejiu.setText(zhejiu_money+"");
			Log.e("lll_zhejiu", zhejiu_money+"");
			break;
//		case 1:
//			peijianList = new ArrayList<Type>();
//			peijianList = (ArrayList<Type>) arg2.getBundleExtra("peijian").getSerializable("mDatas");
//			peijian_money = arg2.getBundleExtra("peijian").getString("money");
//			adapter = new DeletItemAdapter(CashAndDiscountActivity.this, peijianList);
//			lv_peijian.setAdapter(adapter);
//			break;
//		case 2:
//			zhekouList = new ArrayList<Type>();
//			zhekouList = (ArrayList<Type>) arg2.getBundleExtra("zhekou").getSerializable("zhekou_list");
//			zhekou_money  = zhekouList.get(0).getPrice();
//			adapter = new DeletItemAdapter(CashAndDiscountActivity.this, zhekouList);
//			lv_dikou.setAdapter(adapter);
//			break;
		case 5:
			tv_yuqi.setText(arg2.getExtras().getString("yuqi_money"));
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
	private void initType() {


		
			pd.show();
			String shop_id = (String) SPUtils.get(CashAndDiscountActivity.this,
					SPutilsKey.SHOP_ID, "");
			int token = (Integer) SPUtils.get(CashAndDiscountActivity.this,
					SPutilsKey.TOKEN, 0);
			// 请求网络
			RequestParams params = new RequestParams(RequestUrl.BOTTLE_TYPE);
			params.addBodyParameter(SPutilsKey.SHOP_ID, shop_id);
			params.addBodyParameter(SPutilsKey.TOKEN, token + "");
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
				if (resultCode == 1) {
					// 继续解析
					JSONArray array = obj.getJSONArray("resultInfo");
					for (int j = 0; j < array.length(); j++) {
						int num = 0;
						JSONObject object = array.getJSONObject(j);
						if(object.getString("type").equals("1")){
							String bottle_name = object.getString("typename");
							String price = object.getString("yj_price");
							for (int k = 0; k < weightList.size(); k++) {
								if(bottle_name.equals(weightList.get(k).getName())){
									num = Integer.parseInt(weightList.get(k).getNum());
								}
							}
							mTypeList.add(new Type(price, bottle_name, num+""));
						}
					
						
					}
					for (int j = 0; j < mTypeList.size(); j++) {
						total_money += Double.parseDouble(mTypeList.get(j).getPrice()) * Integer.parseInt(mTypeList.get(j).getNum()) ;
						 total_num += Integer.parseInt(mTypeList.get(j).getNum());
					}
					tv_total.setText(total_money+"");
					
//					// 创建适配器
//					adapter1 = new CashAdapter(mTypeList,
//							CashAndDiscountActivity.this);
//
//					// 填充适配器
//					old = new SelectOrderInfoPopupWindow(
//							CashAndDiscountActivity.this, itemsOnClickType,
//							lv_select_address, adapter1, onclickType);
//					old.setPopDismiss(this);
//					old.showAtLocation(
//							CashAndDiscountActivity.this.findViewById(R.id.ll_main),
//							Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void popDismissCalBack() {
		// TODO Auto-generated method stub
		
	}
	// listview子项的点击
		private OnItemClickListener itemsOnClickType = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {

			}
		};
		private OnClickListener onclickType = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			switch (v.getId()) {
				
			case R.id.tv_sure:// 确定
				
				typeSure();
				break;
			case R.id.tv_quxiao:// 取消
				old.dismiss();
				break;
			}
			}

			private void typeSure() {
				// TODO Auto-generated method stub
				Log.e("lll", mTypeList.size()+"");
				total_money = 0;
				
				old.dismiss();
			}
		};
		public List<Type> getNum(List<Weight> list){
			numList = new ArrayList<Type>();
//			nullList = new ArrayList<Type>();
//			weightList = new ArrayList<Type>();
			 info = new HashMap<String, Integer>();
			
			for (int i = 0; i < list.size(); i++) {
				 int num = 1;
				if(info.containsKey(list.get(i).getType())){
					info.put(list.get(i).getType(), num+=1);
				}else{
					info.put(list.get(i).getType(), num);
				}
			}
			 for(Map.Entry<String, Integer> entry : info.entrySet()) {  
		            String key = entry.getKey();  
		            Integer value = entry.getValue();  
		            Type type = new Type();
		            type.setName(key);
		            type.setNum(value+"");
		            numList.add(type);
		        }  
			 Log.e("lll", numList.toString());
			 return numList;
		}
		void changeNum(){
			weightList = new ArrayList<Type>();
			for (int i = 0; i < getNum(NfcActivity.mDataWeight).size(); i++) {
				Type te = new Type();
				int num1 = Integer.parseInt(getNum(NfcActivity.mDataWeight).get(i).getNum());
				String type_name = getNum(NfcActivity.mDataWeight).get(i).getName();
				if(NfcActivity.mDataNull != null&&NfcActivity.mDataNull.size()>0){
					
				
				for (int j = 0; j <getNum(NfcActivity.mDataNull).size(); j++) {
					
					if(type_name.equals(getNum(NfcActivity.mDataNull).get(j).getName())){
						if(num1 != 0){
							num1--;
//							Log.e("lll_bull", num1+"");
						}
					}else{
//						Log.e("lll_bull---", num1+"");
//						Toast.makeText(NullActivity.this, "空瓶不匹配.请先退瓶", 1).show();
						// 待跳转到退瓶订单
					}
				}
				}
				te.setNum(num1+"");
				te.setName(getNum(NfcActivity.mDataWeight).get(i).getName());
				weightList.add(te);
			}
//			Intent intent = new Intent(NullActivity.this, CashAndDiscountActivity.class);
//			Bundle bundle = new Bundle();
//			intent.putExtra("flag", "nullbottle");
//			intent.putExtra("show", getIntent().getStringExtra("show"));
//			
//			bundle.putSerializable("numList", (Serializable) weightList);
//			intent.putExtra("nullBundle", bundle);
//			startActivity(intent);
		}
		

}

