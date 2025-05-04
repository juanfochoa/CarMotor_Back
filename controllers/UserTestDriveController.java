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
import co.edu.uniandes.dse.carmotor.services.UserTestDriveService;

@RestController
@RequestMapping("/users/{userId}/testdrives")
public class UserTestDriveController {
    @Autowired
    private UserTestDriveService userTestDriveService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{testDriveId}")
    @ResponseStatus(HttpStatus.CREATED)
    public TestDriveDTO addTestDriveToUser(@PathVariable Long userId, @PathVariable Long testDriveId)
            throws IllegalOperationException, EntityNotFoundException {
        TestDriveEntity testDrive = userTestDriveService.addTestDriveToUser(userId, testDriveId);
        return modelMapper.map(testDrive, TestDriveDTO.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TestDriveDTO> getTestDrivesFromUser(@PathVariable Long userId) throws EntityNotFoundException {
        List<TestDriveEntity> testDrives = userTestDriveService.getTestDrivesFromUser(userId);
        return modelMapper.map(testDrives, new TypeToken<List<TestDriveDTO>>() {}.getType());
    }

    @GetMapping(value = "/{testDriveId}")
    @ResponseStatus(HttpStatus.OK)
    public TestDriveDTO getTestDriveFromUser(@PathVariable Long userId, @PathVariable Long testDriveId)
            throws EntityNotFoundException, IllegalOperationException {
        TestDriveEntity testDrive = userTestDriveService.getTestDriveFromUser(userId, testDriveId);
        return modelMapper.map(testDrive, TestDriveDTO.class);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TestDriveDTO> updateTestDrivesFromUser(@PathVariable Long userId, @RequestBody List<TestDriveDTO> testDrives)
            throws EntityNotFoundException, IllegalOperationException {
        List<TestDriveEntity> updatedTestDrives = userTestDriveService.updateTestDrivesFromUser(userId,
                modelMapper.map(testDrives, new TypeToken<List<TestDriveEntity>>() {}.getType()));
        return modelMapper.map(updatedTestDrives, new TypeToken<List<TestDriveDTO>>() {}.getType());
    }

    @DeleteMapping(value = "/{testDriveId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTestDriveFromUser(@PathVariable Long userId, @PathVariable Long testDriveId)
            throws EntityNotFoundException, IllegalOperationException {
        userTestDriveService.deleteTestDriveFromUser(userId, testDriveId);
    }
}