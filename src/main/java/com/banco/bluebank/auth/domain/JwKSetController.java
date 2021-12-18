package com.banco.bluebank.auth.domain;

import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class JwKSetController {

    @Autowired
    private JWKSet jwkSet;

    @GetMapping("/.well-know/jwks.json")
    public Map<String,Object> keys(){
        return this.jwkSet.toJSONObject();
    }
}
