package com.example.jiayin.readilyexpressdemo.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiayin.readilyexpressdemo.R;
import com.example.jiayin.readilyexpressdemo.chat.fragment.ChatFragment;
import com.example.jiayin.readilyexpressdemo.friend.activity.AddContactActivity;
import com.example.jiayin.readilyexpressdemo.friend.fragment.FriendFragment;
import com.example.jiayin.readilyexpressdemo.community.fragment.CommunityFragment;
import com.example.jiayin.readilyexpressdemo.model.Model;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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
    private TextView tv_add;
    private TextView tv_more;
    //修改nva

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
        initViewHead();
        registerForContextMenu(image_add);

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
                Toast.makeText(MainActivity.this,"点击了头像",Toast.LENGTH_SHORT).show();
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
        rg_main.check(R.id.rb_chat);
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
        tv_add = (TextView) findViewById(R.id.tv_add);
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
