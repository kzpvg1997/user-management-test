package co.com.globalogic.usermanagement.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Phone {

    private String number;
    private String cityCode;
    private String countryCode;
}
