package com.unicloud.unirtc;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.unicloud.unirtcuikitulink.util.NetWorkStateReceiver;
import com.unicloud.unirtcuikitulink.wapper.ULinkMeetingActivity;
import com.unicloud.wapper.UniCloudMeetingManager;

public class UniRTCApplication extends android.app.Application {


    @Override
    public void onCreate() {
        super.onCreate();

        UniCloudMeetingManager.getInstance().initialize(this,
                (code, msg) -> {

                });

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                if(null != activity && activity.getClass().equals(ULinkMeetingActivity.class)){
                    stopNotificationService(activity);
                }
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
                if(null != activity && activity.getClass().equals(ULinkMeetingActivity.class)){
                    startNotificationService(activity);
                }
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });


        registerReceiver();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver();
    }

    private void startNotificationService(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android8.0以上通过startForegroundService启动service
            startForegroundService(new Intent(activity, NotificationService.class));
        }else{
            startService(new Intent(activity, NotificationService.class));
        }
    }

    private void stopNotificationService(Activity activity){
        stopService(new Intent(activity, NotificationService.class));
    }

    private NetWorkStateReceiver netStateReceiver;
    /**
     * 注册网络状态监听器（广播接收者）
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        netStateReceiver = new NetWorkStateReceiver();
        this.registerReceiver(netStateReceiver, filter);
    }

    /**
     * 注销网络状态监听器
     */
    private void unregisterReceiver() {
        if (netStateReceiver != null) {
            this.unregisterReceiver(netStateReceiver);
        }
    }

    public void setNetStateMyListener(NetWorkStateReceiver.NetStateListener myListener) {
        if(netStateReceiver!=null){
            netStateReceiver.setNetChangeListener(myListener);
        }
    }

}
