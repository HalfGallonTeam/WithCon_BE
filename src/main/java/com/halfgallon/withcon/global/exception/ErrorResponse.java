package com.halfgallon.withcon.global.exception;

public record ErrorResponse(
    int status,
    String message
) {

}
