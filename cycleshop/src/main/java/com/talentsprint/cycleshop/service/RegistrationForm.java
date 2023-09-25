package com.talentsprint.cycleshop.service;

import lombok.Data;

@Data
public class RegistrationForm {
	
	private String name;
    private String password;
    private String repeatPassword;
	private String role;
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepeatPassword() {
		return repeatPassword;
	}

	public void setRepeatPassword(String repeatPassword) {
		this.repeatPassword = repeatPassword;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}


    public boolean isValid() {
        return password.equals(repeatPassword);
    }
}
