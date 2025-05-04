package co.edu.uniandes.dse.carmotor.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import co.edu.uniandes.dse.carmotor.entities.LocationEntity;
import co.edu.uniandes.dse.carmotor.entities.TestDriveEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(LocationTestDriveService.class)
public class LocationTestDriveServiceTest {
    @Autowired
    private LocationTestDriveService locationTestDriveService;

    @Autowired 
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    
    private List<LocationEntity> locationList = new ArrayList<>();
    private List<TestDriveEntity> testDriveList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from LocationEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from TestDriveEntity").executeUpdate();
        locationList.clear();
        testDriveList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            TestDriveEntity testDrive = factory.manufacturePojo(TestDriveEntity.class);
            entityManager.persist(testDrive);
            testDriveList.add(testDrive);
        }
        for (int i = 0; i < 3; i++) {
            LocationEntity locationEntity = factory.manufacturePojo(LocationEntity.class);
            entityManager.persist(locationEntity);
            locationList.add(locationEntity);
            if (i == 2) {
                locationEntity.getTestDrives().add(testDriveList.get(i));
            }
        }
    }

    @Test
    void testAddTestDriveToLocationValid() throws EntityNotFoundException, IllegalOperationException {
        LocationEntity locationEntity = locationList.get(0);
        TestDriveEntity testDriveEntity = testDriveList.get(0);
        TestDriveEntity result = locationTestDriveService.addTestDriveToLocation(locationEntity.getId(), testDriveEntity.getId());
    
        assertNotNull(result);
        assertEquals(testDriveEntity.getId(), result.getId());
    }
    
    @Test
    void testAddTestDriveToLocationInvalidLocation() {
        TestDriveEntity testDriveEntity = testDriveList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            locationTestDriveService.addTestDriveToLocation(0L, testDriveEntity.getId());
        });
    }
    
    @Test
    void testAddTestDriveToLocationInvalidTestDrive() {
        LocationEntity locationEntity = locationList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            locationTestDriveService.addTestDriveToLocation(locationEntity.getId(), 0L);
        });
    }	
    
    @Test
    void testGetTestDrivesFromLocationValid() throws EntityNotFoundException {
        LocationEntity locationEntity = locationList.get(2);
        List<TestDriveEntity> testDrives = locationTestDriveService.getTestDrivesFromLocation(locationEntity.getId());
        
        assertNotNull(testDrives);
        assertEquals(1, testDrives.size());
        assertEquals(testDriveList.get(2).getId(), testDrives.get(0).getId());
    }

    @Test
    void testGetTestDrivesFromLocationInvalidLocation() {
        assertThrows(EntityNotFoundException.class, () -> {
            locationTestDriveService.getTestDrivesFromLocation(0L);
        });
    }

    @Test
    void testGetTestDriveFromLocationValid() throws EntityNotFoundException, IllegalOperationException {
        LocationEntity locationEntity = locationList.get(2);
        TestDriveEntity testDriveEntity = testDriveList.get(2);
        TestDriveEntity result = locationTestDriveService.getTestDriveFromLocation(locationEntity.getId(), testDriveEntity.getId());

        assertNotNull(result);
        assertEquals(testDriveEntity.getId(), result.getId());
    }

    @Test
    void testGetTestDriveFromLocationInvalidLocation() {
        assertThrows(EntityNotFoundException.class, () -> {
            locationTestDriveService.getTestDriveFromLocation(0L, testDriveList.get(2).getId());
        });
    }

    @Test
    void testGetTestDriveFromLocationInvalidTestDrive() {
        assertThrows(EntityNotFoundException.class, () -> {
            locationTestDriveService.getTestDriveFromLocation(locationList.get(2).getId(), 0L);
        });
    }

    @Test
    void testGetTestDriveFromLocationNotRelated() {
        LocationEntity locationEntity = locationList.get(0);
        TestDriveEntity testDriveEntity = testDriveList.get(0);
        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> {
            locationTestDriveService.getTestDriveFromLocation(locationEntity.getId(), testDriveEntity.getId());
        });
        assertEquals("TestDrive isn't related to location with ID: " + locationEntity.getId(), exception.getMessage());
    }

    @Test
    void testUpdateTestDrivesFromLocationValid() throws EntityNotFoundException, IllegalOperationException {
        LocationEntity locationEntity = locationList.get(0);
        List<TestDriveEntity> newTestDriveList = new ArrayList<>();
        newTestDriveList.add(testDriveList.get(0));
        newTestDriveList.add(testDriveList.get(1));
        
        List<TestDriveEntity> result = locationTestDriveService.updateTestDrivesFromLocation(locationEntity.getId(), newTestDriveList);
        
        assertNotNull(result);
        assertEquals(newTestDriveList.size(), result.size());
        for (int i = 0; i < newTestDriveList.size(); i++) {
            assertEquals(newTestDriveList.get(i).getId(), result.get(i).getId());
        }
    }

    @Test
    void testUpdateTestDrivesFromLocationInvalidLocation() {
        List<TestDriveEntity> newTestDriveList = new ArrayList<>();
        newTestDriveList.add(testDriveList.get(0));
        
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            locationTestDriveService.updateTestDrivesFromLocation(0L, newTestDriveList);
        });
        assertEquals(ErrorMessage.LOCATION_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testUpdateTestDrivesFromLocationInvalidTestDrive() {
        LocationEntity locationEntity = locationList.get(0);
        TestDriveEntity invalidTestDrive = factory.manufacturePojo(TestDriveEntity.class);
        invalidTestDrive.setId(0L);
        
        List<TestDriveEntity> newTestDriveList = new ArrayList<>();
        newTestDriveList.add(invalidTestDrive);
        
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            locationTestDriveService.updateTestDrivesFromLocation(locationEntity.getId(), newTestDriveList);
        });
        assertEquals(ErrorMessage.TEST_DRIVE_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testDeleteTestDriveFromLocationValid() throws EntityNotFoundException, IllegalOperationException {
        LocationEntity locationEntity = locationList.get(2);
        TestDriveEntity testDriveEntity = testDriveList.get(2);
        assertTrue(locationEntity.getTestDrives().contains(testDriveEntity));
        locationTestDriveService.deleteTestDriveFromLocation(locationEntity.getId(), testDriveEntity.getId());
        assertTrue(!locationEntity.getTestDrives().contains(testDriveEntity));
    }

    @Test
    void testDeleteTestDriveFromLocationInvalidLocation() {
        assertThrows(EntityNotFoundException.class, () -> {
            locationTestDriveService.deleteTestDriveFromLocation(0L, testDriveList.get(2).getId());
        });
    }

    @Test
    void testDeleteTestDriveFromLocationInvalidTestDrive() {
        assertThrows(EntityNotFoundException.class, () -> {
            locationTestDriveService.deleteTestDriveFromLocation(locationList.get(2).getId(), 0L);
        });
    }
}