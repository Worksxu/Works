package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.Table.OrderTable;
import com.ruiqi.Table.TabAdapter;
import com.ruiqi.Table.TabRow;
import com.ruiqi.bean.Order;
import com.ruiqi.bean.Orderdeail;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.db.OrderDao;
import com.ruiqi.utils.DateUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.IsPhone;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.view.AutoListView;
import com.ruiqi.view.AutoListView.OnLoadListener;
import com.ruiqi.view.AutoListView.OnRefreshListener;
import com.ruiqi.works.CreateOrderActivity;
import com.ruiqi.works.DownOrderActivity;
import com.ruiqi.works.GrassOrder;
import com.ruiqi.works.OrderInfoActivity;
import com.ruiqi.works.R;
import com.ruiqi.works.WeightActivity;
import com.ruiqi.xuworks.ChangeOrderInfoActivity;
import com.ruiqi.xuworks.ChangeOrderListActivity;
import com.ruiqi.xuworks.NewOrderInfoActivity;
import com.ruiqi.xuworks.RepairInfoActivity;
import com.ruiqi.xuworks.Repairorder;
import com.ruiqi.xuworks.ZheJiuOrderListActivity;
import com.ruiqi.xuworks.ZhejiuInfoActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TableRow;
import android.widget.Toast;

