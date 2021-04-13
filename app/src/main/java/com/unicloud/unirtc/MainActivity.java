package com.unicloud.unirtc;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.unicloud.unirtcuikitulink.util.PermissionManager;
import com.unicloud.unirtcuikitulink.wapper.UniContant;

public class MainActivity extends AppCompatActivity {

    private EditText mEditMeetingNum;
    private EditText mEditDisplayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_joinmeeting = findViewById(R.id.btn_joinmeeting_login);
        Button btn_createmeeting = findViewById(R.id.btn_createmeeting);

        mEditMeetingNum = findViewById(R.id.edit_joinmeeting_id_txt);
        mEditDisplayName = findViewById(R.id.edit_joinmeeting_name_txt);

        PermissionManager.requestEachCombined(this, null, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO});

        btn_joinmeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String meetingnum = mEditMeetingNum.getText().toString();
                String displayname = mEditDisplayName.getText().toString();
                if(TextUtils.isEmpty(meetingnum)){
                    Toast.makeText(MainActivity.this, "会议号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(displayname)) {
                    displayname = "android";
                }

                UniUserInfo info = new UniUserInfo(12367890, displayname, "");
                Intent intent = new Intent(MainActivity.this, UniUIKitActivity.class);
                intent.putExtra(UniContant.UNIRTCMEETISCREATE, false);
                intent.putExtra(UniContant.UNIRTCMEETUSEINFO, info);
                intent.putExtra(UniContant.UNIRTCMEETROOMID, meetingnum);
                startActivity(intent);

            }
        });

        btn_createmeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String displayname = mEditDisplayName.getText().toString();

                if (TextUtils.isEmpty(displayname)) {
                    displayname = "android";
                }

                UniUserInfo info = new UniUserInfo(12367890, displayname, "");
                Intent intent = new Intent(MainActivity.this, UniUIKitActivity.class);
                intent.putExtra(UniContant.UNIRTCMEETISCREATE, true);
                intent.putExtra(UniContant.UNIRTCMEETUSEINFO, info);
                startActivity(intent);
            }
        });

        if (!isTaskRoot()) {
            finish();
        }
    }
}