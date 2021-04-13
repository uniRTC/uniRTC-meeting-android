package com.unicloud.unirtc;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.unicloud.tools.entity.MeetingItemInfo;
import com.unicloud.unirtcuikitulink.util.DateUtils;

public class MeetingInfoDialog extends Dialog {

    private Context mContext;
    private RelativeLayout mRlMeetingInfoViews;
    private TextView mMeetingTitle;
    private ImageView mMeetingClose;
    private TextView mMeetingTheme;
    private TextView mMeetingTime;
    private TextView mMeetingRoomId;
    private TextView mMeetingName;
    private TextView mMeetingInvite;
    TextView mTxtSipNum;



    int mOri;
    MeetingItemInfo mMeetInfo;

    public MeetingInfoDialog(@NonNull Context context) {
        super(context);
        init();
    }

    public MeetingInfoDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init();
    }

    protected MeetingInfoDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    public MeetingInfoDialog(@NonNull Context context, MeetingItemInfo meetingItemInfo, int ori){
        super(context);
        mContext = context;
        this.mOri = ori;
        mMeetInfo = meetingItemInfo;
        init();
    }


    protected void init(){
        Window win = this.getWindow();
        Display display = win.getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        win.requestFeature(Window.FEATURE_NO_TITLE);
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        if (mOri == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏
            lp.width = (int) (0.40*width);
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.RIGHT;

            if(mRlMeetingInfoViews != null)
                mRlMeetingInfoViews.setBackgroundResource(R.drawable.shape_width_left_raduis_15_bg);

        } else if (mOri == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = (int) (0.50*height);
            lp.gravity = Gravity.BOTTOM;

            if(mRlMeetingInfoViews != null)
                mRlMeetingInfoViews.setBackgroundResource(R.drawable.shape_width_top_raduis_15_bg);

        }

        win.setAttributes(lp);
        win.setBackgroundDrawableResource(android.R.color.transparent);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meetinginfos_dialog);
        initView();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initView(){
        mRlMeetingInfoViews = findViewById(R.id.rl_meeting_info_dialog);
        mMeetingTitle = findViewById(R.id.txt_meetinginfo_title);
        mMeetingClose = findViewById(R.id.img_meetinginfo_close);
        mMeetingTheme = findViewById(R.id.txt_meeting_theme);
        mMeetingTime  = findViewById(R.id.txt_meeting_time);
        mMeetingRoomId = findViewById(R.id.txt_meeting_meetroomid);
        mMeetingName = findViewById(R.id.txt_meeting_username);
        mMeetingInvite = findViewById(R.id.txt_meeting_invite);
        mTxtSipNum = findViewById(R.id.txt_meeting_sipnum);

        //横屏
        if (mOri == Configuration.ORIENTATION_LANDSCAPE) {
            mRlMeetingInfoViews.setBackgroundResource(R.drawable.shape_width_left_raduis_15_bg);
        } else if (mOri == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏
            mRlMeetingInfoViews.setBackgroundResource(R.drawable.shape_width_top_raduis_15_bg);
        }

        if(!TextUtils.isEmpty(mMeetInfo.getTelephone())&& !TextUtils.isEmpty(mMeetInfo.getSip())){
            mTxtSipNum.setText(mMeetInfo.getTelephone()+"-"+mMeetInfo.getSip());
        }


        mMeetingTheme.setText("即时会议");

        String week = DateUtils.getWeek(mMeetInfo.getStartTime());
        String date = mMeetInfo.getStartTime()+ "    "+week;

        mMeetingTime.setText(date);

        if(mMeetInfo.getRoomId() > 0){
            mMeetingRoomId.setText(String.valueOf(mMeetInfo.getRoomId()));
        }

        if(!TextUtils.isEmpty(mMeetInfo.getHostname())){
            mMeetingName.setText(mMeetInfo.getHostname());
        }

        if(!TextUtils.isEmpty(mMeetInfo.getUrl())){
            mMeetingInvite.setText(mMeetInfo.getUrl());
        }

        mMeetingClose.setOnClickListener(v->{
            if(isShowing())
                dismiss();
        });

        mMeetingRoomId.setOnClickListener(v -> {
            copyStringToClipboard("会议号"+mMeetInfo.getRoomId());
            Toast.makeText(mContext, "复制成功", Toast.LENGTH_LONG).show();
        });

        mMeetingInvite.setOnClickListener(v->{
            copyStringToClipboard("会议链接"+mMeetInfo.getUrl());
            Toast.makeText(mContext, "复制成功", Toast.LENGTH_LONG).show();
        });

    }

    private void copyStringToClipboard(String txt) {
        //获取剪贴板管理器：
        ClipboardManager cm =  null;
        if(mContext.getSystemService(Context.CLIPBOARD_SERVICE) instanceof ClipboardManager)
            cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mData = ClipData.newPlainText("Copy", txt);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mData);
    }
}
