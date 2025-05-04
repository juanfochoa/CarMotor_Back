package co.edu.uniandes.dse.carmotor.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.carmotor.dto.TestDriveDTO;
import co.edu.uniandes.dse.carmotor.entities.TestDriveEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.services.LocationTestDriveService;

@RestController
@RequestMapping("/locations/{locationId}/testdrives")
public class LocationTestDriveController {   
    @Autowired
    private LocationTestDriveService locationTestDriveService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{testDriveId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public TestDriveDTO addTestDriveToLocation(@PathVariable Long locationId, @PathVariable Long testDriveId)
            throws EntityNotFoundException, IllegalOperationException {
        TestDriveEntity testDriveEntity = locationTestDriveService.addTestDriveToLocation(locationId, testDriveId);
        return modelMapper.map(testDriveEntity, TestDriveDTO.class);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<TestDriveDTO> getTestDrivesFromLocation(@PathVariable Long locationId) throws EntityNotFoundException {
        List<TestDriveEntity> testDrives = locationTestDriveService.getTestDrivesFromLocation(locationId);
        return modelMapper.map(testDrives, new TypeToken<List<TestDriveDTO>>() {}.getType());
    }

    @GetMapping(value = "/{testDriveId}")
    @ResponseStatus(code = HttpStatus.OK)
    public TestDriveDTO getTestDriveFromLocation(@PathVariable Long locationId, @PathVariable Long testDriveId)
            throws EntityNotFoundException, IllegalOperationException {
        TestDriveEntity testDriveEntity = locationTestDriveService.getTestDriveFromLocation(locationId, testDriveId);
        return modelMapper.map(testDriveEntity, TestDriveDTO.class);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<TestDriveDTO> updateTestDrivesFromLocation(@PathVariable Long locationId, @RequestBody List<TestDriveDTO> testDrives)
            throws EntityNotFoundException, IllegalOperationException {
        List<TestDriveEntity> updatedTestDrives = locationTestDriveService.updateTestDrivesFromLocation(locationId,
                modelMapper.map(testDrives, new TypeToken<List<TestDriveEntity>>() {}.getType()));
        return modelMapper.map(updatedTestDrives, new TypeToken<List<TestDriveDTO>>() {}.getType());
    }

    @DeleteMapping(value = "/{testDriveId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteTestDriveFromLocation(@PathVariable Long locationId, @PathVariable Long testDriveId)
            throws EntityNotFoundException, IllegalOperationException {
        locationTestDriveService.deleteTestDriveFromLocation(locationId, testDriveId);
    }
}