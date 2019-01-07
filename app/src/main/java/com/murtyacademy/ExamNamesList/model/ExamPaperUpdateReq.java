package com.murtyacademy.ExamNamesList.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * Created by srikanth on 1/4/2019.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "ep_id",
        "postExam"
})
public class ExamPaperUpdateReq {

    @JsonProperty("ep_id")
    private String epId;
    @JsonProperty("postExam")
    private List<PostExam> postExam = null;

    @JsonProperty("ep_id")
    public String getEpId() {
        return epId;
    }

    @JsonProperty("ep_id")
    public void setEpId(String epId) {
        this.epId = epId;
    }

    @JsonProperty("postExam")
    public List<PostExam> getPostExam() {
        return postExam;
    }

    @JsonProperty("postExam")
    public void setPostExam(List<PostExam> postExam) {
        this.postExam = postExam;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PostExam {

        @JsonProperty("student_id")
        private String studentId;
        @JsonProperty("exam_id")
        private String examId;
        @JsonProperty("trn_id")
        private String trnId;
        @JsonProperty("question_name")
        private String questionName;
        @JsonProperty("submit_answer")
        private String submitAnswer;
        @JsonProperty("submit_answer_details")
        private String submit_answer_details;
        @JsonProperty("answer")
        private String answer;
        @JsonProperty("answer_details")
        private String answer_details;

        @JsonProperty("student_id")
        public String getStudentId() {
            return studentId;
        }

        @JsonProperty("student_id")
        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        @JsonProperty("exam_id")
        public String getExamId() {
            return examId;
        }

        @JsonProperty("exam_id")
        public void setExamId(String examId) {
            this.examId = examId;
        }

        @JsonProperty("trn_id")
        public String getTrnId() {
            return trnId;
        }

        @JsonProperty("trn_id")
        public void setTrnId(String trnId) {
            this.trnId = trnId;
        }

        @JsonProperty("question_name")
        public String getQuestionName() {
            return questionName;
        }

        @JsonProperty("question_name")
        public void setQuestionName(String questionName) {
            this.questionName = questionName;
        }

        @JsonProperty("submit_answer")
        public String getSubmitAnswer() {
            return submitAnswer;
        }

        @JsonProperty("submit_answer")
        public void setSubmitAnswer(String submitAnswer) {
            this.submitAnswer = submitAnswer;
        }

        @JsonProperty("answer")
        public String getAnswer() {
            return answer;
        }

        @JsonProperty("answer")
        public void setAnswer(String answer) {
            this.answer = answer;
        }


        public String getSubmit_answer_details() {
            return this.submit_answer_details;
        }

        public void setSubmit_answer_details(String submit_answer_details) {
            this.submit_answer_details = submit_answer_details;
        }

        public String getAnswer_details() {
            return this.answer_details;
        }

        public void setAnswer_details(String answer_details) {
            this.answer_details = answer_details;
        }
    }
}
