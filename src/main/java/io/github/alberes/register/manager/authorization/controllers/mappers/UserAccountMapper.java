package io.github.alberes.register.manager.authorization.controllers.mappers;

import io.github.alberes.register.manager.authorization.controllers.dto.UserAccountDto;
import io.github.alberes.register.manager.authorization.controllers.dto.UserAccountProfileDto;
import io.github.alberes.register.manager.authorization.controllers.dto.UserAccountReportDto;
import io.github.alberes.register.manager.authorization.domains.UserAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    public UserAccount toEntity(UserAccountDto dto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
    @Mapping(source = "createdDate", target = "createdDate")
    public UserAccountReportDto toDto(UserAccount userAccount);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "roles", target = "profiles", qualifiedByName = "mapRoles")
    public UserAccountProfileDto toProfileDto(UserAccount userAccount);

    @Named("mapRoles")
    default Set<String> mapRoles(Set<String> roles) {
        return roles;
    }

}
