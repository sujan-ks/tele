package com.telemart.model;

public class LoginRequest {
	
	String emailId;
	String password;
	Captcha captcha;
	public LoginRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Captcha getCaptcha() {
		return captcha;
	}
	public void setCaptcha(Captcha captcha) {
		this.captcha = captcha;
	}
	@Override
	public String toString() {
		return "LoginRequest [emailId=" + emailId + ", password=" + password + ", captcha=" + captcha + "]";
	}
	
	

}
