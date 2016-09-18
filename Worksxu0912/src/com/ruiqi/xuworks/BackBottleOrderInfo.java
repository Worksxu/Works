package com.ruiqi.xuworks;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruiqi.bean.BackBottle;
import com.ruiqi.db.BackBottleDao;
import com.ruiqi.fragment.BackBottleFragment;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.XuDialog;
import com.ruiqi.utils.XuDialog.No;
import com.ruiqi.utils.XuDialog.Yes;
import com.ruiqi.works.BackBottleOrder;
import com.ruiqi.works.BackSaoMiaoActivity;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.R;
/**
 * 退瓶订单详情
 * @author Administrator
 *
 */
public class BackBottleOrderInfo extends BaseActivity implements Yes,No{
	private TextView tv_number,tv_status,tv_name,tv_phone,tv_address,tv_time,tv_total,tv_orderStatus,tv_next;
	private String username,mobile,address,time,doormoney,money,productmoney,status,shouldmoney,depositsn,kid,from,sheng,shi,qu,cun;
	private List<BackBottle> mList;
	public static String back_flag = null;//1代表退货,2代表退押金
	public static String bottle_flag = null;// 1代表重瓶,2代表故障瓶
	private BackBottleDao bbd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.backbottleorderinfo_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("退瓶订单详情");
		tv_next = (TextView) findViewById(R.id.orderInfo_nextStep);
		tv_next.setOnClickListener(this);
		
		tv_number = (TextView) findViewById(R.id.textView_orderInfo_number);
		tv_status = (TextView) findViewById(R.id.tv_newOrderInfo_status);
		tv_address = (TextView) findViewById(R.id.textView_orderInfo_address);
		tv_orderStatus = (TextView) findViewById(R.id.textView_orderInfo_status);
		tv_time = (TextView) findViewById(R.id.textView_orderInfo_time);
		tv_phone = (TextView) findViewById(R.id.textView_orderInfo_phone);
		tv_name = (TextView) findViewById(R.id.textView_orderInfo_name);
		depositsn = getIntent().getStringExtra("depositsn");
		from = getIntent().getStringExtra("from");
		if(from.equals("bottleorder")){
			Log.e("llll", BackBottleFragment.mData.size()+"");
			for(int i=0;i<BackBottleFragment.mData.size();i++){
				BackBottle bb = BackBottleFragment.mData.get(i);
				if(depositsn.equals(bb.getDepositsn())){
					username = bb.getUsername();
					mobile = bb.getMobile();
					address = bb.getAddress();
					time = bb.getTime();
					doormoney = bb.getDoormoney();
					money = bb.getMoney();
					productmoney = bb.getProductmoney();
					status = bb.getStatus();
					kid = bb.getKid();
					sheng = bb.getSheng();
					shi= bb.getShi();
					qu = bb.getQu();
					cun = bb.getCun();
				}
				
				}
			tv_number.setText(depositsn);
			tv_name.setText(username);
			tv_phone.setText(mobile);
			tv_address.setText(address);
			tv_time.setText(time);
			SPUtils.put(BackBottleOrderInfo.this, "tpnumber", depositsn);
			SPUtils.put(BackBottleOrderInfo.this, "oldUserName", username);
			SPUtils.put(BackBottleOrderInfo.this, "oldUserMobile", mobile);
			SPUtils.put(BackBottleOrderInfo.this, "oldUserAddress", address);
			SPUtils.put(BackBottleOrderInfo.this, "sheng", sheng);
			SPUtils.put(BackBottleOrderInfo.this, "shi", shi);
			SPUtils.put(BackBottleOrderInfo.this, "qu", qu);
			SPUtils.put(BackBottleOrderInfo.this, "cun", cun);
			if(!TextUtils.isEmpty(kid)){
				SPUtils.put(BackBottleOrderInfo.this, "kid", kid);
			}
			
			}else{
				tv_number.setText((CharSequence) SPUtils.get(BackBottleOrderInfo.this, "tpnumber", ""));
				tv_name.setText((CharSequence) SPUtils.get(BackBottleOrderInfo.this, "oldUserName", ""));
				tv_phone.setText((CharSequence) SPUtils.get(BackBottleOrderInfo.this, "oldUserMobile", ""));
				tv_address.setText((CharSequence) SPUtils.get(BackBottleOrderInfo.this, "oldUserAddress", ""));
				tv_time.setText(getNowTime());
			}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.orderInfo_nextStep:
//			XuDialog.getInstance().setno(this);
//			XuDialog.getInstance().setyes(this);
//			XuDialog.getInstance().show(BackBottleOrderInfo.this, "选择类型","退货","退押金", 1);
			Intent intent = new Intent(BackBottleOrderInfo.this,BackSaoMiaoActivity.class);
			startActivity(intent);
			finish();
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
	private String getNowTime() {
		Date nowDate = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String now = df.format(nowDate);
		return now;
	}

	@Override
	public void XuNo(int i) {
		// TODO Auto-generated method stub
		if(i == 1){//退押金
			back_flag = "2";
			Intent intent = new Intent(BackBottleOrderInfo.this,BackSaoMiaoActivity.class);
			startActivity(intent);
			finish();
			Log.e("llll_back", back_flag+"退押金"+bottle_flag);
		}else{// 故障瓶
			bottle_flag = "2";
			Intent intent = new Intent(BackBottleOrderInfo.this,BackSaoMiaoActivity.class);
			startActivity(intent);
			finish();
			Log.e("llll_back", back_flag+"故障瓶"+bottle_flag);
		}
	}

	@Override
	public void XuCallback(int i) {
		// TODO Auto-generated method stub
		if(i == 1){// 退货
			back_flag = "1";
			Intent intent = new Intent(BackBottleOrderInfo.this,BackSaoMiaoActivity.class);
			startActivity(intent);
			finish();
//			XuDialog.getInstance().show(BackBottleOrderInfo.this, "选择瓶的状态","重瓶","故障瓶", 2);
			Log.e("llll_back", back_flag+"退货"+bottle_flag);
		}else{//重瓶//
			bottle_flag = "1";
			Intent intent = new Intent(BackBottleOrderInfo.this,BackSaoMiaoActivity.class);
			startActivity(intent);
			finish();
			Log.e("llll_back", back_flag+"zhongping"+bottle_flag);
		}
	}

}
