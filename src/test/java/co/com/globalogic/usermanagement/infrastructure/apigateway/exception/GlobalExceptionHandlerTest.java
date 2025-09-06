package co.com.globalogic.usermanagement.infrastructure.apigateway.exception;

import co.com.globalogic.usermanagement.domain.exception.ErrorException;
import co.com.globalogic.usermanagement.domain.exception.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleErrorException() {
        ErrorException ex = new ErrorException(ExceptionMessage.BAD_REQUEST);

        WebRequest request = mock(WebRequest.class);

        ResponseEntity<ErrorList> response = handler.handleErrorException(ex, request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(ExceptionMessage.BAD_REQUEST.getCode(), response.getBody().getErrors().get(0).getCode());
        assertEquals(ExceptionMessage.BAD_REQUEST.getDetail(), response.getBody().getErrors().get(0).getDetail());
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() throws NoSuchMethodException {
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "objectName");
        bindingResult.addError(new FieldError("objectName", "email", "must not be empty"));

        Method method = this.getClass().getDeclaredMethod("dummyMethod", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);


        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(methodParameter, bindingResult);
        ResponseEntity<ErrorList> response = handler.handleValidationExceptions(ex);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(ExceptionMessage.BAD_REQUEST.getCode(), response.getBody().getErrors().get(0).getCode());
        assertTrue(response.getBody().getErrors().get(0).getDetail().contains("email"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldHandleConstraintViolationException() {
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);

        Path path = mock(Path.class);
        when(path.toString()).thenReturn("username");

        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("must not be null");


        ConstraintViolationException ex =
                new ConstraintViolationException(Set.of(violation));

        ResponseEntity<ErrorList> response = handler.handleConstraintViolation(ex);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(ExceptionMessage.BAD_REQUEST.getCode(), response.getBody().getErrors().get(0).getCode());
        assertTrue(response.getBody().getErrors().get(0).getDetail().contains("username"));
    }

    @Test
    void shouldHandleGeneralException() {
        Exception ex = new NullPointerException("Something went wrong");

        WebRequest request = mock(WebRequest.class);

        ResponseEntity<ErrorList> response = handler.handleGeneralException(ex, request);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals(ExceptionMessage.SERVER_ERROR.getCode(), response.getBody().getErrors().get(0).getCode());
        assertEquals(ExceptionMessage.SERVER_ERROR.getDetail(), response.getBody().getErrors().get(0).getDetail());
        assertNotNull(response.getBody().getErrors().get(0).getTimestamp());
    }

    @SuppressWarnings("unused")
    private void dummyMethod(String email) {}
}
