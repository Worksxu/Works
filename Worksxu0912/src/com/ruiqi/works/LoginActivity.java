package com.ruiqi.works;

import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.ruiqi.db.BackBottleDao;
import com.ruiqi.db.OrderDao;
import com.ruiqi.receiver.AutoReceiver;
import com.ruiqi.service.TimerService;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.MyActivityManager;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

//登录界面
public class LoginActivity extends Activity {
	// 用户名和密码的输入框
	private EditText et_ID, et_password;
	// 对话框
	private ProgressDialog pd;

	private boolean flag = false; // 用于判定是否自动登录
	private String passWord;
	private LinearLayout ll_phone;
	private String phone;

	private OrderDao od;
	private BackBottleDao bbd;
	int tag = 1;
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// 网络请求成功
			String strSu = (String) msg.obj;
			parseData(strSu);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login1);
		// Intent intent=new Intent(this,AutoReceiver.class);
		//
		// PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent,
		// 0);
		// AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		// am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
		// SystemClock.elapsedRealtime(), 10*1000, sender);

		od = OrderDao.getInstances(this);
		bbd = BackBottleDao.getInstances(this);
		// setNotShow();
		// 组件的初始化
		init();
		phone = getIntent().getStringExtra("mobile");
		if (phone != null) {
			et_ID.setText(phone);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initData() {
		getFlag(SPutilsKey.MOBILLE, et_ID);
		getFlag(SPutilsKey.PWD, et_password);
		if (SPUtils.contains(LoginActivity.this, SPutilsKey.FLAG)) {
			boolean flag = (Boolean) SPUtils.get(LoginActivity.this,
					SPutilsKey.FLAG, false);
			if (flag) { // 自动登录
				userLogin();
			}
		}

	}

	/**
	 * 用于判定输入框的显示
	 */

	private void getFlag(String key, EditText et) {
		if (SPUtils.contains(LoginActivity.this, key)) {// 手机号
			String str = (String) SPUtils.get(LoginActivity.this, key, "");
			if (!str.equals("")) {
				et.setText(str);
			}
		}
	}

	/**
	 * 初始化组件
	 */
	private void init() {
		pd = new ProgressDialog(LoginActivity.this);
		pd.setMessage("正在登录.....");
		ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
		ll_phone.getBackground().setAlpha(50);// 用于设置账号密码的背景透明度
		et_ID = (EditText) findViewById(R.id.et_ID);
		et_password = (EditText) findViewById(R.id.et_password);
	}

	/**
	 * 找回密码
	 */
	public void find(View view) {
		String userName = et_ID.getText().toString().trim();
		// 跳转到找回密码界面
		Intent intent = new Intent(LoginActivity.this, FindActivity.class);
		intent.putExtra("mobile", userName);
		intent.putExtra("flag", "login");
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == 2 && arg2 != null) {
			// et_ID.setText(arg2.getStringExtra("mobile"));
		}
	}

	/**
	 * 登录
	 */
	public void login(View view) {
		userLogin();
	}

	// 登录
	private void userLogin() {
		pd.show();
		// 获取输入框中的内容
		String userName = et_ID.getText().toString().trim();
		passWord = et_password.getText().toString().trim();
		String passWord64 = Base64.encodeToString(passWord.getBytes(),
				Base64.DEFAULT);// 加密
		if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(passWord)) {
			Toast.makeText(LoginActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT)
					.show();
			pd.dismiss();
		} else {// 网络请求进行登录
				// 封装参数
			RequestParams params = new RequestParams(RequestUrl.LOGIN_PATH);
			params.setConnectTimeout(6000);
			params.addBodyParameter("mobile", userName);
			params.addBodyParameter("password",
					passWord64 + (int) Math.random() * 10);
			HttpUtil.PostHttp(params, mHandler, pd);
		}

	}

	private void parseData(String strSu) {
		System.out.println("strSu=" + strSu);
		Log.e("lll钢瓶内容", strSu);
		try {
			JSONObject object = new JSONObject(strSu);

			int resultCode = object.getInt("resultCode");
			String info = object.getString("resultInfo");
			if (resultCode != 1) {
				Toast.makeText(LoginActivity.this, info, Toast.LENGTH_SHORT)
						.show();
			} else if (resultCode == 1) {

				flag = true;
				Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT)
						.show();
				// 登录成功,继续解析
				od.deleteAll();
				bbd.deleteAll();
				JSONObject object1 = object.getJSONObject("resultInfo");
				String mobile = object1.getString("mobile");
				String userName = object1.getString("username");
				String shiper_id = object1.getString("shiper_id");
				int token = object1.getInt("token");
				String shop_id = object1.getString("shop_id");
				String role = object1.getString("role");
				String shop_name = object1.getString("shop_name");
				String time = object1.getString("ctime");

//				if (tag == 1) {
//					JPushInterface.setAlias(this, "sy", new TagAliasCallback() {
//
//						@Override
//						public void gotResult(int arg0, String arg1,
//								Set<String> arg2) {
//
//							Log.e("lllll_yasdasd", arg0 + "");
//						}
//					});
//				}else{

				// Log.e("lllll_yasdasd", tag+"");
				
					JPushInterface.setAlias(this, shiper_id,
							new TagAliasCallback() {
								@Override
								public void gotResult(int arg0, String arg1,
										Set<String> arg2) {
									Log.e("lllll_yasdasd_dfdsf", tag + "");
								}
							});
//				}

				//

				// 将数据存入share中
				saveDatas(token, mobile, flag, passWord, shop_id, shiper_id,
						userName, shop_name, time);
				// 开启服务
				Intent Intentservice = new Intent(LoginActivity.this,
						TimerService.class);
				Intentservice.setAction("VIDEO_TIMER"); // 要启动的Activity
				Log.e("guangbo", "fuwu");
				// Intentservice.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				LoginActivity.this.startService(Intentservice);
				// 跳转到主页
				Intent intent = new Intent(LoginActivity.this,
						HomePageActivity.class);
				startActivity(intent);
				finish();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void saveDatas(int token, String mobile, boolean flag,
			String passWord, String shop_id, String shiper_id, String username,
			String shop_name, String time) {
		SPUtils.put(LoginActivity.this, SPutilsKey.TOKEN, token);
		SPUtils.put(LoginActivity.this, SPutilsKey.MOBILLE, mobile);
		SPUtils.put(LoginActivity.this, SPutilsKey.FLAG, flag);
		SPUtils.put(LoginActivity.this, SPutilsKey.PWD, passWord);
		SPUtils.put(LoginActivity.this, SPutilsKey.SHOP_ID, shop_id);
		SPUtils.put(LoginActivity.this, SPutilsKey.SHIP_ID, shiper_id);
		SPUtils.put(LoginActivity.this, "shipper_name", username);
		SPUtils.put(LoginActivity.this, "shop_name", shop_name);
		SPUtils.put(LoginActivity.this, "shipper_time", time);
		SPUtils.put(LoginActivity.this, SPutilsKey.LUNCHFALG, true);// 已经登录
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// 这里重写返回键
			MyActivityManager.getInstance().finishAllActivity();
			return true;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CurrencyUtils.recoveryHandler(mHandler);// 回收
	}

}
