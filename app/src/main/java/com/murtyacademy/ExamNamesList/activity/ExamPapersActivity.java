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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.murtyacademy.ExamNamesList.model.ExamNamesListRes;
import com.murtyacademy.ExamNamesList.model.ExamPaperReq;
import com.murtyacademy.ExamNamesList.model.ExamPaperRes;
import com.murtyacademy.ExamNamesList.model.ExamPaperUpdateReq;
import com.murtyacademy.ExamNamesList.model.ExamPaperUpdateRes;
import com.murtyacademy.QuationsList.activity.QuationsListActivity;
import com.murtyacademy.R;
import com.murtyacademy.home.activity.HomeActivity;
import com.murtyacademy.sharedpref.SharedPrefHelper;
import com.murtyacademy.utils.RestAPI;
import com.murtyacademy.utils.SuperCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExamPapersActivity extends SuperCompatActivity {

    private ExamPaperRes examNamesListRes;
    private Context context;
    private RestAPI restAPI;
    private ProgressDialog progressDialog;
    private FloatingActionButton register_fab;
    private RecyclerView admins_recycle;
    private String transId,cat_name,ans_a,ans_b,ans_c,ans_d,ans_e,ans_f;
    private Button  submit_button;
    private ProgressDialog pgDig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setCustomTheme(getApplicationContext());
        setContentView(R.layout.activity_exampapers);



        initializeUiElements();

    }


    private void initializeUiElements() {

        setToolBar(getApplicationContext(), "Exam Paper", "yes");
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


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            transId = extras.getString("transId");
            cat_name = extras.getString("cat_name");
        }



        submit_button = findViewById(R.id.submit_button);


        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Random Number = new Random();
               int Rnumber = Number.nextInt(100000);



                ExamPaperUpdateReq examPaperUpdateReq = new  ExamPaperUpdateReq();

                examPaperUpdateReq.setEpId(String.valueOf(Rnumber));

                ArrayList<ExamPaperUpdateReq.PostExam> arrayListStrDt = new ArrayList<>();
                arrayListStrDt.clear();

                StringBuffer stringBuffer1 = new StringBuffer();

                for(ExamPaperRes.Result result : examNamesListRes.getResult()){
                    stringBuffer1.append(result.getSelectedVal()+" ," + result.getAnswer());
                    ExamPaperUpdateReq.PostExam storageDatum = new ExamPaperUpdateReq.PostExam();
                    storageDatum.setAnswer(result.getAnswer());
                    storageDatum.setSubmitAnswer(result.getSelectedVal());
                    storageDatum.setExamId(transId);
                    storageDatum.setQuestionName(result.getQuestionName());
                    storageDatum.setStudentId(SharedPrefHelper.getLogin(context).getResult().get(0).getStudent_id());
                    storageDatum.setTrnId(result.getTrnId());
                    storageDatum.setSubmit_answer_details(result.getSelectedVal());
                    storageDatum.setAnswer_details();


                    arrayListStrDt.add(storageDatum);

                }

                examPaperUpdateReq.setPostExam(arrayListStrDt);


                getUpdateExam(examPaperUpdateReq);

                Gson gson = new Gson();
                String Json = gson.toJson(examPaperUpdateReq);  //see firstly above above



            }
        });

       /*Service call*/

        ExamPaperReq examPaperReq = new ExamPaperReq();
        examPaperReq.setExamId(transId);

        if (checkInternet()) {
           AdminDetailsList(examPaperReq);
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


    private void AdminDetailsList(ExamPaperReq examPaperReq) {

        if (!progressDialog.isShowing())
        {
            progressDialog = new ProgressDialog(ExamPapersActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Please Wait Updating Data...");
            progressDialog.show();
        }


        try {
            final Call<ExamPaperRes> adminListResCall = restAPI.getExamPaperResCall(examPaperReq);
            adminListResCall.enqueue(new Callback<ExamPaperRes>() {
                @Override
                public void onResponse(Call<ExamPaperRes> call, Response<ExamPaperRes> response) {

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
                public void onFailure(Call<ExamPaperRes> call, Throwable t) {
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

        private List<ExamPaperRes.Result> mDataList;
        private boolean click_pos;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView quation_name_txt,ans_a_txt,ans_b_txt,ans_c_txt,ans_d_txt,ans_e_txt,ans_f_txt;
            private LinearLayout a_ll,b_ll,c_ll,d_ll,e_ll,f_ll;


            public MyViewHolder(View view) {
                super(view);
                quation_name_txt = (TextView) view.findViewById(R.id.quation_name_txt);
                ans_a_txt = (TextView) view.findViewById(R.id.ans_a_txt);
                ans_b_txt = (TextView) view.findViewById(R.id.ans_b_txt);
                ans_c_txt = (TextView) view.findViewById(R.id.ans_c_txt);
                ans_d_txt = (TextView) view.findViewById(R.id.ans_d_txt);
                ans_e_txt = (TextView) view.findViewById(R.id.ans_e_txt);
                ans_f_txt = (TextView) view.findViewById(R.id.ans_f_txt);


                a_ll = (LinearLayout) view.findViewById(R.id.a_ll);
                b_ll = (LinearLayout) view.findViewById(R.id.b_ll);
                c_ll = (LinearLayout) view.findViewById(R.id.c_ll);
                d_ll = (LinearLayout) view.findViewById(R.id.d_ll);
                e_ll = (LinearLayout) view.findViewById(R.id.e_ll);
                f_ll = (LinearLayout) view.findViewById(R.id.f_ll);



            }
        }


        public AdminsListAdapter(List<ExamPaperRes.Result> mDataList) {
            this.mDataList = mDataList;
        }

        @Override
        public AdminsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.exampaper_list_inflater, parent, false);

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
        public void onBindViewHolder(final AdminsListAdapter.MyViewHolder holder, final int position) {

            holder.quation_name_txt.setText(mDataList.get(position).getQuestionName() != null ?  mDataList.get(position).getQuestionName() : "--");

            holder.ans_a_txt.setText(mDataList.get(position).getA() != null ?  mDataList.get(position).getA() : "--");
            holder.ans_b_txt.setText(mDataList.get(position).getB() != null ?  mDataList.get(position).getB() : "--");

            holder.ans_c_txt.setText(mDataList.get(position).getC() != null ?  mDataList.get(position).getC() : "--");
            holder.ans_d_txt.setText(mDataList.get(position).getD() != null ?  mDataList.get(position).getD() : "--");

            holder.ans_e_txt.setText(mDataList.get(position).getE() != null ?  mDataList.get(position).getE() : "--");
            holder.ans_f_txt.setText(mDataList.get(position).getF() != null ?  mDataList.get(position).getF() : "--");


            holder.ans_a_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ans_a = mDataList.get(position).getA();

                    mDataList.get(position).setSelectedVal("A");


                    holder.ans_b_txt.setClickable(false);
                    holder.ans_c_txt.setClickable(false);
                    holder.ans_d_txt.setClickable(false);
                    holder.ans_e_txt.setClickable(false);
                    holder.ans_f_txt.setClickable(false);

                    holder.ans_b_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_c_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_d_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_e_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_f_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));

                }
            });

            holder.ans_b_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ans_b = mDataList.get(position).getB();

                    mDataList.get(position).setSelectedVal("B");


                    holder.ans_a_txt.setClickable(false);
                    holder.ans_c_txt.setClickable(false);
                    holder.ans_d_txt.setClickable(false);
                    holder.ans_e_txt.setClickable(false);
                    holder.ans_f_txt.setClickable(false);


                    holder.ans_a_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_c_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_d_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_e_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_f_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));

                }
            });


            holder.ans_c_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ans_c = mDataList.get(position).getC();

                    mDataList.get(position).setSelectedVal("C");



                    holder.ans_a_txt.setClickable(false);
                    holder.ans_b_txt.setClickable(false);
                    holder.ans_d_txt.setClickable(false);
                    holder.ans_e_txt.setClickable(false);
                    holder.ans_f_txt.setClickable(false);


                    holder.ans_a_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_b_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_d_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_e_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_f_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));

                }
            });


            holder.ans_d_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ans_d = mDataList.get(position).getD();

                    mDataList.get(position).setSelectedVal("D");


                    holder.ans_a_txt.setClickable(false);
                    holder.ans_b_txt.setClickable(false);
                    holder.ans_c_txt.setClickable(false);
                    holder.ans_e_txt.setClickable(false);
                    holder.ans_f_txt.setClickable(false);



                    holder.ans_a_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_b_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_c_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_e_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_f_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));

                }
            });



            holder.ans_e_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ans_e = mDataList.get(position).getE();

                    mDataList.get(position).setSelectedVal("E");


                    holder.ans_a_txt.setClickable(false);
                    holder.ans_b_txt.setClickable(false);
                    holder.ans_c_txt.setClickable(false);
                    holder.ans_d_txt.setClickable(false);
                    holder.ans_f_txt.setClickable(false);


                    holder.ans_a_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_b_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_c_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_d_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_f_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));

                }
            });
            holder.ans_f_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ans_f = mDataList.get(position).getF();

                    mDataList.get(position).setSelectedVal("F");


                    holder.ans_a_txt.setClickable(false);
                    holder.ans_b_txt.setClickable(false);
                    holder.ans_c_txt.setClickable(false);
                    holder.ans_d_txt.setClickable(false);
                    holder.ans_e_txt.setClickable(false);


                    holder.ans_a_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_b_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_c_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_d_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));
                    holder.ans_e_txt.setTextColor(getResources().getColor(R.color.btn_sel_txt_clr));

                }
            });





        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }
    }



    public void getUpdateExam(ExamPaperUpdateReq examPaperUpdateReq) {



        try {
            pgDig = new ProgressDialog(this);
            pgDig.setCancelable(true);
            pgDig.setMessage("Please wait... ");
            pgDig.show();

            Call<ExamPaperUpdateRes> damageHistoryResPayLoadCall = restAPI.getExamUpdateCall(examPaperUpdateReq);
            damageHistoryResPayLoadCall.enqueue(new Callback<ExamPaperUpdateRes>() {
                @Override
                public void onResponse(Call<ExamPaperUpdateRes> call, Response<ExamPaperUpdateRes> response) {
                    if (response.isSuccessful()) {

                        ExamPaperUpdateRes responsePayLoad = response.body();

                        if(responsePayLoad.getStatus() ==200){

                             Toast.makeText(context,responsePayLoad.getMessage(),Toast.LENGTH_SHORT).show();

                             startActivity(new Intent(ExamPapersActivity.this, HomeActivity.class));
                        }
                    }

                    if (pgDig != null)
                        pgDig.dismiss();
                }

                @Override
                public void onFailure(Call<ExamPaperUpdateRes> call, Throwable t) {
                    Toast.makeText(context,"Failure",Toast.LENGTH_SHORT).show();

                    if (pgDig != null)
                        pgDig.dismiss();
                }
            });

        }  catch (Exception e) {


        } finally {

        }
    }



}
