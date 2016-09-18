package com.ruiqi.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ruiqi.bean.Weight;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.works.ApplyPjDeailActivity;
import com.ruiqi.works.BottleWeightDeails;
import com.ruiqi.works.GoodsReceiActivity;
import com.ruiqi.works.NfcActivity;
import com.ruiqi.works.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class GetGoodsFragment extends Fragment implements OnCheckedChangeListener,OnClickListener{
	private View view;
private RadioGroup rg_select;
	
	private RelativeLayout rl_back;
	
	private TextView apply;
	private boolean flag = true;
	
	private List<Weight> list;
	public static String TAG = GetGoodsFragment.class.getName();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.activity_goods_recei, null);
		init();
		return view;
	}
	private void init() {
		rg_select = (RadioGroup) view.findViewById(R.id.rg_select);
		rg_select.setOnCheckedChangeListener(this);
		
		rl_back = (RelativeLayout) view.findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		
		apply = (TextView) view.findViewById(R.id.apply);
		apply.setOnClickListener(this);
		
		apply.setText("下一步");
	}
//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		setIntent(intent);
//	}
	private BootleWeightFragment boot;
	private void initData() {
		if(flag){
			list = new ArrayList<Weight>();
			List<String> mList = (List<String>) getActivity().getIntent().getSerializableExtra("mData");
			if(NfcActivity.mDataRecei!=null){
				for(int i =0;i<NfcActivity.mDataRecei.size();i++){
					list.add(new Weight(NfcActivity.mDataRecei.get(i).getXinpian(), "1", "1"));
				}
			}
			boot = new BootleWeightFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("mData", (Serializable)list);
			boot.setArguments(bundle);
			getChildFragmentManager().beginTransaction().replace(R.id.rl_content, boot).commitAllowingStateLoss();
		}else{
			getChildFragmentManager().beginTransaction().replace(R.id.rl_content, new ApplyPeiJianFragment()).commit();
			apply.setVisibility(View.GONE);
			apply.setText("申请配件");
		}
		
		
	}
	@Override
	public void onResume() {
		super.onResume();
		SPUtils.put(getActivity(), "UI", "recei");
		initData();
	}
	

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_weight://重瓶领取
			//切换到收入fragment
			BootleWeightFragment boot = new BootleWeightFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("mData", (Serializable)list);
			boot.setArguments(bundle);
			getChildFragmentManager().beginTransaction().replace(R.id.rl_content, boot).commit();
			flag = true;
			apply.setVisibility(View.VISIBLE);
			apply.setText("下一步");
			break;
		case R.id.rb_peijian://配件领取
			//切换到支出界面（先申请，再领取）
			getChildFragmentManager().beginTransaction().replace(R.id.rl_content, new ApplyPeiJianFragment()).commit();
			flag = false;
			apply.setVisibility(View.GONE);
			apply.setText("申请配件");
			//直接领取
//			if(NfcActivity.mData!=null){
//				NfcActivity.mData=null;
//			}
//			Intent intent = new Intent(GoodsReceiActivity.this, ApplyPjDeailActivity.class);
//			startActivity(intent);
//			finish();
//			SPUtils.remove(GoodsReceiActivity.this, "UI");
//			break;

		default:
			break;
		}
	}
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.rl_back:
//			if(NfcActivity.mData!=null){
//				NfcActivity.mData=null;
//			}
//			SPUtils.remove(getActivity(), "UI");
//			getActivity().finish();
//			break;
		case R.id.apply:
			if(NfcActivity.mData!=null){
				NfcActivity.mData=null;
			}
			SPUtils.remove(getActivity(), "UI");
			if(flag==true){
				Intent intent = new Intent(getActivity(), BottleWeightDeails.class);
				intent.putExtra("mData", (Serializable)list);
				startActivity(intent);
				
			}else{
				Intent intent = new Intent(getActivity(), ApplyPjDeailActivity.class);
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// 待定
	}
	


}
