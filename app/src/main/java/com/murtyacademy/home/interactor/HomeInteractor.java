package com.murtyacademy.home.interactor;

import android.content.Context;
import android.util.Log;

import com.murtyacademy.R;
import com.murtyacademy.home.model.HomeCompanyNamesRes;
import com.murtyacademy.home.presenter.HomePresenter;
import com.murtyacademy.utils.RestAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeInteractor implements HomeInteractorInt {

    private Context context;
    private RestAPI restAPI;
    private HomePresenter splashPresenter;
    private HomeCompanyNamesRes brandResponse;

    public HomeInteractor(Context context, RestAPI restAPI) {
        this.context = context;
        this.restAPI = restAPI;
    }



    @Override
    public void getCompanyName(HomePresenter splashPresenter) {
        this.splashPresenter = splashPresenter;
          companyNameService();
    }


    private void companyNameService() {

//        splashPresenter.context.showProgress();
        try {
            final Call<HomeCompanyNamesRes> checkForUpdateResCall = restAPI.getHomeBrandNameResCall();
            checkForUpdateResCall.enqueue(new Callback<HomeCompanyNamesRes>() {
                @Override
                public void onResponse(Call<HomeCompanyNamesRes> call, Response<HomeCompanyNamesRes> response) {
                 //   splashPresenter.context.hideProgress();
                    if (response.isSuccessful()) {
                        brandResponse = response.body();
                        splashPresenter.responseSuccess(brandResponse);
                    }
                    else {
                        splashPresenter.responseFailed(brandResponse.getMessage().toString() != null ? brandResponse.getMessage().toString() : context.getResources().getString(R.string.server_not_responding));
                    }
                }

                @Override
                public void onFailure(Call<HomeCompanyNamesRes> call, Throwable t) {
                  //  splashPresenter.context.hideProgress();
                    splashPresenter.responseFailed(splashPresenter.context.getResources().getString(R.string.server_not_responding));
                }
            });
        } catch (Exception e) {
            Log.v("Login Exception", " Exception Msg ==> " + e.getMessage());
          //  splashPresenter.context.hideProgress();
            splashPresenter.responseFailed(splashPresenter.context.getResources().getString(R.string.server_not_responding));
        }
    }




}
