package com.telemart.config;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.telemart.entity.UserEntity;
import com.telemart.model.TokenClassifier;
import com.telemart.respository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

@Component
public class JWTUtil {

	@Value("${token.timeout}")
	private long JWT_TOKEN_VALIDITY;
	@Autowired
	UserRepository userRepo;

	public String generateToken(String username) {

//		Claims claims= Jwts.claims();
//        claims.put("name", user.getUserName());
//        claims.put("email", user.getEmail());
//        claims.put("user_id", user.getUserId());
//        claims.setSubject("MY Blog");
//        claims.setIssuedAt(new Date());
//        
//        String token = Jwts.builder()
//                .setClaims(claims)
//                .signWith(SignatureAlgorithm.HS256, Constants.JWT_SECRET)
//                .compact();

		String secretKey = "telemartPortalSecretKey";
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

		String token = Jwts.builder().setId("softtekJWT").setSubject(username)
				.claim("authorities",
						grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
				.signWith(SignatureAlgorithm.HS256, secretKey.getBytes()).compact();
		System.out.println("token "+token);
		return "Bearer "+token;
	}
	
	public boolean validateHrToken(String emailId,String jwtToken) throws Exception{
		boolean validate = false;
		UserEntity userEntity = userRepo.findByEmailId(emailId);
		if(userEntity == null) {
			throw new Exception("HR not registered");
		}
		if(userEntity.getExpiredToken()!=null && userEntity.getExpiredToken().equals(jwtToken)) {
			throw new Exception("Your session has expired. Please login back.");
		}else {
			validate = true;
		}
		return validate;
	}
	
	public String decodeEmailId(String jwtToken) {
		String emailId = null;
		String[] split_string = jwtToken.split("\\.");
		String base64EncodedBody = split_string[1];
		Base64 base64Url = new Base64(true);
		try {
			String body = new String(base64Url.decode(base64EncodedBody));
			Gson gson = new Gson();
			TokenClassifier list = gson.fromJson(body,TokenClassifier.class);
			emailId = list.getSub();
			System.out.println("emailId1 "+emailId);
		}catch(Exception e) {
			e.printStackTrace();
			return "Invalid Token";
		}
		boolean val = true;
//		val = validateJwtTokenSig(split_string[0],split_string[1],split_string[2]);
		if(!val)
			emailId="Invalid Token";
		return emailId;
	}
	
	
	
	
	public boolean validateJwtTokenSig(String header,String payload,String signature) {
		try {
		String secretKey = "telemartPortalSecretKey";
		System.out.println("header "+header);
		System.out.println("payload "+payload);
		System.out.println("signature "+signature);
		String tokenWithoutSignature = header+"."+payload;
		SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
		DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(SignatureAlgorithm.HS256, secretKeySpec);
		System.out.println("tokenWithoutSignature "+tokenWithoutSignature);
		System.out.println("isValid "+validator.isValid(tokenWithoutSignature, signature));
		 byte[] str = TextCodec.BASE64URL.decode(signature);
		 
		if(!validator.isValid(tokenWithoutSignature, signature)) {
			System.out.println("not valid");
			return false;
		}else {
			return true;
		}	
		}catch(Exception e) {
			e.printStackTrace();	}
		return false;
	}	
}
