package com.cnt.domein.models.views;

import java.math.BigDecimal;
import java.util.*;

public class ResultViewModel {
    private Integer neighborsCount;
    private Set<String> neighbors;
    private Integer arounds;
    private String currency;
    private BigDecimal leftover;
    private Map<String,BigDecimal> foreighnCurrency;

    public ResultViewModel() {
        this.neighbors = new HashSet<>();
        this.foreighnCurrency = new HashMap<>();
    }

    public Integer getNeighborsCount() {
        return this.neighborsCount;
    }

    public void setNeighborsCount(Integer neighborsCount) {
        this.neighborsCount = neighborsCount;
    }

    public Set<String> getNeighbors() {
        return this.neighbors;
    }

    public void setNeighbors(Set<String> neighbors) {
        this.neighbors = neighbors;
    }

    public Integer getArounds() {
        return this.arounds;
    }

    public void setArounds(Integer arounds) {
        this.arounds = arounds;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getLeftover() {
        return this.leftover;
    }

    public void setLeftover(BigDecimal leftover) {
        this.leftover = leftover;
    }

    public Map<String, BigDecimal> getForeighnCurrency() {
        return this.foreighnCurrency;
    }

    public void setForeighnCurrency(Map<String, BigDecimal> foreighnCurrency) {
        this.foreighnCurrency = foreighnCurrency;
    }
}
