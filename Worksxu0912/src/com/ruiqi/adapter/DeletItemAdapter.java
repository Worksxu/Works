package com.ruiqi.adapter;

import java.util.ArrayList;

import com.ruiqi.bean.Type;
import com.ruiqi.bean.YouHuiZheKouInfo;
import com.ruiqi.works.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeletItemAdapter extends BaseAdapter{
	 ArrayList<Type> list;
	 LayoutInflater inflater;
	public DeletItemAdapter(Context context,ArrayList<Type> list) {
		// TODO Auto-generated constructor stub
		this.list = list;
		this.inflater = LayoutInflater.from(context);
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
	public class ViewHolder{
		TextView tv_toast;
		ImageView img_delete;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.delete_item_layout, null);
			holder.img_delete = (ImageView) convertView.findViewById(R.id.imageView_delete);
			holder.tv_toast = (TextView) convertView.findViewById(R.id.textView_delete);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_toast.setText(list.get(position).getName()+"("+list.get(position).getNum()+")");
		return convertView;
	}

}
