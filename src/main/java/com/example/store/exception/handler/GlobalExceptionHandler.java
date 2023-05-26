package com.example.store.exception.handler;

import com.example.store.dto.ResponseMessage;
import com.example.store.exception.BadRequestException;
import com.example.store.exception.GenricException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String EXCEPTION_OCCURED = "Exception Occured:: {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(GenricException.class)
    public ResponseEntity<ResponseMessage> handleGenricException(
            GenricException ex,
            HttpServletResponse httpServletResponse
    ) {
        httpServletResponse.setStatus(ex.getCode().value());
        LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
        return new ResponseEntity<>(new ResponseMessage(ex.getCode().value(), ex.getMessage()), ex.getCode());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseMessage> handleValidationException(BadRequestException ex) {
        LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
        return new ResponseEntity<>(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseMessage> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex
    ) {
        LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
        return new ResponseEntity<>(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseMessage> handleMethodNotAllowedException(
            HttpRequestMethodNotSupportedException ex
    ) {
        LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
        return new ResponseEntity<>(
                new ResponseMessage(HttpStatus.EXPECTATION_FAILED.value(), ex.getMessage()),
                HttpStatus.EXPECTATION_FAILED
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseMessage> httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
        return new ResponseEntity<>(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseMessage> handleMethodNotAllowedException(AccessDeniedException ex) {
        LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
        return new ResponseEntity<>(new ResponseMessage(HttpStatus.FORBIDDEN.value(), ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseMessage> handleValidationException(ValidationException ex) {
        LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
        return new ResponseEntity<>(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseMessage> handleUnauthorizedException(BadCredentialsException ex) {
        LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
        return new ResponseEntity<>(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Invalid Username Or Password"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseMessage> handleDuplicateException(DataIntegrityViolationException ex) {
        LOGGER.info(EXCEPTION_OCCURED, ex.getMessage());
        return new ResponseEntity<>(new ResponseMessage(HttpStatus.BAD_REQUEST.value(), "This ID Already Exist."), HttpStatus.BAD_REQUEST);
    }
}
