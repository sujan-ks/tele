package com.telemart.validator;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.telemart.model.LoginRequest;
import com.telemart.service.EncryptService;

@Component
public class EntityValidator {
	@Autowired
	EncryptService encryptService;
	@Value("${email.extentions}")
	private String[] validExtentions;

	private static final String EMAILID = "emailId";

	public boolean validCaptcha(String c) throws Exception {
		return validNumeric(c);
	}

	boolean validNumeric(String str) {
		try {
			if (str.length() <= 2) {
				Integer.parseInt(str);
				return true;
			}
		} catch (NumberFormatException e) {
			return false;
		}
		return false;
	}

	public boolean validateLogin(LoginRequest loginRequest) throws Exception {
		boolean validate = false;

		if (loginRequest != null) {
			if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()
					|| !isValidEncryption(loginRequest.getPassword())) {
				throw new Exception("password is not valid");
			}
			if (loginRequest.getEmailId() == null || loginRequest.getEmailId().isEmpty()
					|| !isValidEncryption(loginRequest.getEmailId())) {
				throw new Exception("EmailId is not valid");
			} else {
				String emailId = encryptService.decrptPwd(loginRequest.getEmailId(), EMAILID);
				String[] splitEmailId = emailId.split("@");

				if (!isValidEmail(emailId)
						|| !Arrays.stream(validExtentions).anyMatch(ext -> splitEmailId[1].endsWith(ext))
						|| !splitEmailId[1].equalsIgnoreCase("telemart.com")) {
					throw new Exception("EmailId is not valid");
				}

			}

		}

		return validate;

	}

	public static boolean isValidEncryption(String str) {
		// TODO Auto-generated method stub
		return ((str!=null)&&(!str.equals(""))&&(str.matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$")));		
	}
	
	public static boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@"+"(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		if(email==null)
			return false;
		return pat.matcher(email).matches();
	}

	public boolean validateEmailId(String emailId) throws Exception {
		boolean valid = false;
		if(emailId!=null&&!emailId.trim().isEmpty()) {
			if(!isValidEmail(emailId)) {
				throw new Exception("Email id is not valid");
			}
		}else {
			throw new Exception("Email id is not valid");
		}
		valid = true;
		return valid;
		
		
	}

}
