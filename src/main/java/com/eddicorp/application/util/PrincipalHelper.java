package com.eddicorp.application.util;

import com.eddicorp.application.service.users.User;
import com.eddicorp.application.session.Session;

/**
 * @since       2023.03.02
 * @author      martin
 * @description principal helper
 **********************************************************************************************************************/
public class PrincipalHelper {

	private static ThreadLocal<User> USER = new ThreadLocal<>();
	private static ThreadLocal<Session> SESSION = new ThreadLocal<>();

	public static void setUser(User user) {
		USER.set(user);
	}

	public static User getUser() {
		return USER.get();
	}

	public static Session getSession() {
		return SESSION.get();
	}

	public static void setSession(Session session) {
		SESSION.set(session);
	}

	public static boolean isLoggedIn() {
		return USER.get() != null;
	}

	public static void close() {
		USER.remove();
		SESSION.remove();
	}
}
