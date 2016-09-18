package com.ruiqi.works;


import java.io.Serializable;
import java.util.List;

import com.ruiqi.bean.Order;
import com.ruiqi.fragment.FinshBottleFragment;
import com.ruiqi.fragment.FinshFragment;
import com.ruiqi.fragment.FinshRepairFragment;
import com.ruiqi.fragment.ProblemFragment;
import com.ruiqi.fragment.UnfinshBottle;
import com.ruiqi.fragment.UnfinshFragment;
import com.ruiqi.fragment.UnfinshRepairFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;


public  abstract class BaseOrderActivity extends BaseActivity implements OnCheckedChangeListener{

	public RelativeLayout rl_grass_order;
	
	public RadioGroup rg_select;
	RadioButton rb_problem;
	private List<Order> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grassorder);
		
		list = initListData();
		System.out.println("list"+list);
		init();
	}
	
	private void init() {
		rl_grass_order = (RelativeLayout) findViewById(R.id.rl_grass_order);
		rb_problem = (RadioButton) findViewById(R.id.rb_problem);
		if(initInteger()==2||initInteger()==3){
			rb_problem.setVisibility(View.GONE);
		}
		rg_select = (RadioGroup) findViewById(R.id.rg_select);
		if(initInteger()==4){
			rg_select.setVisibility(View.GONE);
		}
		rg_select.setOnCheckedChangeListener(this);
		rl_grass_order.setOnClickListener(this);
		
			
	}
	
	public abstract Fragment initFragment();
	public abstract int initInteger();
	public abstract List<Order> initListData();
	public void setFragment(){
		Fragment fragment = initFragment();
		
		getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment, fragment).commitAllowingStateLoss();
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_unfinsh:
			if(initInteger()==1){
				getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment,new UnfinshFragment() ).commit();
			}else if(initInteger()==2){
				getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment, new UnfinshBottle()).commit();
			}else if(initInteger()==3){
				getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment, new UnfinshRepairFragment()).commit();
			}
			break;

		
		case R.id.rb_problem:
			if(initInteger()==1){
			getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment,new ProblemFragment() ).commit();
			}
			break;
		case R.id.rb_finsh:
			if(initInteger()==1){
				getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment, new FinshFragment()).commit();
			}else if(initInteger()==2){
				getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment, new FinshBottleFragment()).commit();
			}else if(initInteger()==3){
				getSupportFragmentManager().beginTransaction().replace(R.id.ll_fragment, new FinshRepairFragment()).commit();
			}
			break;
		default:
			
			break;
		}
	}
	

}
