package io.github.alberes.register.manager.authorization.controllers.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AddressViaCEP")
public record AddressViaCEPDto(String cep,
                               String logradouro,
                               String complemento,
                               String unidade,
                               String bairro,
                               String localidade,
                               String uf,
                               String estado,
                               String regiao,
                               String ibge,
                               String gia,
                               String ddd,
                               String siafi,
                               String erro) {
}
