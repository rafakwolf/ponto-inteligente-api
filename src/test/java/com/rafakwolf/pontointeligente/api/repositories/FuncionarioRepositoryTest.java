package com.rafakwolf.pontointeligente.api.repositories;

import com.rafakwolf.pontointeligente.api.entities.Empresa;
import com.rafakwolf.pontointeligente.api.entities.Funcionario;
import com.rafakwolf.pontointeligente.api.enums.PerfilEnum;
import com.rafakwolf.pontointeligente.api.utils.PasswordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTest {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    private static final String EMAIL = "teste@teste.com.br";
    private static final String CPF = "00945088725";

    @Before
    public void setUp() throws Exception {
        Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());
        this.funcionarioRepository.save(obterDadosFuncionario(empresa));
    }

    @After
    public void tearDown() {
        this.empresaRepository.deleteAll();
    }

    private Funcionario obterDadosFuncionario(Empresa empresa) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Fulano de tal");
        funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
        funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
        funcionario.setCpf(CPF);
        funcionario.setEmail(EMAIL);
        funcionario.setEmpresa(empresa);
        return funcionario;
    }

    private Empresa obterDadosEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("Empresa exemplo");
        empresa.setCnpj("12345678910111213");
        return empresa;
    }

    @Test
    public void testandoBuscaFuncionarioPorEmail() {
        Funcionario funcionario = funcionarioRepository.findByEmail(EMAIL);
        assertEquals(EMAIL, funcionario.getEmail());
    }

    @Test
    public void testandoBuscaFuncionarioPorCpf() {
        Funcionario funcionario = funcionarioRepository.findByCpf(CPF);
        assertEquals(CPF, funcionario.getCpf());
    }

    @Test
    public void testandoBuscaFuncionarioPorEmailECpf() {
        Funcionario funcionario = funcionarioRepository.findByCpfOrEmail(CPF, EMAIL);
        assertNotNull(funcionario);
    }

    @Test
    public void testandoBuscaFuncionarioPorEmailOuCpfComEmailInvalido() {
        Funcionario funcionario = funcionarioRepository.findByCpfOrEmail(CPF, "email@invalido.com");
        assertNotNull(funcionario);
    }

    @Test
    public void testandoBuscaFuncionarioPorEmailOuCpfComCpfInvalido() {
        Funcionario funcionario = funcionarioRepository.findByCpfOrEmail("0123456789", EMAIL);
        assertNotNull(funcionario);
    }
}
