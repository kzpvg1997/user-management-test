package co.com.globalogic.usermanagement.domain.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class Error {

    private String timestamp;
    private String code;
    private String detail;

}
