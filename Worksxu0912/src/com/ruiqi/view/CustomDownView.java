package com.ruiqi.view;

import com.ruiqi.works.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CustomDownView extends LinearLayout{
	private TextView tv_title,tv_toast;
	private View blue;// 布局前端蓝色标记
	 @SuppressLint("NewApi")
		public CustomDownView(Context context, AttributeSet attrs, int defStyle) {
		        super(context, attrs, defStyle);
		        init(context);
		    }
		 public CustomDownView(Context context, AttributeSet attrs) {
		        super(context, attrs);
		        init(context);
		    }

		public CustomDownView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			init(context);
		}
		private void init(Context context) {
			// TODO Auto-generated method stub
			View.inflate(context, R.layout.custom_down_layout, this);
			tv_title = (TextView) findViewById(R.id.textView_down_title);
			tv_toast = (TextView) findViewById(R.id.textView_down_show);
			blue = findViewById(R.id.custom_down);
		}
		public void setString(String str){
			tv_title.setText(str);
		}
		public void settvString(String str){
			tv_toast.setText(str);
		}
		public void setView(int i){
			blue.setVisibility(i);
		}
		public void settvView(int i){
			tv_toast.setVisibility(i);
		}
}
