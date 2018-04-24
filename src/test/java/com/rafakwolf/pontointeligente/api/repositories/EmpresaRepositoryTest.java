package com.rafakwolf.pontointeligente.api.repositories;

import com.rafakwolf.pontointeligente.api.entities.Empresa;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmpresaRepositoryTest {

    @Autowired
    private EmpresaRepository empresaRepository;

    private static final String CNPJ = "43822963000183";

    @Before
    public void setUp(){
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("Empresa teste");
        empresa.setCnpj(CNPJ);
        this.empresaRepository.save(empresa);
    }

    @After
    public void tearDown(){
        this.empresaRepository.deleteAll();
    }

    @Test
    public void testandoBuscaEmpresaPorCnpj(){
        Empresa empresa = this.empresaRepository.findByCnpj(CNPJ);

        assertEquals(CNPJ, empresa.getCnpj());
    }
}
