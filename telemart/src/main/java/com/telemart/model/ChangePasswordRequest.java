package com.telemart.model;

public class ChangePasswordRequest {
	
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
	private Captcha captcha;
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public Captcha getCaptcha() {
		return captcha;
	}
	public void setCaptcha(Captcha captcha) {
		this.captcha = captcha;
	}
	@Override
	public String toString() {
		return "ChangePasswordRequest [oldPassword=" + oldPassword + ", newPassword=" + newPassword
				+ ", confirmPassword=" + confirmPassword + ", captcha=" + captcha + "]";
	}
	
	
	
	
	

}
