package co.com.globalogic.usermanagement.infrastructure.apigateway.userapi.dto;

import co.com.globalogic.usermanagement.domain.user.Phone;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class PhoneDTO {

    @NotNull
    private String number;
    @NotNull
    private String cityCode;
    @NotNull
    private String countryCode;

    public Phone toModel(){
        return Phone.builder()
                .number(this.number)
                .cityCode(this.cityCode)
                .countryCode(this.countryCode)
                .build();
    }
}
