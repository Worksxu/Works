package com.ruiqi.receiver;

import com.ruiqi.service.TimerService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AutoReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals("VIDEO_TIMER"))   
        {  
            Intent Intentservice = new Intent(context, TimerService.class);  // 要启动的Activity  
            Log.e("guangbo", "fuwu");
            Intentservice.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
            context.startService(Intentservice);  
              
  
        }  
	}

}
