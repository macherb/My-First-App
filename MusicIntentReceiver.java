package com.example.myfirstapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MusicIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    	if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
        	Toast.makeText(context, "Power connected!", Toast.LENGTH_SHORT).show();
        }
    }
}
