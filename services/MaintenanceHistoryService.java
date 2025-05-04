package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.MaintenanceHistoryEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.MaintenanceHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MaintenanceHistoryService {
    @Autowired
    private MaintenanceHistoryRepository maintenanceHistoryRepository;
    
    @Transactional
    public MaintenanceHistoryEntity createMaintenanceHistory(MaintenanceHistoryEntity maintenanceHistoryEntity)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Starting maintenance history creation process");

        if (maintenanceHistoryEntity.getDate() == null || maintenanceHistoryEntity.getDate().after(new Date())) {
            throw new IllegalOperationException("Maintenance history date is not valid");
        }
        if (maintenanceHistoryEntity.getType() == null || maintenanceHistoryEntity.getType().length() == 0) {
            throw new IllegalOperationException("Maintenance history type is not valid");
        }
        if (maintenanceHistoryEntity.getAddress() == null || maintenanceHistoryEntity.getAddress().length() == 0) {
            throw new IllegalOperationException("Maintenance history address is not valid");
        }

        log.info("Maintenance history creation process finished");
        return maintenanceHistoryRepository.save(maintenanceHistoryEntity);
    }

    @Transactional
    public List<MaintenanceHistoryEntity> getMaintenanceHistories() {
        log.info("Consultation process for all maintenance histories begins");
        return maintenanceHistoryRepository.findAll();
    }

    @Transactional
    public MaintenanceHistoryEntity getMaintenanceHistory(Long maintenanceHistoryId) throws EntityNotFoundException {
        log.info("Starting query process for maintenance history with id = {0}", maintenanceHistoryId);

        Optional<MaintenanceHistoryEntity> maintenanceHistoryEntity = maintenanceHistoryRepository.findById(maintenanceHistoryId);
        if (maintenanceHistoryEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.MAINTENANCE_HISTORY_NOT_FOUND);

        return maintenanceHistoryEntity.get();
    }

    @Transactional
    public MaintenanceHistoryEntity updateMaintenanceHistory(Long maintenanceHistoryId, MaintenanceHistoryEntity maintenanceHistory)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Starting the process of updating the maintenance history with id = {0}", maintenanceHistoryId);

        Optional<MaintenanceHistoryEntity> maintenanceHistoryEntity = maintenanceHistoryRepository.findById(maintenanceHistoryId);
        if (maintenanceHistoryEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.MAINTENANCE_HISTORY_NOT_FOUND);
        }

        if (maintenanceHistory.getDate() == null || maintenanceHistory.getDate().after(new Date())) {
            throw new IllegalOperationException("Maintenance history date is not valid");
        }
        if (maintenanceHistory.getType() == null || maintenanceHistory.getType().length() == 0) {
            throw new IllegalOperationException("Maintenance history type is not valid");
        }
        if (maintenanceHistory.getAddress() == null || maintenanceHistory.getAddress().length() == 0) {
            throw new IllegalOperationException("Maintenance history address is not valid");
        }

        maintenanceHistory.setId(maintenanceHistoryId);
        
        log.info("Finishing the process of updating the maintenance history with id = {0}", maintenanceHistoryId);
        return maintenanceHistoryRepository.save(maintenanceHistory);
    }
    
    @Transactional
    public void deleteMaintenanceHistory(Long maintenanceHistoryId) throws EntityNotFoundException, IllegalOperationException {
        log.info("The process of deleting the maintenance history with ID = {0} begins", maintenanceHistoryId);

        Optional<MaintenanceHistoryEntity> maintenance = maintenanceHistoryRepository.findById(maintenanceHistoryId);
        if (maintenance.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.MAINTENANCE_HISTORY_NOT_FOUND);
        }
        MaintenanceHistoryEntity maintenanceHistoryEntity = maintenance.get();
        maintenanceHistoryRepository.delete(maintenanceHistoryEntity);

        log.info("Finishing the process of deleting the maintenance history with id = {0}", maintenanceHistoryId);
    } 
}