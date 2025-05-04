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
import org.springframework.transaction.annotation.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;

@DataJpaTest
@Transactional
@Import(VehicleService.class)
public class VehicleServiceTest {
    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<VehicleEntity> vehicleList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from VehicleEntity").executeUpdate();
        vehicleList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            VehicleEntity vehicleEntity = factory.manufacturePojo(VehicleEntity.class);
            entityManager.persist(vehicleEntity);
            vehicleList.add(vehicleEntity);
        }
    }

    @Test
    void testCreateVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        VehicleEntity result = vehicleService.createVehicle(newEntity);
        assertNotNull(result);
        VehicleEntity storedEntity = entityManager.find(VehicleEntity.class, result.getId());
        assertEquals(newEntity.getId(), storedEntity.getId());
        assertEquals(newEntity.getBrand(), storedEntity.getBrand());
        assertEquals(newEntity.getSeries(), storedEntity.getSeries());
        assertEquals(newEntity.getLastPlateDigit(), storedEntity.getLastPlateDigit());
        assertEquals(newEntity.getModel(), storedEntity.getModel());
        assertEquals(newEntity.getType(), storedEntity.getType());
        assertEquals(newEntity.getCapacity(), storedEntity.getCapacity());
        assertEquals(newEntity.getPrice(), storedEntity.getPrice());
    }

    @Test
    void testCreateVehicleInvalidBrandNull() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setBrand(null);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testCreateVehicleInvalidBrandEmpty() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setBrand("");
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testCreateVehicleInvalidSeriesNull() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setSeries(null);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testCreateVehicleInvalidSeriesEmpty() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setSeries("");
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testCreateVehicleInvalidLastPlateDigitNull() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setLastPlateDigit(null);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testCreateVehicleInvalidLastPlateDigitEmpty() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setLastPlateDigit("");
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testCreateVehicleInvalidModelNull() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setModel(null);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testCreateVehicleInvalidModelEmpty() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setModel("");
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testCreateVehicleInvalidTypeNull() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setType(null);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testCreateVehicleInvalidTypeEmpty() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setType("");
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testCreateVehicleInvalidCapacityNull() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setCapacity(null);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testCreateVehicleInvalidCapacityLessThanZero() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setCapacity(-1);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testCreateVehicleInvalidCapacityZero() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setCapacity(0);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testCreateVehicleInvalidPriceNull() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setPrice(null);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testCreateVehicleInvalidPriceNaN() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setPrice(Double.NaN);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testCreateVehicleInvalidPricePositiveInfinite() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setPrice(Double.POSITIVE_INFINITY);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testCreateVehicleInvalidPriceNegativeInfinite() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setPrice(Double.NEGATIVE_INFINITY);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.createVehicle(newEntity);
        });
    }

    @Test
    void testGetVehicles() {
        List<VehicleEntity> list = vehicleService.getVehicles();
        assertEquals(vehicleList.size(), list.size());
        for (VehicleEntity entity : list) {
            boolean found = vehicleList.stream().anyMatch(stored -> stored.getId().equals(entity.getId()));
            assertTrue(found);
        }
    }

    @Test
    void testGetVehicleValid() throws EntityNotFoundException {
        VehicleEntity entity = vehicleList.get(0);
        VehicleEntity resultEntity = vehicleService.getVehicle(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getBrand(), resultEntity.getBrand());
        assertEquals(entity.getSeries(), resultEntity.getSeries());
        assertEquals(entity.getLastPlateDigit(), resultEntity.getLastPlateDigit());
        assertEquals(entity.getModel(), resultEntity.getModel());
        assertEquals(entity.getType(), resultEntity.getType());
        assertEquals(entity.getCapacity(), resultEntity.getCapacity());
        assertEquals(entity.getPrice(), resultEntity.getPrice());
    }

    @Test
    void testGetVehicleInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleService.getVehicle(0L);
        });
    }

    @Test
    void testUpdateVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity entity = vehicleList.get(0);
        VehicleEntity updatedEntity = factory.manufacturePojo(VehicleEntity.class);
        updatedEntity.setId(entity.getId());
        vehicleService.updateVehicle(entity.getId(), updatedEntity);
        
        VehicleEntity resp = entityManager.find(VehicleEntity.class, entity.getId());
        assertEquals(updatedEntity.getId(), resp.getId());
        assertEquals(updatedEntity.getBrand(), resp.getBrand());
        assertEquals(updatedEntity.getSeries(), resp.getSeries());
        assertEquals(updatedEntity.getLastPlateDigit(), resp.getLastPlateDigit());
        assertEquals(updatedEntity.getModel(), resp.getModel());
        assertEquals(updatedEntity.getType(), resp.getType());
        assertEquals(updatedEntity.getCapacity(), resp.getCapacity());
        assertEquals(updatedEntity.getPrice(), resp.getPrice());
    }

    @Test
    void testUpdateVehicleInvalidId() {
        VehicleEntity updatedEntity = factory.manufacturePojo(VehicleEntity.class);
        updatedEntity.setId(0L);
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleService.updateVehicle(0L, updatedEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidBrandNull() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setBrand(null);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidBrandEmpty() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setBrand("");
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidSeriesNull() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setSeries(null);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidSeriesEmpty() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setSeries("");
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidLastPlateDigitNull() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setLastPlateDigit(null);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidLastPlateDigitEmpty() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setLastPlateDigit("");
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidModelNull() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setModel(null);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidModelEmpty() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setModel("");
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidTypeNull() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setType(null);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidTypeEmpty() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setType("");
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidCapacityNull() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setCapacity(null);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidCapacityLessThanZero() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setCapacity(-1);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidCapacityZero() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setCapacity(0);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidPriceNull() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setPrice(null);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidPriceNaN() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setPrice(Double.NaN);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidPricePositiveInfinite() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setPrice(Double.POSITIVE_INFINITY);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateVehicleInvalidPriceNegativeInfinite() {
        VehicleEntity newEntity = factory.manufacturePojo(VehicleEntity.class);
        newEntity.setPrice(Double.NEGATIVE_INFINITY);
        assertThrows(IllegalOperationException.class, () -> {
            vehicleService.updateVehicle(vehicleList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testDeleteVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity entity = vehicleList.get(1);
        vehicleService.deleteVehicle(entity.getId());
        VehicleEntity deleted = entityManager.find(VehicleEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteVehicleInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleService.deleteVehicle(0L);
        });
    }
}