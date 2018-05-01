package com.rafakwolf.pontointeligente.api.controllers;


import com.rafakwolf.pontointeligente.api.dtos.EmpresaDto;
import com.rafakwolf.pontointeligente.api.entities.Empresa;
import com.rafakwolf.pontointeligente.api.response.Response;
import com.rafakwolf.pontointeligente.api.services.EmpresaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/empresas")
@CrossOrigin(origins = "*")
public class EmpresaController {

    private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);

    private EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<Response<EmpresaDto>> buscarPorCnpj(@PathVariable("cnpj") String cnpj){
        log.info("Buscando empresa por CNPJ: {}", cnpj);

        Response<EmpresaDto> response = new Response<>();

        Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cnpj);

        if (!empresa.isPresent()){
            log.info("Empresa por CNPJ: {} não encontrada.", cnpj);
            response.getErrors().add("Empresa não encontrada para o CNPJ "+cnpj);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.converterEmpresaDto(empresa.get()));
        return ResponseEntity.ok(response);
    }

    private EmpresaDto converterEmpresaDto(Empresa empresa) {
        EmpresaDto empresaDto = new EmpresaDto();
        empresaDto.setId(empresa.getId());
        empresaDto.setRazaoSocial(empresa.getRazaoSocial());
        empresaDto.setCnpj(empresa.getCnpj());
        return empresaDto;
    }
}
