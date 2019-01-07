package com.murtyacademy.Login.interactor;


import com.murtyacademy.Login.interactor.model.LoginActivityReq;
import com.murtyacademy.Login.presenter.LoginPresenter;

public interface LoginInteractorInt {

    public void callLoginWebService(LoginPresenter userLoginPresenter, LoginActivityReq userLoginActivityReq);
}
