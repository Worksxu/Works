package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.adapter.ChaiTypePopupAdapter1;
import com.ruiqi.adapter.TypePopupAdapter;
import com.ruiqi.bean.OldUserInfo;
import com.ruiqi.db.GpDao;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;
import com.ruiqi.view.popupwindow.SelectOrderInfoPopupWindow;
import com.ruiqi.works.CreateOrderActivity;
import com.ruiqi.works.DownOrderActivity;
import com.ruiqi.works.R;



import com.ruiqi.works.RegisterActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreateOrderFragment extends Fragment implements OnClickListener{
	private View view;
	private TextView tv_name,tv_phone,tv_address,tv_create,tv_old;
	private EditText et_oldUser;
	private ImageView img_oldUser;
	private LinearLayout ll_oldUser;
	private ProgressDialog pd;
	private List<OldUserInfo> mOldDatas; //老用户数据
	private GpDao gd;// 数据库
	private RelativeLayout rl_title;
	private String oldUserMobile;// 老用户手机号
	String address;
	String deduction;
	String create_phone;//记录输入的手机号
	public static String shengcode, shicode, qucode, cuncode, detail; //地址的变量
	public static String TAG = CreateOrderFragment.class.getName();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.create_order_layout, null);
		init();
		return view;
	}
	private void init() {
		// TODO Auto-generated method stub
//		gd = GpDao.getInstances(getActivity());
		
		pd = new ProgressDialog(getActivity());
		pd.setMessage("正在加载");
//		SPutilsKey.neworold=2;
//		SPutilsKey.type = 2;
		rl_title = (RelativeLayout) view.findViewById(R.id.rl_title);
		rl_title.setVisibility(View.VISIBLE);
		tv_create = (TextView) view.findViewById(R.id.tv_createNew);
		tv_old = (TextView) view.findViewById(R.id.tv_old);
		tv_old.setOnClickListener(this);
		tv_create.setOnClickListener(this);
		tv_address  = (TextView) view.findViewById(R.id.textView_olderAddress);
		tv_phone  = (TextView) view.findViewById(R.id.textView_olderPhone);
		tv_name  = (TextView) view.findViewById(R.id.textView_olderName);
		et_oldUser = (EditText) view.findViewById(R.id.editText_oldUser);
		img_oldUser = (ImageView) view.findViewById(R.id.imageView_oldUser);
		img_oldUser.setOnClickListener(this);
		ll_oldUser = (LinearLayout) view.findViewById(R.id.ll_oldUser);
		ll_oldUser.setOnClickListener(this);
		
				
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imageView_oldUser:
			// 请求网络，加载数据
			initOldData(v);
			break;
		case R.id.tv_old:
			Intent  down = new Intent(getActivity(),DownOrderActivity.class);
			startActivity(down);
			break;
		case R.id.tv_createNew:
			Intent intent = new Intent(getActivity(),RegisterActivity.class);
//			intent.putExtra("phone", create_phone);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
	/**
	 * 老用户请求数据
	 */
	private void initOldData(View v) {
		// 消失软键盘
		CurrencyUtils.dissMiss(getActivity(), v);
		oldUserMobile = et_oldUser.getText().toString().trim();
		// 判断手机号是否输入正确
//		if (!IsPhone.isOrNotPhone(oldUserMobile)) {
//			T.showShort(getActivity(), "电话号码有误，请重新输入");
//		} else {
//			requestHttp();
//		}
		if (oldUserMobile.isEmpty()) {
			T.showShort(getActivity(), "请输入手机号");
		} else {
			if(panDuanShiYiWei(oldUserMobile)){
				requestHttp();
			}else{
				T.showShort(getActivity(), "请输入正确手机号");
			}
			
		}

	}
	/**
	 * 判断是否为十一位数
	 */
	public boolean panDuanShiYiWei(String str) {

		if(str.length()==11){
			return true;
		}
		return false;
	}

	// 请求网络
	private void requestHttp() {
		pd.show();
		mOldDatas = new ArrayList<OldUserInfo>();
		int token = (Integer) SPUtils.get(getActivity(),
				SPutilsKey.TOKEN, 0);
		// 请求网络
		RequestParams params = new RequestParams(RequestUrl.USER_INFO);
		params.addBodyParameter(SPutilsKey.MOBILLE, oldUserMobile);
		params.addBodyParameter("kid", "");
		params.addBodyParameter(SPutilsKey.TOKEN, token + "");
		HttpUtil.PostHttp(params, oldHandler, pd);
	}
	// 老用户数据
	private Handler oldHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			if (result != null) {
				parseData(result, 1);
			}
		}

	};
