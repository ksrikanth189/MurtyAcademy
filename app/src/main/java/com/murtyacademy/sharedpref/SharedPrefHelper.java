package com.murtyacademy.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.murtyacademy.Login.interactor.model.LoginActivityReq;
import com.murtyacademy.Login.interactor.model.LoginActivityRes;
import com.murtyacademy.splash.interactor.model.CheckForUpdateRes;


public class SharedPrefHelper {

    private static SharedPrefHelper objInstance;
    // Shared preferences file name
    public static final String PREFER_NAME = "Reg";
    int PRIVATE_MODE = 0;
    // Shared Preferences reference
    SharedPreferences pref;
    public static final String KEY_NotificationRegID="regid";
    // Editor reference for Shared preferences
    static SharedPreferences.Editor editor;

    // Context
    Context _context;



    public static void checkUpdate(Context context, CheckForUpdateRes checkForUpdateRes) {
        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "CheckUpdate", Context.MODE_PRIVATE);
        ObscuredSharedPreferences.Editor editor = prefsLogin.edit();
        Gson gson = new Gson();
        String json = gson.toJson(checkForUpdateRes);
        editor.putString("checkUpdate_data", json);
        editor.commit();
    }

    public static CheckForUpdateRes getCheckUpdate(Context context) {
        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "CheckUpdate", Context.MODE_PRIVATE);
        String user_data = prefsLogin.getString("checkUpdate_data", "");
        CheckForUpdateRes checkUpdate_dataRes = new Gson().fromJson(user_data, CheckForUpdateRes.class);
        return checkUpdate_dataRes;
    }


    // Constructor
    public SharedPrefHelper(Context context) {
        context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public static SharedPrefHelper getInstance(Context context) {
        if (objInstance == null) {
            objInstance = new SharedPrefHelper(context);
        }
        return objInstance;
    }


    public void SaveNotificationRegID(String regid){
        editor.putString(KEY_NotificationRegID,regid);
        editor.commit();
    }

    public String getNotificationRegID(){
        return pref.getString(KEY_NotificationRegID,"");
    }




    public static void saveLoginDts(Context context, LoginActivityRes userLoginActivityRes) {
        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "LOGIN_DTS", Context.MODE_PRIVATE);
        ObscuredSharedPreferences.Editor editor = prefsLogin.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userLoginActivityRes);
        editor.putString("user_data", json);
        editor.commit();
    }



    public static LoginActivityRes getLogin(Context context) {
        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "LOGIN_DTS", Context.MODE_PRIVATE);
        String user_data = prefsLogin.getString("user_data", "");
        LoginActivityRes loginActivityRes = new Gson().fromJson(user_data, LoginActivityRes.class);
        return loginActivityRes;
    }



    public static void clearLoginData(Context context) {
        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "LOGIN_DTS_REQ", Context.MODE_PRIVATE);
        ObscuredSharedPreferences.Editor editor = prefsLogin.edit();
        editor.putString("user_data_req", null);
        editor.commit();

    }

    public static void saveLoginDtsReq(Context context, LoginActivityReq userLoginActivityRes) {
        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "LOGIN_DTS_REQ", Context.MODE_PRIVATE);
        ObscuredSharedPreferences.Editor editor = prefsLogin.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userLoginActivityRes);
        editor.putString("user_data_req", json);
        editor.commit();
    }


    public static LoginActivityReq getUserDatWithOutLogin(Context context) {
        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "LOGIN_DTS_REQ", Context.MODE_PRIVATE);
        String user_data = prefsLogin.getString("user_data_req", "");
        LoginActivityReq loginActivityReq = new Gson().fromJson(user_data, LoginActivityReq.class);
        return loginActivityReq;
    }


}
