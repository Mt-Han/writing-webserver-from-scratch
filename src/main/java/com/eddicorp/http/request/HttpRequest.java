package com.eddicorp.http.request;

import com.eddicorp.http.session.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final String uri;
    private final HttpMethod httpMethod;

    private final Map<String, String> headerMap = new HashMap<>();

    private final Map<String, String> parameterMap = new HashMap<>();

    private final Map<String, Cookie> cookieMap = new HashMap<>();

    private byte[] rawBody;

    public HttpRequest(final InputStream inputStream) throws IOException {

        if(inputStream.available() == 0) {
            uri = null;
            httpMethod = null;
            return;
        }

        final String request = readLine(inputStream);
        System.out.println(request);
        String[] requestInfo = request.split(" ");

        this.httpMethod = HttpMethod.find(requestInfo[0]);
        this.uri = requestInfo[1];

        while (inputStream.available() > 0) {

            String headerLine = readLine(inputStream);

            if("".equals(headerLine)) {
                continue;
            }

            String[] headerValues = headerLine.split(":");
            headerMap.put(headerValues[0].trim(), headerValues[1].trim());

            if(headerMap.containsKey("Content-Length")) {
                setRawBody(inputStream);
            }
        }
    }

    private void setRawBody(InputStream inputStream) throws IOException {

        int readCount = Integer.parseInt(headerMap.get("Content-Length"));

        if(inputStream.available() == readCount + 2) {
            readLine(inputStream);
            if(inputStream.available() > 0) {

                byte[] buffer = new byte[8];
                buffer = Arrays.copyOf(buffer, readCount);

                inputStream.read(buffer);
                this.rawBody = buffer;
            }
        }
    }

    private static String readLine(InputStream inputStream) throws IOException {
        final StringBuilder stringBuilder = new StringBuilder();
        int readCharacter;
        while ((readCharacter = inputStream.read()) != -1) {
            final char currentChar = (char) readCharacter;
            if (currentChar == '\r') {
                if (((char) inputStream.read()) == '\n') {
                    return stringBuilder.toString();
                } else {
                    throw new IllegalStateException("Invalid HTTP request.");
                }
            }
            stringBuilder.append(currentChar);
        }
        throw new IllegalStateException("Unable to find CRLF");
    }

    public String getUri() {
        return uri;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getHeader(String header) {
        return headerMap.get(header);
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

    @Override
    public String toString() {
        return "HttpRequest{" +
                "uri='" + uri + '\'' +
                ", httpMethod=" + httpMethod +
                ", headerMap=" + headerMap +
                ", parameterMap=" + parameterMap +
                ", cookieMap=" + cookieMap +
                ", rawBody=" + Arrays.toString(rawBody) +
                '}';
    }
}
