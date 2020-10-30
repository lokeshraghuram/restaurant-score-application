package com.nhs.inspection.restaurantscoring.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CustomRuntimeException extends RuntimeException {

    private String code;
    private Date timestamp;
    private HttpStatus exceptionHttpStatus;
    private Throwable actualException;

    public CustomRuntimeException() {
        super();
    }

    public CustomRuntimeException(String message) {
        super(message);
    }

    public CustomRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomRuntimeException(Throwable cause) {
        super(cause);
    }

    public CustomRuntimeException(String code, String message, Date timestamp, HttpStatus exceptionHttpStatus, Throwable ex) {
        super(message, ex);
        this.code = code;
        this.timestamp = timestamp;
        this.exceptionHttpStatus = exceptionHttpStatus;
        this.actualException = ex;
    }

    public CustomRuntimeException(String code, String message, Date timestamp, HttpStatus exceptionHttpStatus) {
        super(message);
        this.code = code;
        this.timestamp = timestamp;
        this.exceptionHttpStatus = exceptionHttpStatus;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrorLogMessage() throws JsonProcessingException {
        Map<String, Object> payload = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        payload.put("code", this.code);
        payload.put("message", this.getMessage());
        payload.put("timestamp", this.timestamp);
        if (Objects.nonNull(this.actualException)) {
            Map<String, String> actualExceptionDetails = Maps.newHashMap();
            actualExceptionDetails.put("detailMessage", this.actualException.toString());
            //For Http 5xx responses from repositories, cause will be present
            if (Objects.nonNull(this.actualException.getCause())) {
                actualExceptionDetails.put("cause", this.actualException.getCause().toString());
            }
            payload.put("childException", actualExceptionDetails);
        } else payload.put("childException", null);
        return objectMapper.writeValueAsString(payload);
    }

    public HttpStatus getExceptionHttpStatus() {
        return exceptionHttpStatus;
    }

    public void setExceptionHttpStatus(HttpStatus exceptionHttpStatus) {
        this.exceptionHttpStatus = exceptionHttpStatus;
    }

    public Throwable getActualException() {
        return actualException;
    }

    public void setActualException(Throwable actualException) {
        this.actualException = actualException;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}