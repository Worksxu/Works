package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import com.ruiqi.bean.Bottle;
import com.ruiqi.bean.TableInfo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

//退瓶详情的表格类碎片
public class BackBottleDeails extends OrderFragment{
	private List<Bottle> bottle;
	@Override
	public void initData() {
		mDatas = new ArrayList<TableInfo>();
		Bundle bundle = getArguments();
		bottle = (List<Bottle>) bundle.getSerializable("mData");
		if(bottle!=null){
			
			for(int i = 0;i<bottle.size();i++){
				mDatas.add(new TableInfo(bottle.get(i).getXinpian(), bottle.get(i).getWeight(), bottle.get(i).getType()));		
			}
		}
	}

	@Override
	public String[] initTitles() {
		String titles [] ={"芯片号","规格","钢印号"};
		return titles;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}

	@Override
	public int isOrNoSet() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isRefush() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void initData(String type) {
		// TODO Auto-generated method stub
		
	}

}
