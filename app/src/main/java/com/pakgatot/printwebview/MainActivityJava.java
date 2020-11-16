package com.pakgatot.printwebview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivityJava extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.addJavascriptInterface(new MyJavascriptInterface(this, webView), "Android");
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                String url2="https://pays.wijayamart.com";
                if (url != null && url.startsWith(url2)){
                    return false;
                }
                // all links that points outside the site will be open in a normal android browser
                else  {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }
            }
        });

        webView.loadUrl("https://pays.wijayamart.com");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    class MyJavascriptInterface {
        private ArrayList<PrintJob> mPrintJobs = new ArrayList();

        Context context;
        WebView webView;

        public MyJavascriptInterface(Context mContext, WebView mWebView) {
            context = mContext;
            webView = mWebView;
        }

        @JavascriptInterface
        public void doPrint() {
            runOnUiThread(() -> createWebPrintJob(webView));
        }

        private void createWebPrintJob(WebView webView) {
            // Get a PrintManager instance
            PrintManager printManager = (PrintManager) webView.getContext()
                    .getSystemService(Context.PRINT_SERVICE);

            String jobName = getString(R.string.app_name) + " Document";

            // Get a print adapter instance
            PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

            // Create a print job with name and adapter instance
            PrintJob printJob = printManager.print(jobName, printAdapter,
                    new PrintAttributes.Builder().build());

            // Save the job object for later status checking

            mPrintJobs.add(printJob);
        }

    }
}
