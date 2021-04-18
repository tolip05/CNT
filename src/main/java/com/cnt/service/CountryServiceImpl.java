package com.cnt.service;

import com.cnt.domein.entities.Country;
import com.cnt.domein.models.service.CountryServiceModel;
import com.cnt.domein.models.service.CurrencyServiceModel;
import com.cnt.domein.models.service.RequestServiceModel;
import com.cnt.domein.models.views.ResultViewModel;
import com.cnt.repository.CountryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class CountryServiceImpl implements CountryService {
    private static final String WRONG_MESSAGE = "Country name is not correct!!!";
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final CurrencyService currencyService;
    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository,
                              ModelMapper modelMapper,
                              CurrencyService currencyService) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.currencyService = currencyService;
    }

    @Override
    public ResultViewModel calculate(RequestServiceModel requestServiceModel) {
        ResultViewModel resultViewModel = new ResultViewModel();
        //Get country from database if it exist
        Country country = this.countryRepository
                .findByName(requestServiceModel.getStartingCountry())
                .orElse(null);
        if (country == null){
            throw new IllegalArgumentException(WRONG_MESSAGE);
        }
        //Mapping entity in to service model
        CountryServiceModel countryServiceModel =
                this.modelMapper.map(country,CountryServiceModel.class);
        //Get number of neighbors from starting country
        int countOfNeighbors = countryServiceModel.getNeighbors().size();
        if (countOfNeighbors == 0){
            this.setDeafultValues(resultViewModel,requestServiceModel.getTotalBudget());
            return resultViewModel;
        }
        //Check for enough money for minimum one tour
        boolean checkForCountTour = this.checkTourIsValid(countOfNeighbors,
                requestServiceModel.getBudgetPerCountry()
                ,requestServiceModel.getTotalBudget());
        //Model for results add neighbors names
        countryServiceModel.getNeighbors().forEach(e -> resultViewModel
                .getNeighbors().add(e.getName()));
        //Model for results set currency
        resultViewModel.setCurrency(requestServiceModel.getCurrency());
        //Model for results set neighbors count
        resultViewModel.setNeighborsCount(countryServiceModel.getNeighbors().size());
        //If has not enough money for minimum one tour,in result model set zero tours
        // and total budget for unspent funds
        if (!checkForCountTour){
            this.setDeafultValues(resultViewModel,requestServiceModel.getTotalBudget());
            return resultViewModel;
        }
        //Calculated price for one tour
        BigDecimal priceOfTour = this.calculatePriceOfTour(countOfNeighbors,
                requestServiceModel.getBudgetPerCountry());
        //Calculated count of tours can Angel do
        int numberOfTour = requestServiceModel.getTotalBudget().divide(priceOfTour).intValue();
        //Set number of tour in result model
        resultViewModel.setArounds(numberOfTour);
        //Calculated remain money
        BigDecimal remainMoney = BigDecimal.valueOf(requestServiceModel
                .getTotalBudget().intValue() % priceOfTour.intValue());
        //Result model set remain money
        resultViewModel.setLeftover(remainMoney);
        //Convert money for country to euro, because euro is base currency to converting
        //all currencies have a exchange rate to euro. We can use too USD, then all currencies must have
        //exchange rate to USD
        BigDecimal amountOfMoneyPerCountryToEuro = this.convertMoneyToEuro(requestServiceModel.getBudgetPerCountry(),numberOfTour
                ,requestServiceModel.getCurrency());
        //Iteration about neighbours and if neighbor have local currency
        //then we calculated price per country to local currency and write result in hash map in result model
        for (CountryServiceModel neighbor : countryServiceModel.getNeighbors()) {
            if (neighbor.getLocalCurrency() != null){
                BigDecimal exchangeRate = neighbor.getLocalCurrency().getExchangeRate();
                BigDecimal money = amountOfMoneyPerCountryToEuro
                         .multiply(exchangeRate);
                 resultViewModel.getForeighnCurrency()
                         .putIfAbsent(neighbor.getName(),money);
            }
        }
        return resultViewModel;
    }

    private void setDeafultValues(ResultViewModel resultViewModel, BigDecimal totalBudget) {
        resultViewModel.setLeftover(totalBudget);
        resultViewModel.setArounds(0);
    }

    @Override
    public boolean createCountry(Country country) {
        try {
            this.countryRepository.saveAndFlush(country);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    private BigDecimal convertMoneyToEuro( BigDecimal pricePerCountry,int numberOfTour, String currencyName) {
        BigDecimal moneyPerCountry = pricePerCountry
                .multiply(BigDecimal.valueOf(numberOfTour));
        CurrencyServiceModel currencyServiceModel = this.currencyService
                .findByName(currencyName);
        return moneyPerCountry.divide(currencyServiceModel.getExchangeRate());
    }

    private boolean checkTourIsValid(int countOfNeighbors, BigDecimal budgetPerCountry,
                                     BigDecimal totalBudget) {
        BigDecimal priceOfTour = this.calculatePriceOfTour(countOfNeighbors,budgetPerCountry);
        return priceOfTour.compareTo(totalBudget) <= 0;
    }

    private BigDecimal calculatePriceOfTour(int countOfNeighbors,
                                            BigDecimal budgetPerCountry) {
        return budgetPerCountry.multiply(BigDecimal.valueOf(countOfNeighbors));
    }
}
