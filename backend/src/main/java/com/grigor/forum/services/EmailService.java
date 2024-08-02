package com.grigor.forum.services;

import com.grigor.forum.model.User;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Transactional
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendVerificationEmail(String userEmail) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userEmail);
        mailMessage.setSubject("ForumApp");
        mailMessage.setText("Administrator je odobrio vaš nalog!");

        javaMailSender.send(mailMessage);
    }

    @Async
    public void sendVerificationCode(String userEmail, int code) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userEmail);
        mailMessage.setSubject("ForumApp");
        mailMessage.setText("Vaš verifikacioni kod je: " + code);

        javaMailSender.send(mailMessage);
    }
}
