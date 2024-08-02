package com.grigor.forum.exceptions;

public class AccountStatusException extends RuntimeException {
    public AccountStatusException() {
    }

    public AccountStatusException(String message) {
        super(message);
    }
}
