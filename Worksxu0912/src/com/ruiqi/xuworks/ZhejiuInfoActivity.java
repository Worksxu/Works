package com.ruiqi.xuworks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.ruiqi.bean.Bottle;
import com.ruiqi.bean.Order;
import com.ruiqi.bean.Weight;
import com.ruiqi.fragment.BackBottleDeails;
import com.ruiqi.fragment.ZhejiuBottleDeails;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.R;

public class ZhejiuInfoActivity extends BaseActivity{
	private TextView tv_number, tv_finishtime, tv_username, tv_userphone,
	tv_address, tv_shopname, tv_worker, tv_backtime, tv_yajin, tv_yuqi,
	tv_pay;
	String ordeson,data,money;
	double zhejiu = 0;
	private List<Bottle> mlist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhejiu_info_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("折旧详情");
		tv_address = (TextView) findViewById(R.id.textView_finishbottle_address);
		ordeson = getIntent().getExtras().getString("id");// 定单号
		tv_yuqi = (TextView) findViewById(R.id.textView_finishbottle_yuqi);
		tv_yajin = (TextView) findViewById(R.id.textView_finishbottle_yajin);
		tv_worker = (TextView) findViewById(R.id.textView_finishbottle_username);
		tv_userphone = (TextView) findViewById(R.id.textView_finishbottle_userphone);
		tv_username = (TextView) findViewById(R.id.textView_finishbottle_username);
		tv_shopname = (TextView) findViewById(R.id.textView_finishbottle_shopname);
		tv_pay = (TextView) findViewById(R.id.textView_finishbottle_pay);
		tv_number = (TextView) findViewById(R.id.textView_finishbottle_number);
		tv_finishtime = (TextView) findViewById(R.id.textView_finishbottle_finishtime);
		tv_backtime = (TextView) findViewById(R.id.textView_finishbottle_weight);// 残液重
		if(ZheJiuOrderListActivity.mData != null&&ZheJiuOrderListActivity.mData.size()>0){
			mlist = new ArrayList<Bottle>();
			for (int i = 0; i < ZheJiuOrderListActivity.mData.size(); i++) {
				if(ordeson.equals(ZheJiuOrderListActivity.mData.get(i).getOrdersn())){
					String time = ZheJiuOrderListActivity.mData.get(i).getTime();
					String mobile = ZheJiuOrderListActivity.mData.get(i).getMobile();
					money = ZheJiuOrderListActivity.mData.get(i).getPay_money();
					String weight = ZheJiuOrderListActivity.mData.get(i).getKid();
					String address = ZheJiuOrderListActivity.mData.get(i).getAddress();
					String user = ZheJiuOrderListActivity.mData.get(i).getUsername();
					data = ZheJiuOrderListActivity.mData.get(i).getComment();//订单串
					tv_address.setText(address);
					tv_finishtime.setText(time);
					tv_number.setText(ordeson);
					tv_userphone.setText(mobile);
					tv_pay.setText(money);
					tv_backtime.setText(weight+" kg");
					tv_worker.setText(user);
				}
			}
			try {
				JSONArray array = new JSONArray(data);
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					String weight = object.getString("good_name");
					String num = object.getString("good_num");// 数量
					String money = object.getString("money");//每个瓶钱
					mlist.add(new Bottle(weight,num,money));
					zhejiu += Integer.parseInt(num)*Double.parseDouble(money);
					tv_yajin.setText(zhejiu+"");
					
				}
				tv_yuqi.setText((Double.parseDouble(money)-zhejiu)+""+"元");
				ZhejiuBottleDeails b = new ZhejiuBottleDeails();
				Bundle bundle = new Bundle();
				bundle.putSerializable("mData", (Serializable)mlist);
				b.setArguments(bundle);
				
				getSupportFragmentManager().beginTransaction().replace(R.id.ll_finishbottle_toast, b).commit();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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

}
