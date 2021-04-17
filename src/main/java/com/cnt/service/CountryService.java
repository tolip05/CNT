package com.cnt.service;

import com.cnt.domein.entities.Country;
import com.cnt.domein.models.service.RequestServiceModel;
import com.cnt.domein.models.views.ResultViewModel;

public interface CountryService {
ResultViewModel calculate(RequestServiceModel requestServiceModel);
boolean createCountry(Country country);
}
