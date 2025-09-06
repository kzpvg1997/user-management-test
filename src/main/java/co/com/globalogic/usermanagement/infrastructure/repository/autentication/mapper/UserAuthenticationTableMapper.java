package co.com.globalogic.usermanagement.infrastructure.repository.autentication.mapper;

import co.com.globalogic.usermanagement.domain.user.UserAuthentication;
import co.com.globalogic.usermanagement.infrastructure.repository.autentication.UserAuthenticationTable;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserAuthenticationTableMapper {

    UserAuthentication toModel (UserAuthenticationTable userAuthenticationTable);

    UserAuthenticationTable toTable (UserAuthentication userAuthentication);

}