private SelectOrderInfoPopupWindow old;
	
	private String dataString;
	
	

	private void parseData(String result, int i) {
		Log.e("lll", result);
	if (i == 1) {
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			
			if (resultCode == 1) {
				ll_oldUser.setVisibility(view.VISIBLE);
				// 继续解析
				JSONObject obj1 = obj.getJSONObject("resultInfo");
				String name = obj1.getString("user_name");
				String userPhone = obj1.getString("mobile_phone");
				
				if(obj1.getString("address").isEmpty()){
					 address = obj1.getString("sheng_name")+obj1.getString("shi_name")+obj1.getString("qu_name")+obj1.getString("cun_name");
				}else{
					 address = obj1.getString("address");
				}
				String kid = obj1.getString("kid");
				String sheng = obj1.getString("sheng");
				String shi = obj1.getString("shi");
				String qu = obj1.getString("qu");
				String cun = obj1.getString("cun");
				String card_sn = obj1.getString("card_sn");
				String user_type = obj1.getString("ktype");// 1代表居民,2代表商业用户
				String safe_time = obj1.getString("safe_date");// 安检时间
				String safe_detials = obj1.getString("orderlist");// 安检内容
				String comment = obj1.getString("comment");// 备注
				String BuyTime = obj1.getString("buy_time");// 购买次数
				Log.e("lll", name);
//				if((obj1.get("yhdata") instanceof JSONArray)){
//					 deduction = obj1.getString("yhdata");// 抵扣券
//				}else{
//					deduction = "";
//					T.show(getActivity(), "没有可使用的抵扣券", 1);
//				}
				if(obj1.has("yhdata")){
					deduction = obj1.getString("yhdata");// 抵扣券
				}else{
					deduction = "";
					T.show(getActivity(), "没有可使用的抵扣券", 1);
				}
				
				tv_address.setText(address);
				tv_name.setText(name);
				tv_phone.setText(userPhone);
//			Log.e("lll", deduction);
				SPUtils.put(getActivity(), "oldUserName", name);
				SPUtils.put(getActivity(), "oldUserMobile",userPhone);
				SPUtils.put(getActivity(), "oldUserAddress", address);
				SPUtils.put(getActivity(), "sheng", sheng);
				SPUtils.put(getActivity(), "shi", shi);
				if(!BuyTime.equals("0")){
					SPUtils.put(getActivity(), "old_new", "2");// 老客户
				}else{
					SPUtils.put(getActivity(), "old_new", "1");// 新客户
				}
				
				
				SPUtils.put(getActivity(), "qu", qu);
				SPUtils.put(getActivity(), "cun", cun);
				SPUtils.put(getActivity(), "card_sn", card_sn);
				SPUtils.put(getActivity(), "user_type", user_type);
				SPUtils.put(getActivity(), "safe_time", safe_time);
				SPUtils.put(getActivity(), "safe_detials", safe_detials);
				SPUtils.put(getActivity(), "BuyTime", BuyTime);
				SPUtils.put(getActivity(), "kid", kid);
				SPUtils.put(getActivity(), "deduction", deduction);// 抵扣券的data串
				SPUtils.put(getActivity(), "comment", comment);// 用户备注信息
				//拿到客户的kid和余瓶data串
				
				if((obj1.get("bottle_data") instanceof JSONArray)){
					JSONArray jsa=obj1.getJSONArray("bottle_data");
					for (int j = 0; j < jsa.length(); j++) {
						JSONObject jso=jsa.getJSONObject(j);
						if(!jso.getString("good_num").equals("0")){
							SPutilsKey.kehuyou=1;//客户有瓶
							dataString=obj1.getJSONArray("bottle_data").toString();
							break;
						}else{
							dataString="";
							SPutilsKey.kehuyou=2;//客户没有瓶
						}
					}
				}else{
					SPutilsKey.GANGPINGZONGSHU=0;
					dataString="";
					SPutilsKey.kehuyou=2;//客户没有瓶
				}
			tv_old.setVisibility(View.VISIBLE);
			tv_create.setVisibility(View.GONE);
				 //得到新打开Activity关闭后返回的数据
                //第二个参数为请求码，可以根据业务需求自己编号
//                startActivityForResult(new Intent(CreateOrderActivity.this, RegisterActivity.class), 1);
			}else{
				tv_old.setVisibility(View.GONE);
				tv_create.setVisibility(View.VISIBLE);
				String info = obj.getString("resultInfo");
				create_phone = et_oldUser.getText().toString();// 记录新手机号,以便创建新客户
				Toast.makeText(getActivity(), info, 1).show();
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		}
	}
	

}
