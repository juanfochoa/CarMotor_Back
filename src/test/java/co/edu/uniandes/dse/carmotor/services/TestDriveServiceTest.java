package co.edu.uniandes.dse.carmotor.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import co.edu.uniandes.dse.carmotor.entities.TestDriveEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;

@DataJpaTest
@Transactional
@Import(TestDriveService.class)
class TestDriveServiceTest {
    @Autowired
    private TestDriveService testDriveService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<TestDriveEntity> testDriveList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from TestDriveEntity").executeUpdate();
        testDriveList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            TestDriveEntity testDriveEntity = factory.manufacturePojo(TestDriveEntity.class);
            entityManager.persist(testDriveEntity);
            testDriveList.add(testDriveEntity);
        }
    }

    @Test
    void testCreateTestDriveValid() throws EntityNotFoundException, IllegalOperationException {
        TestDriveEntity newEntity = factory.manufacturePojo(TestDriveEntity.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2999);
        Date validDate = calendar.getTime();
        newEntity.setDate(validDate);
        TestDriveEntity result = testDriveService.createTestDrive(newEntity);
        assertNotNull(result);
        TestDriveEntity storedEntity = entityManager.find(TestDriveEntity.class, result.getId());
        assertEquals(newEntity.getId(), storedEntity.getId());
        assertEquals(newEntity.getDate(), storedEntity.getDate());
    }

    @Test
    void testCreateTestDriveWithInvalidDateNull() {
        assertThrows(IllegalOperationException.class, () -> {
            TestDriveEntity newEntity = factory.manufacturePojo(TestDriveEntity.class);
            newEntity.setDate(null);
            testDriveService.createTestDrive(newEntity);
        });
    }

    @Test
    void testCreateTestDriveWithInvalidDateBefore() {
        assertThrows(IllegalOperationException.class, () -> {
            TestDriveEntity newEntity = factory.manufacturePojo(TestDriveEntity.class);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 1910);
            Date invalidDate = calendar.getTime();
            newEntity.setDate(invalidDate);
            testDriveService.createTestDrive(newEntity);
        });
    }

    @Test
    void testGetTestDrivesValid() {
        List<TestDriveEntity> list = testDriveService.getTestDrives();
        assertEquals(testDriveList.size(), list.size());
        for (TestDriveEntity entity : list) {
            boolean found = testDriveList.stream().anyMatch(stored -> stored.getId().equals(entity.getId()));
            assertTrue(found);
        }
    }

    @Test
    void testGetTestDriveValid() throws EntityNotFoundException {
        TestDriveEntity entity = testDriveList.get(0);
        TestDriveEntity resultEntity = testDriveService.getTestDrive(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getDate(), resultEntity.getDate());
    }

    @Test
    void testGetTestDriveInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> {
            testDriveService.getTestDrive(0L);
        });
    }

    @Test
    void testUpdateTestDriveValid() throws EntityNotFoundException, IllegalOperationException {
        TestDriveEntity entity = testDriveList.get(0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2999);
        Date validDate = calendar.getTime();
        TestDriveEntity updatedEntity = factory.manufacturePojo(TestDriveEntity.class);
        updatedEntity.setId(entity.getId());
        updatedEntity.setDate(validDate);
        testDriveService.updateTestDrive(entity.getId(), updatedEntity);

        TestDriveEntity resp = entityManager.find(TestDriveEntity.class, entity.getId());
        assertEquals(updatedEntity.getId(), resp.getId());
        assertEquals(updatedEntity.getDate(), resp.getDate());
    }

    @Test
    void testUpdateTestDriveInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> {
            TestDriveEntity updatedEntity = factory.manufacturePojo(TestDriveEntity.class);
            updatedEntity.setId(0L);
            testDriveService.updateTestDrive(0L, updatedEntity);
        });
    }

    @Test
    void testUpdateTestDriveWithInvalidDateNull() {
        assertThrows(IllegalOperationException.class, () -> {
            TestDriveEntity newEntity = factory.manufacturePojo(TestDriveEntity.class);
            newEntity.setDate(null);
            testDriveService.updateTestDrive(testDriveList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateTestDriveWithInvalidDateBefore() {
        assertThrows(IllegalOperationException.class, () -> {
            TestDriveEntity newEntity = factory.manufacturePojo(TestDriveEntity.class);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 1910);
            Date invalidDate = calendar.getTime();
            newEntity.setDate(invalidDate);
            testDriveService.updateTestDrive(testDriveList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testDeleteTestDriveValid() throws EntityNotFoundException {
        TestDriveEntity entity = testDriveList.get(1);
        testDriveService.deleteTestDrive(entity.getId());
        TestDriveEntity deleted = entityManager.find(TestDriveEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteTestDriveInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> {
            testDriveService.deleteTestDrive(0L);
        });
    }
}