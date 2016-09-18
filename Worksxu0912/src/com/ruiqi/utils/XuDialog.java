package com.ruiqi.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

import com.ruiqi.adapter.ContentAdapter;

public class XuDialog {
private  Yes yes;
private  No no;
private YesIntwnt yestent;
private NoIntwnt notent;
	private static XuDialog xuDialog;
	private XuDialog(){}
	
	public static XuDialog getInstance(){
		
		
		if(xuDialog==null){
			xuDialog=new XuDialog();
		}
		return xuDialog;
	}
	public  void show(Context context,String str,final int i){
		new AlertDialog.Builder(context)
		.setTitle(str)
		.setNegativeButton("是", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				yes.XuCallback(i);
			}
		})
		.setPositiveButton("否",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				no.XuNo(i);
			}
		}).show();
	}
	public  void show(Context context,String str,final int i,final Intent intent){
		new AlertDialog.Builder(context)
		.setTitle(str)
		.setNegativeButton("是", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				yes.XuCallback(i);
				yestent.XuCallbackIntent(i, intent);
			}
		})
		.setPositiveButton("否",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				no.XuNo(i);
				notent.XuNoIntent(i, intent);
			}
		}).show();
	}
	public  void show(Context context,String str,String stryes,String strno,final int i){
		new AlertDialog.Builder(context)
		.setTitle(str)
		.setNegativeButton(stryes, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				yes.XuCallback(i);
			}
		})
		.setPositiveButton(strno,new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				no.XuNo(i);
			}
		}).show();
	}
	
	public interface Yes{
		void XuCallback(int i);
	}
	public  void setyes(Yes yes){
	this.yes  = yes;
	}
	public interface No{
		void XuNo(int i);
	}
	public  void setno(No no){
	this.no = no;
	}
	public interface YesIntwnt{
		void XuCallbackIntent(int i,Intent intent);
	}
	public  void setyesintent(YesIntwnt yestent){
		this.yestent  = yestent;
		}
	public interface NoIntwnt{
		void XuNoIntent(int i,Intent intent);
	}
	public  void setnointent(NoIntwnt notent){
		this.notent  = notent;
	}
	
}
