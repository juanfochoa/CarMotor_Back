package co.edu.uniandes.dse.carmotor.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import co.edu.uniandes.dse.carmotor.entities.TestDriveEntity;
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Import(TestDriveVehicleService.class)
@Transactional
class TestDriveVehicleServiceTest {
    @Autowired
    private TestDriveVehicleService testDriveVehicleService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private TestDriveEntity testDrive;
    private VehicleEntity vehicle;

    @BeforeEach
    void setUp() {
        testDrive = factory.manufacturePojo(TestDriveEntity.class);
        vehicle = factory.manufacturePojo(VehicleEntity.class);
        entityManager.persist(testDrive);
        entityManager.persist(vehicle);
    }

    @Test
    void testAddVehicleToTestDriveValid() throws EntityNotFoundException {
        VehicleEntity added = testDriveVehicleService.addVehicleToTestDrive(testDrive.getId(), vehicle.getId());
        assertNotNull(added);
        assertTrue(testDrive.getVehicles().contains(vehicle));
        assertTrue(vehicle.getTestDrives().contains(testDrive));
    }
    
    @Test
    void testAddVehicleToNonExistentTestDrive() {
        assertThrows(EntityNotFoundException.class, () -> {
            testDriveVehicleService.addVehicleToTestDrive(999L, testDrive.getId());
        });
    }
    
    @Test
    void testAddNonExistentVehicleToTestDrive() {
        assertThrows(EntityNotFoundException.class, () -> {
            testDriveVehicleService.addVehicleToTestDrive(vehicle.getId(), 999L);
        });
    }
    
    @Test
    void testGetVehiclesFromTestDriveValid() throws EntityNotFoundException {
        testDrive.getVehicles().add(vehicle);
        vehicle.getTestDrives().add(testDrive);
        entityManager.persist(testDrive);
        entityManager.persist(vehicle);

        List<VehicleEntity> vehicles = testDriveVehicleService.getVehiclesFromTestDrive(testDrive.getId());
        assertEquals(1, vehicles.size());
        assertEquals(vehicle.getId(), vehicles.get(0).getId());
    }

    @Test
    void testGetVehiclesEmptyListFromTestDrive() throws EntityNotFoundException {
        List<VehicleEntity> vehicles = testDriveVehicleService.getVehiclesFromTestDrive(testDrive.getId());
        assertTrue(vehicles.isEmpty());
    }

    @Test
    void testGetVehicleFromTestDriveValid() throws EntityNotFoundException, IllegalOperationException {
        testDrive.getVehicles().add(vehicle);
        vehicle.getTestDrives().add(testDrive);
        entityManager.persist(testDrive);
        entityManager.persist(vehicle);
        
        VehicleEntity found = testDriveVehicleService.getVehicleFromTestDrive(testDrive.getId(), vehicle.getId());
        assertNotNull(found);
        assertEquals(vehicle.getId(), found.getId());
    }
    
    @Test
    void testGetVehicleNotAssociatedToTestDrive() {
        assertThrows(IllegalOperationException.class, () -> {
            testDriveVehicleService.getVehicleFromTestDrive(testDrive.getId(), vehicle.getId());
        });
    }

    @Test
    void testUpdateVehiclesFromTestDriveValid() throws EntityNotFoundException {
        VehicleEntity newVehicle = factory.manufacturePojo(VehicleEntity.class);
        entityManager.persist(newVehicle);
        List<VehicleEntity> newList = List.of(newVehicle);

        List<VehicleEntity> updated = testDriveVehicleService.updateVehiclesFromTestDrive(testDrive.getId(), newList);
        assertEquals(1, updated.size());
        assertEquals(newVehicle.getId(), updated.get(0).getId());
    }

    @Test
    void testUpdateVehiclesFromNonExistentTestDrive() {
        VehicleEntity newVehicle = factory.manufacturePojo(VehicleEntity.class);
        entityManager.persist(newVehicle);
        List<VehicleEntity> newList = List.of(newVehicle);

        assertThrows(EntityNotFoundException.class, () -> {
            testDriveVehicleService.updateVehiclesFromTestDrive(999L, newList);
        });
    }

    @Test
    void testRemoveVehicleFromTestDriveValid() throws EntityNotFoundException {
        vehicle.getTestDrives().add(testDrive);
        testDrive.getVehicles().add(vehicle);
        entityManager.persist(testDrive);
        entityManager.persist(vehicle);

        testDriveVehicleService.deleteVehicleFromTestDrive(testDrive.getId(), vehicle.getId());
        assertFalse(testDrive.getVehicles().contains(vehicle));
        assertFalse(vehicle.getTestDrives().contains(testDrive));
    }

    @Test
    void testRemoveVehicleFromNonExistentTestDrive() {
        assertThrows(EntityNotFoundException.class, () -> {
            testDriveVehicleService.deleteVehicleFromTestDrive(999L, vehicle.getId());
        });
    }

    @Test
    void testRemoveNonExistentVehicleFromTestDrive() {
        assertThrows(EntityNotFoundException.class, () -> {
            testDriveVehicleService.deleteVehicleFromTestDrive(testDrive.getId(), 999L);
        });
    }
}