package com.murtyacademy.ExamNamesList.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by srikanth on 1/4/2019.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "exam_id"
})
public class ExamPaperReq {

    @JsonProperty("exam_id")
    private String examId;

    @JsonProperty("exam_id")
    public String getExamId() {
        return examId;
    }

    @JsonProperty("exam_id")
    public void setExamId(String examId) {
        this.examId = examId;
    }

}
