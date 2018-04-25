package com.rafakwolf.pontointeligente.api.services;

import com.rafakwolf.pontointeligente.api.entities.Funcionario;

import java.util.Optional;

public interface FuncionarioService {
    Funcionario persistir(Funcionario funcionario);
    Optional<Funcionario> buscarPorCpf(String cpf);
    Optional<Funcionario> buscarPorEmail(String email);
    Optional<Funcionario> buscarPorId(Long id);
}
