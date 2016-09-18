package com.ruiqi.fragment;

import com.ruiqi.works.R;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class CanyeWeightFragment extends BaseFragment{
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
//			paraseData(result);
		}
	};
	private TextView tv_today,tv_total;
	@Override
	public View initView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.canye_weight_fragment, null);
		tv_today = (TextView) view.findViewById(R.id.textView_canyeTody);
		tv_total = (TextView) view.findViewById(R.id.textView_canyeTotal);
		initData();
		return view;
	}
	private void initData() {
		// TODO Auto-generated method stub
		
	}

}
