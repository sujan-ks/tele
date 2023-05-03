package com.telemart.controller;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.telemart.model.ChangePasswordRequest;
import com.telemart.model.ForgotPasswordRequest;
import com.telemart.model.LoginRequest;
import com.telemart.model.LoginResponse;
import com.telemart.model.LoginResponseData;
import com.telemart.model.ResponseModel;
import com.telemart.service.AuthenticationService;
import com.telemart.validator.EntityValidator;

@RestController
@RequestMapping("/")
public class AuthenticationController {

	@Autowired
	EntityValidator validator;
	@Autowired
	AuthenticationService authenticationService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest req)
			throws Exception {
		System.out.print("entry");
		HttpHeaders responseHeaders = new HttpHeaders();
		LoginResponseData loginResponseData = null;
		LoginResponse response = new LoginResponse();
		boolean validate = false;
		try {
			loginResponseData = authenticationService.login(loginRequest, req);
			response.setResponse(loginResponseData);
			System.out.print("end");
		} catch (UsernameNotFoundException userException) {
			userException.printStackTrace();
			System.out.print("end " + userException);
			response = new LoginResponse(null, "ERR_CD_001", "Incorrect Username or password");
			return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("end " + e);
			response = new LoginResponse(null, "ERR_CD_002", "Login failed, please try again.");
			return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
		}

		return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);

	}

	@PostMapping("/logoutUser")
	public ResponseEntity<ResponseModel> logoutUser(@RequestPart("jwtToken") String jwtToken) {
		System.out.print("logout entry");
		ResponseModel resModel = null;
		String response = null;
		try {
			response = authenticationService.logout(jwtToken);
		} catch (Exception e) {
			if (e.getMessage().length() < 80) {
				resModel = new ResponseModel(null, "ER_CD_003", e.getMessage());
			} else
				resModel = new ResponseModel(null, "ER_CD_004", "Failed to logout");
			return ResponseEntity.status(HttpStatus.OK).body(resModel);
		}
		resModel = new ResponseModel(response, null, null);
		return ResponseEntity.status(HttpStatus.OK).body(resModel);

	}

	@PostMapping("/forgotPassword")
	public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest req) {
		ResponseModel resModel = null;
		String response = null;
		try {
			response = authenticationService.forgotPassword(forgotPasswordRequest,req);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().length() < 80) {
				resModel = new ResponseModel(null, "ER_CD_003", e.getMessage());
			} else
				resModel = new ResponseModel(null, "ER_CD_004", "Invalid credentials");
			return ResponseEntity.status(HttpStatus.OK).body(resModel);
		}
		resModel = new ResponseModel(response, null, null);
		return ResponseEntity.status(HttpStatus.OK).body(resModel);
	}
	
	@PostMapping("/changePassword")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest req) {
		ResponseModel resModel = null;
		String response = null;
		try {
			response = authenticationService.changePassword(changePasswordRequest,req);
		} catch (Exception e) {
			e.printStackTrace();
			if (e.getMessage().length() < 80) {
				resModel = new ResponseModel(null, "ER_CD_003", e.getMessage());
			} else
				resModel = new ResponseModel(null, "ER_CD_004", "Invalid credentials");
			return ResponseEntity.status(HttpStatus.OK).body(resModel);
		}
		resModel = new ResponseModel(response, null, null);
		return ResponseEntity.status(HttpStatus.OK).body(resModel);


	}

}
