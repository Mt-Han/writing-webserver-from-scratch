package com.eddicorp.application.session;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @since       2023.03.03
 * @author      martin
 * @description session
 **********************************************************************************************************************/
public class Session {
	private final String id;
	private final Map<String, Object> attributes;
	private final LocalDateTime createTime;
	private LocalDateTime lastAccessTime;
	private int maxInactiveInterval;

	public Session(String id, int maxInactiveInterval) {
		this.id = id;
		this.createTime = LocalDateTime.now();
		this.lastAccessTime = this.createTime;
		this.maxInactiveInterval = maxInactiveInterval;
		this.attributes = new HashMap<>();
	}

	public String getId() {
		return id;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public LocalDateTime getLastAccessTime() {
		return lastAccessTime;
	}

	public int getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public void setMaxInactiveInterval(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

	public Object getAttribute(String name) {
		return attributes.get(name);
	}

	public void removeAttribute(String name) {
		attributes.remove(name);
	}

	public boolean isValid() {
		if (maxInactiveInterval < 0) {
			return true;
		}

		LocalDateTime now = LocalDateTime.now();
		return now.minusSeconds(maxInactiveInterval).isBefore(lastAccessTime);
	}

	public void access() {
		lastAccessTime = LocalDateTime.now();
	}
}
