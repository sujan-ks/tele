package com.telemart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="PasswordHistory")
public class PasswordHistoryEntity {
	@Id
	@Column(name="emailId",nullable = false)
	private String emailId;
	@Column(name="password")
	private String password;
	@Column(name="password1")
	private String password1;
	@Column(name="password2")
	private String password2;
	@Column(name="password3")
	private String password3;
	@Column(name="password4")
	private String password4;
	@Column(name="password5")
	private String password5;
	public String getEmailid() {
		return emailId;
	}
	public void setEmailid(String emailId) {
		this.emailId = emailId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword1() {
		return password1;
	}
	public void setPassword1(String password1) {
		this.password1 = password1;
	}
	public String getPassword2() {
		return password2;
	}
	public void setPassword2(String password2) {
		this.password2 = password2;
	}
	public String getPassword3() {
		return password3;
	}
	public void setPassword3(String password3) {
		this.password3 = password3;
	}
	public String getPassword4() {
		return password4;
	}
	public void setPassword4(String password4) {
		this.password4 = password4;
	}
	public String getPassword5() {
		return password5;
	}
	public void setPassword5(String password5) {
		this.password5 = password5;
	}
	@Override
	public String toString() {
		return "PasswordHistoryEntity [emailId=" + emailId + ", password=" + password + ", password1=" + password1
				+ ", password2=" + password2 + ", password3=" + password3 + ", password4=" + password4 + ", password5="
				+ password5 + "]";
	}
	
	
	

}
