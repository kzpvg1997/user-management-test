package co.com.globalogic.usermanagement.domain.user.gateway;

import co.com.globalogic.usermanagement.domain.user.User;
import co.com.globalogic.usermanagement.domain.user.UserAuthentication;

public interface UserAuthenticationGateway {

    UserAuthentication authenticate(User user);
}
