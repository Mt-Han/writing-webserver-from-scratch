package com.eddicorp.http.response;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {

    private static final String CRLF = "\r\n";
    private static final String HTTP = "HTTP/1.1";

    private final OutputStream outputStream;
    private final Map<String, String> responseHeaders = new HashMap<>();
    private HttpStatus httpStatus = null;
    private byte[] contents = new byte[0];

    public HttpResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setHeader(String headerName, String headerValue) {
        responseHeaders.put(headerName, headerValue);
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }

    public void write() throws IOException {

        StringBuilder builder = new StringBuilder();

        builder.append(HTTP).append(" ").append(httpStatus.getHttpStatusCode()).append(" ").append(httpStatus.getReasonPhrase()).append(CRLF);

        for (String s : responseHeaders.keySet()) {
            builder.append(s).append(": ").append(responseHeaders.get(s)).append(CRLF);
        }

        builder.append("ContentLength: ").append(contents.length).append(CRLF);
        builder.append(CRLF);
        builder.append(new String(contents));

        byte[] responseBytes = builder.toString().getBytes();

        outputStream.write(responseBytes, 0, responseBytes.length);
        outputStream.flush();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void renderResponseWithBody(byte[] rawBody) {

    }
}
