package com.ruiqi.xuworks;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.R;

public class CanyeActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.canye_activity);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("残液折现");
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
