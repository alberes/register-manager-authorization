package io.github.alberes.register.manager.authorization.services;

import io.github.alberes.register.manager.authorization.controllers.dto.AddressViaCEPDto;
import io.github.alberes.register.manager.authorization.services.exceptions.ViaCEPErrorDecoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viacepapi", url = "${viacep.url}", configuration = ViaCEPErrorDecoder.class)
public interface ViaCEPService {

    @GetMapping("{zipCode}/json/")
    public AddressViaCEPDto getZipCodeViaCEP(@PathVariable String zipCode);
}
