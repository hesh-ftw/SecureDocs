package com.secure.docs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String resetUrl){
        SimpleMailMessage message= new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset email from SECURE DOCS ");
        message.setText("Click the attached link to reset your password : "+ resetUrl);

        mailSender.send(message);

    }
}
