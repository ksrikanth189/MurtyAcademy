package com.murtyacademy.Login.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import com.murtyacademy.ExamNamesList.activity.ExamPaperNamesListActivity;
import com.murtyacademy.Login.interactor.LoginInteractor;
import com.murtyacademy.Login.interactor.LoginInteractorInt;
import com.murtyacademy.Login.interactor.model.LoginActivityReq;
import com.murtyacademy.Login.interactor.model.LoginActivityRes;
import com.murtyacademy.Login.presenter.LoginPresenter;
import com.murtyacademy.Login.view.LoginView;
import com.murtyacademy.R;
import com.murtyacademy.utils.RestAPI;
import com.murtyacademy.utils.SuperCompatActivity;


public class LoginActivity extends SuperCompatActivity implements LoginView {

    private Context context;
    private RestAPI restAPI;
    private ProgressDialog progressDialog;
    private LoginPresenter userLoginPresenter;
    private LoginActivityReq loginActivityReq;

    private EditText userId, password;
    private final int REQUEST_CODE_PERMISSION = 222;
    private final int REQUEST_READ_CODE_PERMISSION = 121;

    String mobile_num,password_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setCustomTheme(getApplicationContext());
        setContentView(R.layout.activity_login);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mobile_num = extras.getString("mobile_num");
            password_str = extras.getString("password_str");
        }


        initializeUiElements();

    }

    @Override
    public void serviceSuccess(LoginActivityRes userLoginActivityRes) {
        if (userLoginActivityRes.getResult() != null) {
            userLoginPresenter.saveLogin(userLoginActivityRes);

            Toast toast = Toast.makeText(getApplicationContext(), userLoginActivityRes.getMessage(),Toast.LENGTH_SHORT);
            toast.show();

        } else {
            serviceFailed(context.getResources().getString(R.string.server_not_responding));

        }
    }


    @Override
    public void serviceFailed(String str) {
        Toast toast = Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT);
        toast.show();

    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showSuccessToast(String str) {
        Toast.makeText(context, "" + str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeApp(String str) {
        Toast toast = Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT);
        toast.show();

    }

    @Override
    public void showValidationToast(String str) {
        Toast toast = Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT);
        toast.show();

    }

    @Override
    public void navigateToHomeScreen() {

        startActivity(new Intent(this,ExamPaperNamesListActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }


    public void signIn(View view) {

        if (userId.getText().toString().length() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(),getResources().getText(R.string.pls_enter_register_mobile),Toast.LENGTH_SHORT);
            toast.show();


        } else if (password.getText().toString().length() == 0) {
            Toast toast = Toast.makeText(getApplicationContext(),getResources().getText(R.string.pls_enter_password),Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
               if (checkInternet()) {
                    userLoginPresenter.signInBtnClicked(userId.getText().toString(), password.getText().toString());

               } else {
                    showValidationToast(context.getResources().getString(R.string.plz_chk_your_net));

                    Toast toast = Toast.makeText(getApplicationContext(),getResources().getText(R.string.plz_chk_your_net),Toast.LENGTH_SHORT);
                    toast.show();
                }
             }
    }


    @Override
    protected void onResume() {

        super.onResume();
        restAPI = getRestAPIObj();
        LoginInteractorInt userLoginInteractorInt = new LoginInteractor(context, restAPI);
        userLoginPresenter = new LoginPresenter(this, userLoginInteractorInt);
    }

    private void initializeUiElements() {

        context = this;
        restAPI = getRestAPIObj();
        progressDialog = initializeProgressDialog(this);

        userId = findViewById(R.id.user_id);
        password = findViewById(R.id.password);

        if(mobile_num != null && password_str != null){

            userId.setText(mobile_num);
            password.setText(password_str);

          //  navigateToHomeScreen();

        }else {
            userId.setText("");
            password.setText("");

        }
        LoginInteractorInt userLoginInteractorInt = new LoginInteractor(context, restAPI);
        userLoginPresenter = new LoginPresenter(this, userLoginInteractorInt);

    }




}
