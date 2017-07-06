package com.example.jiayin.readilyexpressdemo.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiayin.readilyexpressdemo.R;
import com.example.jiayin.readilyexpressdemo.model.Model;
import com.example.jiayin.readilyexpressdemo.model.bean.UserInfo;
import com.example.jiayin.readilyexpressdemo.utils.Constant;
//import com.example.jiayin.readilyexpressdemo.utils.CustomVideoView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class LoginActivity extends Activity {

    private EditText et_login_name;
    private EditText et_login_pwd;
    private Button btn_send_to;//获取验证码
    private Button bt_login_login;
    private TextView tv_to_login;
    private TextView tv_to_register;
    //    private CustomVideoView videoview;
    private String phoneNumber;     // 电话号码
    private String verificationCode;  // 验证码
    private boolean flag;  // 操作是否成功
    //按钮样式改变
    //控制按钮样式是否改变
    private boolean tag = true;
    //每次验证请求需要间隔60S
    private int i=60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login);
        initView();
        Intent intent = getIntent();
        String username = intent.getStringExtra(Constant.REGISTER_ID);
        Log.e(TAG, "login: " + username);
        String password = intent.getStringExtra(Constant.REGISTER_PWD);
        Log.e(TAG, "login: " + password);
        et_login_name.setText(username);
        et_login_pwd.setText(password);
        initListener();
        final Context context = LoginActivity.this;            // context
        final String AppKey = "1f1583adef930";            // AppKey
        final String AppSecret = "2c49434b2a8c9c959f851f7d967f569d"; // AppSecret
//
        MobSDK.init(context, AppKey, AppSecret); // 初始化 SDK 单例，可以多次调用

        EventHandler eventHandler = new EventHandler() {    // 操作回调
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

    // 登录按钮的页面逻辑处理
    private void login() {
        // 1 获取输入的用户名和密码
        final String loginName = et_login_name.getText().toString();
        final String loginPwd = et_login_pwd.getText().toString();
        // 3 登录逻辑处理
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                // 去环信服务器登录
                EMClient.getInstance().login(loginName, loginPwd, new EMCallBack() {
                    // 登录成功后的处理
                    @Override
                    public void onSuccess() {
                        // 对模型层数据的处理
                        Model.getInstance().loginSuccess(new UserInfo(loginName));
                        // 保存用户账号信息到本地数据库
                        Model.getInstance().getUserAccountDao().addAccount(new UserInfo(loginName));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 提示登录成功
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                // 跳转到主页面
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                Log.d(TAG, "登陆成功");
                                finish();
                            }
                        });
                    }

                    // 登录失败的处理
                    @Override
                    public void onError(int i, final String s) {
                        // 提示登录失败
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登录失败" + s, Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "登陆失败");
                            }
                        });
                    }

                    // 登录过程中的处理
                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });
    }
    private void register() {
        final String registerName = et_login_name.getText().toString().trim();
        final String registerPassword = et_login_pwd.getText().toString().trim();
        //去服务器注册
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(registerName, registerPassword);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "注册成功");
                            Log.e(TAG, "login: " + registerName);
                            Log.e(TAG, "login: " + registerPassword);
                            login();
                        }
                    });

                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LoginActivity.this, "注册失败" + e.toString(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "注册失败");
                        }
                    });
                }
            }
        });
    }
    private void initView() {
        et_login_name = (EditText) findViewById(R.id.et_login_name);
        et_login_pwd = (EditText) findViewById(R.id.et_login_pwd);
        btn_send_to = (Button) findViewById(R.id.btn_send_to);
        bt_login_login = (Button) findViewById(R.id.bt_login_login);
        tv_to_login = (TextView) findViewById(R.id.tv_to_login);
        tv_to_register = (TextView) findViewById(R.id.tv_to_register);
    }
    /**
     * 改变按钮样式
     * */
    private void changeBtnGetCode() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                if (tag) {
                    while (i > 0) {
                        i--;
                        //如果活动为空
                        if (LoginActivity.this == null) {
                            break;
                        }
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btn_send_to.setText("重新发送(" + i + "s" + ")");
                                btn_send_to.setClickable(false);
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    tag = false;
                }
                i = 60;
                tag = true;

                if (LoginActivity.this != null) {
                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_send_to.setText("获取验证码");
                            btn_send_to.setClickable(true);
                        }
                    });
                }
            }
        };
        thread.start();
    }


    private void initListener() {
        btn_send_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et_login_name.getText())) {
                    if (et_login_name.getText().length() == 11) {
                        phoneNumber = et_login_name.getText().toString();
                        SMSSDK.getVerificationCode("86", phoneNumber); // 发送验证码给号码的 phoneNumber 的手机
                        et_login_name.requestFocus();
                    } else {
                        Toast.makeText(LoginActivity.this, "请输入完整的电话号码", Toast.LENGTH_SHORT).show();
                        et_login_name.requestFocus();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "请输入电话号码", Toast.LENGTH_SHORT).show();
                    et_login_name.requestFocus();
                }
            }
        });
        bt_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et_login_pwd.getText())) {
                    if (et_login_pwd.getText().length() == 4) {
                        verificationCode = et_login_pwd.getText().toString();
                        SMSSDK.submitVerificationCode("86", phoneNumber, verificationCode);
                        flag = false;
                        register();
                    } else {
                        Toast.makeText(LoginActivity.this, "请输入完整的验证码", Toast.LENGTH_SHORT).show();
                        et_login_pwd.requestFocus();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    et_login_pwd.requestFocus();
                }
            }
        });
        tv_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, LoginByUserActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tv_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
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
                    Toast.makeText(LoginActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    // 获取验证码成功，true为智能验证，false为普通下发短信
                    changeBtnGetCode();
                    Toast.makeText(LoginActivity.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    // 返回支持发送验证码的国家列表
                }
            } else {
                // 如果操作失败
                if (flag) {
                    Toast.makeText(LoginActivity.this, "验证码获取失败，请重新获取", Toast.LENGTH_SHORT).show();
                    et_login_name.requestFocus();
                } else {
                    ((Throwable) data).printStackTrace();
                    Toast.makeText(LoginActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
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