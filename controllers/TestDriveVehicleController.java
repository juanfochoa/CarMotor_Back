package co.edu.uniandes.dse.carmotor.controllers;


import co.edu.uniandes.dse.carmotor.dto.VehicleDTO;
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.services.TestDriveVehicleService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/testdrives/{testdrivesId}/vehicles")
public class TestDriveVehicleController {
    @Autowired
    private TestDriveVehicleService testDriveVehicleService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{vehicleId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public VehicleDTO addVehicleToTestDrive(@PathVariable Long testDriveId, @PathVariable Long vehicleId)
            throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicleEntity = testDriveVehicleService.addVehicleToTestDrive(testDriveId, vehicleId);
        return modelMapper.map(vehicleEntity, VehicleDTO.class);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<VehicleDTO> getVehiclesFromTestDrive(@PathVariable Long testDriveId)
            throws EntityNotFoundException, IllegalOperationException {
        List<VehicleEntity> vehicles = testDriveVehicleService.getVehiclesFromTestDrive(testDriveId);
        return modelMapper.map(vehicles, new TypeToken<List<VehicleDTO>>() {
                }.getType());
    }

    @GetMapping(value = "/{vehicleId}")
    @ResponseStatus(code = HttpStatus.OK)
    public VehicleDTO getVehicleFromTestDrive(@PathVariable Long testDriveId, @PathVariable Long vehicleID)
            throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicleEntity = testDriveVehicleService.getVehicleFromTestDrive(testDriveId, vehicleID);
        return modelMapper.map(vehicleEntity, VehicleDTO.class);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<VehicleDTO> updateVehiclesFromLocation(@PathVariable Long testDriveId, @RequestBody List<VehicleDTO> vehicles)
            throws EntityNotFoundException, IllegalOperationException {
        List<VehicleEntity> updatedVehicles = testDriveVehicleService.updateVehiclesFromTestDrive(testDriveId,
                modelMapper.map(vehicles, new TypeToken<List<VehicleEntity>>() {}.getType()));
        return modelMapper.map(updatedVehicles, new TypeToken<List<VehicleDTO>>() {
            }.getType());
    }

    @DeleteMapping(value = "/{vehicleId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteVehicleFromTestDrive(@PathVariable Long testDriveId, @PathVariable Long vehicleId)
            throws EntityNotFoundException {
        testDriveVehicleService.deleteVehicleFromTestDrive(testDriveId, vehicleId);
    }
}