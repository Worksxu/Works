package com.ruiqi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class CustomListView extends ListView{
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {// 解决listview 与ScrollView 滑动冲突展示
		
		// TODO Auto-generated method stub
		int expandSpace = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpace);
	}

	public CustomListView(Context context) {
		super(context);
		
	}

	public CustomListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
	}

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
	}


}
