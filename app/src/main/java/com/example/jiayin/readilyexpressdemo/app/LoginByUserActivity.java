package com.example.jiayin.readilyexpressdemo.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jiayin.readilyexpressdemo.R;
import com.example.jiayin.readilyexpressdemo.model.Model;
import com.example.jiayin.readilyexpressdemo.model.bean.UserInfo;
import com.example.jiayin.readilyexpressdemo.utils.Constant;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

public class LoginByUserActivity extends Activity {
    private static final String TAG = "LoginByUserActivity";
    private EditText et_login_name1;
    private EditText et_login_pwd1;
    private Button bt_login_regist;
    private Button bt_login_login1;
//    private CustomVideoView videoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_login_by_user);
        initView();
        Intent intent = getIntent();
        String username = intent.getStringExtra(Constant.REGISTER_ID);
        Log.e(TAG, "login: "+ username );
        String password = intent.getStringExtra(Constant.REGISTER_PWD);
        Log.e(TAG, "login: "+ password );
        et_login_name1.setText(username);
        et_login_pwd1.setText(password);
        initListener();
    }

    private void initListener() {
        bt_login_login1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
//        bt_login_regist.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                register();
//                Intent intent = new Intent(LoginByUserActivity.this, RegisterActivity.class);
//                startActivity(intent);
//                Log.d(TAG, "跳转到注册页面");
//            }
//        });
    }

    private void register() {

    }

    // 登录按钮的页面逻辑处理
    private void login() {
        // 1 获取输入的用户名和密码

        final String loginName = et_login_name1.getText().toString();
        final String loginPwd = et_login_pwd1.getText().toString();

        // 2 校验输入的用户名和密码
        if (TextUtils.isEmpty(loginName) || TextUtils.isEmpty(loginPwd)) {
            Toast.makeText(LoginByUserActivity.this, "输入的用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

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
                                Toast.makeText(LoginByUserActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                // 跳转到主页面
                                Intent intent = new Intent(LoginByUserActivity.this, MainActivity.class);
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
                                Toast.makeText(LoginByUserActivity.this, "登录失败" + s, Toast.LENGTH_SHORT).show();
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

    private void initView() {
        et_login_name1 = (EditText) findViewById(R.id.et_login_name1);
        et_login_pwd1 = (EditText) findViewById(R.id.et_login_pwd1);
//        bt_login_regist = (Button) findViewById(R.id.bt_login_regist);
        bt_login_login1 = (Button) findViewById(R.id.bt_login_login1);
        //加载视频资源控件
//        videoview = (CustomVideoView) findViewById(R.id.videoView);
//        //设置播放加载路径
//        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ichat_2));
//        //播放
//        videoview.start();
//        //循环播放
//        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                videoview.start();
//            }
//        });

    }
    //返回重启加载
    @Override
    protected void onRestart() {
//        initView();
        super.onRestart();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
//        videoview.stopPlayback();
        super.onStop();
    }
}