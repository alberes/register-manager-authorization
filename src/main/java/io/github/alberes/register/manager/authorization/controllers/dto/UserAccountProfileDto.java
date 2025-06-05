package io.github.alberes.register.manager.authorization.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(name = "UserAccountProfile")
public record UserAccountProfileDto(
        String id,
        String name,
        String email,
        Set<String> profiles) {
}
