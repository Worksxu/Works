package com.ruiqi.fragment;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.adapter.CommonAdapter;
import com.ruiqi.adapter.ViewHolder;
import com.ruiqi.bean.Pensoral;
import com.ruiqi.bean.Verson;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.AppUtils;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;
import com.ruiqi.view.RoundImageView;
import com.ruiqi.view.UpdateManager;
import com.ruiqi.view.popupwindow.SelectPicPopupWindow;
import com.ruiqi.works.ArrearsUserActivity;
import com.ruiqi.works.BackBottleActivity;
import com.ruiqi.works.FindActivity;
import com.ruiqi.works.LoginActivity;
import com.ruiqi.works.MainActivity;
import com.ruiqi.works.MyMoneyActivity;
import com.ruiqi.works.PersonalActivity;
import com.ruiqi.works.R;
import com.ruiqi.works.StockActivity;
import com.ruiqi.xuworks.BackShopActivity;
import com.ruiqi.xuworks.ChangePassWardActivity;
import com.ruiqi.xuworks.PeijianBackShopActvity;
import com.ruiqi.xuworks.TurnOverActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonaFragment extends Fragment implements OnClickListener,ParserData{
	public static String TAG = PersonaFragment.class.getName();
	View view;
private ListView lv_top,lv_buttom;
	
	//适配器
	private CommonAdapter<Pensoral> topAdapter;
	private CommonAdapter<Pensoral> buttomAdapter;
	
	//数据源
	private List<Pensoral> mTopDatas;
	private List<Pensoral> mButtomDatas;
	
	private ImageView img_personal;
	private TextView account_back,tv_shop_name,tv_time;
	private RelativeLayout rl_personal_select; //修改手机号
	private TextView tv_personal_selectHend;
	private RoundImageView riv_personal_head;
	
	private int token;
	private String mobile,name;
	
	private ProgressDialog pd;
	
	private SelectPicPopupWindow mPopupWindow;
	
	private LinearLayout rl_personal_pwd_select,ll_info,ll_gengxin;
	
	private TextView tv_personal_name,tv_personal_phone;
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.activity_personal, null);
		initView();
		//数据的初始化
		initData();
		
		
		buttomAdapter = setDataToListView1(buttomAdapter, mButtomDatas);
