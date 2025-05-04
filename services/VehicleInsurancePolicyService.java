package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.entities.InsurancePolicyEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.VehicleRepository;
import co.edu.uniandes.dse.carmotor.repositories.InsurancePolicyRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VehicleInsurancePolicyService {
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private InsurancePolicyRepository insurancePolicyRepository;

    @Transactional
    public InsurancePolicyEntity addInsurancePolicyToVehicle(Long vehicleId, Long insurancePolicyId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Adding insurance policy {} to vehicle {}", insurancePolicyId, vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        Optional<InsurancePolicyEntity> insurancePolicyEntity = insurancePolicyRepository.findById(insurancePolicyId);
        if (insurancePolicyEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.INSURANCE_POLICY_NOT_FOUND);
        }
        InsurancePolicyEntity insurancePolicy = insurancePolicyEntity.get();

        vehicle.getInsurancePolicies().add(insurancePolicy);
        log.info("Insurance policy {} added to vehicle {}", insurancePolicyId, vehicleId);
        return insurancePolicy;
    }
    
    @Transactional
    public List<InsurancePolicyEntity> getInsurancePoliciesFromVehicle(Long vehicleId)
            throws EntityNotFoundException {
        log.info("Getting insurance policies from vehicle {}", vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();
        List<InsurancePolicyEntity> policies = vehicle.getInsurancePolicies();
        log.info("Insurance policies retrieved from vehicle {}", vehicleId);
        return policies;
    }

    @Transactional
    public InsurancePolicyEntity getInsurancePolicyFromVehicle(Long vehicleId, Long insurancePolicyId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Getting insurance policy {} from vehicle {}", insurancePolicyId, vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        Optional<InsurancePolicyEntity> insurancePolicyEntity = insurancePolicyRepository.findById(insurancePolicyId);
        if (insurancePolicyEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.INSURANCE_POLICY_NOT_FOUND);
        }
        InsurancePolicyEntity insurancePolicy = insurancePolicyEntity.get();

        if (!vehicle.getInsurancePolicies().contains(insurancePolicy)) {
            throw new IllegalOperationException("InsurancePolicy isn't related to vehicle with ID: " + vehicleId);
        }
        log.info("Insurance policy {} retrieved from vehicle {}", insurancePolicyId, vehicleId);
        return insurancePolicy;
    }

    @Transactional
    public List<InsurancePolicyEntity> updateInsurancePoliciesFromVehicle(Long vehicleId, List<InsurancePolicyEntity> insurancePolicyList)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Updating insurance policies for vehicle {}", vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        for (InsurancePolicyEntity insurancePolicy : insurancePolicyList) {
            Optional<InsurancePolicyEntity> insurancePolicyEntity = insurancePolicyRepository.findById(insurancePolicy.getId());
            if (insurancePolicyEntity.isEmpty()) {
                throw new EntityNotFoundException(ErrorMessage.INSURANCE_POLICY_NOT_FOUND);
            }
        }
        
        vehicle.setInsurancePolicies(insurancePolicyList);
        log.info("Insurance policies updated for vehicle {}", vehicleId);
        return vehicle.getInsurancePolicies();
    }

    @Transactional
    public void deleteInsurancePolicyFromVehicle(Long vehicleId, Long insurancePolicyId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Deleting insurance policy {} from vehicle {}", insurancePolicyId, vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        Optional<InsurancePolicyEntity> insurancePolicyEntity = insurancePolicyRepository.findById(insurancePolicyId);
        if (insurancePolicyEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.INSURANCE_POLICY_NOT_FOUND);
        }
        InsurancePolicyEntity insurancePolicy = insurancePolicyEntity.get();

        vehicle.getInsurancePolicies().remove(insurancePolicy);
        log.info("Insurance policy {} deleted from vehicle {}", insurancePolicyId, vehicleId);
    }
}