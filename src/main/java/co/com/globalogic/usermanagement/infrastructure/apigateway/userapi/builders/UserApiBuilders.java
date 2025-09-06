package co.com.globalogic.usermanagement.infrastructure.apigateway.userapi.builders;

import co.com.globalogic.usermanagement.domain.user.User;
import co.com.globalogic.usermanagement.infrastructure.apigateway.userapi.dto.LoginResponseDTO;
import co.com.globalogic.usermanagement.infrastructure.apigateway.userapi.dto.SingUpResponseDTO;
import lombok.experimental.UtilityClass;
import java.time.LocalDateTime;

@UtilityClass
public class UserApiBuilders {

    public static SingUpResponseDTO buildSingUpResponse(User user) {

        return SingUpResponseDTO.builder()
                .id(user.getId())
                .created(LocalDateTime.now().toString())
                .isActive(user.getIsActive())
                .token(user.getAuthentication().getToken())
                .lastLogin(user.getAuthentication().getLastLogin().toString())
                .build();
    }

    public static LoginResponseDTO buildLoginResponse(User user) {
        return LoginResponseDTO.builder()
                .id(user.getId())
                .created(user.getCreatedAt())
                .lastLogin(user.getAuthentication().getLastLogin())
                .token(user.getAuthentication().getToken())
                .isActive(user.getIsActive())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phones(user.getPhones())
                .build();
    }
}
