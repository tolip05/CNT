package com.cnt;
import com.cnt.domein.entities.Currency;
import com.cnt.domein.models.service.CurrencyServiceModel;
import com.cnt.repository.CurrencyRepository;
import com.cnt.service.CurrencyService;
import com.cnt.service.CurrencyServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CurrencyServiceTests {
    private static final String EUR = "EUR";
    private static final String USD = "USD";
    @Autowired
    private CurrencyRepository currencyRepository;
    private CurrencyService currencyService;
    private ModelMapper modelMapper;

    @Before
    public void init(){
        this.modelMapper = new ModelMapper();
        this.currencyService =
                new CurrencyServiceImpl(this.currencyRepository,this.modelMapper);
    }
    @Test
    public void currencyService_createCurrencyWithCorrectValue_returnCorrect(){
        Currency currency = new Currency();
        currency.setName(EUR);
        currency.setExchangeRate(BigDecimal.valueOf(1));
        this.currencyService.createCurrency(currency);
        Currency expected = new Currency();
        expected.setName(EUR);
        expected.setExchangeRate(BigDecimal.valueOf(1));
        Currency actual = this.currencyRepository.findByName(EUR).orElse(null);
        Assert.assertEquals(expected.getName(),actual.getName());
        Assert.assertEquals(expected.getExchangeRate(),actual.getExchangeRate());
    }
    @Test(expected = Exception.class)
    public void currencyService_getCurrencY_withNotCorrectName(){
        Currency currency = new Currency();
        currency.setName(USD);
        this.currencyService.createCurrency(currency);
        this.currencyService.findByName("LEV");
    }
}

