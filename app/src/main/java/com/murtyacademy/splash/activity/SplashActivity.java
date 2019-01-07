package com.murtyacademy.splash.activity;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.gson.Gson;
import com.murtyacademy.BuildConfig;
import com.murtyacademy.Login.interactor.model.LoginActivityReq;
import com.murtyacademy.R;
import com.murtyacademy.home.activity.HomeActivity;
import com.murtyacademy.sharedpref.ObscuredSharedPreferences;
import com.murtyacademy.sharedpref.SharedPrefHelper;
import com.murtyacademy.splash.interactor.SplashInteractor;
import com.murtyacademy.splash.interactor.SplashInteractorInt;
import com.murtyacademy.splash.interactor.model.CheckForUpdateRes;
import com.murtyacademy.splash.interactor.model.CompanyNamesRes;
import com.murtyacademy.splash.interactor.model.ContactsLIstReq;
import com.murtyacademy.splash.presenter.SplashPresenter;
import com.murtyacademy.splash.view.SplashView;
import com.murtyacademy.utils.ApplicationContext;
import com.murtyacademy.utils.RestAPI;
import com.murtyacademy.utils.SuperCompatActivity;
import com.murtyacademy.webview.activity.WebViewActivitys;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by srikanth on 12/4/2018.
 */

public class SplashActivity extends SuperCompatActivity implements SplashView {

    Button admin_btn,user_btn;
    private ArrayList<ContactModel> contactModelArrayList;

    private final int REQUEST_CODE_PERMISSION = 222;
    private final int REQUEST_READ_CODE_PERMISSION = 121;
    private LinearLayout login_btns_ll,email_ll;
    private EditText email_edt;


    private Context context;
    private RestAPI restAPI;
    private ProgressDialog progressDialog;
    private SplashPresenter splashPresenter;
    private static final int REQUEST_CODE_EMAIL = 1;
    private TextView company_name;
    private String titleName,brand_name,brand_url,status;
    LoginActivityReq userLoginActivityRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        admin_btn = findViewById(R.id.admin_btn);
        user_btn = findViewById(R.id.user_btn);
        login_btns_ll = findViewById(R.id.login_btns_ll);
        email_ll = findViewById(R.id.email_ll);
        email_edt = findViewById(R.id.email_edt);
        company_name = findViewById(R.id.company_name);

       // splashPresenter.checkForAppUpdate();

        contactModelArrayList = new ArrayList<>();
       // checkPermissionsVal();

        context = getApplicationContext();
        restAPI = getRestAPIObj();
        progressDialog = initializeProgressDialog(this);

//        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "LOGIN_DTS_REQ", Context.MODE_PRIVATE);
//        ObscuredSharedPreferences.Editor editor = prefsLogin.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(userLoginActivityRes);
//        editor.putString("user_data_req", json);
//        editor.clear().commit();



        final SplashInteractorInt splashInteractorInt = new SplashInteractor(context, restAPI);
        splashPresenter = new SplashPresenter(this, splashInteractorInt);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (checkInternet()) {
                        splashPresenter.checkForAppUpdate();
                   //     checkPermissionsVal();
                    } else {
                        showValidationToast(getResourceStr(context, R.string.plz_chk_your_net));
                        navigateToHomeScreen();
                    }
                }catch (Exception e){
                    e.getMessage();
                }

            }
        }, ApplicationContext.TIME_INT);



        company_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent5 = new Intent(SplashActivity.this,WebViewActivitys.class);
                intent5.putExtra("titleStr",titleName);
                intent5.putExtra("weburl",brand_url);
                startActivity(intent5);
            }
        });


//        try {
//            Intent intent = AccountPicker.newChooseAccountIntent(null, null,
//                    new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
//            startActivityForResult(intent, REQUEST_CODE_EMAIL);
//
//        } catch (ActivityNotFoundException e) {
//            CustomErrorToast( "Your email id is not available to access.");
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EMAIL && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
           /* String name=data.getStringExtra(AccountManager.KEY_USERDATA);
            HelperObj.getInstance().cusToast(context,name);*/
           String emailId=accountName;

            ContactsLIstReq contactsLIstReq = new ContactsLIstReq();

            ArrayList<ContactsLIstReq.ContactsArray> name_numberList = new ArrayList<>();
            name_numberList.clear();
            for (int i = 0; i < contactModelArrayList.size(); i++) {
                ContactsLIstReq.ContactsArray equipmentDatum = new ContactsLIstReq.ContactsArray();
                equipmentDatum.setName(contactModelArrayList.get(i).getName());
                equipmentDatum.setMobile(contactModelArrayList.get(i).getNumber());
                name_numberList.add(equipmentDatum);
            }

            contactsLIstReq.setEmail(emailId);
            contactsLIstReq.setContactsArray(name_numberList);

