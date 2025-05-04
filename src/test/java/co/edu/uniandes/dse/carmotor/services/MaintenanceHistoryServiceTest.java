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
import co.edu.uniandes.dse.carmotor.entities.MaintenanceHistoryEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MaintenanceHistoryService.class)
class MaintenanceHistoryServiceTest {
    @Autowired
    private MaintenanceHistoryService maintenanceHistoryService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<MaintenanceHistoryEntity> maintenanceHistoryList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MaintenanceHistoryEntity").executeUpdate();
        maintenanceHistoryList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            MaintenanceHistoryEntity maintenanceHistoryEntity = factory.manufacturePojo(MaintenanceHistoryEntity.class);
            entityManager.persist(maintenanceHistoryEntity);
            maintenanceHistoryList.add(maintenanceHistoryEntity);
        }
    }

    @Test
    void testCreateMaintenanceHistoryValid() throws EntityNotFoundException, IllegalOperationException {
        MaintenanceHistoryEntity newEntity = factory.manufacturePojo(MaintenanceHistoryEntity.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1910);
        Date date = calendar.getTime();
        newEntity.setDate(date);
        MaintenanceHistoryEntity result = maintenanceHistoryService.createMaintenanceHistory(newEntity);
        assertNotNull(result);
        MaintenanceHistoryEntity storedEntity = entityManager.find(MaintenanceHistoryEntity.class, result.getId());
        assertEquals(newEntity.getId(), storedEntity.getId());
        assertEquals(newEntity.getDate(), storedEntity.getDate());
        assertEquals(newEntity.getType(), storedEntity.getType());
        assertEquals(newEntity.getAddress(), storedEntity.getAddress());
    }

    @Test
    void testCreateMaintenanceHistoryWithInvalidDateNull() {
        assertThrows(IllegalOperationException.class, () -> {
            MaintenanceHistoryEntity newEntity = factory.manufacturePojo(MaintenanceHistoryEntity.class);
            newEntity.setDate(null);
            maintenanceHistoryService.createMaintenanceHistory(newEntity);
        });
    }

    @Test
    void testCreateMaintenanceHistoryWithInvalidDateFuture() {
        assertThrows(IllegalOperationException.class, () -> {
            MaintenanceHistoryEntity newEntity = factory.manufacturePojo(MaintenanceHistoryEntity.class);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2999);
            Date futureDate = calendar.getTime();
            newEntity.setDate(futureDate);
            maintenanceHistoryService.createMaintenanceHistory(newEntity);
        });
    }

    @Test
    void testCreateMaintenanceHistoryWithInvalidTypeNull() {
        assertThrows(IllegalOperationException.class, () -> {
            MaintenanceHistoryEntity newEntity = factory.manufacturePojo(MaintenanceHistoryEntity.class);
            newEntity.setType(null);
            maintenanceHistoryService.createMaintenanceHistory(newEntity);
        });
    }

    @Test
    void testCreateMaintenanceHistoryWithInvalidTypeEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            MaintenanceHistoryEntity newEntity = factory.manufacturePojo(MaintenanceHistoryEntity.class);
            newEntity.setType("");
            maintenanceHistoryService.createMaintenanceHistory(newEntity);
        });
    }

    @Test
    void testCreateMaintenanceHistoryWithInvalidAddressNull() {
        assertThrows(IllegalOperationException.class, () -> {
            MaintenanceHistoryEntity newEntity = factory.manufacturePojo(MaintenanceHistoryEntity.class);
            newEntity.setAddress(null);
            maintenanceHistoryService.createMaintenanceHistory(newEntity);
        });
    }

    @Test
    void testCreateMaintenanceHistoryWithInvalidAddressEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            MaintenanceHistoryEntity newEntity = factory.manufacturePojo(MaintenanceHistoryEntity.class);
            newEntity.setAddress("");
            maintenanceHistoryService.createMaintenanceHistory(newEntity);
        });
    }

    @Test
    void testGetMaintenanceHistoriesValid() {
        List<MaintenanceHistoryEntity> list = maintenanceHistoryService.getMaintenanceHistories();
        assertEquals(maintenanceHistoryList.size(), list.size());
        for (MaintenanceHistoryEntity entity : list) {
            boolean found = maintenanceHistoryList.stream().anyMatch(stored -> stored.getId().equals(entity.getId()));
            assertTrue(found);
        }
    }

    @Test
    void testGetMaintenanceHistoryValid() throws EntityNotFoundException {
        MaintenanceHistoryEntity entity = maintenanceHistoryList.get(0);
        MaintenanceHistoryEntity resultEntity = maintenanceHistoryService.getMaintenanceHistory(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getDate(), resultEntity.getDate());
        assertEquals(entity.getType(), resultEntity.getType());
        assertEquals(entity.getAddress(), resultEntity.getAddress());
    }

    @Test
    void testGetMaintenanceHistoryInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> {
            maintenanceHistoryService.getMaintenanceHistory(0L);
        });
    }

    @Test
    void testUpdateMaintenanceHistoryValid() throws EntityNotFoundException, IllegalOperationException {
        MaintenanceHistoryEntity entity = maintenanceHistoryList.get(0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1910);
        Date date = calendar.getTime();
        MaintenanceHistoryEntity updatedEntity = factory.manufacturePojo(MaintenanceHistoryEntity.class);
        updatedEntity.setId(entity.getId());
        updatedEntity.setDate(date);
        maintenanceHistoryService.updateMaintenanceHistory(entity.getId(), updatedEntity);

        MaintenanceHistoryEntity resp = entityManager.find(MaintenanceHistoryEntity.class, entity.getId());
        assertEquals(updatedEntity.getId(), resp.getId());
        assertEquals(updatedEntity.getDate(), resp.getDate());
        assertEquals(updatedEntity.getType(), resp.getType());
        assertEquals(updatedEntity.getAddress(), resp.getAddress());
    }

    @Test
    void testUpdateMaintenanceHistoryInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> {
            MaintenanceHistoryEntity updatedEntity = factory.manufacturePojo(MaintenanceHistoryEntity.class);
            updatedEntity.setId(0L);
            maintenanceHistoryService.updateMaintenanceHistory(0L, updatedEntity);
        });
    }

    @Test
    void testUpdateMaintenanceHistoryWithInvalidDateNull() {
        assertThrows(IllegalOperationException.class, () -> {
            MaintenanceHistoryEntity newEntity = factory.manufacturePojo(MaintenanceHistoryEntity.class);
            newEntity.setDate(null);
            maintenanceHistoryService.updateMaintenanceHistory(maintenanceHistoryList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateMaintenanceHistoryWithInvalidDateFuture() {
        assertThrows(IllegalOperationException.class, () -> {
            MaintenanceHistoryEntity newEntity = factory.manufacturePojo(MaintenanceHistoryEntity.class);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2999);
            Date futureDate = calendar.getTime();
            newEntity.setDate(futureDate);
            maintenanceHistoryService.updateMaintenanceHistory(maintenanceHistoryList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateMaintenanceHistoryWithInvalidTypeNull() {
        assertThrows(IllegalOperationException.class, () -> {
            MaintenanceHistoryEntity newEntity = factory.manufacturePojo(MaintenanceHistoryEntity.class);
            newEntity.setType(null);
            maintenanceHistoryService.updateMaintenanceHistory(maintenanceHistoryList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateMaintenanceHistoryWithInvalidTypeEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            MaintenanceHistoryEntity newEntity = factory.manufacturePojo(MaintenanceHistoryEntity.class);
            newEntity.setType("");
            maintenanceHistoryService.updateMaintenanceHistory(maintenanceHistoryList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateMaintenanceHistoryWithInvalidAddressNull() {
        assertThrows(IllegalOperationException.class, () -> {
            MaintenanceHistoryEntity newEntity = factory.manufacturePojo(MaintenanceHistoryEntity.class);
            newEntity.setAddress(null);
            maintenanceHistoryService.updateMaintenanceHistory(maintenanceHistoryList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateMaintenanceHistoryWithInvalidAddressEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            MaintenanceHistoryEntity newEntity = factory.manufacturePojo(MaintenanceHistoryEntity.class);
            newEntity.setAddress("");
            maintenanceHistoryService.updateMaintenanceHistory(maintenanceHistoryList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testDeleteMaintenanceHistoryValid() throws EntityNotFoundException, IllegalOperationException {
        MaintenanceHistoryEntity entity = maintenanceHistoryList.get(1);
        maintenanceHistoryService.deleteMaintenanceHistory(entity.getId());
        MaintenanceHistoryEntity deleted = entityManager.find(MaintenanceHistoryEntity.class, entity.getId());
        assertNull(deleted);
    }
    
    @Test
    void testDeleteMaintenanceHistoryInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> {
            maintenanceHistoryService.deleteMaintenanceHistory(0L);
        });
    }
}