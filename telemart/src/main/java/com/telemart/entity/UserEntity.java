package com.telemart.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="userdetails")
public class UserEntity {
	@Id
	@Column(name="emailId")
	private String emailId;
	@Column(name="password", length =500)
	private String password;
	@Column(name="name")
	private String name;
	@Column(name="role")
	private String role;
	@Column(name="isNewUser")
	private boolean isNewuser;
	@Column(name="isLoggedIn")
	private boolean isLoggedIn;
	@Column(name="lastLoginTime")
	private LocalDateTime lastLoginTime;
	@Column(name="incorrectLoginAttempts")
	private int incorrectLoginAttempts;
	@Column(name="accountLockedTime")
	private LocalDateTime accountLockedTime;
	@Column(name="expiredToken", length =500)
	private String expiredToken;

	@Column(name="generatedToken", length =500)
	private String generatedToken;
	@Column(name="forgotPassword")
	private boolean forgotPassword;
	
	@Column(name="forgotPasswordAttempts",columnDefinition = "integer default 0")
	private int forgotPasswordAttempts;
	@Column(name="forgotPasswordLockedTime")
	private LocalDateTime forgotPasswordLockedTime;
	@Column(name="changePasswordAttempts",columnDefinition = "integer default 0")
	private int changePasswordAttempts;
	@Column(name="changePasswordLockedTime")
	private LocalDateTime changePasswordLockedTime;
	
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isLoggedIn() {
		return isLoggedIn;
	}
	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}
	public LocalDateTime getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(LocalDateTime lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public int getIncorrectLoginAttempts() {
		return incorrectLoginAttempts;
	}
	public void setIncorrectLoginAttempts(int incorrectLoginAttempts) {
		this.incorrectLoginAttempts = incorrectLoginAttempts;
	}
	public LocalDateTime getAccountLockedTime() {
		return accountLockedTime;
	}
	public void setAccountLockedTime(LocalDateTime accountLockedTime) {
		this.accountLockedTime = accountLockedTime;
	}
	public String getExpiredToken() {
		return expiredToken;
	}
	public void setExpiredToken(String expiredToken) {
		this.expiredToken = expiredToken;
	}
	public String getGeneratedToken() {
		return generatedToken;
	}
	public void setGeneratedToken(String generatedToken) {
		this.generatedToken = generatedToken;
	}
	public boolean isForgotPassword() {
		return forgotPassword;
	}
	public void setForgotPassword(boolean forgotPassword) {
		this.forgotPassword = forgotPassword;
	}
	public int getForgotPasswordAttempts() {
		return forgotPasswordAttempts;
	}
	public void setForgotPasswordAttempts(int forgotPasswordAttempts) {
		this.forgotPasswordAttempts = forgotPasswordAttempts;
	}
	public LocalDateTime getForgotPasswordLockedTime() {
		return forgotPasswordLockedTime;
	}
	public void setForgotPasswordLockedTime(LocalDateTime forgotPasswordLockedTime) {
		this.forgotPasswordLockedTime = forgotPasswordLockedTime;
	}
	public int getChangePasswordAttempts() {
		return changePasswordAttempts;
	}
	public void setChangePasswordAttempts(int changePasswordAttempts) {
		this.changePasswordAttempts = changePasswordAttempts;
	}
	public LocalDateTime getChangePasswordLockedTime() {
		return changePasswordLockedTime;
	}
	public void setChangePasswordLockedTime(LocalDateTime changePasswordLockedTime) {
		this.changePasswordLockedTime = changePasswordLockedTime;
	}
	
	public boolean isNewuser() {
		return isNewuser;
	}
	public void setNewuser(boolean isNewuser) {
		this.isNewuser = isNewuser;
	}
	@Override
	public String toString() {
		return "UserEntity [emailId=" + emailId + ", password=" + password + ", name=" + name + ", role=" + role
				+ ", isNewuser=" + isNewuser + ", isLoggedIn=" + isLoggedIn + ", lastLoginTime=" + lastLoginTime
				+ ", incorrectLoginAttempts=" + incorrectLoginAttempts + ", accountLockedTime=" + accountLockedTime
				+ ", expiredToken=" + expiredToken + ", generatedToken=" + generatedToken + ", forgotPassword="
				+ forgotPassword + ", forgotPasswordAttempts=" + forgotPasswordAttempts + ", forgotPasswordLockedTime="
				+ forgotPasswordLockedTime + ", changePasswordAttempts=" + changePasswordAttempts
				+ ", changePasswordLockedTime=" + changePasswordLockedTime + "]";
	}
	
}
