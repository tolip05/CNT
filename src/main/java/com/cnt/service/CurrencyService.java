package com.cnt.service;

import com.cnt.domein.entities.Currency;
import com.cnt.domein.models.service.CurrencyServiceModel;

public interface CurrencyService {
    CurrencyServiceModel findByName(String name);
    boolean createCurrency(Currency currency);
}
