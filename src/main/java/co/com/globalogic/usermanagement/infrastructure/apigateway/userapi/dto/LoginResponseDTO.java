package co.com.globalogic.usermanagement.infrastructure.apigateway.userapi.dto;

import co.com.globalogic.usermanagement.domain.user.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    private String id;
    private LocalDateTime created;
    private LocalDateTime lastLogin;
    private Boolean isActive;
    private String token;
    private String name;
    private String email;
    private String password;
    private List<Phone> phones;
}
