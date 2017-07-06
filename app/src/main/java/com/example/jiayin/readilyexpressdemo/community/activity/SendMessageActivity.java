package com.example.jiayin.readilyexpressdemo.community.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiayin.readilyexpressdemo.R;
import com.example.jiayin.readilyexpressdemo.app.WelcomeActivity;
import com.example.jiayin.readilyexpressdemo.utils.SystemBarTintManager;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class SendMessageActivity extends Activity {
    private EditText numET;
    private EditText contentET;
    ImageView community_exit;
    TextView community_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //系统版本大于19
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.main);//设置标题栏颜色，此颜色在color中声明
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;        // a|=b的意思就是把a和b按位或然后赋值给a   按位或的意思就是先把a和b都换成2进制，然后用或操作，相当于a=a|b
        } else {
            winParams.flags &= ~bits;        //&是位运算里面，与运算  a&=b相当于 a = a&b  ~非运算符
        }
        win.setAttributes(winParams);
        setContentView(R.layout.activity_send_message);

        initView(); // 初始化控件、注册点击事件
        initListener();
    }

    private void initListener() {
        community_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SendMessageActivity.this,"点击了返回", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void sendSms(View view){
        String num=numET.getText().toString();
        String content=contentET.getText().toString();

        SmsManager manager= SmsManager.getDefault();
        manager.sendTextMessage(num,null,content,null,null);

        contentET.setText("");
        Toast.makeText(SendMessageActivity.this,"发送给" + num + "联系人短信成功", Toast.LENGTH_SHORT).show();

    }
    private void initView() {
        numET = (EditText) findViewById(R.id.numberET);
        contentET = (EditText) findViewById(R.id.contentET);
        community_exit = (ImageView) findViewById(R.id.community_exit);
        community_name = (TextView) findViewById(R.id.community_name);
        community_name.setText("发送短信");
    }
}