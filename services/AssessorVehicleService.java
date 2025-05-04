package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.AssessorEntity;
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.AssessorRepository;
import co.edu.uniandes.dse.carmotor.repositories.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssessorVehicleService {
    @Autowired
    private AssessorRepository assessorRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;

    @Transactional
    public VehicleEntity addVehicleToAssessor(Long assessorId, Long vehicleId) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Adding vehicle {} to assessor {}", vehicleId, assessorId);
        Optional<AssessorEntity> assessorEntity = assessorRepository.findById(assessorId);
        if (assessorEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSESSOR_NOT_FOUND);
        }
        AssessorEntity assessor = assessorEntity.get();

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        assessor.getVehicles().add(vehicle);
        vehicle.setAssessor(assessor);
        log.info("Vehicle {} added to assessor {}", vehicleId, assessorId);
        return vehicle;
    }
    
    @Transactional
    public List<VehicleEntity> getVehiclesFromAssessor(Long assessorId) 
            throws EntityNotFoundException {
        log.info("Getting vehicles from assessor {}", assessorId);
        Optional<AssessorEntity> assessorEntity = assessorRepository.findById(assessorId);
        if (assessorEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSESSOR_NOT_FOUND);
        }
        AssessorEntity assessor = assessorEntity.get();

        return assessor.getVehicles();
    }

    @Transactional
    public VehicleEntity getVehicleFromAssessor(Long assessorId, Long vehicleId) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Getting vehicle {} from assessor {}", vehicleId, assessorId);
        Optional<AssessorEntity> assessorEntity = assessorRepository.findById(assessorId);
        if (assessorEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSESSOR_NOT_FOUND);
        }
        AssessorEntity assessor = assessorEntity.get();

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        if (!assessor.getVehicles().contains(vehicle)) {
            throw new IllegalOperationException("Vehicle isn't related to assessor with ID: " + assessorId);
        }

        return vehicle;
    }

    @Transactional
    public List<VehicleEntity> updateVehiclesFromAssessor(Long assessorId, List<VehicleEntity> vehicleList) 
            throws EntityNotFoundException {
        log.info("Updating vehicles for assessor {}", assessorId);
        Optional<AssessorEntity> assessorEntity = assessorRepository.findById(assessorId);
        if (assessorEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSESSOR_NOT_FOUND);
        }
        AssessorEntity assessor = assessorEntity.get();

        for (VehicleEntity vehicle : vehicleList) {
            Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicle.getId());
            if (vehicleEntity.isEmpty()) {
                throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
            }
            vehicleEntity.get().setAssessor(assessor);
        }
        
        assessor.setVehicles(vehicleList);
        
        return assessor.getVehicles();
    }

    @Transactional
    public void deleteVehicleFromAssessor(Long assessorId, Long vehicleId) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Deleting vehicle {} from assessor {}", vehicleId, assessorId);
        Optional<AssessorEntity> assessorEntity = assessorRepository.findById(assessorId);
        if (assessorEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSESSOR_NOT_FOUND);
        }
        AssessorEntity assessor = assessorEntity.get();

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        if (!assessor.getVehicles().contains(vehicle)) {
            throw new IllegalOperationException("Vehicle isn't related to assessor with ID: " + assessorId);
        }

        assessor.getVehicles().remove(vehicle);
        vehicle.setAssessor(null);
        log.info("Vehicle {} deleted from assessor {}", vehicleId, assessorId);
    }
}