package co.edu.uniandes.dse.carmotor.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.carmotor.dto.VehicleDTO;
import co.edu.uniandes.dse.carmotor.dto.VehicleDetailDTO;
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.services.VehicleService;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<VehicleDetailDTO> findAll() {
        List<VehicleEntity> vehicles = vehicleService.getVehicles();
        return modelMapper.map(vehicles, new TypeToken<List<VehicleDetailDTO>>() {}.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public VehicleDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        VehicleEntity vehicleEntity = vehicleService.getVehicle(id);
        return modelMapper.map(vehicleEntity, VehicleDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public VehicleDTO create(@RequestBody VehicleDTO vehicleDTO)
            throws IllegalOperationException, EntityNotFoundException {
        VehicleEntity vehicleEntity = vehicleService.createVehicle(modelMapper.map(vehicleDTO, VehicleEntity.class));
        return modelMapper.map(vehicleEntity, VehicleDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public VehicleDTO update(@PathVariable Long id, @RequestBody VehicleDTO vehicleDTO)
            throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicleEntity = vehicleService.updateVehicle(id, modelMapper.map(vehicleDTO, VehicleEntity.class));
        return modelMapper.map(vehicleEntity, VehicleDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        vehicleService.deleteVehicle(id);
    }
}