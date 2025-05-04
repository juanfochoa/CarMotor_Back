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
import co.edu.uniandes.dse.carmotor.entities.LocationEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(LocationService.class)
class LocationServiceTest {
    @Autowired
    private LocationService locationService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<LocationEntity> locationList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from LocationEntity").executeUpdate();
        locationList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            LocationEntity locationEntity = factory.manufacturePojo(LocationEntity.class);
            entityManager.persist(locationEntity);
            locationList.add(locationEntity);
        }
    }

    @Test
    void testCreateLocationValid() throws EntityNotFoundException, IllegalOperationException {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        LocationEntity result = locationService.createLocation(newEntity);
        assertNotNull(result);
        LocationEntity storedEntity = entityManager.find(LocationEntity.class, result.getId());
        assertEquals(newEntity.getId(), storedEntity.getId());
        assertEquals(newEntity.getName(), storedEntity.getName());
        assertEquals(newEntity.getAddress(), storedEntity.getAddress());
        assertEquals(newEntity.getPhoneNumber(), storedEntity.getPhoneNumber());
        assertEquals(newEntity.getSchedule(), storedEntity.getSchedule());
    }

    @Test
    void testCreateLocationInvalidNameNull() {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        newEntity.setName(null);
        assertThrows(IllegalOperationException.class, () -> {
            locationService.createLocation(newEntity);
        });
    }

    @Test
    void testCreateLocationInvalidNameEmpty() {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        newEntity.setName("");
        assertThrows(IllegalOperationException.class, () -> {
            locationService.createLocation(newEntity);
        });
    }

    @Test
    void testCreateLocationInvalidAddressNull() {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        newEntity.setAddress(null);
        assertThrows(IllegalOperationException.class, () -> {
            locationService.createLocation(newEntity);
        });
    }

    @Test
    void testCreateLocationInvalidAddressEmpty() {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        newEntity.setAddress("");
        assertThrows(IllegalOperationException.class, () -> {
            locationService.createLocation(newEntity);
        });
    }

    @Test
    void testCreateLocationInvalidPhoneNumberNull() {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        newEntity.setPhoneNumber(null);
        assertThrows(IllegalOperationException.class, () -> {
            locationService.createLocation(newEntity);
        });
    }

    @Test
    void testCreateLocationInvalidPhoneNumberEmpty() {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        newEntity.setPhoneNumber("");
        assertThrows(IllegalOperationException.class, () -> {
            locationService.createLocation(newEntity);
        });
    }

    @Test
    void testCreateLocationInvalidScheduleNull() {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        newEntity.setSchedule(null);
        assertThrows(IllegalOperationException.class, () -> {
            locationService.createLocation(newEntity);
        });
    }

    @Test
    void testCreateLocationInvalidScheduleEmpty() {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        newEntity.setSchedule("");
        assertThrows(IllegalOperationException.class, () -> {
            locationService.createLocation(newEntity);
        });
    }

    @Test
    void testGetLocations() {
        List<LocationEntity> list = locationService.getLocations();
        assertEquals(locationList.size(), list.size());
        for (LocationEntity entity : list) {
            boolean found = locationList.stream()
                    .anyMatch(stored -> stored.getId().equals(entity.getId()));
            assertTrue(found);
        }
    }

    @Test
    void testGetLocationValid() throws EntityNotFoundException {
        LocationEntity entity = locationList.get(0);
        LocationEntity resultEntity = locationService.getLocation(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getName(), resultEntity.getName());
        assertEquals(entity.getAddress(), resultEntity.getAddress());
        assertEquals(entity.getPhoneNumber(), resultEntity.getPhoneNumber());
        assertEquals(entity.getSchedule(), resultEntity.getSchedule());
    }

    @Test
    void testGetLocationInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            locationService.getLocation(0L);
        });
    }

    @Test
    void testUpdateLocationValid() throws EntityNotFoundException, IllegalOperationException {
        LocationEntity entity = locationList.get(0);
        LocationEntity updatedEntity = factory.manufacturePojo(LocationEntity.class);
        updatedEntity.setId(entity.getId());
        LocationEntity result = locationService.updateLocation(entity.getId(), updatedEntity);
        assertNotNull(result);
        LocationEntity storedEntity = entityManager.find(LocationEntity.class, entity.getId());
        assertEquals(updatedEntity.getName(), storedEntity.getName());
        assertEquals(updatedEntity.getAddress(), storedEntity.getAddress());
        assertEquals(updatedEntity.getPhoneNumber(), storedEntity.getPhoneNumber());
        assertEquals(updatedEntity.getSchedule(), storedEntity.getSchedule());
    }

    @Test
    void testUpdateLocationInvalidId() {
        LocationEntity updatedEntity = factory.manufacturePojo(LocationEntity.class);
        updatedEntity.setId(0L);
        assertThrows(EntityNotFoundException.class, () -> {
            locationService.updateLocation(0L, updatedEntity);
        });
    }

    @Test
    void testUpdateLocationInvalidNameNull() {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        newEntity.setName(null);
        assertThrows(IllegalOperationException.class, () -> {
            locationService.updateLocation(locationList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateLocationInvalidNameEmpty() {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        newEntity.setName("");
        assertThrows(IllegalOperationException.class, () -> {
            locationService.updateLocation(locationList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateLocationInvalidAddressNull() {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        newEntity.setAddress(null);
        assertThrows(IllegalOperationException.class, () -> {
            locationService.updateLocation(locationList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateLocationInvalidAddressEmpty() {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        newEntity.setAddress("");
        assertThrows(IllegalOperationException.class, () -> {
            locationService.updateLocation(locationList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateLocationInvalidPhoneNumberNull() {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        newEntity.setPhoneNumber(null);
        assertThrows(IllegalOperationException.class, () -> {
            locationService.updateLocation(locationList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateLocationInvalidPhoneNumberEmpty() {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        newEntity.setPhoneNumber("");
        assertThrows(IllegalOperationException.class, () -> {
            locationService.updateLocation(locationList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateLocationInvalidScheduleNull() {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        newEntity.setSchedule(null);
        assertThrows(IllegalOperationException.class, () -> {
            locationService.updateLocation(locationList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateLocationInvalidScheduleEmpty() {
        LocationEntity newEntity = factory.manufacturePojo(LocationEntity.class);
        newEntity.setSchedule("");
        assertThrows(IllegalOperationException.class, () -> {
            locationService.updateLocation(locationList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testDeleteLocationValid() throws EntityNotFoundException, IllegalOperationException {
        LocationEntity entity = locationList.get(1);
        locationService.deleteLocation(entity.getId());
        LocationEntity deleted = entityManager.find(LocationEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteLocationInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            locationService.deleteLocation(0L);
        });
    }
}