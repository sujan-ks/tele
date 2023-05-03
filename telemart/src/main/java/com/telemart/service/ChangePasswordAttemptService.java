package com.telemart.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Service
public class ChangePasswordAttemptService {
	
	public static final int MAX_ATTEMPT = 5;
	@Value("${max.changePassword.attempts}")
	private int maxForgotPasswordAttempts;
	private LoadingCache<String, Integer> changePasswordAttemptsCache;
	
	@Autowired
	private HttpServletRequest request;
	
	public ChangePasswordAttemptService() {
		super();
		changePasswordAttemptsCache = CacheBuilder.newBuilder().expireAfterWrite(1,TimeUnit.DAYS).build(new CacheLoader<String, Integer>(){
			@Override
			public Integer load(final String key) throws Exception {				
				return 0;
			}			
		});		
	}
	
	
	public void changePassword(final String key) {
		int attempts;
		try {
			attempts = changePasswordAttemptsCache.get(key);
		}catch(final ExecutionException e) {
			attempts = 0;
		}
		attempts++;
		changePasswordAttemptsCache.put(key, attempts);
	}
	
	public void unblock(final String key) {
		int attempts;
		try {
			attempts = changePasswordAttemptsCache.get(key);
		}catch(final ExecutionException e) {
			attempts = 0;
		}		
		changePasswordAttemptsCache.put(key, 1);		
	}
	
	public boolean isBlocked(String key) {
		try {
			return changePasswordAttemptsCache.get(key)>maxForgotPasswordAttempts;
		}catch(final ExecutionException e) {
			return false;
		}
	}
	
	private String getClientIP() {
		final String xfHeader  = request.getHeader("X-Forwarded-For");
		if(xfHeader!=null) {
			return xfHeader.split(",")[0];
		}
		return request.getRemoteAddr();
	}

}
