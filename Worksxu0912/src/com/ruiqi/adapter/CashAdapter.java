package com.ruiqi.adapter;

import java.util.List;

import com.ruiqi.bean.Pensoral;


import com.ruiqi.bean.Type;
import com.ruiqi.works.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CashAdapter extends BaseAdapter{
	LayoutInflater inflater;
	List<Type>list;
	public CashAdapter(List<Type>list ,Context context) {
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
			convertView = inflater.inflate(R.layout.cash_layout_item, null);
			viewHolder.tv_yj = (TextView) convertView.findViewById(R.id.textView_cashitem_price);
			viewHolder.tv_weight = (TextView) convertView.findViewById(R.id.textView_cashitem_weight);
			viewHolder.tv_num = (TextView) convertView.findViewById(R.id.textView_cashitem_num);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		viewHolder.tv_title.setText(list.get(position).getTitle());
//		viewHolder.tv_content.setText(list.get(position));
		viewHolder.tv_weight.setText(list.get(position).getWeight());
		viewHolder.tv_yj.setText("￥"+list.get(position).getPrice());
		viewHolder.tv_num.setText(list.get(position).getNum());
		return convertView;
	}
	class ViewHolder{
		TextView tv_content,tv_yj,tv_weight,tv_num;
		
	}

}
