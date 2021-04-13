package com.unicloud.unirtc;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.unicloud.unirtcuikitulink.wapper.ULinkMeetingActivity;


public class NotificationService extends Service {

    private NotificationManager notificationManager;
    private String notificationId = "channelId";
    private String notificationName = "channelName";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("uLink")
                .setContentText("音视频会议，正在运行...");
        //设置Notification的ChannelID,否则不能正常显示
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationId);
        }
        Intent intent = new Intent(getApplicationContext(), ULinkMeetingActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);

        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();
        return notification;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //创建NotificationChannel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(notificationId, notificationName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }


        startForeground(1,getNotification());
    }

}
