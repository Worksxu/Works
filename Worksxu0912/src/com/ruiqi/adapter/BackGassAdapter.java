package com.ruiqi.adapter;

import java.util.List;

import com.ruiqi.bean.Pensoral;


import com.ruiqi.bean.TableInfo;
import com.ruiqi.bean.Weight;
import com.ruiqi.works.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class BackGassAdapter extends BaseAdapter{
	LayoutInflater inflater;
	List<Weight>list;
	int click;
	public BackGassAdapter(Context context,List<Weight>list) {
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
	public View getView( final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.backbottlegass_layout, null);
			viewHolder.tv_number = (TextView) convertView.findViewById(R.id.textView_bottlegass_number);
			viewHolder.tv_weight = (TextView) convertView.findViewById(R.id.textView_bottlegass_weight);
			viewHolder.edit_in = (TextView) convertView.findViewById(R.id.editText_bottlegass_yj);
			convertView.setTag(viewHolder);
//		}else{
//			viewHolder = (ViewHolder) convertView.getTag();
		
//		viewHolder.tv_title.setText(list.get(position).getTitle());
//		viewHolder.tv_content.setText(list.get(position));
		viewHolder.tv_number.setText(list.get(position).getStatus());
		viewHolder.tv_weight.setText(list.get(position).getType());
		viewHolder.edit_in.addTextChangedListener(new TextWatcher() {
			
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
				
				if(!TextUtils.isEmpty(s.toString().trim())){
					list.get(position).setYj(s.toString());
				}
					
				
				}
		});
		}
		return convertView;
	}
	class ViewHolder{
		TextView tv_number,tv_weight,edit_in;
		
	}

}
