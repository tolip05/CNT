package com.cnt.domein.models.service;

import java.math.BigDecimal;

public class RequestServiceModel {
    private String startingCountry;
    private BigDecimal budgetPerCountry;
    private BigDecimal totalBudget;
    private String currency;

    public RequestServiceModel() {
    }

    public String getStartingCountry() {
        return this.startingCountry;
    }

    public void setStartingCountry(String startingCountry) {
        this.startingCountry = startingCountry;
    }

    public BigDecimal getBudgetPerCountry() {
        return this.budgetPerCountry;
    }

    public void setBudgetPerCountry(BigDecimal budgetPerCountry) {
        this.budgetPerCountry = budgetPerCountry;
    }

    public BigDecimal getTotalBudget() {
        return this.totalBudget;
    }

    public void setTotalBudget(BigDecimal totalBudget) {
        this.totalBudget = totalBudget;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
