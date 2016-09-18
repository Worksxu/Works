package com.ruiqi.xuworks;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.ruiqi.bean.Order;
import com.ruiqi.bean.Orderdeail;
import com.ruiqi.bean.Type;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.CreateOrderActivity;
import com.ruiqi.works.GrassOrder;
import com.ruiqi.works.R;

public class FinishOrderActivity extends BaseActivity{
	private TextView tv_number, tv_finishtime, tv_username, tv_userphone,
	tv_address, tv_shopname, tv_worker, tv_selftime,tv_canyeWeight,tv_canyeMoney
	,tv_shouldMoney,tv_money,tv_debttype,tv_MoneyDebt,tv_finishStatus,
	tv_pay;
	private String ordersn;// 订单号
	private String username, mobile, address, shopName, workersname,
	workersmobile, status, kid, total, yunfei, yajin, zheijiu, canye,
	pay_money, ispayment,commtent,peijianprice,finishtime,selftime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.finishorderinfo_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		tv_address = (TextView) findViewById(R.id.textView_finishbottle_address);
		ordersn = getIntent().getExtras().getString("ordersn");
//		tv_yuqi = (TextView) findViewById(R.id.textView_finishbottle_yuqi);
//		tv_yajin = (TextView) findViewById(R.id.textView_finishbottle_yajin);
		tv_worker = (TextView) findViewById(R.id.textView_finishbottle_worker);
		tv_userphone = (TextView) findViewById(R.id.textView_finishbottle_userphone);
		tv_username = (TextView) findViewById(R.id.textView_finishbottle_username);
		tv_shopname = (TextView) findViewById(R.id.textView_finishbottle_shopname);
//		tv_pay = (TextView) findViewById(R.id.textView_finishbottle_pay);
		tv_number = (TextView) findViewById(R.id.textView_finishbottle_number);
		tv_finishtime = (TextView) findViewById(R.id.textView_finishbottle_finishtime);
		tv_selftime = (TextView) findViewById(R.id.textView_finishbottle_backtime);// 上次安检时间
		tv_canyeWeight = (TextView) findViewById(R.id.tv_finishorder_canweight);
		tv_canyeMoney = (TextView) findViewById(R.id.tv_finishorder_canprice);
		tv_shouldMoney  = (TextView) findViewById(R.id.textView_finishorder_shouldmoney);// 应收款
		tv_money  = (TextView) findViewById(R.id.textView_finishorder_money);// 实收款
		tv_MoneyDebt  = (TextView) findViewById(R.id.textView_finishorder_Debtmoney);// 欠款
		tv_debttype  = (TextView) findViewById(R.id.textView_finishorder_moneyType);// 欠款类型
		tv_finishStatus = (TextView) findViewById(R.id.textView_finishStatus);
		initDta();
	}

	private void initDta() {
		// TODO Auto-generated method stub
		if (GrassOrder.mData != null) {
			for (int i = 0; i < GrassOrder.mData.size(); i++) {
				Order o = GrassOrder.mData.get(i);
				if (o.getOrdersn().equals(ordersn)) {
					username = o.getUsername();
					mobile = o.getMobile();
					address = o.getAddress();
					shopName = o.getShop_name();
					workersname = o.getWorksname();
					workersmobile = o.getWorksmobile();
					status = o.getStatus();
					kid = o.getKid();
					total = o.getTotal();
					yunfei = "0";
					yajin = o.getDeposit();
					zheijiu = o.getDepreciation();
					canye = o.getRaffinat();
					pay_money = o.getPay_money();
					ispayment = o.getIspayment();
					commtent=o.getComment();
					peijianprice=o.getPeijianprice();
					finishtime = o.getTime();
					List<Orderdeail> mList = o.getOd();
					
					
				}
			}
			tv_address.setText(address);
			tv_canyeMoney.setText(canye);
			tv_number.setText(ordersn);
			tv_username.setText(username);
			tv_userphone.setText(mobile);
			tv_finishtime.setText(finishtime);
			tv_shouldMoney.setText(total);
			tv_shopname.setText(shopName);
			tv_worker.setText(workersname);
			tv_money.setText(pay_money);
			if(ispayment.equals("2")){
				tv_finishStatus.setText("未支付");
			}else{
				tv_finishStatus.setText("已完成");
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
