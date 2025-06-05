package io.github.alberes.register.manager.authorization.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Token")
public record TokenDto(String token, Long expirationDate) {
}