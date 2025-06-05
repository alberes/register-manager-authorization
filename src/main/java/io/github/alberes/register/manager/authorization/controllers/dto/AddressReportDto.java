package io.github.alberes.register.manager.authorization.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "AddressReport")
public record AddressReportDto(
        String id,
        String publicArea,
        Integer number,
        String additionalAddress,
        String neighborhood,
        String city,
        String state,
        String zipCode,
        LocalDateTime createdDate) {
}
