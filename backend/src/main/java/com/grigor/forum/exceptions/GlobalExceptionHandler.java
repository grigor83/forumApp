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

import com.grigor.forum.model.LogLevel;
import com.grigor.forum.services.LogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final LogService logService;

    public GlobalExceptionHandler(LogService logService) {
        this.logService = logService;
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

}
