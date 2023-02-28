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
        return Arrays.stream(HttpMethod.values()).filter(httpMethod -> httpMethod.name().equals(value)).findFirst().orElse(null);
    }
}
