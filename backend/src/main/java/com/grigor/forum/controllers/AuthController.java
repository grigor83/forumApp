package com.grigor.forum.controllers;

import com.grigor.forum.dto.LoginRequest;
import com.grigor.forum.dto.RegisterRequest;
import com.grigor.forum.dto.VerificationRequest;
import com.grigor.forum.dto.VerificationResponse;
import com.grigor.forum.model.User;
import com.grigor.forum.security.WAFService;
import com.grigor.forum.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final WAFService wafService;

    public AuthController(AuthService authService, WAFService wafService) {
        this.authService = authService;
        this.wafService = wafService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody RegisterRequest registerRequest,
                                          BindingResult result) {
        wafService.checkRequest(result);
        authService.registerUser(registerRequest);
        return ResponseEntity.ok().body(HttpStatus.ACCEPTED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Validated @RequestBody LoginRequest loginRequest,
                                       BindingResult result) {
        wafService.checkRequest(result);
        int userId = authService.loginUser(loginRequest);
        return ResponseEntity.ok().body(userId);
    }

    @PostMapping("/login-with-code")
    public ResponseEntity<VerificationResponse> loginWithCode(@Validated @RequestBody VerificationRequest verificationRequest,
                                                              BindingResult result) {
        wafService.checkRequest(result);
        return ResponseEntity.ok().body(authService.loginWithCode(verificationRequest));
    }
}
