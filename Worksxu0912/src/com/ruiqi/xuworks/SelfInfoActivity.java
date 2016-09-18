package com.ruiqi.xuworks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.R;

public class SelfInfoActivity extends BaseActivity{
	Bundle bundle;
	private TextView tv_name,tv_phone,tv_card,tv_address,tv_time,tv_type,tv_flag,tv_next,tv_worker;
	String flag,kid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.self_info_activity);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("客户信息");
		bundle = new Bundle();
		bundle = getIntent().getBundleExtra("infos");
		String address = bundle.getString("self_address");
		String card = bundle.getString("self_card");
		String name = bundle.getString("self_name");
		String phone = bundle.getString("self_mobile");
		 flag = bundle.getString("self_list");
		String time = bundle.getString("self_time");
		String type = bundle.getString("self_type");
		String user = bundle.getString("self_user");
		 kid = bundle.getString("self_kid");
		tv_address = (TextView) findViewById(R.id.textView_down_address);
		tv_card = (TextView) findViewById(R.id.textView_down_card);
		tv_phone = (TextView) findViewById(R.id.textView_down_phone);
		tv_name = (TextView) findViewById(R.id.textView_down_name);
		tv_time = (TextView) findViewById(R.id.textView_down_time);
		tv_type = (TextView) findViewById(R.id.textView_down_type);
		tv_worker = (TextView) findViewById(R.id.textView_selfinfo_worker);
		tv_flag = (TextView) findViewById(R.id.textView_selfinfo_biaozhu);
		tv_next = (TextView) findViewById(R.id.tv_selfinfo_next);
		tv_next.setOnClickListener(this);
		tv_address.setText(address);
		tv_card.setText(card);
		tv_name.setText(name);
		tv_phone.setText(phone);
		tv_flag.setText(flag);
		tv_time.setText(time);
		if(user.equals("null")){
			tv_worker.setText("");
		}else{
		tv_worker.setText(user);}
		if(type.equals("1")){
			tv_type.setText("居民用户");
		}else{
			tv_type.setText("商业用户");
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_selfinfo_next:
			Intent intent = new Intent(SelfInfoActivity.this,SelfCheckActivity.class);
			bundle = new Bundle();
			bundle.putString("flag", flag);
			bundle.putString("kid", kid);
			intent.putExtra("flag", bundle);
			startActivity(intent);
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

}
