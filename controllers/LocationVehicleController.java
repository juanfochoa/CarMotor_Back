package co.edu.uniandes.dse.carmotor.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.carmotor.dto.VehicleDTO;
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.services.LocationVehicleService;

@RestController
@RequestMapping("/locations/{locationId}/vehicles")
public class LocationVehicleController {
    @Autowired
    private LocationVehicleService locationVehicleService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{vehicleId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public VehicleDTO addVehicleToLocation(@PathVariable Long locationId, @PathVariable Long vehicleId)
            throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicleEntity = locationVehicleService.addVehicleToLocation(locationId, vehicleId);
        return modelMapper.map(vehicleEntity, VehicleDTO.class);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<VehicleDTO> getVehiclesFromLocation(@PathVariable Long locationId) throws EntityNotFoundException {
        List<VehicleEntity> vehicles = locationVehicleService.getVehiclesFromLocation(locationId);
        return modelMapper.map(vehicles, new TypeToken<List<VehicleDTO>>() {}.getType());
    }

    @GetMapping(value = "/{vehicleId}")
    @ResponseStatus(code = HttpStatus.OK)
    public VehicleDTO getVehicleFromLocation(@PathVariable Long locationId, @PathVariable Long vehicleId)
            throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicleEntity = locationVehicleService.getVehicleFromLocation(locationId, vehicleId);
        return modelMapper.map(vehicleEntity, VehicleDTO.class);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<VehicleDTO> updateVehiclesFromLocation(@PathVariable Long locationId, @RequestBody List<VehicleDTO> vehicles)
            throws EntityNotFoundException, IllegalOperationException {
        List<VehicleEntity> updatedVehicles = locationVehicleService.updateVehiclesFromLocation(locationId,
                modelMapper.map(vehicles, new TypeToken<List<VehicleEntity>>() {}.getType()));
        return modelMapper.map(updatedVehicles, new TypeToken<List<VehicleDTO>>() {}.getType());
    }

    @DeleteMapping(value = "/{vehicleId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteVehicleFromLocation(@PathVariable Long locationId, @PathVariable Long vehicleId)
            throws EntityNotFoundException, IllegalOperationException {
        locationVehicleService.deleteVehicleFromLocation(locationId, vehicleId);
    }
}
