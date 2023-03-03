package com.eddicorp.application.controller;

import com.eddicorp.http.request.HttpRequest;
import com.eddicorp.http.response.HttpResponse;
import com.eddicorp.http.response.HttpStatus;

import java.io.FileInputStream;
import java.io.IOException;
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

        response.setStatus(HttpStatus.OK);
        response.setHeader("Content-Type", contentType);
        response.setContents(fis.readAllBytes());
    }
}
