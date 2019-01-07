package com.murtyacademy.CategoryLists.model;

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
public class CategoryListRes {

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
    @JsonPropertyOrder({
            "trn_id",
            "name",
            "date"
    })
    public static class Result {

        @JsonProperty("trn_id")
        private String trnId;
        @JsonProperty("name")
        private String name;
        @JsonProperty("date")
        private String date;

        @JsonProperty("trn_id")
        public String getTrnId() {
            return trnId;
        }

        @JsonProperty("trn_id")
        public void setTrnId(String trnId) {
            this.trnId = trnId;
        }

        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
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
