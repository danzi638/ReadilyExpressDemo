package com.example.jiayin.readilyexpressdemo.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.jiayin.readilyexpressdemo.R;
import com.example.jiayin.readilyexpressdemo.app.bean.User;

import java.util.ArrayList;

public class AccountInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);

        //////////////
        Intent intent = getIntent();

        Bundle bund = intent.getBundleExtra("user");

        Object[] objs = (Object[]) bund.get("user");

        //在里面用的动态list
        ArrayList<User> users = new ArrayList<User>();

        for (int i = 0; i < objs.length; i++) {
            users.add((User) objs[i]);
        }

        //把intent中的值放进文本框中

        TextView self_edt_u = (TextView) findViewById(R.id.txt_self_username);
        String msg1 = intent.getStringExtra("name");
        self_edt_u.setText(msg1);

        TextView self_edt_c = (TextView) findViewById(R.id.txt_self_cellphone);
        String msg2 = intent.getStringExtra("cellphone");
        self_edt_c.setText(msg2);

        TextView self_edt_e = (TextView) findViewById(R.id.txt_self_email);
        String msg3 = intent.getStringExtra("email");
        self_edt_e.setText(msg3);

        TextView self_score = (TextView) findViewById(R.id.txt_score);
        String msg4 = intent.getStringExtra("score");
        self_score.setText(msg4);
        ////////////
    }


    //点击按钮回到登陆页面
    public void onClick(View v) {
        Intent intent1 = new Intent(AccountInfoActivity.this, MainActivity.class);
        intent1.getExtras();
        startActivity(intent1);

    }

}