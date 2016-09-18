package com.ruiqi.adapter;

import java.util.List;

import com.ruiqi.bean.Pensoral;


import com.ruiqi.bean.TableInfo;
import com.ruiqi.bean.Type;
import com.ruiqi.works.R;
import com.ruiqi.xuworks.PeijianBackShopActvity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BottleToShopAdapter extends BaseAdapter{
	LayoutInflater inflater;
	List<TableInfo>list;
	
	Context context;
	
	public BottleToShopAdapter(Context context,List<TableInfo>list) {
		// TODO Auto-generated constructor stub
		this.list  = list;
		this.context = context;
		this.inflater = inflater.from(context);
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	

	@SuppressLint("InflateParams")
	@Override
	public View getView( final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.bottletoshop_item, null);
			viewHolder.tv_content = (TextView) convertView.findViewById(R.id.textView_bottleitem_title);
			viewHolder.et_num = (EditText) convertView.findViewById(R.id.editText_bottleitem_num);
			
//			convertView.setTag(viewHolder);
		
//			else
//			{
//			viewHolder = (ViewHolder) convertView.getTag();
//		}

		viewHolder.tv_content.setText( list.get(position).getOrderNum()+" "+"("+list.get(position).getOrderMoney()+")"+" "+":");
		
//		viewHolder.et_num.setText(list.get(position).getOrderStatus());
		
//		Log.e("lllll", position+"");
		
		viewHolder.et_num.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				TableInfo p = list.get(position);
				
					
				if(!TextUtils.isEmpty(s.toString())){
					if(Integer.parseInt(s.toString())>Integer.parseInt(list.get(position).getOrderMoney())){
						Toast.makeText(context, "提交数量大于实际数量", 1).show();
					}else{
					p.setKid(s.toString());
					
				}
				
			}else{
				p.setKid("0");
			}
				}
		});
		
		}
		return convertView;
	}
	public  class ViewHolder{
		TextView tv_content;
		 EditText et_num;
	}
//	private TextWatcher watcher = new TextWatcher() {
//			
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				// TODO Auto-generated method stub
//				Log.e("lll","on");
//			}
//			
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				// TODO Auto-generated method stub
//				Log.e("lll","before");
//			}
//			
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//			Log.e("lll", s.toString());
////			Log.e("lll", click+"");
////				int pos = (Integer) ViewHolder.et_num.getTag();
//				
//				p.setKid(s.toString());
////				
//			}
//			
//	};

}
