package com.murtyacademy.splash.interactor;

import android.content.Context;
import android.util.Log;

import com.murtyacademy.R;
import com.murtyacademy.sharedpref.SharedPrefHelper;
import com.murtyacademy.splash.interactor.model.CheckForUpdateRes;
import com.murtyacademy.splash.interactor.model.CompanyNamesRes;
import com.murtyacademy.splash.interactor.model.ContactsLIstReq;
import com.murtyacademy.splash.interactor.model.ContactsLIstRes;
import com.murtyacademy.splash.presenter.SplashPresenter;
import com.murtyacademy.utils.RestAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashInteractor implements SplashInteractorInt {

    private Context context;
    private RestAPI restAPI;
    private SplashPresenter splashPresenter;
    private CompanyNamesRes brandResponse;
    private CheckForUpdateRes checkForUpdateRes;


    public SplashInteractor(Context context, RestAPI restAPI) {
        this.context = context;
        this.restAPI = restAPI;
    }


    @Override
    public void contactsListService(SplashPresenter splashPresenter, ContactsLIstReq contactsLIstReq) {
        this.splashPresenter = splashPresenter;
        contactsListSave(contactsLIstReq);

    }

    @Override
    public void getCompanyName(SplashPresenter splashPresenter) {
          companyNameService();
    }

    @Override
    public void checkAppUpdate(SplashPresenter splashPresenter) {
        this.splashPresenter = splashPresenter;
        checkApplicationUpdate();
    }

    private void contactsListSave(ContactsLIstReq contactsLIstReq) {
        splashPresenter.context.showProgress();
        try {
            final Call<ContactsLIstRes> cargoStrPayloadCall = restAPI.getContactsListResCall(contactsLIstReq);
            cargoStrPayloadCall.enqueue(new Callback<ContactsLIstRes>() {
                @Override
                public void onResponse(Call<ContactsLIstRes> call, Response<ContactsLIstRes> response) {
                    splashPresenter.context.hideProgress();
                    if (response.isSuccessful()) {
                        ContactsLIstRes equipmentRes = response.body();
                        splashPresenter.responseSave(equipmentRes);
                    } else {
                        splashPresenter.responseFailed(splashPresenter.context.getResources().getString(R.string.server_not_responding));
                    }
                }

                @Override
                public void onFailure(Call<ContactsLIstRes> call, Throwable t) {
                    splashPresenter.context.hideProgress();
                    splashPresenter.responseFailed(splashPresenter.context.getResources().getString(R.string.server_not_responding));
                }
            });
        } catch (Exception e) {
            Log.v("Login Exception", " Exception Msg ==> " + e.getMessage());
            splashPresenter.context.hideProgress();
            splashPresenter.responseFailed(splashPresenter.context.getResources().getString(R.string.server_not_responding));
        }
    }

    private void companyNameService() {

//        splashPresenter.context.showProgress();
        try {
            final Call<CompanyNamesRes> checkForUpdateResCall = restAPI.getCompanyNameResCall();
            checkForUpdateResCall.enqueue(new Callback<CompanyNamesRes>() {
                @Override
                public void onResponse(Call<CompanyNamesRes> call, Response<CompanyNamesRes> response) {
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
                public void onFailure(Call<CompanyNamesRes> call, Throwable t) {
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


    private void checkApplicationUpdate() {

        splashPresenter.context.showProgress();
        try {
            final Call<CheckForUpdateRes> checkForUpdateResCall = restAPI.getCheckForUpdateResCall();
            checkForUpdateResCall.enqueue(new Callback<CheckForUpdateRes>() {
                @Override
                public void onResponse(Call<CheckForUpdateRes> call, Response<CheckForUpdateRes> response) {
                    splashPresenter.context.hideProgress();
                    if (response.isSuccessful()) {
                        checkForUpdateRes = response.body();
                        splashPresenter.responseSuccess(checkForUpdateRes);
                    }
                    else {
                        splashPresenter.responseFailed(checkForUpdateRes.getMessage().toString() != null ? checkForUpdateRes.getMessage().toString() : context.getResources().getString(R.string.server_not_responding));

                    }
                }

                @Override
                public void onFailure(Call<CheckForUpdateRes> call, Throwable t) {
                    splashPresenter.context.hideProgress();
                    splashPresenter.responseFailed(splashPresenter.context.getResources().getString(R.string.server_not_responding));
                }
            });
        } catch (Exception e) {
            Log.v("Login Exception", " Exception Msg ==> " + e.getMessage());
            splashPresenter.context.hideProgress();
            splashPresenter.responseFailed(splashPresenter.context.getResources().getString(R.string.server_not_responding));
        }
    }





}
