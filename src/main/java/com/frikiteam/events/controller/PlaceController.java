package com.rodrigo.miparte.controllers;

import com.rodrigo.miparte.domain.model.District;
import com.rodrigo.miparte.domain.model.Place;
import com.rodrigo.miparte.domain.service.IDistrictService;
import com.rodrigo.miparte.domain.service.IPlaceService;
import com.rodrigo.miparte.resource.DistrictResource;
import com.rodrigo.miparte.resource.PlaceResource;
import com.rodrigo.miparte.resource.SaveDistrictResource;
import com.rodrigo.miparte.resource.SavePlaceResource;
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
public class PlaceController {
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private IPlaceService placeService;

    //Conversiones
    private Place convertToEntity(SavePlaceResource resource){
        return mapper.map(resource,Place.class);
    }
    private PlaceResource convertToResource(Place entity){
        return mapper.map(entity,PlaceResource.class);
    }

    @GetMapping("/places")
    public Page<PlaceResource> getAllPlaces(Pageable pageable){
        List<PlaceResource> resources = placeService.getAllPlaces(pageable)
                .getContent().stream().map(this::convertToResource)
                .collect(Collectors.toList());
        return new PageImpl<>(resources,pageable,resources.size());
    }

    @PostMapping("/places/create")
    public PlaceResource createPlace(@Valid @RequestBody SavePlaceResource resource){
        Place place = convertToEntity(resource);
        return convertToResource(placeService.createPlace(place));
    }

    @PutMapping("/places/update/{placeId}")
    public PlaceResource updatePlace(@PathVariable Long placeId,@RequestBody SavePlaceResource resource){
        Place place = convertToEntity(resource);
        return convertToResource(placeService.updatePlace(placeId,place));
    }

    @DeleteMapping("/places/delete/{placeId}")
    public ResponseEntity<?> deletePlace(@PathVariable Long placeId)
    {
        return placeService.deletePlace(placeId);
    }

    @GetMapping("/places/get/{placeId}")
    public PlaceResource getPlaceById(@PathVariable Long placeId){
        return convertToResource(placeService.getPlaceById(placeId));
    }
}
