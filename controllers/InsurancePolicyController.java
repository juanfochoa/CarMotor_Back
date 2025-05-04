package co.edu.uniandes.dse.carmotor.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.carmotor.dto.InsurancePolicyDTO;
import co.edu.uniandes.dse.carmotor.entities.InsurancePolicyEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.services.InsurancePolicyService;

@RestController
@RequestMapping("/insurancepolicies")
public class InsurancePolicyController {
    @Autowired
    private InsurancePolicyService insurancepolicyService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InsurancePolicyDTO> findAll() {
        List<InsurancePolicyEntity> insurancePolicies = insurancepolicyService.getInsurancePolicies();
        return modelMapper.map(insurancePolicies, new TypeToken<List<InsurancePolicyDTO>>() {}.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InsurancePolicyDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        InsurancePolicyEntity insurancePolicyEntity = insurancepolicyService.getInsurancePolicy(id);
        return modelMapper.map(insurancePolicyEntity, InsurancePolicyDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InsurancePolicyDTO create(@RequestBody InsurancePolicyDTO insurancePolicyDTO)
            throws IllegalOperationException, EntityNotFoundException {
        InsurancePolicyEntity insurancePolicyEntity = insurancepolicyService
                .createInsurancePolicy(modelMapper.map(insurancePolicyDTO, InsurancePolicyEntity.class));
        return modelMapper.map(insurancePolicyEntity, InsurancePolicyDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InsurancePolicyDTO update(@PathVariable Long id, @RequestBody InsurancePolicyDTO insurancePolicyDTO)
            throws EntityNotFoundException, IllegalOperationException {
        InsurancePolicyEntity insurancePolicyEntity = insurancepolicyService
                .updateInsurancePolicy(id, modelMapper.map(insurancePolicyDTO, InsurancePolicyEntity.class));
        return modelMapper.map(insurancePolicyEntity, InsurancePolicyDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        insurancepolicyService.deleteInsurancePolicy(id);
    }
}