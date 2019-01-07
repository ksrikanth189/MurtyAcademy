package com.murtyacademy.webview.view;


import com.murtyacademy.webview.interactor.model.WebviewCompanyNamesRes;

public interface WebviewView {

    void serviceFailed(String str);
    void companyserviceSuccess(WebviewCompanyNamesRes str);

    void showProgress();

    void hideProgress();

    void showSuccessToast(String str);

    void showValidationToast(String str);

    void navigateToHomeScreen();

    void closeApp(String str);

    void serviceSuccess(String str);

}
