package com.example.notificationapplication;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HelloReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        Log.d("log", "Hello!!");
    }

}
