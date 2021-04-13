# uniRTC-meeting-android
#### 详细了解：http://meeting.unicloud.com/
#### 如何使用
#### 1、将uniwebrtc.aar、unirtcsdk.aar、unirtcuikit.aar三个包导入开发工程
![avatar](https://github.com/uniRTC/uniRTC-meeting-android/blob/main/img/1618285518910.png)
blob:
#### 2、使用时添加相关的build.gradle 配置
仅保留armeabi-v7a
```
ndk{
 abiFilters "armeabi-v7a"
}
```
重复文件过滤
```
packagingOptions {
        pickFirst 'lib/armeabi-v7a/libssl.1.1.so'
        pickFirst 'lib/armeabi-v7a/libcrypto.1.1.so'
        pickFirst 'lib/armeabi-v7a/libz.so'
        pickFirst 'lib/armeabi-v7a/libsignal-sdk.so'
    }
```
相关依赖的添加
```
dependencies {

    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.google.android.material:material:1.1.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'

    implementation "androidx.recyclerview:recyclerview:1.2.0-beta01"

//RxPermission权限申请
    implementation "com.tbruyelle.rxpermissions2:rxpermissions:0.9.5@aar"
    implementation "io.reactivex.rxjava2:rxandroid:2.0.2"

//加载图片
    implementation "com.github.bumptech.glide:glide:4.11.0"
    implementation "com.google.code.gson:gson:2.8.6"

    implementation project(path: ':uniwebrtc')
    implementation project(path: ':unirtcsdk')
    implementation project(path: ':unirtcuikit')
}
```
#### 3、UIKit使用相关方法
在工程的XXXApplication的onCreate()中初始化sdk、监听网络和监听activity后台状态
```java
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
```
其他相关方法
```java
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
```
在工程中新建一个XXXMeetingActivity继承父类ULinkMeetingActivity 实现 会议详情和网络切换监听
```java
public class UniUIKitActivity extends ULinkMeetingActivity {

    private MeetingInfoDialog mMeetInfoDialog;
    private Configuration mConfiguration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((UniRTCApplication)getApplication()).setNetStateMyListener(this);
        mConfiguration = getResources().getConfiguration();
    }

    @Override
    public void onChange(NetWorkState state) {
        super.onChange(state);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mConfiguration = newConfig;
    }

    @Override
    public void onClickWithMeet(String clickname) {
        super.onClickWithMeet(clickname);
        switch(clickname){
            case "会议信息":
                if(mMeetInfoDialog!=null && mMeetInfoDialog.isShowing()){
                    mMeetInfoDialog.dismiss();
                    mMeetInfoDialog = null;
                }

                MeetingItemInfo info = new MeetingItemInfo();
                info.setRoomId(86960089);
                info.setTelephone("0571-87789889");
                info.setType(1);
                info.setHostId("23455");
                info.setHostname("lee");
                info.setTourist(true);
                info.setStatus(1);
                info.setAddress("s1-meeting.unicloud.com");
                info.setExtensionNumber("7878");
                info.setServerNum(1);
                info.setUrl("s1-meeting.unicloud.com");
                info.setHttpPort(443);
                info.setWsPort(8188);
                info.setWssPort(443);
                info.setSip("122223");
                info.setStartTime("2020-11-18 10:48:26");

                if(mMeetInfoDialog == null)
                    mMeetInfoDialog = new MeetingInfoDialog(this, info, mConfiguration.orientation);

                mMeetInfoDialog.show();
                break;
            case "会议ID":

                break;

            case "邀请":

                break;

            case "会议成员列表邀请":

                break;
        }
    }
}
```
加入会议
```java
UniUserInfo info = new UniUserInfo(12367890,"显示的会议昵称", "");
                Intent intent = new Intent(MainActivity.this, UniUIKitActivity.class);
                intent.putExtra(UniContant.UNIRTCMEETISCREATE, false);
                intent.putExtra(UniContant.UNIRTCMEETUSEINFO, info);
                intent.putExtra(UniContant.UNIRTCMEETROOMID, meetingnum);
                startActivity(intent);
```
创建会议
```java
UniUserInfo info = new UniUserInfo(12367890,"显示的会议昵称", "");
                Intent intent = new Intent(MainActivity.this, UniUIKitActivity.class);
                intent.putExtra(UniContant.UNIRTCMEETISCREATE, true);
                intent.putExtra(UniContant.UNIRTCMEETUSEINFO, info);
                startActivity(intent);
```

#### 4、后台保活相关实现 请查看 工程中的NotificationService类
