package co.com.globalogic.usermanagement.service;

import co.com.globalogic.usermanagement.domain.exception.ErrorException;
import co.com.globalogic.usermanagement.domain.exception.ExceptionMessage;
import co.com.globalogic.usermanagement.domain.user.User;
import co.com.globalogic.usermanagement.domain.user.gateway.UserAuthenticationGateway;
import co.com.globalogic.usermanagement.domain.user.gateway.UserGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class UserServiceImp {

    private final UserGateway userGateway;
    private final UserAuthenticationGateway authenticationGateway;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImp jwtServiceImp;

    public User login(String token){
        var userToken = jwtServiceImp.decodeUserToken(token);
        var userRegistered = userGateway.getUserByEmail(userToken.getEmail());
        var authentication = jwtServiceImp.generateUserAuthentication(userRegistered);
        userRegistered.setAuthentication(authentication);
        authenticationGateway.authenticate(userRegistered);

        return userRegistered;
    }

    public User singUpUser(User user){
        var userExist = userGateway.getUserByEmail(user.getEmail());
        if (userExist == null){
            user.setIsActive(Boolean.TRUE);
            // Encrypt Password
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedAt(LocalDateTime.now());
            var userCreated = userGateway.createUser(user);
            var authentication = jwtServiceImp.generateUserAuthentication(userCreated);
            userCreated.setAuthentication(authentication);
            return userCreated;
        }
        throw new ErrorException(ExceptionMessage.USER_ALREADY_EXIST);
    }
}
