package com.ruiqi.adapter;

import java.util.List;

import com.ruiqi.bean.Pensoral;


import com.ruiqi.bean.Type;
import com.ruiqi.bean.Weight;
import com.ruiqi.works.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ZheJiuLvAdapter extends BaseAdapter{
	/**
	 * 空瓶余气展示
	 */
	LayoutInflater inflater;
	List<Type>list;
	public ZheJiuLvAdapter(Context context,List<Type>list) {
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
			convertView = inflater.inflate(R.layout.zhejiu_lv_item_layout, null);
			viewHolder.tv_num = (TextView) convertView.findViewById(R.id.tv_zhejiu_item_num);
			viewHolder.tv_weight = (TextView) convertView.findViewById(R.id.tv_zhejiu_item_weight);
			viewHolder.tv_price = (TextView) convertView.findViewById(R.id.tv_zhejiu_item_price);
			viewHolder.tv_year = (TextView) convertView.findViewById(R.id.tv_zhejiu_item_year);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
////		viewHolder.tv_title.setText(list.get(position).getTitle());
//		viewHolder.tv_content.setText(list.get(position).getXinpian());
		viewHolder.tv_weight.setText(list.get(position).getBottle_name());
		viewHolder.tv_price.setText(list.get(position).getPrice());
		viewHolder.tv_num.setText(list.get(position).getNum());
		viewHolder.tv_year.setText(list.get(position).getName());

		return convertView;
	}
	class ViewHolder{
		TextView tv_num,tv_weight,tv_price,tv_year;
		
	}

}
