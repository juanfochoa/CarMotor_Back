package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.UserEntity;
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.UserRepository;
import co.edu.uniandes.dse.carmotor.repositories.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserVehicleService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;

    @Transactional
    public VehicleEntity addVehicleToUser(Long userId, Long vehicleId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Adding vehicle {} to user {}", vehicleId, userId);
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        UserEntity user = userEntity.get();

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        user.getVehicles().add(vehicle);
        log.info("Vehicle {} added to user {}", vehicleId, userId);
        return vehicle;
    }

    @Transactional
    public List<VehicleEntity> getVehiclesFromUser(Long userId) throws EntityNotFoundException {
        log.info("Getting vehicles from user {}", userId);
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        UserEntity user = userEntity.get();

        List<VehicleEntity> vehicles = user.getVehicles();
        log.info("Vehicles retrieved from user {}", userId);
        return vehicles;
    }

    @Transactional
    public VehicleEntity getVehicleFromUser(Long userId, Long vehicleId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Getting vehicle {} from user {}", vehicleId, userId);
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        UserEntity user = userEntity.get();

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        if (!user.getVehicles().contains(vehicle)) {
            throw new IllegalOperationException("Vehicle isn't related to user with ID: " + userId);
        }
        log.info("Vehicle {} retrieved from user {}", vehicleId, userId);
        return vehicle;
    }

    @Transactional
    public List<VehicleEntity> updateVehiclesFromUser(Long userId, List<VehicleEntity> vehicleList) throws EntityNotFoundException, IllegalOperationException {
        log.info("Updating vehicles for user {}", userId);
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        UserEntity user = userEntity.get();

        for (VehicleEntity vehicle : vehicleList) {
            Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicle.getId());
            if (vehicleEntity.isEmpty()) {
                throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
            }
        }
        
        user.setVehicles(vehicleList);
        log.info("Vehicles updated for user {}", userId);
        return user.getVehicles();
    }

    @Transactional
    public void deleteVehicleFromUser(Long userId, Long vehicleId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Deleting vehicle {} from user {}", vehicleId, userId);
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        UserEntity user = userEntity.get();

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        user.getVehicles().remove(vehicle);
        log.info("Vehicle {} deleted from user {}", vehicleId, userId);
    }
}