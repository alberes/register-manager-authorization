package io.github.alberes.register.manager.authorization.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "Address")
public record AddressDto(
        @NotBlank(message = "Obligatory field")
        @Size(min = 10, max = 100, message = "Fill this field with size between 10 and 100")
        String publicArea,
        @NotNull(message = "Obligatory field")
        @Min(value = 1, message = "Fill this field greater than 0")
        Integer number,
        String additionalAddress,
        @NotBlank(message = "Obligatory field")
        @Size(min = 5, max = 100, message = "Fill this field with size between 5 and 100")
        String neighborhood,
        @NotBlank(message = "Obligatory field")
        @Size(min = 5, max = 100, message = "Fill this field with size between 5 and 100")
        String city,
        @NotBlank(message = "Obligatory field")
        @Size(min = 2, max = 2, message = "Fill this field with size between 2 and 2")
        String state,
        @NotBlank(message = "Obligatory field")
        @Size(min = 8, max = 8, message = "Fill this field with size between 8 and 8")
        String zipCode) {
}
