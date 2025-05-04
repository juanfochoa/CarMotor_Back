package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.LocationEntity;
import co.edu.uniandes.dse.carmotor.entities.TestDriveEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.LocationRepository;
import co.edu.uniandes.dse.carmotor.repositories.TestDriveRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocationTestDriveService {
    @Autowired
    private LocationRepository locationRepository;
    
    @Autowired
    private TestDriveRepository testDriveRepository;

    @Transactional
    public TestDriveEntity addTestDriveToLocation(Long locationId, Long testDriveId) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Adding test drive {} to location {}", testDriveId, locationId);
        Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
        if (locationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }
        LocationEntity location = locationEntity.get();

        Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDriveId);
        if (testDriveEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TEST_DRIVE_NOT_FOUND);
        }
        TestDriveEntity testDrive = testDriveEntity.get();

        location.getTestDrives().add(testDrive);
        log.info("Test drive {} added to location {}", testDriveId, locationId);
        return testDrive;
    }
    
    @Transactional
    public List<TestDriveEntity> getTestDrivesFromLocation(Long locationId) throws EntityNotFoundException {
        log.info("Getting test drives from location {}", locationId);
        Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
        if (locationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }
        LocationEntity location = locationEntity.get();

        return location.getTestDrives();
    }

    @Transactional
    public TestDriveEntity getTestDriveFromLocation(Long locationId, Long testDriveId) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Getting test drive {} from location {}", testDriveId, locationId);
        Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
        if (locationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }
        LocationEntity location = locationEntity.get();

        Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDriveId);
        if (testDriveEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TEST_DRIVE_NOT_FOUND);
        }
        TestDriveEntity testDrive = testDriveEntity.get();

        if (!location.getTestDrives().contains(testDrive)) {
            throw new IllegalOperationException("TestDrive isn't related to location with ID: " + locationId);
        }

        return testDrive;
    }

    @Transactional
    public List<TestDriveEntity> updateTestDrivesFromLocation(Long locationId, List<TestDriveEntity> testDriveList) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Updating test drives for location {}", locationId);
        Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
        if (locationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }
        LocationEntity location = locationEntity.get();

        for (TestDriveEntity testDrive : testDriveList) {
            Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDrive.getId());
            if (testDriveEntity.isEmpty()) {
                throw new EntityNotFoundException(ErrorMessage.TEST_DRIVE_NOT_FOUND);
            }
        }
        
        location.setTestDrives(testDriveList);
        
        return location.getTestDrives();
    }

    @Transactional
    public void deleteTestDriveFromLocation(Long locationId, Long testDriveId) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Deleting test drive {} from location {}", testDriveId, locationId);
        Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
        if (locationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }
        LocationEntity location = locationEntity.get();

        Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDriveId);
        if (testDriveEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TEST_DRIVE_NOT_FOUND);
        }
        TestDriveEntity testDrive = testDriveEntity.get();

        location.getTestDrives().remove(testDrive);
        log.info("Test drive {} deleted from location {}", testDriveId, locationId);
    }
}