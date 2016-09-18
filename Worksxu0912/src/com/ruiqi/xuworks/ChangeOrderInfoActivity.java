package com.ruiqi.xuworks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.ruiqi.Table.OrderTable;
import com.ruiqi.Table.TabAdapter;
import com.ruiqi.Table.TabRow;
import com.ruiqi.bean.Bottle;
import com.ruiqi.bean.Order;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.bean.Weight;
import com.ruiqi.fragment.BackBottleDeails;
import com.ruiqi.fragment.ChangeOrderFinishFragment;
import com.ruiqi.fragment.ZhejiuBottleDeails;
import com.ruiqi.view.CustomListView;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.R;

public class ChangeOrderInfoActivity extends BaseActivity{
	private TextView tv_number, tv_finishtime, tv_username, tv_userphone,
	tv_address, tv_shopname, tv_worker, tv_backtime, tv_yajin, tv_yuqi,
	tv_pay;
	private CustomListView lv_good,lv_bad;
	String ordeson,data,money,change_data;
	double zhejiu = 0;
	private List<TableInfo> mlist;//故障瓶信息
	private List<TableInfo> list;//置换瓶信息
	public TabAdapter adapter;
	public List<TabRow> table;
	String title_weight[] = { "芯片号", "规格", "钢印号" };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changeorder_info_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("置换详情");
		tv_address = (TextView) findViewById(R.id.textView_finishbottle_address);
		ordeson = getIntent().getExtras().getString("id");// 定单号
		
		lv_good = (CustomListView) findViewById(R.id.listView_changeorderinfo_good);
		lv_bad = (CustomListView) findViewById(R.id.listView_changeorderinfo_bad);
		tv_userphone = (TextView) findViewById(R.id.textView_finishbottle_userphone);
		tv_username = (TextView) findViewById(R.id.textView_finishbottle_username);
	
	
		tv_number = (TextView) findViewById(R.id.textView_finishbottle_number);
		tv_finishtime = (TextView) findViewById(R.id.textView_finishbottle_finishtime);
		
		if(ChangeOrderFinishFragment.mData != null&&ChangeOrderFinishFragment.mData.size()>0){
			mlist = new ArrayList<TableInfo>();
			list = new ArrayList<TableInfo>();
			for (int i = 0; i < ChangeOrderFinishFragment.mData.size(); i++) {
				if(ordeson.equals(ChangeOrderFinishFragment.mData.get(i).getOrdersn())){
					String time = ChangeOrderFinishFragment.mData.get(i).getTime();
					String mobile = ChangeOrderFinishFragment.mData.get(i).getMobile();
					money = ChangeOrderFinishFragment.mData.get(i).getPay_money();
//					String weight = ChangeOrderFinishFragment.mData.get(i).getKid();
					String address = ChangeOrderFinishFragment.mData.get(i).getAddress();
					String username = ChangeOrderFinishFragment.mData.get(i).getUsername();
					data = ChangeOrderFinishFragment.mData.get(i).getComment();//故障串
					change_data = ChangeOrderFinishFragment.mData.get(i).getKid();//重瓶串
					tv_address.setText(address);
					tv_finishtime.setText(time);
					tv_number.setText(ordeson);
					tv_userphone.setText(mobile);
					tv_username.setText(username);
					
				}
			}
			try {
				JSONArray array = new JSONArray(data);
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					String xinpian = object.getString("xinpian");
					String number = object.getString("number");// 钢印号
					String type = object.getString("good_name");//规格
					mlist.add(new TableInfo(xinpian,type,number));
//					zhejiu += Integer.parseInt(num)*Double.parseDouble(money);
//					tv_yajin.setText(zhejiu+"");
//					tv_yuqi.setText((Double.parseDouble(money)-zhejiu)+"");
				}
				table = new OrderTable().addData(mlist, title_weight);
				adapter = new TabAdapter(ChangeOrderInfoActivity.this, table);
				lv_bad.setAdapter(adapter);
//				ZhejiuBottleDeails b = new ZhejiuBottleDeails();
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("mData", (Serializable)mlist);
//				b.setArguments(bundle);
//				
//				getSupportFragmentManager().beginTransaction().replace(R.id.ll_finishbottle_toast, b).commit();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				JSONArray arr1 = new JSONArray(change_data);
				for (int i = 0; i < arr1.length(); i++) {
					JSONObject object = arr1.getJSONObject(i);
					String xinpian = object.getString("xinpian");
					String number = object.getString("number");// 钢印号
					String type = object.getString("good_name");//规格
					list.add(new TableInfo(xinpian,type,number));
//					
				}
				table = new OrderTable().addData(list, title_weight);
				adapter = new TabAdapter(ChangeOrderInfoActivity.this, table);
				lv_good.setAdapter(adapter);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

}
