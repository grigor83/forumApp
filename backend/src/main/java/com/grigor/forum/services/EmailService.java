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
    private final UserService userService;

    public EmailService(JavaMailSender javaMailSender, UserService userService) {
        this.javaMailSender = javaMailSender;
        this.userService = userService;
    }

    @Async
    public void sendVerificationEmail(User user) {
        user.setVerified(true);
        userService.updateUser(user);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("ForumApp");
        mailMessage.setText("Administrator je odobrio vaš nalog!");

        javaMailSender.send(mailMessage);
    }

    @Async
    public void sendVerificationCode(User user) {
        Random rand = new Random();
        int code = rand.nextInt(50) + 1;
        user.setCode(code);
        userService.updateUser(user);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("ForumApp");
        mailMessage.setText("Vaš verifikacioni kod je: " + code);

        javaMailSender.send(mailMessage);
    }
}
