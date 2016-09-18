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
import com.ruiqi.bean.MyMoneyInfo;
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
import android.widget.TableRow;
import android.widget.Toast;

public class NewMoneyOutFragment extends OrderFragment {
	private List<MyMoneyInfo> list;
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
				RequestParams params = new RequestParams(RequestUrl.ZHICHU);
				String id = (String) SPUtils.get(getContext(),
						SPutilsKey.SHIP_ID, "error");
				params.addBodyParameter("shipper_id", id);
				params.addBodyParameter("page", "1");
				// params.addBodyParameter("type", "1");// 0 未完成 1 已完成
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
				RequestParams params = new RequestParams(RequestUrl.ZHICHU);
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
		RequestParams params = new RequestParams(RequestUrl.ZHICHU);
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
	 * @param r
	 */
	private void praseData(String result) {
		Log.e("llll_zhejiu", result);
		list = new ArrayList<MyMoneyInfo>();
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				JSONObject obj1 = obj.getJSONObject("resultInfo");
				
				if(!obj1.isNull("datalist")){
					
					JSONArray array = obj1.getJSONArray("datalist");
					System.out.println("length="+array.length());
					for(int j=0;j<array.length();j++){
						JSONObject obj2 = array.getJSONObject(j);
						String order_sn = obj2.getString("paylist_no");
						String pay_money = obj2.getString("money");
						String ctime = obj2.getString("time");
						String type_name = obj2.getString("type_name");
						
						list.add(new MyMoneyInfo(order_sn, pay_money, ctime,type_name));
					}
				}
				for(int j = 0;j<list.size();j++){
					mDatas.add(new TableInfo(list.get(j).getOrdersn(),list.get(j).getType_name(),list.get(j).getPay_money(),  list.get(j).getCtime()));		
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
		
	}

	@Override
	public String[] initTitles() {
		String titles[] = {"单号","类型","金额","日期"};
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
	public interface OrderInCallBack{
		public void callBack(String a,String b,String c);
	}
	
	public OrderInCallBack callBack;
	public void setCallBack(OrderInCallBack call){
		this.callBack = call;
	}

}
