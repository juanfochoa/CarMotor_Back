package co.edu.uniandes.dse.carmotor.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.carmotor.dto.TestDriveDTO;
import co.edu.uniandes.dse.carmotor.dto.TestDriveDetailDTO;
import co.edu.uniandes.dse.carmotor.entities.TestDriveEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.services.VehicleTestDriveService;

@RestController
@RequestMapping("/vehicles/{vehicleId}/testdrives")
public class VehicleTestDriveController {
    @Autowired
    private VehicleTestDriveService vehicleTestDriveService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{testDriveId}")
    @ResponseStatus(HttpStatus.CREATED)
    public TestDriveDetailDTO addTestDriveToVehicle(@PathVariable Long vehicleId, @PathVariable Long testDriveId)
            throws EntityNotFoundException {
        TestDriveEntity testDriveEntity = vehicleTestDriveService.addTestDriveToVehicle(vehicleId, testDriveId);
        return modelMapper.map(testDriveEntity, TestDriveDetailDTO.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TestDriveDetailDTO> getTestDrivesFromVehicle(@PathVariable Long vehicleId) throws EntityNotFoundException {
        List<TestDriveEntity> testDrives = vehicleTestDriveService.getTestDrivesFromVehicle(vehicleId);
        return modelMapper.map(testDrives, new TypeToken<List<TestDriveDetailDTO>>() {}.getType());
    }

    @GetMapping(value = "/{testDriveId}")
    @ResponseStatus(HttpStatus.OK)
    public TestDriveDetailDTO getTestDriveFromVehicle(@PathVariable Long vehicleId, @PathVariable Long testDriveId)
            throws EntityNotFoundException, IllegalOperationException {
        TestDriveEntity testDriveEntity = vehicleTestDriveService.getTestDriveFromVehicle(vehicleId, testDriveId);
        return modelMapper.map(testDriveEntity, TestDriveDetailDTO.class);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TestDriveDetailDTO> updateTestDrivesFromVehicle(@PathVariable Long vehicleId,
            @RequestBody List<TestDriveDTO> testDrives) throws EntityNotFoundException {
        List<TestDriveEntity> testDriveEntities = modelMapper.map(testDrives, new TypeToken<List<TestDriveEntity>>() {}.getType());
        List<TestDriveEntity> updatedList = vehicleTestDriveService.updateTestDrivesFromVehicle(vehicleId, testDriveEntities);
        return modelMapper.map(updatedList, new TypeToken<List<TestDriveDetailDTO>>() {}.getType());
    }

    @DeleteMapping(value = "/{testDriveId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTestDriveFromVehicle(@PathVariable Long vehicleId, @PathVariable Long testDriveId)
            throws EntityNotFoundException {
        vehicleTestDriveService.deleteTestDriveFromVehicle(vehicleId, testDriveId);
    }
}