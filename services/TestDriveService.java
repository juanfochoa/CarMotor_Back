package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.TestDriveEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.TestDriveRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestDriveService {
    @Autowired
    private TestDriveRepository testDriveRepository;

    @Transactional
    public TestDriveEntity createTestDrive(TestDriveEntity testDriveEntity) throws EntityNotFoundException, IllegalOperationException {
        log.info("The test drive creation process begins");

        if (testDriveEntity.getDate() == null || testDriveEntity.getDate().before(new Date())) {
            throw new IllegalOperationException("Test drive date is not valid");
        }
    
        log.info("The test drive creation process ends");
        return testDriveRepository.save(testDriveEntity);
    }
    
    @Transactional
    public List<TestDriveEntity> getTestDrives() {
        log.info("The process of getting all test drives begins");
        return testDriveRepository.findAll();
    }
    
    @Transactional
    public TestDriveEntity getTestDrive(Long testDriveId) throws EntityNotFoundException {
        log.info("The process of getting the test drive with ID = {0} begins", testDriveId);

        Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDriveId);
        if (testDriveEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TEST_DRIVE_NOT_FOUND);
        }
        
        log.info("The process of retrieving the test drive with ID = {0} ends", testDriveId);
        return testDriveEntity.get();
    }
    
    @Transactional
    public TestDriveEntity updateTestDrive(Long testDriveId, TestDriveEntity testDrive) throws EntityNotFoundException, IllegalOperationException {
        log.info("The process of updating the test drive with ID = {0} begins", testDriveId);

        Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDriveId);
        if (testDriveEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TEST_DRIVE_NOT_FOUND);
        }

        if (testDrive.getDate() == null || testDrive.getDate().before(new Date())) {
            throw new IllegalOperationException("Test drive date is not valid");
        }

        testDrive.setId(testDriveId);

        log.info("The process of updating the test drive with ID = {0} ends", testDriveId);
        return testDriveRepository.save(testDrive);
    }
    
    @Transactional
    public void deleteTestDrive(Long testDriveId) throws EntityNotFoundException {
        log.info("The process of deleting the test drive with ID = {0} begins", testDriveId);

        Optional<TestDriveEntity> testDrive = testDriveRepository.findById(testDriveId);
        if (testDrive.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TEST_DRIVE_NOT_FOUND);
        }

        TestDriveEntity testDriveEntity = getTestDrive(testDriveId);
        testDriveRepository.delete(testDriveEntity);

        log.info("The process of deleting the test drive with ID = {0} ends", testDriveId);
    }    
}