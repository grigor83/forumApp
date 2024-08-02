package com.grigor.forum.services;

import com.grigor.forum.dto.LoginRequest;
import com.grigor.forum.dto.RegisterRequest;
import com.grigor.forum.dto.VerificationRequest;
import com.grigor.forum.dto.VerificationResponse;
import com.grigor.forum.exceptions.*;
import com.grigor.forum.model.Permission;
import com.grigor.forum.model.User;
import com.grigor.forum.repository.UserRepository;
import com.grigor.forum.security.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }

    public void registerUser(RegisterRequest registerRequest) {
        if (this.userRepository.existsByUsername(registerRequest.getUsername()))
            throw new UsernameAlreadyExistsException("Username: " + registerRequest.getUsername() + " is already taken!");

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setEmail(registerRequest.getEmail());
        newUser.setRole(registerRequest.getRole());
        newUser.setPermissions(new ArrayList<Permission>());

        this.userRepository.save(newUser);
    }

    public Integer loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(BadCredentialsException::new);
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
            throw new BadCredentialsException();
        if (user.isBanned() || !user.isVerified())
            throw new AccountStatusException();

        Random rand = new Random();
        int code = rand.nextInt(50) + 1;
        user.setCode(code);
        //Ne trebam uraditi update, automatski je odradjeno u bazi
        //updateUser(u);
        this.emailService.sendVerificationCode(user.getEmail(), code);

        return user.getId();
    }

    public VerificationResponse loginWithCode (VerificationRequest verificationRequest) {
        User user = userRepository.findById(verificationRequest.getId())
                .orElseThrow(NotFoundException::new);

        if (user.getCode() == 0 || (user.getCode() != verificationRequest.getCode()))
            throw new VerificationCodeException("Verification code is not valid!");

        VerificationResponse response = new VerificationResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setPermissions(user.getPermissions());
        response.setVerified(user.isVerified());
        response.setBanned(user.isBanned());
        response.setRole(user.getRole());
        response.setToken(jwtService.generateToken(user));
        user.setCode(0);

        return response;
    }

}
