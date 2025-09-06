package co.com.globalogic.usermanagement.infrastructure.apigateway.exception;

import co.com.globalogic.usermanagement.domain.exception.Error;
import co.com.globalogic.usermanagement.domain.exception.ErrorException;
import co.com.globalogic.usermanagement.domain.exception.ExceptionMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler{

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<Error> handleErrorException(ErrorException ex, WebRequest request) {
        var error = Error.builder()
                .timestamp(formatDate(LocalDateTime.now()))
                .code(ex.getExceptionMessage().getCode())
                .detail(ex.getExceptionMessage().getDetail())
                .build();

        log.error(ex.getClass().getName(),ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        var error = Error.builder()
                .timestamp(formatDate(LocalDateTime.now()))
                .code(ExceptionMessage.BAD_REQUEST.getCode())
                .detail(errors.toString())
                .build();

        log.error(ex.getClass().getName(),ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Error> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv ->
                errors.put(cv.getPropertyPath().toString(), cv.getMessage()));

        var error = Error.builder()
                .timestamp(formatDate(LocalDateTime.now()))
                .code(ExceptionMessage.BAD_REQUEST.getCode())
                .detail(errors.toString())
                .build();

        log.error(ex.getClass().getName(),ex);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Otros errores no controlados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleGeneralException(Exception ex, WebRequest request) {
        var error = Error.builder()
                .timestamp(formatDate(LocalDateTime.now()))
                .code(ExceptionMessage.SERVER_ERROR.getCode())
                .detail(ExceptionMessage.SERVER_ERROR.getDetail())
                .build();

        log.error(ex.getClass().getName(),ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.format(formatter);
    }
}
