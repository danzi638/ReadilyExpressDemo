package com.example.jiayin.readilyexpressdemo.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.jiayin.readilyexpressdemo.R;
import com.example.jiayin.readilyexpressdemo.model.Model;
import com.example.jiayin.readilyexpressdemo.model.bean.UserInfo;
import com.hyphenate.chat.EMClient;


public class WelcomeActivity extends Activity {

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if (isFinishing()){
                return;
            }
            toMainOrLogin();
        }
    };
// 判断进入主页面还是登录页面

    private void toMainOrLogin() {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    // 判断当前账号是否已经登录过
                    if(EMClient.getInstance().isLoggedInBefore()) {// 登录过
                        // 获取到当前登录用户的信息
                        UserInfo account = Model.getInstance().getUserAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());
                        if(account == null) {
                            // 跳转到登录页面
                            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else {
                            // 登录成功后的方法
                            Model.getInstance().loginSuccess(account);

                            // 跳转到主页面
                            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }else {// 没登录过
                        // 跳转到登录页面
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    // 结束当前页面
                    finish();
                }
            });
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        handler.sendMessageDelayed(Message.obtain(),1000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
