package com.eddicorp.application.controller;

import com.eddicorp.application.service.posts.Post;
import com.eddicorp.application.service.posts.PostService;
import com.eddicorp.application.service.posts.PostServiceImpl;
import com.eddicorp.application.service.users.User;
import com.eddicorp.application.service.users.UserService;
import com.eddicorp.application.service.users.UserServiceImpl;
import com.eddicorp.application.session.SessionManager;
import com.eddicorp.application.util.ParseUtil;
import com.eddicorp.application.util.PrincipalHelper;
import com.eddicorp.examples.week1.Example3OutputStream;
import com.eddicorp.http.request.Cookie;
import com.eddicorp.http.request.HttpRequest;
import com.eddicorp.http.response.HttpResponse;
import com.eddicorp.http.response.HttpStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.*;
import java.net.HttpCookie;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since       2023.02.28
 * @author      martin
 * @description get controller
 **********************************************************************************************************************/
public class GetController implements Controller {

	private PostService postService = new PostServiceImpl();
	private UserService userService = new UserServiceImpl();
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public void handle(HttpRequest request, HttpResponse response) throws IOException {

		switch (request.getUri()) {
			case "/" : main(request, response); break;
			case "/logout" : logout(request, response); break;
		}
	}

	private void main(HttpRequest request, HttpResponse response) throws IOException {

		final String pathToLoad = Paths.get("pages", "index.html").toString();
		FileInputStream fis = new FileInputStream(this.getClass().getClassLoader().getResource(pathToLoad).getFile());

		Mustache.Compiler compiler = Mustache.compiler();
		Template template = compiler.compile(new String(fis.readAllBytes()));

		List<Post> posts = postService.getAllPosts();

		Map<String, Object> context = new HashMap<>();
		context.put("posts", posts);
		context.put("isLoggedIn", PrincipalHelper.isLoggedIn());

		response.setStatus(HttpStatus.OK);
		response.setHeader("Content-Type", "text/html; charset=UTF-8");
		response.setContents(template.execute(context).getBytes(StandardCharsets.UTF_8));
	}

	public void logout(HttpRequest request, HttpResponse response) {

		SessionManager.removeSession(PrincipalHelper.getSession().getId());
		Cookie cookie = new Cookie("user", null);

		response.setHeader("Set-Cookie", cookie.toString());
		redirect(request, response, "/");
	}

	private void redirect(HttpRequest request, HttpResponse response, String uri) {
		response.setStatus(HttpStatus.FOUND);
		response.setHeader("Location", uri);
		response.setHeader("Content-Type","text/html;charset=UTF-8");
	}
}
