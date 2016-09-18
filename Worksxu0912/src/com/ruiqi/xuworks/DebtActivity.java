package com.ruiqi.xuworks;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.utils.SPUtils;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.R;
/**
 * 欠款页面
 * 
 * @author Administrator
 *
 */
public class DebtActivity extends BaseActivity{
	@Override
	public void jumpPage() {
		// TODO Auto-generated method stub
		super.jumpPage();
		if(!TextUtils.isEmpty(et_goodsNumber.getText().toString())){
			SPUtils.put(DebtActivity.this, "debtgoods", et_goodsNumber.getText().toString());// 欠款合同
		}
		if(!TextUtils.isEmpty(et_goodsNumber.getText().toString())){
			SPUtils.put(DebtActivity.this, "debtyj", et_cashNumber.getText().toString());//yajin合同	
		}
		
	}

	private EditText et_goodsNumber,et_goodsMoney,et_cashNumber,et_cash_Money;
	double goods, cash,debt_goods,debt_cash;
	private TextView tv_next;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debt_layout);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setTitle("欠款");
		et_cash_Money = (EditText) findViewById(R.id.editText_debt_cashMoney);
		et_goodsNumber = (EditText) findViewById(R.id.editText_debt_goodsNumber);
		et_goodsMoney = (EditText) findViewById(R.id.editText_debt_goodsMoney);
		et_cashNumber = (EditText) findViewById(R.id.editText_debt_cashNumber);
		if(!TextUtils.isEmpty((CharSequence) SPUtils.get(DebtActivity.this, "debtyj", ""))){
			et_cashNumber.setText((CharSequence) SPUtils.get(DebtActivity.this, "debtyj", ""));
		}
		if(!TextUtils.isEmpty((CharSequence) SPUtils.get(DebtActivity.this, "debtgoods", ""))){
			et_goodsNumber.setText((CharSequence) SPUtils.get(DebtActivity.this, "debtgoods", ""));
		}
		tv_next = (TextView) findViewById(R.id.tv_debt_sure);
		tv_next.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty((String) SPUtils.get(DebtActivity.this, "yajin", ""))){
					cash = Double.parseDouble((String) SPUtils.get(DebtActivity.this, "yajin", ""));
				}else{
					cash = 0;
				}
				
				goods = Double.parseDouble((String) SPUtils.get(DebtActivity.this, "payTotal", ""));// 应支付金额
				if(!TextUtils.isEmpty(et_cash_Money.getText().toString())){
					debt_cash = Double.parseDouble(et_cash_Money.getText().toString());
				}
				if(!TextUtils.isEmpty(et_goodsMoney.getText().toString())){
					debt_goods = Double.parseDouble(et_goodsMoney.getText().toString());
				}
				
			
				Log.e("lll",et_cashNumber.getText().toString());
				if(TextUtils.isEmpty(et_goodsMoney.getText().toString().trim())&&
						TextUtils.isEmpty(et_goodsNumber.getText().toString())
						&&TextUtils.isEmpty(et_cash_Money.getText().toString())
						&&TextUtils.isEmpty(et_cashNumber.getText().toString())){
					Toast.makeText(DebtActivity.this, "请完善信息", 1).show();;
				}
				else if(!TextUtils.isEmpty(et_goodsMoney.getText().toString())
							&&TextUtils.isEmpty(et_goodsNumber.getText().toString())){
						Toast.makeText(DebtActivity.this, "请输入欠款合同", 1).show();;
					}
				else if(!TextUtils.isEmpty(et_goodsNumber.getText().toString())
							&&TextUtils.isEmpty(et_goodsMoney.getText().toString())){
						Toast.makeText(DebtActivity.this, "请输入欠款金额", 1).show();
					}
				else if(!TextUtils.isEmpty(et_cashNumber.getText().toString())
							&&TextUtils.isEmpty(et_cash_Money.getText().toString())){
						Toast.makeText(DebtActivity.this, "请输入欠款金额", 1).show();
					}
				else if(!TextUtils.isEmpty(et_cash_Money.getText().toString())
							&&TextUtils.isEmpty(et_cashNumber.getText().toString())){
						Toast.makeText(DebtActivity.this, "请输入欠款合同", 1).show();
					}
				else if(!TextUtils.isEmpty(et_cash_Money.getText().toString())&& debt_cash >cash){
						Toast.makeText(DebtActivity.this, "欠款押金不能大于"+cash+"", 1).show();
					}
				else if(!TextUtils.isEmpty(et_goodsMoney.getText().toString())&& debt_goods > goods){
						Toast.makeText(DebtActivity.this, "商品欠款不能大于"+(goods-cash)+"", 1).show();
					}else{
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("goodsMoney", et_goodsMoney.getText().toString());
					bundle.putString("goodsNumber", et_goodsNumber.getText().toString());
					bundle.putString("cashMoney", et_cash_Money.getText().toString());
					bundle.putString("cashNumber", et_cashNumber.getText().toString());
					intent.putExtra("debt", bundle);
					setResult(2, intent);
					if(!TextUtils.isEmpty(et_goodsNumber.getText().toString())){
						SPUtils.put(DebtActivity.this, "debtgoods", et_goodsNumber.getText().toString());// 欠款合同
					}
					if(!TextUtils.isEmpty(et_goodsNumber.getText().toString())){
						SPUtils.put(DebtActivity.this, "debtyj", et_cashNumber.getText().toString());//yajin合同	
					}
					finish();}
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
