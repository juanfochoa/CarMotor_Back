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
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(LocationVehicleService.class)
public class LocationVehicleServiceTest {
    @Autowired
    private LocationVehicleService locationVehicleService;

    @Autowired 
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    
    private List<LocationEntity> locationList = new ArrayList<>();
    private List<VehicleEntity> vehicleList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from LocationEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VehicleEntity").executeUpdate();
        locationList.clear();
        vehicleList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            VehicleEntity vehicle = factory.manufacturePojo(VehicleEntity.class);
            entityManager.persist(vehicle);
            vehicleList.add(vehicle);
        }
        for (int i = 0; i < 3; i++) {
            LocationEntity location = factory.manufacturePojo(LocationEntity.class);
            entityManager.persist(location);
            locationList.add(location);
            if (i == 2) {
                location.getVehicles().add(vehicleList.get(i));
                vehicleList.get(i).setLocation(location);
            }
        }
    }

    @Test
    void testAddVehicleToLocationValid() throws EntityNotFoundException, IllegalOperationException {
        LocationEntity location = locationList.get(0);
        VehicleEntity vehicle = vehicleList.get(0);
        VehicleEntity result = locationVehicleService.addVehicleToLocation(location.getId(), vehicle.getId());
        assertNotNull(result);
        assertEquals(vehicle.getId(), result.getId());
        assertEquals(location, vehicle.getLocation());
    }

    @Test
    void testAddVehicleToLocationInvalidLocation() {
        VehicleEntity vehicle = vehicleList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            locationVehicleService.addVehicleToLocation(0L, vehicle.getId());
        });
    }

    @Test
    void testAddVehicleToLocationInvalidVehicle() {
        LocationEntity location = locationList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            locationVehicleService.addVehicleToLocation(location.getId(), 0L);
        });
    }
    
    @Test
    void testGetVehiclesFromLocationValid() throws EntityNotFoundException {
        LocationEntity location = locationList.get(2);
        List<VehicleEntity> vehicles = locationVehicleService.getVehiclesFromLocation(location.getId());
        assertNotNull(vehicles);
        assertEquals(1, vehicles.size());
        assertEquals(vehicleList.get(2).getId(), vehicles.get(0).getId());
    }

    @Test
    void testGetVehiclesFromLocationInvalidLocation() {
        assertThrows(EntityNotFoundException.class, () -> {
            locationVehicleService.getVehiclesFromLocation(0L);
        });
    }

    @Test
    void testGetVehicleFromLocationValid() throws EntityNotFoundException, IllegalOperationException {
        LocationEntity location = locationList.get(2);
        VehicleEntity vehicle = vehicleList.get(2);
        VehicleEntity result = locationVehicleService.getVehicleFromLocation(location.getId(), vehicle.getId());
        assertNotNull(result);
        assertEquals(vehicle.getId(), result.getId());
        assertEquals(location, vehicle.getLocation());
    }

    @Test
    void testGetVehicleFromLocationInvalidLocation() {
        VehicleEntity vehicle = vehicleList.get(2);
        assertThrows(EntityNotFoundException.class, () -> {
            locationVehicleService.getVehicleFromLocation(0L, vehicle.getId());
        });
    }

    @Test
    void testGetVehicleFromLocationInvalidVehicle() {
        LocationEntity location = locationList.get(2);
        assertThrows(EntityNotFoundException.class, () -> {
            locationVehicleService.getVehicleFromLocation(location.getId(), 0L);
        });
    }

    @Test
    void testUpdateVehiclesFromLocationValid() throws EntityNotFoundException, IllegalOperationException {
        LocationEntity location = locationList.get(0);
        List<VehicleEntity> newVehicleList = new ArrayList<>();
        newVehicleList.add(vehicleList.get(0));
        newVehicleList.add(vehicleList.get(1));
        List<VehicleEntity> result = locationVehicleService.updateVehiclesFromLocation(location.getId(), newVehicleList);
        assertNotNull(result);
        assertEquals(newVehicleList.size(), result.size());
        for (int i = 0; i < newVehicleList.size(); i++) {
            assertEquals(newVehicleList.get(i).getId(), result.get(i).getId());
            assertEquals(location, result.get(i).getLocation());
        }
    }

    @Test
    void testUpdateVehiclesFromLocationInvalidLocation() {
        List<VehicleEntity> newVehicleList = new ArrayList<>();
        newVehicleList.add(vehicleList.get(0));
        assertThrows(EntityNotFoundException.class, () -> {
            locationVehicleService.updateVehiclesFromLocation(0L, newVehicleList);
        });
    }

    @Test
    void testUpdateVehiclesFromLocationInvalidVehicle() {
        LocationEntity location = locationList.get(0);
        VehicleEntity invalidVehicle = factory.manufacturePojo(VehicleEntity.class);
        invalidVehicle.setId(0L);
        List<VehicleEntity> newVehicleList = new ArrayList<>();
        newVehicleList.add(invalidVehicle);
        assertThrows(EntityNotFoundException.class, () -> {
            locationVehicleService.updateVehiclesFromLocation(location.getId(), newVehicleList);
        });
    }

    @Test
    void testDeleteVehicleFromLocationValid() throws EntityNotFoundException, IllegalOperationException {
        LocationEntity location = locationList.get(2);
        VehicleEntity vehicle = vehicleList.get(2);
        assertTrue(location.getVehicles().contains(vehicle));
        locationVehicleService.deleteVehicleFromLocation(location.getId(), vehicle.getId());
        assertFalse(location.getVehicles().contains(vehicle));
        assertNull(vehicle.getLocation());
    }

    @Test
    void testDeleteVehicleFromLocationInvalidLocation() {
        VehicleEntity vehicle = vehicleList.get(2);
        assertThrows(EntityNotFoundException.class, () -> {
            locationVehicleService.deleteVehicleFromLocation(0L, vehicle.getId());
        });
    }

    @Test
    void testDeleteVehicleFromLocationInvalidVehicle() {
        LocationEntity location = locationList.get(2);
        assertThrows(EntityNotFoundException.class, () -> {
            locationVehicleService.deleteVehicleFromLocation(location.getId(), 0L);
        });
    }
}