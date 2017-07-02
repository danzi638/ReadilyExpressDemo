package com.example.jiayin.readilyexpressdemo.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jiayin.readilyexpressdemo.R;
import com.example.jiayin.readilyexpressdemo.app.bean.User;

import java.util.ArrayList;

public class SettingUserActivity extends Activity {

    //
    public ArrayList<User> users = new ArrayList<User>();

    //
    EditText edt_username;
    EditText edt_cellphone;
    EditText edt_email;
    ImageView imag_1;
    ImageView imag_2;
    Button btn_send;
    Button btn_view_selfinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_user);


        //
        edt_cellphone = (EditText) findViewById(R.id.edt_cellphone);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_username = (EditText) findViewById(R.id.edt_username);
        imag_1 = (ImageView) findViewById(R.id.imag_1);
        imag_2 = (ImageView) findViewById(R.id.imag_2);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_view_selfinfo = (Button) findViewById(R.id.btn_view_selfinfo);

        //

    }

    @SuppressLint("NewApi")
    public void onClick(View v) {
        String edt_e = edt_email.getText().toString();
        String edt_c = edt_cellphone.getText().toString();
        String edt_u = edt_username.getText().toString();

        users.add(new User(edt_u, edt_c, edt_e, R.drawable.icon_ichat, R.drawable.icon_ichat));
        switch (v.getId()) {
            case R.id.btn_send:
                Toast.makeText(SettingUserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btn_view_selfinfo:

                //
                String score = null;

                //intent 页面跳转语句
                Intent intent = new Intent(SettingUserActivity.this, AccountInfoActivity.class);

                Bundle bund = new Bundle();

                bund.putSerializable("user", users.toArray());

                intent.putExtra("user", bund);

                if ((!edt_c.isEmpty() || !edt_c.equals("")) && (!edt_u.isEmpty() || !edt_u.equals("")) && (!edt_e.isEmpty() || !edt_e.equals("")) && (imag_1 != null) && (imag_2 != null)) {
                    score = "极好";

                } else if ((!edt_c.isEmpty() || !edt_c.equals("")) && (!edt_u.isEmpty() || !edt_u.equals("")) && (!edt_e.isEmpty() || !edt_e.equals("")) && (imag_1 != null)) {

                    score = "好";

                } else if ((!edt_c.isEmpty() || !edt_c.equals("")) && (!edt_u.isEmpty() || !edt_u.equals("")) && (!edt_e.isEmpty() || !edt_e.equals(""))) {
                    score = "中";
                } else {
                    score = "一般";
                }
                //如果正确就把值放进 intent中
                intent.putExtra("name", edt_u);
                intent.putExtra("cellphone", edt_c);
                intent.putExtra("email", edt_e);
                intent.putExtra("score", score);
                //页面跳转
                startActivity(intent);

                break;
        }
    }
}
