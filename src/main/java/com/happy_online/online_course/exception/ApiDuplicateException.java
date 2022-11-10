package com.happy_online.online_course.exception;

public class ApiDuplicateException extends RuntimeException {
    public ApiDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiDuplicateException(String message) {
        super(message);
    }
}
