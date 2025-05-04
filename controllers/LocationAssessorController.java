package co.edu.uniandes.dse.carmotor.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.carmotor.dto.AssessorDTO;
import co.edu.uniandes.dse.carmotor.entities.AssessorEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.services.LocationAssessorService;

@RestController
@RequestMapping("/locations/{locationId}/assessors")
public class LocationAssessorController {
    @Autowired
    private LocationAssessorService locationAssessorService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{assessorId}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public AssessorDTO addAssessorToLocation(@PathVariable Long locationId, @PathVariable Long assessorId)
            throws EntityNotFoundException, IllegalOperationException {
        AssessorEntity assessorEntity = locationAssessorService.addAssessorToLocation(locationId, assessorId);
        return modelMapper.map(assessorEntity, AssessorDTO.class);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AssessorDTO> getAssessorsFromLocation(@PathVariable Long locationId) throws EntityNotFoundException {
        List<AssessorEntity> assessors = locationAssessorService.getAssessorsFromLocation(locationId);
        return modelMapper.map(assessors, new TypeToken<List<AssessorDTO>>() {}.getType());
    }

    @GetMapping(value = "/{assessorId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AssessorDTO getAssessorFromLocation(@PathVariable Long locationId, @PathVariable Long assessorId)
            throws EntityNotFoundException, IllegalOperationException {
        AssessorEntity assessorEntity = locationAssessorService.getAssessorFromLocation(locationId, assessorId);
        return modelMapper.map(assessorEntity, AssessorDTO.class);
    }

    @PutMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AssessorDTO> updateAssessorsFromLocation(@PathVariable Long locationId, @RequestBody List<AssessorDTO> assessors)
            throws EntityNotFoundException, IllegalOperationException {
        List<AssessorEntity> updatedAssessors = locationAssessorService.updateAssessorsFromLocation(locationId,
                modelMapper.map(assessors, new TypeToken<List<AssessorEntity>>() {}.getType()));
        return modelMapper.map(updatedAssessors, new TypeToken<List<AssessorDTO>>() {}.getType());
    }

    @DeleteMapping(value = "/{assessorId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteAssessorFromLocation(@PathVariable Long locationId, @PathVariable Long assessorId)
            throws EntityNotFoundException, IllegalOperationException {
        locationAssessorService.deleteAssessorFromLocation(locationId, assessorId);
    }
}
