package com.happy_online.online_course.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ApiExceptionController {
    @ResponseBody
    @ExceptionHandler(value = {ApiDuplicateException.class})
    public ResponseEntity<Object> controlApiRequestException(ApiDuplicateException e) {
        ApiException apiException = new ApiException
                (e.getMessage());
//                        e,
//                        HttpStatus.CONFLICT,
//                        ZonedDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(apiException.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> controlNotFoundException(NotFoundException e) {
        NotFoundException notFoundException = new NotFoundException
                (e.getMessage());
//                        e,
//                        HttpStatus.CONFLICT,
//                        ZonedDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(notFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }
}
