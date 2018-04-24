package com.rafakwolf.pontointeligente.api.repositories;

import com.rafakwolf.pontointeligente.api.entities.Empresa;
import com.rafakwolf.pontointeligente.api.entities.Funcionario;
import com.rafakwolf.pontointeligente.api.entities.Lancamento;
import com.rafakwolf.pontointeligente.api.enums.PerfilEnum;
import com.rafakwolf.pontointeligente.api.enums.TipoEnum;
import com.rafakwolf.pontointeligente.api.utils.PasswordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    private long funcionarioId;

    @Before
    public void setUp() {
        Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());
        Funcionario funcionario = this.funcionarioRepository.save(obterDadosFuncionario(empresa));
        this.funcionarioId = funcionario.getId();

        this.lancamentoRepository.save(obterDadosLancamentos(funcionario));
        this.lancamentoRepository.save(obterDadosLancamentos(funcionario));
    }

    @After
    public void tearDown() {
        this.lancamentoRepository.deleteAll();
    }

    @Test
    public void testandoBuscarLancamentosPorFuncionarioId(){
        List<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId);
        assertEquals(2,lancamentos.size());
    }

    @Test
    public void testandoBuscarLancamentosPorFuncionarioIdPaginado(){
        PageRequest page = PageRequest.of(0,10);
        Page<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId, page);
        assertEquals(2,lancamentos.getTotalElements());
    }


    private Lancamento obterDadosLancamentos(Funcionario funcionario) {
        Lancamento lancamento = new Lancamento();
        lancamento.setDescricao("lancamento teste");
        lancamento.setData(new Date());
        lancamento.setTipo(TipoEnum.INICIO_ALMOCO);
        lancamento.setFuncionario(funcionario);
        lancamento.setLocalizacao("maringa");
        return lancamento;
    }

    private Funcionario obterDadosFuncionario(Empresa empresa) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Fulano de tal");
        funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
        funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
        funcionario.setCpf("00945088956");
        funcionario.setEmail("email@teste.com.br");
        funcionario.setEmpresa(empresa);
        return funcionario;
    }


    private Empresa obterDadosEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("Empresa teste");
        empresa.setCnpj("123456789101112");
        return empresa;
    }

}
