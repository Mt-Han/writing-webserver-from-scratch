package com.eddicorp.application.controller;

import com.eddicorp.application.service.users.User;
import com.eddicorp.application.service.users.UserService;
import com.eddicorp.application.service.users.UserServiceImpl;
import com.eddicorp.application.session.Session;
import com.eddicorp.application.session.SessionManager;
import com.eddicorp.application.util.PrincipalHelper;
import com.eddicorp.http.request.Cookie;
import com.eddicorp.http.request.HttpMethod;
import com.eddicorp.http.request.HttpRequest;
import com.eddicorp.http.response.HttpResponse;
import com.eddicorp.http.response.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    private final UserService userService = new UserServiceImpl();

    public RootController() {
        requestMap.put(new RequestMapper("/", HttpMethod.GET), getController);
        requestMap.put(new RequestMapper("/logout", HttpMethod.GET), getController);
        requestMap.put(new RequestMapper("/post", HttpMethod.POST), postController);
        requestMap.put(new RequestMapper("/users", HttpMethod.POST), postController);
        requestMap.put(new RequestMapper("/login", HttpMethod.POST), postController);
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {

        try {

            final String uri = request.getUri();
            final RequestMapper requestMapper = new RequestMapper(uri, request.getHttpMethod());
            final Controller maybeController = requestMap.get(requestMapper);

            Cookie cookie = request.getCookie("user");
            if(cookie != null && !cookie.getValue().equals("null")) {
                Session session = SessionManager.getSession(cookie.getValue());
                if(session != null) {
                    PrincipalHelper.setSession(session);
                    PrincipalHelper.setUser(userService.findByUsername(session.getAttribute("username").toString()));
                }
            }

            // handle application API
            if (maybeController != null) {
                maybeController.handle(request, response);
                if(response.getHttpStatus() != null) {
                    return;
                }
            }

            // handle static files
            final String pathToLoad = Paths.get(STATIC_FILE_PATH, uri).toString();
            final URL maybeResource = this.getClass().getClassLoader().getResource(pathToLoad);
            if (maybeResource != null) {
                staticFileController.handle(request, response);
                if(response.getHttpStatus() != null) {
                    return;
                }
            }

            notFoundController.handle(request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
