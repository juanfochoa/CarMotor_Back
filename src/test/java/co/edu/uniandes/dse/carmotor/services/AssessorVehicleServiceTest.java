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
import co.edu.uniandes.dse.carmotor.entities.AssessorEntity;
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(AssessorVehicleService.class)
public class AssessorVehicleServiceTest {
    @Autowired
    private AssessorVehicleService assessorVehicleService;

    @Autowired 
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    
    private List<AssessorEntity> assessorList = new ArrayList<>();
    private List<VehicleEntity> vehicleList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from AssessorEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VehicleEntity").executeUpdate();
        assessorList.clear();
        vehicleList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            VehicleEntity vehicle = factory.manufacturePojo(VehicleEntity.class);
            entityManager.persist(vehicle);
            vehicleList.add(vehicle);
        }
        for (int i = 0; i < 3; i++) {
            AssessorEntity assessor = factory.manufacturePojo(AssessorEntity.class);
            entityManager.persist(assessor);
            assessorList.add(assessor);
            if (i == 2) {
                assessor.getVehicles().add(vehicleList.get(i));
                vehicleList.get(i).setAssessor(assessor);
            }
        }
    }

    @Test
    void testAddVehicleToAssessorValid() throws EntityNotFoundException, IllegalOperationException {
        AssessorEntity assessor = assessorList.get(0);
        VehicleEntity vehicle = vehicleList.get(0);
        VehicleEntity result = assessorVehicleService.addVehicleToAssessor(assessor.getId(), vehicle.getId());
        assertNotNull(result);
        assertEquals(vehicle.getId(), result.getId());
        assertEquals(assessor, vehicle.getAssessor());
    }

    @Test
    void testAddVehicleToAssessorInvalidAssessor() {
        VehicleEntity vehicle = vehicleList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            assessorVehicleService.addVehicleToAssessor(0L, vehicle.getId());
        });
    }
    
    @Test
    void testAddVehicleToAssessorInvalidVehicle() {
        AssessorEntity assessor = assessorList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            assessorVehicleService.addVehicleToAssessor(assessor.getId(), 0L);
        });
    }
    
    @Test
    void testGetVehiclesFromAssessorValid() throws EntityNotFoundException {
        AssessorEntity assessor = assessorList.get(2);
        List<VehicleEntity> vehicles = assessorVehicleService.getVehiclesFromAssessor(assessor.getId());
        assertNotNull(vehicles);
        assertEquals(assessor.getVehicles().size(), vehicles.size());
        assertTrue(vehicles.contains(vehicleList.get(2)));
        for (VehicleEntity vehicle : vehicles) {
            assertEquals(assessor, vehicle.getAssessor());
        }
    }

    @Test
    void testGetVehiclesFromAssessorInvalidAssessor() {
        assertThrows(EntityNotFoundException.class, () -> {
            assessorVehicleService.getVehiclesFromAssessor(0L);
        });
    }

    @Test
    void testGetVehicleFromAssessorValid() throws EntityNotFoundException, IllegalOperationException {
        AssessorEntity assessor = assessorList.get(2);
        VehicleEntity vehicle = vehicleList.get(2);
        VehicleEntity result = assessorVehicleService.getVehicleFromAssessor(assessor.getId(), vehicle.getId());
        assertNotNull(result);
        assertEquals(vehicle.getId(), result.getId());
        assertEquals(assessor, vehicle.getAssessor());
    }

    @Test
    void testGetVehicleFromAssessorInvalidAssessor() {
        assertThrows(EntityNotFoundException.class, () -> {
            assessorVehicleService.getVehicleFromAssessor(0L, vehicleList.get(2).getId());
        });
    }

    @Test
    void testGetVehicleFromAssessorInvalidVehicle() {
        AssessorEntity assessor = assessorList.get(2);
        assertThrows(EntityNotFoundException.class, () -> {
            assessorVehicleService.getVehicleFromAssessor(assessor.getId(), 0L);
        });
    }

    @Test
    void testGetVehicleFromAssessorNotAssociated() {
        AssessorEntity assessor = assessorList.get(2);
        VehicleEntity vehicleNotAssociated = vehicleList.get(0);
        assertThrows(IllegalOperationException.class, () -> {
            assessorVehicleService.getVehicleFromAssessor(assessor.getId(), vehicleNotAssociated.getId());
        });
    }

    @Test
    void testUpdateVehiclesFromAssessorValid() throws EntityNotFoundException {
        AssessorEntity assessor = assessorList.get(2);
        List<VehicleEntity> newVehicleList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            VehicleEntity vehicle = factory.manufacturePojo(VehicleEntity.class);
            entityManager.persist(vehicle);
            newVehicleList.add(vehicle);
        }
        List<VehicleEntity> updatedVehicles = assessorVehicleService.updateVehiclesFromAssessor(assessor.getId(), newVehicleList);
        assertNotNull(updatedVehicles);
        assertEquals(newVehicleList.size(), updatedVehicles.size());
        for (VehicleEntity vehicle : newVehicleList) {
            assertTrue(updatedVehicles.contains(vehicle));
            assertEquals(assessor, vehicle.getAssessor());
        }
    }

    @Test
    void testUpdateVehiclesFromAssessorInvalidAssessor() {
        assertThrows(EntityNotFoundException.class, () -> {
            assessorVehicleService.updateVehiclesFromAssessor(0L, vehicleList);
        });
    }

    @Test
    void testUpdateVehiclesFromAssessorInvalidVehicle() {
        AssessorEntity assessor = assessorList.get(2);
        List<VehicleEntity> invalidVehicleList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            VehicleEntity vehicle = factory.manufacturePojo(VehicleEntity.class);
            vehicle.setId(0L);
            invalidVehicleList.add(vehicle);
        }
        assertThrows(EntityNotFoundException.class, () -> {
            assessorVehicleService.updateVehiclesFromAssessor(assessor.getId(), invalidVehicleList);
        });
    }

    @Test
    void testDeleteVehicleFromAssessorValid() throws EntityNotFoundException, IllegalOperationException {
        AssessorEntity assessor = assessorList.get(2);
        VehicleEntity vehicle = vehicleList.get(2);
        assertTrue(assessor.getVehicles().contains(vehicle));
        assessorVehicleService.deleteVehicleFromAssessor(assessor.getId(), vehicle.getId());
        assertFalse(assessor.getVehicles().contains(vehicle));
        assertNull(vehicle.getAssessor());
    }

    @Test
    void testDeleteVehicleFromAssessorInvalidAssessor() {
        assertThrows(EntityNotFoundException.class, () -> {
            assessorVehicleService.deleteVehicleFromAssessor(0L, vehicleList.get(2).getId());
        });
    }

    @Test
    void testDeleteVehicleFromAssessorInvalidVehicle() {
        AssessorEntity assessor = assessorList.get(2);
        assertThrows(EntityNotFoundException.class, () -> {
            assessorVehicleService.deleteVehicleFromAssessor(assessor.getId(), 0L);
        });
    }

    @Test
    void testDeleteVehicleFromAssessorNotAssociated() {
        AssessorEntity assessor = assessorList.get(2);
        VehicleEntity vehicleNotAssociated = vehicleList.get(0);
        assertThrows(IllegalOperationException.class, () -> {
            assessorVehicleService.deleteVehicleFromAssessor(assessor.getId(), vehicleNotAssociated.getId());
        });
    }
}