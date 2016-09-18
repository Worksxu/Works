package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.adapter.DeductionAdapter;
import com.ruiqi.adapter.ReceideailAdapter;
import com.ruiqi.adapter.ReceideailAdapter.ViewHolder;
import com.ruiqi.bean.NopayDetail;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.bean.Type;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.XuDialog;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.XuDialog.No;
import com.ruiqi.utils.XuDialog.Yes;
import com.ruiqi.view.SwipeMenuListView;
import com.ruiqi.works.ArrearsDeails;
import com.ruiqi.works.ArrearsUserActivity;
import com.ruiqi.works.CommintOrder;
import com.ruiqi.works.HomePageActivity;
import com.ruiqi.works.R;
import com.ruiqi.xuworks.SelfCheckActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * qian款详情表格fragment
 * @author Administrator
 *
 */
public class ReceiDeailFragment extends BaseFragment implements ParserData{
	private ProgressDialog pd;
	private ListView lv_select;
	 ReceideailAdapter adapter;
	private ArrayList<NopayDetail> arrayList;
	private ArrayList<NopayDetail> List;
	private TextView tv_next;
	HashMap<NopayDetail, Integer> info = new HashMap<NopayDetail, Integer>();
	String kid;
	double money;
	@Override
	public View initView() {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.receideailfragment_layout, null);
		pd = new ProgressDialog(getActivity());
		lv_select = (ListView) view.findViewById(R.id.lv_receideail_fragment);
		tv_next = (TextView) view.findViewById(R.id.tv_receideail_next);
		tv_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getSure();
				
				
			}
		});
		kid = getArguments().getString("kid");
		getData();
		return view;
	}

	
	private HttpUtil httpUtil;
	private void getData() {
		// TODO Auto-generated method stub
		pd.show();
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(getActivity(), SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.RECEIDEAIL);
//		params.addBodyParameter("type", (String) SPUtils.get(SelfCheckActivity.this, "self_type", ""));
		params.addBodyParameter("Token",  token+"");
		params.addBodyParameter("kid",  kid);
		httpUtil.PostHttp(params, 0);
	}
	private void commintData() {// 交欠款
		// TODO Auto-generated method stub
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(getActivity(), SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.ADDREPAYMENT);

		params.addBodyParameter("Token",  token+"");
		params.addBodyParameter("kid",  kid);
		params.addBodyParameter("shipper_name",  (String) SPUtils.get(getActivity(), "shipper_name",
				"error"));
		params.addBodyParameter("shipper_mobile",  (String) SPUtils.get(getActivity(),
				SPutilsKey.MOBILLE, "error"));
		params.addBodyParameter("money",  money+"");
		params.addBodyParameter("type",  "4");
		params.addBodyParameter("shipper_id",  (String) SPUtils.get(getActivity(), SPutilsKey.SHIP_ID, ""));
		params.addBodyParameter("contractno",  getJSON(List));
		Log.e("lll_hetong", params.getStringParams().toString());
		httpUtil.PostHttp(params, 1);
	}
	/**
	 *  "resultCode": 1,
    "resultInfo": [
        {
            "order_sn": "Dd160804575649472",
            "money": "40",
            "contractno": "UUI",
            "arrears_type": "1",
            "time": "1470275416",
            "status": "1",
            "time_list": "2016-08-04",
            "username": null,
            "typename": "商品欠款"
        },
	 */
	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("llll", result);
		if(what == 0){
			try {
				JSONObject object = new JSONObject(result);
				int code = object.getInt("resultCode");
				if(code == 1){
				pd.dismiss();
				arrayList = new ArrayList<NopayDetail>();
				JSONArray array = object.getJSONArray("resultInfo");
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					NopayDetail nd = new NopayDetail();
					nd.setCtime(obj.getString("arrears_type"));
					nd.setMoney(obj.getString("money"));
					nd.setOrder_sn(obj.getString("contractno"));// 合同号
					nd.setUsername(obj.getString("typename"));// 欠款名
					nd.setTime_list(obj.getString("time_list"));//时间
					arrayList.add(nd);
				}
				if(arrayList.size() ==0){
					tv_next.setVisibility(View.GONE);
				}
				adapter = new ReceideailAdapter(getActivity(), arrayList);
				lv_select.setAdapter(adapter);
				lv_select.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						ViewHolder holder = (ViewHolder) view.getTag();
						holder.cb_recei.toggle();
						// 将CheckBox的选中状况记录下来
						adapter.getIsSelected().put(position, holder.cb_recei.isChecked());
					}
				});
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(what == 1){
			try {
				JSONObject object = new JSONObject(result);
				int code = object.getInt("resultCode");
				String info = object.getString("resultInfo");
				if(code == 1){
					Intent intent = new Intent(getActivity(),ArrearsUserActivity.class);//
					startActivity(intent);
					Toast.makeText(getActivity(), info, 1).show();
				}else{
					Toast.makeText(getActivity(), info, 1).show();
					
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	 void getSure(){
		 money = 0;
		 List = new ArrayList<NopayDetail>();
		 HashMap<Integer, Boolean> map = ReceideailAdapter.getIsSelected();
		 info = new HashMap<NopayDetail, Integer>();
		 if(map.size()>0){
		 for (int i = 0; i < map.size(); i++) {
			 if(map.get(i)){
//			

				 if(map.size()>0){
					
					money += Double.parseDouble(arrayList.get(i).getMoney());
				 }
				info.put(arrayList.get(i), i);
			
				
			 }
			
		}}
		 for(Entry<NopayDetail, Integer> entry : info.entrySet()) {  
			 NopayDetail key = entry.getKey();  
	             List.add(key);
	           
	        }  
		 XuDialog.getInstance().show(getActivity(), "收缴欠款:"+money+"", 1);
			XuDialog.getInstance().setno(new No() {
				
				@Override
				public void XuNo(int i) {
					// TODO Auto-generated method stub
					
				}
			});
			XuDialog.getInstance().setyes(new Yes() {
				
				@Override
				public void XuCallback(int i) {
					// TODO Auto-generated method stub
					if(money ==0){
						Toast.makeText(getActivity(), "上缴款项不能为0", 1).show();
					}else{
					commintData();}
				}
			});
	 }
	 public String getJSON(ArrayList<NopayDetail> list){
			JSONArray array = new JSONArray();
			for (int i = 0; i < list.size(); i++) {
				JSONObject object = new JSONObject();
				try {
					object.put("contractno", list.get(i).getOrder_sn());
					object.put("arrears_type", list.get(i).getCtime());// 型号
					object.put("money", list.get(i).getMoney());
					array.put(object);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return array.toString();
		}
	

}
