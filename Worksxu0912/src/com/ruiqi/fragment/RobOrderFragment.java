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
import com.ruiqi.adapter.RobOrderAdapter;
import com.ruiqi.bean.Order;
import com.ruiqi.bean.Orderdeail;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.db.OrderDao;
import com.ruiqi.utils.DateUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.IsPhone;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.view.AutoListView;
import com.ruiqi.view.AutoListView.OnLoadListener;
import com.ruiqi.view.AutoListView.OnRefreshListener;
import com.ruiqi.works.R;
import com.ruiqi.xuworks.NewOrderInfoActivity;
import com.ruiqi.xuworks.SelfCheckActivity;

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
import android.widget.TableRow;
import android.widget.Toast;

public class RobOrderFragment extends OrderFragment implements ParserData{

	private OrderDao od;
	String orderson,kid;
	private RobOrderAdapter adapter;
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

	@Override
	public View initView() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.roborder_fragment, null);
		lv_unfinsh_order = (AutoListView) view
				.findViewById(R.id.lv_unfinsh_order);
		pd = new ProgressDialog(getContext());
		pd.setMessage("正在加载");
		initData();
//		table = new OrderTable().addData(mDatas, initTitles());
//
//		adapter = new RobOrderAdapter(getContext(), table);

		

		// lv_unfinsh_order.setResultSize(mDatas.size());
//		lv_unfinsh_order.setOnItemClickListener(this);
		// 下拉刷新
		lv_unfinsh_order.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				isRefulsh = true;
				page = 1;
				mDatas = new ArrayList<TableInfo>();
				mData = new ArrayList<Order>();
				// 请求网络
				RequestParams params = new RequestParams(RequestUrl.ROBORDER);
				String id = (String) SPUtils.get(getContext(),
						SPutilsKey.SHIP_ID, "error");
				params.addBodyParameter("shipper_id", id);
//				 params.addBodyParameter("page", "1");
				// params.addBodyParameter("type", "2");// 2置换
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
				RequestParams params = new RequestParams(RequestUrl.ROBORDER);
				String id = (String) SPUtils.get(getContext(),
						SPutilsKey.SHIP_ID, "error");
				params.addBodyParameter("shipper_id", id);
				 params.addBodyParameter("page", page + "");
				// params.addBodyParameter("type", "2");//1 未完成 2 已完成
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
		RequestParams params = new RequestParams(RequestUrl.ROBORDER);
		String mobile = (String) SPUtils.get(getContext(), SPutilsKey.MOBILLE,
				"error");
		String ship_id = (String) SPUtils.get(getContext(), SPutilsKey.SHIP_ID,
				"error");
		params.addBodyParameter("shipper_id", ship_id);
		// params.addBodyParameter("page", "1");
		// params.addBodyParameter("type", "2");//0
		HttpUtil.PostHttp(params, handler, pd);

		od = OrderDao.getInstances(getContext());

	}

	/**
	 * 解析数据
	 * 
	 * @param result
	 *            { "resultCode": 1, "resultInfo": [ "order_sn":
	 *            "Dd160809525748472", "sheng_name": "\u6ca7\u5dde", "shi_name":
	 *            "\u8fd0\u6cb3\u533a", "qu_name":
	 *            "\u6c34\u6708\u5bfa\u8857\u9053", "cun_name":
	 *            "\u5927\u5316\u793e\u533a\r", "address":
	 *            "\u6ca7\u5dde\u8fd0\u6cb3\u533a\u6c34\u6708\u5bfa\u8857\u9053\u6c34\u5316\u793e\u533a\u56db\u60e0\u5927\u53a6113"
	 *            , "time": "2016-08-09", "username": "admin15", "money":
	 *            "122.00", "kh_name": "\u5c45\u6c11\u7528\u6237" },
	 * 
	 */
	private void praseData(String result) {
		Log.e("llll_抢单池", result);

		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if (resultCode == 1) {
				JSONArray array = obj.getJSONArray("resultInfo");

				 for (int i = 0; i < array.length(); i++) {
				
				 StringBuffer sb = new StringBuffer();
				 JSONObject obj1 = array.getJSONObject(i);
				
				 Order ord = new Order();
				
				 Log.e("lll", obj1.toString());
				
				 String ordersn = obj1.getString("order_sn");// 订单编号
				
				 
				 String time = obj1.getString("time");// 订单创建时间
				 
				
				 String status = obj1.getString("kh_name");//用户类型
				
				 String username = obj1.getString("username");// 客户姓名
				 String money = obj1.getString("money");
				 String address = obj1.getString("address");
				String kid = obj1.getString("kid");
				 ord.setOrdersn(ordersn);
				 
				 ord.setTime(time);
				 ord.setStatus(status);
				 ord.setAddress(address);
				 ord.setPay_money(money);
				 ord.setKid(kid);
				 ord.setUsername(username);
				 
				 mData.add(ord);
				
				
				 }
				 adapter = new RobOrderAdapter(getContext(), mData);
				 lv_unfinsh_order.setAdapter(adapter);
				 if (isRefulsh) {
						lv_unfinsh_order.setResultSize(array.length());
						lv_unfinsh_order.onRefreshComplete();
					} else {
						// lv_unfinsh_order.onLoadComplete();
						lv_unfinsh_order.onLoadComplete();
						lv_unfinsh_order.setSelection(position);
						lv_unfinsh_order.setResultSize(array.length());
					}
			}else{
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

		// 跳转到订单详情界面
		// Intent intent = new Intent(getContext(), .class);
		// intent.putExtra("id", ordersn);

		// intent.putExtra("id", id1);
		// startActivity(intent);
		orderson = mData.get(position-1).getOrdersn();
		kid = mData.get(position-1).getKid();
//		Log.e("fsafsfsafas", mData.get(position).getOrdersn());
		getData(orderson,kid);

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
	private HttpUtil httpUtil;
	private void getData(String ordeson,String kid) {
		// TODO Auto-generated method stub
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
//		int token = (Integer) SPUtils.get(SelfCheckActivity.this, SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.ROBORDERSURE);
//		params.addBodyParameter("type", (String) SPUtils.get(SelfCheckActivity.this, "self_type", ""));
		params.addBodyParameter("shipper_id",  (String) SPUtils.get(getActivity(),
				SPutilsKey.SHIP_ID, "error"));
		params.addBodyParameter("shipper_mobile",   (String) SPUtils.get(getActivity(),
				SPutilsKey.MOBILLE, "error"));
		params.addBodyParameter("shipper_name",  (String) SPUtils.get(getActivity(),
				"shipper_name", "error"));
		params.addBodyParameter("ordersn",  ordeson);
		httpUtil.PostHttp(params, 0);
	}

	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("llllll", result);
		try {
			JSONObject object = new JSONObject(result);
			int code = object.getInt("resultCode");
			String info = object.getString("resultInfo");
			if(code == 1){
				Toast.makeText(getContext(), info, 1).show();
				Intent order = new Intent(getActivity(),NewOrderInfoActivity.class);
				order.putExtra("ordersn", orderson);
				order.putExtra("kid", kid);
				startActivity(order);
			}else{
				Toast.makeText(getContext(), info, 1).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
