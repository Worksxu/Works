package com.ruiqi.works;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.google.gson.Gson;
import com.ruiqi.adapter.MainUISwitchCallBack;
import com.ruiqi.bean.GangPingList;
import com.ruiqi.db.GpDao;
import com.ruiqi.fragment.CreateOrderFragment;
import com.ruiqi.fragment.GetGoodsFragment;
import com.ruiqi.fragment.JumpFragment;
import com.ruiqi.fragment.PersonaFragment;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.MyActivityManager;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.view.CustomFootView;
import com.ruiqi.xuworks.SelfPhotoActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class HomePageActivity extends FragmentActivity implements OnClickListener,ParserData,TabHost.OnTabChangeListener,MainUISwitchCallBack{

	 private FragmentTabHost tabHost;
	    private static Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);
		init();
	}
	private void init() {
		 setupNavigation();
	        prepareNavigationBar();
	}
	 /**
     * 添置 Tabs 作为导航。
     */
    private void prepareNavigationBar() {


        addTab(
                newTabWithLabelAndIcon(JumpFragment.TAG,
                        getString(R.string.message),
                        R.drawable.nav_tab_home),
                        JumpFragment.class, null);

        addTab(
                newTabWithLabelAndIcon(CreateOrderFragment.TAG,
                        getString(R.string.create),
                        R.drawable.nav_tab_create),
                        CreateOrderFragment.class, null);
        addTab(newTabWithLabelAndIcon(GetGoodsFragment.TAG,
                getString(R.string.getgoods),
                R.drawable.nav_tab_getgoods),
                GetGoodsFragment.class, null);

        addTab(newTabWithLabelAndIcon(PersonaFragment.TAG,
                getString(R.string.myself),
                R.drawable.nav_tab_myself),
                PersonaFragment.class, null);

    }
    private TabHost.TabSpec newTabWithLabelAndIcon(String tag, String label, int iconResid) {
        View indicator = getLayoutInflater().inflate(R.layout.tab_main,
                tabHost.getTabWidget(), false);

        ImageView icon = (ImageView) indicator.findViewById(R.id.icon);
        TextView title = (TextView) indicator.findViewById(R.id.title);

        icon.setImageDrawable(getResources().getDrawable(iconResid));
        title.setText(label);

        return tabHost.newTabSpec(tag).setIndicator(indicator);
    }
    public void addTab(TabHost.TabSpec tabSpec, Class<?> clazz, Bundle args) {
        String tag = tabSpec.getTag();
        // FIXME(XCL): Don't use the method add( TabSpec ) of Supperclass class
        //             Tabhost to add some Tab.
        tabHost.addTab(tabSpec, clazz, args);
    }

    private void activateTab(final String tag) {
        tabHost.setCurrentTabByTag(tag);
    }
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getWorkerBottle();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	 private void setupNavigation() {
	        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
	        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
	        tabHost.setOnTabChangedListener(this);
	    }
	
	public void getWorkerBottle(){
		int token = (Integer) SPUtils.get(HomePageActivity.this, SPutilsKey.TOKEN, 0);
		String shop_id = (String) SPUtils.get(this, SPutilsKey.SHOP_ID, "error");
		String shiper_id = (String) SPUtils.get(this, SPutilsKey.SHIP_ID, "error");
		RequestParams params = new RequestParams(RequestUrl.LOGIN_BOTTLELIST);
		
		SPUtils.get(this, SPutilsKey.SHIP_ID, "");
		
//		params.addBodyParameter("shop_id", shop_id);
		
		params.addBodyParameter("shipper_id", shiper_id);
		params.addBodyParameter("Token", token+"");
		Log.e("llll_params", params.getStringParams().toString());
		HttpUtil httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
		httpUtil.PostHttp(params, 1);
	}
	@Override
	public void sendResult(final String result, int what) {
		// TODO Auto-generated method stub
		Log.e("lll", "钢瓶库存"+result);
		 final GpDao gd=GpDao.getInstances(this);
		 Thread t = new Thread(new Runnable(){  
	            public void run(){  
	            	try {
	        			JSONObject jsob=new JSONObject(result);
	        			Gson gson=new Gson();
	        			if(jsob.getInt("resultCode")==1){
	        				GangPingList gangPingList=gson.fromJson(result,  GangPingList.class);
	        				gd.writeDb.delete("gangpingxinxi", null, null);
	        				for (int i = 0; i < gangPingList.getResultInfo().size(); i++) {
	        					gd.insertGangPingXinXi(gangPingList.getResultInfo().get(i).getXinpian(), gangPingList.getResultInfo().get(i).getNumber(),gangPingList.getResultInfo().get(i).getType_name(),gangPingList.getResultInfo().get(i).getIs_open(),gangPingList.getResultInfo().get(i).getType());
	        					Log.e("fdsfdsfdsfdsf", gangPingList.getResultInfo().get(i).getType());
	        					
							}
	        			}else{
//	        				Toast.makeText(HomePageActivity.this, jsob.getString("resultInfo"), 1).show();
	        				gd.writeDb.delete("gangpingxinxi", null, null);
	        			}
	        		} catch (JSONException e) {
	        			e.printStackTrace();
	        		}
	            }});  
	        t.start();  
	}
	/**
	 * 按两次返回键退出
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			//调用双击退出程序
			exit();
		}
		return false;
	}    
	/**
	 * 双击退出程序功能
	 */
	private static boolean isExit = false; //判定是否退出
	private void exit() {
		Timer tExit = null;
		if(isExit==false){
			isExit = true; //准备退出
			Toast.makeText(HomePageActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; //取消退出
				}
			}, 2000);//如果两秒中没有按下，启动定时器，取消上次执行的操作
		}else{//退出系统
			MyActivityManager.getInstance().finishAllActivity();
			System.exit(0);
			Process.killProcess(0);
		}
	}
	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void switchTab(String tag) {
		// TODO Auto-generated method stub
		if (JumpFragment.TAG.equals(tag)) {
        } else if (CreateOrderFragment.TAG.equals(tag)) {
        } else if (GetGoodsFragment.TAG.equals(tag)) {
        } else if (PersonaFragment.TAG.equals(tag)) {
        } else {
            return;
        }

        activateTab(tag);
	}

}
