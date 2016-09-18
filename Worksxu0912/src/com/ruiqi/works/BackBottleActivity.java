package com.ruiqi.works;

import java.io.Serializable;
import java.util.List;

import com.ruiqi.bean.BackBottle;
import com.ruiqi.bean.Bottle;
import com.ruiqi.db.BackBottleDao;
import com.ruiqi.fragment.BackBottleDeails;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 退瓶详情界面
 * @author Administrator
 *
 */
public class BackBottleActivity extends BaseActivity{
	
	private TextView tv_songqi_title,tv_songqi_money; //设置隐藏
	
	private TextView tv_yunfei_title,tv_pay_title,tv_back_modify,tv_order_commit;//修改文本内容
	
	private String depositsn;
	
	private BackBottleDao od;
	
	private String username,mobile,address,time,doormoney,money,productmoney,status,shouldmoney;
	
	private TextView tv_name,tv_phone,tv_address,tv_time,tv_money,tv_yunfei_money,tv_pay;
	
	
	private RelativeLayout rl_out_money,rl_youhui,rl_pj;
	
	private TextView next;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_details);
		setTitle("退瓶详情");
		od = BackBottleDao.getInstances(this);
		init();
		
		initData();
		BackBottleDeails b = new BackBottleDeails();
		Bundle bundle = new Bundle();
		bundle.putSerializable("mData", (Serializable)list);
		b.setArguments(bundle);
		
		getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, b).commit();
	}
	private List<Bottle> list;
	private void initData() {
		depositsn = getIntent().getStringExtra("depositsn");
		if(BackBottleOrder.mData!=null){
			for(int i=0;i<BackBottleOrder.mData.size();i++){
				BackBottle bb = BackBottleOrder.mData.get(i);
				if(depositsn.equals(bb.getDepositsn())){
					username = bb.getUsername();
					mobile = bb.getMobile();
					address = bb.getAddress();
					time = bb.getTime();
					doormoney = bb.getDoormoney();
					money = bb.getMoney();
					productmoney = bb.getProductmoney();
					status = bb.getStatus();
					shouldmoney = bb.getShouldmoney();
					list = bb.getList();
				}
			}
		}
		/*Cursor c = od.getFromOrderSn(depositsn);
		while (c.moveToNext()) {
			username = c.getString(c.getColumnIndex("username"));
			mobile = c.getString(c.getColumnIndex("mobile"));
			address = c.getString(c.getColumnIndex("address"));
			time = c.getString(c.getColumnIndex("time"));
			doormoney = c.getString(c.getColumnIndex("doormoney"));
			money = c.getString(c.getColumnIndex("money"));
			productmoney = c.getString(c.getColumnIndex("productmoney"));
			status = c.getString(c.getColumnIndex("status"));
			
		}*/
		
		tv_name.setText(username);
		tv_phone.setText(mobile);
		tv_address.setText(address);
		tv_time.setText(time);
		tv_money.setText(shouldmoney);
		tv_yunfei_money.setText(doormoney);
		
		
		if("2".equals(status)){
			next.setVisibility(View.GONE);
			tv_songqi_title.setVisibility(View.VISIBLE);
			tv_songqi_title.setText("残液");
			tv_songqi_money.setVisibility(View.VISIBLE);
			tv_songqi_money.setText(shouldmoney);
		}else {
			next.setVisibility(View.VISIBLE);
		}
		
	}

	private void init() {
		tv_songqi_title = (TextView) findViewById(R.id.tv_songqi_title);
		tv_songqi_money = (TextView) findViewById(R.id.tv_songqi_money);
		tv_songqi_title.setVisibility(View.GONE);
		tv_songqi_money.setVisibility(View.GONE);
		
		tv_yunfei_title = (TextView) findViewById(R.id.tv_yunfei_title);
		tv_yunfei_title.setText("上门费");
		
		
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_yunfei_money = (TextView) findViewById(R.id.tv_yunfei_money);
		
		
		rl_out_money=(RelativeLayout) findViewById(R.id.rl_out_money);
		rl_out_money.setVisibility(View.GONE);
		
		rl_pj = (RelativeLayout) findViewById(R.id.rl_pj);
		rl_pj.setVisibility(View.GONE);
		
		next = (TextView) findViewById(R.id.sure_pay);
		next.setOnClickListener(this);
		
		rl_youhui = (RelativeLayout) findViewById(R.id.rl_youhui);
		rl_youhui.setVisibility(View.GONE);
		
		
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = null;
		switch (v.getId()) {
		case R.id.sure_pay://开始退瓶，跳转到退瓶的扫描空瓶界面
			intent = new Intent(BackBottleActivity.this,BackSaoMiaoActivity.class );
			break;

		default:
			break;
		}
		if(intent!=null){
			startActivity(intent);
		}
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public Handler[] initHandler() {
		return null;
	}

}
