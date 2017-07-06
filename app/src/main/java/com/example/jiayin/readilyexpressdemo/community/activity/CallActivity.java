package com.example.jiayin.readilyexpressdemo.community.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jiayin.readilyexpressdemo.R;
import com.example.jiayin.readilyexpressdemo.utils.SystemBarTintManager;

public class CallActivity extends Activity implements View.OnClickListener {

    public Intent intent2;
    public String str;

    Button btn_0;
    Button btn_1;
    Button btn_2;
    Button btn_3;
    Button btn_4;
    Button btn_5;
    Button btn_6;
    Button btn_7;
    Button btn_8;
    Button btn_9;
    Button btn_xinghao;
    Button btn_jinhao;
    Button btn_del;
    Button btn_chg;
    Button btn_call;
    EditText editText;
    ImageView community_exit;
    TextView community_name;
    Menu menu ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.activity_call);

        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_xinghao = (Button) findViewById(R.id.btn_xinghao);
        btn_jinhao = (Button) findViewById(R.id.btn_jinhao);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_chg = (Button) findViewById(R.id.btn_chg);
        btn_call = (Button) findViewById(R.id.btn_call);


        editText = (EditText) findViewById(R.id.et_input);

        community_exit = (ImageView) findViewById(R.id.community_exit);
        community_name = (TextView) findViewById(R.id.community_name);

        btn_0.setOnClickListener(this);
        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_xinghao.setOnClickListener(this);
        btn_jinhao.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_chg.setOnClickListener(this);
        btn_call.setOnClickListener(this);
        community_exit.setOnClickListener(this);
        community_name.setText("打电话");
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        str = editText.getText().toString().trim();
        switch (v.getId()) {
            case R.id.community_exit:
                Toast.makeText(CallActivity.this, "点击了返回", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
            case R.id.btn_jinhao:
            case R.id.btn_xinghao:

                editText.setText(str + ((Button) v).getText());
                break;
            case R.id.btn_chg:
                editText.setText("");
                break;
            case R.id.btn_del:
                if (!str.isEmpty() || !str.equals("")) {
                    editText.setText(str.substring(0, str.length() - 1));
                }
                break;
            case R.id.btn_call:
                if (isNubmer() && str.length() == 11) {
                    if (ContextCompat.checkSelfPermission(CallActivity.this, Manifest.permission
                            .CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CallActivity.this, new String[]{Manifest.permission
                                .CALL_PHONE}, 1);
                    } else {
                        call();
                    }

                } else {
                    Toast.makeText(CallActivity.this, "无法呼叫该用户！！", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    private void call() {
        try {
            intent2 = new Intent(intent2.ACTION_CALL, Uri.parse("tel:" + str));
            startActivity(intent2);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public boolean isNubmer() {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.call_setting,menu);
        return true;
    }
}