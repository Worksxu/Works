package com.ruiqi.xuworks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.ruiqi.bean.Order;
import com.ruiqi.db.OrderDao;
import com.ruiqi.fragment.UnfinshFragment;
import com.ruiqi.fragment.UnfinshRepairFragment;
import com.ruiqi.fragment.ZheJiuFinishFragment;
import com.ruiqi.works.BaseOrderActivity;

public class ZheJiuOrderListActivity extends BaseOrderActivity {
	private ProgressDialog pd;
	
	
	private boolean flag=true;
	
	public static List<Order> mData;
	private  List<Order> mList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		pd = new ProgressDialog(this);
		pd.setMessage("正在加载......");
	
		setTitle("折旧订单");
		//rg_select.setOnCheckedChangeListener(this);
		initData();
		setFragment();
	}

	private void initData() {
		// TODO Auto-generated method stub
		mData = new ArrayList<Order>();
		mList = new ArrayList<Order>();
	}

	@Override
	public Fragment initFragment() {
		// TODO Auto-generated method stub
		ZheJiuFinishFragment ufh = new ZheJiuFinishFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("mData", (Serializable) mList);
		ufh.setArguments(bundle);
		return ufh;
	}

	@Override
	public int initInteger() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public List<Order> initListData() {
		// TODO Auto-generated method stub
		return mData;
	}

	@Override
	public Activity getActivity() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public Handler[] initHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	

	

}
