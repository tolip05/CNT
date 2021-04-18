package com.cnt;

import com.cnt.domein.entities.Country;
import com.cnt.domein.entities.Currency;
import com.cnt.domein.models.service.RequestServiceModel;
import com.cnt.domein.models.views.ResultViewModel;
import com.cnt.repository.CountryRepository;
import com.cnt.repository.CurrencyRepository;
import com.cnt.service.CountryService;
import com.cnt.service.CountryServiceImpl;
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
public class AllApplicatinTests {
    private static final String BULGARIA = "Bulgaria";
    private static final String ROMANIA = "Romania";
    private static final String GREECE = "Greece";
    private static final String TURCEY = "Turkey";
    private static final String MACEDONIA = "Macedonia";
    private static final String SERBIA = "Serbia";
    private static final String BUL = "BUL";
    private static final String RSD = "RSD";
    private static final String MCD = "MCD";
    private static final String TYR = "TYR";
    private static final String EUR = "EUR";
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    private CountryService countryService;
    private ModelMapper modelMapper;
    private CurrencyService currencyService;

    private Currency euro;
    private Currency turceyLira;
    private Currency macedonDenar;
    private Currency srebskiDinar;
    private Currency bulgariaLev;
    private Country country;
    private Country greece;
    private Country romania;
    private Country turkey;
    private Country macedonia;
    private Country serbia;

    @Before
    public void init() {
        this.modelMapper = new ModelMapper();
        this.currencyService = new CurrencyServiceImpl(this.currencyRepository, this.modelMapper);
        this.countryService = new CountryServiceImpl(this.countryRepository, this.modelMapper, this.currencyService);

        this.euro = new Currency();
        this.turceyLira = new Currency();
        this.macedonDenar = new Currency();
        this.srebskiDinar = new Currency();
        this.bulgariaLev = new Currency();
        this.country = new Country();
        this.greece = new Country();
        this.romania = new Country();
        this.turkey = new Country();
        this.macedonia = new Country();
        this.serbia = new Country();
    }
    @Test
    public void check_result_when_moneyIsNotEnaught(){
        this.country.setName(BULGARIA);
        this.romania.setName(ROMANIA);
        this.greece.setName(GREECE);
        this.country.addNeighbor(this.greece);
        this.country.addNeighbor(this.greece);
        this.country.addNeighbor(this.romania);
        this.countryService.createCountry(country);

        RequestServiceModel requestServiceModel = new RequestServiceModel();
        requestServiceModel.setStartingCountry(BULGARIA);
        requestServiceModel.setBudgetPerCountry(BigDecimal.valueOf(1000));
        requestServiceModel.setCurrency(EUR);
        requestServiceModel.setTotalBudget(BigDecimal.valueOf(120));

        ResultViewModel resultViewModel =
                this.countryService.calculate(requestServiceModel);
        Assert.assertEquals(new BigDecimal("120"),resultViewModel.getLeftover());
        Assert.assertEquals(0,resultViewModel.getForeighnCurrency().size());
        Assert.assertEquals(0,(int)resultViewModel.getArounds());

    }
    @Test
    public void checkResult_whenNotNeighbors(){
        this.country.setName(BULGARIA);
        this.countryService.createCountry(country);
        RequestServiceModel requestServiceModel = new RequestServiceModel();
        requestServiceModel.setStartingCountry(BULGARIA);
        requestServiceModel.setBudgetPerCountry(BigDecimal.valueOf(100));
        requestServiceModel.setCurrency(EUR);
        requestServiceModel.setTotalBudget(BigDecimal.valueOf(1200));
        ResultViewModel resultViewModel =
                this.countryService.calculate(requestServiceModel);
        Assert.assertEquals(new BigDecimal("1200"),resultViewModel.getLeftover());
        Assert.assertEquals(0,resultViewModel.getForeighnCurrency().size());
        Assert.assertEquals(0,resultViewModel.getNeighbors().size());
        Assert.assertEquals(0,(int)resultViewModel.getArounds());
    }
    @Test
    public void checkCorrectProcessing_calculateMethod() {
        this.bulgariaLev.setName(BUL);
        this.bulgariaLev.setExchangeRate(BigDecimal.valueOf(1.9555));
        this.euro.setName(EUR);
        this.euro.setExchangeRate(BigDecimal.valueOf(1));
        this.turceyLira.setName(TYR);
        this.turceyLira.setExchangeRate(BigDecimal.valueOf(9.666));
        this.macedonDenar.setName(MCD);
        this.macedonDenar.setExchangeRate(BigDecimal.valueOf(62.5));
        this.srebskiDinar.setName(RSD);
        this.srebskiDinar.setExchangeRate(BigDecimal.valueOf(117));
        this.currencyRepository.saveAndFlush(this.euro);
        this.currencyRepository.saveAndFlush(this.turceyLira);
        this.currencyRepository.saveAndFlush(this.macedonDenar);
        this.currencyRepository.saveAndFlush(this.srebskiDinar);
        this.currencyRepository.saveAndFlush(this.bulgariaLev);

        this.country.setName(BULGARIA);
        this.country.setLocalCurrency(bulgariaLev);
        this.romania.setName(ROMANIA);
        this.greece.setName(GREECE);
        this.turkey.setName(TURCEY);
        this.turkey.setLocalCurrency(turceyLira);
        this.macedonia.setName(MACEDONIA);
        this.macedonia.setLocalCurrency(macedonDenar);
        this.serbia.setName(SERBIA);
        this.serbia.setLocalCurrency(srebskiDinar);
        this.country.addNeighbor(romania);
        this.country.addNeighbor(greece);
        this.country.addNeighbor(turkey);
        this.country.addNeighbor(macedonia);
        this.country.addNeighbor(serbia);
        this.countryService.createCountry(romania);
        this.countryService.createCountry(greece);
        this.countryService.createCountry(macedonia);
        this.countryService.createCountry(serbia);
        this.countryService.createCountry(country);
        this.countryService.createCountry(turkey);


        RequestServiceModel requestServiceModel = new RequestServiceModel();
        requestServiceModel.setStartingCountry(BULGARIA);
        requestServiceModel.setBudgetPerCountry(BigDecimal.valueOf(100));
        requestServiceModel.setCurrency(EUR);
        requestServiceModel.setTotalBudget(BigDecimal.valueOf(1200));

        ResultViewModel resultViewModel =
                this.countryService.calculate(requestServiceModel);
        Assert.assertEquals(EUR,resultViewModel.getCurrency());
        Assert.assertEquals(new BigDecimal("200"),resultViewModel.getLeftover());
        Assert.assertEquals(3,resultViewModel.getForeighnCurrency().size());
        Assert.assertEquals(5,resultViewModel.getNeighbors().size());
        Assert.assertEquals(2,(int)resultViewModel.getArounds());
    }
}
