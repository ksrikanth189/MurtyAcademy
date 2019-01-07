package com.murtyacademy.splash.presenter;


import com.murtyacademy.splash.activity.SplashActivity;
import com.murtyacademy.splash.interactor.SplashInteractorInt;
import com.murtyacademy.splash.interactor.model.CheckForUpdateRes;
import com.murtyacademy.splash.interactor.model.CompanyNamesRes;
import com.murtyacademy.splash.interactor.model.ContactsLIstReq;
import com.murtyacademy.splash.interactor.model.ContactsLIstRes;

public class SplashPresenter {

    public SplashActivity context;
    private SplashInteractorInt splashInteractorInt;

    public SplashPresenter(SplashActivity context, SplashInteractorInt splashInteractorInt) {
        this.splashInteractorInt = splashInteractorInt;
        this.context = context;
    }

    public void getCompanyName() {
        splashInteractorInt.getCompanyName(this);
    }


    public void responseFailed(String str) {
        context.showValidationToast(str);
    }

     public void contactsList(ContactsLIstReq contactsLIstReq) {

        splashInteractorInt.contactsListService(this,contactsLIstReq);
    }

    public void responseSave(ContactsLIstRes userForgetPasswordRes) {
            context.serviceSuccess(userForgetPasswordRes.getMessage());
         }

    public void responseSuccess(CompanyNamesRes checkForUpdateRes) {
        if (checkForUpdateRes.getStatusCode() != null
                && checkForUpdateRes.getStatusCode() == 200
                && checkForUpdateRes.getStatus().equalsIgnoreCase("Success")) {
            context.companyserviceSuccess(checkForUpdateRes);
        }
        else {
            context.serviceFailed(checkForUpdateRes.getMessage());
        }
    }




    public void checkForAppUpdate() {
        splashInteractorInt.checkAppUpdate(this);
    }


    public void responseSuccess(CheckForUpdateRes checkForUpdateRes) {
        if (checkForUpdateRes.getStatusCode() != null
                && checkForUpdateRes.getStatusCode() == 200
                && checkForUpdateRes.getStatus().equalsIgnoreCase("Success")) {
            context.serviceUpdateSuccess(checkForUpdateRes);
        }
        else {
            context.serviceFailed(checkForUpdateRes.getMessage());
        }
    }











}
