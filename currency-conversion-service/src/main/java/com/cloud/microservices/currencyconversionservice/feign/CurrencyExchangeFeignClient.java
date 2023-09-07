package com.cloud.microservices.currencyconversionservice.feign;

import com.cloud.microservices.currencyconversionservice.model.CurrencyConversion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// feign client will talk to Eureka - service registry and find instances
// load will be automatically balanced for feign client as eureka-client has spring cloud load balancer depenedency included, all that is needed to be done is to omit the "url" parameter of the FeignClient annotation, othewise, all load will go to that url
@FeignClient(name = "currency-exchange-service")
public interface CurrencyExchangeFeignClient {
    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    CurrencyConversion retrieveExchangeValue(@PathVariable String from, @PathVariable String to);
}
