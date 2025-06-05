package io.github.alberes.register.manager.authorization.controllers;

import io.github.alberes.register.manager.authorization.controllers.dto.LoginDto;
import io.github.alberes.register.manager.authorization.controllers.dto.TokenDto;
import io.github.alberes.register.manager.authorization.controllers.exceptions.dto.StandardErrorDto;
import io.github.alberes.register.manager.authorization.services.LoginService;
import io.github.alberes.register.manager.authorization.utils.ControllerUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
@Tag(name = "Login")
public class LoginController {

    private final LoginService service;

    private final ControllerUtils utils;

    @PostMapping
    @Operation(summary = "Login user.", description = "Login user.", operationId = "login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login with success."),
            @ApiResponse(responseCode = "400", description = "Validation error.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "401", description = "Not Authorized.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginDto dto, HttpServletRequest request) throws Exception {
        TokenDto token = this.service.verify(dto, this.utils.origins(dto.username(), request));
        return ResponseEntity
                .status(HttpStatus.OK.value()).body(token);
    }

}