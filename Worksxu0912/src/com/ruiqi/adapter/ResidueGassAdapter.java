package com.ruiqi.adapter;

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
import android.widget.TextView;

public class ResidueGassAdapter extends BaseAdapter{
	/**
	 * 芯片号选择(余气折现)
	 */
	LayoutInflater inflater;
	List<Weight>list;
	public ResidueGassAdapter(Context context,List<Weight>list) {
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
			convertView = inflater.inflate(R.layout.user_type_layout, null);
			viewHolder.tv_content = (TextView) convertView.findViewById(R.id.textView_user_type);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
//		viewHolder.tv_title.setText(list.get(position).getTitle());
		viewHolder.tv_content.setText(list.get(position).getXinpian());
		return convertView;
	}
	class ViewHolder{
		TextView tv_content;
		
	}

}
