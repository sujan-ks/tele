package com.telemart.model;

public class ForgotPasswordRequest {
	
	private String emailId;
	Captcha captcha;
	
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public Captcha getCaptcha() {
		return captcha;
	}
	public void setCaptcha(Captcha captcha) {
		this.captcha = captcha;
	}
	@Override
	public String toString() {
		return "ForgotPasswordRequest [emailId=" + emailId + ", captcha=" + captcha + "]";
	}
	
	
	

}
