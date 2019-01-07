package com.murtyacademy.splash.view;


import com.murtyacademy.splash.interactor.model.CheckForUpdateRes;
import com.murtyacademy.splash.interactor.model.CompanyNamesRes;

public interface SplashView {

    void serviceFailed(String str);
    void companyserviceSuccess(CompanyNamesRes str);

    void showProgress();

    void hideProgress();

    void showSuccessToast(String str);

    void showValidationToast(String str);

    void navigateToHomeScreen();

    void closeApp(String str);

    void serviceSuccess(String str);
    void serviceUpdateSuccess(CheckForUpdateRes  checkForUpdateRes);

}
