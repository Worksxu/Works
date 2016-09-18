package com.ruiqi.xuworks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.ruiqi.bean.Order;
import com.ruiqi.db.OrderDao;
import com.ruiqi.fragment.ChangeOrderFinishFragment;
import com.ruiqi.fragment.RobOrderFragment;
import com.ruiqi.fragment.UnfinshFragment;
import com.ruiqi.fragment.UnfinshRepairFragment;
import com.ruiqi.fragment.ZheJiuFinishFragment;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.works.BaseOrderActivity;

public class RobOrderActivity extends BaseOrderActivity {
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
	
		setTitle("抢单池列表");
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
		RobOrderFragment ufh = new RobOrderFragment();
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

//	private HttpUtil httpUtil;
//	private void getData() {
//		// TODO Auto-generated method stub
//		httpUtil=new HttpUtil();
//		httpUtil.setParserData(this);
//		int token = (Integer) SPUtils.get(RobOrderActivity.this, SPutilsKey.TOKEN, 0);
//		RequestParams params = new RequestParams(RequestUrl.ROBORDER);
//		params.addBodyParameter("shipper_id", (String) SPUtils.get(RobOrderActivity.this,
//				SPutilsKey.SHIP_ID, "error"));
////		params.addBodyParameter("Token",  token+"");
//		httpUtil.PostHttp(params, 0);
//	}

	

}
