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
import co.edu.uniandes.dse.carmotor.services.AssessorVehicleService;

@RestController
@RequestMapping("/assessors/{assessorId}/vehicles")
public class AssessorVehicleController {
    @Autowired
    private AssessorVehicleService assessorVehicleService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{vehicleId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public VehicleDTO addVehicleToAssessor(@PathVariable Long assessorId, @PathVariable Long vehicleId)
            throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicleEntity = assessorVehicleService.addVehicleToAssessor(assessorId, vehicleId);
        return modelMapper.map(vehicleEntity, VehicleDTO.class);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<VehicleDTO> getVehiclesFromAssessor(@PathVariable Long assessorId)
            throws EntityNotFoundException, IllegalOperationException {
        List<VehicleEntity> vehicles = assessorVehicleService.getVehiclesFromAssessor(assessorId);
        return modelMapper.map(vehicles, new TypeToken<List<VehicleDTO>>() {}.getType());
    }

    @GetMapping(value = "/{vehicleId}")
    @ResponseStatus(code = HttpStatus.OK)
    public VehicleDTO getVehicleFromAssessor(@PathVariable Long assessorId, @PathVariable Long vehicleId)
            throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicleEntity = assessorVehicleService.getVehicleFromAssessor(assessorId, vehicleId);
        return modelMapper.map(vehicleEntity, VehicleDTO.class);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<VehicleDTO> updateVehiclesFromAssessor(@PathVariable Long assessorId, @RequestBody List<VehicleDTO> vehicles)
            throws EntityNotFoundException, IllegalOperationException {
        List<VehicleEntity> updatedVehicles = assessorVehicleService.updateVehiclesFromAssessor(assessorId,
                modelMapper.map(vehicles, new TypeToken<List<VehicleEntity>>() {}.getType()));
        return modelMapper.map(updatedVehicles, new TypeToken<List<VehicleDTO>>() {}.getType());
    }

    @DeleteMapping(value = "/{vehicleId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteVehicleFromAssessor(@PathVariable Long assessorId, @PathVariable Long vehicleId)
            throws EntityNotFoundException, IllegalOperationException {
        assessorVehicleService.deleteVehicleFromAssessor(assessorId, vehicleId);
    }
}