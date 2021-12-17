package com.banco.bluebank.auth.core;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private JwtKeyStoreProperties jwtKeyStoreProperties;

    @Value("${bluebank.authorizationpassword}")
    private String authorizationPassword;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.inMemory()
                .withClient("bluebank-app")
                    .secret(passwordEncoder.encode(authorizationPassword))
                    .authorizedGrantTypes("password")
                    .scopes("READ", "WRITE")
                    .accessTokenValiditySeconds(60*60*12)
                .and()
                .withClient("checktoken")
                    .secret(passwordEncoder.encode("check978"));
    }




    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        security.checkTokenAccess("isAuthenticated()");
        security.checkTokenAccess("permitAll()")
                .tokenKeyAccess("permitAll()");
    }


    @Bean
    public JWKSet jwkSet() {
        RSAKey.Builder builder = new RSAKey.Builder((RSAPublicKey) keyPair().getPublic())
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID("bluebank-key-id");

        return new JWKSet(builder.build());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        var jwtAccessTokenConverter = new JwtAccessTokenConverter();

        jwtAccessTokenConverter.setKeyPair(keyPair());

        return jwtAccessTokenConverter;
    }

    private KeyPair keyPair() {

        var keyStorePass = jwtKeyStoreProperties.getPassword();
        var keyPairAlias = jwtKeyStoreProperties.getKeypairAlias();
        var keyStoreKeyFactory = new KeyStoreKeyFactory(
                jwtKeyStoreProperties.getJksLocation(), keyStorePass.toCharArray());

        return keyStoreKeyFactory.getKeyPair(keyPairAlias);

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .reuseRefreshTokens(false)
                .accessTokenConverter(jwtAccessTokenConverter());
    }
}
