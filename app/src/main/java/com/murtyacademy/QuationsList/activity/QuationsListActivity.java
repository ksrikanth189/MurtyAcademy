package com.murtyacademy.QuationsList.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.murtyacademy.QuationsList.model.QuationsListReq;
import com.murtyacademy.QuationsList.model.QuationsListRes;
import com.murtyacademy.R;
import com.murtyacademy.utils.PDFWebview;
import com.murtyacademy.utils.RestAPI;
import com.murtyacademy.utils.SuperCompatActivity;
import com.murtyacademy.webview.activity.WebViewActivitys;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class QuationsListActivity extends SuperCompatActivity {

    private QuationsListRes quationsListRes;
    private Context context;
    private RestAPI restAPI;
    private ProgressDialog progressDialog;
    private FloatingActionButton register_fab;
    private RecyclerView recycle_view;
    private String transId,cat_name;
    private TextView quation_cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setCustomTheme(getApplicationContext());
        setContentView(R.layout.activity_quations_lists);

        initializeUiElements();

    }


    private void initializeUiElements() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            transId = extras.getString("transId");
            cat_name = extras.getString("cat_name");
        }


        setToolBar(getApplicationContext(), cat_name, "yes");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        context = getApplicationContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.top_bar));
        }
        //========================================
        restAPI = getRestAPIObj();
        progressDialog = initializeProgressDialog(this);

        quation_cat = findViewById(R.id.quation_cat);
        quation_cat.setText(cat_name);
        recycle_view = findViewById(R.id.recycle_view);
        recycle_view.setLayoutManager(new LinearLayoutManager(this));

       /*Service call*/

        if (checkInternet()) {
           QuationsListDetails(transId);
        } else {
           Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.plz_chk_your_net),Toast.LENGTH_SHORT);
           toast.show();
        }
    }


    @Override
    public void onBackPressed() {
            super.onBackPressed();
            finish();
            }


    private void QuationsListDetails(String transId) {

        QuationsListReq quationsListReq = new QuationsListReq();
        quationsListReq.setCat_id(transId);

        if (!progressDialog.isShowing())
        {
            progressDialog = new ProgressDialog(QuationsListActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please Wait Updating Data...");
            progressDialog.show();
        }

        try {
            final Call<QuationsListRes> adminListResCall = restAPI.getQuestionsListResCall(quationsListReq);
            adminListResCall.enqueue(new Callback<QuationsListRes>() {
                @Override
                public void onResponse(Call<QuationsListRes> call, Response<QuationsListRes> response) {

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful()) {
                        quationsListRes = response.body();
                        if(quationsListRes.getResult() != null){

                            AdminsListAdapter summeryAdapter = new AdminsListAdapter(quationsListRes.getResult());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recycle_view.setLayoutManager(mLayoutManager);
                            recycle_view.setItemAnimator(new DefaultItemAnimator());
                            recycle_view.setAdapter(summeryAdapter);
                            if (quationsListRes.getResult().size() > 0) {
                                recycle_view.setVisibility(View.VISIBLE);
                                // noSummaryData.setVisibility(View.GONE);
                            } else {
                                recycle_view.setVisibility(View.GONE);
                                //  noSummaryData.setVisibility(View.VISIBLE);
                            }
                        }
                  } else {
                        Toast toast = Toast.makeText(getApplicationContext(), quationsListRes.getMessage(),Toast.LENGTH_SHORT);
                        toast.show();

                    }
                }

                @Override
                public void onFailure(Call<QuationsListRes> call, Throwable t) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.server_not_responding),Toast.LENGTH_SHORT);
                    toast.show();

                }
            });
        } catch (Exception e) {
//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }

            Log.v("Login Exception", " Exception Msg ==> " + e.getMessage());
            Toast toast = Toast.makeText(getApplicationContext(),getResources().getString(R.string.server_not_responding),Toast.LENGTH_SHORT);
            toast.show();
        }
    }



    public class AdminsListAdapter extends RecyclerView.Adapter<AdminsListAdapter.MyViewHolder> {

        private List<QuationsListRes.Result> mDataList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name_txt,date_txt;
            public ImageView pdf_image;
            public LinearLayout list_item_ll;

            public MyViewHolder(View view) {
                super(view);
                name_txt = (TextView) view.findViewById(R.id.name_txt);
                date_txt = (TextView) view.findViewById(R.id.date_txt);
                pdf_image = (ImageView) view.findViewById(R.id.pdf_image);
                list_item_ll = (LinearLayout) view.findViewById(R.id.list_item_ll);

            }
        }


        public AdminsListAdapter(List<QuationsListRes.Result> mDataList) {
            this.mDataList = mDataList;
        }

        @Override
        public AdminsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.quations_list_inflater, parent, false);

            return new AdminsListAdapter.MyViewHolder(itemView);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }


        @Override
        public void onBindViewHolder(AdminsListAdapter.MyViewHolder holder, final int position) {

            holder.name_txt.setText(mDataList.get(position).getName() != null ?  mDataList.get(position).getName() : "--");
            holder.date_txt.setText(mDataList.get(position).getDate() != null ?  mDataList.get(position).getDate() : "--");

            holder.list_item_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent5 = new Intent(QuationsListActivity.this,PDFWebview.class);
                    intent5.putExtra("titleStr","Question Paper");
                    intent5.putExtra("weburl",mDataList.get(position).getPdf());
                    startActivity(intent5);

                }
            });



            holder.pdf_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent5 = new Intent(QuationsListActivity.this,PDFWebview.class);
                    intent5.putExtra("titleStr","Question Paper");
                    intent5.putExtra("weburl",mDataList.get(position).getPdf());
                    startActivity(intent5);

                  }
            });

        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }


}
