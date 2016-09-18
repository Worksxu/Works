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
import com.ruiqi.works.GrassOrder;
import com.ruiqi.works.OrderInfoActivity;
import com.ruiqi.works.R;
import com.ruiqi.xuworks.NewOrderInfoActivity;
import com.ruiqi.xuworks.RepairInfoActivity;
import com.ruiqi.xuworks.Repairorder;

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

public class FinshRepairFragment extends OrderFragment {

	private OrderDao od;

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
				R.layout.unfinsh_fragment, null);
		lv_unfinsh_order = (AutoListView) view
				.findViewById(R.id.lv_unfinsh_order);
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
				RequestParams params = new RequestParams(RequestUrl.REPAIRLIST);
				String id = (String) SPUtils.get(getContext(),
						SPutilsKey.SHIP_ID, "error");
				params.addBodyParameter("shipper_id", id);
				params.addBodyParameter("page", "1");
//				params.addBodyParameter("type", "1");// 0 未完成 1 已完成
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
				RequestParams params = new RequestParams(RequestUrl.REPAIRLIST);
				String id = (String) SPUtils.get(getContext(),
						SPutilsKey.SHIP_ID, "error");
				params.addBodyParameter("shipper_id", id);
				params.addBodyParameter("page", page + "");
				// params.addBodyParameter("type", "1");//1 未完成 2 已完成
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

		Repairorder.mData = new ArrayList<Order>();
		RequestParams params = new RequestParams(RequestUrl.REPAIRLIST);
		String mobile = (String) SPUtils.get(getContext(), SPutilsKey.MOBILLE,
				"error");
		String ship_id = (String) SPUtils.get(getContext(), SPutilsKey.SHIP_ID,
				"error");
		params.addBodyParameter("shipper_id", ship_id);
		params.addBodyParameter("page", "1");
		// params.addBodyParameter("type", "1");//0 未完成 1 已完成
		HttpUtil.PostHttp(params, handler, pd);

		od = OrderDao.getInstances(getContext());

	}

	/**
	 * 解析数据
	 * 
	 * @param result
	 *            { "resultCode": 1, "resultInfo": [ { "id": "19", "encode_id":
	 *            "bx16080210054985", "shop_id": "1", "kid": "1", "kname":
	 *            "涂俊英", "baoxiu_wt": "[\"12\",\"15\"]", "comment": "ceshi",
	 *            "ctime": "1470134221", "treatment": null, "admin_user_id":
	 *            "0", "admin_user_name": null, "shipper_id": "18",
	 *            "shipper_name": "测试2号", "shipper_mobile": "18001364488",
	 *            "status": "1" } ] }
	 */
	private void praseData(String result) {
		Log.e("llll", result);
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if (resultCode == 1) {
				JSONArray array = obj.getJSONArray("resultInfo");
				System.out.println("未完成length=" + array.length());
				for (int i = 0; i < array.length(); i++) {
					
					StringBuffer sb = new StringBuffer();
					JSONObject obj1 = array.getJSONObject(i);
					if(obj1.getString("status").equals("2")){
					Order ord = new Order();
					
					
					Log.e("lll", obj1.toString());
					
					String ordersn = obj1.getString("encode_id");// 订单编号
					String comment = obj1.getString("comment");// 订单备注

					String time = obj1.getString("ctime");// 订单创建时间
					String date = DateUtils.TimeStampToDate(time, "yyyy-MM-dd HH:mm");
					String reeult = obj1.getString("treatment");//处理结果
					String status = obj1.getString("status");// 订单状态
					String id = obj1.getString("id");// 订单id
					String mobile = obj1.getString("mobile_phone");// 客户电话
					String address = obj1.getString("address");// 客户电话
					String username = obj1.getString("kname");// 客户姓名
					String project = obj1.getString("baoxiu_text");//报修项目
//					String username = obj1.getString("kname");// 客户姓名
					ord.setOrdersn(ordersn);
					ord.setComment(comment);
					ord.setTime(time);
					ord.setStatus(status);
					ord.setUsername(username);
					ord.setTotal(reeult);
					ord.setKid(id);
					ord.setAddress(address);
					ord.setMobile(mobile);
					ord.setDelivery(project);//保修项目
					Repairorder.mData.add(ord);
					mDatas.add(new TableInfo(ordersn, username, "已完成", date,id));
					}
				}
				// initData();
				System.out.println("mdatas=" + mDatas);
				table = new OrderTable().addData(mDatas, initTitles());
				adapter = new TabAdapter(getContext(), table);

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
		if (position > 1 && position < mDatas.size() + 2) {

			TableInfo tableInfo = mDatas.get(position - 2);
			String id1 = tableInfo.getKid();
			
			// 跳转到订单详情界面
			Intent intent = new Intent(getContext(), RepairInfoActivity.class);
//			intent.putExtra("id", ordersn);
			intent.putExtra("id", id1);
			startActivity(intent);
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

}
