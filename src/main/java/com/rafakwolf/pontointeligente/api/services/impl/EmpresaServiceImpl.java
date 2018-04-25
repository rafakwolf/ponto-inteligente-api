package com.rafakwolf.pontointeligente.api.services.impl;

import com.rafakwolf.pontointeligente.api.entities.Empresa;
import com.rafakwolf.pontointeligente.api.repositories.EmpresaRepository;
import com.rafakwolf.pontointeligente.api.services.EmpresaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmpresaServiceImpl implements EmpresaService {

    private static final Logger log = LoggerFactory.getLogger(EmpresaServiceImpl.class);
    private EmpresaRepository empresaRepository;

    public EmpresaServiceImpl(EmpresaRepository empresaRepository){
        this.empresaRepository = empresaRepository;
    }

    @Override
    public Optional<Empresa> buscarPorCnpj(String cnpj) {
        log.info("Buscando uma empresa para o cnpj: {}", cnpj);
        return Optional.ofNullable(this.empresaRepository.findByCnpj(cnpj));
    }

    @Override
    public Empresa persistir(Empresa empresa) {
        log.info("Persistindo uma empresa: {}", empresa);
        return this.empresaRepository.save(empresa);
    }
}
