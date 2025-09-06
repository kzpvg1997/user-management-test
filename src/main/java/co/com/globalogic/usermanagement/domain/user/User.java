package co.com.globalogic.usermanagement.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String id;
    private String name;
    private String email;
    private String password;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private List<Phone> phones;
    private UserAuthentication authentication;
}
