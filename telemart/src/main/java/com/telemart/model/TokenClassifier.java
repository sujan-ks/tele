package com.telemart.model;

import java.util.Arrays;

public class TokenClassifier {
	private String jti;
	private String sub;
	private String[] authorities;
	private String iat;
	private String exp;
	public String getJti() {
		return jti;
	}
	public void setJti(String jti) {
		this.jti = jti;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public String[] getAuthorities() {
		return authorities;
	}
	public void setAuthorities(String[] authorities) {
		this.authorities = authorities;
	}
	public String getIat() {
		return iat;
	}
	public void setIat(String iat) {
		this.iat = iat;
	}
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
	@Override
	public String toString() {
		return "TokenClassifier [jti=" + jti + ", sub=" + sub + ", authorities=" + Arrays.toString(authorities)
				+ ", iat=" + iat + ", exp=" + exp + "]";
	}
	
	
	
	

}
