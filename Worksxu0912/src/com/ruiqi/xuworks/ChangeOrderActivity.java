package com.ruiqi.xuworks;




import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.adapter.ZheKouAdapter;
import com.ruiqi.adapter.ZheKouAdapter.Send;
import com.ruiqi.adapter.ZheKouAdapter.ViewHolder;
import com.ruiqi.bean.YouHuiZheKouInfo;
import com.ruiqi.bean.ZheKouInfo;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.view.CustomListView;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.R;

public class ChangeOrderActivity extends BaseActivity implements Send ,OnCheckedChangeListener,ParserData{
	private ArrayList<String> list = new ArrayList<String>();
	private ArrayList<YouHuiZheKouInfo> mList = new ArrayList<YouHuiZheKouInfo>();
	private ZheKouAdapter adapter;
	private CustomListView lv_change;
	private RadioGroup rg_change;
	private EditText et_beizhu;
	private TextView tv_next;
	private LinearLayout ll_good,ll_problem,ll_zhengchang,ll_deditals;
	private ImageView img_good,img_bad;
	String status = "2";
	String title = "";
	String kid,orderson;
	Bundle bundle = new Bundle();
	boolean good ,bad;
	 private int a = 0;// 用来判定是否是第一次点击，0代表是，1代表不是
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_order_status);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("修改订单状态");
		kid = getIntent().getExtras().getString("kid");
		orderson = getIntent().getExtras().getString("orderson");
		img_bad = (ImageView) findViewById(R.id.img_changeorder_bad);
		img_good = (ImageView) findViewById(R.id.img_changeorder_good);
		ll_good = (LinearLayout) findViewById(R.id.ll_change_goodorder);
		ll_problem = (LinearLayout) findViewById(R.id.ll_changeorder_problem);
		ll_zhengchang = (LinearLayout) findViewById(R.id.ll_changeorder_good);
		ll_deditals = (LinearLayout) findViewById(R.id.ll_changeorder_goodDetials);
		ll_problem.setOnClickListener(this);
		ll_zhengchang.setOnClickListener(this);
		rg_change = (RadioGroup) findViewById(R.id.radioGroup_changeOrder_status);
		rg_change.setOnCheckedChangeListener(this);
		tv_next = (TextView) findViewById(R.id.tv_changeorder_next);
		tv_next.setOnClickListener(this);
		et_beizhu = (EditText) findViewById(R.id.editText_changeOrder);
		lv_change = (CustomListView) findViewById(R.id.listView_change_order);
		if(status.equals("2")){
			ll_good.setVisibility(View.GONE);
			
		}
		list.add("联系不到客户");
		list.add("配送地址错误");
		list.add("客户取消订单");
		list.add("客户拒收商品");
		for (int i = 0; i < list.size(); i++) {
			YouHuiZheKouInfo info = new YouHuiZheKouInfo();
			info.setTitle(list.get(i));
			info.setType("0");
			info.setImage(R.drawable.unchecked);
			mList.add(info);
		}
		adapter = new ZheKouAdapter(ChangeOrderActivity.this, mList);
		adapter.Setsend(this);
		lv_change.setAdapter(adapter);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_changeorder_next:
			if(status.equals("2")){
				getData();
			}else{
				if(TextUtils.isEmpty(title)||TextUtils.isEmpty(et_beizhu.getText().toString())){
					Toast.makeText(ChangeOrderActivity.this, "请完善信息后再提交", 1).show();
				}else{
				getData();
				}
			}
		case R.id.ll_changeorder_good:
			
			status  = "2";// 代表正常订单
			if(a==0){// 第一次
				bad = true;
				img_good.setImageResource(R.drawable.huise_jianup);
				ll_good.setVisibility(View.GONE);
				ll_deditals.setVisibility(View.GONE);
				a=1;
			}else if(a== 1){//第二次
				a =0;
				bad = false;
				ll_good.setVisibility(View.GONE);
				img_good.setImageResource(R.drawable.huise_jiandown);
				ll_deditals.setVisibility(View.VISIBLE);
			}
			
			Log.e("正常状态", status);
			break;
		case R.id.ll_changeorder_problem:
			good = true;
			
			if(a==0){// 第一次
				bad = true;
				img_bad.setImageResource(R.drawable.huise_jiandown);
				ll_good.setVisibility(View.VISIBLE);
				ll_deditals.setVisibility(View.GONE);
				a=1;
			}else if(a== 1){//第二次
				a= 0;
				good = false;
				img_bad.setImageResource(R.drawable.huise_jianup);
				ll_deditals.setVisibility(View.GONE);
				ll_good.setVisibility(View.GONE);
			}
			
			status = "5";// 代表问题订单
			Log.e("问题状态", status);
			break;

		default:
			break;
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

	@Override
	public Bundle bunndle(Bundle bunndle) {
		// TODO Auto-generated method stub
		title = bunndle.getString("title");
		return bunndle;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.radio_change_badOrder:
			ll_good.setVisibility(View.VISIBLE);
			status = "5";// 代表问题订单
			Log.e("lll", status);
			break;
		case R.id.radio_change_goodOrder:
			ll_good.setVisibility(View.GONE);
			status  = "2";// 代表正常订单
			Log.e("lll", status);
			break;

		default:
			break;
		}
	}

	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("lll", result);
		try {
			JSONObject object = new JSONObject(result);
			int code = object.getInt("resultCode");
			String info = object.getString("resultInfo");
			if(code == 1){
				Toast.makeText(ChangeOrderActivity.this, info, 1).show();
				Intent intent = new Intent();
//				
				bundle.putString("beizhu", title+"("+et_beizhu.getText().toString()+")");
				
				bundle.putString("status", status);
				intent.putExtra("change", bundle);
				setResult(2, intent);
				this.finish();
				
			}else{
				Toast.makeText(ChangeOrderActivity.this, info, 1).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private HttpUtil httpUtil;
	private Click click;
	private void getData() {
		// TODO Auto-generated method stub
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(ChangeOrderActivity.this, SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.EDITOR_ORDER);
		params.addBodyParameter("ordersn", orderson);
		params.addBodyParameter("kid", kid);
		params.addBodyParameter("status", status);
		params.addBodyParameter("Token",  token+"");
		if(!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(et_beizhu.getText().toString())){
			params.addBodyParameter("comment",  title+et_beizhu.getText().toString());
		}
		
		Log.e("lll", params.getStringParams().toString());
		httpUtil.PostHttp(params, 0);
	}
	public interface Click{
		void chuan();
	}
	public void SetClick(Click click){
		this.click = click;
		
	}
	
}
