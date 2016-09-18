package com.ruiqi.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Node;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruiqi.bean.YouHuiZheKouInfo;
import com.ruiqi.bean.ZheKouInfo;
import com.ruiqi.utils.DateUtils;
import com.ruiqi.works.R;

public class YouHuiAdapter extends BaseAdapter{
	 ArrayList<YouHuiZheKouInfo> list;
	 LayoutInflater inflater;
	// 用来控制CheckBox的选中状况
	 private int a = 0;// 用来判定是否是第一次点击，0代表是，1代表不是
		private int cliPos;// 记录第一次点击时的位置
		private Boolean flag = true;
		private String title, money_youhui,num,id;
	 Send send;
//	private static HashMap<Integer, Boolean> isSelected;
	 @SuppressLint("UseSparseArrays")
	public YouHuiAdapter(Context context,ArrayList<YouHuiZheKouInfo> list) {
		this.list = list;
		this.inflater = LayoutInflater.from(context);
//		isSelected = new HashMap<Integer, Boolean>();
		// 初始化数据
//		initDate();
	}
//	 public void noti(){
//		 notifyDataSetChanged();
//	 }
	// 初始化isSelected的数据
//		private void initDate() {
//			for (int i = 0; i < list.size(); i++) {
//				getIsSelected().put(i, false);
//			}
//		}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	public static class ViewHolder{
		TextView tv_title,tv_price,tv_time;
		LinearLayout  ll_click;
		ImageView img_show;
//		 public CheckBox cb_select;
//		public ImageView img_show;
	}
	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.youhui_adapter, null);
			holder.tv_price = (TextView) convertView.findViewById(R.id.textView_youhui_adapter_money);
			holder.tv_title = (TextView) convertView.findViewById(R.id.textView_youhui_adapter_title);
			holder.tv_time = (TextView) convertView.findViewById(R.id.textView_youhui_adapter_date);
			holder.ll_click = (LinearLayout) convertView.findViewById(R.id.ll_youhui_click);
			holder.img_show = (ImageView) convertView.findViewById(R.id.imageView_youhuishow);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_price.setText(list.get(arg0).getMoney()+"");
		holder.tv_title.setText(list.get(arg0).getTitle());
		if(TextUtils.isEmpty(list.get(arg0).getTime_end())){
			holder.tv_time.setText(list.get(arg0).getComment());
		}else{
		String date = DateUtils.TimeStampToDate(list.get(arg0).getTime_end(), "yyyy-MM-dd HH:mm");
		
		holder.tv_time.setText(date);
		}
		holder.img_show.setImageResource(list.get(arg0).getImage());
		holder.ll_click.setTag(arg0);
		holder.ll_click.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean bl = flag;
				int pos = (Integer) v.getTag();
				YouHuiZheKouInfo info = list.get(pos);
				title = info.getTitle();
				money_youhui = info.getMoney()+"";
				Bundle bundle = new Bundle();
				
				
				if (a == 0) {
					// 代表是第一次点击
					cliPos = pos;
					a = 1;
					info.setImage(R.drawable.checked);
					flag = false;
				} else if (a == 1) {
					// 不是第一次点击
					if (cliPos == pos) {// 点击相同的位置
						if (info.getImage() == R.drawable.checked) {
							info.setImage(R.drawable.unchecked);
							flag = true;
						} else {
							info.setImage(R.drawable.checked);
							flag = false;
						}
					} else {// 不是相同的位置
							// 将前面已经点击了的设置成未被点击
						YouHuiZheKouInfo info1 = list.get(cliPos);
						info1.setImage(R.drawable.unchecked);
						// 将自己设置成点击
						info.setImage(R.drawable.checked);
						flag = false;
						cliPos = pos;
					}

				}
				if(flag ){
					bundle.putString("youhui_name", "优惠");
					
					bundle.putString("youhui_money", "0");
				}else{
					
					bundle.putString("youhui_name", title);
					
					bundle.putString("youhui_money", money_youhui);
				}
				
//				bundle.putSerializable("zhekou", (Serializable) ((ZheKouInfo) info).getData());
				send.bunndle(bundle);
				notifyDataSetChanged();
				
			}
		});
		return convertView;
	}
	
//	public static HashMap<Integer, Boolean> getIsSelected() {
//		return isSelected;
//	}

//	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
//		ZheKouAdapter.isSelected = isSelected;
//	}

	public interface Send{
		Bundle bunndle(Bundle bunndle);
	}
	public void Setsend(Send send){
		this.send = send;
		
	}

}
