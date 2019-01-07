package com.murtyacademy.Login.presenter;


import com.murtyacademy.Login.activity.LoginActivity;
import com.murtyacademy.Login.interactor.LoginInteractorInt;
import com.murtyacademy.Login.interactor.model.LoginActivityReq;
import com.murtyacademy.Login.interactor.model.LoginActivityRes;
import com.murtyacademy.R;
import com.murtyacademy.sharedpref.SharedPrefHelper;

public class LoginPresenter {

    public LoginActivity context;

    private LoginInteractorInt loginInteractorInt;

    public LoginPresenter(LoginActivity context, LoginInteractorInt loginInteractorInt) {
        this.loginInteractorInt = loginInteractorInt;
        this.context = context;
    }

    public void signInBtnClicked(String userId, String password) {
        if (userId.length() == 0) {
            context.showValidationToast(context.getResourceStr(context, R.string.pls_enter_register_mobile));

        } else if (password.length() == 0) {
            context.showValidationToast(context.getResourceStr(context, R.string.pls_enter_password));
        }  else {
            LoginActivityReq adminLoginReq = new LoginActivityReq();
            adminLoginReq.setMobile(userId);
            adminLoginReq.setPassword(password);

            loginInteractorInt.callLoginWebService(this, adminLoginReq);

            SharedPrefHelper.saveLoginDtsReq(context, adminLoginReq);




        }
    }



    public void responseSuccess(LoginActivityRes userLoginActivityRes) {
        if (userLoginActivityRes.getStatusCode() != null
                && userLoginActivityRes.getStatusCode() == 200
                && userLoginActivityRes.getStatus().equalsIgnoreCase("Success")) {
            if (userLoginActivityRes.getResult() != null) {
                context.serviceSuccess(userLoginActivityRes);
            }

        } else {
            context.serviceFailed(userLoginActivityRes.getMessage());
        }
    }

    public void responseFailed(String str) {
        context.showValidationToast(str);
    }

    public void saveLogin(LoginActivityRes userLoginActivityRes) {
        SharedPrefHelper.saveLoginDts(context, userLoginActivityRes);

        if (userLoginActivityRes.getResult() != null) {
            context.navigateToHomeScreen();

        }
    }

}
