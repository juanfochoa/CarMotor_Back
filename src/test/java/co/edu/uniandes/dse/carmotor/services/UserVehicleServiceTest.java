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
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(UserVehicleService.class)
public class UserVehicleServiceTest {
    @Autowired
    private UserVehicleService userVehicleService;

    @Autowired 
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<UserEntity> userList = new ArrayList<>();
    private List<VehicleEntity> vehicleList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from UserEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VehicleEntity").executeUpdate();
        userList.clear();
        vehicleList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            VehicleEntity vehicle = factory.manufacturePojo(VehicleEntity.class);
            entityManager.persist(vehicle);
            vehicleList.add(vehicle);
        }
        for (int i = 0; i < 3; i++) {
            UserEntity userEntity = factory.manufacturePojo(UserEntity.class);
            entityManager.persist(userEntity);
            userList.add(userEntity);
            if (i == 2) {
                userEntity.getVehicles().add(vehicleList.get(i));
            }
        }
    }

    @Test
    void testAddVehicleToUserValid() throws EntityNotFoundException, IllegalOperationException {
        UserEntity userEntity = userList.get(0);
        VehicleEntity vehicleEntity = vehicleList.get(0);
        VehicleEntity result = userVehicleService.addVehicleToUser(userEntity.getId(), vehicleEntity.getId());
        assertNotNull(result);
        assertEquals(vehicleEntity.getId(), result.getId());
    }

    @Test
    void testAddVehicleToUserInvalidUser() {
        assertThrows(EntityNotFoundException.class, () -> {
            userVehicleService.addVehicleToUser(0L, vehicleList.get(2).getId());
        });
    }

    @Test
    void testAddVehicleToUserInvalidVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            userVehicleService.addVehicleToUser(userList.get(0).getId(), 0L);
        });
    }
    
    @Test
    void testGetVehiclesFromUserValid() throws EntityNotFoundException {
        UserEntity userEntity = userList.get(2);
        List<VehicleEntity> vehicles = userVehicleService.getVehiclesFromUser(userEntity.getId());
        assertNotNull(vehicles);
        assertEquals(userEntity.getVehicles().size(), vehicles.size());
        for (VehicleEntity vehicle : vehicles) {
            assertTrue(userEntity.getVehicles().contains(vehicle));
        }
    }

    @Test
    void testGetVehiclesFromUserInvalidUser() {
        assertThrows(EntityNotFoundException.class, () -> {
            userVehicleService.getVehiclesFromUser(0L);
        });
    }

    @Test
    void testGetVehicleFromUserValid() throws EntityNotFoundException, IllegalOperationException {
        UserEntity userEntity = userList.get(2);
        VehicleEntity vehicleEntity = vehicleList.get(2);
        VehicleEntity result = userVehicleService.getVehicleFromUser(userEntity.getId(), vehicleEntity.getId());
        assertNotNull(result);
        assertEquals(vehicleEntity.getId(), result.getId());
    }

    @Test
    void testGetVehicleFromUserInvalidUser() {
        assertThrows(EntityNotFoundException.class, () -> {
            userVehicleService.getVehicleFromUser(0L, vehicleList.get(2).getId());
        });
    }

    @Test
    void testGetVehicleFromUserInvalidVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            userVehicleService.getVehicleFromUser(userList.get(2).getId(), 0L);
        });
    }

    @Test
    void testUpdateVehiclesFromUserValid() throws EntityNotFoundException, IllegalOperationException {
        UserEntity userEntity = userList.get(0);
        List<VehicleEntity> newVehicleList = new ArrayList<>();
        newVehicleList.add(vehicleList.get(0));
        newVehicleList.add(vehicleList.get(1));
        List<VehicleEntity> updatedVehicles = userVehicleService.updateVehiclesFromUser(userEntity.getId(), newVehicleList);
        assertNotNull(updatedVehicles);
        assertEquals(newVehicleList.size(), updatedVehicles.size());
        for (VehicleEntity vehicle : newVehicleList) {
            assertTrue(updatedVehicles.contains(vehicle));
        }
    }

    @Test
    void testUpdateVehiclesFromUserInvalidUser() {
        List<VehicleEntity> newVehicleList = new ArrayList<>();
        newVehicleList.add(vehicleList.get(0));
        assertThrows(EntityNotFoundException.class, () -> {
            userVehicleService.updateVehiclesFromUser(0L, newVehicleList);
        });
    }

    @Test
    void testUpdateVehiclesFromUserInvalidVehicle() {
        UserEntity userEntity = userList.get(0);
        VehicleEntity invalidVehicle = factory.manufacturePojo(VehicleEntity.class);
        invalidVehicle.setId(0L);
        List<VehicleEntity> newVehicleList = new ArrayList<>();
        newVehicleList.add(invalidVehicle);
        assertThrows(EntityNotFoundException.class, () -> {
            userVehicleService.updateVehiclesFromUser(userEntity.getId(), newVehicleList);
        });
    }

    @Test
    void testDeleteVehicleFromUserValid() throws EntityNotFoundException, IllegalOperationException {
        UserEntity userEntity = userList.get(2);
        VehicleEntity vehicleEntity = vehicleList.get(2);
        assertTrue(userEntity.getVehicles().contains(vehicleEntity));
        userVehicleService.deleteVehicleFromUser(userEntity.getId(), vehicleEntity.getId());
        assertFalse(userEntity.getVehicles().contains(vehicleEntity));
    }

    @Test
    void testDeleteVehicleFromUserInvalidUser() {
        assertThrows(EntityNotFoundException.class, () -> {
            userVehicleService.deleteVehicleFromUser(0L, vehicleList.get(2).getId());
        });
    }

    @Test
    void testDeleteVehicleFromUserInvalidVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            userVehicleService.getVehicleFromUser(userList.get(2).getId(), 0L);
        });
    }
}