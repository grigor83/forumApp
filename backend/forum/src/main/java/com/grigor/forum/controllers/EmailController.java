package com.grigor.forum.controllers;

import com.grigor.forum.model.User;
import com.grigor.forum.services.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<String> sendVerificationCode(@RequestBody User user) {
        emailService.sendVerificationCode(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> sendVerificationEmail(@PathVariable Integer id, @RequestBody User user) {
        emailService.sendVerificationEmail(user);
        return ResponseEntity.ok().build();
    }
}
