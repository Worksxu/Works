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
import com.ruiqi.xuworks.BackShopActivity;
import com.ruiqi.xuworks.BackShopDetialsActivity;
import com.ruiqi.xuworks.BackShopNotesActivity;
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

public class BackshopNotesFragment extends OrderFragment implements OnCheckedChangeListener {
	// 区分类型1未完成,2已完成
	private OrderDao od;
	String type,status,title;//ftype类型
	public static List<Order> mData;
	private List<TableInfo> mList;
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
		rg_select.setVisibility(View.GONE);;
		type = getArguments().getString("type");
		Log.e("lllll_回库记录", type);
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
//				GrassOrder.mData = new ArrayList<Order>();
				// 请求网络
				RequestParams params = new RequestParams(
						RequestUrl.BACKLIST);
				String id = (String) SPUtils.get(getContext(),
						SPutilsKey.SHIP_ID, "error");
				params.addBodyParameter("shop_id",
						(String) SPUtils.get(getActivity(), SPutilsKey.SHOP_ID, "error"));
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
						RequestUrl.BACKLIST);
				String id = (String) SPUtils.get(getContext(),
						SPutilsKey.SHIP_ID, "error");
				params.addBodyParameter("shop_id",
						(String) SPUtils.get(getActivity(), SPutilsKey.SHOP_ID, "error"));
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
		RequestParams params = new RequestParams(RequestUrl.BACKLIST);
		params.addBodyParameter("shop_id",
				(String) SPUtils.get(getActivity(), SPutilsKey.SHOP_ID, "error"));
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
	 *           /**
	 * { "confirme_no": "tp16080851525554", "shop_id": "17", "shipper_id": "53",
	 * "shipper_name": "门店1送气工", "bottle": [ { "type_name": "5KG", "type_id":
	 * "2" } ], "bottle_data": [ { "type_num": "1", "number": "3CD4ECB9",
	 * "xinpian": "3CD4ECB9" } ], "time": "2016-08-08", "status": "0" },
	 */
	 
	private void praseData(String result) {
		Log.e("llll_changeOrder", result);

		try {
			JSONObject obj1 = new JSONObject(result);
			int resultCode = obj1.getInt("resultCode");
			if (resultCode == 1) {
				mList = new ArrayList<TableInfo>();
				if(!obj1.getString("resultInfo").equals("null")){
				JSONArray array = obj1.getJSONArray("resultInfo");
				System.out.println("未完成length=" + array.length());
				
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					 String type1 = obj.getString("ftype");// 1:空,2:重,3:折旧,0配件,4guzhang
					 Log.e("llll_pingtype", type1);
					
					String bottle = obj.getString("bottle");// 带有bottle的字符串
					String bottle_data = obj.getString("bottle_data");// 带有bottle_data的字符串
					if(type1.equals(type)){
						String ordeson = obj.getString("confirme_no");// 订单编号
						 status = obj.getString("status");// 订单状态
						if(status.equals("0")){
							 title = "未确认";
						}else{
							 title = "已确认";
						}
						String time = obj.getString("time");//订单时间
						mDatas.add(new TableInfo(ordeson,title,time));
						if(type.equals("1")){
							mList.add(new TableInfo(ordeson,title,time,bottle_data,type));
						}else if(type.equals("2")){
							mList.add(new TableInfo(ordeson,title,time,bottle_data,type));
						}else if(type.equals("3")){
							mList.add(new TableInfo(ordeson,title,time,bottle,type));
						}else if(type.equals("4")){
							mList.add(new TableInfo(ordeson,title,time,bottle_data,type));
						}else if(type.equals("0")){
							mList.add(new TableInfo(ordeson,title,time,bottle,type));
						}
					}
				}
				}else{
					lv_unfinsh_order.onRefreshComplete();
					lv_unfinsh_order.onLoadComplete();
					lv_unfinsh_order.setResultSize(0);
				}
//					mDatas.add(new TableInfo(ordersn, username, "已完成", time));
//					if (type.equals("1")) {
//						if ("1".equals(status)) {
//
//							mDatas.add(new TableInfo(ordersn, username,
//									"配送中", time));
//						}
//					} else {
//						if ("2".equals(status)) {
//						mDatas.add(new TableInfo(ordersn, username, "已完成",
//								time));
//					}
//						}

			
				// initData();
				System.out.println("mdatas=" + mDatas);
				table = new OrderTable().addData(mDatas, initTitles());
				adapter = new TabAdapter(getContext(), table);

				lv_unfinsh_order.setAdapter(adapter);
				if (isRefulsh) {
					lv_unfinsh_order.setResultSize(mList.size());
					lv_unfinsh_order.onRefreshComplete();
				} else {
					// lv_unfinsh_order.onLoadComplete();
					lv_unfinsh_order.onLoadComplete();
					lv_unfinsh_order.setSelection(position);
					lv_unfinsh_order.setResultSize(mList.size());
				}
				

			} else {
				String info = obj1.getString("resultInfo");
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

			TableInfo tableInfo = mList.get(position - 2);
			String id1 = tableInfo.getOrderNum();// 订单号
			Intent intent = new Intent(getActivity(), BackShopDetialsActivity.class);
//			intent.putExtra("id", ordersn);
			Bundle bundle = new Bundle();
			bundle.putSerializable("json", tableInfo);
			intent.putExtra("bundle", bundle);
			startActivity(intent);
//			if(type.equals("1")){// 未完成
//				Intent backgass = new Intent(getActivity(),WeightActivity.class);
//				backgass.putExtra("show", "DownOrderActivity");
//				DownOrderActivity.changebottle = "1";
//				startActivity(backgass);
//			}else{
//				// 跳转到订单详情界面
//				Intent intent = new Intent(getContext(), ChangeOrderInfoActivity.class);
//				// intent.putExtra("id", ordersn);
//
//				intent.putExtra("id", id1);
//				startActivity(intent);
//			}
			
		}
	}

	@Override
	public String[] initTitles() {
		String titles[] = { "回库单号", "订单状态", "订单时间" };
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
//		switch (checkedId) {
//		case R.id.rb_backbottle_finish:
//			type = "2";
//			initData();
//			break;
//		case R.id.rb_backbottle_unfinish:
//			type = "1";
//			initData();
//			break;
//
//		default:
//			break;
//		}
	}

}
