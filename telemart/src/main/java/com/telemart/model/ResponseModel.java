package com.telemart.model;

public class ResponseModel {
	
	private String response;
	private String errorCode;
	private String errorMessage;
	public ResponseModel(String response, String errorCode, String errorMessage) {
		super();
		this.response = response;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	public ResponseModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
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
		return "ResponseModel [response=" + response + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage
				+ "]";
	}
	
	

}
