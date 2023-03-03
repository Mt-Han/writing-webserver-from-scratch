package com.eddicorp.application.util;

import com.eddicorp.application.service.users.User;

/**
 * @since       2023.03.02
 * @author      martin
 * @description principal helper
 **********************************************************************************************************************/
public class PrincipalHelper {

	private static ThreadLocal<User> USER = new ThreadLocal<>();

	public static void setUser(User user) {
		USER.set(user);
	}

	public static User getUser() {
		return USER.get();
	}

	public static boolean isLoggedIn() {
		return USER.get() != null;
	}
}
