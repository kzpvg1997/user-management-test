package co.com.globalogic.usermanagement.infrastructure.repository.authentication;

import co.com.globalogic.usermanagement.domain.exception.ErrorException;
import co.com.globalogic.usermanagement.domain.exception.ExceptionMessage;
import co.com.globalogic.usermanagement.domain.user.User;
import co.com.globalogic.usermanagement.domain.user.UserAuthentication;
import co.com.globalogic.usermanagement.infrastructure.repository.autentication.UserAuthenticationRepositoryAdapter;
import co.com.globalogic.usermanagement.infrastructure.repository.autentication.UserAuthenticationTable;
import co.com.globalogic.usermanagement.infrastructure.repository.autentication.UserAuthenticationTableRepository;
import co.com.globalogic.usermanagement.infrastructure.repository.autentication.mapper.UserAuthenticationTableMapper;
import co.com.globalogic.usermanagement.infrastructure.repository.user.UserRepositoryAdapter;
import co.com.globalogic.usermanagement.infrastructure.repository.user.UserTable;
import co.com.globalogic.usermanagement.infrastructure.repository.user.mapper.UserTableMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserAuthenticationRepositoryAdapterTest {

    @Mock
    private UserAuthenticationTableRepository authenticationRepository;

    @Mock
    private UserAuthenticationTableMapper authenticationMapper;

    @Mock
    private UserTableMapper userMapper;

    @InjectMocks
    private UserAuthenticationRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveAuthenticationWhenUserAuthNotExist() {
        User user = new User();
        user.setId("1");
        UserAuthentication auth = new UserAuthentication();
        auth.setToken("token-123");
        auth.setLastLogin(LocalDateTime.now());
        user.setAuthentication(auth);

        UserAuthenticationTable authTable = new UserAuthenticationTable();
        when(authenticationMapper.toTable(auth)).thenReturn(authTable);
        when(userMapper.toTable(user)).thenReturn(new UserTable());
        when(authenticationRepository.findByUser("1")).thenReturn(Optional.empty());

        UserAuthenticationRepositoryAdapter spyAdapter = spy(adapter);
        doReturn(auth).when(spyAdapter).saveAndFlushData(authTable);

        UserAuthentication result = spyAdapter.authenticate(user);

        assertNotNull(result);
        assertEquals("token-123", result.getToken());
        verify(authenticationRepository).findByUser("1");
        verify(spyAdapter).saveAndFlushData(authTable);
    }

    @Test
    void shouldSaveAuthenticationWhenUserAuthExist() {
        User user = new User();
        user.setId("1");
        UserAuthentication auth = new UserAuthentication();
        auth.setToken("new-token");
        auth.setLastLogin(LocalDateTime.now());
        user.setAuthentication(auth);

        UserAuthenticationTable authTable = new UserAuthenticationTable();
        authTable.setToken("new-token");
        authTable.setLastLogin(auth.getLastLogin());
        when(authenticationMapper.toTable(auth)).thenReturn(authTable);
        when(userMapper.toTable(user)).thenReturn(new UserTable());
        when(authenticationRepository.findByUser("1")).thenReturn(Optional.of(authTable));
        when(authenticationRepository.updateAuthentication(
                eq("1"),
                eq(authTable.getToken()),
                eq(authTable.getLastLogin())
        )).thenReturn(1);
        when(authenticationMapper.toModel(authTable)).thenReturn(auth);

        UserAuthentication result = adapter.authenticate(user);

        assertNotNull(result);
        assertEquals("new-token", result.getToken());
        verify(authenticationRepository).updateAuthentication(
                eq("1"),
                eq(authTable.getToken()),
                eq(authTable.getLastLogin())
        );
    }

    @Test
    void shouldThrowExceptionWhenSaveAuthenticationFails() {
        User user = new User();
        user.setId("1");
        UserAuthentication auth = new UserAuthentication();
        auth.setToken("token-fail");
        auth.setLastLogin(LocalDateTime.now());
        user.setAuthentication(auth);

        UserAuthenticationTable authTable = new UserAuthenticationTable();
        when(authenticationMapper.toTable(auth)).thenReturn(authTable);
        when(userMapper.toTable(user)).thenReturn(new UserTable());
        when(authenticationRepository.findByUser("1")).thenReturn(Optional.of(authTable));
        when(authenticationRepository.updateAuthentication(
                anyString(),
                anyString(),
                any(LocalDateTime.class)
        )).thenReturn(0); // Simula fallo en update

        ErrorException exception = assertThrows(ErrorException.class, () -> adapter.authenticate(user));
        assertEquals(ExceptionMessage.DB_SAVE_ERROR.getDetail(), exception.getMessage());
    }
}
