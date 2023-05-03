package com.telemart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

@Service
public class EmailService {
	
	@Value("${email.from.address}")
	private String emailFromAddress;
	@Autowired
	JavaMailSender javaMailSender;
	
	public void forgotPasswordEmail(String newPassword,String emailId) throws MessagingException {
		MimeMessage msg = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg,true);
		MimeMultipart multipart = new MimeMultipart();
		String content = "Your new Password is {PASSWORD}";
		content = content.replace("{PASSWORD}",newPassword );
		MimeBodyPart mbp = new MimeBodyPart();
		mbp.setContent(content,"text/html");
		multipart.addBodyPart(mbp);
		helper.setFrom(emailFromAddress);
		helper.setTo(emailId);
		helper.setSubject("New Password");
		helper.setText(content);
		msg.setContent(multipart);
		javaMailSender.send(msg);
		
	}

}
