package com.ruiqi.xuworks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.ruiqi.adapter.DeductionAdapter;
import com.ruiqi.adapter.DeductionAdapter.ViewHolder;
import com.ruiqi.bean.Type;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.works.AddPeijianActivity;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.R;

@SuppressLint("NewApi")
public class DeductionActivity extends BaseActivity implements ParserData,OnCheckedChangeListener{
	private ListView lv_deduction;
	private TextView tv_sure;
	private RadioGroup rg_deducation;
	private RadioButton rb_use,rb_nouse;
	private ArrayList<Type > mList = new ArrayList<Type>();
	private ArrayList<Type > allList = new ArrayList<Type>();
	private ArrayList<Type > list = new ArrayList<Type>();
	private ArrayList<Integer> idList = new ArrayList<Integer>();
	HashMap<String, Integer> info = new HashMap<String, Integer>();
	
	private DeductionAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.deducation_layout);
		init();
	}


	private void init() {
		// TODO Auto-generated method stub
		setTitle("用户优惠券");
		Bundle bundle = new Bundle();
		rg_deducation = (RadioGroup) findViewById(R.id.rg_select);
		rg_deducation.setOnCheckedChangeListener(this);
		rb_use = (RadioButton) findViewById(R.id.rb_deducation_select);
		rb_nouse = (RadioButton) findViewById(R.id.rb_deducation_all);
		mList = (ArrayList<Type>) getIntent().getBundleExtra("select").getSerializable("select");
		allList = (ArrayList<Type>) getIntent().getBundleExtra("select").getSerializable("all");
		rb_nouse.setText("全部优惠券"+allList.size()+""+"张");
		rb_use.setText("可使用优惠券"+mList.size()+""+"张");
		lv_deduction = (ListView) findViewById(R.id.listView_deducation);
		tv_sure = (TextView) findViewById(R.id.tv_deducation_sure);
		tv_sure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				sure();
			}
		});
//		if(SPUtils.get(DeductionActivity.this, "deducation", "").equals("")){
		Log.e("lll", "111");
		adapter = new DeductionAdapter(DeductionActivity.this, mList);
		lv_deduction.setAdapter(adapter);
		lv_deduction.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ViewHolder holder = (ViewHolder) view.getTag();
				holder.cb_select.toggle();
				// 将CheckBox的选中状况记录下来
				adapter.getIsSelected().put(position, holder.cb_select.isChecked());
			}
		});
		
//		try {
//			JSONArray array = new JSONArray((String)SPUtils.get(DeductionActivity.this, "deduction", ""));
//			for (int i = 0; i < array.length(); i++) {
//				
//				JSONObject object = array.getJSONObject(i);
//				Type type = new Type();
//				type.setPrice(object.getString("price"));
//				type.setWeight(object.getString("title"));
//				
//				type.setId(object.getString("id"));
//				if(info.containsKey(object.getString("price"))){
//					info.put(object.getString("price"), info.get(object.getString("price"))+1);
//				}else{
//					info.put(object.getString("price"), 1);
//				}
//				type.setNum(info.get(object.getString("price"))+"");
//				mList.add(type);
//				
//			}
//			Log.e("lll", mList.toString());
//			
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		
		
		
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

	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		
	}
	private double deduction_money;
	public void sure(){
		 HashMap<Integer, Boolean> map = DeductionAdapter.getIsSelected();
		 info = new HashMap<String, Integer>();
//		 int id[]  = new int[map.size()];
		 deduction_money = 0;
		 for (int i = 0; i < map.size(); i++) {
			 if(map.get(i) == true){
//				 
				idList.add(Integer.parseInt(mList.get(i).getId()));

			
				
				 if(info.containsKey(mList.get(i).getWeight())){
						info.put(mList.get(i).getWeight(), info.get(mList.get(i).getWeight())+1);
					}else{
						info.put(mList.get(i).getWeight(), 1);
					}
				 Log.e("lllljhdfjhlfd", info.get(mList.get(i).getWeight())+"");
				 if(map.size()>0){
						
					 deduction_money += Double.parseDouble(mList.get(i).getPrice())*Integer.parseInt(mList.get(i).getNum());
					 Log.e("dsgdsg_选中的数量", mList.get(i).getNum());
				 }
			 }
			
			
		}
//		 Log.e("llll", info.get(key));
//		 JSONArray array = JSONArray.optJSONObject(id);
//		 Log.e("lll", id.toString());
		//取得map中所有的key和value  放到list里
	        for(Map.Entry<String, Integer> entry : info.entrySet()) {  
	            String key = entry.getKey();  
	            Integer value = entry.getValue();  
	            Type type = new Type();
	            type.setName(key);
	            type.setNum(value+"");
	            list.add(type);
	            
	        }  
	       
	        Log.e("llll数量", idList.size()+"");
//	        if(idList.size() == 0){
//	        	Toast.makeText(DeductionActivity.this, "请选择优惠/余气券", 1).show();
//	        }else{
		 Bundle bundle = new Bundle();
			bundle.putSerializable("deduction", (Serializable) list);
			bundle.putSerializable("idList", (Serializable) idList);
			bundle.putDouble("deduction_money", deduction_money);
			Intent intent = new Intent();
			intent.putExtra("deduction", bundle);
			DeductionActivity.this.setResult(2, intent);
			finish();
//			}
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.rb_deducation_select:
			tv_sure.setVisibility(View.VISIBLE);
//			mList = new ArrayList<Type>();
			adapter = new DeductionAdapter(DeductionActivity.this, mList);
			lv_deduction.setAdapter(adapter);
			lv_deduction.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					ViewHolder holder = (ViewHolder) view.getTag();
					holder.cb_select.toggle();
					// 将CheckBox的选中状况记录下来
					adapter.getIsSelected().put(position, holder.cb_select.isChecked());
				}
			});
			break;
		case R.id.rb_deducation_all:
			
			adapter = new DeductionAdapter(DeductionActivity.this, allList);
			lv_deduction.setAdapter(adapter);
			lv_deduction.setClickable(false);
			tv_sure.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

}
