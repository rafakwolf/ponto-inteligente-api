package com.rafakwolf.pontointeligente.api.services;

import com.rafakwolf.pontointeligente.api.entities.Empresa;

import java.util.Optional;

public interface EmpresaService {
    Optional<Empresa> buscarPorCnpj(String cnpj);
    Empresa persistir(Empresa empresa);
}
