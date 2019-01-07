package com.murtyacademy.home.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.murtyacademy.Login.activity.LoginActivity;
import com.murtyacademy.Login.interactor.model.LoginActivityReq;
import com.murtyacademy.MyPapers.activity.MyPaperListActivity;
import com.murtyacademy.R;
import com.murtyacademy.CategoryLists.activity.CategoryListActivity;
import com.murtyacademy.Testimonials.activity.TestimonialsListActivity;
import com.murtyacademy.home.interactor.HomeInteractor;
import com.murtyacademy.home.interactor.HomeInteractorInt;
import com.murtyacademy.home.model.AdminSlideRes;
import com.murtyacademy.home.model.HomeCompanyNamesRes;
import com.murtyacademy.home.model.SlidingImage_Adapter;
import com.murtyacademy.home.presenter.HomePresenter;
import com.murtyacademy.home.view.HomeView;
import com.murtyacademy.sharedpref.SharedPrefHelper;
import com.murtyacademy.webview.activity.WebViewActivitys;
import com.murtyacademy.utils.RestAPI;
import com.murtyacademy.utils.SuperCompatActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends SuperCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,HomeView {

    private AdminSlideRes adminSlideRes;
    private Context context;
    private RestAPI restAPI;
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SlidingImage_Adapter sliderPagerAdapter;
    ArrayList<AdminSlideRes.Result> slider_image_list = new ArrayList<AdminSlideRes.Result>();
    ArrayList<String> slider_image_array = new ArrayList<>();
    private TextView[] dots;
    int page_position = 0;
    private ProgressDialog progressDialog;
    private TextView name_txt,mobile_txt,username_txt,usermobile_txt;
    private View navHeader;
    private WebView homedata_webview;
    private HomePresenter homePresenter;
    private String titleName,brand_name,brand_url,status;
    private TextView company_name;
    private LoginActivityReq loginActivityReq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeUiElements();
    }


    private void initializeUiElements() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        setToolBar(getApplicationContext(), "Welcome, Murty Academy", "yes");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navHeader = navigationView.getHeaderView(0);
        name_txt = (TextView) navHeader.findViewById(R.id.name_txt);
        mobile_txt = (TextView) navHeader.findViewById(R.id.mobile_txt);


        context = getApplicationContext();
        restAPI = getRestAPIObj();


        final HomeInteractorInt splashInteractorInt = new HomeInteractor(context, restAPI);
        homePresenter = new HomePresenter(this, splashInteractorInt);


        company_name = findViewById(R.id.company_name);


        vp_slider = (ViewPager) findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);

        homedata_webview = (WebView) findViewById(R.id.homedata_webview);


        homedata_webview.setWebViewClient(new MyBrowser());
        homedata_webview.getSettings().setLoadsImagesAutomatically(true);
        homedata_webview.getSettings().setJavaScriptEnabled(true);
        homedata_webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        homedata_webview.loadUrl("http://murtyacademy.com/mac/home.php");


        if (checkInternet()) {
            homePresenter.getCompanyName();
        } else {
            showValidationToast(getResourceStr(context, R.string.plz_chk_your_net));
        }



        company_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent5 = new Intent(HomeActivity.this,WebViewActivitys.class);
                intent5.putExtra("titleStr",titleName);
                intent5.putExtra("weburl",brand_url);
                startActivity(intent5);
            }
        });






