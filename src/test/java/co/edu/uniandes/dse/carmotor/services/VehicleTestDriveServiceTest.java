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
@Import(VehicleTestDriveService.class)
@Transactional
class VehicleTestDriveServiceTest {
    @Autowired
    private VehicleTestDriveService vehicleTestDriveService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private VehicleEntity vehicle;
    private TestDriveEntity testDrive;

    @BeforeEach
    void setUp() {
        vehicle = factory.manufacturePojo(VehicleEntity.class);
        testDrive = factory.manufacturePojo(TestDriveEntity.class);
        entityManager.persist(vehicle);
        entityManager.persist(testDrive);
    }

    @Test
    void testAddTestDriveValid() throws EntityNotFoundException {
        TestDriveEntity added = vehicleTestDriveService.addTestDriveToVehicle(vehicle.getId(), testDrive.getId());
        assertNotNull(added);
        assertTrue(vehicle.getTestDrives().contains(testDrive));
        assertTrue(testDrive.getVehicles().contains(vehicle));
    }

    @Test
    void testAddTestDriveToNonExistentVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleTestDriveService.addTestDriveToVehicle(999L, testDrive.getId());
        });
    }

    @Test
    void testAddNonExistentTestDriveToVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleTestDriveService.addTestDriveToVehicle(vehicle.getId(), 999L);
        });
    }
    
    @Test
    void testGetTestDrivesValid() throws EntityNotFoundException {
        vehicle.getTestDrives().add(testDrive);
        testDrive.getVehicles().add(vehicle);
        entityManager.persist(vehicle);
        entityManager.persist(testDrive);

        List<TestDriveEntity> testDrives = vehicleTestDriveService.getTestDrivesFromVehicle(vehicle.getId());
        assertEquals(1, testDrives.size());
        assertEquals(testDrive.getId(), testDrives.get(0).getId());
    }

    @Test
    void testGetTestDrivesEmptyList() throws EntityNotFoundException {
        List<TestDriveEntity> testDrives = vehicleTestDriveService.getTestDrivesFromVehicle(vehicle.getId());
        assertTrue(testDrives.isEmpty());
    }

    @Test
    void testGetTestDriveValid() throws EntityNotFoundException, IllegalOperationException {
        vehicle.getTestDrives().add(testDrive);
        testDrive.getVehicles().add(vehicle);
        entityManager.persist(vehicle);
        entityManager.persist(testDrive);

        TestDriveEntity found = vehicleTestDriveService.getTestDriveFromVehicle(vehicle.getId(), testDrive.getId());
        assertNotNull(found);
        assertEquals(testDrive.getId(), found.getId());
    }

    @Test
    void testGetTestDriveNotAssociated() {
        assertThrows(IllegalOperationException.class, () -> {
            vehicleTestDriveService.getTestDriveFromVehicle(vehicle.getId(), testDrive.getId());
        });
    }

    @Test
    void testUpdateTestDrivesValid() throws EntityNotFoundException {
        TestDriveEntity newTestDrive = factory.manufacturePojo(TestDriveEntity.class);
        entityManager.persist(newTestDrive);
        List<TestDriveEntity> newList = List.of(newTestDrive);

        List<TestDriveEntity> updated = vehicleTestDriveService.updateTestDrivesFromVehicle(vehicle.getId(), newList);
        assertEquals(1, updated.size());
        assertEquals(newTestDrive.getId(), updated.get(0).getId());
    }

    @Test
    void testUpdateTestDrivesNonExistentVehicle() {
        TestDriveEntity newTestDrive = factory.manufacturePojo(TestDriveEntity.class);
        entityManager.persist(newTestDrive);
        List<TestDriveEntity> newList = List.of(newTestDrive);

        assertThrows(EntityNotFoundException.class, () -> {
            vehicleTestDriveService.updateTestDrivesFromVehicle(999L, newList);
        });
    }

    @Test
    void testRemoveTestDriveValid() throws EntityNotFoundException {
        vehicle.getTestDrives().add(testDrive);
        testDrive.getVehicles().add(vehicle);
        entityManager.persist(vehicle);
        entityManager.persist(testDrive);

        vehicleTestDriveService.deleteTestDriveFromVehicle(vehicle.getId(), testDrive.getId());
        assertFalse(vehicle.getTestDrives().contains(testDrive));
        assertFalse(testDrive.getVehicles().contains(vehicle));
    }

    @Test
    void testRemoveTestDriveFromNonExistentVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleTestDriveService.deleteTestDriveFromVehicle(999L, testDrive.getId());
        });
    }

    @Test
    void testRemoveNonExistentTestDriveFromVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleTestDriveService.deleteTestDriveFromVehicle(vehicle.getId(), 999L);
        });
    }
}