package com.example.store.exception.handler

import com.example.store.dto.ResponseMessage
import com.example.store.exception.BadRequestException
import com.example.store.exception.GenricException
import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletResponse
import javax.xml.bind.ValidationException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(GenricException::class)
    fun handleGenricException(
        ex: GenricException?,
        httpServletResponse: HttpServletResponse?
    ): ResponseEntity<ResponseMessage> {
        if (ex != null) {
            ex.getCode()?.let {
                if (httpServletResponse != null) {
                    httpServletResponse.setStatus(it.value())
                }
            }
        }
        if (ex != null) {
            LOGGER.info(EXCEPTION_OCCURED, ex.message)
        }
        return ResponseEntity(ResponseMessage(ex?.getCode()?.value(), ex?.message), ex?.getCode())
    }

    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    fun handleValidationException(ex: BadRequestException?): ResponseEntity<ResponseMessage?> {
        if (ex != null) {
            LOGGER.info(EXCEPTION_OCCURED, ex.message)
        }
        return ResponseEntity(ResponseMessage(HttpStatus.BAD_REQUEST.value(), ex?.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException?
    ): ResponseEntity<ResponseMessage> {
        if (ex != null) {
            LOGGER.info(EXCEPTION_OCCURED, ex.message)
        }
        return ResponseEntity(ResponseMessage(HttpStatus.BAD_REQUEST.value(), ex?.message), HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(code = HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotAllowedException(
        ex: HttpRequestMethodNotSupportedException?
    ): ResponseEntity<ResponseMessage?> {
        if (ex != null) {
            LOGGER.info(EXCEPTION_OCCURED, ex.message)
        }
        return ResponseEntity(
            ResponseMessage(HttpStatus.EXPECTATION_FAILED.value(), ex?.message),
            HttpStatus.EXPECTATION_FAILED
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    fun httpMessageNotReadableException(ex: HttpMessageNotReadableException?): ResponseEntity<ResponseMessage> {
        if (ex != null) {
            LOGGER.info(EXCEPTION_OCCURED, ex.message)
        }
        return ResponseEntity(ResponseMessage(HttpStatus.BAD_REQUEST.value(), ex?.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AccessDeniedException::class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    fun handleMethodNotAllowedException(ex: AccessDeniedException?): ResponseEntity<ResponseMessage> {
        if (ex != null) {
            LOGGER.info(EXCEPTION_OCCURED, ex.message)
        }
        return ResponseEntity(ResponseMessage(HttpStatus.FORBIDDEN.value(), ex?.message), HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(ValidationException::class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    fun handleValidationException(ex: ValidationException?): ResponseEntity<ResponseMessage?> {
        if (ex != null) {
            LOGGER.info(EXCEPTION_OCCURED, ex.message)
        }
        return ResponseEntity(ResponseMessage(HttpStatus.BAD_REQUEST.value(), ex?.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BadCredentialsException::class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    fun handleUnauthorizedException(ex: BadCredentialsException?): ResponseEntity<ResponseMessage?> {
        if (ex != null) {
            LOGGER.info(EXCEPTION_OCCURED, ex.message)
        }
        return ResponseEntity(
            ResponseMessage(HttpStatus.BAD_REQUEST.value(), "Invalid Username Or Password"),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    fun handleDuplicateException(ex: DataIntegrityViolationException?): ResponseEntity<ResponseMessage?> {
        if (ex != null) {
            LOGGER.info(EXCEPTION_OCCURED, ex.message)
        }
        return ResponseEntity(
            ResponseMessage(HttpStatus.BAD_REQUEST.value(), "This ID Already Exist."),
            HttpStatus.BAD_REQUEST
        )
    }

    companion object {
        private val EXCEPTION_OCCURED: String = "Exception Occured:: {}"
        private val LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }
}