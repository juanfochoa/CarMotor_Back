package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.entities.MaintenanceHistoryEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.VehicleRepository;
import co.edu.uniandes.dse.carmotor.repositories.MaintenanceHistoryRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VehicleMaintenanceHistoryService {
    @Autowired
    private VehicleRepository vehicleRepository;
    
    @Autowired
    private MaintenanceHistoryRepository maintenanceHistoryRepository;

    @Transactional
    public MaintenanceHistoryEntity addMaintenanceHistoryToVehicle(Long vehicleId, Long maintenanceHistoryId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Adding maintenance history {} to vehicle {}", maintenanceHistoryId, vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        Optional<MaintenanceHistoryEntity> maintenanceHistoryEntity = maintenanceHistoryRepository.findById(maintenanceHistoryId);
        if (maintenanceHistoryEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.MAINTENANCE_HISTORY_NOT_FOUND);
        }
        MaintenanceHistoryEntity maintenanceHistory = maintenanceHistoryEntity.get();

        vehicle.getMaintenances().add(maintenanceHistory);
        log.info("Maintenance history {} added to vehicle {}", maintenanceHistoryId, vehicleId);
        return maintenanceHistory;
    }
    
    @Transactional
    public List<MaintenanceHistoryEntity> getMaintenanceHistoriesFromVehicle(Long vehicleId)
            throws EntityNotFoundException {
        log.info("Getting maintenance histories from vehicle {}", vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();
        List<MaintenanceHistoryEntity> maintenances = vehicle.getMaintenances();
        log.info("Maintenance histories retrieved from vehicle {}", vehicleId);
        return maintenances;
    }

    @Transactional
    public MaintenanceHistoryEntity getMaintenanceHistoryFromVehicle(Long vehicleId, Long maintenanceHistoryId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Getting maintenance history {} from vehicle {}", maintenanceHistoryId, vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        Optional<MaintenanceHistoryEntity> maintenanceHistoryEntity = maintenanceHistoryRepository.findById(maintenanceHistoryId);
        if (maintenanceHistoryEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.MAINTENANCE_HISTORY_NOT_FOUND);
        }
        MaintenanceHistoryEntity maintenanceHistory = maintenanceHistoryEntity.get();

        if (!vehicle.getMaintenances().contains(maintenanceHistory)) {
            throw new IllegalOperationException("MaintenanceHistory isn't related to vehicle with ID: " + vehicleId);
        }
        log.info("Maintenance history {} retrieved from vehicle {}", maintenanceHistoryId, vehicleId);
        return maintenanceHistory;
    }

    @Transactional
    public List<MaintenanceHistoryEntity> updateMaintenanceHistoriesFromVehicle(Long vehicleId, List<MaintenanceHistoryEntity> maintenanceHistoryList)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Updating maintenance histories for vehicle {}", vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        for (MaintenanceHistoryEntity maintenanceHistory : maintenanceHistoryList) {
            Optional<MaintenanceHistoryEntity> maintenanceHistoryEntity = maintenanceHistoryRepository.findById(maintenanceHistory.getId());
            if (maintenanceHistoryEntity.isEmpty()) {
                throw new EntityNotFoundException(ErrorMessage.MAINTENANCE_HISTORY_NOT_FOUND);
            }
        }
        
        vehicle.setMaintenances(maintenanceHistoryList);
        log.info("Maintenance histories updated for vehicle {}", vehicleId);
        return vehicle.getMaintenances();
    }

    @Transactional
    public void deleteMaintenanceHistoryFromVehicle(Long vehicleId, Long maintenanceHistoryId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Deleting maintenance history {} from vehicle {}", maintenanceHistoryId, vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        Optional<MaintenanceHistoryEntity> maintenanceHistoryEntity = maintenanceHistoryRepository.findById(maintenanceHistoryId);
        if (maintenanceHistoryEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.MAINTENANCE_HISTORY_NOT_FOUND);
        }
        MaintenanceHistoryEntity maintenanceHistory = maintenanceHistoryEntity.get();

        vehicle.getMaintenances().remove(maintenanceHistory);
        log.info("Maintenance history {} deleted from vehicle {}", maintenanceHistoryId, vehicleId);
    }
}