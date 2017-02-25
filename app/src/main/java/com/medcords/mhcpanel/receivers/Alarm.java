package com.medcords.mhcpanel.receivers;

/**
 * Created by Sidharth on 08/06/16.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import com.medcords.mhcpanel.database.DatabaseHandler;
import com.medcords.mhcpanel.database.ImageRecord;
import com.medcords.mhcpanel.services.ImageUploadService;
import com.medcords.mhcpanel.utilities.Constants;

import java.util.List;

public class Alarm extends BroadcastReceiver
{
    public static final int REQUEST_CODE = 12345;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        Log.e("Inside alarm", "true");
        wl.acquire();
        DatabaseHandler db = new DatabaseHandler(context);
        List<ImageRecord> imageRecords = db.getAllImages();
        for (ImageRecord imageRecord : imageRecords){
            //logs dont run inside wake log
            // Log.e("Id", "" + imageRecord.getId());

            if (imageRecord.getHasBeenUploaded() == Constants.FLAG_UPLOAD_FALSE){
                Intent uploadImageIntent = new Intent(context, ImageUploadService.class);
                uploadImageIntent.putExtra("imageId", imageRecord.getId());
                context.startService(uploadImageIntent);
            } else {
                db.deleteImage(imageRecord);
            }

        }
        wl.release();
        Log.e("Inside alarm", "false");
    }

    public void setAlarm(Context context)
    {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(context, Alarm.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, Alarm.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                AlarmManager.INTERVAL_HOUR, pIntent);


        Log.e("Alarm set","true");
    }

    public void cancelAlarm(Context context)
    {
        Intent intent = new Intent(context, Alarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
}
