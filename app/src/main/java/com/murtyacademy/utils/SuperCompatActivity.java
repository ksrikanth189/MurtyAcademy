package com.murtyacademy.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.murtyacademy.R;
import com.murtyacademy.sharedpref.ObscuredSharedPreferences;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public abstract class SuperCompatActivity extends AppCompatActivity {

    public Context context, myContext;
    public String pushToken;
    private Toolbar toolbar;
    private ProgressDialog pg_dialog;
    private ConstraintLayout mainLayout;
    public LinearLayout linearmLeftDrawer;
    public DrawerLayout mDrawerLayout;
    private ImageView sync_data, home_img;
    private FrameLayout sync_frame;

    private final Pattern pattern = Pattern
            .compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
                    + "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        context = getApplicationContext();
        // pushToken = FirebaseInstanceId.getInstance().getToken();
        Log.v("Push Token", "" + pushToken);

    }


    public void setCustomTheme(Context context) {
        setTheme(R.style.AppTheme);
    }


    @Override
    protected void onResume() {
        super.onResume();
        closedrawer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    public void setToolBar(final Context myContext, String title, String cartIcon) {

        this.myContext = myContext;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sync_frame = (FrameLayout) toolbar.findViewById(R.id.sync_frame);
        sync_data = (ImageView) toolbar.findViewById(R.id.sync_data);
        home_img = (ImageView) toolbar.findViewById(R.id.home_img);

        TextView textView = toolbar.findViewById(R.id.title_txt);
        textView.setText("" + title);

//        home_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new
//                        Intent(getApplicationContext(), AdminLoginActivity.class).
//                        setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
//                finish();
//            }
//        });

    }


    public boolean checkInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public RestAPI getRestAPIObj() {

        ObscuredSharedPreferences prefsLogin = ObscuredSharedPreferences.getPrefs(context, "URL_DTS", Context.MODE_PRIVATE);
        String url = prefsLogin.getString("savedUrl", "");
        if (url.length() > 0) {
            ApplicationContext.BASE_URL = url;
        }

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApplicationContext.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create()).build();
        RestAPI restAPI = retrofit.create(RestAPI.class);
        return restAPI;
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public float pxFromDp(float dp, Context mContext) {
        return dp * mContext.getResources().getDisplayMetrics().density;
    }

    public ProgressDialog initializeProgressDialog(Context context) {
        pg_dialog = new ProgressDialog(context);
        pg_dialog.setMessage("Please wait.");
        pg_dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pg_dialog.setIndeterminate(true);
        pg_dialog.setProgress(0);
        pg_dialog.setCancelable(false);
        return pg_dialog;
    }

    public boolean chkEmailPattern(String email) {
        Matcher m = pattern.matcher(email);
        return m.matches();
    }

    public String getVersionCode(Activity activity) {

        String appVersion = "";
        PackageManager manager = activity.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(activity.getPackageName(), 0);
            appVersion = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return appVersion;
    }

    public String getResourceStr(Context context, int id) {
        String returnVal = null;
        try {
            returnVal = getResources().getString(id);
        } catch (Exception e) {
            returnVal = context.getResources().getString(id);
        }
        return returnVal;
    }

    public void closedrawer() {
        try {
            if (mDrawerLayout != null) {
                if (mDrawerLayout.isDrawerOpen(linearmLeftDrawer))
                    mDrawerLayout.closeDrawer(linearmLeftDrawer);
            }
        } catch (Exception e) {
            Log.d("tag", "slider exce-->" + e.getMessage());
        }
    }

    public String getMacAddress() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    String hex = Integer.toHexString(b & 0xFF);
                    if (hex.length() == 1)
                        hex = "0".concat(hex);
                    res1.append(hex.concat(":"));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "";
    }


    public void smoothScrollToPosition(final AbsListView view, final int position) {
        View child = getChildAtPosition(view, position);
        // There's no need to scroll if child is already at top or view is already scrolled to its end
        if ((child != null) && ((child.getTop() == 0) || ((child.getTop() > 0) && !view.canScrollVertically(1)))) {
            return;
        }

        view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    view.setOnScrollListener(null);

                    // Fix for scrolling bug
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            view.setSelection(position);
                        }
                    });
                }
            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
                                 final int totalItemCount) {
            }
        });

        // Perform scrolling to position
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                view.smoothScrollToPositionFromTop(position, 0);
            }
        });
    }

    public static View getChildAtPosition(final AdapterView view, final int position) {
        final int index = position - view.getFirstVisiblePosition();
        if ((index >= 0) && (index < view.getChildCount())) {
            return view.getChildAt(index);
        } else {
            return null;
        }
    }


    public void CustomSuccessToast(String msg) {

        Toast newToast = Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_SHORT);
        newToast.setGravity(Gravity.CENTER, 0, 0);
        LayoutInflater inflater = getLayoutInflater();
        View tastyToast = inflater.inflate(R.layout.tasty_toast, (ViewGroup) findViewById(R.id.tastyLayout));
        TextView toastText = (TextView) tastyToast.findViewById(R.id.textView1);
        toastText.setText(msg);
        newToast.setView(tastyToast);
        newToast.show();
    }
    public void CustomErrorToast(String msg) {

        Toast newToast = Toast.makeText(getApplicationContext(), R.string.app_name, Toast.LENGTH_SHORT);
        newToast.setGravity(Gravity.CENTER, 0, 0);
        LayoutInflater inflater = getLayoutInflater();
        View tastyToast = inflater.inflate(R.layout.tasty_toast, (ViewGroup) findViewById(R.id.tastyLayout));
        TextView toastText = (TextView) tastyToast.findViewById(R.id.textView1);
        toastText.setText(msg);
        newToast.setView(tastyToast);
        newToast.show();
    }


}
