package com.example.jiayin.readilyexpressdemo.app;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiayin.readilyexpressdemo.R;
import com.example.jiayin.readilyexpressdemo.chat.fragment.ChatFragment;
import com.example.jiayin.readilyexpressdemo.community.activity.ShareActivity;
import com.example.jiayin.readilyexpressdemo.friend.activity.AddContactActivity;
import com.example.jiayin.readilyexpressdemo.friend.fragment.FriendFragment;
import com.example.jiayin.readilyexpressdemo.community.fragment.CommunityFragment;
import com.example.jiayin.readilyexpressdemo.model.Model;
import com.example.jiayin.readilyexpressdemo.utils.PermissionsManager;
import com.example.jiayin.readilyexpressdemo.utils.PermissionsResultAction;
import com.example.jiayin.readilyexpressdemo.utils.SystemBarTintManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.mob.MobSDK;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class MainActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity:";
    private DrawerLayout mDrawerLayout;
    private ImageView image_user;
    private NavigationView navView;
    private RadioGroup rg_main;
    private ChatFragment chatFragment;
    private FriendFragment friendFragment;
    private CommunityFragment communityFragment;

    //修改title属性
    private TextView tv_title_name;
    private ImageView image_add;
    private ImageView tv_add;
    private TextView tv_more;
    //修改nva

    //设置分享
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
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
        setContentView(R.layout.activity_main);
        /**
         * 请求所有必要的权限----
         */
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                Log.e(TAG, "onGranted: " + "All permissions have been granted");
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                Log.e(TAG, "onGranted: " + "Permission:" + permission + " has been denied");

                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });

        initView();
        initData();
        initListener();
        initViewHead();
        registerForContextMenu(image_add);
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    private void initViewHead() {

        View headerView = navView.inflateHeaderView(R.layout.nav_header);
        de.hdodenhof.circleimageview.CircleImageView icon_image =
                (de.hdodenhof.circleimageview.CircleImageView) headerView.findViewById(R.id.icon_image);
        TextView nav_username = (TextView) headerView.findViewById(R.id.nav_username);
        nav_username.append(EMClient.getInstance().getCurrentUser());
        TextView phone = (TextView) headerView.findViewById(R.id.nav_phone);
        phone.append("15880293295");
        TextView xinyong = (TextView) headerView.findViewById(R.id.nav_xinyong);
        xinyong.append("良好");
        icon_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "点击了头像", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
    }

    private void initListener() {
        //RadioGroup的选择事件
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                image_add.setVisibility(View.GONE);
                tv_add.setVisibility(View.GONE);
                tv_more.setVisibility(View.GONE);
                Fragment fragment = null;
                switch (checkedId) {
                    // 会话列表页面
                    case R.id.rb_chat:
                        fragment = chatFragment;
                        tv_title_name.setText("会话");
                        image_add.setVisibility(View.VISIBLE);
                        break;

                    // 联系人列表页面
                    case R.id.rb_friend:
                        fragment = friendFragment;
                        tv_title_name.setText("联系人");
                        tv_more.setVisibility(View.VISIBLE);
                        break;

                    // 设置页面
                    case R.id.rb_community:
                        fragment = communityFragment;
                        tv_title_name.setText("发现");
                        tv_add.setVisibility(View.VISIBLE);
                        break;
                }
                // 实现fragment切换的方法
                switchFragment(fragment);
            }
        });
        image_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                startActivity(intent);
            }
        });
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "点击了分享", Toast.LENGTH_SHORT).show();
                showShare();
            }
        });
        rg_main.check(R.id.rb_chat);
    }

    private void showShare() {
        MobSDK.init(MainActivity.this,"1f1583adef930","2c49434b2a8c9c959f851f7d967f569d");
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("iChat");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我正在使用iChat,推荐您也来一起使用");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://i4.piimg.com/1949/14dafddbcf7c5c27.png");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("iChat是当今非常流行的一款聊天软件，大家快来用吧");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Share Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            ContentResolver cr = this.getContentResolver();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                ImageView imageView = (ImageView) findViewById(R.id.icon_image);
                /* 将Bitmap设定到ImageView */
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // 添加布局
        MainActivity.this.getMenuInflater().inflate(R.menu.more, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.more_group) {
            // 执行删除选中的联系人操作
//        deleteContact();
            Toast.makeText(MainActivity.this, "点了了新建群组", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.more_add_friend) {
            // 执行删除选中的联系人操作
//        deleteContact();
            Toast.makeText(MainActivity.this, "点了了添加好友", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.more_help) {
            // 执行删除选中的联系人操作
//        deleteContact();
            Toast.makeText(MainActivity.this, "点了了帮助", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    // 实现fragment切换的方法
    private void switchFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
    }

    private void initData() {
        // 创建三个fragment对象
        chatFragment = new ChatFragment();
        friendFragment = new FriendFragment();
        communityFragment = new CommunityFragment();

    }

    private void initView() {
        rg_main = (RadioGroup) findViewById(R.id.rg_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.nav_view);
        image_user = (ImageView) findViewById(R.id.image_user);
        tv_title_name = (TextView) findViewById(R.id.tv_title_name);
        image_add = (ImageView) findViewById(R.id.image_add);
        tv_add = (ImageView) findViewById(R.id.tv_add);
        tv_more = (TextView) findViewById(R.id.tv_more);
        navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_exit) {
            // Handle the camera action
//            Toast.makeText(MainActivity.this,"为",Toast.LENGTH_SHORT).show();
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    // 登录环信服务器退出登录
                    EMClient.getInstance().logout(false, new EMCallBack() {
                        @Override
                        public void onSuccess() {
                            // 关闭DBHelper
                            Model.getInstance().getDbManager().close();
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // 更新ui显示
                                    Toast.makeText(MainActivity.this, "退出成功", Toast.LENGTH_SHORT).show();
                                    // 回到登录页面
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    MainActivity.this.finish();
                                }
                            });

                        }

                        @Override
                        public void onError(int i, final String s) {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "退出失败" + s, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onProgress(int i, String s) {

                        }
                    });
                }
            });
        } else if (id == R.id.nav_setting_user) {
            Intent intent = new Intent(MainActivity.this, SettingUserActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "点击了设置个人信息", Toast.LENGTH_SHORT).show();
        }
// else if (id == R.id.nav_slideshow) {
//            Toast.makeText(MainActivity.this,"为",Toast.LENGTH_SHORT).show();
//
//        }
// else if (id == R.id.nav_manage) {
//            Toast.makeText(MainActivity.this,"为",Toast.LENGTH_SHORT).show();
//
//        }
// else if (id == R.id.nav_share) {
//            Toast.makeText(MainActivity.this,"为",Toast.LENGTH_SHORT).show();
//
//        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
