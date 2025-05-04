package co.edu.uniandes.dse.carmotor.services;

import java.util.Optional;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.InsurancePolicyEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.repositories.InsurancePolicyRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InsurancePolicyService {
    @Autowired
    private InsurancePolicyRepository insurancePolicyRepository;
    
    @Transactional
    public InsurancePolicyEntity createInsurancePolicy(InsurancePolicyEntity insurancePolicyEntity)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Insurance policy creation process starts");

        if (insurancePolicyEntity.getPrice() == null || 
            Double.isNaN(insurancePolicyEntity.getPrice()) || 
            Double.isInfinite(insurancePolicyEntity.getPrice())) {
            throw new IllegalOperationException("Insurance policy price is not valid");
        }
        if (insurancePolicyEntity.getDuration() == null || 
            insurancePolicyEntity.getDuration().before(new Date())) {
            throw new IllegalOperationException("Insurance policy duration is not valid");
        }
        if (insurancePolicyEntity.getPremiumRate() == null || 
            Double.isNaN(insurancePolicyEntity.getPremiumRate()) || 
            Double.isInfinite(insurancePolicyEntity.getPremiumRate())) {
            throw new IllegalOperationException("Insurance policy premium rate is not valid");
        }
        if (insurancePolicyEntity.getInsuranceCompany() == null || 
            insurancePolicyEntity.getInsuranceCompany().length() == 0) {
            throw new IllegalOperationException("Insurance policy's insurance company is not valid");
        }
        
        log.info("Insurance policy creation process ends");
        return insurancePolicyRepository.save(insurancePolicyEntity);
    }
    
    @Transactional
    public List<InsurancePolicyEntity> getInsurancePolicies() {
        log.info("The process of getting all insurance policies begins");
        return insurancePolicyRepository.findAll();
    }
    
    @Transactional
    public InsurancePolicyEntity getInsurancePolicy(Long insurancePolicyId) throws EntityNotFoundException {
        log.info("The process of getting the insurance policy with ID = {0} begins", insurancePolicyId);

        Optional<InsurancePolicyEntity> insurancePolicyEntity = insurancePolicyRepository.findById(insurancePolicyId);
        if (insurancePolicyEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.INSURANCE_POLICY_NOT_FOUND);

        log.info("The process of getting the insurance policy with ID = {0} ends", insurancePolicyId);
        return insurancePolicyEntity.get();
    }
    
    @Transactional
    public InsurancePolicyEntity updateInsurancePolicy(Long insurancePolicyId, InsurancePolicyEntity insurancePolicy)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("The process of updating the insurance policy with ID = {0} begins", insurancePolicyId);

        Optional<InsurancePolicyEntity> insurancePolicyEntity = insurancePolicyRepository.findById(insurancePolicyId);
        if (insurancePolicyEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.INSURANCE_POLICY_NOT_FOUND);

        if (insurancePolicy.getPrice() == null || 
            Double.isNaN(insurancePolicy.getPrice()) || 
            Double.isInfinite(insurancePolicy.getPrice())) {
            throw new IllegalOperationException("Insurance policy price is not valid");
        }
        if (insurancePolicy.getDuration() == null || 
            insurancePolicy.getDuration().before(new Date())) {
            throw new IllegalOperationException("Insurance policy duration is not valid");
        }
        if (insurancePolicy.getPremiumRate() == null || 
            Double.isNaN(insurancePolicy.getPremiumRate()) || 
            Double.isInfinite(insurancePolicy.getPremiumRate())) {
            throw new IllegalOperationException("Insurance policy premium rate is not valid");
        }
        if (insurancePolicy.getInsuranceCompany() == null || 
            insurancePolicy.getInsuranceCompany().length() == 0) {
            throw new IllegalOperationException("Insurance policy's insurance company is not valid");
        }
        
        insurancePolicy.setId(insurancePolicyId);
        
        log.info("The process of updating the insurance policy with ID = {0} ends", insurancePolicyId);
        return insurancePolicyRepository.save(insurancePolicy);
    }
    
    @Transactional
    public void deleteInsurancePolicy(Long insurancePolicyId) throws EntityNotFoundException, IllegalOperationException {
        log.info("The process of deleting the insurance policy with ID = {0} begins", insurancePolicyId);

        Optional<InsurancePolicyEntity> insurancePolicyEntity = insurancePolicyRepository.findById(insurancePolicyId);
        if (insurancePolicyEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.INSURANCE_POLICY_NOT_FOUND);
        
        insurancePolicyRepository.deleteById(insurancePolicyId);

        log.info("The process of deleting the insurance policy with ID = {0} ends", insurancePolicyId);
    }    
}