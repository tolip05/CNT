package com.cnt;

import com.cnt.domein.entities.Country;
import com.cnt.domein.entities.Currency;
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

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CountryServiceTests {
    private static final String BULGARIA = "Bulgaria";
    private static final String ROMANIA = "Romania";
    private static final String GREECE = "Greece";
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    private CountryService countryService;
    private ModelMapper modelMapper;
    private CurrencyService currencyService;

    private Country country;
    private Country greece;
    private Country romania;

    @Before
    public void init() {
        this.modelMapper = new ModelMapper();
        this.currencyService = new CurrencyServiceImpl(this.currencyRepository, this.modelMapper);
        this.countryService = new CountryServiceImpl(this.countryRepository, this.modelMapper, this.currencyService);
        this.country = new Country();
        this.greece = new Country();
        this.romania = new Country();
    }

    @Test
    public void countryService_saveCountryWithCorrectValue_returnCorrect() {
        this.country.setName(BULGARIA);
        this.countryService.createCountry(country);
        Country expectedCountry = new Country();
        expectedCountry.setName(BULGARIA);
        Country actualCountry = this.countryRepository
                .findByName(BULGARIA).orElse(null);
        Assert.assertEquals(expectedCountry.getName(), actualCountry.getName());
    }

    @Test(expected = Exception.class)
    public void countryService_getCountry_with_wrongName() {
        this.country.setName(BULGARIA);
        this.countryService.createCountry(country);
        Country actualCountry = this.countryRepository
                .findByName("Bulgari").get();
    }

    @Test
    public void get_size_of_neighbors() {
        this.country.setName(BULGARIA);
        this.greece.setName(GREECE);
        this.romania.setName(ROMANIA);
        this.country.addNeighbor(greece);
        this.country.addNeighbor(romania);
        this.countryService.createCountry(greece);
        this.countryService.createCountry(romania);
        this.countryService.createCountry(country);
        Country expectedCountry = this.countryRepository
                .findByName("Bulgaria")
                .get();
        Country expectedNeighbor = this.countryRepository
                .findByName(ROMANIA).orElse(null);
        Assert.assertEquals(2, expectedCountry.getNeighbors().size());
        Assert.assertEquals(1, expectedNeighbor.getNeighbors().size());
    }

}
