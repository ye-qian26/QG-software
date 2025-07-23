package com.qg.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String MY_EMAIL;

    public void sendSimpleEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MY_EMAIL); // 发件人邮箱
        message.setTo(to);                   // 收件人邮箱
        message.setSubject(subject);         // 邮件主题
        message.setText(content);            // 邮件内容

        mailSender.send(message);
        System.out.println("邮件发送成功");
    }
}

