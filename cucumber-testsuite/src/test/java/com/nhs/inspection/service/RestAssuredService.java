package com.nhs.inspection.service;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.assertj.core.util.Lists;

import java.util.List;
import java.util.Map;

public abstract class RestAssuredService {
    Response response;

    public RestAssuredService() {
    }

    protected abstract String givenStep();

    protected Response whenStep(Map<String, String> headers, String requestBody, String url, String requestType, String contentType, Map<String, String> pathParams) {
        if (requestType.equalsIgnoreCase("POST")) {
            return this.postRestAssured(headers, requestBody, url, contentType, pathParams);
        } else if (requestType.equalsIgnoreCase("GET")) {
            return this.getRestAssured(headers, requestBody, url, contentType, pathParams);
        } else if (requestType.equalsIgnoreCase("DELETE")) {
            return this.deleteRestAssured(headers, requestBody, url, contentType, pathParams);
        } else {
            return requestType.equalsIgnoreCase("PUT") ? this.putRestAssured(headers, requestBody, url, contentType, pathParams) : this.response;
        }
    }

    protected abstract void thenStep(Response response);

    protected abstract void thenStep(Response response, int expectedStatusCode);

    private Response getRestAssured(Map<String, String> headers, String requestBody, String url, String contentType, Map<String, String> pathParams) {
        if (pathParams == null) {

            this.response = (Response) RestAssured.given().headers(createHeadersObject(headers)).contentType(contentType).body(requestBody).when().get(url, new Object[0]);
            ((ValidatableResponse) ((Response) RestAssured.given().contentType(contentType).body(requestBody).when().get(url, new Object[0])).then()).log().all();
        }
        return this.response;
    }

    private Response postRestAssured(Map<String, String> headers, String requestBody, String url, String contentType, Map<String, String> pathParams) {
        if (pathParams == null) {
            this.response = (Response) RestAssured.given().headers(createHeadersObject(headers)).contentType(contentType).body(requestBody).when().post(url, new Object[0]);
            ((ValidatableResponse) ((Response) RestAssured.given().contentType(contentType).body(requestBody).when().post(url, new Object[0])).then()).log().all();
        }
        return this.response;
    }

    private Response putRestAssured(Map<String, String> headers, String requestBody, String url, String contentType, Map<String, String> pathParams) {
        if (pathParams == null) {
            this.response = (Response) RestAssured.given().headers(createHeadersObject(headers)).contentType(contentType).body(requestBody).when().delete(url, new Object[0]);
            ((ValidatableResponse) ((Response) RestAssured.given().contentType(contentType).body(requestBody).when().delete(url, new Object[0])).then()).log().all();
        }
        return this.response;
    }

    private Response deleteRestAssured(Map<String, String> headers, String requestBody, String url, String contentType, Map<String, String> pathParams) {
        if (pathParams == null) {
            this.response = (Response) RestAssured.given().headers(createHeadersObject(headers)).contentType(contentType).body(requestBody).when().delete(url, new Object[0]);
            ((ValidatableResponse) ((Response) RestAssured.given().contentType(contentType).body(requestBody).when().delete(url, new Object[0])).then()).log().all();
        }
        return this.response;
    }

    private Headers createHeadersObject(Map<String, String> headers) {
        List<Header> headerList = Lists.newArrayList();
        headers.forEach((headerKey, headerValue) -> {
            headerList.add(new Header(headerKey, headerValue));
        });
        return new Headers(headerList);
    }
}
