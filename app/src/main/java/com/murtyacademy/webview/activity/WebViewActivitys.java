package com.murtyacademy.webview.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.murtyacademy.R;
import com.murtyacademy.splash.view.SplashView;
import com.murtyacademy.utils.RestAPI;
import com.murtyacademy.utils.SuperCompatActivity;
import com.murtyacademy.webview.interactor.WebviewInteractor;
import com.murtyacademy.webview.interactor.WebviewInteractorInt;
import com.murtyacademy.webview.interactor.model.WebviewCompanyNamesRes;
import com.murtyacademy.webview.presenter.WebviewPresenter;
import com.murtyacademy.webview.view.WebviewView;


/**
 * Created by srikanth on 12/17/2018.
 */

public class WebViewActivitys extends SuperCompatActivity  implements WebviewView{

    private Context context;
    private RestAPI restAPI;
    private WebView webView;
    private String titleStr,weburl;
    private String titleName,brand_name,brand_url,status;
    private TextView company_name;
    private WebviewPresenter webviewPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setCustomTheme(getApplicationContext());
        setContentView(R.layout.activity_webview);

        initializeUiElements();

    }


    private void initializeUiElements() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            titleStr = extras.getString("titleStr");
            weburl = extras.getString("weburl");
        }


        setToolBar(getApplicationContext(), titleStr, "yes");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // back button

        context = this;
        restAPI = getRestAPIObj();

        webView = (WebView) findViewById(R.id.webview_ll);
        company_name = findViewById(R.id.company_name);




        webView.setWebViewClient(new WebViewActivitys.MyBrowser());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(weburl);

        final WebviewInteractorInt webviewInteractor = new WebviewInteractor(context, restAPI);
        webviewPresenter = new WebviewPresenter(this, webviewInteractor);



//        if (checkInternet()) {
//            webviewPresenter.getCompanyName();
//        } else {
//            showValidationToast(getResourceStr(context, R.string.plz_chk_your_net));
//        }

//        company_name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent5 = new Intent(WebViewActivitys.this,WebViewActivitys.class);
//                intent5.putExtra("titleStr",titleName);
//                intent5.putExtra("weburl",brand_url);
//                startActivity(intent5);
//            }
//        });


    }

    @Override
    public void serviceFailed(String str) {

    }

    @Override
    public void companyserviceSuccess(WebviewCompanyNamesRes companyNamesRes) {

        if(companyNamesRes.getResult() != null){

            titleName = companyNamesRes.getResult().get(0).getTitle();
            brand_name = companyNamesRes.getResult().get(0).getBrandName();
            brand_url = companyNamesRes.getResult().get(0).getBrandUrl();
            status = companyNamesRes.getResult().get(0).getStatus();

            if(status.equals("0")){
                company_name.setVisibility(View.GONE);
            }else {
                company_name.setVisibility(View.VISIBLE);
                company_name.setText(companyNamesRes.getResult().get(0).getBrandName().toString());
            }
        }

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showSuccessToast(String str) {

    }

    @Override
    public void showValidationToast(String str) {

    }

    @Override
    public void navigateToHomeScreen() {

    }

    @Override
    public void closeApp(String str) {

    }

    @Override
    public void serviceSuccess(String str) {

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}