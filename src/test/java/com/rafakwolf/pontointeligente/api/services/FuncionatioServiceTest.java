package com.rafakwolf.pontointeligente.api.services;

import com.rafakwolf.pontointeligente.api.entities.Funcionario;
import com.rafakwolf.pontointeligente.api.repositories.FuncionarioRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionatioServiceTest {

    @MockBean
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private FuncionarioService funcionarioService;

    @Before
    public void setUp(){
        BDDMockito.given(this.funcionarioRepository.findByCpf(Mockito.anyString())).willReturn(new Funcionario());
        BDDMockito.given(this.funcionarioRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Funcionario()));
        BDDMockito.given(this.funcionarioRepository.findByEmail(Mockito.anyString())).willReturn(new Funcionario());
        BDDMockito.given(this.funcionarioRepository.save(Mockito.any(Funcionario.class))).willReturn(new Funcionario());
    }

    @Test
    public void testandoBuscarFuncionarioPorCpf(){
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorCpf("00945088956");
        assertTrue(funcionario.isPresent());
    }

    @Test
    public void testandoBuscarFuncionarioPorEmail(){
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorEmail("teste@email.com.br");
        assertTrue(funcionario.isPresent());
    }

    @Test
    public void testandoBuscarFuncionarioPorId(){
        Optional<Funcionario> funcionario = this.funcionarioService.buscarPorId(1L);
        assertTrue(funcionario.isPresent());
    }

    @Test
    public void testandoPersistirFuncionario(){
        Funcionario funcionario = this.funcionarioService.persistir(new Funcionario());
        assertNotNull(funcionario);
    }
}
