package io.github.alberes.register.manager.authorization.controllers.mappers;

import io.github.alberes.register.manager.authorization.controllers.dto.AddressDto;
import io.github.alberes.register.manager.authorization.controllers.dto.AddressReportDto;
import io.github.alberes.register.manager.authorization.controllers.dto.AddressViaCEPDto;
import io.github.alberes.register.manager.authorization.domains.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(source = "publicArea", target = "publicArea")
    @Mapping(source = "number", target = "number")
    @Mapping(source = "additionalAddress", target = "additionalAddress")
    @Mapping(source = "neighborhood", target = "neighborhood")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "zipCode", target = "zipCode")
    public Address toEntity(AddressDto dto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "publicArea", target = "publicArea")
    @Mapping(source = "number", target = "number")
    @Mapping(source = "additionalAddress", target = "additionalAddress")
    @Mapping(source = "neighborhood", target = "neighborhood")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "zipCode", target = "zipCode")
    @Mapping(source = "createdDate", target = "createdDate")
    public AddressReportDto toDto(Address address);

    @Mapping(source = "cep", target = "zipCode")
    @Mapping(source = "logradouro", target = "publicArea")
    @Mapping(source = "complemento", target = "additionalAddress")
    @Mapping(source = "bairro", target = "neighborhood")
    @Mapping(source = "localidade", target = "city")
    @Mapping(source = "uf", target = "state")
    public AddressDto fromViaDtoToDto(AddressViaCEPDto dto);
}
