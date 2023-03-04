package com.eddicorp.application.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @since       2023.03.03
 * @author      martin
 * @description session manager
 **********************************************************************************************************************/
public class SessionManager {

	private static final Map<String, Session> sessions = new HashMap<>();
	private static int maxInactiveInterval = 3600;

	public static synchronized Session createSession() {
		String id = UUID.randomUUID().toString();
		Session session = new Session(id, maxInactiveInterval);
		sessions.put(id, session);
		return session;
	}

	public static synchronized void removeSession(String id) {
		sessions.remove(id);
	}

	public static synchronized Session getSession(String id) {
		Session session = sessions.get(id);
		if (session != null && session.isValid()) {
			session.access();
			return session;
		} else {
			return null;
		}
	}

	public static synchronized void setMaxInactiveInterval(int maxInactiveInterval) {
		SessionManager.maxInactiveInterval= maxInactiveInterval;
	}

	public static synchronized int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}
}
