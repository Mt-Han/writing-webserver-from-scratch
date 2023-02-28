package com.eddicorp.http.request;

import com.eddicorp.http.session.HttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final String uri;
    private final HttpMethod httpMethod;

    private final Map<String, String> headerMap = new HashMap<>();

    private final Map<String, String> parameterMap = new HashMap<>();

    private final Map<String, Cookie> cookieMap = new HashMap<>();

    private final byte[] rawBody;

    public HttpRequest(final InputStream inputStream) throws IOException {

        final String request = new String(inputStream.readAllBytes());
        final String[] lines = request.split("\r\n");
        final String requestLine = lines[0];
        final String[] partsOfRequestLines = requestLine.split(" ");

        this.httpMethod = HttpMethod.find(partsOfRequestLines[0]);
        this.uri = partsOfRequestLines[1];
        this.rawBody = lines[lines.length -1].getBytes();

        for (int i=1; i < lines.length; i++) {

            final String headerValue = lines[i];
            if("".equals(headerValue)) {
                break;
            }
            final String[] nameAndValue = headerValue.split(":");
            headerMap.put(nameAndValue[0].trim(), nameAndValue[1].trim());
        }
    }

    public String getUri() {
        return uri;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getRawBody() {
        return new String(rawBody);
    }

    public String getParameter(String parameterName) {
        return parameterMap.get(parameterName);
    }

    public HttpSession getSession() {
        return getSession(false);
    }

    public HttpSession getSession(boolean create) {
        return null;
    }
}
