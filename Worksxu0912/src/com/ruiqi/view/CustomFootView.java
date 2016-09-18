package com.ruiqi.view;


import com.ruiqi.works.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomFootView extends LinearLayout{
	private TextView tv_title,tv_num;
	private ImageView img_foot;
	private LinearLayout ll_tost;
	 @SuppressLint("NewApi")
	public CustomFootView(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        init(context);
	    }
	 public CustomFootView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        init(context);
	    }

	public CustomFootView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	private void init(Context context) {
		// TODO Auto-generated method stub
		View.inflate(context, R.layout.customfootview_layout, this);
		tv_title = (TextView) findViewById(R.id.textView_customfoot);
		tv_num = (TextView) findViewById(R.id.textView_toastOrder);
		img_foot = (ImageView) findViewById(R.id.imageView_customfoot);
		ll_tost = (LinearLayout) findViewById(R.id.ll_custom);
		
	}
	public void setString(String str){
		tv_title.setText(str);
	}
	public void setNum(String str){
		tv_num.setText(str);
	}
	public void setImage(int i){
		img_foot.setImageResource(i);
	}
	public void setColors(int c){
		tv_title.setTextColor(c);
	}
	public void setvisibility(){
		ll_tost.setVisibility(View.VISIBLE);
	}
}
