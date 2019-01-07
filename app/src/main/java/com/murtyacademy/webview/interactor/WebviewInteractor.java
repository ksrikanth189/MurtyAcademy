package com.murtyacademy.webview.interactor;

import android.content.Context;
import android.util.Log;

import com.murtyacademy.R;
import com.murtyacademy.utils.RestAPI;
import com.murtyacademy.webview.interactor.model.WebviewCompanyNamesRes;
import com.murtyacademy.webview.presenter.WebviewPresenter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebviewInteractor implements WebviewInteractorInt {

    private Context context;
    private RestAPI restAPI;
    private WebviewPresenter webviewPresenter;
    private WebviewCompanyNamesRes brandResponse;

    public WebviewInteractor(Context context, RestAPI restAPI) {
        this.context = context;
        this.restAPI = restAPI;
    }



    @Override
    public void getCompanyName(WebviewPresenter webviewPresenter) {
        this.webviewPresenter = webviewPresenter;
          companyNameService();
    }


    private void companyNameService() {

//        webviewPresenter.context.showProgress();
        try {
            final Call<WebviewCompanyNamesRes> checkForUpdateResCall = restAPI.getWebviewCompanyNameResCall();
            checkForUpdateResCall.enqueue(new Callback<WebviewCompanyNamesRes>() {
                @Override
                public void onResponse(Call<WebviewCompanyNamesRes> call, Response<WebviewCompanyNamesRes> response) {
                 //   webviewPresenter.context.hideProgress();
                    if (response.isSuccessful()) {
                        brandResponse = response.body();
                        webviewPresenter.responseSuccess(brandResponse);
                    }
                    else {
                        webviewPresenter.responseFailed(brandResponse.getMessage().toString() != null ? brandResponse.getMessage().toString() : context.getResources().getString(R.string.server_not_responding));
                    }
                }

                @Override
                public void onFailure(Call<WebviewCompanyNamesRes> call, Throwable t) {
                  //  webviewPresenter.context.hideProgress();
                    webviewPresenter.responseFailed(webviewPresenter.context.getResources().getString(R.string.server_not_responding));
                }
            });
        } catch (Exception e) {
            Log.v("Login Exception", " Exception Msg ==> " + e.getMessage());
          //  webviewPresenter.context.hideProgress();
            webviewPresenter.responseFailed(webviewPresenter.context.getResources().getString(R.string.server_not_responding));
        }
    }




}
