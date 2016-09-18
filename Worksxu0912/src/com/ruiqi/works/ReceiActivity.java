package com.ruiqi.works;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;

/**
 * 收款界面
 * @author Administrator
 *
 */
public class ReceiActivity extends BaseActivity {
	

	private TextView tv_money_content,tv_name;
	private EditText et_input;
	private RelativeLayout rl_btn;
	private ProgressDialog pd;
	private String kid,username,total;
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result=(String) msg.obj;
			paraData(result);
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_recei);
		setTitle("收缴欠款");
		kid=getIntent().getStringExtra("kid");
		username=getIntent().getStringExtra("username");
		total=getIntent().getStringExtra("total");
		init();
	}

	protected void paraData(String result) {
		try {
			JSONObject jsob=new JSONObject(result);
			if(jsob.getInt("resultCode")==1){
				Toast.makeText(this, "缴费成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(ReceiActivity.this, MainActivity.class);
				startActivity(intent);
			}else{
				Toast.makeText(this, "请求失败", Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void init() {
		pd=new ProgressDialog(this);
		pd.setMessage("正在加载中.....");
		tv_name=(TextView) findViewById(R.id.tv_name);
		tv_money_content=(TextView) findViewById(R.id.tv_money_content);
		tv_name.setText(username);
		tv_money_content.setText("￥"+total);
		et_input=(EditText) findViewById(R.id.et_input);
		CurrencyUtils.setPricePoint(et_input);
		rl_btn=(RelativeLayout) findViewById(R.id.rl_btn);
		rl_btn.setOnClickListener(this);		
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public Handler[] initHandler() {
		return null;
	}

	private boolean flag=true;
	private boolean isFrist = true;
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_btn:
			if(isFrist){
				if("null".equals(total)){
					T.showShort(ReceiActivity.this, "数据错误,赊欠金额为空");
				}else{
					
					if(getMoney()>0&&(getMoney()<Float.parseFloat(total)||getMoney()==Float.parseFloat(total))){
						isFrist = false;
						requestRecei(getMoney()+"");
					}else{
						if(flag){
							flag=false;
							Toast.makeText(this, "请输入正确的金额", Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
			break;
		default:
			break;
		}
	}

	private float getMoney() {
		String str = et_input.getText().toString().trim();
		if (TextUtils.isEmpty(str)) {
			return 0f;
		} else {
			return Float.parseFloat(str);
		}
	}
	private void requestRecei(String money) {
		RequestParams params=new RequestParams(RequestUrl.ADDREPAYMENT);
		params.addBodyParameter("shipper_id", (String)SPUtils.get(this,SPutilsKey.SHIP_ID, "11"));
		params.addBodyParameter("money", money);
		params.addBodyParameter("kid", kid);
		params.addBodyParameter("type", "4");//现金支付
		HttpUtil httpUtil=new HttpUtil();
		pd.show();
		httpUtil.PostHttp(params, handler, pd);
	}

}
