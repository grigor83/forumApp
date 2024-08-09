package com.grigor.forum.exceptions;

//@ExceptionHandler annotated method can only handle the exceptions thrown by that particular class.
//However, if we want to handle any exception thrown throughout the application we can define a global
// exception handler class and annotate it with @ControllerAdvice.This annotation helps to integrate
// multiple exception handlers into a single global unit.
// Scenario: u user servisu se baca izuzetak RecordNotFoundException ako user pokusa da pronadje zapis u bazi
// na osnovu id-a koji ne postoji. Taj izuzetak ce uhvatiti ova klasa i pozvati odgovarajuci metod.
// U metodama ove klase se prvo kreira Log objekat pomocu poziva log servisa i njegovog metoda createLog(). Na taj
// nacin se Log objekat smjesta u bazu podataka.
// Ova klasa sluzi kao SIEM service iz projektnog zadatka.

import com.grigor.forum.enums.LogLevel;
import com.grigor.forum.services.LogService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.security.SignatureException;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler implements AuthenticationEntryPoint {

    private final LogService logService;

    public GlobalExceptionHandler(LogService logService) {
        this.logService = logService;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // This is invoked when user tries to access a secured REST resource without supplying any credentials
        //response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println("{ \"timestamp\": \"" + LocalDateTime.now() + "\", " +
                "\"status\": " + HttpServletResponse.SC_UNAUTHORIZED + ", " +
                "\"error\": \"Unauthorized access\", " +
                "\"message\": \"" + authException.getMessage() + "\", " +
                //"\"path\": \"" + request.getRequestURI() + "\" }");
                "\"path\": \" /error\" }");

        logService.saveLog(authException.getMessage(), String.valueOf(this.getClass()), LogLevel.WARNING.toString());
    }

    @ExceptionHandler(value = UsernameAlreadyExistsException.class)
    public ResponseEntity<?> handleDuplicateUsername(UsernameAlreadyExistsException ex, WebRequest request) {
        logService.saveLog(ex.getMessage(), String.valueOf(ex.getClass()), LogLevel.INFO.toString());

        RestError restError = new RestError(
                HttpStatus.CONFLICT.value(),
                "Username already exists",
                ex.getMessage()
                //request.getDescription(false)
        );
        return new ResponseEntity<>(restError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<RestError> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        logService.saveLog("User credentials are not valid!", String.valueOf(ex.getClass()),
                LogLevel.INFO.toString());

        RestError restError = new RestError(
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid credentials",
                "User credentials are not valid!"
        );
        return new ResponseEntity<>(restError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(VerificationCodeException.class)
    public ResponseEntity<RestError> handleVerificationCodeException(VerificationCodeException ex, WebRequest request) {
        logService.saveLog(ex.getMessage(), String.valueOf(ex.getClass()),
                LogLevel.INFO.toString());

        RestError restError = new RestError(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized access",
                "Verification code is not valid!"
        );
        return new ResponseEntity<>(restError, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(value = AccountStatusException.class)
    public ResponseEntity<?> handleAccountException(AccountStatusException ex, WebRequest request) {
        logService.saveLog("User account is either banned or unverified!", String.valueOf(ex.getClass()),
                LogLevel.INFO.toString());

        RestError restError = new RestError(
                HttpStatus.FORBIDDEN.value(),
                "Invalid account",
                "User account is either banned or unverified!"
        );
        return new ResponseEntity<>(restError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        logService.saveLog(ex.getMessage(), String.valueOf(ex.getClass()), LogLevel.WARNING.toString());

        RestError restError = new RestError(
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized access",
                ex.getMessage()
        );

        return new ResponseEntity<>(restError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex, WebRequest request) {
        logService.saveLog("Requested item is not found!", String.valueOf(ex.getClass()),
                LogLevel.ERROR.toString());

        RestError restError = new RestError(
                HttpStatus.NOT_FOUND.value(),
                "Item is not found",
                "Requested item is not found!"
        );
        return new ResponseEntity<>(restError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleXSSException(BadRequestException ex, WebRequest request) {
        logService.saveLog(ex.getMessage(), String.valueOf(ex.getClass()), LogLevel.WARNING.toString());

        RestError restError = new RestError(
                HttpStatus.FORBIDDEN.value(),
                "Malicious request",
                ex.getMessage()
        );
        return new ResponseEntity<>(restError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleInvalidFormat(HttpMessageNotReadableException ex) {
        logService.saveLog("Format of input is not valid!", String.valueOf(this.getClass()), LogLevel.INFO.toString());

        RestError restError = new RestError(
                HttpStatus.BAD_REQUEST.value(),
                "Invalid format input",
                ex.getMessage()
        );
        return new ResponseEntity<>(restError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex) {
        logService.saveLog("The JWT token has expired!", String.valueOf(this.getClass()), LogLevel.INFO.toString());

        RestError restError = new RestError(
                HttpStatus.FORBIDDEN.value(),
                "The JWT token has expired",
                ex.getMessage()
        );

        return new ResponseEntity<>(restError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<?> handleSignatureException(SignatureException ex) {
        logService.saveLog("The JWT signature is invalid!",
                String.valueOf(this.getClass()), LogLevel.INFO.toString());

        RestError restError = new RestError(
                HttpStatus.FORBIDDEN.value(),
                "The JWT signature is invalid!",
                ex.getMessage()
        );

        return new ResponseEntity<>(restError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        logService.saveLog("Unknown internal server error!",
                String.valueOf(this.getClass()), LogLevel.INFO.toString());

        RestError restError = new RestError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unknown internal server error",
                ex.getMessage()
        );

        return new ResponseEntity<>(restError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
