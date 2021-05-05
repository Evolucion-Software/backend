package com.rodrigo.miparte.controllers;


import com.rodrigo.miparte.domain.model.Country;
import com.rodrigo.miparte.domain.service.ICountryService;
import com.rodrigo.miparte.resource.CountryResource;
import com.rodrigo.miparte.resource.SaveCountryResource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class CountryController {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private ICountryService countryService;
    //Conversiones
    private Country convertToEntity(SaveCountryResource resource){
        return mapper.map(resource,Country.class);
    }
    private CountryResource convertToResource(Country entity){
        return mapper.map(entity,CountryResource.class);
    }

    @GetMapping("/countries")
    public Page<CountryResource> getAllCountries(Pageable pageable){
        List<CountryResource> resources = countryService.getAllCountries(pageable)
                .getContent().stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources,pageable,resources.size());
    }

    @PostMapping("/countries/create")
    public CountryResource createCountry(@Valid @RequestBody SaveCountryResource resource){
        Country country = convertToEntity(resource);
        return convertToResource(countryService.createCountry(country));
    }

    @PutMapping("/countries/update/{countryId}")
    public CountryResource updateCountry(@PathVariable Long countryId,@RequestBody SaveCountryResource resource){
        Country country = convertToEntity(resource);
        return convertToResource(countryService.updateCountry(countryId, country));
    }

    @DeleteMapping("/countries/delete/{countryId}")
    public ResponseEntity<?> deleteCountry(@PathVariable Long countryId)
    {
        return countryService.deleteCountry(countryId);
    }

    @GetMapping("/countries/get/{countryId}")
    public CountryResource getCountryById(@PathVariable Long countryId){
        return convertToResource(countryService.getCountryById(countryId));
    }

}
