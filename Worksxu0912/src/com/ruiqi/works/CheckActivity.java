package com.ruiqi.works;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CheckActivity extends BaseActivity {
	private EditText et_putin;
	private ImageView img_search;
	//查询网络请求的handler
		private Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				String strSu = (String) msg.obj;
				
			};
		};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("查询");
		et_putin = (EditText) findViewById(R.id.editText_check);
		img_search = (ImageView) findViewById(R.id.imageView_checkout);
		img_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(et_putin.getText().toString().equals("")){
					Toast.makeText(CheckActivity.this, "请输入钢印号", 1).show();
				}else{
					
				}
			}
		});
	}

	@Override
	public Activity getActivity() {
		// TODO Auto-generated method stub
		return CheckActivity.this;
	}

	@Override
	public Handler[] initHandler() {
		// TODO Auto-generated method stub
		return null;
	}

}
