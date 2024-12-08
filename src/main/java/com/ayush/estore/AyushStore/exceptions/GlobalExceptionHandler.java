package com.ayush.estore.AyushStore.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ayush.estore.AyushStore.dtos.ApiResponseMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(
            ResourceNotFoundException resourceNotFoundException) {

        logger.error("Exception Handler");
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message(resourceNotFoundException.getMessage()).status(HttpStatus.NOT_FOUND)
                .success(false).build();

        return new ResponseEntity(responseMessage, HttpStatus.NOT_FOUND);
    }

    // MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumebtNotValidException(
            MethodArgumentNotValidException ex) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        Map<String, Object> response = new HashMap<>();
        allErrors.stream().forEach(objectError -> {
            String message = objectError.getDefaultMessage();
            String field = ((FieldError) objectError).getField();
            response.put(field, message);
        });

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> handlebadApiRequest(
            BadApiRequestException resourceNotFoundException) {

        logger.error("API ISSUE Handler");
        ApiResponseMessage responseMessage = ApiResponseMessage.builder()
                .message(resourceNotFoundException.getMessage()).status(HttpStatus.BAD_REQUEST)
                .success(false).build();

        return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
    }

}
