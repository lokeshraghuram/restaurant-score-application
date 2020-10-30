package com.nhs.inspection.restaurantscoring.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorInfo {

    private Date timestamp;
    private String code;
    private String message;
    private String path;

    public ErrorInfo() {
    }

    public ErrorInfo(Date timestamp, String code, String message, String path) {
        this.timestamp = timestamp;
        this.code = code;
        this.message = message;
        this.path = path;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
