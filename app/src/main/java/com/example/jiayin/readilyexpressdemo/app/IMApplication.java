package com.example.jiayin.readilyexpressdemo.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.jiayin.readilyexpressdemo.model.Model;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;

import static com.hyphenate.chat.EMGCMListenerService.TAG;

/**
 * Created by jiayin on 2017/5/29.
 */

public class IMApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化环信的EaseUI
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);//设置需要同意后才能接受邀请
        options.setAutoAcceptGroupInvitation(false);//设置需要同意后才能接受群聊
        EaseUI.getInstance().init(this,options);
        Log.d(TAG, "环信初始化成功");
        //初始化数据模型层类
        Model.getInstance().init(this);
        Log.d(TAG, "数据库初始化成功");

        //初始化全局上下文
        mContext = this;
        Log.d(TAG, "创建成功");

    }
    //获取全局上下文对象
    public static Context getGolbalApplication(){
        return mContext;
    }
}
