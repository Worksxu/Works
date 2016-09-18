package com.ruiqi.xuworks;

import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.Table.OrderTable;
import com.ruiqi.Table.TabAdapter;
import com.ruiqi.Table.TabRow;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.fragment.BackshopNotesFragment;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.R;

public class BackShopNotesActivity extends BaseActivity {
	private ListView lv_table;
	private LinearLayout ll_next,ll_content;
	private Bundle bundle;
	
	public List<TabRow> table;

	public List<TableInfo> mDatas;// 列表list
	public List<TableInfo> mList;// 传递过来的list
	public TabAdapter adapter;
	String type;
//	String json;// 传递过来的json
	String titles[] = { "回库单号", "订单状态", "订单时间" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.back_shop_notes);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		type = getIntent().getBundleExtra("bundle").getString("type");
		if(type.equals("1")){
			setTitle("空瓶回库记录");
		}else if(type.equals("2")){
			setTitle("重瓶回库记录");
		}else if(type.equals("4")){
			setTitle("故障瓶回库记录");
		}
		else if(type.equals("3")){
			setTitle("折旧瓶回库记录");
		}else{
			setTitle("配件回库记录");
		}
		BackshopNotesFragment fragment = new BackshopNotesFragment();
		Bundle bundle = new Bundle();
		bundle.putString("type", type);
		fragment.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().replace(R.id.ll_content, fragment).commitAllowingStateLoss();
		mList = (List<TableInfo>) getIntent().getBundleExtra("bundle").getSerializable("mdatas");
		lv_table = (ListView) findViewById(R.id.listView_back_shop_notes);
		ll_next = (LinearLayout) findViewById(R.id.ll_backShop);
//		ll_next.setVisibility(View.GONE);
		lv_table.setVisibility(View.GONE);
		table = new OrderTable().addData(mList, titles);
		adapter = new TabAdapter(BackShopNotesActivity.this, table);
		lv_table.setAdapter(adapter);
		lv_table.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position > 0 && position < mList.size() + 2) {

					TableInfo tableInfo = mList.get(position-1);
					Log.e("lll", tableInfo.toString());
					String json = tableInfo.getOrderStatus();
//					Toast.makeText(BackShopNotesActivity.this, position+"", 1).show();
					// 跳转到订单详情界面
					Intent intent = new Intent(BackShopNotesActivity.this, BackShopDetialsActivity.class);
//					intent.putExtra("id", ordersn);
					Bundle bundle = new Bundle();
					bundle.putSerializable("json", tableInfo);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public Activity getActivity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Handler[] initHandler() {
		// TODO Auto-generated method stub
		return null;
	}

}
