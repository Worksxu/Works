package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.ProgressDialog;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruiqi.Table.OrderTable;
import com.ruiqi.Table.TabAdapter;
import com.ruiqi.Table.TabRow;
import com.ruiqi.bean.MyMoneyInfo;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.works.R;

/**
 * 收入的fragment
 * @author Administrator
 *
 */
public class MoneyInFragment extends BaseFragment implements OnCheckedChangeListener{

	
	private RadioGroup rg_select;
	
	private ListView lv_content;
	
	public List<TabRow> table;
	
	public List<TableInfo> mDatas;
	
	public TabAdapter adapter;
	
	private RelativeLayout rl_tabfragment;
	
	private List<MyMoneyInfo> list;
	
	private TextView tv_home_hours_count,tv_home_orders_count,tv_home_paimings_count;
	
	private ProgressDialog pd;
	
	private Handler inhandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result,1);
		}

		
	};
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result,2);
		};
	};
	
	@Override
	public View initView() {
		
		View view = LayoutInflater.from(getContext()).inflate(R.layout.money_in_fragment, null);
		
		pd = new ProgressDialog(getContext());
		pd.setMessage("正在加载......");
		
		shipper_id = (String) SPUtils.get(getContext(), SPutilsKey.SHIP_ID, "error");
		shop_id = (String) SPUtils.get(getContext(), SPutilsKey.SHOP_ID, "error");
		
		rg_select = (RadioGroup) view.findViewById(R.id.rg_select);
		lv_content= (ListView) view.findViewById(R.id.lv_content);
		rl_tabfragment = (RelativeLayout) view.findViewById(R.id.rl_tabfragment);
		rg_select.setOnCheckedChangeListener(this);
		initData();
		
		
		return view;
	}
	//收缴欠款的表头
	
	private String[] initTitles() {
		String [] titles = {"名称","金额","日期"};
		return titles;
	}
	//订单收入的欠款
	private String[] initTitlesOrder() {
		String [] titles = {"单号","类型","金额","日期"};
		return titles;
	}
	private String shipper_id;
	private String shop_id;
	//订单收入数据初始化
	private void initData() {
		pd.show();
		list = new ArrayList<MyMoneyInfo>();
		mDatas = new ArrayList<TableInfo>();
		
		RequestParams params = new RequestParams(RequestUrl.SHOURU);
		params.addBodyParameter("shipper_id", shipper_id);
		params.addBodyParameter("page", "1");
		HttpUtil.PostHttp(params, handler, pd);
	}
	public List<TableInfo> mList;//收缴欠款的数据
	//收缴欠款数据初始化
	private void initDataQian(){
		pd.show();
		mList = new ArrayList<TableInfo>();
		RequestParams params = new RequestParams(RequestUrl.SHIPPER_ARREARS);
		params.addBodyParameter("shipper_id", shipper_id);
		params.addBodyParameter("shop_id", shop_id);
		HttpUtil.PostHttp(params, inhandler, pd);
		System.out.println("shipper_id="+shipper_id);
		
	}
	
	public interface OrderInCallBack{
		public void callBack(String a,String b,String c);
	}
	public OrderInCallBack callBack;
	public void setCallBack(OrderInCallBack call){
		this.callBack = call;
	}
	
	private void paraseData(String result, int i) {
		System.out.println("result_收入="+result);
		if(i==2){
			try{
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				JSONObject obj1 = obj.getJSONObject("resultInfo");
				String stotal = obj1.getString("stotal");
				String ztotal = obj1.getString("ztotal");
				String ytotal = obj1.getString("ytotal");
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
				callBack.callBack(stotal, ztotal, ytotal);
				setAdapterToListView(mDatas,initTitlesOrder());
			}else{
				
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
			}
		}
//		
		
	}
	
	private void setAdapterToListView(List<TableInfo> mData,String [] titles){
		table = new OrderTable().addData( mData, titles);
		
		adapter = new TabAdapter(getContext(), table);
		
		lv_content.setAdapter(adapter);
		
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_in://订单收入
			initData();
			rl_tabfragment.removeAllViews();
			rl_tabfragment.addView(lv_content);
			setAdapterToListView(mDatas,initTitlesOrder());
			break;
		case R.id.rb_qiankuan://收缴欠款
			initDataQian();
			rl_tabfragment.removeAllViews();
			rl_tabfragment.addView(lv_content);
			setAdapterToListView(mList,initTitles());
			break;

		default:
			break;
		}
	}

	

}
