package com.ruiqi.receiver;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.works.GrassOrder;
import com.ruiqi.works.HomePageActivity;
import com.ruiqi.works.LoginActivity;
import com.ruiqi.works.MyOrderActivity;
import com.ruiqi.xuworks.NewOrderInfoActivity;

public class JpushReceiver extends BroadcastReceiver implements ParserData{
	String orderson,tag;//type :1抢单
	Context context;
/**
 * {"send_all":1,"image":"","fromer":"15940924129","fromer_name":"15940924129","fromorder":"Dd160827999956366","sound":"","fromer_icon":""}
 * @param context
 * @param intent
 */
	@Override
	public void onReceive(Context context, Intent intent) {
		
		this.context = context;
		Bundle bundle = intent.getExtras();
		
		String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
		
		if(!TextUtils.isEmpty(type)){
			try {
				JSONObject object = new JSONObject(type);
				orderson = object.getString("fromorder");
				tag = object.getString("send_all");
				Log.e("lll_dfdfdf", tag);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		String action = intent.getAction();
		if ("cn.jpush.android.intent.NOTIFICATION_OPENED".equals(action)) {
			
			System.out.println("推送");
			
			if(SPUtils.contains(context, SPutilsKey.FLAG)){
				boolean flag = (Boolean) SPUtils.get(context, SPutilsKey.LUNCHFALG, false);
				if(flag){
					if(TextUtils.isEmpty("tag")&&tag.equals("1")){
						getData(context);
					}else{
						Intent i = new Intent(context, HomePageActivity.class);
						i.putExtras(bundle);
						//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
						context.startActivity(i);
					}
					
				}else{
					Intent i = new Intent(context, LoginActivity.class);
					i.putExtras(bundle);
					//i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
					context.startActivity(i);
				}
			}
			
        	
        	String regId = JPushInterface.getRegistrationID(context);
        	
        	
        	
        	Log.e("注册id",regId );
		}
		

	}
	// 抢单接口
	private HttpUtil httpUtil;
	private void getData(Context context) {
		// TODO Auto-generated method stub
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
//		int token = (Integer) SPUtils.get(SelfCheckActivity.this, SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.ROBORDERSURE);
//		params.addBodyParameter("type", (String) SPUtils.get(SelfCheckActivity.this, "self_type", ""));
		params.addBodyParameter("shipper_id",  (String) SPUtils.get(context,
				SPutilsKey.SHIP_ID, "error"));
		params.addBodyParameter("shipper_mobile",   (String) SPUtils.get(context,
				SPutilsKey.MOBILLE, "error"));
		params.addBodyParameter("shipper_name",  (String) SPUtils.get(context,
				"shipper_name", "error"));
		params.addBodyParameter("ordersn",  orderson);
		Log.e("llllllllllll_tongzhi", params.getStringParams().toString());
		httpUtil.PostHttp(params, 0);
	}
	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("lllll_qiandan", result);
		try {
			JSONObject object = new JSONObject(result);
			int code = object.getInt("resultCode");
			String info = object.getString("resultInfo");
			if(code == 1){
				Toast.makeText(context, info, 1).show();
				Intent order = new Intent(context,GrassOrder.class);
//				order.putExtra("ordersn", orderson);
//				order.putExtra("kid", kid);
				order.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
				context.startActivity(order);
			}else{
				Toast.makeText(context, info, 1).show();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
}
}