package co.com.globalogic.usermanagement.infrastructure.repository.user;

import co.com.globalogic.usermanagement.domain.user.User;
import co.com.globalogic.usermanagement.domain.user.gateway.UserGateway;
import co.com.globalogic.usermanagement.infrastructure.repository.helper.H2AdapterOperations;
import co.com.globalogic.usermanagement.infrastructure.repository.phone.mapper.PhoneTableMapper;
import co.com.globalogic.usermanagement.infrastructure.repository.user.mapper.UserTableMapper;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.stream.Collectors;


@Component
public class UserRepositoryAdapter extends H2AdapterOperations<User,UserTable, String> implements UserGateway {

    private final UserTableRepository repository;
    private final UserTableMapper mapper;
    private final PhoneTableMapper phoneMapper;


    public UserRepositoryAdapter(UserTableMapper mapper,UserTableRepository repository,PhoneTableMapper phoneMapper) {
        super(UserTable.class,mapper::toTable,mapper::toModel);
        this.repository = repository;
        this.mapper = mapper;
        this.phoneMapper = phoneMapper;
    }

    @Override
    @Transactional
    public User createUser(User user) {
        return saveAndFlushData(mapUserToTable(user));
    }

    @Override
    public User getUserByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toModel)
                .orElse(null);
    }

    private UserTable mapUserToTable(User user) {
        var userTable = mapper.toTable(user);
        var phonesTable = user.getPhones().stream()
                .map(phone -> {
                    var phoneTable = phoneMapper.toTable(phone);
                    phoneTable.setUser(userTable);
                    return phoneTable;
                })
                .collect(Collectors.toList());
        userTable.setPhones(phonesTable);
        return userTable;
    }
}
