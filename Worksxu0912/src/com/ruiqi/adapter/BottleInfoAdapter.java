package com.ruiqi.adapter;

import java.util.HashMap;
import java.util.List;

import com.ruiqi.bean.Pensoral;


import com.ruiqi.bean.Weight;
import com.ruiqi.works.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class BottleInfoAdapter extends BaseAdapter{
	LayoutInflater inflater;
	List<Weight>list;
	
	public BottleInfoAdapter(Context context,List<Weight>list) {
		// TODO Auto-generated constructor stub
		this.list  = list;
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
		return 0;
	}
	

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.bottle_info_item, null);
			viewHolder.tv_xinpian = (TextView) convertView.findViewById(R.id.tv_checkbottle_xinpian);
			viewHolder.tv_number = (TextView) convertView.findViewById(R.id.tv_checkbottle_number);
			viewHolder.tv_weight = (TextView) convertView.findViewById(R.id.tv_checkbottle_weight);
			
			viewHolder.tv_yuqi = (TextView) convertView.findViewById(R.id.tv_checkbottle_yuqi);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		if(list.get(position).getStatus().equals("0")){
//			viewHolder.tv_yuqi.setVisibility(View.VISIBLE);
//		}
		viewHolder.tv_xinpian.setText(list.get(position).getXinpian());
		viewHolder.tv_number.setText(list.get(position).getXuliehao());
		viewHolder.tv_weight.setText(list.get(position).getType_name());
		
		return convertView;
	}
	
	public  class ViewHolder{
		TextView tv_xinpian,tv_number,tv_weight,tv_yuqi;
		 
	}

}
