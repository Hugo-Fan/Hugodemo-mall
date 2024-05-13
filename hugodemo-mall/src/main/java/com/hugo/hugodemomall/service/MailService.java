package com.hugo.hugodemomall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Executor executor;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    @Async
    public CompletableFuture<Void> sendPlainText(String receivers , String subject, String content){
        return CompletableFuture.runAsync(() ->{
            String mailFrom = "Hugo購物中心<excel840617@gmail.com>" ;
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(receivers);
            message.setSubject(subject);
            message.setText(content);
            message.setFrom(mailFrom);

            mailSender.send(message);
        },executor);
    }
}
