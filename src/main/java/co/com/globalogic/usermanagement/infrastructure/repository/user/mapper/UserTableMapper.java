package co.com.globalogic.usermanagement.infrastructure.repository.user.mapper;

import co.com.globalogic.usermanagement.domain.user.User;
import co.com.globalogic.usermanagement.infrastructure.repository.user.UserTable;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserTableMapper {

    User toModel (UserTable userTable);

    UserTable toTable (User user);

}
