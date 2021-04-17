package com.cnt.domein.models.service;

import java.util.HashSet;
import java.util.Set;

public class CountryServiceModel extends BaseServiceModel{
    private String name;
    private CurrencyServiceModel localCurrency;
    private Set<CountryServiceModel> neighbors;

    public CountryServiceModel() {
        this.neighbors = new HashSet<>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CurrencyServiceModel getLocalCurrency() {
        return this.localCurrency;
    }

    public void setLocalCurrency(CurrencyServiceModel localCurrency) {
        this.localCurrency = localCurrency;
    }

    public Set<CountryServiceModel> getNeighbors() {
        return this.neighbors;
    }

    public void setNeighbors(Set<CountryServiceModel> neighbors) {
        this.neighbors = neighbors;
    }
}
