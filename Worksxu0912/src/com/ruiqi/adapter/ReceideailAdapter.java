package com.ruiqi.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;





import com.ruiqi.bean.NopayDetail;
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
/**
 * 欠款记录适配器
 * @author Administrator
 *
 */
public class ReceideailAdapter extends BaseAdapter{
	ArrayList<NopayDetail > list;
	LayoutInflater inflater;

	private static HashMap<Integer, Boolean> isSelected;
	@SuppressLint("UseSparseArrays")
	public ReceideailAdapter(Context context,ArrayList<NopayDetail > list) {
		
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
		TextView tv_monery,tv_type,tv_time,tv_name,tv_numeber;
		public CheckBox cb_recei;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.receideail_item_layout, null);
			holder.cb_recei = (CheckBox) convertView.findViewById(R.id.checkBox_receideail);
			holder.tv_type = (TextView) convertView.findViewById(R.id.tv_receideail_type);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_receideail_time);
			holder.tv_monery = (TextView) convertView.findViewById(R.id.tv_receideail_money);
			holder.tv_numeber = (TextView) convertView.findViewById(R.id.tv_receideail_number);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_monery.setText(list.get(position).getMoney());
		holder.tv_time.setText(list.get(position).getTime_list());
		holder.tv_numeber.setText(list.get(position).getOrder_sn());
		holder.tv_type.setText(list.get(position).getUsername());
		holder.cb_recei.setChecked(getIsSelected().get(position));
		return convertView;
	}
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

//	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
//		DeductionAdapter.isSelected = isSelected;
//	}

}
