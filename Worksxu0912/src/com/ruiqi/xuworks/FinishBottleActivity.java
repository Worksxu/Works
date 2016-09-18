package com.ruiqi.xuworks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruiqi.bean.BackBottle;
import com.ruiqi.bean.Bottle;
import com.ruiqi.fragment.BackBottleDeails;
import com.ruiqi.fragment.BackBottleFragment;
import com.ruiqi.utils.DateUtils;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.works.BackBottleOrder;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.R;

public class FinishBottleActivity extends BaseActivity {
	private TextView tv_number, tv_finishtime, tv_username, tv_userphone,
			tv_address, tv_shopname, tv_worker, tv_backtime, tv_yajin, tv_yuqi,
			tv_pay;
	private LinearLayout ll_toast;
	private String username, mobile, address, time, doormoney, money,
			productmoney, status, shouldmoney, depositsn, kid,create_time;
	private List<BackBottle> mList = new ArrayList<BackBottle>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finishbottleinfo_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("退瓶订单详情");
		tv_address = (TextView) findViewById(R.id.textView_finishbottle_address);
		depositsn = getIntent().getExtras().getString("depositsn");
		tv_yuqi = (TextView) findViewById(R.id.textView_finishbottle_yuqi);
		tv_yajin = (TextView) findViewById(R.id.textView_finishbottle_yajin);
		tv_worker = (TextView) findViewById(R.id.textView_finishbottle_worker);
		tv_userphone = (TextView) findViewById(R.id.textView_finishbottle_userphone);
		tv_username = (TextView) findViewById(R.id.textView_finishbottle_username);
		tv_shopname = (TextView) findViewById(R.id.textView_finishbottle_shopname);
		tv_pay = (TextView) findViewById(R.id.textView_finishbottle_pay);
		tv_number = (TextView) findViewById(R.id.textView_finishbottle_number);
		tv_finishtime = (TextView) findViewById(R.id.textView_finishbottle_finishtime);
		tv_backtime = (TextView) findViewById(R.id.textView_finishbottle_backtime);
		if (BackBottleFragment.mData != null) {
			for (int i = 0; i < BackBottleFragment.mData.size(); i++) {
				BackBottle bb = BackBottleFragment.mData.get(i);
				if(depositsn.equals(bb.getDepositsn())){
					username = bb.getUsername();
					mobile = bb.getMobile();
					address = bb.getAddress();
					time = bb.getTime();// 完成时间
					doormoney = bb.getDoormoney();
					money = bb.getMoney();
					productmoney = bb.getShouldmoney();// yajin
					status = bb.getStatus();
					kid = bb.getKid();
					create_time = bb.getStatus_name();// 创建时间
					mList = (List) bb.getList();
				}
				
			}
			BackBottleDeails b = new BackBottleDeails();
			Bundle bundle = new Bundle();
			bundle.putSerializable("mData", (Serializable)mList);
			b.setArguments(bundle);
			
			getSupportFragmentManager().beginTransaction().replace(R.id.ll_finishbottle_toast, b).commit();
			tv_number.setText(depositsn);
			tv_finishtime.setText(time);
			tv_username.setText(username);
			tv_userphone.setText(mobile);
			tv_address.setText(address);
			tv_backtime.setText(DateUtils.getHMS(create_time));
			tv_pay.setText(money);
			tv_yajin.setText(productmoney);
			tv_yuqi.setText(Double.parseDouble(money)- Double.parseDouble(productmoney)+"");
			tv_worker.setText((CharSequence) SPUtils.get(FinishBottleActivity.this, "shipper_name", ""));
			tv_shopname.setText((CharSequence) SPUtils.get(FinishBottleActivity.this, "shop_name", ""));
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
