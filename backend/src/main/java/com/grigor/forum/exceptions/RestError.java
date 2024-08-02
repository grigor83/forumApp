package com.grigor.forum.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestError {
    private int status;
    private String error;
    private String message;
    private String path = "uri=/error";

    public RestError(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }

}
