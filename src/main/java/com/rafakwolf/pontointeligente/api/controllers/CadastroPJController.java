package com.rafakwolf.pontointeligente.api.controllers;

import com.rafakwolf.pontointeligente.api.dtos.CadastroPJDto;
import com.rafakwolf.pontointeligente.api.response.Response;
import com.rafakwolf.pontointeligente.api.services.EmpresaService;
import com.rafakwolf.pontointeligente.api.services.FuncionarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin(origins = "*")
public class CadastroPJController {

    private FuncionarioService funcionarioService;
    private EmpresaService empresaService;

    public CadastroPJController(FuncionarioService funcionarioService, EmpresaService empresaService) {
        this.funcionarioService = funcionarioService;
        this.empresaService = empresaService;
    }

    public ResponseEntity<Response<CadastroPJDto>> cadastrar(@Valid @RequestBody CadastroPJDto cadastroPJDto,
                                                             BindingResult result) {
        return null;
    }
}
