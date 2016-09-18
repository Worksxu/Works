package com.ruiqi.fragment;

import java.util.zip.Inflater;

























import com.ruiqi.bean.BackBottle;
import com.ruiqi.view.CustomFootView;
import com.ruiqi.works.BackBottleOrder;
import com.ruiqi.works.CheckActivity;
import com.ruiqi.works.GrassOrder;
import com.ruiqi.works.R;
import com.ruiqi.works.WeightActivity;
import com.ruiqi.xuworks.BackAndChangeList;
import com.ruiqi.xuworks.CashAndDiscountActivity;
import com.ruiqi.xuworks.ChangeOrderListActivity;
import com.ruiqi.xuworks.Repairorder;
import com.ruiqi.xuworks.RobOrderActivity;
import com.ruiqi.xuworks.SelfUserActivity;
import com.ruiqi.xuworks.ZheJiuActivity;
import com.ruiqi.xuworks.ZheJiuOrderListActivity;

import android.app.SearchManager.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author xtl
 * 首页
 *
 */
public class JumpFragment extends Fragment implements OnClickListener {
public  static String TAG  = JumpFragment.class.getName();
private View view;
private LinearLayout ll_homeSearch;
private CustomFootView cf_gass,cf_peijian,cf_backbottle,cf_repair,cf_self,cf_zhexian,cf_huanghuo;
private ImageView img_search;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.jump_layout, null);
		init();
		return view;
	}
	private void init() {
		// TODO Auto-generated method stub
//		img_search = (ImageView) view.findViewById(R.id.imageView_homesearch);
		
		
		cf_gass = (CustomFootView) view.findViewById(R.id.cf_gass);
		cf_gass.setImage(R.drawable.gass);
		cf_gass.setString("订气订单");
		cf_backbottle = (CustomFootView) view.findViewById(R.id.cf_backbottle);
		cf_backbottle.setImage(R.drawable.backbottle);
		cf_backbottle.setString("退换订单");
		cf_peijian = (CustomFootView) view.findViewById(R.id.cf_peijian);
		cf_peijian.setImage(R.drawable.peijian);
		cf_peijian.setString("配件订单");
		cf_repair = (CustomFootView) view.findViewById(R.id.cf_repair);
		cf_repair.setImage(R.drawable.repair);
		cf_repair.setString("报修订单");
		cf_self = (CustomFootView) view.findViewById(R.id.cf_self);
		cf_self.setImage(R.drawable.self);
		cf_self.setString("安全检查");
		cf_huanghuo = (CustomFootView) view.findViewById(R.id.cf_tuihuo);
		cf_huanghuo.setImage(R.drawable.huanhuo1);
		cf_huanghuo.setString("抢单池");
		cf_zhexian = (CustomFootView) view.findViewById(R.id.cf_zhejiu);
		cf_zhexian.setImage(R.drawable.zhexian);
		cf_zhexian.setString("折旧订单");
//		tv_gass = (TextView) view.findViewById(R.id.textView_gass);
		cf_self.setOnClickListener(this);
		cf_gass.setOnClickListener(this);
		cf_peijian.setOnClickListener(this);
		cf_repair.setOnClickListener(this);
		cf_backbottle.setOnClickListener(this);
		cf_zhexian.setOnClickListener(this);
		cf_huanghuo.setOnClickListener(this);
	
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		
	
		case R.id.cf_self:
			Intent self = new Intent(getActivity(),SelfUserActivity.class);
			startActivity(self);
			
			break;
		case R.id.cf_zhejiu:
			Intent zhejiu = new Intent(getActivity(),ZheJiuOrderListActivity.class);
			startActivity(zhejiu);
//			zhejiu.putExtra("type", 1);
//			startActivityForResult(zhejiu, 1);
//			Toast.makeText(getActivity(), "此功能未开通", 1).show();
			break;
		case R.id.cf_tuihuo:
			Intent tuihuo = new Intent(getActivity(),RobOrderActivity.class);
			startActivity(tuihuo);
//			Toast.makeText(getActivity(), "此功能未开通", 1).show();
			break;
		case R.id.cf_gass:
			Intent gass = new Intent(getActivity(),GrassOrder.class);
			startActivity(gass);
			break;
		case R.id.cf_backbottle:
			Intent backbottle = new Intent(getActivity(),BackAndChangeList.class);
			startActivity(backbottle);
			break;
		case R.id.cf_repair:
			Intent repair = new Intent(getActivity(),Repairorder.class);
			startActivity(repair);
			break;
		default:
			break;
		}
	}
	

}
