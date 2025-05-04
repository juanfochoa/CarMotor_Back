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
import co.edu.uniandes.dse.carmotor.services.VehicleInsurancePolicyService;

@RestController
@RequestMapping("/vehicles/{vehicleId}/insurancepolicies")
public class VehicleInsurancePolicyController {
    @Autowired
    private VehicleInsurancePolicyService vehicleInsurancePolicyService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{insurancePolicyId}")
    @ResponseStatus(HttpStatus.CREATED)
    public InsurancePolicyDTO addInsurancePolicyToVehicle(@PathVariable Long vehicleId, @PathVariable Long insurancePolicyId)
            throws EntityNotFoundException, IllegalOperationException {
        InsurancePolicyEntity insurancePolicyEntity = vehicleInsurancePolicyService.addInsurancePolicyToVehicle(vehicleId, insurancePolicyId);
        return modelMapper.map(insurancePolicyEntity, InsurancePolicyDTO.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InsurancePolicyDTO> getInsurancePoliciesFromVehicle(@PathVariable Long vehicleId)
            throws EntityNotFoundException {
        List<InsurancePolicyEntity> insurancePolicies = vehicleInsurancePolicyService.getInsurancePoliciesFromVehicle(vehicleId);
        return modelMapper.map(insurancePolicies, new TypeToken<List<InsurancePolicyDTO>>() {}.getType());
    }

    @GetMapping(value = "/{insurancePolicyId}")
    @ResponseStatus(HttpStatus.OK)
    public InsurancePolicyDTO getInsurancePolicyFromVehicle(@PathVariable Long vehicleId, @PathVariable Long insurancePolicyId)
            throws EntityNotFoundException, IllegalOperationException {
        InsurancePolicyEntity insurancePolicyEntity = vehicleInsurancePolicyService.getInsurancePolicyFromVehicle(vehicleId, insurancePolicyId);
        return modelMapper.map(insurancePolicyEntity, InsurancePolicyDTO.class);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InsurancePolicyDTO> updateInsurancePoliciesFromVehicle(@PathVariable Long vehicleId,
            @RequestBody List<InsurancePolicyDTO> insurancePolicies)
            throws EntityNotFoundException, IllegalOperationException {
        List<InsurancePolicyEntity> updatedInsurancePolicies = vehicleInsurancePolicyService.updateInsurancePoliciesFromVehicle(
                vehicleId, modelMapper.map(insurancePolicies, new TypeToken<List<InsurancePolicyEntity>>() {}.getType()));
        return modelMapper.map(updatedInsurancePolicies, new TypeToken<List<InsurancePolicyDTO>>() {}.getType());
    }

    @DeleteMapping(value = "/{insurancePolicyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInsurancePolicyFromVehicle(@PathVariable Long vehicleId, @PathVariable Long insurancePolicyId)
            throws EntityNotFoundException, IllegalOperationException {
        vehicleInsurancePolicyService.deleteInsurancePolicyFromVehicle(vehicleId, insurancePolicyId);
    }
}