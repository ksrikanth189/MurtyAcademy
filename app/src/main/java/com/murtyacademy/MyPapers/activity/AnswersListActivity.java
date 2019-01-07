package com.murtyacademy.MyPapers.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.murtyacademy.MyPapers.model.AnswersListReq;
import com.murtyacademy.MyPapers.model.AnswersListRes;
import com.murtyacademy.QuationsList.model.QuationsListReq;
import com.murtyacademy.QuationsList.model.QuationsListRes;
import com.murtyacademy.R;
import com.murtyacademy.sharedpref.SharedPrefHelper;
import com.murtyacademy.utils.PDFWebview;
import com.murtyacademy.utils.RestAPI;
import com.murtyacademy.utils.SuperCompatActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AnswersListActivity extends SuperCompatActivity {

    private AnswersListRes answersListRes;
    private Context context;
    private RestAPI restAPI;
    private ProgressDialog progressDialog;
    private FloatingActionButton register_fab;
    private RecyclerView recycle_view;
    private String examId,exam_name;
    private TextView quation_cat;
    private ArrayList<String> arrayList;

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
            examId = extras.getString("examId");
            exam_name = extras.getString("exam_name");
        }


        setToolBar(getApplicationContext(), exam_name, "yes");
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
        quation_cat.setText(exam_name);
        recycle_view = findViewById(R.id.recycle_view);
        recycle_view.setLayoutManager(new LinearLayoutManager(this));

       /*Service call*/

        if (checkInternet()) {
           QuationsListDetails(examId);
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


    private void QuationsListDetails(String examId) {

        AnswersListReq quationsListReq = new AnswersListReq();
        quationsListReq.setExam_id(examId);
        quationsListReq.setStudent_id(SharedPrefHelper.getLogin(context).getResult().get(0).getStudent_id());

        if (!progressDialog.isShowing())
        {
            progressDialog = new ProgressDialog(AnswersListActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please Wait Updating Data...");
            progressDialog.show();
        }

        try {
            final Call<AnswersListRes> adminListResCall = restAPI.getAnswersListResCall(quationsListReq);
            adminListResCall.enqueue(new Callback<AnswersListRes>() {
                @Override
                public void onResponse(Call<AnswersListRes> call, Response<AnswersListRes> response) {

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (response.isSuccessful()) {
                        answersListRes = response.body();
                        if(answersListRes.getResult() != null){

                            AdminsListAdapter summeryAdapter = new AdminsListAdapter(answersListRes.getResult());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            recycle_view.setLayoutManager(mLayoutManager);
                            recycle_view.setItemAnimator(new DefaultItemAnimator());
                            recycle_view.setAdapter(summeryAdapter);
                            if (answersListRes.getResult().size() > 0) {
                                recycle_view.setVisibility(View.VISIBLE);
                                // noSummaryData.setVisibility(View.GONE);
                            } else {
                                recycle_view.setVisibility(View.GONE);
                                //  noSummaryData.setVisibility(View.VISIBLE);
                            }
                        }
                  } else {
                        Toast toast = Toast.makeText(getApplicationContext(), answersListRes.getMessage(),Toast.LENGTH_SHORT);
                        toast.show();

                    }
                }

                @Override
                public void onFailure(Call<AnswersListRes> call, Throwable t) {
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

        private List<AnswersListRes.Result> mDataList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView quation_name_txt,submit_ans_txt,exact_ans_txt;

            public MyViewHolder(View view) {
                super(view);
                quation_name_txt = (TextView) view.findViewById(R.id.quation_name_txt);
                submit_ans_txt = (TextView) view.findViewById(R.id.submit_ans_txt);
                exact_ans_txt = (TextView) view.findViewById(R.id.exact_ans_txt);

            }
        }


        public AdminsListAdapter(List<AnswersListRes.Result> mDataList) {
            this.mDataList = mDataList;
        }

        @Override
        public AdminsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.answers_list_inflater, parent, false);

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

            if(mDataList.get(position).getSubmit_answer() == mDataList.get(position).getAnswer()){
                holder.quation_name_txt.setText(mDataList.get(position).getQuestion_name() != null ?  mDataList.get(position).getQuestion_name() : "--");
                holder.quation_name_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));

                arrayList = new ArrayList<String>();

                 arrayList.add(String.valueOf(mDataList.size()));
            }else {
                holder.quation_name_txt.setText(mDataList.get(position).getQuestion_name() != null ?  mDataList.get(position).getQuestion_name() : "--");
                holder.quation_name_txt.setTextColor(getResources().getColor(R.color.colorPrimary));

            }

            holder.submit_ans_txt.setText(mDataList.get(position).getSubmit_answer() != null ?
                    "Submit Answer :"+mDataList.get(position).getSubmit_answer() : "--");
            holder.exact_ans_txt.setText(mDataList.get(position).getAnswer() != null ? "Answer :"+  mDataList.get(position).getAnswer() : "--");





        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }



    }




}
