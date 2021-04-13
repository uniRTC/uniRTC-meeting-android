package com.unicloud.unirtc;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.unicloud.tools.entity.MeetingItemInfo;
import com.unicloud.unirtcuikitulink.util.NetWorkState;
import com.unicloud.unirtcuikitulink.wapper.ULinkMeetingActivity;

public class UniUIKitActivity extends ULinkMeetingActivity {

    private MeetingInfoDialog mMeetInfoDialog;
    //
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }




}
