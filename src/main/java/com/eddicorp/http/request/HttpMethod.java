package com.eddicorp.http.request;

import java.util.Arrays;

public enum HttpMethod {
    OPTIONS,
    GET,
    POST,
    PUT,
    DELETE,
    HEAD,
    TRACE,
    CONNECT,
    PATCH;

    public static HttpMethod find(String value) {

        for (HttpMethod httpMethod : HttpMethod.values()) {
            if(httpMethod.name().equals(value)) {
                return httpMethod;
            }
        }
        throw new IllegalStateException("Unable to find HTTPMETHOD");
    }
}
