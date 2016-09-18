package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.adapter.BottleToShopAdapter;
import com.ruiqi.adapter.BottleToShopAdapter.ViewHolder;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.view.SwipeMenuListView;
import com.ruiqi.works.R;
import com.ruiqi.xuworks.SelfCheckActivity;

import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BottleToShopFragment extends BaseFragment implements ParserData{
	private List<TableInfo> mWeightDatas;//重瓶的数据
	private List<TableInfo> mNullDatas;//空瓶的数据
	private List<TableInfo> mDatas;//废瓶的数据
	private BottleToShopAdapter adapter;
	private TextView tv_weight,tv_null,tv_zhejiu,tv_commint;
	private ListView lv_weight,lv_null,lv_zhejiu;
	private ScrollView sv_shop;
	int click;
	private HashMap<String, Integer> map = new HashMap<String, Integer>();
	@Override
	public View initView() {
		// TODO Auto-generated method stub
		View view =  LayoutInflater.from(getContext()).inflate(R.layout.bottletoshop_fragment, null);
		sv_shop = (ScrollView) view.findViewById(R.id.scrollView_bottletoshop);
		sv_shop.smoothScrollTo(0, 20);
		tv_null = (TextView) view.findViewById(R.id.textView_nullTotal);
		tv_weight = (TextView) view.findViewById(R.id.textView_weightTotal);
		tv_zhejiu = (TextView) view.findViewById(R.id.textView_zhejiuTotal);
		lv_null = (ListView) view.findViewById(R.id.lv_null);
		lv_weight = (ListView) view.findViewById(R.id.lv_weight);
		lv_zhejiu = (ListView) view.findViewById(R.id.lv_zhejiu);
		tv_commint = (TextView) view.findViewById(R.id.tv_bottleshop_sure);
		tv_commint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				adapter.notifyDataSetChanged();
				
				Log.e("llll", mWeightDatas.get(0).getKid());
				Log.e("llll", mNullDatas.get(0).getKid());
				Log.e("llll", mDatas.get(0).getKid());
			
			}
		});
		initData();
		return view;
	}
	private HttpUtil httpUtil;
	private void initData() {
		// TODO Auto-generated method stub
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		int token = (Integer) SPUtils.get(getActivity(), SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.STOCK_LIST);
		params.addBodyParameter("shipper_id", (String) SPUtils.get(getActivity(), SPutilsKey.SHIP_ID, ""));
		params.addBodyParameter("Token",  token+"");
		httpUtil.PostHttp(params, 0);
	}

	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("lll", result);
		mWeightDatas = new ArrayList<TableInfo>();
		mNullDatas = new ArrayList<TableInfo>();
		mDatas = new ArrayList<TableInfo>();
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				JSONObject obj1 = obj.getJSONObject("resultInfo");
				String total = obj1.getString("total");
				JSONArray array = obj1.getJSONArray("data");
				for(int i=0;i<array.length();i++){
					System.out.println("==================");
					JSONObject object = array.getJSONObject(i);
					String title = object.getString("title");
					String typename = object.getString("typename");
					int num = object.getInt("num");
					
					if("重瓶".equals(typename)){
						mWeightDatas.add(new TableInfo(title, num+"","0"));
						 
					}else if("空瓶".equals(typename)){
						mNullDatas.add(new TableInfo(title, num+"","0"));
					}else {
						mDatas.add(new TableInfo(title, num+"","0"));
					}
					
				}
				if(mWeightDatas != null){
					int weight = 0;
					for (int i = 0; i < mWeightDatas.size(); i++) {
						weight += Integer.parseInt(mWeightDatas.get(i).getOrderMoney());
					}
					tv_weight.setText("重瓶合计"+" "+weight);
					adapter = new BottleToShopAdapter(getActivity(), mWeightDatas);
					lv_weight.setAdapter(adapter);
					CurrencyUtils.setListViewHeightBasedOnChildren(lv_weight);
					
				}
				if(mNullDatas != null){
					int weight = 0;
					for (int i = 0; i < mNullDatas.size(); i++) {
						weight += Integer.parseInt(mNullDatas.get(i).getOrderMoney());
					}
					tv_null.setText("空瓶合计"+" "+weight);
					adapter = new BottleToShopAdapter(getActivity(), mNullDatas);
					lv_null.setAdapter(adapter);
					CurrencyUtils.setListViewHeightBasedOnChildren(lv_null);
				}
				if(mDatas != null){
					int weight = 0;
					for (int i = 0; i < mDatas.size(); i++) {
						weight += Integer.parseInt(mDatas.get(i).getOrderMoney());
					}
					tv_zhejiu.setText("折旧瓶合计"+" "+weight);
					adapter = new BottleToShopAdapter(getActivity(), mDatas);
					lv_zhejiu.setAdapter(adapter);
					CurrencyUtils.setListViewHeightBasedOnChildren(lv_zhejiu);
				}
				
				
				
				
			}
		}catch (Exception e) {
			}
	}



}
