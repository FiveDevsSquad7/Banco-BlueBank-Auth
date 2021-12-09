package com.banco.bluebank.auth.core;

import com.banco.bluebank.auth.domain.Conta;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

public class AuthUser extends User {

    private static final long serialVersionUID = 1L;

    public AuthUser(Conta conta, Collection<? extends GrantedAuthority> authorities) {
        super(conta.getUsuario(), conta.getSenha(), authorities);
    }
}
