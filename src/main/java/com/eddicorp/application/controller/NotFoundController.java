package com.eddicorp.application.controller;

import com.eddicorp.http.request.HttpRequest;
import com.eddicorp.http.response.HttpResponse;
import com.eddicorp.http.response.HttpStatus;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class NotFoundController implements Controller {
    private static final String STATIC_FILE_PATH = "pages";

    @Override
    public void handle(HttpRequest request, HttpResponse response) throws IOException {

        final String pathToLoad = Paths.get(STATIC_FILE_PATH, "not-found.html").toString();

        FileInputStream fis = new FileInputStream(this.getClass().getClassLoader().getResource(pathToLoad).getFile());
;
        response.setStatus(HttpStatus.OK);
        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setContents(fis.readAllBytes());
    }
}
