package com.halfgallon.withcon.global.exception;

public record ErrorResponse(
    int status,
    ErrorCode errorCode,
    String message
) {

}
