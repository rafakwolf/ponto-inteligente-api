package com.rafakwolf.pontointeligente.api.utils;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static junit.framework.TestCase.assertTrue;

public class PasswordUtilsTest {

    private static final String SENHA = "123456";
    private final BCryptPasswordEncoder bCryptEncoder = new BCryptPasswordEncoder();


    @Test
    public void testandoGeracaoHashSenha(){
        String hash = PasswordUtils.gerarBCrypt(SENHA);
        assertTrue(bCryptEncoder.matches(SENHA,hash));
    }
}
