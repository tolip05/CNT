package com.cnt.web;

import com.cnt.domein.models.binding.BindingRequestModel;
import com.cnt.domein.models.service.RequestServiceModel;
import com.cnt.domein.models.views.ResultViewModel;
import com.cnt.service.CountryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping(value = "/travels", consumes = "application/json", produces = "application/json")
public class BaseController {
    private final CountryService countryService;
    private final ModelMapper modelMapper;

    @Autowired
    public BaseController(CountryService countryService, ModelMapper modelMapper) {
        this.countryService = countryService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/country")
    public ResponseEntity calculateTravel(@ModelAttribute BindingRequestModel model) throws URISyntaxException {
        RequestServiceModel requestServiceModel = this.modelMapper
                .map(model,RequestServiceModel.class);
        ResultViewModel result = this.countryService.calculate(requestServiceModel);
        return ResponseEntity.created(new URI("/travels/create")).body(result);
    }
}
