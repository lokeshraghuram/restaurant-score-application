package com.nhs.inspection.restaurantscoring.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.logstash.logback.encoder.org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final ZoneId DEFAULT_ZONE_ID = ZoneId.of("UTC");
    private Logger errorLogger = LoggerFactory.getLogger("ErrorLogger");
    private final ObjectMapper objectMapper;

    @Autowired
    public RestResponseEntityExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleIllegalArgument(RuntimeException ex, WebRequest request) {
        CustomRuntimeException exception = new CustomRuntimeException("ILLEGAL_ARGUMENT_FOUND", "Illegal Argument Exception occurred",
                Timestamp.valueOf(LocalDateTime.now(DEFAULT_ZONE_ID)), HttpStatus.INTERNAL_SERVER_ERROR);
        return handleExceptionInternal(ex, exception,
                getResponseHeaders(request), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomRuntimeException exception = new CustomRuntimeException("SCHEMA_VALIDATON_ERROR", "Message not valid",
                Timestamp.valueOf(LocalDateTime.now(DEFAULT_ZONE_ID)), HttpStatus.BAD_REQUEST);
        return handleExceptionInternal(ex, exception,
                getResponseHeaders(request), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        CustomRuntimeException exception = new CustomRuntimeException("SCHEMA_VALIDATON_ERROR", "Message not readable",
                Timestamp.valueOf(LocalDateTime.now(DEFAULT_ZONE_ID)), HttpStatus.BAD_REQUEST);
        return handleExceptionInternal(ex, exception,
                getResponseHeaders(request), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {CustomRuntimeException.class})
    protected ResponseEntity<Object> handleSPERuntimeException(CustomRuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex,
                getResponseHeaders(request), ex.getExceptionHttpStatus(), request);
    }

    @ExceptionHandler(value = {NullPointerException.class})
    protected ResponseEntity<Object> handleNullPointerException(NullPointerException ex, WebRequest request) {
        CustomRuntimeException exception = new CustomRuntimeException("INTERNAL_ERROR", "Null pointer exception",
                Timestamp.valueOf(LocalDateTime.now(DEFAULT_ZONE_ID)), HttpStatus.INTERNAL_SERVER_ERROR);
        return handleExceptionInternal(ex, exception,
                getResponseHeaders(request), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private HttpHeaders getResponseHeaders(WebRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, request.getHeader(HttpHeaders.CONTENT_TYPE));
        return httpHeaders;
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorInfo errorInfo;
        if (body instanceof CustomRuntimeException) {
            errorInfo = new ErrorInfo(((CustomRuntimeException) body).getTimestamp(), ((CustomRuntimeException) body).getCode(),
                    ((CustomRuntimeException) body).getMessage(), ((ServletWebRequest) request).getRequest().getRequestURI());
        } else {
            errorInfo = new ErrorInfo(Timestamp.valueOf(LocalDateTime.now(DEFAULT_ZONE_ID)), "INTERNAL_ERROR", ex.getMessage(), ((ServletWebRequest) request).getRequest().getRequestURI());
        }
        try {
            CustomRuntimeException exception;
            if (ex instanceof CustomRuntimeException) {
                exception = (CustomRuntimeException) ex;
            } else if (body instanceof CustomRuntimeException) {
                exception = (CustomRuntimeException) body;
            } else {
                exception = new CustomRuntimeException(errorInfo.getCode(), errorInfo.getMessage(), Timestamp.valueOf(LocalDateTime.now(DEFAULT_ZONE_ID)), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            errorLogger.error(net.logstash.logback.marker.Markers.appendRaw("error", exception.getErrorLogMessage()), null, kv("message", ExceptionUtils.getStackTrace(ex)));
        } catch (JsonProcessingException e1) {
            errorLogger.error(net.logstash.logback.marker.Markers.appendRaw("error", "JACKSON_ERROR"), null, kv("message", ExceptionUtils.getStackTrace(ex)));
        }
        return super.handleExceptionInternal(ex, errorInfo, headers, status, request);
    }
}