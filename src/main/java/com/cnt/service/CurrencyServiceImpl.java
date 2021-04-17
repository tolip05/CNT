package com.cnt.service;

import com.cnt.domein.entities.Currency;
import com.cnt.domein.models.service.CurrencyServiceModel;
import com.cnt.repository.CurrencyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private static final String WRONG_MESSAGE = "Currency name is not correct!!!";
    private final CurrencyRepository currencyRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository, ModelMapper modelMapper) {
        this.currencyRepository = currencyRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CurrencyServiceModel findByName(String name) {
        Currency currency = this.currencyRepository.findByName(name)
                .orElse(null);
        if (currency == null){
            throw new IllegalArgumentException(WRONG_MESSAGE);
        }
        return this.modelMapper.map(currency,CurrencyServiceModel.class);
    }

    @Override
    public boolean createCurrency(Currency currency) {
        try{
            this.currencyRepository.saveAndFlush(currency);
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
