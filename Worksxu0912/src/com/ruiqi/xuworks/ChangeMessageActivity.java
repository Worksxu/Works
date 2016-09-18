package com.ruiqi.xuworks;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.adapter.UserTypeAdapter;
import com.ruiqi.addreselector.MyAdapter;
import com.ruiqi.addreselector.MyListItem;
import com.ruiqi.bean.OldUserInfo;
import com.ruiqi.db.GpDao;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;
import com.ruiqi.view.popupwindow.SelectOrderInfoPopupWindow;
import com.ruiqi.view.popupwindow.SelectOrderInfoPopupWindow.PopDismiss;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.CreateOrderActivity;
import com.ruiqi.works.DownOrderActivity;
import com.ruiqi.works.R;
import com.ruiqi.works.RegisterActivity;

public class ChangeMessageActivity extends BaseActivity implements
		OnClickListener, PopDismiss {
	private EditText et_name, et_phone, et_card, et_address, et_tuijian;
	private TextView tv_type, tv_shi, tv_qu, tv_zhen, tv_cun, tv_save,
			tv_tuijian;
	private GpDao gd;
	private LinearLayout ll_change;
	private ListView lv_select_address;
	private SelectOrderInfoPopupWindow old;// 地址弹出框
	public String city = "";
	public String district = "";
	public String cun = "";
	public String province = "";
	public String shengcode, shicode, qucode, cuncode, id;
	List<MyListItem> list;// 地址list
	List<String> sList;// 用户类型
	private String flag ;
	private ProgressDialog pd;
	private List<OldUserInfo> mOldDatas; // 老用户数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_user);
		gd = GpDao.getInstances(this);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("修改信息");
		id = getIntent().getExtras().getString("kid");
		pd = new ProgressDialog(ChangeMessageActivity.this);
		pd.setMessage("正在加载");
		ll_change = (LinearLayout) findViewById(R.id.ll_change_old);
		ll_change.setVisibility(View.GONE);
		lv_select_address = (ListView) findViewById(R.id.lv_select_address);
		et_name = (EditText) findViewById(R.id.et_new_input);
		et_phone = (EditText) findViewById(R.id.et_new_inputMobile);
		et_card = (EditText) findViewById(R.id.et_new_customer_card);
		et_address = (EditText) findViewById(R.id.textView_new_detials);
		// et_tuijian = (EditText) findViewById(R.id.et_new_tuiPhone);
		// et_tuijian.addTextChangedListener(watcher);
		tv_type = (TextView) findViewById(R.id.textView_new_userType);
		tv_tuijian = (TextView) findViewById(R.id.textView_new_tuiPhone);
		tv_cun = (TextView) findViewById(R.id.textView_new_cun);
		tv_shi = (TextView) findViewById(R.id.textView_new_shi);
		tv_qu = (TextView) findViewById(R.id.textView_new_qu);
		tv_zhen = (TextView) findViewById(R.id.textView_new_zhen);
		tv_save = (TextView) findViewById(R.id.tv_newSave);
		tv_type = (TextView) findViewById(R.id.textView_new_userType);
		tv_type.setOnClickListener(this);
		tv_save.setOnClickListener(this);
		tv_shi.setOnClickListener(this);
		tv_qu.setOnClickListener(this);
		tv_cun.setOnClickListener(this);
		tv_zhen.setOnClickListener(this);
		et_name.setText((CharSequence) SPUtils.get(ChangeMessageActivity.this, "oldUserName", ""));
		et_phone.setText((CharSequence) SPUtils.get(ChangeMessageActivity.this, "oldUserMobile", ""));
//		et_address.setText((CharSequence) SPUtils.get(ChangeMessageActivity.this, "oldUserAddress", ""));
		et_card.setText((CharSequence) SPUtils.get(ChangeMessageActivity.this, "card_sn", ""));
		if(SPUtils.get(ChangeMessageActivity.this, "user_type", "").equals("1")){
//			Log.e("llll_type", SPUtils.get(DownOrderActivity.this, "use_type", ""));
			tv_type.setText("居民用户");
			flag = "1";
		}else{
			flag = "2";
			tv_type.setText("商业用户");
		}
		Cursor cursor1 = gd.readDb.query("citys",new String[] { "name" }, "code=?",new String[] { (String) SPUtils.get(ChangeMessageActivity.this, "sheng", "") }, null,null, null, null);
		cursor1.moveToFirst();
		if (cursor1.getCount() != 0) {
			 tv_shi.setText(cursor1.getString(cursor1.getColumnIndex("name")));
			 province = cursor1.getString(cursor1.getColumnIndex("name"));
			shengcode = (String) SPUtils.get(ChangeMessageActivity.this, "sheng", "");
		}
		Cursor cursor2 = gd.readDb.query("citys",new String[] { "name" }, "code=?",new String[] { (String) SPUtils.get(ChangeMessageActivity.this, "shi", "") }, null, null,null, null);
		cursor2.moveToFirst();
		if (cursor2.getCount() != 0) {
			tv_qu.setText(cursor2.getString(cursor2.getColumnIndex("name")));
			city = cursor2.getString(cursor2.getColumnIndex("name"));
			shicode = (String) SPUtils.get(ChangeMessageActivity.this, "shi", "");
			
		}
		Cursor cursor3 = gd.readDb.query("citys",new String[] { "name" }, "code=?",new String[] { (String) SPUtils.get(ChangeMessageActivity.this, "qu", "") }, null, null,null, null);
		cursor3.moveToFirst();
		if (cursor3.getCount() != 0) {
			district = cursor3.getString(cursor3.getColumnIndex("name"));
			tv_zhen.setText(cursor3.getString(cursor3.getColumnIndex("name")));
			qucode = (String) SPUtils.get(ChangeMessageActivity.this, "qu", "");
		}
		Cursor cursor4 = gd.readDb.query("citys",new String[] { "name" }, "code=?",new String[] { (String) SPUtils.get(ChangeMessageActivity.this, "cun", "") }, null, null,null, null);
		cursor4.moveToFirst();
		if (cursor4.getCount() != 0) {
			cun = cursor4.getString(cursor4.getColumnIndex("name"));
			 tv_cun.setText(cursor4.getString(cursor4.getColumnIndex("name")));
			cuncode = (String) SPUtils.get(ChangeMessageActivity.this, "cun", "");
		}

	}

	@Override
	public Activity getActivity() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public Handler[] initHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.textView_new_shi:
			initData();// 获取地址的方法(第一步)
			break;
		case R.id.textView_new_qu:
			String code_sheng = (String) SPUtils.get(
					ChangeMessageActivity.this, "shengcode", "");
			initQu(code_sheng);
			break;
		case R.id.textView_new_cun:
			String code_cun = (String) SPUtils.get(ChangeMessageActivity.this,
					"qucode", "");
			initCun(code_cun);
			break;
		case R.id.textView_new_zhen:
			String code_shi = (String) SPUtils.get(ChangeMessageActivity.this,
					"shicode", "");
			initZhen(code_shi);
			break;
		case R.id.tv_newSave:
			if(TextUtils.isEmpty(et_name.getText().toString())){
				Toast.makeText(ChangeMessageActivity.this, "请填写姓名", 1).show();
			}else if (!panDuanShiYiWei(et_phone.getText().toString().trim())){
				Toast.makeText(ChangeMessageActivity.this, "请输入正确手机号", 1).show();
			}
			else if(TextUtils.isEmpty(province)&&TextUtils.isEmpty(city)&&TextUtils.isEmpty(district)&&TextUtils.isEmpty(cun)){
				Toast.makeText(ChangeMessageActivity.this, "请选择地址", 1).show();
			}else{
			RequestParams params = new RequestParams(RequestUrl.CHANGE);
			params.addBodyParameter("mobile", et_phone.getText().toString());
			params.addBodyParameter("username", et_name.getText().toString());
			params.addBodyParameter("card_sn", et_card.getText().toString());
			params.addBodyParameter("address", tv_shi.getText().toString()+tv_qu.getText().toString()+tv_zhen.getText().toString()+tv_cun.getText().toString()+et_address.getText().toString());
			params.addBodyParameter("sheng", shengcode);
			params.addBodyParameter("shi", shicode);
			params.addBodyParameter("qu", qucode);
			params.addBodyParameter("cun", cuncode);
			params.addBodyParameter("kid", id);

			// if(tv_type.getText().toString().equals("商业用户")){
			// params.addBodyParameter("user_type", "2");
			// }else if(tv_type.getText().toString().equals("居民用户")){
//			params.addBodyParameter("user_type", flag);
			// }

			Log.e("lll", params.getStringParams().toString());
			HttpUtil.PostHttp(params, handler, pd);
			}
			break;
	
		default:
			break;
		}
	}

	private void initType() {
		// TODO Auto-generated method stub
		sList = new ArrayList<String>();
		sList.add("居民客户");
		sList.add("商业客户");
		// MyAdapter myAdapter = new MyAdapter(RegisterActivity.this, list);
		UserTypeAdapter adapter = new UserTypeAdapter(
				ChangeMessageActivity.this, sList);
		old = new SelectOrderInfoPopupWindow(ChangeMessageActivity.this,
				itemsOnClickType, lv_select_address, adapter);
		Log.e("lll", "进类");
		old.setPopDismiss(this);
		old.showAtLocation(
				ChangeMessageActivity.this.findViewById(R.id.ll_main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	private void initCun(String pcode) {
		// TODO Auto-generated method stub
		list = new ArrayList<MyListItem>();
		try {
			Cursor cursor = gd.readDb.query("citys", null, "pcode=?",
					new String[] { pcode }, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				String code = cursor.getString(cursor.getColumnIndex("code"));
				String pcode1 = cursor
						.getString(cursor.getColumnIndex("pcode"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				MyListItem myListItem = new MyListItem(code, pcode1, name);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code = cursor.getString(cursor.getColumnIndex("code"));
			String pcode1 = cursor.getString(cursor.getColumnIndex("pcode"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			MyListItem myListItem = new MyListItem(code, pcode1, name);
			list.add(myListItem);

		} catch (Exception e) {
		}

		MyAdapter myAdapter = new MyAdapter(ChangeMessageActivity.this, list);
		old = new SelectOrderInfoPopupWindow(ChangeMessageActivity.this,
				itemsOnClickCun, lv_select_address, myAdapter);
		Log.e("lll", "进类");
		old.setPopDismiss(this);
		old.showAtLocation(
				ChangeMessageActivity.this.findViewById(R.id.ll_main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	private void initZhen(String pcode) {
		// TODO Auto-generated method stub
		list = new ArrayList<MyListItem>();
		try {
			Cursor cursor = gd.readDb.query("citys", null, "pcode=?",
					new String[] { pcode }, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				String code = cursor.getString(cursor.getColumnIndex("code"));
				String pcode1 = cursor
						.getString(cursor.getColumnIndex("pcode"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				MyListItem myListItem = new MyListItem(code, pcode1, name);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code = cursor.getString(cursor.getColumnIndex("code"));
			String pcode1 = cursor.getString(cursor.getColumnIndex("pcode"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			MyListItem myListItem = new MyListItem(code, pcode1, name);
			list.add(myListItem);

		} catch (Exception e) {
		}

		MyAdapter myAdapter = new MyAdapter(ChangeMessageActivity.this, list);
		old = new SelectOrderInfoPopupWindow(ChangeMessageActivity.this,
				itemsOnClickZhen, lv_select_address, myAdapter);
		Log.e("lll", "进类");
		old.setPopDismiss(this);
		old.showAtLocation(
				ChangeMessageActivity.this.findViewById(R.id.ll_main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	private void initQu(String pcode) {
		// TODO Auto-generated method stub
		list = new ArrayList<MyListItem>();
		try {
			Cursor cursor = gd.readDb.query("citys", null, "pcode=?",
					new String[] { pcode }, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				String code = cursor.getString(cursor.getColumnIndex("code"));
				String pcode1 = cursor
						.getString(cursor.getColumnIndex("pcode"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				MyListItem myListItem = new MyListItem(code, pcode1, name);
				myListItem.setPcode(code);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code = cursor.getString(cursor.getColumnIndex("code"));
			String pcode1 = cursor.getString(cursor.getColumnIndex("pcode"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			MyListItem myListItem = new MyListItem(code, pcode1, name);
			list.add(myListItem);

		} catch (Exception e) {
		}

		MyAdapter myAdapter = new MyAdapter(ChangeMessageActivity.this, list);
		old = new SelectOrderInfoPopupWindow(ChangeMessageActivity.this,
				itemsOnClickQU, lv_select_address, myAdapter);
		Log.e("lll", "进类");
		old.setPopDismiss(this);
		old.showAtLocation(
				ChangeMessageActivity.this.findViewById(R.id.ll_main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	private void initData() {
		// TODO Auto-generated method stub

		gd = GpDao.getInstances(ChangeMessageActivity.this);
		list = new ArrayList<MyListItem>();
		try {
			Cursor cursor = gd.readDb.query("citys", null, "pcode=10", null,
					null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				String code = cursor.getString(cursor.getColumnIndex("code"));
				String pcode = cursor.getString(cursor.getColumnIndex("pcode"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				MyListItem myListItem = new MyListItem(code, pcode, name);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code = cursor.getString(cursor.getColumnIndex("code"));
			String pcode = cursor.getString(cursor.getColumnIndex("pcode"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			MyListItem myListItem = new MyListItem(code, pcode, name);
			list.add(myListItem);

		} catch (Exception e) {
		}

		MyAdapter myAdapter = new MyAdapter(ChangeMessageActivity.this, list);

		old = new SelectOrderInfoPopupWindow(ChangeMessageActivity.this,
				itemsOnClickShi, lv_select_address, myAdapter);
		Log.e("lll", "进类");
		old.setPopDismiss(this);
		old.showAtLocation(
				ChangeMessageActivity.this.findViewById(R.id.ll_main),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	// listview子项的点击 市
	private OnItemClickListener itemsOnClickShi = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			province = list.get(position).getName();
			shengcode = list.get(position).getCode();
			SPUtils.put(ChangeMessageActivity.this, "shengcode", shengcode);
			tv_shi.setText(province);
			old.dismiss();
		}
	};
	// listview子项的点击 区县
	private OnItemClickListener itemsOnClickQU = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			city = list.get(position).getName();
			shicode = list.get(position).getCode();
			SPUtils.put(ChangeMessageActivity.this, "shicode", shicode);
			tv_qu.setText(city);
			old.dismiss();
		}
	};
	// listview子项的点击 镇
	private OnItemClickListener itemsOnClickZhen = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			district = list.get(position).getName();
			qucode = list.get(position).getCode();
			SPUtils.put(ChangeMessageActivity.this, "qucode", qucode);
			tv_zhen.setText(district);
			old.dismiss();
		}
	};
	// listview子项的点击 cun
	private OnItemClickListener itemsOnClickCun = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			cun = list.get(position).getName();
			cuncode = list.get(position).getCode();
			SPUtils.put(ChangeMessageActivity.this, "cuncode", cuncode);
			tv_cun.setText(cun);
			old.dismiss();
		}
	};
	// listview子项的点击 用户
	private OnItemClickListener itemsOnClickType = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String type = sList.get(position);
			tv_type.setText(type);
			if (type.equals("居民客户")) {
				flag = "1";
				Log.e("lll", "1");
			} else if (type.equals("商业客户")) {
				flag = "2";

			}
			old.dismiss();
		}
	};

	@Override
	public void popDismissCalBack() {
		// TODO Auto-generated method stub

	}

	// 修改用户
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			if (result != null) {
				parseData(result, 2);
			} else {
				Log.e("lll", "sdsa");
			}
		}

	};

	private void parseData(String result, int i) {

		if (i == 2) {
			Log.e("lll", result);

			try {
				JSONObject obj1 = new JSONObject(result);
				int resultCode1 = obj1.getInt("resultCode");
				String info = obj1.getString("resultInfo");
				if (resultCode1 == 1) {

					// JSONObject Obj = obj1.getJSONObject("resultInfo");
					// String kid = Obj.getString("kid");
					// String ordersn = Obj.getString("kehusn");
					//
					// SPUtils.put(this, "kehusn", ordersn);// 客户sn
					// SPUtils.put(this, "kid", kid);
					T.show(ChangeMessageActivity.this, info, 1);
					if (!TextUtils.isEmpty(et_name.getText().toString())) {
						SPUtils.put(ChangeMessageActivity.this, "oldUserName",
								et_name.getText().toString());

					}
					if (!TextUtils.isEmpty(et_phone.getText())) {
						SPUtils.put(ChangeMessageActivity.this,
								"oldUserMobile", et_phone.getText().toString());
					}

					if (!TextUtils.isEmpty(province + city + district + cun
							+ et_address.getText().toString())) {
						SPUtils.put(ChangeMessageActivity.this,
								"oldUserAddress", province + city + district
										+ cun + et_address.getText().toString());
					}

					if (!TextUtils.isEmpty(et_card.getText().toString())) {
						SPUtils.put(ChangeMessageActivity.this, "card_sn",
								et_card.getText().toString());
					}

					if (!TextUtils.isEmpty(flag)) {
						SPUtils.put(ChangeMessageActivity.this, "user_type",
								flag);
					}
					if(!TextUtils.isEmpty(qucode)){
						SPUtils.put(getActivity(), "qu", qucode);
					}else{
						SPUtils.put(getActivity(), "qu", "0");
					}
					if(!TextUtils.isEmpty(cuncode)){
						SPUtils.put(getActivity(), "cun", cuncode);
					}else{
						SPUtils.put(getActivity(), "cun", "0");
					}
					if(!TextUtils.isEmpty(shengcode)){
						SPUtils.put(getActivity(), "sheng", shengcode);
					}
					if(!TextUtils.isEmpty(shicode)){
						SPUtils.put(getActivity(), "shi", shicode);
					}else{
						SPUtils.put(getActivity(), "shi", "0");
					}

//					SPUtils.put(ChangeMessageActivity.this, "old_new", "1");

					Intent intent = new Intent(ChangeMessageActivity.this,
							DownOrderActivity.class);
					startActivity(intent);
					finish();
				} else {
					T.show(ChangeMessageActivity.this, info, 1);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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

}
