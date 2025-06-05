package io.github.alberes.register.manager.authorization.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "Login")
public record LoginDto(
        @NotBlank(message = "Obligatory field")
        @Email(message = "Type valid e-mail")
        String username,
        @NotBlank(message = "Obligatory field")
        @Size(min = 8, max = 20, message = "Fill this field with size between 8 and 20")
        String password) {
}
