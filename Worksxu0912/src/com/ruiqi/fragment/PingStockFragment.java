package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.Table.OrderTable;
import com.ruiqi.Table.TabAdapter;
import com.ruiqi.Table.TabAdapterNoTitle;
import com.ruiqi.Table.TabRow;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.XuDialog;
import com.ruiqi.utils.XuDialog.No;
import com.ruiqi.utils.XuDialog.Yes;
import com.ruiqi.works.HomePageActivity;
import com.ruiqi.works.R;
import com.ruiqi.xuworks.CheckBottleInfoActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 钢瓶库存fragment
 * 
 * @author Administrator
 *
 */
public class PingStockFragment extends BaseFragment implements ParserData {

	private ListView lv_content, lv_content_null, lv_content_not,lv_content_bad;

	private List<TabRow> table;

	private List<TableInfo> mWeightDatas;// 重瓶的数据
	private List<TableInfo> mNullDatas;// 空瓶的数据
	public static List<TableInfo> mDatas;// 折旧瓶的数据
	public static List<TableInfo> badDatas;// 故障瓶瓶的数据

	private TabAdapterNoTitle adapter;

	private TextView tv_weight;
	private TextView tv_null;
	private TextView tv_not;
	private TextView tv_bad;
	String typeid, title;
	int num;
	int num_old;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}

	};

	@Override
	public View initView() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.ping_stock_fragment, null);

		tv_weight = (TextView) view.findViewById(R.id.tv_weight);
		tv_null = (TextView) view.findViewById(R.id.tv_null);
		tv_not = (TextView) view.findViewById(R.id.tv_not);
		tv_bad = (TextView) view.findViewById(R.id.tv_bad);

		lv_content = (ListView) view.findViewById(R.id.lv_content);
		lv_content_null = (ListView) view.findViewById(R.id.lv_content_null);
		lv_content_not = (ListView) view.findViewById(R.id.lv_content_not);
		lv_content_bad = (ListView) view.findViewById(R.id.lv_content_bad);

		initData();
		// mWeightDatas = initData(mWeightDatas);
		/*
		 * mNullDatas = initData(mNullDatas); mDatas = initData(mDatas);
		 * 
		 * setDataToListView(mWeightDatas,tv_weight,lv_content);
		 * setDataToListView(mNullDatas,tv_null,lv_content_null);
		 * setDataToListView(mDatas,tv_not,lv_content_not);
		 */
		return view;
	}

	private void paraseData(String result) {
		System.out.println("pingresult=" + result);
		Log.e("lll", result);
		// json解析
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if (resultCode == 1) {
				JSONObject obj1 = obj.getJSONObject("resultInfo");
				String total = obj1.getString("total");
				JSONArray array = obj1.getJSONArray("data");
				for (int i = 0; i < array.length(); i++) {
					System.out.println("==================");
					JSONObject object = array.getJSONObject(i);
					title = object.getString("title");
					String typename = object.getString("typename");
					typeid = object.getString("typeid");
					num = object.getInt("num");
					String kind = object.getString("is_open");// 规格id
					if ("重瓶".equals(typename)) {
						mWeightDatas
								.add(new TableInfo(title, num + "", typeid));
					} else if ("空瓶".equals(typename)) {
						mNullDatas.add(new TableInfo(title, num + "", typeid));
					} else if("待修瓶".equals(typename)){
						badDatas.add(new TableInfo(title, num + "", typeid));
					}else
					{
						mDatas.add(new TableInfo(title, num + "", typeid, kind));
					}

				}

				System.out.println("mWeightDatas=" + mWeightDatas);
				System.out.println("mNullDatas=" + mNullDatas);
				System.out.println("mDatas=" + mDatas);

				setDataToListView(mWeightDatas, tv_weight, lv_content);
				setDataToListView(mNullDatas, tv_null, lv_content_null);
				setDataToListView(mDatas, tv_not, lv_content_not);
				setDataToListView(badDatas, tv_bad, lv_content_bad);

				CurrencyUtils.setListViewHeightBasedOnChildren(lv_content);
				CurrencyUtils.setListViewHeightBasedOnChildren(lv_content_null);
				CurrencyUtils.setListViewHeightBasedOnChildren(lv_content_not);
				CurrencyUtils.setListViewHeightBasedOnChildren(lv_content_bad);
				lv_content.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						Log.e("lll", mWeightDatas.get(position).getOrderNum());
						Intent weight = new Intent(getActivity(),
								CheckBottleInfoActivity.class);
						weight.putExtra("typeid", mWeightDatas.get(position)
								.getOrderStatus());
						weight.putExtra("num", mWeightDatas.get(position)
								.getOrderMoney());// 数量
						weight.putExtra("typename", mWeightDatas.get(position)
								.getOrderNum());// 规格名字
						startActivity(weight);
					}
				});
				lv_content_null
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// TODO Auto-generated method stub
//								Log.e("lll", mWeightDatas.get(position)
//										.getOrderNum());
								Intent weight = new Intent(getActivity(),
										CheckBottleInfoActivity.class);
								weight.putExtra("typeid",
										mNullDatas.get(position)
												.getOrderStatus());
								weight.putExtra("num", mNullDatas.get(position)
										.getOrderMoney());// 数量
								weight.putExtra("typename",
										mNullDatas.get(position).getOrderNum());
								startActivity(weight);
							}
						});
				lv_content_bad
				.setOnItemClickListener(new OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
//								Log.e("lll", mWeightDatas.get(position)
//										.getOrderNum());
						Intent weight = new Intent(getActivity(),
								CheckBottleInfoActivity.class);
						weight.putExtra("typeid",
								badDatas.get(position)
								.getOrderStatus());
						weight.putExtra("num", badDatas.get(position)
								.getOrderMoney());// 数量
						weight.putExtra("typename",
								badDatas.get(position).getOrderNum());
						startActivity(weight);
					}
				});
			

			}
		} catch (Exception e) {
		}
	}

	private void initData() {
		mWeightDatas = new ArrayList<TableInfo>();
		mNullDatas = new ArrayList<TableInfo>();
		mDatas = new ArrayList<TableInfo>();
		badDatas = new ArrayList<TableInfo>();
		String shipid = (String) SPUtils.get(getContext(), SPutilsKey.SHIP_ID,
				"error");
		RequestParams params = new RequestParams(RequestUrl.STOCK_LIST);
		params.addBodyParameter("shipper_id", shipid);
		HttpUtil.PostHttp(params, handler, new ProgressDialog(getContext()));

	}

	private void setDataToListView(List<TableInfo> datas, TextView tv,
			ListView lv) {
		table = new OrderTable().addDataNoTitle(datas);

		adapter = new TabAdapterNoTitle(getContext(), table, tv, null);

		lv.setAdapter(adapter);
	}

	

	@Override
	public void sendResult(String result, int what) {}

	

}
