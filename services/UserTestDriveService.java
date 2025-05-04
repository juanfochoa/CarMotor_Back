package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.UserEntity;
import co.edu.uniandes.dse.carmotor.entities.TestDriveEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.UserRepository;
import co.edu.uniandes.dse.carmotor.repositories.TestDriveRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserTestDriveService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TestDriveRepository testDriveRepository;

    @Transactional
    public TestDriveEntity addTestDriveToUser(Long userId, Long testDriveId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Adding test drive {} to user {}", testDriveId, userId);
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        UserEntity user = userEntity.get();

        Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDriveId);
        if (testDriveEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TEST_DRIVE_NOT_FOUND);
        }
        TestDriveEntity testDrive = testDriveEntity.get();

        user.getTestDrives().add(testDrive);
        testDrive.setUser(user);
        log.info("Test drive {} added to user {}", testDriveId, userId);
        return testDrive;
    }
    
    @Transactional
    public List<TestDriveEntity> getTestDrivesFromUser(Long userId) throws EntityNotFoundException {
        log.info("Getting test drives from user {}", userId);
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        UserEntity user = userEntity.get();
        return user.getTestDrives();
    }

    @Transactional
    public TestDriveEntity getTestDriveFromUser(Long userId, Long testDriveId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Getting test drive {} from user {}", testDriveId, userId);
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        UserEntity user = userEntity.get();

        Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDriveId);
        if (testDriveEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TEST_DRIVE_NOT_FOUND);
        }
        TestDriveEntity testDrive = testDriveEntity.get();

        if (!user.getTestDrives().contains(testDrive)) {
            throw new IllegalOperationException("TestDrive isn't related to user with ID: " + userId);
        }
        log.info("Test drive {} retrieved from user {}", testDriveId, userId);
        return testDrive;
    }

    @Transactional
    public List<TestDriveEntity> updateTestDrivesFromUser(Long userId, List<TestDriveEntity> testDriveList) throws EntityNotFoundException, IllegalOperationException {
        log.info("Updating test drives for user {}", userId);
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        UserEntity user = userEntity.get();

        for (TestDriveEntity testDrive : testDriveList) {
            Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDrive.getId());
            if (testDriveEntity.isEmpty()) {
                throw new EntityNotFoundException(ErrorMessage.TEST_DRIVE_NOT_FOUND);
            }
            testDriveEntity.get().setUser(user);
        }
        
        user.setTestDrives(testDriveList);
        log.info("Test drives updated for user {}", userId);
        return user.getTestDrives();
    }

    @Transactional
    public void deleteTestDriveFromUser(Long userId, Long testDriveId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Deleting test drive {} from user {}", testDriveId, userId);
        Optional<UserEntity> userEntity = userRepository.findById(userId);
        if (userEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }
        UserEntity user = userEntity.get();

        Optional<TestDriveEntity> testDriveEntity = testDriveRepository.findById(testDriveId);
        if (testDriveEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.TEST_DRIVE_NOT_FOUND);
        }
        TestDriveEntity testDrive = testDriveEntity.get();

        user.getTestDrives().remove(testDrive);
        testDrive.setUser(null);
        log.info("Test drive {} deleted from user {}", testDriveId, userId);
    }
}