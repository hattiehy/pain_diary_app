package com.example.ass3.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"It's time to enter pain diary!",Toast.LENGTH_LONG).show();
//        Log.e("AlertReceiver", "RECEIVED");
    }
}