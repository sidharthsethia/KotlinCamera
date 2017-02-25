package com.medcords.mhcpanel.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.medcords.mhcpanel.receivers.Alarm;

/**
 * Created by Sidharth on 08/06/16.
 */

public class SetAlarmService extends Service
{
    Alarm alarm;
    public void onCreate()
    {
        super.onCreate();
        alarm = new Alarm();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e("Inside start command", "true");
        alarm.setAlarm(this);
        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        Log.e("Inside onStart", "true");
        alarm.setAlarm(this);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        Log.e("Inside onBind", "true");
        return null;
    }
}
