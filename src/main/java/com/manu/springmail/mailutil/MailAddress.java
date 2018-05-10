package com.manu.springmail.mailutil;

public class MailAddress {
	
	private String name;
	
	private String email;

	
	public String getName() {
		return name;
	}

	public MailAddress(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	
	public void setEmail(String email) {
		this.email = email;
	}

}
