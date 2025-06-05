package io.github.alberes.register.manager.authorization.controllers;

import io.github.alberes.register.manager.authorization.constants.Constants;
import io.github.alberes.register.manager.authorization.controllers.dto.UserAccountDto;
import io.github.alberes.register.manager.authorization.controllers.dto.UserAccountProfileDto;
import io.github.alberes.register.manager.authorization.controllers.dto.UserAccountReportDto;
import io.github.alberes.register.manager.authorization.controllers.dto.UserAccountUpdateDto;
import io.github.alberes.register.manager.authorization.controllers.exceptions.dto.StandardErrorDto;
import io.github.alberes.register.manager.authorization.controllers.mappers.UserAccountMapper;
import io.github.alberes.register.manager.authorization.domains.UserAccount;
import io.github.alberes.register.manager.authorization.services.UserAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "UserAccount")
public class UserAccountController implements GenericController{

    private final UserAccountService service;

    private final UserAccountMapper mapper;

    @PostMapping
    @PreAuthorize(Constants.HAS_ROLE_ADMIN)
    @Operation(summary = "Save user.", description = "Save user in database. Only user with profile ADMIN can create user.",
        operationId = "saveUser")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Saved with success."),
            @ApiResponse(responseCode = "400", description = "Validation error.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "409", description = "There is a User with email.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<Void> save(@RequestBody @Valid UserAccountDto dto){
        UserAccount userAccount = this.mapper.toEntity(dto);
        userAccount.setRoles(new HashSet<String>());
        userAccount.getRoles().add(dto.role().toUpperCase());
        userAccount = this.service.save(userAccount);
        return ResponseEntity
                .created(this.createURI("/{id}", userAccount.getId().toString()))
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize(Constants.HAS_ROLE_ADMIN_USER)
    @Operation(summary = "Find user.", description = "Find user in database. User with profile ADMIN can access any users and other profiles can only access resources that belong to him.",
        operationId = "findUser")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found user in database."),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Could not find user in database.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<UserAccountReportDto> find(@PathVariable String id){
        UUID userId = UUID.fromString(id);
        UserAccount userAccount = this.service.find(userId);
        userAccount.setPassword(null);
        UserAccountReportDto dto = this.mapper.toDto(userAccount);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize(Constants.HAS_ROLE_ADMIN_USER)
    @Operation(summary = "Update user.", description = "Update with success. User with profile ADMIN can access any users and other profiles can only access resources that belong to him.",
        operationId = "updateUser")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Update name with success."),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Could not find user in database.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody @Valid UserAccountUpdateDto dto){
        UUID userId = UUID.fromString(id);
        UserAccount userAccount = new UserAccount();
        userAccount.setId(userId);
        userAccount.setName(dto.name());
        this.service.update(userAccount);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(Constants.HAS_ROLE_ADMIN_USER)
    @Operation(summary = "Delete user.", description = "Delete with success. User with profile ADMIN can access any users and other profiles can only access resources that belong to him.",
        operationId = "deleteUser")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Delete user in database."),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Could not find user in database.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<Void> delete(@PathVariable String id){
        UUID userId = UUID.fromString(id);
        this.service.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/all")
    @PreAuthorize(Constants.HAS_ROLE_ADMIN_USER)
    @Operation(summary = "Find users.", description = "Find users in database. User with profile ADMIN can access any users and other profiles can only access resources that belong to him.",
        operationId = "findUsers")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Find users in database."),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "204", description = "Could not find users in database.",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Page<UserAccountReportDto>> page(
            @PathVariable String id,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ){
        Page<UserAccount> pageUsers = this.service.findPage(UUID.fromString(id), page, linesPerPage, orderBy, direction);

        if(pageUsers.getTotalElements() == 0){
            return ResponseEntity.noContent().build();
        }
        Page<UserAccountReportDto> pageReport = pageUsers
                .map(u -> new UserAccountReportDto(
                        u.getId().toString(), u.getName(), u.getEmail(), u.getLastModifiedDate(), u.getCreatedDate()));
        return ResponseEntity.ok(pageReport);
    }

    @GetMapping("/authenticated")
    @PreAuthorize(Constants.HAS_ROLE_ADMIN_USER)
    @Operation(summary = "Find profile user.", description = "Find profile user in database. User with profile ADMIN can access any users and other profiles can only access resources that belong to him.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found user in database."),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<UserAccountProfileDto> userAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAccount userAccount = (UserAccount)authentication.getPrincipal();
        UserAccountProfileDto userAccountProfileDto = this.mapper.toProfileDto(userAccount);
        return ResponseEntity.ok(userAccountProfileDto);
    }

}
