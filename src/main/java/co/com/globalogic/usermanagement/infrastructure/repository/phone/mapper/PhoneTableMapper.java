package co.com.globalogic.usermanagement.infrastructure.repository.phone.mapper;

import co.com.globalogic.usermanagement.domain.user.Phone;
import co.com.globalogic.usermanagement.infrastructure.repository.phone.PhoneTable;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface PhoneTableMapper {

    Phone toModel (PhoneTable phoneTable);

    PhoneTable toTable (Phone phone);
}
