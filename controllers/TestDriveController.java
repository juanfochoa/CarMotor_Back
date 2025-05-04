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
import co.edu.uniandes.dse.carmotor.services.TestDriveService;

@RestController
@RequestMapping("/testdrives")
public class TestDriveController {
    @Autowired
    private TestDriveService testDriveService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<TestDriveDetailDTO> findAll() {
        List<TestDriveEntity> testDrives = testDriveService.getTestDrives();
        return modelMapper.map(testDrives, new TypeToken<List<TestDriveDetailDTO>>() {}.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public TestDriveDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        TestDriveEntity testDriveEntity = testDriveService.getTestDrive(id);
        return modelMapper.map(testDriveEntity, TestDriveDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public TestDriveDTO create(@RequestBody TestDriveDTO testDriveDTO)
            throws IllegalOperationException, EntityNotFoundException {
        TestDriveEntity testDriveEntity = testDriveService.createTestDrive(
                modelMapper.map(testDriveDTO, TestDriveEntity.class));
        return modelMapper.map(testDriveEntity, TestDriveDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public TestDriveDTO update(@PathVariable Long id, @RequestBody TestDriveDTO testDriveDTO)
            throws EntityNotFoundException, IllegalOperationException {
        TestDriveEntity testDriveEntity = testDriveService.updateTestDrive(
                id, modelMapper.map(testDriveDTO, TestDriveEntity.class));
        return modelMapper.map(testDriveEntity, TestDriveDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        testDriveService.deleteTestDrive(id);
    }
}