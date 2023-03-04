package com.eddicorp.application.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @since       2023.03.02
 * @author      martin
 * @description parse util
 **********************************************************************************************************************/
public class ParseUtil {

	private static ObjectMapper mapper = new ObjectMapper();
	public static Map<String, String> xFormUrlEncoded(String body) throws UnsupportedEncodingException {

		Map<String, String> parameters = new HashMap<>();
		String[] pairs = body.split("&");
		for (String pair : pairs) {
			int idx = pair.indexOf("=");
			String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
			String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
			parameters.put(key, value);
		}

		return parameters;
	}

	public static HttpCookie cookieParse(String cookie) {

		String[] values = cookie.split(" ");
		String[] nameAndValue = values[0].replace(";", "").split("=");
		String[] path = values[1].replace(";", "").split("=");

		HttpCookie requestCookie = new HttpCookie(nameAndValue[0], nameAndValue[1]);
		requestCookie.setPath(path[1]);

		return requestCookie;
	}

	public static String ObjectToJson(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
