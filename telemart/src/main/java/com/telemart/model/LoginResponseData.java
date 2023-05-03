package com.telemart.model;

public class LoginResponseData {
	private String emailId;
	private String role;
	private String jwtToken;
	private Enum<?> authenticationCode;
	private int incorrectLoginAttempts;
	private boolean isHRLoggedIn;
	private boolean forgotPassword;
	
	
	public LoginResponseData() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getJwtToken() {
		return jwtToken;
	}
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	public int getIncorrectLoginAttempts() {
		return incorrectLoginAttempts;
	}
	public void setIncorrectLoginAttempts(int incorrectLoginAttempts) {
		this.incorrectLoginAttempts = incorrectLoginAttempts;
	}
	public boolean isHRLoggedIn() {
		return isHRLoggedIn;
	}
	public void setHRLoggedIn(boolean isHRLoggedIn) {
		this.isHRLoggedIn = isHRLoggedIn;
	}
	public boolean isForgotPassword() {
		return forgotPassword;
	}
	public void setForgotPassword(boolean forgotPassword) {
		this.forgotPassword = forgotPassword;
	}
	
	public Enum<?> getAuthenticationCode() {
		return authenticationCode;
	}
	public void setAuthenticationCode(Enum<?> authenticationCode) {
		this.authenticationCode = authenticationCode;
	}
	
	@Override
	public String toString() {
		return "LoginReponseData [emailId=" + emailId + ", role=" + role + ", jwtToken=" + jwtToken
				+ ", authenticationCode=" + authenticationCode + ", incorrectLoginAttempts=" + incorrectLoginAttempts
				+ ", isHRLoggedIn=" + isHRLoggedIn + ", forgotPassword=" + forgotPassword + "]";
	}
	
	
	

}
