package co.com.globalogic.usermanagement.service;

import co.com.globalogic.usermanagement.domain.exception.ErrorException;
import co.com.globalogic.usermanagement.domain.exception.ExceptionMessage;
import co.com.globalogic.usermanagement.domain.user.User;
import co.com.globalogic.usermanagement.domain.user.UserAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceImpTest {

    private JwtServiceImp jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImp();
    }

    @Test
    void generateAuthenticationAndSetToken() {
        User user = User.builder()
                .id("123")
                .name("John Doe")
                .email("john@example.com")
                .password("secret")
                .build();

        UserAuthentication auth = jwtService.generateUserAuthentication(user);

        assertNotNull(auth);
        assertNotNull(auth.getToken());
        assertNotNull(auth.getLastLogin());


        User decodedUser = jwtService.decodeUserToken(auth.getToken());

        assertEquals("123", decodedUser.getId());
        assertEquals("John Doe", decodedUser.getName());
        assertEquals("john@example.com", decodedUser.getEmail());
    }

    @Test
    void shouldThrowErrorWhenIsInvalidSignature() {
        // Token firmado con firma invalida
        String fakeToken = "eyJhbGciOiJIUzI1NiJ9." +
                "eyJzdWIiOiIxMjMiLCJuYW1lIjoiSm9obiBEb2UiLCJlbWFpbCI6ImpvaG5AZXhhbXBsZS5jb20ifQ." +
                "fake-signature";

        ErrorException ex = assertThrows(ErrorException.class,
                () -> jwtService.decodeUserToken(fakeToken));

        assertEquals(ExceptionMessage.INVALID_TOKEN, ex.getExceptionMessage());
    }

    @Test
    void shouldThrowErrorWhenIsInvalidToken() {
        String malformedToken = "not-a-jwt-token";

        ErrorException ex = assertThrows(ErrorException.class,
                () -> jwtService.decodeUserToken(malformedToken));

        assertEquals(ExceptionMessage.SERVER_ERROR, ex.getExceptionMessage());
    }
}