//		topAdapter = setDataToListView(topAdapter, mTopDatas);
//		lv_top.setAdapter(topAdapter);
		lv_buttom.setAdapter(buttomAdapter);
		lv_buttom.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = null;
				switch (position) {
				
				case 0:
					intent = new Intent(getActivity(), MyMoneyActivity.class);
					Log.e("lll", "财务");
					break;
				case 1:
					intent = new Intent(getActivity(), StockActivity.class);
					Log.e("lll", "库存");
					break;
				case 2:
					intent = new Intent(getActivity(), ArrearsUserActivity.class);
					Log.e("lll", "欠款");
					break;
				case 3:
					intent = new Intent(getActivity(), BackShopActivity.class);
					Log.e("lll", "回库");
					break;
				case 4:
					intent = new Intent(getActivity(), TurnOverActivity.class);
					Log.e("lll", "上缴支出");
					break;

				default:
					break;
				}
				startActivity(intent);
			}
		});
		return view;
	}
	private void initData() {
		pd.setMessage("正在退出，请稍后......");
		
		mTopDatas = new ArrayList<Pensoral>();
		mTopDatas.add(new Pensoral("隶属", ""));
		mTopDatas.add(new Pensoral("岗位", ""));
		mTopDatas.add(new Pensoral("入职时间", ""));
		
		
		mButtomDatas = new ArrayList<Pensoral>();
		mButtomDatas.add(new Pensoral("我的财务", R.drawable.huise_jiantou));
		mButtomDatas.add(new Pensoral("我的库存", R.drawable.huise_jiantou));
		mButtomDatas.add(new Pensoral("欠款账户", R.drawable.huise_jiantou));
		mButtomDatas.add(new Pensoral("回库", R.drawable.huise_jiantou));
		mButtomDatas.add(new Pensoral("上缴支出", R.drawable.huise_jiantou));
		
		
		token = (Integer) SPUtils.get(getActivity(), "token", 0);
		mobile = (String) SPUtils.get(getActivity(), "mobile", "error");
		name = (String) SPUtils.get(getActivity(), "shipper_name", "error");
		
		tv_personal_name.setText(name);
//		tv_personal_phone.setText(mobile);
	}

	@SuppressLint("NewApi")
	private void initView() {
		pd = new ProgressDialog(getActivity());
		ll_gengxin = (LinearLayout) view.findViewById(R.id.ll_gengxin);
		ll_gengxin.setOnClickListener(this);
		lv_top = (ListView)view.findViewById(R.id.lv_top);
		lv_buttom = (ListView) view.findViewById(R.id.lv_buttom);
		img_personal = (ImageView) view.findViewById(R.id.riv_personal_head);
//		iv_personal_back = (ImageView) findViewById(R.id.iv_personal_back);
		account_back = (TextView) view.findViewById(R.id.account_back);
		ll_info = (LinearLayout) view.findViewById(R.id.ll_workinfo);
		ll_info.getBackground().setAlpha(50);
		tv_shop_name = (TextView) view.findViewById(R.id.tv_workShop);
		tv_shop_name.setText((CharSequence) SPUtils.get(getActivity(), "shop_name", ""));
		
		tv_time = (TextView) view.findViewById(R.id.tv_workTime);
		tv_time.setText((CharSequence) SPUtils.get(getActivity(), "shipper_time", ""));
		rl_personal_pwd_select = (LinearLayout) view.findViewById(R.id.rl_personal_pwd_select);
		rl_personal_pwd_select.setOnClickListener(this);

		account_back.setOnClickListener(this);

		img_personal.setOnClickListener(this);
		tv_personal_name = (TextView) view.findViewById(R.id.tv_personal_name);
		
	}
	
	/**
	 * 初始化填充数据的adapter
	 */
	private CommonAdapter<Pensoral> setDataToListView(CommonAdapter<Pensoral> adapter,List<Pensoral> mDatas){
		adapter = new CommonAdapter<Pensoral>(getActivity(), mDatas, R.layout.personal_content) {
			
			@Override
			public void convert(ViewHolder helper, Pensoral item,int position) {
				helper.setText(R.id.tv_personal_left, item.getTitle());
				helper.setText(R.id.tv_personal_right, item.getContent());
				
			}
		};
		return adapter;
	}
	private CommonAdapter<Pensoral> setDataToListView1(CommonAdapter<Pensoral> adapter,List<Pensoral> mDatas){
		adapter = new CommonAdapter<Pensoral>(getActivity(), mDatas, R.layout.personal_click) {
			
			@Override
			public void convert(ViewHolder helper, Pensoral item,int position) {
				helper.setText(R.id.tv_personal_left, item.getTitle());
				helper.setImageResource(R.id.tv_personal_right, item.getI());
				
			}
		};
		return adapter;
	}
	/**
	 * 退出账号功能
	 */
	private void accountExit(){
		new AlertDialog.Builder(getActivity())
		.setTitle("确定退出？")
		.setMessage("继续退出？")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				pd.show();
				//请求账号退出接口
				RequestParams params = new RequestParams(RequestUrl.PWD);
				params.addBodyParameter(SPutilsKey.TOKEN, token+"");
				params.addBodyParameter(SPutilsKey.MOBILLE, mobile);
				HttpUtil.PostHttp(params, handler, pd);
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
			}
		})
		.create().show();
	}
	/**
	 * 数据解析
	 * @param str
	 */
	private void parseData(String str) {
		System.out.println("str="+str);
		//json解析
		try {
			JSONObject obj = new JSONObject(str);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				//继续解析
				//JSONObject object = obj.getJSONObject("resultInfo");
				T.showShort(getActivity(), "退出成功");
				//修改flag为false
				SPUtils.put(getActivity(), SPutilsKey.FLAG, false);
				SPUtils.put(getActivity(), SPutilsKey.LUNCHFALG, false);
				//跳转到登录界面
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
				getActivity().finish();
			}else{
				T.showShort(getActivity(), "退出失败");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.iv_personal_back:
//			finish();
//			break;
		case R.id.account_back://账号退出
			accountExit();
			break;
		case R.id.ll_gengxin://检测更新
			getData();
			break;
			
		case R.id.riv_personal_head://更换头像
			mPopupWindow = new SelectPicPopupWindow(getActivity(), itemsOnClick);
			//设置显示位置
			mPopupWindow.showAtLocation(getActivity().findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.rl_personal_pwd_select://修改密码
			String mobile = (String) SPUtils.get(getActivity(), SPutilsKey.MOBILLE, "");
			Intent intent = new Intent(getActivity(), ChangePassWardActivity.class);
//			intent.putExtra("mobile",mobile );
//			intent.putExtra("flag", "personal");
			startActivity(intent);
			break;
			

		default:
			break;
		}
	}
	
	
	
	private OnClickListener itemsOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_take_photo://拍照
				takePhoto();
				break;
			case R.id.btn_pick_photo://从相册中选择
				getImageFromAlbum();
				break;

			default:
				break;
			}
		}
	};
	/**
	 * 从相册中获取图片
	 */
	private void getImageFromAlbum(){
		 Intent intent = new Intent(Intent.ACTION_PICK);  
	     intent.setType("image/*");//相片类型  
	     startActivityForResult(intent, 2); 
	}
	/**
	 * 拍照
	 */
	private void takePhoto(){
		//验证sd卡是否正确安装
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			//判断父目录是否存在
			//得到根目录
			File paths = new File(SPutilsKey.PATH);
			File path = new File(SPutilsKey.PATH_NAME);
			if(!paths.exists()){//不存在
				//创建
				paths.mkdirs();
			}
			//跳转到系统拍照的activity
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE,null);
			//存储到path路径下
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(path));
			//跳转到拍照界面
			startActivityForResult(intent, 1);
			//拍照完成后上传
		}else{
			Toast.makeText(getActivity(), "请检查SD卡是否正确安装", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == getActivity().RESULT_CANCELED){
		}else if(requestCode == 1&&resultCode ==getActivity().RESULT_OK){
			mPopupWindow.dismiss();
			
			CurrencyUtils.setImageFromSdCard(SPutilsKey.PATH_NAME, riv_personal_head);
			
			//上传到服务器
			
		}else if(requestCode==2&&resultCode ==getActivity().RESULT_OK){//从相册中返回来的
			mPopupWindow.dismiss();
			ContentResolver resolver = getActivity().getContentResolver();
			Bitmap bitmap = null;
			Uri uri = data.getData();
			try {
				bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
				if(bitmap!=null){
					img_personal.setImageBitmap(bitmap);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String str = (String) msg.obj;
			parseData(str);
		}

	};
	@Override
	public void onDestroy() {
		super.onDestroy();
		CurrencyUtils.recoveryHandler(handler);
	}
	@Override
	public void sendResult(String result, int what) {
		// TODO Auto-generated method stub
		Log.e("dsfsaf_结果", result);
		try {
			JSONObject object = new JSONObject(result);
			int code = object.getInt("resultCode");
			if(code == 1){
				Verson  info = new Verson();
				JSONObject obj = object.getJSONObject("resultInfo");
				String url = obj.getString("version_address");
				String verson = obj.getString("version_number");
				String verson_code = obj.getString("version_code");
				String type = obj.getString("version_type");
				String status = obj.getString("version_status");
				info.setVersion_address(url);
				info.setVersion_code(verson_code);
				info.setVersion_address(url);
				UpdateManager up = new UpdateManager(getActivity(), info);
				up.checkUpdate();
			}else{
				String info = object.getString("resultInfo");
				Toast.makeText(getActivity(), info, 1).show();
			}
			
		
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private HttpUtil httpUtil;
	private void getData() {// 获取更新信息
		// TODO Auto-generated method stub
		httpUtil=new HttpUtil();
		httpUtil.setParserData(this);
//		int token = (Integer) SPUtils.get(SelfCheckActivity.this, SPutilsKey.TOKEN, 0);
		RequestParams params = new RequestParams(RequestUrl.GENGXIN);

		httpUtil.PostHttp(params, 0);
	}
	

}
