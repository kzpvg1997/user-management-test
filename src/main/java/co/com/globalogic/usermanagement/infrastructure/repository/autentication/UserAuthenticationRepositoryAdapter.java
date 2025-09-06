package co.com.globalogic.usermanagement.infrastructure.repository.autentication;

import co.com.globalogic.usermanagement.domain.exception.ErrorException;
import co.com.globalogic.usermanagement.domain.exception.ExceptionMessage;
import co.com.globalogic.usermanagement.domain.user.User;
import co.com.globalogic.usermanagement.domain.user.UserAuthentication;
import co.com.globalogic.usermanagement.domain.user.gateway.UserAuthenticationGateway;
import co.com.globalogic.usermanagement.infrastructure.repository.autentication.mapper.UserAuthenticationTableMapper;
import co.com.globalogic.usermanagement.infrastructure.repository.helper.H2AdapterOperations;
import co.com.globalogic.usermanagement.infrastructure.repository.user.mapper.UserTableMapper;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;


@Component
public class UserAuthenticationRepositoryAdapter extends H2AdapterOperations<UserAuthentication,
        UserAuthenticationTable, Long> implements UserAuthenticationGateway {

    private final UserAuthenticationTableRepository authenticationRepository;
    private final UserAuthenticationTableMapper authenticationMapper;
    private final UserTableMapper userMapper;

    public UserAuthenticationRepositoryAdapter(UserAuthenticationTableRepository authenticationRepository,
                                               UserAuthenticationTableMapper authenticationMapper,
                                               UserTableMapper userMapper) {
        super(UserAuthenticationTable.class, authenticationMapper::toTable, authenticationMapper::toModel);
        this.authenticationRepository = authenticationRepository;
        this.authenticationMapper = authenticationMapper;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserAuthentication authenticate(User user) {
        var authenticationData = authenticationMapper.toTable(user.getAuthentication());
        authenticationData.setUser(userMapper.toTable(user));
        return authenticationRepository.findByUser(user.getId())
                .map(existing -> validateAndUpdate(user, authenticationData))
                .map(authenticationMapper::toModel)
                .orElseGet(() -> saveAndFlushData(authenticationData));
    }

    private UserAuthenticationTable validateAndUpdate(User user, UserAuthenticationTable authenticationData) {
        return Optional.of(authenticationRepository.updateAuthentication(
                        user.getId(),
                        authenticationData.getToken(),
                        authenticationData.getLastLogin()
                ))
                .filter(updated -> updated > 0)
                .map(updated -> authenticationData)
                .orElseThrow(() -> new ErrorException(ExceptionMessage.DB_SAVE_ERROR));
    }
}
