package com.ruiqi.xuworks;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.a.a.o;

import com.ruiqi.adapter.ChaiSelfAdapter;
import com.ruiqi.bean.SelfContent;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.R;

public class SelfCheckActivity extends BaseActivity implements ParserData{
	private TextView tv_anjian, tv_back, tv_title;
	Bundle bundle;
	String flag,kid,ordeson,type;
	private RelativeLayout rl_button_paizhao;
	private ListView lv_self;
	private ChaiSelfAdapter commonAdapter;
	private ArrayList<SelfContent> mDatas=new ArrayList<SelfContent>();
	public static List<String> numbers = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.self_check);
		init();
		
	}

	private void init() {
		// TODO Auto-generated method stub
		bundle = new Bundle();
		bundle = getIntent().getBundleExtra("flag");
		if(!TextUtils.isEmpty(bundle.getString("flag"))){
			flag = bundle.getString("flag");
		}
		if(!TextUtils.isEmpty(bundle.getString("kid"))){
			kid = bundle.getString("kid");
		}
		if(!TextUtils.isEmpty(bundle.getString("orderson"))){
			ordeson = bundle.getString("orderson");
		}
		if(!TextUtils.isEmpty(bundle.getString("type"))){
			type = bundle.getString("type");
			SPUtils.put(SelfCheckActivity.this, "self_type", type);
		}
		
	
		setTitle("安全检查");
		tv_anjian = (TextView) findViewById(R.id.tv_anjian);
		lv_self = (ListView) findViewById(R.id.lv_self);
		rl_button_paizhao = (RelativeLayout) findViewById(R.id.rl_button_paizhao);
		tv_anjian.setText(TextUtils.isEmpty(flag)?"暂无":flag);
		rl_button_paizhao.setOnClickListener(this);
		getData();
	}
	
	private HttpUtil httpUtil;
	private void getData() {
		// TODO Auto-generated method stub
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(SelfCheckActivity.this, SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.SELF_REPORT);
		params.addBodyParameter("type", (String) SPUtils.get(SelfCheckActivity.this, "self_type", ""));
//		params.addBodyParameter("Token",  token+"");
		httpUtil.PostHttp(params, 0);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_button_paizhao:
			SPUtils.put(SelfCheckActivity.this, "self_kid", kid);
			if(!TextUtils.isEmpty(ordeson)){
				SPUtils.put(SelfCheckActivity.this, "self_orderson", ordeson);
			}
			
			Intent photo = new Intent(SelfCheckActivity.this,SelfPhotoActivity.class);
			startActivity(photo);
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
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("lll", result);
		try {
			JSONObject object = new JSONObject(result);
			int code = object.getInt("resultCode");
			if(code == 1){
				JSONArray array = object.getJSONArray("resultInfo");
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj = array.getJSONObject(i);
					SelfContent sc = new SelfContent();
					sc.setContent(obj.getString("listorder")+"."+obj.getString("title"));
					sc.setIcon(R.drawable.xiaoqi_hui);
					sc.setId(obj.getString("id"));
					mDatas.add(sc);
				}
				commonAdapter = new ChaiSelfAdapter(SelfCheckActivity.this, mDatas);
				lv_self.setAdapter(commonAdapter);
				lv_self.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						//改变小旗的状态
						if(mDatas.get(position).getIcon()==R.drawable.xiaoqi_hui){
							mDatas.get(position).setIcon(R.drawable.xiaoqi_lan);
							commonAdapter.notifyDataSetChanged();
							//将编号存入数组中
							numbers.add(mDatas.get(position).getId());
						}else{
							mDatas.get(position).setIcon(R.drawable.xiaoqi_hui);
							commonAdapter.notifyDataSetChanged();
							//删除对应的编号
							numbers.remove(mDatas.get(position).getId());
						}
					}
				});
			}else{
				
				String info = object.getString("resultInfo");
				Toast.makeText(SelfCheckActivity.this, info, 1).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
