package com.ruiqi.xuworks;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.ruiqi.adapter.BackGassAdapter;
import com.ruiqi.adapter.ResidueGassAdapter;
import com.ruiqi.adapter.ResidueGassLvAdapter;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.bean.Weight;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.view.CustomListView;
import com.ruiqi.view.SwipeMenu;
import com.ruiqi.view.SwipeMenuCreator;
import com.ruiqi.view.SwipeMenuItem;
import com.ruiqi.view.SwipeMenuListView;
import com.ruiqi.view.SwipeMenuListView.OnMenuItemClickListener;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.NfcActivity;
import com.ruiqi.works.R;

public class BackBottleResidueGassActivity extends BaseActivity {
	private Spinner sp_number = null;
	private ResidueGassAdapter adapter;
	private EditText et_weight;
	private TextView tv_price, tv_money, tv_total, tv_commint, tv_sure,tv_yuqi;
	private SwipeMenuListView lv_toast;
	private Switch sh_select;
	private CustomListView lv_yj;
	private LinearLayout ll_yuqi;
	String number;
	String type = "2";//初始化type
	String flag = "1";//初始化type
	private BackGassAdapter ba;//押金
	double weight = 0;
	private List<Weight> mData ;
	private List<TableInfo> mList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backbottle_residue_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("押金/余气");
		tv_yuqi = (TextView) findViewById(R.id.textView_backbottle_yuqiToast);
		ll_yuqi = (LinearLayout) findViewById(R.id.ll_backbottlegass_residue);
		ll_yuqi.setOnClickListener(this);
//		ll_yuqi.setVisibility(View.GONE);
//		sh_select = (Switch) findViewById(R.id.switch_backbottle);
		
		lv_yj = (CustomListView) findViewById(R.id.listview_ll_yj);
		if(NfcActivity.mDataBottle != null&&NfcActivity.mDataBottle.size()>0){
			ba = new BackGassAdapter(BackBottleResidueGassActivity.this, NfcActivity.mDataBottle);
			lv_yj.setAdapter(ba);
//			NfcActivity.mDataRecei.get(0).getYj();
			
		}
		
		lv_toast = (SwipeMenuListView) findViewById(R.id.lv_yuqi_Toast);
	
		tv_total = (TextView) findViewById(R.id.textView_yuqiToatal);
		tv_commint = (TextView) findViewById(R.id.textView_yuqi_commint);
		
		tv_commint.setOnClickListener(this);
		tv_money = (TextView) findViewById(R.id.textView_yuqi_money);// 折现钱
	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_backbottlegass_residue:
			Intent xu_yu = new Intent(BackBottleResidueGassActivity.this,ResidueGassActivity.class);
			xu_yu.putExtra("type", "9");
			startActivityForResult(xu_yu, 9);
			break;
		case R.id.textView_yuqi_commint:
//			SPUtils.put(BackBottleResidueGassActivity.this, "null_data",
//					getJsonData(mData));
//			getlist();
						getWeight();
						Log.e("llll_type", type);
						if(type.equals("1")){
							Toast.makeText(BackBottleResidueGassActivity.this, "押金不能为空", 1).show();
						}else{
						Intent xu = new Intent(BackBottleResidueGassActivity.this,XuBottleGassCommint.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("mList", (Serializable) getlist());
						bundle.putDouble("yuqi", Double.parseDouble( tv_yuqi.getText().toString()));
						bundle.putDouble("weight", weight);
						xu.putExtra("bundle", bundle);
						startActivity(xu);}
						
			
			break;

		default:
			break;
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

	

	

	/**
	 * 获得钱数
	 */
//	double getMOney() {
//		double total = 0;
//		if (mData!= null&&mData.size() != 0) {
//			for (int i = 0; i < mData.size(); i++) {
// 
//				tv_total.setText(total + "");
//			}
//		}else{
//			total = 0;
//		}
//		return total;
//	}
	// 获得yj
  public  void  getWeight(){
	  weight = 0;
	  if (NfcActivity.mDataBottle.size() != 0) {
			for (int i = 0; i < NfcActivity.mDataBottle.size(); i++) {
				if(!TextUtils.isEmpty(NfcActivity.mDataBottle.get(i).getYj())){
					type = "2";
					Log.e("llll_nokong", NfcActivity.mDataBottle.get(i).getYj());
					weight += Double.parseDouble(NfcActivity.mDataBottle.get(i).getYj());
				}
				
				if(TextUtils.isEmpty(NfcActivity.mDataBottle.get(i).getYj())){
					type = "1";
					Log.e("llll_kong", type);
				}
			}
			}
	
  }
	
	public  List<TableInfo>  getlist(){
		mList = new ArrayList<TableInfo>();
		for (int i = 0; i < NfcActivity.mDataBottle.size(); i++) {
//			if(TextUtils.isEmpty(NfcActivity.mDataBottle.get(i).getYj())){
//				Toast.makeText(BackBottleResidueGassActivity.this, "押金不能为空", 1).show();
//				return;
//			}
			if(mData != null && mData.size()>0){
				for (int j = 0; j < mData.size(); j++) {
					if(NfcActivity.mDataBottle.get(i).getXinpian().equals(mData.get(j).getXinpian())){
						TableInfo tf = new TableInfo();
						tf.setOrderMoney(mData.get(j).getType()+"kg"+"/"+mData.get(j).getType_name()+"元");//qian
						tf.setYuqi(mData.get(j).getType());//zhongliang
						tf.setKid(mData.get(j).getType_name());//余气qian
						tf.setOrderStatus(NfcActivity.mDataBottle.get(i).getStatus());//钢印号
						tf.setOrderNum(NfcActivity.mDataBottle.get(i).getYj());//yj
						tf.setOrderTime(NfcActivity.mDataBottle.get(i).getType());//规格
						tf.setXinpian(NfcActivity.mDataBottle.get(i).getXinpian());//xinpian
						mList.add(tf);
					}
				}
			}else{
				TableInfo tf = new TableInfo();
				tf.setOrderMoney("0kg/0元");//qian
//				tf.setOrderNum(mData.get(j).getType());//zhongliang
				tf.setYuqi("0");//zhongliang
				tf.setKid("0");//余气qian
				tf.setOrderStatus(NfcActivity.mDataBottle.get(i).getStatus());//钢印号
				tf.setOrderNum(NfcActivity.mDataBottle.get(i).getYj());//yj
				tf.setOrderTime(NfcActivity.mDataBottle.get(i).getType());//规格
				tf.setXinpian(NfcActivity.mDataBottle.get(i).getXinpian());//xinpian
				mList.add(tf);
			}
//			
			
		}
		return mList;
		}
		
		
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if(arg1 == 9){
			mData = new ArrayList<Weight>();
			mData = (List<Weight>) arg2.getBundleExtra("bundle").getSerializable("yuqi_list");
			tv_yuqi.setText(arg2.getBundleExtra("bundle").getString("yuqi_money"));
		}
	}
	
}
