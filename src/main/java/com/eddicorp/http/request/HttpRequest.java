package com.eddicorp.http.request;

import com.eddicorp.application.util.ParseUtil;
import com.eddicorp.http.session.HttpSession;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final String uri;
    private final HttpMethod httpMethod;

    private final Map<String, String> headerMap = new HashMap<>();

    private final Map<String, String> parameterMap = new HashMap<>();

    private final Map<String, Cookie> cookieMap = new HashMap<>();

    private byte[] rawBody = new byte[0];

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
                setRawBody(inputStream);
                return;
            }

            String[] headerValues = headerLine.split(": ");
            if(headerValues[0].trim().equals("Cookie")) {
                String[] cookies = headerValues[1].split(";");
                for (String cookieSpecialty : cookies) {
                    if(!cookieSpecialty.trim().equals("null")) {
                        String[] cookie = cookieSpecialty.trim().split("=");
                        cookieMap.put(cookie[0], new Cookie(cookie[0], cookie[1]));
                    }
                }
                continue;
            }

            headerMap.put(headerValues[0].trim(), headerValues[1].trim());
        }
    }

    private void setRawBody(InputStream inputStream) throws IOException {

        if(!headerMap.containsKey("Content-Length") || inputStream.available() == 0) {
            return;
        }

        int readCount = Integer.parseInt(headerMap.get("Content-Length"));

        if(inputStream.available() == readCount) {

            byte[] buffer = new byte[8];
            buffer = Arrays.copyOf(buffer, readCount);

            inputStream.read(buffer);
            this.rawBody = buffer;

            String contentType = headerMap.get("Content-Type");
            ObjectMapper mapper = new ObjectMapper();

            if(contentType == null) {
                return;
            }

            switch (headerMap.get("Content-Type")) {
                case "application/x-www-form-urlencoded" :
                    parameterMap.putAll(ParseUtil.xFormUrlEncoded(new String(rawBody)));
                    break;
                case "application/json" :
                    parameterMap.putAll(mapper.readValue(new String(rawBody), Map.class));
                    break;
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

    public Map<String, Cookie> getCookieMap() {
        return cookieMap;
    }

    public Cookie getCookie(String name) {
        return cookieMap.get(name);
    }
}
