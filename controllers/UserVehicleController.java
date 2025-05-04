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
import co.edu.uniandes.dse.carmotor.services.UserVehicleService;

@RestController
@RequestMapping("/users/{userId}/vehicles")
public class UserVehicleController {
    @Autowired
    private UserVehicleService userVehicleService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{vehicleId}")
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleDTO addVehicleToUser(@PathVariable Long userId, @PathVariable Long vehicleId)
            throws IllegalOperationException, EntityNotFoundException {
        VehicleEntity vehicle = userVehicleService.addVehicleToUser(userId, vehicleId);
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleDTO> getVehiclesFromUser(@PathVariable Long userId) throws EntityNotFoundException {
        List<VehicleEntity> vehicles = userVehicleService.getVehiclesFromUser(userId);
        return modelMapper.map(vehicles, new TypeToken<List<VehicleDTO>>() {}.getType());
    }

    @GetMapping(value = "/{vehicleId}")
    @ResponseStatus(HttpStatus.OK)
    public VehicleDTO getVehicleFromUser(@PathVariable Long userId, @PathVariable Long vehicleId)
            throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicle = userVehicleService.getVehicleFromUser(userId, vehicleId);
        return modelMapper.map(vehicle, VehicleDTO.class);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VehicleDTO> updateVehiclesFromUser(@PathVariable Long userId, @RequestBody List<VehicleDTO> vehicles)
            throws EntityNotFoundException, IllegalOperationException {
        List<VehicleEntity> updatedVehicles = userVehicleService.updateVehiclesFromUser(userId,
                modelMapper.map(vehicles, new TypeToken<List<VehicleEntity>>() {}.getType()));
        return modelMapper.map(updatedVehicles, new TypeToken<List<VehicleDTO>>() {}.getType());
    }

    @DeleteMapping(value = "/{vehicleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVehicleFromUser(@PathVariable Long userId, @PathVariable Long vehicleId)
            throws EntityNotFoundException, IllegalOperationException {
        userVehicleService.deleteVehicleFromUser(userId, vehicleId);
    }
}