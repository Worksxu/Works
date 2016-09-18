package com.ruiqi.xuworks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.ruiqi.Table.OrderTable;
import com.ruiqi.Table.TabAdapter;
import com.ruiqi.Table.TabRow;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.R;

public class BackShopDetialsActivity extends BaseActivity{
	private TextView tv_number,tv_status,tv_num,tv_weight;
	private ListView lv_toast;
	public List<TabRow> table;

	private List<TableInfo> badDatas;// 故障瓶列表list
	private List<TableInfo> weightDatas;// 重瓶列表list
	private List<TableInfo> nullDatas;// 空瓶列表list
	private List<TableInfo> oldDatas;// 折旧瓶列表list
	private List<TableInfo> peijianDatas;// peijian列表list
	Bundle bundle = new Bundle();
	public TabAdapter adapter;
	int old_num;
	TableInfo tf;
	String title_weight[] = { "芯片号", "钢印号", "规格" };
	String title_null[] = { "芯片号", "钢印号", "规格" };
	String title_old[] = { "数量", "规格" };
	String peijian_old[] = { "数量", "名称" };
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.back_shop_detials_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		tf = (TableInfo) getIntent().getBundleExtra("bundle").getSerializable("json");
		tv_num = (TextView) findViewById(R.id.textView_backshop_detials_num);
		tv_status = (TextView) findViewById(R.id.textView_backshop_detials_status);
		tv_number = (TextView) findViewById(R.id.textView_backshop_detials_number);
		lv_toast = (ListView) findViewById(R.id.listView_backshop_detials);
		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub
		
		String orderson = tf.getOrderNum();
		String status = tf.getOrderMoney();
		String json = tf.getOrderTime();
		String type = tf.getKid();
		tv_number.setText("订单号:"+orderson);
		tv_status.setText(status);
		Log.e("lll", json);
		Log.e("lll", type);
		if(type.equals("2")){// 重瓶
			setTitle("重瓶回库详情");
			weightDatas = new ArrayList<TableInfo>();
			try {
				JSONArray array = new JSONArray(json);
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					String number = obj.getString("number");
					String xinpian = obj.getString("xinpian");
					String type_name = obj.getString("type_name");
					weightDatas.add(new TableInfo(number,xinpian,type_name));
				}
				
				tv_num.setText("统计:"+array.length()+"");
				table = new OrderTable().addData(weightDatas, title_weight);
				adapter = new TabAdapter(BackShopDetialsActivity.this, table);
				lv_toast.setAdapter(adapter);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else if(type.equals("3")){// 折旧
			setTitle("折旧瓶回库详情");
			oldDatas = new ArrayList<TableInfo>();
			try {
				JSONArray array = new JSONArray(json);
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					String type_num = obj.getString("type_num");
					
					String type_name = obj.getString("type_name");
					oldDatas.add(new TableInfo(type_num,type_name));
					if(array.length()>0){
						old_num += Integer.parseInt(type_num);
					}else{
						old_num = 0;
					}
				}
				
				tv_num.setText("统计:"+old_num+"");
				table = new OrderTable().addData(oldDatas, title_old);
				adapter = new TabAdapter(BackShopDetialsActivity.this, table);
				lv_toast.setAdapter(adapter);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(type.equals("1")){// 空瓶
			setTitle("空瓶回库详情");
			nullDatas = new ArrayList<TableInfo>();
			try {
				JSONArray array = new JSONArray(json);
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					String number = obj.getString("number");
					String xinpian = obj.getString("xinpian");
					String type_name = obj.getString("type_name");
					nullDatas.add(new TableInfo(number,xinpian,type_name));
				}
				
				tv_num.setText("统计:"+array.length()+"");
				table = new OrderTable().addData(nullDatas, title_null);
				adapter = new TabAdapter(BackShopDetialsActivity.this, table);
				lv_toast.setAdapter(adapter);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(type.equals("4")){// 故障
			setTitle("故障瓶回库详情");
			badDatas = new ArrayList<TableInfo>();
			try {
				JSONArray array = new JSONArray(json);
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					String number = obj.getString("number");
					String xinpian = obj.getString("xinpian");
					String type_name = obj.getString("type_name");
					badDatas.add(new TableInfo(number,xinpian,type_name));
				}
				Log.e("llll", "sdsadsad");
				tv_num.setText("统计:"+array.length()+"");
				table = new OrderTable().addData(badDatas, title_null);
				adapter = new TabAdapter(BackShopDetialsActivity.this, table);
				lv_toast.setAdapter(adapter);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();}
		}
		else{
			setTitle("配件回库详情");
			peijianDatas = new ArrayList<TableInfo>();
			try {
				JSONArray array = new JSONArray(json);
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					String number = obj.getString("type_name");
					String xinpian = obj.getString("type_num");
					String type_name = obj.getString("type_name");
					peijianDatas.add(new TableInfo(number,xinpian));
				}
				
				tv_num.setText("统计:"+array.length()+"");
				table = new OrderTable().addData(peijianDatas, peijian_old);
				adapter = new TabAdapter(BackShopDetialsActivity.this, table);
				lv_toast.setAdapter(adapter);
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
