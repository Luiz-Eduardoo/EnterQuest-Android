package com.example.enterquest;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class Chatbot extends AppCompatActivity {

    private WebView webViewChatbot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        webViewChatbot = findViewById(R.id.webViewChatbot);

        WebSettings webSettings = webViewChatbot.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        webViewChatbot.setWebViewClient(new WebViewClient());

        webViewChatbot.loadUrl(
                "https://cdn.botpress.cloud/webchat/v3.6/shareable.html?configUrl=https://files.bpcontent.cloud/2026/05/11/20/20260511201310-LNNPPBZ2.json"
        );
    }

    @Override
    public void onBackPressed() {
        if (webViewChatbot.canGoBack()) {
            webViewChatbot.goBack();
        } else {
            super.onBackPressed();
        }
    }
}