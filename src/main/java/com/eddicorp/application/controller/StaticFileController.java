package com.eddicorp.application.controller;

import com.eddicorp.http.request.HttpRequest;
import com.eddicorp.http.response.HttpResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class StaticFileController implements Controller {

    private static final String STATIC_FILE_PATH = "pages";

    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException {

        final String uri = request.getUri();
        final String pathToLoad = Paths.get(STATIC_FILE_PATH, uri).toString();

        FileInputStream fis = new FileInputStream(this.getClass().getClassLoader().getResource(pathToLoad).getFile());

        String preFix = uri.substring(uri.lastIndexOf(".") + 1);
        String contentType = "text/html; charset=UTF-8";
        switch (preFix) {
            case "svg" : contentType = "image/svg+xml; charset=UTF-8"; break;
            case "css" : contentType = "text/css; charset=UTF-8"; break;
        }

        final byte[] contents = fis.readAllBytes();

        final String responseMeta =
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + contentType + "\r\n" +
                        "Content-Length: " + contents.length +
                        "\r\n" +
                        "\r\n" + new String(contents);
        response.getOutputStream().write(responseMeta.getBytes(StandardCharsets.UTF_8), 0, responseMeta.getBytes(StandardCharsets.UTF_8).length);
        response.getOutputStream().flush();
    }
}