public class ChangeOrderFinishFragment extends OrderFragment implements OnCheckedChangeListener {
	String type = "1";// 区分类型1未完成,2已完成
	private OrderDao od;
	public static List<Order> mData;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			praseData(result);
		}
	};

	private boolean isRefulsh = true;
	private int page = 1;
	private int position;
	private ProgressDialog pd;
	private RadioGroup rg_select;
	@Override
	public View initView() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.backbottle_layout, null);
		lv_unfinsh_order = (AutoListView) view
				.findViewById(R.id.lv_unfinsh_order);
		rg_select = (RadioGroup) view.findViewById(R.id.rg_select);
		rg_select.setOnCheckedChangeListener(this);
		pd = new ProgressDialog(getContext());
		pd.setMessage("正在加载");
		initData();
		table = new OrderTable().addData(mDatas, initTitles());

		adapter = new TabAdapter(getContext(), table);

		lv_unfinsh_order.setAdapter(adapter);

		// lv_unfinsh_order.setResultSize(mDatas.size());
		lv_unfinsh_order.setOnItemClickListener(this);
		// 下拉刷新
		lv_unfinsh_order.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				isRefulsh = true;
				page = 1;
				mDatas = new ArrayList<TableInfo>();
				GrassOrder.mData = new ArrayList<Order>();
				// 请求网络
				RequestParams params = new RequestParams(
						RequestUrl.CHANGEORDERLIST);
				String id = (String) SPUtils.get(getContext(),
						SPutilsKey.SHIP_ID, "error");
				params.addBodyParameter("shipper_id", id);
				params.addBodyParameter("page", "1");
				 params.addBodyParameter("type", "2");// 2置换
				HttpUtil.PostHttp(params, handler, new ProgressDialog(
						getContext()));
			}
		});
		// 上拉加载更多
		lv_unfinsh_order.setOnLoadListener(new OnLoadListener() {
			@Override
			public void onLoad() {
				position = lv_unfinsh_order.getFirstVisiableItem();
				isRefulsh = false;
				page++;
				System.out.println("page=" + page);
				RequestParams params = new RequestParams(
						RequestUrl.CHANGEORDERLIST);
				String id = (String) SPUtils.get(getContext(),
						SPutilsKey.SHIP_ID, "error");
				params.addBodyParameter("shipper_id", id);
				params.addBodyParameter("page", page + "");
				 params.addBodyParameter("type", "2");//1 未完成 2 已完成
				HttpUtil.PostHttp(params, handler, new ProgressDialog(
						getContext()));
			}
		});

		return view;
	}

	@Override
	public void initData() {
		if (isRefulsh) {
			mDatas = new ArrayList<TableInfo>();
		}
		pd.show();

		mData = new ArrayList<Order>();
		RequestParams params = new RequestParams(RequestUrl.CHANGEORDERLIST);
		String mobile = (String) SPUtils.get(getContext(), SPutilsKey.MOBILLE,
				"error");
		String ship_id = (String) SPUtils.get(getContext(), SPutilsKey.SHIP_ID,
				"error");
		params.addBodyParameter("shipper_id", ship_id);
		params.addBodyParameter("page", "1");
		 params.addBodyParameter("type", "2");//0
		HttpUtil.PostHttp(params, handler, pd);

		od = OrderDao.getInstances(getContext());

	}

	/**
	 * 解析数据
	 * 
	 * @param result
	 *            { "resultCode": 1, "resultInfo": [ { "id": "1", "order_sn":
	 *            "tp160819481015366", "kid": "366", "mobile": "15940924129",
	 *            "sheng": "140", "shi": "1128", "qu": "5985", "cun": "5987",
	 *            "address": "nullnullnullnull", "bottle_data":
	 *            "[{\"xinpian\":\"BEE9141C\",\"number\":\"BEE9141C\",\"price\":\"0\",\"weight\":\"0\",\"type\":\"4\"}]"
	 *            , "change_data":
	 *            "[{\"xinpian\":\"BE3EDC31\",\"number\":\"BE3EDC31\",\"price\":\"0\",\"weight\":\"0\",\"type\":\"4\"}]"
	 *            , "shop_id": "17", "shipper_id": "53", "shipper_name":
	 *            "门店1送气工", "shipper_mobile": "13693394732", "comment": null,
	 *            "weight": "0.00", "deposit": "0.00", "money": "0.00",
	 *            "status": "1", "time_created": "1471575296", "shop_name":
	 *            "门店1", "sheng_name": "沧州", "shi_name": "新华区", "qu_name":
	 *            "车站街道", "cun_name": "沧运社区居委会", "time": "2016-08-19", "data": [
	 *            { "xinpian": "BEE9141C", "number": "BEE9141C", "price": "0",
	 *            "weight": "0", "type": "4" } ] } ] }
	 * 
	 */
	private void praseData(String result) {
		Log.e("llll_changeOrder", result);

		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if (resultCode == 1) {
				JSONArray array = obj.getJSONArray("resultInfo");
				System.out.println("未完成length=" + array.length());
				for (int i = 0; i < array.length(); i++) {

					StringBuffer sb = new StringBuffer();
					JSONObject obj1 = array.getJSONObject(i);

					Order ord = new Order();

					Log.e("lll", obj1.toString());

					String ordersn = obj1.getString("depositsn");// 订单编号

//					String weight = obj1.getString("weight");// 重量
					String time = obj1.getString("time");// 订单创建时间
					// String date = DateUtils.TimeStampToDate(time,
					// "yyyy-MM-dd HH:mm");
					String status = obj1.getString("status");// 订单状态
					String id = obj1.getString("id");// 订单id
					String mobile = obj1.getString("mobile");// 客户电话
					 String username = obj1.getString("username");// 客户姓名
					String money = obj1.getString("money");
					String address = obj1.getString("address");
					String data = obj1.getString("data");
					String change_data = obj1.getString("change_data");
					ord.setOrdersn(ordersn);
					ord.setComment(data);// 故障串
					ord.setTime(time);
					ord.setStatus(status);
					ord.setAddress(address);
					ord.setPay_money(money);
					ord.setKid(change_data);//重瓶串
					ord.setUsername(username);
					ord.setMobile(mobile);
					mData.add(ord);
//					mDatas.add(new TableInfo(ordersn, username, "已完成", time));
					if (type.equals("1")) {
						if ("1".equals(status)) {

							mDatas.add(new TableInfo(ordersn, username,
									"配送中", time));
						}
					} else {
						if ("2".equals(status)) {
						mDatas.add(new TableInfo(ordersn, username, "已完成",
								time));
					}
						}

				}
				// initData();
				System.out.println("mdatas=" + mDatas);
				table = new OrderTable().addData(mDatas, initTitles());
				adapter = new TabAdapter(getContext(), table);

				lv_unfinsh_order.setAdapter(adapter);
				if (isRefulsh) {
					lv_unfinsh_order.setResultSize(mDatas.size());
					lv_unfinsh_order.onRefreshComplete();
				} else {
					// lv_unfinsh_order.onLoadComplete();
					lv_unfinsh_order.onLoadComplete();
					lv_unfinsh_order.setSelection(position);
					lv_unfinsh_order.setResultSize(mDatas.size());
				}

			} else {
				String info = obj.getString("resultInfo");
				Toast.makeText(getActivity(), info, 1).show();
				lv_unfinsh_order.onRefreshComplete();
				lv_unfinsh_order.onLoadComplete();
				lv_unfinsh_order.setResultSize(0);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position > 1 && position < mDatas.size() + 2) {

			TableInfo tableInfo = mDatas.get(position - 2);
			String id1 = tableInfo.getOrderNum();// 订单号
			if(type.equals("1")){// 未完成
				Intent backgass = new Intent(getActivity(),WeightActivity.class);
				backgass.putExtra("show", "DownOrderActivity");
				DownOrderActivity.changebottle = "1";
				startActivity(backgass);
			}else{
				// 跳转到订单详情界面
				Intent intent = new Intent(getContext(), ChangeOrderInfoActivity.class);
				// intent.putExtra("id", ordersn);

				intent.putExtra("id", id1);
				startActivity(intent);
			}
			
		}
	}

	@Override
	public String[] initTitles() {
		String titles[] = { "订单号", "客户姓名", "订单状态", "订单时间" };
		return titles;
	}

	@Override
	public int isOrNoSet() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean isRefush() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void initData(String type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.rb_backbottle_finish:
			type = "2";
			mDatas = new ArrayList<TableInfo>();
			initData();
			break;
		case R.id.rb_backbottle_unfinish:
			type = "1";
			mDatas = new ArrayList<TableInfo>();
			initData();
			break;

		default:
			break;
		}
	}

}
