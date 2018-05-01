package com.rafakwolf.pontointeligente.api.controllers;

import com.rafakwolf.pontointeligente.api.dtos.CadastroPFDto;
import com.rafakwolf.pontointeligente.api.entities.Empresa;
import com.rafakwolf.pontointeligente.api.entities.Funcionario;
import com.rafakwolf.pontointeligente.api.enums.PerfilEnum;
import com.rafakwolf.pontointeligente.api.response.Response;
import com.rafakwolf.pontointeligente.api.services.EmpresaService;
import com.rafakwolf.pontointeligente.api.services.FuncionarioService;
import com.rafakwolf.pontointeligente.api.utils.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins = "*")
public class CadastroPFController {

    private static final Logger log = LoggerFactory.getLogger(CadastroPFController.class);

    private FuncionarioService funcionarioService;
    private EmpresaService empresaService;

    public CadastroPFController(FuncionarioService funcionarioService, EmpresaService empresaService) {
        this.funcionarioService = funcionarioService;
        this.empresaService = empresaService;
    }

    @PostMapping
    public ResponseEntity<Response<CadastroPFDto>> cadastrar(@Valid @RequestBody CadastroPFDto cadastroPFDto,
                                                             BindingResult result) {
        log.info("Cadastrando PF: {}", cadastroPFDto.toString());

        Response<CadastroPFDto> response = new Response<>();

        validarDadosExistentes(cadastroPFDto, result);

        Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPFDto);

        if (result.hasErrors()) {
            log.error("Erro validando dados de cadastro PF: {}", result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
        empresa.ifPresent(funcionario::setEmpresa);

        this.funcionarioService.persistir(funcionario);

        response.setData(this.converterCadastroPFDto(funcionario));
        return ResponseEntity.ok(response);
    }

    private void validarDadosExistentes(CadastroPFDto cadastroPFDto, BindingResult result) {
        Optional<Empresa> empresa = this.empresaService.buscarPorCnpj(cadastroPFDto.getCnpj());
        if (!empresa.isPresent()){
            result.addError(new ObjectError("empresa", "Empresa não cadastrada."));
        }

        this.funcionarioService.buscarPorCpf(cadastroPFDto.getCpf())
                .ifPresent(func -> result.addError(
                        new ObjectError("funcionario", "CPF já existente.")));

        this.funcionarioService.buscarPorEmail(cadastroPFDto.getEmail())
                .ifPresent(func -> result.addError(
                        new ObjectError("funcionario", "Email já existente.")));
    }

    private Funcionario converterDtoParaFuncionario(CadastroPFDto cadastroPFDto) {
        Funcionario funcionario = new Funcionario();

        funcionario.setNome(cadastroPFDto.getNome());
        funcionario.setEmail(cadastroPFDto.getEmail());
        funcionario.setCpf(cadastroPFDto.getCpf());
        funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
        funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPFDto.getSenha()));

        cadastroPFDto.getQtdHorasAlmoco()
                .ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));


        cadastroPFDto.getQtdHorasTrabalhoDia()
                .ifPresent(qtdHorasTrabalhoDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhoDia)));


        cadastroPFDto.getValorHora()
                .ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

        return funcionario;
    }

    private CadastroPFDto converterCadastroPFDto(Funcionario funcionario) {
        CadastroPFDto cadastroPFDto = new CadastroPFDto();

        cadastroPFDto.setId(funcionario.getId());
        cadastroPFDto.setNome(funcionario.getNome());
        cadastroPFDto.setEmail(funcionario.getEmail());
        cadastroPFDto.setCpf(funcionario.getCpf());
        cadastroPFDto.setCnpj(funcionario.getEmpresa().getCnpj());

        funcionario.getQtdHorasAlmocoOpt()
                .ifPresent(qtdHorasAlmoco -> cadastroPFDto.setQtdHorasAlmoco(
                        Optional.of(Float.toString(qtdHorasAlmoco))));


        funcionario.getQtdHorasTrabalhoDiaOpt()
                .ifPresent(qtdHorasTrabalhoDia -> cadastroPFDto.setQtdHorasTrabalhoDia(
                        Optional.of(Float.toString(qtdHorasTrabalhoDia))));


        funcionario.getValorHoraOpt()
                .ifPresent(valorHora -> cadastroPFDto.setValorHora(Optional.of(valorHora.toString())));


        return cadastroPFDto;
    }
}
