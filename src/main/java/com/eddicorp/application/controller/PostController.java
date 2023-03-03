package com.eddicorp.application.controller;

import com.eddicorp.application.service.posts.PostService;
import com.eddicorp.application.service.posts.PostServiceImpl;
import com.eddicorp.application.service.users.User;
import com.eddicorp.application.service.users.UserService;
import com.eddicorp.application.service.users.UserServiceImpl;
import com.eddicorp.application.util.ParseUtil;
import com.eddicorp.application.util.PrincipalHelper;
import com.eddicorp.http.request.Cookie;
import com.eddicorp.http.request.HttpRequest;
import com.eddicorp.http.response.HttpResponse;
import com.eddicorp.http.response.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpCookie;

/**
 * @since       2023.02.28
 * @author      martin
 * @description post controller
 **********************************************************************************************************************/
public class PostController implements Controller {

	private PostService postService = new PostServiceImpl();
	private UserService userService = new UserServiceImpl();

	@Override
	public void handle(HttpRequest request, HttpResponse response) {

		switch (request.getUri()) {
			case "/post" : addPost(request, response); return;
			case "/users" : addUser(request, response); return;
			case "/login" : login(request, response); return;
		}
	}

	private void notFound(HttpRequest request, HttpResponse response) {
		try {
			new NotFoundController().handle(request, response);
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addPost(HttpRequest request, HttpResponse response) {

		if(!PrincipalHelper.isLoggedIn()) {
			notFound(request, response);
			return;
		}

		final String author = PrincipalHelper.getUser().getUsername();
		final String title = request.getParameter("title");
		final String content = request.getParameter("content");

		postService.write(author, title, content);

		response.setStatus(HttpStatus.FOUND);
		response.setHeader("Location", "/");
		response.setHeader("Content-Type","text/html;charset=UTF-8");
	}

	private void addUser(HttpRequest request, HttpResponse response) {

		final String username = request.getParameter("username");
		final String password = request.getParameter("password");

		userService.signUp(username, password);

		response.setStatus(HttpStatus.FOUND);
		response.setHeader("Location", "/");
		response.setHeader("Content-Type","text/html;charset=UTF-8");
	}

	private void login(HttpRequest request, HttpResponse response) {

		User user = userService.findByUsername(request.getParameter("username"));

		if(user == null) {
			notFound(request, response);
			return;
		}

		if(!user.getPassword().equals(request.getParameter("password"))) {
			notFound(request, response);
			return;
		}

		Cookie cookie = new Cookie("user", ParseUtil.ObjectToJson(user));

		response.setStatus(HttpStatus.FOUND);
		response.setHeader("Set-Cookie", cookie.toString());
		response.setHeader("Location", "/");
		response.setHeader("Content-Type","text/html;charset=UTF-8");
	}
}
