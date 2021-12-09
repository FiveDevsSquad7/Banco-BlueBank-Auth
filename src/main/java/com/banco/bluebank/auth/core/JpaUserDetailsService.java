package com.banco.bluebank.auth.core;

import com.banco.bluebank.auth.domain.Conta;
import com.banco.bluebank.auth.domain.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private DigitoVerificadorLuhn dv;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("username: "+username);

        if(username.length()<2){
            throw new UsernameNotFoundException("Número de conta inválida");
        } else if(!dv.verificaDigitoVerificador(username)) {
            throw new UsernameNotFoundException("Número de conta inválida");
        } else {
            username = username.substring(0, username.length() - 1 );
        }

        System.out.println("username: "+username);
        Conta conta = contaRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> new UsernameNotFoundException("Conta não encontrada"));

        if(conta.getSenha()==null || conta.getSenha().isEmpty()) {
            throw new UsernameNotFoundException("Conta com senha indefinida");
        }

//        return new AuthUser(conta, getAuthorities(conta));
        return new AuthUser(conta, getAuthorities(conta));

    }

    private Collection<GrantedAuthority> getAuthorities(Conta conta) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        if(conta.getNumeroConta() > 2) {
            authorities.add(new SimpleGrantedAuthority("CUSTOMER"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        }
        return authorities;
    }

}
