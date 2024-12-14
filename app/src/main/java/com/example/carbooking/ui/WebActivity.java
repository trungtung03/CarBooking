package com.example.carbooking.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.carbooking.R;
import com.example.carbooking.utils.Constants;
import com.example.carbooking.utils.Helper;

import java.util.Objects;

public class WebActivity extends AppCompatActivity {

    private static final String TAG = WebActivity.class.getSimpleName();

    private WebView webView;
    private WebSettings webSettings;

    private String filename;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        filename = getRequiredPage();

        webView = findViewById(R.id.webview);

        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(true);

        webView.setWebViewClient(new CustomWebView());

        //prepare external url to load
        String remoteUrl = Constants.REMOTE_PATH + filename;
        if(!remoteUrl.contains("http")){
            Helper.displayErrorMessage(this, "Invalid url");
        }else{
            webView.loadUrl(remoteUrl);
        }
    }

    private String getRequiredPage(){
        return Objects.requireNonNull(getIntent().getExtras()).getString("INFORMATION");
    }

    private class CustomWebView extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
