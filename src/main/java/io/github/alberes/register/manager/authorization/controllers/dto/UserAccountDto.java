package io.github.alberes.register.manager.authorization.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "UserAccount")
public record UserAccountDto(
        @NotBlank(message = "Obligatory field")
        @Size(min = 10, max = 100, message = "Fill this field with size between 10 and 100")
        String name,
        @NotBlank(message = "Obligatory field")
        @Email(message = "Type valid e-mail")
        String email,
        @NotBlank(message = "Obligatory field")
        @Size(min = 8, max = 20, message = "Fill this field with size between 8 and 20")
        String password,
        @NotBlank(message = "Obligatory field")
        @Size(min = 4, max = 20, message = "Fill this field with size between 4 and 20")
        String role) {
}
