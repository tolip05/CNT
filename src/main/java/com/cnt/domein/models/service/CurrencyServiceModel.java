package com.cnt.domein.models.service;

import java.math.BigDecimal;

public class CurrencyServiceModel extends BaseServiceModel {
    private String name;
    private BigDecimal exchangeRate;

    public CurrencyServiceModel() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getExchangeRate() {
        return this.exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
