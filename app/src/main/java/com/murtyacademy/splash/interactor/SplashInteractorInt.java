package com.murtyacademy.splash.interactor;


import com.murtyacademy.splash.interactor.model.ContactsLIstReq;
import com.murtyacademy.splash.presenter.SplashPresenter;

public interface SplashInteractorInt {

    void contactsListService(SplashPresenter splashPresenter, ContactsLIstReq contactsLIstReq);
    void getCompanyName(SplashPresenter splashPresenter);
    void checkAppUpdate(SplashPresenter splashPresenter);


}
