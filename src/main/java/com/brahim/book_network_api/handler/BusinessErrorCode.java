package com.brahim.book_network_api.handler;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public enum BusinessErrorCode {
    NO_CODE(0, "No code", HttpStatus.NOT_IMPLEMENTED),
    INCORRECT_CURRENT_PASSWORD(300, "Incorrect current password", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_MISMATCH(301, "New password mismatch", HttpStatus.BAD_REQUEST),
    ACCOUNT_LOCKED(302, "User account is locked", HttpStatus.FORBIDDEN),
    ACCOUNT_DISABLED(303, "User account is disabled", HttpStatus.FORBIDDEN),
    BAD_CREDENTIALS(304, "Login and / or password is incorrect", HttpStatus.UNAUTHORIZED),
    ;

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    private BusinessErrorCode(int code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
