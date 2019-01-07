package com.murtyacademy.home.view;


import com.murtyacademy.home.model.HomeCompanyNamesRes;

public interface HomeView {

    void serviceFailed(String str);
    void companyserviceSuccess(HomeCompanyNamesRes str);

    void showProgress();

    void hideProgress();

    void showSuccessToast(String str);

    void showValidationToast(String str);

    void navigateToHomeScreen();

    void closeApp(String str);

    void serviceSuccess(String str);

}
