package com.rafakwolf.pontointeligente.api.services.impl;

import com.rafakwolf.pontointeligente.api.entities.Funcionario;
import com.rafakwolf.pontointeligente.api.repositories.FuncionarioRepository;
import com.rafakwolf.pontointeligente.api.services.FuncionarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

    private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);

    private FuncionarioRepository funcionarioRepository;

    public FuncionarioServiceImpl(FuncionarioRepository funcionarioRepository){
        this.funcionarioRepository = funcionarioRepository;
    }

    @Override
    public Funcionario persistir(Funcionario funcionario) {
        log.info("Persistindo um funcionario: {}", funcionario);
        return this.funcionarioRepository.save(funcionario);
    }

    @Override
    public Optional<Funcionario> buscarPorCpf(String cpf) {
        log.info("Buscando um funcionario por cpf: {}", cpf);
        return Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
    }

    @Override
    public Optional<Funcionario> buscarPorEmail(String email) {
        log.info("Buscando um funcionario por e-mail: {}", email);
        return Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
    }

    @Override
    public Optional<Funcionario> buscarPorId(Long id) {
        log.info("Buscando um funcionario por ID: {}", id);
        return this.funcionarioRepository.findById(id);
    }
}