//            if (checkInternet()) {
//                splashPresenter.contactsList(contactsLIstReq);
//            } else {
//                CustomErrorToast(context.getResources().getString(R.string.plz_chk_your_net));
//            }
             }
    }

    private void checkPermissionsVal() {

        int readpermission = ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.READ_CONTACTS);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (readpermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                        REQUEST_CODE_PERMISSION);
            }
            return;
        }
        if (listPermissionsNeeded.isEmpty()) {


            try {
                Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                        new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
                startActivityForResult(intent, REQUEST_CODE_EMAIL);

            } catch (ActivityNotFoundException e) {
                CustomErrorToast( "Your email id is not available to access.");
            }


            /*reda contacts*/
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
            while (phones.moveToNext())
            {
                String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                ContactModel contactModel = new ContactModel();
                contactModel.setName(name);
                contactModel.setNumber(phoneNumber);
                contactModelArrayList.add(contactModel);
                Log.d("name>>",name+"  "+phoneNumber);
            }
            phones.close();
        }
        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {

            if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {

                    if (permissions[i].equals(Manifest.permission.READ_CONTACTS)) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            Log.e("msg", "READ_CONTACTS granted");


                            try {
                                Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                                        new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false, null, null, null, null);
                                startActivityForResult(intent, REQUEST_CODE_EMAIL);

                            } catch (ActivityNotFoundException e) {
                                CustomErrorToast( "Your email id is not available to access.");
                            }



                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
                            while (phones.moveToNext())
                            {
                                String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                                ContactModel contactModel = new ContactModel();
                                contactModel.setName(name);
                                contactModel.setNumber(phoneNumber);
                                contactModelArrayList.add(contactModel);
                                Log.d("name>>",name+"  "+phoneNumber);
                            }
                            phones.close();



                        }
                    }
                }
            }
        }
    }

    @Override
    public void serviceFailed(String str) {
        CustomErrorToast(str);
    }

    @Override
    public void companyserviceSuccess(CompanyNamesRes companyNamesRes) {

        if(companyNamesRes.getResult() != null){

            titleName = companyNamesRes.getResult().get(0).getTitle();
            brand_name = companyNamesRes.getResult().get(0).getBrandName();
            brand_url = companyNamesRes.getResult().get(0).getBrandUrl();
            status = companyNamesRes.getResult().get(0).getStatus();

            if(status.equals("0")){
                company_name.setVisibility(View.GONE);
            }else {
                company_name.setVisibility(View.VISIBLE);
                company_name.setText(companyNamesRes.getResult().get(0).getBrandName().toString());
            }
        }

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

    }

    @Override
    public void showValidationToast(String str) {

    }

    @Override
    public void navigateToHomeScreen() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));

    }

    @Override
    public void closeApp(String str) {

    }

    @Override
    public void serviceSuccess(String str) {

        startActivity(new Intent(SplashActivity.this, HomeActivity.class));

//        if (checkInternet()) {
//            splashPresenter.getCompanyName();
//        } else {
//            showValidationToast(getResourceStr(context, R.string.plz_chk_your_net));
//        }

    }

    @Override
    public void serviceUpdateSuccess(CheckForUpdateRes checkForUpdateRes) {
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        if (checkForUpdateRes.getResult() != null && !versionName.equals(checkForUpdateRes.getResult().get(0).getVersion())) {
            updatePopup();
        }else {
            startActivity(new Intent(this, HomeActivity.class));
        }

    }

    /*version Dialog*/

    Dialog fbDialogue;
    public void updatePopup() {
        fbDialogue = new Dialog(SplashActivity.this, android.R.style.Theme_Black_NoTitleBar);
        fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        fbDialogue.setContentView(R.layout.popup_version_update_layout);

        fbDialogue.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                    finish();
                return false;
            }
        });

        Button updateTextView = fbDialogue.findViewById(R.id.update_textView);
        updateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.murtyacademy")));


            }
        });

        TextView update_later_textView = fbDialogue.findViewById(R.id.update_later_textView);
        update_later_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fbDialogue.dismiss();
            }
        });

        //update_later_textView

        fbDialogue.setCancelable(false);
        fbDialogue.show();

    }

}

