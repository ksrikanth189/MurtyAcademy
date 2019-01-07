package com.murtyacademy.webview.presenter;
import com.murtyacademy.webview.activity.WebViewActivitys;
import com.murtyacademy.webview.interactor.WebviewInteractorInt;
import com.murtyacademy.webview.interactor.model.WebviewCompanyNamesRes;

public class WebviewPresenter {

    public WebViewActivitys context;
    private WebviewInteractorInt webviewInteractorInt;

    public WebviewPresenter(WebViewActivitys context, WebviewInteractorInt webviewInteractorInt) {
        this.webviewInteractorInt = webviewInteractorInt;
        this.context = context;
    }

    public void getCompanyName() {
        webviewInteractorInt.getCompanyName(this);
    }


    public void responseFailed(String str) {
        context.showValidationToast(str);
    }

    public void responseSuccess(WebviewCompanyNamesRes checkForUpdateRes) {
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
