package com.cloud.microservices.currencyconversionservice.controller;

import com.cloud.microservices.currencyconversionservice.feign.CurrencyExchangeFeignClient;
import com.cloud.microservices.currencyconversionservice.model.CurrencyConversion;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CurrencyConversionController {
    private final CurrencyExchangeFeignClient proxy;
    private final RestTemplate restTemplate;
    @Value("${currency-exchange-service.url}")
    private final String currencyExchangeUrl;
    private static final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(@PathVariable String from,
                                                          @PathVariable String to,
                                                          @PathVariable BigDecimal quantity) {

        Map<String, String> uriVariables = Map.ofEntries(
                Map.entry("from", from),
                Map.entry("to", to)
        );

        System.out.println(currencyExchangeUrl + " keno");
        logger.info(currencyExchangeUrl + " keno");
        ResponseEntity<CurrencyConversion> response = restTemplate.getForEntity(
                "http://currency-exchange-service:8000/currency-exchange/from/{from}/to/{to}",
                CurrencyConversion.class, uriVariables
        );

        CurrencyConversion currencyConversion = response.getBody();

        return CurrencyConversion.builder()
                .id(currencyConversion.getId())
                .from(from)
                .to(to)
                .quantity(quantity)
                .conversionMultiple(currencyConversion.getConversionMultiple())
                .totalCalculatedAmount(quantity.multiply(currencyConversion.getConversionMultiple()))
                .environment(currencyConversion.getEnvironment() + " rest template")
                .build();
    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from,
                                                               @PathVariable String to,
                                                               @PathVariable BigDecimal quantity) {
        CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(from, to);

        return CurrencyConversion.builder()
                .id(currencyConversion.getId())
                .from(from)
                .to(to)
                .quantity(quantity)
                .conversionMultiple(currencyConversion.getConversionMultiple())
                .totalCalculatedAmount(quantity.multiply(currencyConversion.getConversionMultiple()))
                .environment(currencyConversion.getEnvironment() + " feign")
                .build();
    }
}