//        username_txt.setText(SharedPrefHelper.getAdminLogin(context).getResult().get(0).getName().toString().trim());
//        usermobile_txt.setText(SharedPrefHelper.getAdminLogin(context).getResult().get(0).getMobile().toString().trim());
//
//
//        if(SharedPrefHelper.getAdminLogin(context).getResult().get(0).getRole().equals("2")){
//            navigationView.getMenu().findItem(R.id.nav_source).setVisible(false);
//            navigationView.getMenu().findItem(R.id.nav_admins).setVisible(false);
//        }else {
//            navigationView.getMenu().findItem(R.id.nav_source).setVisible(true);
//            navigationView.getMenu().findItem(R.id.nav_admins).setVisible(true);
//        }


      /*Service call*/
        HomeSlidingDetailsList();

        addBottomDots(0);

        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == slider_image_array.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                vp_slider.setCurrentItem(page_position, true);
            }
        };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 100, 5000);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(HomeActivity.this, HomeActivity.class));
        }else if (id == R.id.nav_about_us) {
            Intent intent5 = new Intent(HomeActivity.this,WebViewActivitys.class);
            intent5.putExtra("titleStr","About Us");
            intent5.putExtra("weburl","http://murtyacademy.com/mac/about.php");
            startActivity(intent5);
        }else if (id == R.id.nav_contact_us) {
            Intent intent5 = new Intent(HomeActivity.this,WebViewActivitys.class);
            intent5.putExtra("titleStr","Contact Us");
            intent5.putExtra("weburl","http://murtyacademy.com/mac/contact.php");
            startActivity(intent5);
        }else if (id == R.id.nav_share_link) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.murtyacademy");
            startActivity(Intent.createChooser(shareIntent, "Share link using"));
        }else if (id == R.id.nav_termscondition) {
            Intent intent5 = new Intent(HomeActivity.this,WebViewActivitys.class);
            intent5.putExtra("titleStr","Terms and Conditions");
            intent5.putExtra("weburl","http://murtyacademy.com/mac/terms.php");
            startActivity(intent5);
        }else if (id == R.id.nav_category) {
            startActivity(new Intent(HomeActivity.this, CategoryListActivity.class));
        }else if (id == R.id.nav_testimonals) {
            startActivity(new Intent(HomeActivity.this, TestimonialsListActivity.class));
        }else if (id == R.id.nav_my_papers) {
            startActivity(new Intent(HomeActivity.this, MyPaperListActivity.class));
        }else if (id == R.id.nav_online_exam) {
            if(SharedPrefHelper.getUserDatWithOutLogin(context) != null){
                Intent intent5 = new Intent(HomeActivity.this,LoginActivity.class);
                intent5.putExtra("mobile_num",SharedPrefHelper.getUserDatWithOutLogin(context).getMobile());
                intent5.putExtra("password_str",SharedPrefHelper.getUserDatWithOutLogin(context).getPassword());
                startActivity(intent5);

                name_txt.setText(SharedPrefHelper.getLogin(context).getResult().get(0).getName().toString().trim());
                mobile_txt.setText(SharedPrefHelper.getLogin(context).getResult().get(0).getMobile().toString().trim());



            }else {
                Intent intent5 = new Intent(HomeActivity.this,LoginActivity.class);
                intent5.putExtra("mobile_num","");
                intent5.putExtra("password_str","");
                startActivity(intent5);
            }

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void serviceFailed(String str) {

    }

    @Override
    public void companyserviceSuccess(HomeCompanyNamesRes companyNamesRes) {
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

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showSuccessToast(String str) {

    }

    @Override
    public void showValidationToast(String str) {

    }

    @Override
    public void navigateToHomeScreen() {

    }

    @Override
    public void closeApp(String str) {

    }

    @Override
    public void serviceSuccess(String str) {

    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }



    /*service getting*/
    private void HomeSlidingDetailsList() {

        try {
            final Call<AdminSlideRes> adminSlideResCall = restAPI.getAdminSlideResCall();
            adminSlideResCall.enqueue(new Callback<AdminSlideRes>() {
                @Override
                public void onResponse(Call<AdminSlideRes> call, Response<AdminSlideRes> response) {
                    if (response.isSuccessful()) {
                        adminSlideRes = response.body();

                        for (int i = 0; i< adminSlideRes.getResult().size();i++) {

                            slider_image_array.add(adminSlideRes.getResult().get(i).getImage());

                        }

                        slidingMethod();


                    } else {

                        Toast toast = Toast.makeText(getApplicationContext(),adminSlideRes.getMessage(),Toast.LENGTH_SHORT);
                        toast.show();

                    }
                }

                @Override
                public void onFailure(Call<AdminSlideRes> call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.server_not_responding),Toast.LENGTH_SHORT);
                    toast.show();

                }
            });
        } catch (Exception e) {
            Log.v("Login Exception", " Exception Msg ==> " + e.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.server_not_responding),Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void slidingMethod() {

       if(slider_image_array != null){

            sliderPagerAdapter = new SlidingImage_Adapter(HomeActivity.this,slider_image_array);
            vp_slider.setAdapter(sliderPagerAdapter);

            vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    addBottomDots(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }

    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_array.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#000000"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#FFFFFF"));
    }
}
