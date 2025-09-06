package co.com.globalogic.usermanagement.infrastructure.repository.user;

import co.com.globalogic.usermanagement.domain.user.Phone;
import co.com.globalogic.usermanagement.domain.user.User;
import co.com.globalogic.usermanagement.infrastructure.repository.phone.PhoneTable;
import co.com.globalogic.usermanagement.infrastructure.repository.phone.mapper.PhoneTableMapper;
import co.com.globalogic.usermanagement.infrastructure.repository.user.mapper.UserTableMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserRepositoryAdapterTest {

    @Mock
    private UserTableRepository repository;

    @Mock
    private UserTableMapper userMapper;

    @Mock
    private PhoneTableMapper phoneMapper;

    @InjectMocks
    private UserRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUser() {

        User user = new User();
        user.setId("1");
        user.setEmail("test@example.com");
        Phone phone = new Phone();
        phone.setNumber("123456789");
        user.setPhones(List.of(phone));

        UserTable userTable = new UserTable();
        when(userMapper.toTable(user)).thenReturn(userTable);
        when(userMapper.toModel(userTable)).thenReturn(user);
        PhoneTable phoneTable = new PhoneTable();
        when(phoneMapper.toTable(phone)).thenReturn(phoneTable);

        UserRepositoryAdapter spyAdapter = spy(adapter);
        doReturn(user).when(spyAdapter).saveAndFlushData(any(UserTable.class));

        User result = spyAdapter.createUser(user);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userMapper).toTable(user);
        verify(phoneMapper).toTable(phone);
        verify(spyAdapter).saveAndFlushData(any(UserTable.class));
    }

    @Test
    void shouldReturnUserWhenFindByEmail() {

        UserTable userTable = new UserTable();
        userTable.setEmail("found@example.com");

        when(repository.findByEmail("found@example.com")).thenReturn(Optional.of(userTable));
        when(userMapper.toModel(userTable)).thenReturn(User.builder()
                        .email("found@example.com")
                .build());

        User result = adapter.getUserByEmail("found@example.com");

        assertNotNull(result);
        assertEquals("found@example.com", result.getEmail());
    }

    @Test
    void shouldReturnNullWhenUserNotFound() {
        when(repository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());
        User result = adapter.getUserByEmail("notfound@example.com");
        assertNull(result);
    }
}
