package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.entities.BankingEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.VehicleRepository;
import co.edu.uniandes.dse.carmotor.repositories.BankingRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VehicleBankingService {
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private BankingRepository bankingRepository;

    @Transactional
    public BankingEntity addBankingToVehicle(Long vehicleId, Long bankingId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Adding banking {} to vehicle {}", bankingId, vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        Optional<BankingEntity> bankingEntity = bankingRepository.findById(bankingId);
        if (bankingEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.BANKING_NOT_FOUND);
        }
        BankingEntity banking = bankingEntity.get();

        vehicle.getBanks().add(banking);
        log.info("Banking {} added to vehicle {}", bankingId, vehicleId);
        return banking;
    }

    @Transactional
    public List<BankingEntity> getBankingsFromVehicle(Long vehicleId) throws EntityNotFoundException {
        log.info("Getting bankings from vehicle {}", vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();
        List<BankingEntity> banks = vehicle.getBanks();
        log.info("Bankings retrieved from vehicle {}", vehicleId);
        return banks;
    }

    @Transactional
    public BankingEntity getBankingFromVehicle(Long vehicleId, Long bankingId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Getting banking {} from vehicle {}", bankingId, vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        Optional<BankingEntity> bankingEntity = bankingRepository.findById(bankingId);
        if (bankingEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.BANKING_NOT_FOUND);
        }
        BankingEntity banking = bankingEntity.get();

        if (!vehicle.getBanks().contains(banking)) {
            throw new IllegalOperationException("Banking isn't related to vehicle with ID: " + vehicleId);
        }
        log.info("Banking {} retrieved from vehicle {}", bankingId, vehicleId);
        return banking;
    }

    @Transactional
    public List<BankingEntity> updateBankingsFromVehicle(Long vehicleId, List<BankingEntity> bankingList) throws EntityNotFoundException, IllegalOperationException {
        log.info("Updating bankings for vehicle {}", vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        for (BankingEntity banking : bankingList) {
            Optional<BankingEntity> bankingEntity = bankingRepository.findById(banking.getId());
            if (bankingEntity.isEmpty()) {
                throw new EntityNotFoundException(ErrorMessage.BANKING_NOT_FOUND);
            }
        }
        
        vehicle.setBanks(bankingList);
        log.info("Bankings updated for vehicle {}", vehicleId);
        return vehicle.getBanks();
    }

    @Transactional
    public void deleteBankingFromVehicle(Long vehicleId, Long bankingId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Deleting banking {} from vehicle {}", bankingId, vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        Optional<BankingEntity> bankingEntity = bankingRepository.findById(bankingId);
        if (bankingEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.BANKING_NOT_FOUND);
        }
        BankingEntity banking = bankingEntity.get();

        vehicle.getBanks().remove(banking);
        log.info("Banking {} deleted from vehicle {}", bankingId, vehicleId);
    }
}