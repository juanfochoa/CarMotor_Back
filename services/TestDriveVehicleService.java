package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.uniandes.dse.carmotor.entities.TestDriveEntity;
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.TestDriveRepository;
import co.edu.uniandes.dse.carmotor.repositories.VehicleRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestDriveVehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private TestDriveRepository testDriveRepository;

    @Transactional
    public VehicleEntity addVehicleToTestDrive(Long testDriveId, Long vehicleId) throws EntityNotFoundException {
        log.info("Adding vehicle {} to test drive {}", vehicleId, testDriveId);
        Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDriveId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);

        if (testDriveEntity.isEmpty()) {
            throw new EntityNotFoundException("Test drive not found");
        }
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException("Vehicle not found");
        }

        testDriveEntity.get().getVehicles().add(vehicleEntity.get());
        vehicleEntity.get().getTestDrives().add(testDriveEntity.get());

        log.info("Vehicle {} added to test drive {}", vehicleId, testDriveId);
        return vehicleEntity.get();
    }

    @Transactional
    public List<VehicleEntity> getVehiclesFromTestDrive(Long testDriveId) throws EntityNotFoundException {
        log.info("Getting vehicles from test drive {}", testDriveId);
        Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDriveId);

        if (testDriveEntity.isEmpty()) {
            throw new EntityNotFoundException("Test drive not found");
        }

        return testDriveEntity.get().getVehicles();
    }

    @Transactional
    public VehicleEntity getVehicleFromTestDrive(Long testDriveId, Long vehicleId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Getting vehicle {} from test drive {}", vehicleId, testDriveId);
        Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDriveId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);

        if (testDriveEntity.isEmpty()) {
            throw new EntityNotFoundException("Test drive not found");
        }
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException("Vehicle not found");
        }
        if (!testDriveEntity.get().getVehicles().contains(vehicleEntity.get())) {
            throw new IllegalOperationException("Vehicle is not associated with this test drive");
        }

        return vehicleEntity.get();
    }

    @Transactional
    public List<VehicleEntity> updateVehiclesFromTestDrive(Long testDriveId, List<VehicleEntity> vehicles)
            throws EntityNotFoundException {
        log.info("Updating vehicles for test drive {}", testDriveId);
        Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDriveId);
        if (testDriveEntity.isEmpty()) {
            throw new EntityNotFoundException("Test drive not found");
        }

        testDriveEntity.get().setVehicles(vehicles);
        return testDriveEntity.get().getVehicles();
    }

    @Transactional
    public void deleteVehicleFromTestDrive(Long testDriveId, Long vehicleId) throws EntityNotFoundException {
        log.info("Deleting vehicle {} from test drive {}", vehicleId, testDriveId);
        Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDriveId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);

        if (testDriveEntity.isEmpty()) {
            throw new EntityNotFoundException("Test drive not found");
        }
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException("Vehicle not found");
        }

        testDriveEntity.get().getVehicles().remove(vehicleEntity.get());
        vehicleEntity.get().getTestDrives().remove(testDriveEntity.get());

        log.info("Vehicle {} deleted from test drive {}", vehicleId, testDriveId);
    }
}