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
@JsonPropertyOrder({
        "status",
        "statusCode",
        "message",
        "result"
})
public class ExamPaperRes {

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

        @JsonProperty("trn_id")
        private String trnId;
        @JsonProperty("question_name")
        private String questionName;
        @JsonProperty("a")
        private String a;
        @JsonProperty("b")
        private String b;
        @JsonProperty("c")
        private String c;
        @JsonProperty("d")
        private String d;
        @JsonProperty("e")
        private String e;
        @JsonProperty("f")
        private String f;
        @JsonProperty("answer")
        private String answer;
        @JsonProperty("date")
        private String date;

        private String selectedVal;

        public String getSelectedVal() {
            return this.selectedVal;
        }

        public void setSelectedVal(String selectedVal) {
            this.selectedVal = selectedVal;
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

        @JsonProperty("a")
        public String getA() {
            return a;
        }

        @JsonProperty("a")
        public void setA(String a) {
            this.a = a;
        }

        @JsonProperty("b")
        public String getB() {
            return b;
        }

        @JsonProperty("b")
        public void setB(String b) {
            this.b = b;
        }

        @JsonProperty("c")
        public String getC() {
            return c;
        }

        @JsonProperty("c")
        public void setC(String c) {
            this.c = c;
        }

        @JsonProperty("d")
        public String getD() {
            return d;
        }

        @JsonProperty("d")
        public void setD(String d) {
            this.d = d;
        }

        @JsonProperty("e")
        public String getE() {
            return e;
        }

        @JsonProperty("e")
        public void setE(String e) {
            this.e = e;
        }

        @JsonProperty("f")
        public String getF() {
            return f;
        }

        @JsonProperty("f")
        public void setF(String f) {
            this.f = f;
        }

        @JsonProperty("answer")
        public String getAnswer() {
            return answer;
        }

        @JsonProperty("answer")
        public void setAnswer(String answer) {
            this.answer = answer;
        }

        @JsonProperty("date")
        public String getDate() {
            return date;
        }

        @JsonProperty("date")
        public void setDate(String date) {
            this.date = date;
        }

    }
}
