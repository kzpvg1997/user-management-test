package co.com.globalogic.usermanagement.service;

import co.com.globalogic.usermanagement.domain.exception.ErrorException;
import co.com.globalogic.usermanagement.domain.exception.ExceptionMessage;
import co.com.globalogic.usermanagement.domain.user.User;
import co.com.globalogic.usermanagement.domain.user.UserAuthentication;
import co.com.globalogic.usermanagement.domain.user.gateway.UserAuthenticationGateway;
import co.com.globalogic.usermanagement.domain.user.gateway.UserGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImpTest {


    @Mock
    private UserGateway userGateway;
    @Mock
    private UserAuthenticationGateway authenticationGateway;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtServiceImp jwtServiceImp;

    @InjectMocks
    private UserServiceImp userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("plainPassword");
    }

    @Test
    void shouldAuthenticateAndReturnUser() {
        String token = "fakeToken";

        User decodedUser = new User();
        decodedUser.setEmail("test@example.com");

        UserAuthentication authentication = new UserAuthentication();
        authentication.setToken(token);

        when(jwtServiceImp.decodeUserToken(token)).thenReturn(decodedUser);
        when(userGateway.getUserByEmail(decodedUser.getEmail())).thenReturn(user);
        when(jwtServiceImp.generateUserAuthentication(user)).thenReturn(authentication);

        User result = userService.login(token);

        assertNotNull(result);
        assertEquals(token, result.getAuthentication().getToken());
        verify(authenticationGateway).authenticate(result);
    }

    @Test
    void shouldSingUpUserWhenNotExist() {
        String token = "fakeToken";
        String encodedPassword = "encodedPassword";
        when(userGateway.getUserByEmail(user.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);
        when(userGateway.createUser(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserAuthentication authentication = new UserAuthentication();
        authentication.setToken(token);

        when(jwtServiceImp.generateUserAuthentication(any(User.class))).thenReturn(authentication);

        User createdUser = userService.singUpUser(user);

        assertNotNull(createdUser);
        assertTrue(createdUser.getIsActive());
        assertEquals(encodedPassword, createdUser.getPassword());
        assertNotNull(createdUser.getCreatedAt());
        assertEquals(token, createdUser.getAuthentication().getToken());
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        when(userGateway.getUserByEmail(user.getEmail())).thenReturn(user);

        ErrorException ex = assertThrows(ErrorException.class, () -> userService.singUpUser(user));

        assertEquals(ExceptionMessage.USER_ALREADY_EXIST, ex.getExceptionMessage());
        verify(userGateway, never()).createUser(any());
    }
}
