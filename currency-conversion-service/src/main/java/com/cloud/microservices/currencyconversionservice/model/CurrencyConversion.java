package com.cloud.microservices.currencyconversionservice.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CurrencyConversion {
    private Long id;
    private String from;
    private String to;
    private BigDecimal quantity;
    private BigDecimal conversionMultiple;
    private BigDecimal totalCalculatedAmount;
    private String environment;
}
