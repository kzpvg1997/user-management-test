package co.com.globalogic.usermanagement.infrastructure.apigateway.userapi.dto;

import co.com.globalogic.usermanagement.domain.user.Phone;
import co.com.globalogic.usermanagement.domain.user.User;
import co.com.globalogic.usermanagement.infrastructure.apigateway.userapi.validator.PasswordValidatorGateway;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    //Optional
    private String name;

    @NotBlank(message = "Email can´t be empty")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Email should have a valid format (example: usuario@dominio.com)"
    )
    private String email;

    @PasswordValidatorGateway
    private String password;

    //Optional
    private List<PhoneDTO> phones;


    public User toModel(){
        return User.builder()
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .phones(getPhones())
                .build();
    }

    private List<Phone> getPhones(){
        List<Phone> phoneList = new ArrayList();
        if(this.phones != null && !this.phones.isEmpty()){
            phoneList = this.phones.stream().map(PhoneDTO::toModel).collect(Collectors.toList());
        }
        return phoneList;
    }
}
