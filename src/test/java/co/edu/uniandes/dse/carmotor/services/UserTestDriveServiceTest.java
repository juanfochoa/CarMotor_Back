package co.edu.uniandes.dse.carmotor.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import co.edu.uniandes.dse.carmotor.entities.UserEntity;
import co.edu.uniandes.dse.carmotor.entities.TestDriveEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(UserTestDriveService.class)
public class UserTestDriveServiceTest {
    @Autowired
    private UserTestDriveService userTestDriveService;

    @Autowired 
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<UserEntity> userList = new ArrayList<>();
    private List<TestDriveEntity> testDriveList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from UserEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from TestDriveEntity").executeUpdate();
        userList.clear();
        testDriveList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            TestDriveEntity testDrive = factory.manufacturePojo(TestDriveEntity.class);
            entityManager.persist(testDrive);
            testDriveList.add(testDrive);
        }
        for (int i = 0; i < 3; i++) {
            UserEntity userEntity = factory.manufacturePojo(UserEntity.class);
            entityManager.persist(userEntity);
            userList.add(userEntity);
            if (i == 2) {
                userEntity.getTestDrives().add(testDriveList.get(i));
                testDriveList.get(i).setUser(userEntity);
            }
        }
    }

    @Test
    void testAddTestDriveToUserValid() throws EntityNotFoundException, IllegalOperationException {
        UserEntity userEntity = userList.get(0);
        TestDriveEntity testDriveEntity = testDriveList.get(0);
        TestDriveEntity result = userTestDriveService.addTestDriveToUser(userEntity.getId(), testDriveEntity.getId());
        assertNotNull(result);
        assertEquals(testDriveEntity.getId(), result.getId());
        assertEquals(userEntity, testDriveEntity.getUser());
    }

    @Test
    void testAddTestDriveToUserInvalidUser() {
        assertThrows(EntityNotFoundException.class, () -> {
            userTestDriveService.addTestDriveToUser(0L, testDriveList.get(2).getId());
        });
    }

    @Test
    void testAddTestDriveToUserInvalidTestDrive() {
        assertThrows(EntityNotFoundException.class, () -> {
            userTestDriveService.addTestDriveToUser(userList.get(0).getId(), 0L);
        });
    }

    @Test
    void testGetTestDrivesFromUserValid() throws EntityNotFoundException {
        UserEntity user = userList.get(2);
        List<TestDriveEntity> testDrives = userTestDriveService.getTestDrivesFromUser(user.getId());
        
        assertNotNull(testDrives);
        assertEquals(1, testDrives.size());
        assertEquals(testDriveList.get(2).getId(), testDrives.get(0).getId());
    }

    @Test
    void testGetTestDrivesFromUserEmpty() throws EntityNotFoundException {
        UserEntity user = userList.get(0);
        List<TestDriveEntity> testDrives = userTestDriveService.getTestDrivesFromUser(user.getId());
        
        assertNotNull(testDrives);
        assertTrue(testDrives.isEmpty());
    }

    @Test
    void testGetTestDrivesFromUserNonExistent() {
        assertThrows(EntityNotFoundException.class, () -> {
            userTestDriveService.getTestDrivesFromUser(999L);
        });
    }

    @Test
    void testGetTestDriveFromUserValid() throws EntityNotFoundException, IllegalOperationException {
        UserEntity userEntity = userList.get(2);
        TestDriveEntity testDriveEntity = testDriveList.get(2);
        TestDriveEntity result = userTestDriveService.getTestDriveFromUser(userEntity.getId(), testDriveEntity.getId());
        assertNotNull(result);
        assertEquals(testDriveEntity.getId(), result.getId());
        assertEquals(userEntity, testDriveEntity.getUser());
    }

    @Test
    void testGetTestDriveFromUserInvalidUser() {
        assertThrows(EntityNotFoundException.class, () -> {
            userTestDriveService.getTestDriveFromUser(0L, testDriveList.get(2).getId());
        });
    }

    @Test
    void testGetTestDriveFromUserInvalidTestDrive() {
        assertThrows(EntityNotFoundException.class, () -> {
            userTestDriveService.getTestDriveFromUser(userList.get(2).getId(), 0L);
        });
    }

    @Test
    void testDeleteTestDriveFromUserValid() throws EntityNotFoundException, IllegalOperationException {
        UserEntity userEntity = userList.get(2);
        TestDriveEntity testDriveEntity = testDriveList.get(2);
        assertTrue(userEntity.getTestDrives().contains(testDriveEntity));
        userTestDriveService.deleteTestDriveFromUser(userEntity.getId(), testDriveEntity.getId());
        assertFalse(userEntity.getTestDrives().contains(testDriveEntity));
        assertNull(testDriveEntity.getUser());
    }

    @Test
    void testDeleteTestDriveFromUserInvalidUser() {
        assertThrows(EntityNotFoundException.class, () -> {
            userTestDriveService.deleteTestDriveFromUser(0L, testDriveList.get(2).getId());
        });
    }

    @Test
    void testDeleteTestDriveFromUserInvalidTestDrive() {
        assertThrows(EntityNotFoundException.class, () -> {
            userTestDriveService.deleteTestDriveFromUser(userList.get(2).getId(), 0L);
        });
    }
}