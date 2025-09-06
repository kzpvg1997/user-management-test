package co.com.globalogic.usermanagement.domain.user.gateway;

import co.com.globalogic.usermanagement.domain.user.User;

public interface UserGateway {

    User createUser(User user);

    User getUserByEmail(String email);
}
