package com.telemart.model;

public class LoginResponse {
	
	private LoginResponseData response;
	private String errorCode;
	private String errorMessage;
	
	public LoginResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LoginResponse(LoginResponseData response, String errorCode, String errorMessage) {
		super();
		this.response = response;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	public LoginResponseData getResponse() {
		return response;
	}
	public void setResponse(LoginResponseData response) {
		this.response = response;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	@Override
	public String toString() {
		return "LoginResponse [response=" + response + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage
				+ "]";
	}
	
	

}
