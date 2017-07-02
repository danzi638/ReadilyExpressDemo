package com.example.jiayin.readilyexpressdemo.community.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jiayin.readilyexpressdemo.R;

public class ShoppingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        TextView community_name = (TextView) findViewById(R.id.community_name);
        community_name.setText("京东购物");
        ImageView community_exit = (ImageView) findViewById(R.id.community_exit);
        community_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings settings = webView.getSettings();
        webView.loadUrl("https://m.jd.com/");
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
    }
}
