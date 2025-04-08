package com.ss.order.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSend;

    public EmailService(JavaMailSender mailSend) {
        this.mailSend = mailSend;
    }

    public void test(String email){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setText("This is a test.");
        msg.setSubject("Testing");
        mailSend.send(msg);
    }
}