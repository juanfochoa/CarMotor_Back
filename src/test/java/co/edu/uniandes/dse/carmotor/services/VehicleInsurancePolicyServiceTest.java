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
import co.edu.uniandes.dse.carmotor.entities.InsurancePolicyEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(VehicleInsurancePolicyService.class)
public class VehicleInsurancePolicyServiceTest {
    @Autowired
    private VehicleInsurancePolicyService vehicleInsurancePolicyService;

    @Autowired 
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    
    private List<VehicleEntity> vehicleList = new ArrayList<>();
    private List<InsurancePolicyEntity> insurancePolicyList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from VehicleEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from InsurancePolicyEntity").executeUpdate();
        vehicleList.clear();
        insurancePolicyList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            InsurancePolicyEntity policy = factory.manufacturePojo(InsurancePolicyEntity.class);
            entityManager.persist(policy);
            insurancePolicyList.add(policy);
        }
        for (int i = 0; i < 3; i++) {
            VehicleEntity vehicle = factory.manufacturePojo(VehicleEntity.class);
            entityManager.persist(vehicle);
            vehicleList.add(vehicle);
            if (i == 2) {
                vehicle.getInsurancePolicies().add(insurancePolicyList.get(i));
            }
        }
    }

    @Test
    void testAddInsurancePolicyToVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicle = vehicleList.get(0);
        InsurancePolicyEntity policy = insurancePolicyList.get(0);
        InsurancePolicyEntity result = vehicleInsurancePolicyService.addInsurancePolicyToVehicle(vehicle.getId(), policy.getId());
        assertNotNull(result);
        assertEquals(policy.getId(), result.getId());
    }

    @Test
    void testAddInsurancePolicyToVehicleInvalidVehicle() {
        InsurancePolicyEntity policy = insurancePolicyList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleInsurancePolicyService.addInsurancePolicyToVehicle(0L, policy.getId());
        });
    }

    @Test
    void testAddInsurancePolicyToVehicleInvalidPolicy() {
        VehicleEntity vehicle = vehicleList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleInsurancePolicyService.addInsurancePolicyToVehicle(vehicle.getId(), 0L);
        });
    }
    
    @Test
    void testGetInsurancePoliciesFromVehicleValid() throws EntityNotFoundException {
        VehicleEntity vehicle = vehicleList.get(2);
        List<InsurancePolicyEntity> policies = vehicleInsurancePolicyService.getInsurancePoliciesFromVehicle(vehicle.getId());
        assertNotNull(policies);
        assertEquals(vehicle.getInsurancePolicies().size(), policies.size());
        for (InsurancePolicyEntity policy : policies) {
            assertTrue(vehicle.getInsurancePolicies().contains(policy));
        }
    }

    @Test
    void testGetInsurancePoliciesFromVehicleInvalidVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleInsurancePolicyService.getInsurancePoliciesFromVehicle(0L);
        });
    }

    @Test
    void testGetInsurancePolicyFromVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicle = vehicleList.get(2);
        InsurancePolicyEntity policy = insurancePolicyList.get(2);
        InsurancePolicyEntity result = vehicleInsurancePolicyService.getInsurancePolicyFromVehicle(vehicle.getId(), policy.getId());
        assertNotNull(result);
        assertEquals(policy.getId(), result.getId());
    }

    @Test
    void testGetInsurancePolicyFromVehicleInvalidVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleInsurancePolicyService.getInsurancePolicyFromVehicle(0L, insurancePolicyList.get(2).getId());
        });
    }

    @Test
    void testGetInsurancePolicyFromVehicleInvalidPolicy() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleInsurancePolicyService.getInsurancePolicyFromVehicle(vehicleList.get(2).getId(), 0L);
        });
    }

    @Test
    void testUpdateInsurancePoliciesFromVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicle = vehicleList.get(0);
        List<InsurancePolicyEntity> newPolicyList = new ArrayList<>();
        newPolicyList.add(insurancePolicyList.get(0));
        newPolicyList.add(insurancePolicyList.get(1));
        List<InsurancePolicyEntity> updatedPolicies = vehicleInsurancePolicyService.updateInsurancePoliciesFromVehicle(vehicle.getId(), newPolicyList);
        assertNotNull(updatedPolicies);
        assertEquals(newPolicyList.size(), updatedPolicies.size());
        for (InsurancePolicyEntity policy : newPolicyList) {
            assertTrue(updatedPolicies.contains(policy));
        }
    }

    @Test
    void testUpdateInsurancePoliciesFromVehicleInvalidVehicle() {
        List<InsurancePolicyEntity> newPolicyList = new ArrayList<>();
        newPolicyList.add(insurancePolicyList.get(0));
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleInsurancePolicyService.updateInsurancePoliciesFromVehicle(0L, newPolicyList);
        });
    }

    @Test
    void testUpdateInsurancePoliciesFromVehicleInvalidPolicy() {
        VehicleEntity vehicle = vehicleList.get(0);
        InsurancePolicyEntity invalidPolicy = factory.manufacturePojo(InsurancePolicyEntity.class);
        invalidPolicy.setId(0L);
        List<InsurancePolicyEntity> newPolicyList = new ArrayList<>();
        newPolicyList.add(invalidPolicy);
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleInsurancePolicyService.updateInsurancePoliciesFromVehicle(vehicle.getId(), newPolicyList);
        });
    }

    @Test
    void testDeleteInsurancePolicyFromVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicle = vehicleList.get(2);
        InsurancePolicyEntity policy = insurancePolicyList.get(2);
        assertTrue(vehicle.getInsurancePolicies().contains(policy));
        vehicleInsurancePolicyService.deleteInsurancePolicyFromVehicle(vehicle.getId(), policy.getId());
        assertFalse(vehicle.getInsurancePolicies().contains(policy));
    }

    @Test
    void testDeleteInsurancePolicyFromVehicleInvalidVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleInsurancePolicyService.deleteInsurancePolicyFromVehicle(0L, insurancePolicyList.get(2).getId());
        });
    }

    @Test
    void testDeleteInsurancePolicyFromVehicleInvalidPolicy() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleInsurancePolicyService.deleteInsurancePolicyFromVehicle(vehicleList.get(2).getId(), 0L);
        });
    }
}