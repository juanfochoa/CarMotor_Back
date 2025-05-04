package co.edu.uniandes.dse.carmotor.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.carmotor.dto.AssessorDTO;
import co.edu.uniandes.dse.carmotor.dto.AssessorDetailDTO;
import co.edu.uniandes.dse.carmotor.entities.AssessorEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.services.AssessorService;

@RestController
@RequestMapping("/assessors")
public class AssessorController {
    @Autowired
    private AssessorService assessorService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AssessorDetailDTO> findAll() {
        List<AssessorEntity> assessors = assessorService.getAssessors();
        return modelMapper.map(assessors, new TypeToken<List<AssessorDetailDTO>>() {}.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public AssessorDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        AssessorEntity assessorEntity = assessorService.getAssessor(id);
        return modelMapper.map(assessorEntity, AssessorDetailDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AssessorDTO create(@RequestBody AssessorDTO assessorDTO) throws IllegalOperationException, EntityNotFoundException {
        AssessorEntity assessorEntity = assessorService.createAssessor(modelMapper.map(assessorDTO, AssessorEntity.class));
        return modelMapper.map(assessorEntity, AssessorDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public AssessorDTO update(@PathVariable Long id, @RequestBody AssessorDTO assessorDTO)
            throws EntityNotFoundException, IllegalOperationException {
        AssessorEntity assessorEntity = assessorService.updateAssessor(id, modelMapper.map(assessorDTO, AssessorEntity.class));
        return modelMapper.map(assessorEntity, AssessorDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        assessorService.deleteAssessor(id);
    }
}