package com.murtyacademy.home.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * Created by srikanth on 12/8/2018.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
        "status",
        "statusCode",
        "message",
        "result"
})
public class AdminSlideRes {

    @JsonProperty("status")
    private String status;
    @JsonProperty("statusCode")
    private Integer statusCode;
    @JsonProperty("message")
    private String message;
    @JsonProperty("result")
    private List<AdminSlideRes.Result> result = null;

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
    public List<AdminSlideRes.Result> getResult() {
        return result;
    }

    @JsonProperty("result")
    public void setResult(List<AdminSlideRes.Result> result) {
        this.result = result;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonPropertyOrder({
            "name",
            "image",
            "date"
    })
    public static class Result {


        @JsonProperty("name")
        private String name;
        @JsonProperty("image")
        private String image;
        @JsonProperty("date")
        private String date;


        @JsonProperty("name")
        public String getName() {
            return name;
        }

        @JsonProperty("name")
        public void setName(String name) {
            this.name = name;
        }

        @JsonProperty("image")
        public String getImage() {
            return image;
        }

        @JsonProperty("image")
        public void setImage(String image) {
            this.image = image;
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