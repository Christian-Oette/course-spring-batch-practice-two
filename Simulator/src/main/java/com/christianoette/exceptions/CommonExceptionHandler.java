package com.christianoette.exceptions;

import com.christianoette.controller.dtos.ErrorResponseDto;
import com.christianoette.services.strategies.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(value = ErrorResponseException.class)
    public ErrorResponseDto errorResponse() {
        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.message = "Processing error";
        return errorResponse;
    }

    @ExceptionHandler(value = Throwable.class)
    public ErrorResponseDto internalServerError(Throwable ex) {
        ErrorResponseDto errorResponse = new ErrorResponseDto();
        errorResponse.message = ex.getMessage();
        return errorResponse;
    }

}
