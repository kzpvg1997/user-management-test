package co.com.globalogic.usermanagement.infrastructure.apigateway.userapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SingUpResponseDTO {

    private String id;
    private String created;
    private String lastLogin;
    private String token;
    private Boolean isActive;
}
