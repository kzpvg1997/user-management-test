package co.com.globalogic.usermanagement.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthentication {

    private Long id;

    private LocalDateTime lastLogin;

    private String token;

}
