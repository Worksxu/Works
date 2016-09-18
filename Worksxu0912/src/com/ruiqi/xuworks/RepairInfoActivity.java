package com.ruiqi.xuworks;


import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.bean.Order;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.HomePageActivity;
import com.ruiqi.works.R;

public class RepairInfoActivity extends BaseActivity implements ParserData{
	private TextView tv_number,tv_status,tv_name,tv_phone,tv_orderStatus,tv_beizhu,tv_next,tv_project,tv_result,tv_address;
	private EditText et_result;
	String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.repairinfo_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("处理订单");
		id = getIntent().getExtras().getString("id");
		tv_next = (TextView) findViewById(R.id.repairInfo_nextStep);
		tv_next.setOnClickListener(this);
		
		tv_number = (TextView) findViewById(R.id.textView_orderInfo_number);
		tv_status = (TextView) findViewById(R.id.tv_newOrderInfo_status);// 状态
		
		tv_phone = (TextView) findViewById(R.id.textView_orderInfo_phone);
		tv_name = (TextView) findViewById(R.id.textView_orderInfo_name);
		et_result = (EditText) findViewById(R.id.editText_repairinfo_result);
		tv_beizhu = (TextView) findViewById(R.id.textView_repairInfo_beizhu);
		tv_address = (TextView) findViewById(R.id.textView_orderInfo_address);
		tv_project = (TextView) findViewById(R.id.textView_repairInfo_project);
		
			initData();
		
		
	}

	private void initData() {
		// TODO Auto-generated method stub
		for (int i = 0; i < Repairorder.mData.size(); i++) {
			Order obj1 =  Repairorder.mData.get(i);
			if(Repairorder.mData.get(i).getKid().equals(id)){
				String ordersn = obj1.getOrdersn();// 订单编号
				String comment = obj1.getComment();// 订单备注
				String phone = obj1.getMobile();// 电话
				String address = obj1.getAddress();//地址
				String project = obj1.getDelivery();// 报修项目
				String time = obj1.getTime();// 订单创建时间

				String status = obj1.getStatus();// 订单状态
				String id = obj1.getKid();// 订单id
				String result = obj1.getTotal();// 订单处理结果
				String username = obj1.getUsername();// 客户姓名
				tv_number.setText(ordersn);
				tv_beizhu.setText(comment);
				tv_name.setText(username);
				tv_phone.setText(phone);
				tv_address.setText(address);
				tv_project.setText(project);
				if(status.equals("1")){
					tv_status.setText("处理中");
				}else{
					tv_status.setText("已完成");
					tv_next.setVisibility(View.GONE);
					et_result.setEnabled(false);
					et_result.setText(result);
				}
			}
			
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.repairInfo_nextStep:
			if(!TextUtils.isEmpty(et_result.getText().toString())){
				commintResult();
			}else{
				Toast.makeText(RepairInfoActivity.this, "处理结果不能为空", 1).show();
			}
			
			break;

		default:
			break;
		}
	}
	private HttpUtil httpUtil;
	private void commintResult() {
		// TODO Auto-generated method stub
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(RepairInfoActivity.this, SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.REPAIRORDER);
		params.addBodyParameter("shipper_name", (String) SPUtils.get(RepairInfoActivity.this, "shipper_name", ""));
		params.addBodyParameter("shipper_id", (String) SPUtils.get(RepairInfoActivity.this,  SPutilsKey.SHIP_ID, ""));
		params.addBodyParameter("Token",  token+"");
		params.addBodyParameter("id",  id);
		params.addBodyParameter("comment",  et_result.getText().toString());
		Log.e("lll", params.getStringParams().toString());
		httpUtil.PostHttp(params, 0);
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
		Log.e("lll", result);
		try {
			JSONObject object = new JSONObject(result);
			int code = object.getInt("resultCode");
			String info = object.getString("resultInfo");
			if(code == 1){
				Toast.makeText(RepairInfoActivity.this, info, 1).show();
				Intent intent = new Intent(RepairInfoActivity.this,HomePageActivity.class);
				startActivity(intent);
			}else{
				Toast.makeText(RepairInfoActivity.this, info, 1).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
