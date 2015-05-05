package com.recursos;


public class ConfiguracionGlobal {

	private static ConfiguracionGlobal singletonObject;
	private User user;

	public static synchronized ConfiguracionGlobal getSingletonObject() {
		if (singletonObject == null) {
			singletonObject = new ConfiguracionGlobal();
		}
		return singletonObject;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}