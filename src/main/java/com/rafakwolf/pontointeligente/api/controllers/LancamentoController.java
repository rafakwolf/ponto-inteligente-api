package com.rafakwolf.pontointeligente.api.controllers;

import com.rafakwolf.pontointeligente.api.dtos.LancamentoDto;
import com.rafakwolf.pontointeligente.api.entities.Funcionario;
import com.rafakwolf.pontointeligente.api.entities.Lancamento;
import com.rafakwolf.pontointeligente.api.enums.TipoEnum;
import com.rafakwolf.pontointeligente.api.response.Response;
import com.rafakwolf.pontointeligente.api.services.FuncionarioService;
import com.rafakwolf.pontointeligente.api.services.LancamentoService;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoController {

    private static final Logger log = LoggerFactory.getLogger(LancamentoController.class);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private LancamentoService lancamentoService;
    private FuncionarioService funcionarioService;

    @Value("${paginacao.qtd_por_pagina}")
    private int qtdPorPagina;

    public LancamentoController(LancamentoService lancamentoService, FuncionarioService funcionarioService) {
        this.lancamentoService = lancamentoService;
        this.funcionarioService = funcionarioService;
    }

    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<Response<Page<LancamentoDto>>> listarPorFuncionarioId(
            @PathVariable("funcionarioId") Long funcionarioId,
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value="ord", defaultValue = "id") String ord,
            @RequestParam(value = "dir", defaultValue = "DESC") String dir){

        log.info("Buscando lançamentos por funcionario ID : {}, página: {}", funcionarioId, pag);

        Response<Page<LancamentoDto>> response = new Response<>();

        PageRequest pageRequest = PageRequest.of(pag, this.qtdPorPagina, Sort.Direction.valueOf(dir), ord);
        Page<Lancamento> lancamentos = this.lancamentoService.buscarPorFuncionarioId(funcionarioId, pageRequest);
        Page<LancamentoDto> lancamentoDtos = lancamentos.map(this::converterLancamentoDto);

        response.setData(lancamentoDtos);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<LancamentoDto>> listarPorId(@PathVariable("id") Long id){
        log.info("Buscando lançamento por ID: {}", id);

        Response<LancamentoDto> response = new Response<>();
        Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

        if (!lancamento.isPresent()){
            log.info("Lancamento com ID: {} não encontrado", id);
            response.getErrors().add("Lançamento não encontrado para o id " + id);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.converterLancamentoDto(lancamento.get()));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response<LancamentoDto>> adicionar(@Valid @RequestBody LancamentoDto lancamentoDto,
                                                             BindingResult result) throws ParseException {
        log.info("Adicionando lançamento: {}", lancamentoDto.toString());

        Response<LancamentoDto> response = new Response<>();
        validarFuncionario(lancamentoDto, result);
        Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);

        if (result.hasErrors()){
            log.error("Erro validando lançamento: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        lancamento = this.lancamentoService.persistir(lancamento);
        response.setData(this.converterLancamentoDto(lancamento));
        return  ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Response<LancamentoDto>> atualizar(@PathVariable("id") Long id,
                                                             @Valid @RequestBody LancamentoDto lancamentoDto,
                                                             BindingResult result) throws ParseException {
        log.info("Atualizando lançamento: {}", lancamentoDto.toString());

        Response<LancamentoDto> response = new Response<>();
        validarFuncionario(lancamentoDto, result);
        lancamentoDto.setId(Optional.of(id));
        Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);

        if (result.hasErrors()){
            log.error("Erro validando lançamento: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        lancamento = this.lancamentoService.persistir(lancamento);
        response.setData(this.converterLancamentoDto(lancamento));
        return  ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id){
        log.info("Removendo lançamento ID: {}", id);
        Response<String> response = new Response<>();

        Optional<Lancamento> lancamento = this.lancamentoService.buscarPorId(id);

         if (!lancamento.isPresent()){
             log.info("Erro ao remover, id inválido: {}", id);
             response.getErrors().add("Erro ao remover o lançamento. Registro não encontrado com ID: {}" + id);
             return ResponseEntity.badRequest().body(response);
         }

         this.lancamentoService.remover(id);
         return ResponseEntity.ok(new Response<>());
    }

    private Lancamento converterDtoParaLancamento(LancamentoDto lancamentoDto, BindingResult result) throws ParseException {
        Lancamento lancamento = new Lancamento();

        if (lancamentoDto.getId().isPresent()) {
            Optional<Lancamento> lanc = this.lancamentoService.buscarPorId(lancamentoDto.getId().get());
            if (lanc.isPresent()) {
                lancamento = lanc.get();
            } else {
                result.addError(new ObjectError("lancamento", "Lançamento não encontrado."));
            }
        } else {
            lancamento.setFuncionario(new Funcionario());
            lancamento.getFuncionario().setId(lancamentoDto.getFuncionarioId());
        }

        lancamento.setDescricao(lancamentoDto.getDescricao());
        lancamento.setLocalizacao(lancamentoDto.getLocalizacao());
        lancamento.setData(this.dateFormat.parse(lancamentoDto.getData()));


        if (EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo())) {
            lancamento.setTipo(TipoEnum.valueOf(lancamentoDto.getTipo()));
        } else {
            result.addError(new ObjectError("tipo", "Tipo inválido."));
        }

        return lancamento;
    }

    private void validarFuncionario(LancamentoDto lancamentoDto, BindingResult result) {
        if (lancamentoDto.getFuncionarioId() == null){
            result.addError(new ObjectError("funcionario", "Funcionário não informado."));
            return;
        }

        log.info("Validando funcionário ID: {}", lancamentoDto.getFuncionarioId());
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(lancamentoDto.getFuncionarioId());
        if(!funcionario.isPresent()){
            result.addError(new ObjectError("funcionario", "Funcionário não encontrado. ID inexistente."));
        }
    }

    private LancamentoDto converterLancamentoDto(Lancamento lanc) {
        LancamentoDto lancamentoDto = new LancamentoDto();
        lancamentoDto.setId(Optional.of(lanc.getId()));
        lancamentoDto.setData(this.dateFormat.format(lanc.getData()));
        lancamentoDto.setDescricao(lanc.getDescricao());
        lancamentoDto.setTipo(lanc.getTipo().toString());
        lancamentoDto.setFuncionarioId(lanc.getFuncionario().getId());
        lancamentoDto.setLocalizacao(lanc.getLocalizacao());
        return lancamentoDto;
    }
}
