package com.fballtech.scoutbasebeta.entities;

import java.util.Set;

public class User {
	private Long id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String email;
	private boolean isAdmin;
	private boolean isActive;
	
	
	
	public User(Long id, String firstName, String lastName, String username, String password, String email,
			boolean isActive, boolean isAdmin) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.isAdmin = isAdmin;
		this.isActive = isActive;
		
	}
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	

	public User() {
		id = (long) 0;
		firstName = "";
		lastName = "";
		username = "";
		password = "";
		email = "";
		isAdmin = false;
		isActive = false;
	}
	

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}


}
