package com.fballtech.scoutbasebeta.service;

import com.fballtech.scoutbasebeta.entities.User;

public class SessionManager {
	
	private static SessionManager instance;
	private User user;
	private Boolean isOnline;
	
	private SessionManager() {
		
	}
	
	public static synchronized SessionManager getInstance() {
		if(instance == null) {
			instance = new SessionManager();
		}
		return instance;
	}
	
	public void startSession(User user) {
		if(user == null) {
			throw new IllegalArgumentException("User is null");
		}
		this.user = user;
		isOnline = true;
		System.out.println("Session started: " + user.getUsername());
	}
	
	public void endSession() {
		this.user = null;
		isOnline = false;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
