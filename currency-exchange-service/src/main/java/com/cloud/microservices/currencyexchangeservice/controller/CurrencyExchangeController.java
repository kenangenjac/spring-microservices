package com.cloud.microservices.currencyexchangeservice.controller;

import com.cloud.microservices.currencyexchangeservice.model.CurrencyExchange;
import com.cloud.microservices.currencyexchangeservice.repository.CurrencyExchangeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
public class CurrencyExchangeController {

    private final Environment environment;
    private final CurrencyExchangeRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(@PathVariable String from,
                                                  @PathVariable String to) {

        logger.info("retrieveExchangeValue called with {} to {}", from, to);

        var port = environment.getProperty("local.server.port");
        var currencyExchange = repository.findByFromAndTo(from, to);

        if (currencyExchange.isEmpty()) {
            throw new RuntimeException("Currency exchange not found");
        }

        currencyExchange.get().setEnvironment(port);
        return currencyExchange.get();
    }
}
