package com.murtyacademy.Login.view;


import com.murtyacademy.Login.interactor.model.LoginActivityRes;

public interface LoginView {

    void serviceSuccess(LoginActivityRes userLoginActivityRes);
    void serviceFailed(String str);
    void showProgress();
    void hideProgress();
    void showSuccessToast(String str);
    void showValidationToast(String str);
    void navigateToHomeScreen();

    void closeApp(String str);
}
