package co.com.globalogic.usermanagement.infrastructure.apigateway.exception;

import co.com.globalogic.usermanagement.domain.exception.Error;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ErrorList {

    private List<Error> errors;
}
