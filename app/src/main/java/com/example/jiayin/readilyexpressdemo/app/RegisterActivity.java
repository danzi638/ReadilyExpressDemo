package com.example.jiayin.readilyexpressdemo.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiayin.readilyexpressdemo.R;
import com.example.jiayin.readilyexpressdemo.community.activity.SendMessageActivity;
import com.example.jiayin.readilyexpressdemo.model.Model;
import com.example.jiayin.readilyexpressdemo.utils.Constant;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class RegisterActivity extends Activity {
    private EditText et_register_username;
    private EditText et_register_phone;
    private Button btn_get;
    private EditText et_register_yzm;
    private EditText et_register_password;
    private EditText et_register_password_again;
    private Button bt_login_regist_to;

    private String phoneNumber;     // 电话号码
    private String verificationCode;  // 验证码
    private boolean flag;  // 操作是否成功


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        initListener();

        final Context context = RegisterActivity.this;            // context
        final String AppKey = "1f1583adef930";            // AppKey
        final String AppSecret = "2c49434b2a8c9c959f851f7d967f569d"; // AppSecret
//
        MobSDK.init(context, AppKey, AppSecret); // 初始化 SDK 单例，可以多次调用

        EventHandler eventHandler = new EventHandler(){    // 操作回调
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eventHandler);   // 注册回调接口
    }

    private void initListener() {
        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et_register_phone.getText())) {
                    if (et_register_phone.getText().length() == 11) {
                        phoneNumber = et_register_phone.getText().toString();
                        SMSSDK.getVerificationCode("86", phoneNumber); // 发送验证码给号码的 phoneNumber 的手机
                        et_register_phone.requestFocus();
                    } else {
                        Toast.makeText(RegisterActivity.this, "请输入完整的电话号码", Toast.LENGTH_SHORT).show();
                        et_register_phone.requestFocus();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    et_register_phone.requestFocus();
                }
            }
        });
        bt_login_regist_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et_register_yzm.getText())) {
                    if (et_register_yzm.getText().length() == 4) {
                        verificationCode = et_register_yzm.getText().toString();
                        SMSSDK.submitVerificationCode("86", phoneNumber, verificationCode);
                        flag = false;
                        register();
                    } else {
                        Toast.makeText(RegisterActivity.this, "请输入完整的验证码", Toast.LENGTH_SHORT).show();
                        et_register_yzm.requestFocus();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    et_register_yzm.requestFocus();
                }
            }
        });
    }

    private void register() {
        final String registerName = et_register_username.getText().toString().trim();
        final String registerPassword = et_register_password.getText().toString().trim();
        final String registerPasswordAgain = et_register_password_again.getText().toString().trim();
        if (TextUtils.isEmpty(registerName) || TextUtils.isEmpty(registerPassword) || TextUtils.isEmpty(registerPasswordAgain)) {
            Toast.makeText(RegisterActivity.this, "输入注册名或者密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //判断二次密码是否正确
        if (!registerPassword.equals(registerPasswordAgain)){
            Log.e(TAG, "register: " + registerPassword + " to " + registerPasswordAgain);
            Toast.makeText(RegisterActivity.this, "两次输入密码不匹配，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        //去服务器注册
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(registerName, registerPassword);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "注册成功");
                            Intent intent  = new Intent(RegisterActivity.this,LoginActivity.class);
                            intent.putExtra(Constant.REGISTER_ID,registerName);
                            intent.putExtra(Constant.REGISTER_PWD,registerPassword);
                            Log.e(TAG, "login: "+ registerName );
                            Log.e(TAG, "login: "+ registerPassword );
                            startActivity(intent);
                        }
                    });

                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this,"注册失败" + e.toString(),Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "注册失败");
                        }
                    });
                }
            }
        });
    }
    private void initView() {
        et_register_username = (EditText) findViewById(R.id.et_register_username);
        et_register_phone = (EditText) findViewById(R.id.et_register_phone);
        btn_get = (Button) findViewById(R.id.btn_get);
        et_register_yzm = (EditText) findViewById(R.id.et_register_yzm);
        et_register_password = (EditText) findViewById(R.id.et_register_password);
        et_register_password_again = (EditText) findViewById(R.id.et_register_password_again);
        bt_login_regist_to = (Button) findViewById(R.id.bt_login_regist_to);
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;

            if (result == SMSSDK.RESULT_COMPLETE) {
                // 如果操作成功
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    // 校验验证码，返回校验的手机和国家代码
                    Toast.makeText(RegisterActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    // 获取验证码成功，true为智能验证，false为普通下发短信
                    Toast.makeText(RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    // 返回支持发送验证码的国家列表
                }
            } else {
                // 如果操作失败
                if (flag) {
                    Toast.makeText(RegisterActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                    et_register_phone.requestFocus();
                } else {
                    ((Throwable) data).printStackTrace();
                    Toast.makeText(RegisterActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler(); // 注销回调接口
    }
}
