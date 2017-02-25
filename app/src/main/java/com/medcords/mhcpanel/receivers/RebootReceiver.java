package com.medcords.mhcpanel.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RebootReceiver extends BroadcastReceiver {

    public RebootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        try {
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                Log.i("got into receiver", "boot true");

                Alarm alarm = new Alarm();
                alarm.setAlarm(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
