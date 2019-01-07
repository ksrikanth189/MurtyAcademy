package com.murtyacademy.utils;

import com.murtyacademy.CategoryLists.model.CategoryListRes;
import com.murtyacademy.ExamNamesList.model.ExamNamesListReq;
import com.murtyacademy.ExamNamesList.model.ExamNamesListRes;
import com.murtyacademy.ExamNamesList.model.ExamPaperReq;
import com.murtyacademy.ExamNamesList.model.ExamPaperRes;
import com.murtyacademy.ExamNamesList.model.ExamPaperUpdateReq;
import com.murtyacademy.ExamNamesList.model.ExamPaperUpdateRes;
import com.murtyacademy.Login.interactor.model.LoginActivityReq;
import com.murtyacademy.Login.interactor.model.LoginActivityRes;
import com.murtyacademy.MyPapers.model.AnswersListReq;
import com.murtyacademy.MyPapers.model.AnswersListRes;
import com.murtyacademy.MyPapers.model.MyPaperListReq;
import com.murtyacademy.MyPapers.model.MyPaperListRes;
import com.murtyacademy.QuationsList.model.QuationsListReq;
import com.murtyacademy.QuationsList.model.QuationsListRes;
import com.murtyacademy.Testimonials.model.TestimonialsListRes;
import com.murtyacademy.home.model.AdminSlideRes;
import com.murtyacademy.home.model.HomeCompanyNamesRes;
import com.murtyacademy.splash.interactor.model.CheckForUpdateRes;
import com.murtyacademy.splash.interactor.model.CompanyNamesRes;
import com.murtyacademy.splash.interactor.model.ContactsLIstReq;
import com.murtyacademy.splash.interactor.model.ContactsLIstRes;
import com.murtyacademy.webview.interactor.model.WebviewCompanyNamesRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestAPI {

    @GET(ApplicationContext.RELATIVE_PATH + ApplicationContext.CheckForAppUpdate)
    Call<CheckForUpdateRes> getCheckForUpdateResCall();

    @GET(ApplicationContext.RELATIVE_PATH + ApplicationContext.brand)
    Call<CompanyNamesRes> getCompanyNameResCall();

    @POST(ApplicationContext.ContactsList)
    Call<ContactsLIstRes> getContactsListResCall(@Body ContactsLIstReq contactsLIstReq);

    @GET(ApplicationContext.RELATIVE_PATH + ApplicationContext.sliide)
    Call<AdminSlideRes> getAdminSlideResCall();

    @GET(ApplicationContext.RELATIVE_PATH + ApplicationContext.brand)
    Call<HomeCompanyNamesRes> getHomeBrandNameResCall();

    @GET(ApplicationContext.RELATIVE_PATH + ApplicationContext.brand)
    Call<WebviewCompanyNamesRes> getWebviewCompanyNameResCall();

    @GET(ApplicationContext.RELATIVE_PATH + ApplicationContext.category)
    Call<CategoryListRes> getCategoryListResCall();

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.questions)
    Call<QuationsListRes> getQuestionsListResCall(@Body QuationsListReq quationsListReq);

    @GET(ApplicationContext.RELATIVE_PATH + ApplicationContext.testimonials)
    Call<TestimonialsListRes> getTestimotalsListResCall();

    @GET(ApplicationContext.RELATIVE_PATH + ApplicationContext.exam_name)
    Call<ExamNamesListRes> getExamPaperCategoryListResCall();

//    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.exam_name)
//    Call<ExamNamesListRes> getExamPaperCategoryListResCall(@Body ExamNamesListReq examNamesListReq);



    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.student_login)
    Call<LoginActivityRes> getLoginResCall(@Body LoginActivityReq adminLoginReq);


    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.exam_paper)
    Call<ExamPaperRes> getExamPaperResCall(@Body ExamPaperReq examPaperReq);

    @POST(ApplicationContext.exam_paperUpdate)
    Call<ExamPaperUpdateRes> getExamUpdateCall(@Body ExamPaperUpdateReq examPaperUpdateReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.my_exams)
    Call<MyPaperListRes> getMyExamsResCall(@Body MyPaperListReq myPaperListReq);

    @POST(ApplicationContext.RELATIVE_PATH + ApplicationContext.marks)
    Call<AnswersListRes> getAnswersListResCall(@Body AnswersListReq answersListReq);


}
