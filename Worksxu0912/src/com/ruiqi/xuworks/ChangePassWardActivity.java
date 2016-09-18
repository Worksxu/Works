package com.ruiqi.xuworks;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.R;

public class ChangePassWardActivity extends BaseActivity implements ParserData{
	private EditText et_now,et_reset,et_sure;
	private TextView tv_change;
	String passWord64,confirm_password64,new_password64;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("修改密码");
		et_now = (EditText) findViewById(R.id.et_changepass_now);
		et_reset = (EditText) findViewById(R.id.et_changepass_reset);
		et_sure = (EditText) findViewById(R.id.et_changepass_sure);
		tv_change = (TextView) findViewById(R.id.tv_changepass_next);
		
		tv_change.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String newPassword = et_reset.getText().toString().trim();// 新
				String oldPassword = et_now.getText().toString().trim();// 老密码
				String surePassword = et_sure.getText().toString().trim();// 确认
				 passWord64 = Base64.encodeToString(oldPassword.getBytes(), Base64.DEFAULT);// 老密码加密
				  confirm_password64 = Base64.encodeToString(surePassword.getBytes(), Base64.DEFAULT);// 确认
				 new_password64 = Base64.encodeToString(newPassword.getBytes(), Base64.DEFAULT);// 新密码
			
				if(!TextUtils.isEmpty(newPassword)&&
						!TextUtils.isEmpty(oldPassword)&&
						!TextUtils.isEmpty(surePassword)){
					
					if(newPassword.equals(surePassword)){
						getData();
					}else{
						Toast.makeText(ChangePassWardActivity.this, "新密码与确认密码不一致", 1).show();
					}
					
				}else{
					Toast.makeText(ChangePassWardActivity.this, "请输入完整信息", 1).show();
					Log.e("lll_bu", newPassword+oldPassword+surePassword+"dfdfd");
				}
//				
			}
		});
		
	}
	private HttpUtil httpUtil;
	private void getData() {
		// TODO Auto-generated method stub
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(ChangePassWardActivity.this, SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.CHANGEPASS);
		
		params.addBodyParameter("token",  token+"");
		params.addBodyParameter("mobile",  (String) SPUtils.get(ChangePassWardActivity.this, SPutilsKey.MOBILLE, ""));// 手机号
		params.addBodyParameter("password",  new_password64);// 新密码
		params.addBodyParameter("confirm_password",  confirm_password64);// 确认密码
		params.addBodyParameter("old_password",  passWord64);// 旧密码
		Log.e("lll_pass", params.getStringParams().toString());
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
		Log.e("lll_result", result);
		
		try {
			JSONObject object = new JSONObject(result);
			int code = object.getInt("resultCode");
			String info = object.getString("resultInfo");
			if(code == 1){
				Toast.makeText(ChangePassWardActivity.this, info, 1).show();
			}else{
				Toast.makeText(ChangePassWardActivity.this, info, 1).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
