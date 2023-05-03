package com.telemart.service;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.telemart.config.JWTUtil;
import com.telemart.entity.PasswordHistoryEntity;
import com.telemart.entity.UserEntity;
import com.telemart.model.Captcha;
import com.telemart.model.ChangePasswordRequest;
import com.telemart.model.ForgotPasswordRequest;
import com.telemart.model.LoginRequest;
import com.telemart.model.LoginResponse;
import com.telemart.model.LoginResponseData;
import com.telemart.respository.PasswordHistoryRepository;
import com.telemart.respository.UserRepository;
import com.telemart.validator.EntityValidator;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService {
	@Value("${date.format}")
	private String dateFormat;
	@Value("${captcha}")
	private String captchaKey;
	@Value("${max.incorrect.login.attempts}")
	private int maxLoginAttempts;
	@Value("${max.forgotPassword.attempts}")
	private int maxForgotPasswordAttempts;
	@Value("${max.changePassword.attempts}")
	private int maxChangePasswordAttempts;

	@Autowired
	EncryptService encryptService;
	@Autowired
	EmailService emailService;
	
	@Autowired
	EntityValidator validator;
	@Autowired
	UserRepository userRepo;
	@Autowired
	JWTUtil jwtUtil;
	@Autowired
	ForgotPasswordAttemptService forgotPasswordAttemptService;
	@Autowired
	ChangePasswordAttemptService changePasswordAttemptService;
	
	@Autowired
	PasswordHistoryRepository PassHistoryRepo;
	private static final String EMAILID = "emailId";
	private static final String PASSWORD = "password";

	public LoginResponseData login(LoginRequest loginRequest, HttpServletRequest req) throws Exception {
		String decryptedEmailId = "";
		String decryptedPassword = "";
		LoginResponseData loginReponseData = new LoginResponseData();
		validateCaptcha(loginRequest.getCaptcha(), req);
//		boolean validate = validator.validateLogin(loginRequest);
		boolean validate = true;
		if (validate) {
			if (loginRequest.getEmailId() != null && loginRequest.getPassword() != null) {
//			decryptedEmailId = encryptService.decrptPwd(loginRequest.getEmailId(), EMAILID);
//			decryptedPassword = encryptService.decrptPwd(loginRequest.getPassword(), PASSWORD);
				decryptedEmailId = loginRequest.getEmailId();
				decryptedPassword = loginRequest.getPassword();
				UserEntity userEntity = userRepo.findByEmailId(decryptedEmailId);
				if (userEntity != null && userEntity.getIncorrectLoginAttempts() > 2) {
					long timeDifference = Duration.between(userEntity.getAccountLockedTime(), LocalDateTime.now())
							.toMinutes();
					if (timeDifference > 60) {
						loginReponseData = authenticateUser(decryptedEmailId, decryptedPassword, userEntity,
								loginReponseData);
					} else {
						loginReponseData.setAuthenticationCode(HttpStatus.UNAUTHORIZED);
						loginReponseData.setIncorrectLoginAttempts(userEntity.getIncorrectLoginAttempts());
						return loginReponseData;
					}
				} else if (userEntity != null) {
					loginReponseData = authenticateUser(decryptedEmailId, decryptedPassword, userEntity,
							loginReponseData);
				} else {
					throw new UsernameNotFoundException("Incorrect Username or Password");
				}
			} else {
				throw new Exception("Username/Password cannot be empty");
			}
		}
		return loginReponseData;
	}

	private LoginResponseData authenticateUser(String email, String password, UserEntity userEntity,
			LoginResponseData loginResponseData) {

		if (!email.isEmpty() && isAuthenticated(email, password, userEntity)) {
			userEntity = userRepo.findByEmailId(email);
			if (userEntity != null && !userEntity.isLoggedIn()) {
				String jwtToken = jwtUtil.generateToken(userEntity.getEmailId());
				loginResponseData = new ModelMapper().map(userEntity, LoginResponseData.class);
				loginResponseData.setJwtToken(jwtToken);
				loginResponseData.setAuthenticationCode(HttpStatus.OK);
				loginResponseData.setHRLoggedIn(true);
				userEntity.setLastLoginTime(LocalDateTime.now());
				userEntity.setLoggedIn(true);
				userEntity.setIncorrectLoginAttempts(0);
				userEntity.setLastLoginTime(LocalDateTime.now());
				userEntity.setGeneratedToken(jwtToken.replace("Bearer ", ""));
				userRepo.save(userEntity);
			} else {
				loginResponseData.setAuthenticationCode(HttpStatus.CONFLICT);
				loginResponseData.setJwtToken(userEntity.getGeneratedToken());
			}

		} else {
			loginResponseData.setAuthenticationCode(HttpStatus.UNAUTHORIZED);
			loginResponseData.setIncorrectLoginAttempts(userEntity.getIncorrectLoginAttempts());
		}
		return loginResponseData;
	}

	private boolean isAuthenticated(String email, String password, UserEntity userEntity) {
		System.out.println("isAuthenticated entry");
		boolean isAuthenticated = false;
		if (email != null && password != null && userEntity != null) {
			isAuthenticated = encryptService.isPasswordMatching(password, userEntity.getPassword());
			if (isAuthenticated) {
				userEntity.setIncorrectLoginAttempts(0);
			} else {
				if (password.equals(userEntity.getPassword())) {
					userEntity.setPassword(encryptService.encryptPassword(password));
					userRepo.save(userEntity);
					return true;
				}
				System.out.println("isAuthenticated else");
				userEntity.setIncorrectLoginAttempts(userEntity.getIncorrectLoginAttempts() + 1);
				if (userEntity.getIncorrectLoginAttempts() == maxLoginAttempts) {
					System.out.println("isAuthenticated set");
					userEntity.setAccountLockedTime(LocalDateTime.now());
				}

			}
			userRepo.save(userEntity);
		} else {
			isAuthenticated = false;
		}
		System.out.println("isAuthenticated exit");

		return isAuthenticated;
	}

	public String logout(String jwtToken) throws Exception {
		UserEntity userEntity = null;
		String emailId = jwtUtil.decodeEmailId(jwtToken);
		if (emailId != null && !emailId.isEmpty()) {
			userEntity = userRepo.findByEmailId(emailId);
			if (userEntity != null) {
				userEntity.setLoggedIn(false);
				userEntity.setExpiredToken(jwtToken);
				userRepo.save(userEntity);
				return "Success";
			} else {
				throw new Exception("User not found");
			}
		} else {
			throw new Exception("User not found");
		}
	}

	public String forgotPassword(ForgotPasswordRequest request, HttpServletRequest req) throws Exception {
		String status = null;
		validateCaptcha(request.getCaptcha(), req);
		boolean valid = validator.validateEmailId(request.getEmailId());
		if (valid) {
			int remainingAttempts = forgotPasswordAttempts(request.getEmailId().toLowerCase());
			if (remainingAttempts < 0)
				throw new Exception("You have Exhausted all the attempts, Please try again after 24Hrs");

			UserEntity userEntity = userRepo.findByEmailId(request.getEmailId().toLowerCase());
			String newPassword = getRandomPassword(8);
			status = savePassword(request.getEmailId().toLowerCase(), newPassword, true);
			
		} else {
			throw new Exception("Invalid credentials");
		}

		return status;

	}

	private String savePassword(String emailId, String newPassword, boolean forgotpassword) throws Exception {
		String status = "failure";
		UserEntity userEntity = userRepo.findByEmailId(emailId);
		if (userEntity == null) {
			throw new UsernameNotFoundException("Invalid Credentials " + emailId);
		}
		String securePwd = encryptService.encryptPassword(newPassword);
		PasswordHistoryEntity passwordHistoryEntity = PassHistoryRepo.findByEmailId(emailId);
		boolean pwdvalidate = (encryptService.isPasswordMatching(newPassword, passwordHistoryEntity.getPassword1())
				|| (passwordHistoryEntity.getPassword2() != null
						&& encryptService.isPasswordMatching(newPassword, passwordHistoryEntity.getPassword2()))
				|| (passwordHistoryEntity.getPassword3() != null
						&& encryptService.isPasswordMatching(newPassword, passwordHistoryEntity.getPassword3()))
				|| (passwordHistoryEntity.getPassword4() != null
						&& encryptService.isPasswordMatching(newPassword, passwordHistoryEntity.getPassword4()))
				|| (passwordHistoryEntity.getPassword5() != null
						&& encryptService.isPasswordMatching(newPassword, passwordHistoryEntity.getPassword5())));

		if (pwdvalidate) {
			throw new Exception("New Password should be different from last 3 passwords");
		} else {
			if (passwordHistoryEntity.getPassword2() == null && passwordHistoryEntity.getPassword3() == null
					&& passwordHistoryEntity.getPassword4() == null && passwordHistoryEntity.getPassword5() == null) {
				passwordHistoryEntity.setPassword2(securePwd);
				userEntity.setPassword(securePwd);
			} else if (passwordHistoryEntity.getPassword2() != null && passwordHistoryEntity.getPassword3() == null
					&& passwordHistoryEntity.getPassword4() == null && passwordHistoryEntity.getPassword5() == null) {
				passwordHistoryEntity.setPassword3(securePwd);
				userEntity.setPassword(securePwd);
			} else if (passwordHistoryEntity.getPassword3() != null && passwordHistoryEntity.getPassword4() == null
					&& passwordHistoryEntity.getPassword5() == null) {
				passwordHistoryEntity.setPassword4(securePwd);
				userEntity.setPassword(securePwd);
			} else if (passwordHistoryEntity.getPassword4() != null && passwordHistoryEntity.getPassword5() == null) {
				passwordHistoryEntity.setPassword4(securePwd);
				userEntity.setPassword(securePwd);
			} else {
				passwordHistoryEntity.setPassword1(passwordHistoryEntity.getPassword2());
				passwordHistoryEntity.setPassword2(passwordHistoryEntity.getPassword3());
				passwordHistoryEntity.setPassword3(passwordHistoryEntity.getPassword4());
				passwordHistoryEntity.setPassword4(passwordHistoryEntity.getPassword5());
				passwordHistoryEntity.setPassword5(securePwd);
				userEntity.setPassword(securePwd);
			}
		}
		userEntity.setNewuser(false);
		userEntity.setLoggedIn(false);
		userEntity.setIncorrectLoginAttempts(0);
		userEntity.setAccountLockedTime(null);
		if (forgotpassword) {
			emailService.forgotPasswordEmail(newPassword,userEntity.getEmailId());
			userEntity.setForgotPassword(true);
		} else {
			userEntity.setForgotPassword(false);
		}
		userRepo.save(userEntity);
		PassHistoryRepo.save(passwordHistoryEntity);
		status = "Password has been sent to your registered Email id";
		return status;
	}

	private String getRandomPassword(int n) {
		final char[] lowerCase = "abcdefghjkmnpqrstuvwxyz".toCharArray();
		final char[] upperCase = "ABCDEFGHJKLMNPRSTUVWXYZ".toCharArray();
		final char[] number = "023456789".toCharArray();
		final char[] symbols = "!@#$^&*~".toCharArray();
		final char[] allAllowed = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPRSTUVWXYZ023456789!@#$^&*~".toCharArray();
		Random random = new SecureRandom();
		StringBuilder password = new StringBuilder();
		for (int i = 0; i < n - 4; i++) {
			password.append(allAllowed[random.nextInt(allAllowed.length)]);
		}
		password.insert(random.nextInt(password.length()), lowerCase[random.nextInt(lowerCase.length)]);
		password.insert(random.nextInt(password.length()), upperCase[random.nextInt(upperCase.length)]);
		password.insert(random.nextInt(password.length()), number[random.nextInt(number.length)]);
		password.insert(random.nextInt(password.length()), symbols[random.nextInt(symbols.length)]);
		return password.toString();
	}

	private int forgotPasswordAttempts(String emailId) {
		try {
			UserEntity userEntity = userRepo.findByEmailId(emailId);
			int attemptsCount = userEntity.getForgotPasswordAttempts();
			forgotPasswordAttemptService.forgotPassword(emailId);
			if (attemptsCount >= maxForgotPasswordAttempts) {
				long timeDifference = Duration.between(userEntity.getForgotPasswordLockedTime(), LocalDateTime.now())
						.toMinutes();
				if (timeDifference > 1440) {
					userEntity.setForgotPasswordAttempts(1);
					attemptsCount = 1;
					userEntity.setForgotPasswordLockedTime(null);
					forgotPasswordAttemptService.unblock(emailId);
				}
			}
			if (attemptsCount == maxForgotPasswordAttempts) {
				userEntity.setForgotPasswordLockedTime(LocalDateTime.now());
			}
			userEntity.setForgotPasswordAttempts(attemptsCount);
			userRepo.save(userEntity);
			if (forgotPasswordAttemptService.isBlocked(emailId)) {
				userEntity.setForgotPasswordAttempts(maxForgotPasswordAttempts + 1);
				userEntity.setForgotPasswordLockedTime(LocalDateTime.now());
				userRepo.save(userEntity);
				return -1;
			}
			int remainingAttempts = maxForgotPasswordAttempts - attemptsCount;
			return remainingAttempts;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return maxForgotPasswordAttempts;
	}

	public void validateCaptcha(Captcha captcha, HttpServletRequest req) throws Exception {
		String decryptedA = "";
		String decryptedB = "";
		String decryptedC = "";
		boolean validCaptcha = false;
		int sum = 0;
		String dateTime = req.getHeader(captchaKey);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String sec = Long.toString(sdf.parse(dateTime).getTime());
		String[] extractDigits = new String[sec.length()];
		extractDigits = sec.split("");

		if (captcha.getC() == null || captcha.getC().isEmpty()) {
			throw new Exception("Kindly enter the valid captcha and try login again");
		}
		validCaptcha = validator.validCaptcha(captcha.getC());
		if (!validCaptcha) {
			throw new Exception("Kindly enter the valid captcha and try login again");
		}
		decryptedA = extractDigits[sec.length() - 2];
		decryptedB = extractDigits[sec.length() - 1];
		decryptedC = captcha.getC();
		sum = (Integer.parseInt(decryptedA) + 1) + (Integer.parseInt(decryptedB) + 1);
		if (sum != (Integer.parseInt(decryptedC))) {
			throw new Exception("Kindly enter the valid captcha and try login again");
		}
	}
	
	private int changePasswordAttempts(String emailId) {
		try {
			UserEntity userEntity = userRepo.findByEmailId(emailId);
			int attemptsCount = userEntity.getChangePasswordAttempts();
			changePasswordAttemptService.changePassword(emailId);
			if (attemptsCount >= maxChangePasswordAttempts) {
				long timeDifference = Duration.between(userEntity.getChangePasswordLockedTime(), LocalDateTime.now())
						.toMinutes();
				if (timeDifference > 1440) {
					userEntity.setChangePasswordAttempts(1);
					attemptsCount = 1;
					userEntity.setChangePasswordLockedTime(null);
					changePasswordAttemptService.unblock(emailId);
				}
			}
			if (attemptsCount == maxChangePasswordAttempts) {
				userEntity.setChangePasswordLockedTime(LocalDateTime.now());
			}
			userEntity.setChangePasswordAttempts(attemptsCount);
			userRepo.save(userEntity);
			if (changePasswordAttemptService.isBlocked(emailId)) {
				userEntity.setChangePasswordAttempts(maxChangePasswordAttempts + 1);
				userEntity.setChangePasswordLockedTime(LocalDateTime.now());
				userRepo.save(userEntity);
				return -1;
			}
			int remainingAttempts = maxChangePasswordAttempts - attemptsCount;
			return remainingAttempts;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return maxChangePasswordAttempts;
	}

	public String changePassword(ChangePasswordRequest changePasswordRequest, HttpServletRequest req) throws Exception {
		String status = null;
		String jwtToken = (String) req.getHeader("Authorization").replace("Bearer", "");
		String emailId = jwtUtil.decodeEmailId(jwtToken);
		System.out.println("emailId "+emailId);
		int remainingAttempts = changePasswordAttempts(emailId);
		if(remainingAttempts<0) {
			throw new Exception("You have Exhausted all the attempts, Please try again after 24Hrs");
		}
		
		if(!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
			throw new Exception("New password and confirm password must be same");
		}
		
		UserEntity userEntity = userRepo.findByEmailId(emailId);
		if(!encryptService.isPasswordMatching(changePasswordRequest.getOldPassword(),userEntity.getPassword())) {
			throw new Exception("Current password is not valid");
		}
		status = savePassword(emailId, changePasswordRequest.getNewPassword(), false);
		
		return status;
	}

}
