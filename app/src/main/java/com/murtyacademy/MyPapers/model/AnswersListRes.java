package com.murtyacademy.MyPapers.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * Created by srikanth on 12/12/2018.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "status",
        "statusCode",
        "message",
        "result"
})
public class AnswersListRes {

    @JsonProperty("status")
    private String status;
    @JsonProperty("statusCode")
    private Integer statusCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("result")
    private List<Result> result = null;

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("statusCode")
    public Integer getStatusCode() {
        return statusCode;
    }

    @JsonProperty("statusCode")
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("result")
    public List<Result> getResult() {
        return result;
    }

    @JsonProperty("result")
    public void setResult(List<Result> result) {
        this.result = result;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)

    public static class Result {

        @JsonProperty("ep_id")
        private String ep_id;
        @JsonProperty("student_id")
        private String student_id;
        @JsonProperty("exam_id")
        private String exam_id;
        @JsonProperty("trn_id")
        private String trn_id;
        @JsonProperty("question_name")
        private String question_name;
        @JsonProperty("submit_answer")
        private String submit_answer;
        @JsonProperty("answer")
        private String answer;
        @JsonProperty("date")
        private String date;

        public String getEp_id() {
            return this.ep_id;
        }

        public void setEp_id(String ep_id) {
            this.ep_id = ep_id;
        }

        public String getStudent_id() {
            return this.student_id;
        }

        public void setStudent_id(String student_id) {
            this.student_id = student_id;
        }

        public String getExam_id() {
            return this.exam_id;
        }

        public void setExam_id(String exam_id) {
            this.exam_id = exam_id;
        }

        public String getTrn_id() {
            return this.trn_id;
        }

        public void setTrn_id(String trn_id) {
            this.trn_id = trn_id;
        }

        public String getQuestion_name() {
            return this.question_name;
        }

        public void setQuestion_name(String question_name) {
            this.question_name = question_name;
        }

        public String getSubmit_answer() {
            return this.submit_answer;
        }

        public void setSubmit_answer(String submit_answer) {
            this.submit_answer = submit_answer;
        }

        public String getAnswer() {
            return this.answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getDate() {
            return this.date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
