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
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.entities.MaintenanceHistoryEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(VehicleMaintenanceHistoryService.class)
public class VehicleMaintenanceHistoryServiceTest {
    @Autowired
    private VehicleMaintenanceHistoryService vehicleMaintenanceHistoryService;

    @Autowired 
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<VehicleEntity> vehicleList = new ArrayList<>();
    private List<MaintenanceHistoryEntity> maintenanceHistoryList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from VehicleEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MaintenanceHistoryEntity").executeUpdate();
        vehicleList.clear();
        maintenanceHistoryList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            MaintenanceHistoryEntity maintenanceHistory = factory.manufacturePojo(MaintenanceHistoryEntity.class);
            entityManager.persist(maintenanceHistory);
            maintenanceHistoryList.add(maintenanceHistory);
        }
        for (int i = 0; i < 3; i++) {
            VehicleEntity vehicle = factory.manufacturePojo(VehicleEntity.class);
            entityManager.persist(vehicle);
            vehicleList.add(vehicle);
            if (i == 2) {
                vehicle.getMaintenances().add(maintenanceHistoryList.get(i));
            }
        }
    }

    @Test
    void testAddMaintenanceHistoryToVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicle = vehicleList.get(0);
        MaintenanceHistoryEntity maintenanceHistory = maintenanceHistoryList.get(0);
        MaintenanceHistoryEntity result = vehicleMaintenanceHistoryService.addMaintenanceHistoryToVehicle(vehicle.getId(), maintenanceHistory.getId());
        assertNotNull(result);
        assertEquals(maintenanceHistory.getId(), result.getId());
    }

    @Test
    void testAddMaintenanceHistoryToVehicleInvalidVehicle() {
        MaintenanceHistoryEntity maintenanceHistory = maintenanceHistoryList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleMaintenanceHistoryService.addMaintenanceHistoryToVehicle(0L, maintenanceHistory.getId());
        });
    }

    @Test
    void testAddMaintenanceHistoryToVehicleInvalidMaintenanceHistory() {
        VehicleEntity vehicle = vehicleList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleMaintenanceHistoryService.addMaintenanceHistoryToVehicle(vehicle.getId(), 0L);
        });
    }
    
    @Test
    void testGetMaintenanceHistoriesFromVehicleValid() throws EntityNotFoundException {
        VehicleEntity vehicle = vehicleList.get(2);
        List<MaintenanceHistoryEntity> list = vehicleMaintenanceHistoryService.getMaintenanceHistoriesFromVehicle(vehicle.getId());
        assertNotNull(list);
        assertEquals(vehicle.getMaintenances().size(), list.size());
        for (MaintenanceHistoryEntity history : list) {
            assertTrue(vehicle.getMaintenances().contains(history));
        }
    }

    @Test
    void testGetMaintenanceHistoriesFromVehicleInvalidVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleMaintenanceHistoryService.getMaintenanceHistoriesFromVehicle(0L);
        });
    }

    @Test
    void testGetMaintenanceHistoryFromVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicle = vehicleList.get(2);
        MaintenanceHistoryEntity maintenanceHistory = maintenanceHistoryList.get(2);
        MaintenanceHistoryEntity result = vehicleMaintenanceHistoryService.getMaintenanceHistoryFromVehicle(vehicle.getId(), maintenanceHistory.getId());
        assertNotNull(result);
        assertEquals(maintenanceHistory.getId(), result.getId());
    }

    @Test
    void testGetMaintenanceHistoryFromVehicleInvalidVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleMaintenanceHistoryService.getMaintenanceHistoryFromVehicle(0L, maintenanceHistoryList.get(2).getId());
        });
    }

    @Test
    void testGetMaintenanceHistoryFromVehicleInvalidMaintenanceHistory() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleMaintenanceHistoryService.getMaintenanceHistoryFromVehicle(vehicleList.get(2).getId(), 0L);
        });
    }

    @Test
    void testUpdateMaintenanceHistoriesFromVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicle = vehicleList.get(0);
        List<MaintenanceHistoryEntity> newHistoryList = new ArrayList<>();
        newHistoryList.add(maintenanceHistoryList.get(0));
        newHistoryList.add(maintenanceHistoryList.get(1));
        List<MaintenanceHistoryEntity> updatedList = vehicleMaintenanceHistoryService.updateMaintenanceHistoriesFromVehicle(vehicle.getId(), newHistoryList);
        assertNotNull(updatedList);
        assertEquals(newHistoryList.size(), updatedList.size());
        for (MaintenanceHistoryEntity history : newHistoryList) {
            assertTrue(updatedList.contains(history));
        }
    }

    @Test
    void testUpdateMaintenanceHistoriesFromVehicleInvalidVehicle() {
        List<MaintenanceHistoryEntity> newHistoryList = new ArrayList<>();
        newHistoryList.add(maintenanceHistoryList.get(0));
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleMaintenanceHistoryService.updateMaintenanceHistoriesFromVehicle(0L, newHistoryList);
        });
    }

    @Test
    void testUpdateMaintenanceHistoriesFromVehicleInvalidHistory() {
        VehicleEntity vehicle = vehicleList.get(0);
        MaintenanceHistoryEntity invalidHistory = factory.manufacturePojo(MaintenanceHistoryEntity.class);
        invalidHistory.setId(0L);
        List<MaintenanceHistoryEntity> newHistoryList = new ArrayList<>();
        newHistoryList.add(invalidHistory);
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleMaintenanceHistoryService.updateMaintenanceHistoriesFromVehicle(vehicle.getId(), newHistoryList);
        });
    }

    @Test
    void testDeleteMaintenanceHistoryFromVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicle = vehicleList.get(2);
        MaintenanceHistoryEntity history = maintenanceHistoryList.get(2);
        assertTrue(vehicle.getMaintenances().contains(history));
        vehicleMaintenanceHistoryService.deleteMaintenanceHistoryFromVehicle(vehicle.getId(), history.getId());
        assertFalse(vehicle.getMaintenances().contains(history));
    }

    @Test
    void testDeleteMaintenanceHistoryFromVehicleInvalidVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleMaintenanceHistoryService.deleteMaintenanceHistoryFromVehicle(0L, maintenanceHistoryList.get(2).getId());
        });
    }

    @Test
    void testDeleteMaintenanceHistoryFromVehicleInvalidHistory() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleMaintenanceHistoryService.deleteMaintenanceHistoryFromVehicle(vehicleList.get(2).getId(), 0L);
        });
    }
}