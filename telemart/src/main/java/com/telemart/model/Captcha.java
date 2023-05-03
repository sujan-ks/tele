package com.telemart.model;

public class Captcha {
	
	String a;
	String b;
	String c;
	
	
	public Captcha() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getA() {
		return a;
	}


	public void setA(String a) {
		this.a = a;
	}


	public String getB() {
		return b;
	}


	public void setB(String b) {
		this.b = b;
	}


	public String getC() {
		return c;
	}


	public void setC(String c) {
		this.c = c;
	}


	@Override
	public String toString() {
		return "Captcha [a=" + a + ", b=" + b + ", c=" + c + "]";
	}
	
	

}
