package com.eddicorp.application.controller;

import com.eddicorp.http.request.HttpMethod;
import com.eddicorp.http.request.HttpRequest;
import com.eddicorp.http.response.HttpResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class RootController implements Controller {

    private static final String STATIC_FILE_PATH = "pages";
    private static final Map<RequestMapper, Controller> requestMap = new HashMap<>();
    private final Controller staticFileController = new StaticFileController();
    private final Controller notFoundController = new NotFoundController();
    private final Controller getController = new GetController();
    private final Controller postController = new PostController();

    public RootController() {
        requestMap.put(new RequestMapper("/", HttpMethod.GET), getController);
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {

        try {

            final String uri = request.getUri();
            final RequestMapper requestMapper = new RequestMapper(uri, request.getHttpMethod());
            final Controller maybeController = requestMap.get(requestMapper);

            // handle application API
            if (maybeController != null) {
                maybeController.handle(request, response);
                return;
            }

            // handle static files
            final String pathToLoad = Paths.get(STATIC_FILE_PATH, uri).toString();
            final URL maybeResource = this.getClass().getClassLoader().getResource(pathToLoad);
            if (maybeResource != null) {
                staticFileController.handle(request, response);
                return;
            }

            notFoundController.handle(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
