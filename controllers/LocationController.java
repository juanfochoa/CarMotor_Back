package co.edu.uniandes.dse.carmotor.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniandes.dse.carmotor.dto.LocationDTO;
import co.edu.uniandes.dse.carmotor.dto.LocationDetailDTO;
import co.edu.uniandes.dse.carmotor.entities.LocationEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.services.LocationService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/locations")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<LocationDetailDTO> findAll() {
        List<LocationEntity> locations = locationService.getLocations();
        return modelMapper.map(locations, new TypeToken<List<LocationDetailDTO>>() {}.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public LocationDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
            LocationEntity LocationEntity = locationService.getLocation(id);
            return modelMapper.map(LocationEntity, LocationDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public LocationDTO create(@RequestBody LocationDTO LocationDTO) throws IllegalOperationException, EntityNotFoundException {
            LocationEntity locationEntity = locationService.createLocation(modelMapper.map(LocationDTO, LocationEntity.class));
            return modelMapper.map(locationEntity, LocationDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public LocationDTO update(@PathVariable Long id, @RequestBody LocationDTO LocationDTO)
        throws EntityNotFoundException, IllegalOperationException {
            LocationEntity locationEntity = locationService.updateLocation(id, modelMapper.map(LocationDTO, LocationEntity.class));
            return modelMapper.map(locationEntity, LocationDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        locationService.deleteLocation(id);
    }
}