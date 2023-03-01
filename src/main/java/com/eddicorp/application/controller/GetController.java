package com.eddicorp.application.controller;

import com.eddicorp.examples.week1.Example3OutputStream;
import com.eddicorp.http.request.HttpRequest;
import com.eddicorp.http.response.HttpResponse;
import com.eddicorp.model.Posts;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

	@Override
	public void handle(HttpRequest request, HttpResponse response) throws IOException {

		if(request.getUri().equals("/")) {
			String str1 = "HTTP/1.1 302\n" +
					"Location: /index.html\n" +
					"Content-Type: text/html;charset=UTF-8\n" +
					"Content-Length: 0";
			response.getOutputStream().write(str1.getBytes(StandardCharsets.UTF_8), 0, str1.getBytes(StandardCharsets.UTF_8).length);
			response.getOutputStream().flush();
		}

		final String str =
				"HTTP/1.1 200 OK\r\n" +
						"Content-Type: text/html; charset=UTF-8\r\n" +
						"Content-Length: 1005"+
						"\r\n" +
						"\r\n";


		response.renderResponseWithBody(str.getBytes(StandardCharsets.UTF_8));
	}
}
