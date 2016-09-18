package com.ruiqi.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




import com.ruiqi.bean.Type;
import com.ruiqi.works.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class DeductionAdapter extends BaseAdapter{
	ArrayList<Type > list;
	LayoutInflater inflater;

	private static HashMap<Integer, Boolean> isSelected;
	@SuppressLint("UseSparseArrays")
	public DeductionAdapter(Context context,ArrayList<Type > list) {
		
		this.inflater  = inflater.from(context);
		this.list = list;
		isSelected = new HashMap<Integer, Boolean>();
		
		// 初始化数据
		initDate();
	}
	// 初始化isSelected的数据
		private void initDate() {
			for (int i = 0; i < list.size(); i++) {
					getIsSelected().put(i, false);
				}
			}
		

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	public static class ViewHolder{
		TextView tv_monery,tv_weight;
		public CheckBox cb_select;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.deduction_adapter, null);
			holder.cb_select = (CheckBox) convertView.findViewById(R.id.checkBox_deduction);
			holder.tv_monery = (TextView) convertView.findViewById(R.id.textView_deduction_adapter_money);
			holder.tv_weight = (TextView) convertView.findViewById(R.id.textView_deduction_adapter_weight);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_monery.setText(list.get(position).getPrice());
		holder.tv_weight.setText(list.get(position).getWeight());
		holder.cb_select.setChecked(getIsSelected().get(position));
		return convertView;
	}
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

//	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
//		DeductionAdapter.isSelected = isSelected;
//	}

}
