package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.BankingEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.repositories.BankingRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BankingService {
    @Autowired
    BankingRepository bankingRepository;

    @Transactional
    public BankingEntity createBanking(BankingEntity bankingEntity) throws IllegalOperationException {
        log.info("The bank creation process begins");

        if (bankingEntity.getName() == null || bankingEntity.getName().trim().isEmpty()) {
            throw new IllegalOperationException("Invalid bank, name not found");
        }
        if (bankingEntity.getUriLogo() == null || bankingEntity.getUriLogo().trim().isEmpty()) {
            throw new IllegalOperationException("Invalid bank, URI not found");
        }
        if (bankingEntity.getAssessorPhone() == null || bankingEntity.getAssessorPhone().trim().isEmpty()) {
            throw new IllegalOperationException("Invalid bank, assesor not found");
        }

        log.info("The bank creation process ends");
        return bankingRepository.save(bankingEntity);
    }

    @Transactional
    public List<BankingEntity> getBankings() {
        log.info("The process of getting all banks begins");
        return bankingRepository.findAll();
    }

    @Transactional
    public BankingEntity getBanking(Long bankingId) throws EntityNotFoundException {
        log.info("The process of getting the bank with ID = {0} begins", bankingId);

        Optional<BankingEntity> bankingEntity = bankingRepository.findById(bankingId);
        if (bankingEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.BANKING_NOT_FOUND);

        log.info("The process of getting the bank with ID = {0} ends", bankingId);
        return bankingEntity.get();
    }

    @Transactional
    public BankingEntity updateBanking(Long bankingId, BankingEntity banking) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("The process of updating the bank with ID = {0} begins", bankingId);

        Optional<BankingEntity> bankingEntity = bankingRepository.findById(bankingId);
        if (bankingEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.BANKING_NOT_FOUND);
        }

        if (banking.getName() == null || banking.getName().length() == 0) {
            throw new IllegalOperationException("Bank name is not valid");
        }
        if (banking.getUriLogo() == null || banking.getUriLogo().length() == 0) {
            throw new IllegalOperationException("Bank address is not valid");
        }
        if (banking.getAssessorPhone() == null || banking.getAssessorPhone().length() == 0) {
            throw new IllegalOperationException("Bank phone number is not valid");
        }
    
        banking.setId(bankingId);
        
        log.info("The process of updating the bank with ID = {0} ends", bankingId);
        return bankingRepository.save(banking);
    }

    @Transactional
    public void deleteBanking(Long bankingId) throws EntityNotFoundException, IllegalOperationException {
        log.info("The process of deleting the bank with ID = {0} begins", bankingId);

        Optional<BankingEntity> bankingEntity = bankingRepository.findById(bankingId);
        if (bankingEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.BANKING_NOT_FOUND);
    
        bankingRepository.deleteById(bankingId);
        
        log.info("The process of deleting the bank with ID = {0} ends", bankingId);
    }    
}