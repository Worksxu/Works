package com.ruiqi.xuworks;


import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.R;

public class SelfUserActivity extends BaseActivity implements ParserData{
	private TextView tv_name,tv_phone,tv_address,tv_create;
	private EditText et_oldUser;
	private ImageView img_oldUser;
	private LinearLayout ll_oldUser;
	private ProgressDialog pd;
	private String self_time;
	private String self_type;
	private String self_name;
	private String self_mobile;
	private String self_address;
	private String self_card;
	private String self_list;
	private String self_user;
	String self_kid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_order_layout);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
//		gd = GpDao.getInstances(getActivity());
		pd = new ProgressDialog(SelfUserActivity.this);
		pd.setMessage("正在加载");
//		SPutilsKey.neworold=2;
//		SPutilsKey.type = 2;
		setTitle("安全报告");
		
		tv_create = (TextView) findViewById(R.id.tv_createNew);
		tv_create.setText("下一步");
		tv_create.setVisibility(View.GONE);
		tv_create.setOnClickListener(this);
		tv_address  = (TextView) findViewById(R.id.textView_olderAddress);
		tv_phone  = (TextView) findViewById(R.id.textView_olderPhone);
		tv_name  = (TextView)findViewById(R.id.textView_olderName);
		et_oldUser = (EditText)findViewById(R.id.editText_oldUser);
		img_oldUser = (ImageView)findViewById(R.id.imageView_oldUser);
		img_oldUser.setOnClickListener(this);
		ll_oldUser = (LinearLayout)findViewById(R.id.ll_oldUser);
		ll_oldUser.setOnClickListener(this);
		
				
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.imageView_oldUser:
			initData();
			break;
		case R.id.tv_createNew:
			Intent infos = new Intent(SelfUserActivity.this, SelfInfoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("self_name", self_name);
			bundle.putString("self_mobile", self_mobile);
			bundle.putString("self_card", self_card);
			bundle.putString("self_address", self_address);
			bundle.putString("self_list", self_list);
			bundle.putString("self_time", self_time);
			bundle.putString("self_type", self_type);
			bundle.putString("self_kid", self_kid);
			bundle.putString("self_user", self_user);
			infos.putExtra("infos",bundle);
			startActivity(infos);
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
	private HttpUtil httpUtil;
	
	
	private void initData() {
		// TODO Auto-generated method stub
		pd.show();
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
//		int token =  (Integer) SPUtils.get(SelfUserActivity.this, SPutilsKey.TOKEN, "");
		int token = (Integer) SPUtils.get(SelfUserActivity.this, SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.SELFUSER);
		params.addBodyParameter("mobile", et_oldUser.getText().toString());
		params.addBodyParameter("Token",  token+"");
		httpUtil.PostHttp(params, 0);
	}
	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("lll", result);
		if(true){
			try {
				JSONObject object = new JSONObject(result);
				int code = object.getInt("resultCode");
				String mess  = object.getString("resultInfo");
				if(code == 1){
					pd.dismiss();
					JSONObject info = object.getJSONObject("resultInfo");
					 self_name = info.getString("user_name");
					 self_kid = info.getString("kid");
					 self_mobile = info.getString("mobile_phone");
					 self_card = info.getString("card_sn");
					 self_address = info.getString("address");
//					String self_worker = info.getString("address");// 待定
					 self_list = info.getString("orderlist");// 安全选项
					 self_time = info.getString("time");// 安全时间
					 self_user = info.getString("safe_user");// 安俭员
					String self_report = info.getString("report");// 安全报告
					self_type = info.getString("ktype");// 用户类型
					ll_oldUser.setVisibility(View.VISIBLE);
					tv_address.setText(self_address);
					tv_name.setText(self_name);
					
					tv_phone.setText(self_mobile);
					SPUtils.put(SelfUserActivity.this, "self_kid", self_kid);// 保存客户的kid
					SPUtils.put(SelfUserActivity.this, "self_report", self_report);// 保存客户报告
					SPUtils.put(SelfUserActivity.this, "self_type", self_type);// 保存客户报告
					tv_create.setVisibility(View.VISIBLE);
					
				}else{
					pd.dismiss();
					Toast.makeText(SelfUserActivity.this, mess, 1);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			pd.dismiss();
		}
		
	}

}
