package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.Table.OrderTable;
import com.ruiqi.Table.TabAdapter;
import com.ruiqi.bean.BackBottle;
import com.ruiqi.bean.Bottle;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.db.BackBottleDao;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.view.AutoListView;
import com.ruiqi.view.AutoListView.OnLoadListener;
import com.ruiqi.view.AutoListView.OnRefreshListener;
import com.ruiqi.works.BackBottleActivity;
import com.ruiqi.works.BackBottleOrder;
import com.ruiqi.works.R;
import com.ruiqi.xuworks.BackBottleOrderInfo;
import com.ruiqi.xuworks.FinishBottleActivity;

import android.R.array;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 退瓶订单订单信息表格碎片
 * 
 * @author Administrator
 *
 */
public class BackBottleFragment extends OrderFragment implements
		OnCheckedChangeListener {

	private BackBottleDao od;
	private ProgressDialog pd;
	public static List<BackBottle> mData;
	private RadioGroup rg_select;
	String type = "1";// 区分类型1未完成,2已完成
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}

	};
	private int page = 1;
	private int page_finish = 1;
	private boolean isRefush = true;
	private int position;

	@Override
	public View initView() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.backbottle_layout, null);
		lv_unfinsh_order = (AutoListView) view
				.findViewById(R.id.lv_unfinsh_order);
		rg_select = (RadioGroup) view.findViewById(R.id.rg_select);
		rg_select.setOnCheckedChangeListener(this);
		pd = new ProgressDialog(getContext());
		initData(type);

		lv_unfinsh_order.setOnItemClickListener(this);

		lv_unfinsh_order.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				page = 1;
				isRefush = true;
				mDatas = new ArrayList<TableInfo>();
				System.out.println("下拉之前的集合" + mDatas);
				mData = new ArrayList<BackBottle>();
				String shop_id = (String) SPUtils.get(getContext(),
						SPutilsKey.SHOP_ID, "error");
				String ship_id = (String) SPUtils.get(getContext(),
						SPutilsKey.SHIP_ID, "error");

				RequestParams params = new RequestParams(
						RequestUrl.CHANGEORDERLIST);
				params.addBodyParameter("shipper_id", ship_id);
				params.addBodyParameter("shop_id", shop_id);
				params.addBodyParameter("page", "1");
				params.addBodyParameter("type", "1");

				System.out.println("shop_id=" + shop_id);

				HttpUtil.PostHttp(params, handler, new ProgressDialog(
						getContext()));
			}
		});

		lv_unfinsh_order.setOnLoadListener(new OnLoadListener() {

			@Override
			public void onLoad() {
				
				if(type.equals("1")){
					page++;
				}else{
					page_finish ++;
				}
				isRefush = false;
				position = lv_unfinsh_order.getFirstVisiableItem();
				String shop_id = (String) SPUtils.get(getContext(),
						SPutilsKey.SHOP_ID, "error");
				String ship_id = (String) SPUtils.get(getContext(),
						SPutilsKey.SHIP_ID, "error");

				RequestParams params = new RequestParams(
						RequestUrl.CHANGEORDERLIST);
				params.addBodyParameter("shipper_id", ship_id);
				params.addBodyParameter("shop_id", shop_id);
				if(type.equals("1")){
					params.addBodyParameter("page", page +"");// 待改
				}else{
					params.addBodyParameter("page", page_finish +"");// 待改
				}
				
				params.addBodyParameter("type", "1");

				System.out.println("shop_id=" + shop_id);
				Log.e("llll_tuipingparams", params.getStringParams().toString());
				HttpUtil.PostHttp(params, handler, new ProgressDialog(
						getContext()));
			}
		});

		return view;
	}

	@Override
	public void initData(String type) {
		pd.show();
		if (isRefush) {
			mDatas = new ArrayList<TableInfo>();
		}
		mData = new ArrayList<BackBottle>();
		od = BackBottleDao.getInstances(getContext());
		String shop_id = (String) SPUtils.get(getContext(), SPutilsKey.SHOP_ID,
				"error");
		String ship_id = (String) SPUtils.get(getContext(), SPutilsKey.SHIP_ID,
				"error");

		RequestParams params = new RequestParams(RequestUrl.CHANGEORDERLIST);
		params.addBodyParameter("shipper_id", ship_id);
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("page", "1");
		params.addBodyParameter("type", "1");
		// if(type.equals("2")){
		// // params.addBodyParameter("type", "2");
		// }else{
		//
		// }
		Log.e("lll_params刷新", params.getStringParams().toString());
		System.out.println("shop_id=" + shop_id);
		System.out.println("shipper_id=" + ship_id);
		HttpUtil.PostHttp(params, handler, pd);
		/*
		 * Cursor cursor = od.getFromStatus("1"); while (cursor.moveToNext()) {
		 * String ordersn =
		 * cursor.getString(cursor.getColumnIndex("depositsn")); String money =
		 * cursor.getString(cursor.getColumnIndex("productmoney")); String
		 * status = cursor.getString(cursor.getColumnIndex("status")); String
		 * time = cursor.getString(cursor.getColumnIndex("time")); String
		 * status_name = cursor.getString(cursor.getColumnIndex("status_name"));
		 * mDatas.add(new TableInfo(ordersn, money, status_name, time)); }
		 */
	}

	/**
	 * { "id": "200", "depositsn": "tp160820525510366", "receiptno": null,
	 * "money": "0.00", "deposit": "0.00", "type": "1", "status": "2",
	 * "shop_id": "17", "shipper_id": "53", "shipper_name":
	 * "\u95e8\u5e971\u9001\u6c14\u5de5", "shipper_mobile": "13693394732",
	 * "kid": "366", "mobile": "15940924129", "username": "\u4f60\u54e6",
	 * "comment": "", "ctime": "1471677572", "time": "2016-08-20", "sheng":
	 * "140", "shi": "1128", "qu": "5976", "cun": "5978", "address":
	 * "\u6ca7\u5dde\u65b0\u534e\u533a\u5efa\u8bbe\u5317\u8857\u9053\u533b\u9662\u4e1c\u8857\u793e\u533a\u5c45\u59d4\u4f1a"
	 * , "bottle":
	 * "[{\"xinpian\":\"5E06051C\",\"number\":\"5E06051C\",\"price\":\"0\",\"weight\":\"0\",\"type\":\"4\",\"deposit\":\"0\"}]"
	 * , "bottle_text": [ "5E06051C" ], "change_data": [ "2E6C0A1C" ],
	 * "good_time": "0", "shipment": "0", "is_discount": "0", "shouldmoney":
	 * "0", "shouldweight": "0", "doormoney": "0", "productmoney": "0",
	 * "time_created": "1471677572", "shop_name": "\u95e8\u5e971", "sheng_name":
	 * "\u6ca7\u5dde", "shi_name": "\u65b0\u534e\u533a", "qu_name":
	 * "\u5efa\u8bbe\u5317\u8857\u9053", "cun_name":
	 * "\u533b\u9662\u4e1c\u8857\u793e\u533a\u5c45\u59d4\u4f1a", "data": [ {
	 * "xinpian": "5E06051C", "number": "5E06051C", "price": "0", "weight": "0",
	 * "type": "4", "deposit": "0" } ] },
	 * 
	 * @param result
	 */

	private void paraseData(String result) {
		System.out.println("未完成=" + result);
		Log.e("lll", result);
		// json解析
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if (resultCode == 1) {
				// 继续解析
				if (!obj.isNull("resultInfo")) {
					JSONArray array = obj.getJSONArray("resultInfo");
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						String depositsn = object.getString("depositsn");
						String money = object.getString("money");//
						String doormoney = object.getString("doormoney");// 上门费
						String productmoney = object.getString("productmoney");
						String shouldmoney = object.getString("deposit");//yajin
						String status = object.getString("status");
						String username = object.getString("username");
						String time = object.getString("time");
						String mobile = object.getString("mobile");
						String id = object.getString("id");
						String address = object.getString("address");
						String kid = object.getString("kid");
						String sheng = object.getString("sheng");
						String shi = object.getString("shi");
						String qu = object.getString("qu");
						String cun = object.getString("cun");
						String status_name = object.getString("time_created");
						String data = object.getString("data");
						// String boottle = object.getString("bottle");
						if (!"null".equals(data)) {

							JSONArray array1 = new JSONArray(data);
							List<Bottle> list = new ArrayList<Bottle>();
							for (int j = 0; j < array1.length(); j++) {
								JSONObject json = array1.getJSONObject(j);
								String typename = json.getString("good_name");//待定
								String xinpian = json.getString("xinpian");
								String number = json.getString("number");
								// if(json.has("goods_title")){
								// String title = json.getString("goods_title");
								// if(!"null".equals(title)){
								// list.add(new Bottle(goods_num, goods_price,
								// title));
								// }else{
								// list.add(new Bottle(goods_num, goods_price,
								// ""));
								// }
								// }else{
								list.add(new Bottle(xinpian, typename, number));// xinpian,规格钢印
								// }

							}
							mData.add(new BackBottle(sheng,shi,qu,cun,depositsn, money,
									doormoney, productmoney, shouldmoney,
									username, time, mobile, id, address,
									status, kid, status_name, list));
						} else {
							mData.add(new BackBottle(sheng,shi,qu,cun,depositsn, money,
									doormoney, productmoney, shouldmoney,
									username, time, mobile, id, address,
									status, kid, status_name,
									new ArrayList<Bottle>()));
						}
						if (type.equals("1")) {
							
							if ("1".equals(status)) {

								mDatas.add(new TableInfo(depositsn, username,
										"配送中", time));
								Log.e("lllll未完成", mDatas.size()+"");
							}
						} else if(type.equals("2")) {
							
							if ("2".equals(status)) {
							mDatas.add(new TableInfo(depositsn, money, "已完成",
									time));
							Log.e("lllllsfdsf", mDatas.size()+"");
						}
							}

						// 保存到数据库
						Cursor cursor = od.getFromOrderSn(depositsn);
						if (cursor.getCount() == 0) {// 没找到，插入到数据库
							od.saveOrder(depositsn, money, doormoney,
									productmoney, shouldmoney, username, time,
									mobile, id, address, status, kid,
									status_name);
						}

					}
					System.out.println("刷新之后的集合=" + mDatas);
					// 显示退瓶订单列表
					table = new OrderTable().addData(mDatas, initTitles());
					adapter = new TabAdapter(getContext(), table);

					lv_unfinsh_order.setAdapter(adapter);
					if (isRefush) {
						lv_unfinsh_order.onRefreshComplete();
						lv_unfinsh_order.onLoadComplete();
						lv_unfinsh_order.setResultSize(mDatas.size());
//						
					} else {
						lv_unfinsh_order.onLoadComplete();
						lv_unfinsh_order.setSelection(position);
						lv_unfinsh_order.setResultSize(mDatas.size());
					}
				} 

			}else {
				String info = obj.getString("resultInfo");
				
				lv_unfinsh_order.onRefreshComplete();
				lv_unfinsh_order.onLoadComplete();
				lv_unfinsh_order.setResultSize(0);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	};

	@Override
	public String[] initTitles() {
		if (type.equals("2")) {// 已完成
			String titles[] = { "订单号", "订单金额", "订单状态", "订单时间" };
			return titles;
		} else {
			String titles[] = { "订单号", "客户姓名", "订单状态", "订单时间" };
			return titles;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position > 1 && position < mDatas.size() + 2) {
			String depositsn = mDatas.get(position - 2).getOrderNum();
			SPUtils.put(getContext(), "depositsn", depositsn);
			// 跳转到订单详情界面
			if (type.equals("1")) {// 未完成
				Intent intent = new Intent(getContext(),
						BackBottleOrderInfo.class);
				intent.putExtra("depositsn", depositsn);
				intent.putExtra("from", "bottleorder");
				startActivity(intent);
			} else {
				Intent intent = new Intent(getContext(),
						FinishBottleActivity.class);
				intent.putExtra("depositsn", depositsn);
				startActivity(intent);
			}

		}

	}

	@Override
	public int isOrNoSet() {
		return 1;
	}

	@Override
	public boolean isRefush() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.rb_backbottle_finish:
			type = "2";
			page = 1;
			mDatas = new ArrayList<TableInfo>();
			initData("2");
			break;
		case R.id.rb_backbottle_unfinish:
			type = "1";
			page_finish = 1;
			mDatas = new ArrayList<TableInfo>();
			initData("1");
			break;

		default:
			break;
		}
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

}
