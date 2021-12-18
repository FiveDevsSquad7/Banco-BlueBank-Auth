package com.banco.bluebank.auth.domain;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healthcheck")
public class HealthCheckController {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public String healthCheck() {
        return "Ok";
    }

}
