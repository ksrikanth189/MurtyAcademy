package com.murtyacademy.ExamNamesList.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.murtyacademy.ExamNamesList.model.ExamNamesListRes;
import com.murtyacademy.R;
import com.murtyacademy.utils.RestAPI;
import com.murtyacademy.utils.SuperCompatActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExamPaperNamesListActivity extends SuperCompatActivity {

    private ExamNamesListRes examNamesListRes;
    private Context context;
    private RestAPI restAPI;
    private ProgressDialog progressDialog;
    private FloatingActionButton register_fab;
    private RecyclerView admins_recycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setCustomTheme(getApplicationContext());
        setContentView(R.layout.activity_category_lists);

        initializeUiElements();

    }


    private void initializeUiElements() {

        setToolBar(getApplicationContext(), "Exam Paper Category", "yes");
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

        admins_recycle = findViewById(R.id.admins_recycle);
        admins_recycle.setLayoutManager(new LinearLayoutManager(this));

       /*Service call*/

       // ExamNamesListReq examNamesListReq = new ExamNamesListReq();

      //  examNamesListReq.setStudent_id(SharedPrefHelper.getLogin(context).getResult().get(0).getStudent_id());

        if (checkInternet()) {
        //   ExamsNamesList(examNamesListReq);
           ExamsNamesList();
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


   // private void ExamsNamesList(ExamNamesListReq examNamesListReq) {
    private void ExamsNamesList() {

        if (!progressDialog.isShowing())
        {
            progressDialog = new ProgressDialog(ExamPaperNamesListActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please Wait Updating Data...");
            progressDialog.show();
        }


        try {
           // final Call<ExamNamesListRes> adminListResCall = restAPI.getExamPaperCategoryListResCall(examNamesListReq);
            final Call<ExamNamesListRes> adminListResCall = restAPI.getExamPaperCategoryListResCall();
            adminListResCall.enqueue(new Callback<ExamNamesListRes>() {
                @Override
                public void onResponse(Call<ExamNamesListRes> call, Response<ExamNamesListRes> response) {

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful()) {
                        examNamesListRes = response.body();
                        if(examNamesListRes.getResult() != null){

                            AdminsListAdapter summeryAdapter = new AdminsListAdapter(examNamesListRes.getResult());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            admins_recycle.setLayoutManager(mLayoutManager);
                            admins_recycle.setItemAnimator(new DefaultItemAnimator());
                            admins_recycle.setAdapter(summeryAdapter);
                            if (examNamesListRes.getResult().size() > 0) {
                                admins_recycle.setVisibility(View.VISIBLE);
                                // noSummaryData.setVisibility(View.GONE);
                            } else {
                                admins_recycle.setVisibility(View.GONE);
                                //  noSummaryData.setVisibility(View.VISIBLE);
                            }
                        }
                  } else {
                        Toast toast = Toast.makeText(getApplicationContext(), examNamesListRes.getMessage(),Toast.LENGTH_SHORT);
                        toast.show();

                    }
                }

                @Override
                public void onFailure(Call<ExamNamesListRes> call, Throwable t) {
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

        private List<ExamNamesListRes.Result> mDataList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name_txt;

            public MyViewHolder(View view) {
                super(view);
                name_txt = (TextView) view.findViewById(R.id.name_txt);

            }
        }


        public AdminsListAdapter(List<ExamNamesListRes.Result> mDataList) {
            this.mDataList = mDataList;
        }

        @Override
        public AdminsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_list_inflater, parent, false);

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

            holder.name_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ExamPaperNamesListActivity.this,ExamPapersActivity.class);
                    intent.putExtra("transId",mDataList.get(position).getTrnId());
                    intent.putExtra("cat_name",mDataList.get(position).getName());
                    startActivity(intent);

                }
            });

        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }


}
