package com.ruiqi.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

public class ChaiMyDialog {
	
	
	private static ChaiMyDialog chaiMyDialog;
	
	private ChaiMyDialog(){}
	
	public static ChaiMyDialog getInstance(){
		
		if(chaiMyDialog==null){
			chaiMyDialog=new ChaiMyDialog();
		}
		return chaiMyDialog;
	}
	/**
	 * 
	 * @param ctx
	 * @param str提示信息
	 */
	public void show(final Context ctx,String str){
			
		final EditText et = new EditText(ctx);
		et.setHint(str);
		et.setBackgroundColor(ctx.getResources().getColor(android.R.color.transparent));
		new AlertDialog.Builder(ctx)
		
		.setView(et)
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String str = et.getText().toString().trim();
						callBack.chaiStringCallBack(str);
			}
		})
		.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		})
		.create().show();
	}
	/**
	 * @param ctx
	 * @param str提示信息
	 * @param length输入长度
	 */
	public  void show(final Context ctx,String str,int length){
		
		final EditText et = new EditText(ctx);
		et.setHint(str);
		et.setInputType(InputType.TYPE_CLASS_NUMBER);
		et.setBackgroundColor(ctx.getResources().getColor(android.R.color.transparent));
		new AlertDialog.Builder(ctx)
		.setView(et)
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String str = et.getText().toString().trim();
				if(str.length()==6){
					callBack.chaiStringCallBack(str);
				}else{
					Toast.makeText(ctx, "请输入正确的内容", Toast.LENGTH_SHORT).show();
				}
			}
		})
		.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		})
		.create().show();
	}
	/**
	 * 修改押金
	 * @param ctx
	 * @param str提示内容
	 * @param money押金
	 */
	public  void showYaJin(final Context ctx,String str,final int where){
		
		final EditText et = new EditText(ctx);
		et.setTextSize(20);
		et.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		et.setGravity(Gravity.CENTER);
		et.setPadding(0, 20, 0, 0);
		et.setInputType(InputType.TYPE_CLASS_NUMBER);
		et.setBackgroundColor(ctx.getResources().getColor(android.R.color.transparent));
		new AlertDialog.Builder(ctx)
		.setTitle(str)
		.setView(et)
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String str = et.getText().toString().trim();
				if(!TextUtils.isEmpty(str)&&Integer.parseInt(str)>=0){
					callBack.chaiIntCallBack(Integer.parseInt(str),where);
				}else{
					callBack.chaiIntCallBack(0,where);
				}
			}
		})
		.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		})
		.create().show();
	}
	/**
	 * 
	 * @param ctx
	 * @param str 提示语
	 * @param view 显示布局
	 */
	
	public  void showListViewSelect(final Context ctx,String str,View view){
		
		new AlertDialog.Builder(ctx)
		.setTitle(str)
		.setView(view)
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cs.chaiSelectCallBack(true);
			}
		}).create().show();
	}
	
	
	private  ChaiCallBack callBack;
	public interface ChaiCallBack{
		/**
		 * 回调字符串
		 * @param str
		 */
		public void chaiStringCallBack(String str);
		/**
		 * 回调int
		 * @param in
		 */
		public void chaiIntCallBack(int in,int where);
	}
	public void setCallBack(ChaiCallBack c){
		this.callBack = c;
	}
	
	/**
	 * View确认回调
	 * @author Administrator
	 *
	 */
	public interface ChaiSelectCallBack{
		public void chaiSelectCallBack(boolean isSelect);
	}
	private ChaiSelectCallBack cs;
	public void setSelectCallBack(ChaiSelectCallBack cs){
		this.cs = cs;
	}
	
	
}
