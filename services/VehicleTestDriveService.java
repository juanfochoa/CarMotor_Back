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
public class VehicleTestDriveService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private TestDriveRepository testDriveRepository;

    @Transactional
    public TestDriveEntity addTestDriveToVehicle(Long vehicleId, Long testDriveId) throws EntityNotFoundException {
        log.info("Adding test drive {} to vehicle {}", testDriveId, vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDriveId);

        if (vehicleEntity.isEmpty())
            throw new EntityNotFoundException("Vehicle not found");
        if (testDriveEntity.isEmpty())
            throw new EntityNotFoundException("Test drive not found");

        vehicleEntity.get().getTestDrives().add(testDriveEntity.get());
        testDriveEntity.get().getVehicles().add(vehicleEntity.get());

        log.info("Test drive {} added to vehicle {}", testDriveId, vehicleId);
        return testDriveEntity.get();
    }

    @Transactional
    public List<TestDriveEntity> getTestDrivesFromVehicle(Long vehicleId) throws EntityNotFoundException {
        log.info("Getting test drives from vehicle {}", vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);

        if (vehicleEntity.isEmpty())
            throw new EntityNotFoundException("Vehicle not found");

        return vehicleEntity.get().getTestDrives();
    }

    @Transactional
    public TestDriveEntity getTestDriveFromVehicle(Long vehicleId, Long testDriveId)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Getting test drive {} from vehicle {}", testDriveId, vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDriveId);

        if (vehicleEntity.isEmpty())
            throw new EntityNotFoundException("Vehicle not found");
        if (testDriveEntity.isEmpty())
            throw new EntityNotFoundException("Test drive not found");

        if (!vehicleEntity.get().getTestDrives().contains(testDriveEntity.get()))
            throw new IllegalOperationException("Test drive is not associated with this vehicle");

        return testDriveEntity.get();
    }

    @Transactional
    public List<TestDriveEntity> updateTestDrivesFromVehicle(Long vehicleId, List<TestDriveEntity> testDrives)
            throws EntityNotFoundException {
        log.info("Updating test drives for vehicle {}", vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty())
            throw new EntityNotFoundException("Vehicle not found");

        vehicleEntity.get().setTestDrives(testDrives);
        return vehicleEntity.get().getTestDrives();
    }

    @Transactional
    public void deleteTestDriveFromVehicle(Long vehicleId, Long testDriveId) throws EntityNotFoundException {
        log.info("Deleting test drive {} from vehicle {}", testDriveId, vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDriveId);

        if (vehicleEntity.isEmpty())
            throw new EntityNotFoundException("Vehicle not found");
        if (testDriveEntity.isEmpty())
            throw new EntityNotFoundException("Test drive not found");

        vehicleEntity.get().getTestDrives().remove(testDriveEntity.get());
        testDriveEntity.get().getVehicles().remove(vehicleEntity.get());

        log.info("Test drive {} deleted from vehicle {}", testDriveId, vehicleId);
    }
}