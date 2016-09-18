package com.ruiqi.xuworks;

import com.ruiqi.fragment.BackBottleFragment;
import com.ruiqi.fragment.ChangeOrderFinishFragment;
import com.ruiqi.works.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

@SuppressLint("NewApi")
public class BackAndChangeList extends FragmentActivity implements android.widget.RadioGroup.OnCheckedChangeListener {
	private RadioGroup rg_select;
	private LinearLayout ll_back;
	BackBottleFragment bf;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.backandchange_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		
		rg_select = (RadioGroup) findViewById(R.id.rg_select);
		rg_select.setOnCheckedChangeListener(this);
		ll_back = (LinearLayout) findViewById(R.id.llBack);
		ll_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		 bf = new BackBottleFragment();
		getSupportFragmentManager().beginTransaction().replace(R.id.rl_content_fragment, bf).commit();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.rb_unfinsh://退瓶
			 bf = new BackBottleFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_content_fragment, bf).commit();
			break;
		case R.id.rb_finsh://退货
			ChangeOrderFinishFragment cf = new ChangeOrderFinishFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_content_fragment, cf).commit();
			break;

		default:
			break;
		}
	}

	
		
	}


