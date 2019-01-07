package com.murtyacademy.home.presenter;


import com.murtyacademy.home.activity.HomeActivity;
import com.murtyacademy.home.interactor.HomeInteractorInt;
import com.murtyacademy.home.model.HomeCompanyNamesRes;

public class HomePresenter {

    public HomeActivity context;
    private HomeInteractorInt splashInteractorInt;

    public HomePresenter(HomeActivity context, HomeInteractorInt splashInteractorInt) {
        this.splashInteractorInt = splashInteractorInt;
        this.context = context;
    }

    public void getCompanyName() {
        splashInteractorInt.getCompanyName(this);
    }


    public void responseFailed(String str) {
        context.showValidationToast(str);
    }



    public void responseSuccess(HomeCompanyNamesRes checkForUpdateRes) {
        if (checkForUpdateRes.getStatusCode() != null
                && checkForUpdateRes.getStatusCode() == 200
                && checkForUpdateRes.getStatus().equalsIgnoreCase("Success")) {
            context.companyserviceSuccess(checkForUpdateRes);
        }
        else {
            context.serviceFailed(checkForUpdateRes.getMessage());
        }
    }



}
