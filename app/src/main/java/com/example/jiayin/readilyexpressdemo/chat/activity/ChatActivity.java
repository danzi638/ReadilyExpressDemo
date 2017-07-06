package com.example.jiayin.readilyexpressdemo.chat.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.jiayin.readilyexpressdemo.R;
import com.example.jiayin.readilyexpressdemo.friend.activity.GroupDetailActivity;
import com.example.jiayin.readilyexpressdemo.utils.Constant;
import com.example.jiayin.readilyexpressdemo.utils.SystemBarTintManager;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

// 会话详情页面
public class ChatActivity extends FragmentActivity {

    private static final String TAG = "ChatActivity:";
    private String mHxid;
    private EaseChatFragment easeChatFragment;
    private LocalBroadcastManager mLBM;
    private int mChatType;
    final int RIGHT = 0;
    final int LEFT = 1;
    GestureDetector myGestureDetector;
    private FrameLayout fl_chat;
    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    float x = e2.getX() - e1.getX();//滑动后的x值减去滑动前的x值 就是滑动的横向水平距离(x)
                    float y = e2.getY() - e1.getY();//滑动后的y值减去滑动前的y值 就是滑动的纵向垂直距离(y)

//            Log.w("tag", "x>" + x);
//            Log.w("tag", "y>" + y);
//            Log.w("tag", "velocityX>" + velocityX);
//            Log.w("tag", "velocityY>" + velocityY);
                    //如果滑动的横向距离大于100，表明是右滑了，那么就执行下面的方法，可以是关闭当前的activity
                    if (x > 100) {
                        doResult(RIGHT);
                        Toast.makeText(ChatActivity.this,"右滑",Toast.LENGTH_SHORT).show();

                        // Log.w("tag", "RIGHT>" + x);
                    }
                    //如果滑动的横向距离大于100，表明是左滑了(因为左滑为负数，所以距离大于100就是x值小于-100)
                    if (x < -100) {

                        // Log.w("tag", "LEFT>" + x);
                        doResult(LEFT);
                        Toast.makeText(ChatActivity.this,"左滑",Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //系统版本大于19
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.main);//设置标题栏颜色，此颜色在color中声明

        fl_chat = (FrameLayout) findViewById(R.id.fl_chat);
        myGestureDetector = new GestureDetector(ChatActivity.this, onGestureListener);
        fl_chat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                myGestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
        initData();
        initListener();

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

    }

    private void initListener() {
        easeChatFragment.setChatFragmentListener(new EaseChatFragment.EaseChatFragmentHelper() {
            @Override
            public void onSetMessageAttributes(EMMessage message) {

            }
            @Override
            public void onEnterToChatDetails() {
                Intent intent = new Intent(ChatActivity.this, GroupDetailActivity.class);
//                 群id
                intent.putExtra(Constant.GROUP_ID, mHxid);
                startActivity(intent);
                Log.d(TAG, "ChatActivity: " + "跳转到群成员页面");
                Toast.makeText(ChatActivity.this,"跳转到群成员页面",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAvatarClick(String username) {

            }
            @Override
            public void onAvatarLongClick(String username) {

            }
            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                return false;
            }
            @Override
            public void onMessageBubbleLongClick(EMMessage message) {

            }
            @Override
            public boolean onExtendMenuItemClick(int itemId, View view) {
                return false;
            }
            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
                return null;
            }
        });

        // 如果当前类型为群聊
        if(mChatType == EaseConstant.CHATTYPE_GROUP) {
            // 注册退群广播
            BroadcastReceiver ExitGroupReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if(mHxid.equals(intent.getStringExtra(Constant.GROUP_ID))) {
                        // 结束当前页面
                        finish();
                    }
                }
            };
            mLBM.registerReceiver(ExitGroupReceiver, new IntentFilter(Constant.EXIT_GROUP));
        }

    }

    private void initData() {
        // 创建一个会话的fragment
        easeChatFragment = new EaseChatFragment();
//        easeChatInputMenu = new EaseChatInputMenu();
        mHxid = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);
        // 获取聊天类型
        mChatType = getIntent().getExtras().getInt(EaseConstant.EXTRA_CHAT_TYPE);
        easeChatFragment.setArguments(getIntent().getExtras());
        // 替换fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_chat, easeChatFragment).commit();
        // 获取发送广播的管理者
        mLBM = LocalBroadcastManager.getInstance(ChatActivity.this);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println(" ACTION_DOWN");//手指在屏幕上按下
                break;
            case MotionEvent.ACTION_MOVE:
                System.out.println(" ACTION_MOVE");//手指正在屏幕上滑动
                break;
            case MotionEvent.ACTION_UP:
                System.out.println(" ACTION_UP");//手指从屏幕抬起了
                break;
            default:
                break;
        }
        return  myGestureDetector.onTouchEvent(event);
    }

    public void doResult(int action) {

        switch (action) {
            case RIGHT:
                System.out.println("go right");
                finish();
                break;
            case LEFT:
                System.out.println("go left");
                break;
        }
    }
}

