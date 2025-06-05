package io.github.alberes.register.manager.authorization.controllers;

import io.github.alberes.register.manager.authorization.constants.Constants;
import io.github.alberes.register.manager.authorization.controllers.dto.AddressDto;
import io.github.alberes.register.manager.authorization.controllers.dto.AddressReportDto;
import io.github.alberes.register.manager.authorization.controllers.dto.AddressViaCEPDto;
import io.github.alberes.register.manager.authorization.controllers.exceptions.dto.StandardErrorDto;
import io.github.alberes.register.manager.authorization.controllers.mappers.AddressMapper;
import io.github.alberes.register.manager.authorization.domains.Address;
import io.github.alberes.register.manager.authorization.domains.UserAccount;
import io.github.alberes.register.manager.authorization.services.AddressService;
import io.github.alberes.register.manager.authorization.services.ViaCEPService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users/{userId}/addresses")
@RequiredArgsConstructor
@Tag(name = "Address")
public class AddressController implements GenericController{

    private final AddressService service;

    private final ViaCEPService viaCEPService;

    private final AddressMapper mapper;

    @PostMapping
    @PreAuthorize(Constants.HAS_ROLE_ADMIN_USER)
    @Operation(summary = "Save address.", description = "Save address in database. User with profile ADMIN can access any users and other profiles can only access resources that belong to him.",
        operationId = "saveAddress")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Saved with success."),
            @ApiResponse(responseCode = "400", description = "Validation error.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Could not find user in database.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<Void> save(@PathVariable String userId, @RequestBody @Valid AddressDto dto){
        Address address = this.mapper.toEntity(dto);
        address.setUserAccount(new UserAccount());
        address.getUserAccount().setId(UUID.fromString(userId));
        address = this.service.save(address);
        return ResponseEntity.created(this.createURI("/{id}", address.getId().toString()))
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize(Constants.HAS_ROLE_ADMIN_USER)
    @Operation(summary = "Find address.", description = "Find address in database. User with profile ADMIN can access any users and other profiles can only access resources that belong to him.",
        operationId = "findAddress")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found address in database."),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Could not find address in database.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<AddressReportDto> find(@PathVariable String userId, @PathVariable String id){
        UUID addressId = UUID.fromString(id);
        Address address = this.service.find(UUID.fromString(userId), addressId);
        AddressReportDto dto = this.mapper.toDto(address);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize(Constants.HAS_ROLE_ADMIN_USER)
    @Operation(summary = "Update address.", description = "Update with success. User with profile ADMIN can access any users and other profiles can only access resources that belong to him.",
        operationId = "updateAddress")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Update address with success."),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Could not find address in database.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<Void> update(@PathVariable String userId, @PathVariable String id, @RequestBody @Valid AddressDto dto){
        Address address = this.mapper.toEntity(dto);
        UUID addressId = UUID.fromString(id);
        address.setId(addressId);
        address.setUserAccount(new UserAccount());
        address.getUserAccount().setId(UUID.fromString(userId));
        this.service.update(address);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(Constants.HAS_ROLE_ADMIN_USER)
    @Operation(summary = "Delete address.", description = "Delete address in database. User with profile ADMIN can access any users and other profiles can only access resources that belong to him.",
        operationId = "findAddresses")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Deleted address with success."),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Could not find user in database.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Could not find address in database.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<Void> delete(@PathVariable String userId, @PathVariable String id){
        UUID addressId = UUID.fromString(id);
        this.service.delete(UUID.fromString(userId), addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/zipcode/{zipcode}")
    @PreAuthorize(Constants.HAS_ROLE_ADMIN_USER)
    @Operation(summary = "Find address VIACEP.", description = "Find address in VIACEP service. User with profile ADMIN can access any users and other profiles can only access resources that belong to him.",
        operationId = "findAddressVIACEP")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found address.",
                    content = @Content(schema = @Schema(implementation = AddressViaCEPDto.class))),
            @ApiResponse(responseCode = "403", description = Constants.UNAUTHORIZED_MESSAGE,
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class))),
            @ApiResponse(responseCode = "404", description = "Could not find address VIACEP service.",
                    content = @Content(schema = @Schema(implementation = StandardErrorDto.class)))
    })
    public ResponseEntity<Object> getAddress(@PathVariable String userId, @PathVariable String zipcode,
        HttpServletRequest httpServletRequest){
        AddressViaCEPDto dto = this.viaCEPService.getZipCodeViaCEP(zipcode);
        if(dto.erro() != null && Constants.TRUE.equals(dto.erro())){
            StandardErrorDto standardErrorDto = new StandardErrorDto(
                    System.currentTimeMillis(),
                    HttpStatus.NOT_FOUND.value(),
                    Constants.NOT_FOUND,
                    Constants.ADDRESS_NOT_FOUND,
                    httpServletRequest.getRequestURI(),
                    List.of()
            );
            return ResponseEntity
                    .status(standardErrorDto.getStatus())
                    .body(standardErrorDto);
        }
        AddressDto addressDto = this.mapper.fromViaDtoToDto(dto);
        return ResponseEntity.ok(addressDto);
    }

    @GetMapping
    @PreAuthorize(Constants.HAS_ROLE_ADMIN_USER)
    @Operation(summary = "Find addresses.", description = "Find addresses in database. User with profile ADMIN can access any users and other profiles can only access resources that belong to him.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found addresses in database."),
            @ApiResponse(responseCode = "204", description = "Could not find addresses in database.",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    public ResponseEntity<Page<AddressReportDto>> page(
            @PathVariable String userId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "publicArea") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction
    ){
        Page<Address> pageAddress = this.service.findPage(UUID.fromString(userId), page, linesPerPage, orderBy, direction);

        if(pageAddress.getTotalElements() == 0){
            return ResponseEntity.noContent().build();
        }
        Page<AddressReportDto> pageReport = pageAddress
                .map(this.mapper::toDto);
        return ResponseEntity.ok(pageReport);
    }

}
