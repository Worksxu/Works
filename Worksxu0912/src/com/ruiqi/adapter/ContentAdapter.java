package com.ruiqi.adapter;

import java.util.List;
import com.ruiqi.bean.Pensoral;


import com.ruiqi.works.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContentAdapter extends BaseAdapter{
	LayoutInflater inflater;
	List<Pensoral>list;
	public ContentAdapter(Context context,List<Pensoral>list) {
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
			convertView = inflater.inflate(R.layout.personal_content, null);
			viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_personal_right);
			viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_personal_left);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.tv_title.setText(list.get(position).getTitle());
		viewHolder.tv_content.setText(list.get(position).getContent());
		return convertView;
	}
	class ViewHolder{
		TextView tv_title,tv_content;
		
	}

}
