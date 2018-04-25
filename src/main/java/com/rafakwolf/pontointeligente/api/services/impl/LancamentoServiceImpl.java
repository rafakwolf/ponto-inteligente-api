package com.rafakwolf.pontointeligente.api.services.impl;

import com.rafakwolf.pontointeligente.api.entities.Lancamento;
import com.rafakwolf.pontointeligente.api.repositories.LancamentoRepository;
import com.rafakwolf.pontointeligente.api.services.LancamentoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LancamentoServiceImpl implements LancamentoService {

    private LancamentoRepository lancamentoRepository;

    public LancamentoServiceImpl(LancamentoRepository lancamentoRepository) {
        this.lancamentoRepository = lancamentoRepository;
    }

    @Override
    public Page<Lancamento> buscarPorFuncionarioId(Long funcionarioId, PageRequest pageRequest) {
        return this.lancamentoRepository.findByFuncionarioId(funcionarioId, pageRequest);
    }

    @Override
    public Optional<Lancamento> buscarPorId(Long id) {
        return this.lancamentoRepository.findById(id);
    }

    @Override
    public Lancamento persistir(Lancamento lancamento) {
        return this.lancamentoRepository.save(lancamento);
    }

    @Override
    public void remover(Long id) {
        this.lancamentoRepository.deleteById(id);
    }
}
