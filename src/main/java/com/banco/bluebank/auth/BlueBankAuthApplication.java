package com.banco.bluebank.auth;

import com.banco.bluebank.auth.core.io.Base64ProtocolResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class BlueBankAuthApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        var app = new SpringApplication(BlueBankAuthApplication.class);
        app.addListeners(new Base64ProtocolResolver());
        app.run(args);

//        SpringApplication.run(BlueBankAuthApplication.class, args);
    }

}
