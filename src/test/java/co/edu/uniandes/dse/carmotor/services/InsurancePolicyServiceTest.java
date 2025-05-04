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
import co.edu.uniandes.dse.carmotor.entities.InsurancePolicyEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(InsurancePolicyService.class)
class InsurancePolicyServiceTest {
    @Autowired
    private InsurancePolicyService insurancePolicyService;

    @Autowired 
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<InsurancePolicyEntity> insurancePolicyList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from InsurancePolicyEntity").executeUpdate();
        insurancePolicyList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            InsurancePolicyEntity policyEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            entityManager.persist(policyEntity);
            insurancePolicyList.add(policyEntity);
        }
    }

    @Test
    void testCreateInsurancePolicyValid() throws EntityNotFoundException, IllegalOperationException {
        InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2999);
        Date duration = calendar.getTime();
        newEntity.setDuration(duration);
        InsurancePolicyEntity result = insurancePolicyService.createInsurancePolicy(newEntity);
        assertNotNull(result);
        InsurancePolicyEntity storedEntity = entityManager.find(InsurancePolicyEntity.class, result.getId());
        assertEquals(newEntity.getId(), storedEntity.getId());
        assertEquals(newEntity.getPrice(), storedEntity.getPrice());
        assertEquals(newEntity.getDuration(), storedEntity.getDuration());
        assertEquals(newEntity.getPremiumRate(), storedEntity.getPremiumRate());
        assertEquals(newEntity.getInsuranceCompany(), storedEntity.getInsuranceCompany());
    }

    @Test
    void testCreateInsurancePolicyWithInvalidPriceNull() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setPrice(null);
            insurancePolicyService.createInsurancePolicy(newEntity);
        });
    }

    @Test
    void testCreateInsurancePolicyWithInvalidPriceNaN() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setPrice(Double.NaN);
            insurancePolicyService.createInsurancePolicy(newEntity);
        });
    }

    @Test
    void testCreateInsurancePolicyWithInvalidPricePositiveInfinite() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setPrice(Double.POSITIVE_INFINITY);
            insurancePolicyService.createInsurancePolicy(newEntity);
        });
    }

    @Test
    void testCreateInsurancePolicyWithInvalidPriceNegativeInfinite() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setPrice(Double.NEGATIVE_INFINITY);
            insurancePolicyService.createInsurancePolicy(newEntity);
        });
    }

    @Test
    void testCreateInsurancePolicyWithInvalidDurationNull() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setDuration(null);
            insurancePolicyService.createInsurancePolicy(newEntity);
        });
    }

    @Test
    void testCreateInsurancePolicyWithInvalidDurationBefore() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 1910);
            Date duration = calendar.getTime();
            newEntity.setDuration(duration);
            insurancePolicyService.createInsurancePolicy(newEntity);
        });
    }

    @Test
    void testCreateInsurancePolicyWithInvalidPremiumRateNull() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setPremiumRate(null);
            insurancePolicyService.createInsurancePolicy(newEntity);
        });
    }

    @Test
    void testCreateInsurancePolicyWithInvalidPremiumRateNaN() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setPremiumRate(Double.NaN);
            insurancePolicyService.createInsurancePolicy(newEntity);
        });
    }

    @Test
    void testCreateInsurancePolicyWithInvalidPremiumRatePositiveInfinite() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setPremiumRate(Double.POSITIVE_INFINITY);
            insurancePolicyService.createInsurancePolicy(newEntity);
        });
    }

    @Test
    void testCreateInsurancePolicyWithInvalidPremiumRateNegativeInfinite() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setPremiumRate(Double.NEGATIVE_INFINITY);
            insurancePolicyService.createInsurancePolicy(newEntity);
        });
    }

    @Test
    void testCreateInsurancePolicyWithInvalidInsuranceCompanyNull() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setInsuranceCompany(null);
            insurancePolicyService.createInsurancePolicy(newEntity);
        });
    }

    @Test
    void testCreateInsurancePolicyWithInvalidInsuranceCompanyEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setInsuranceCompany("");
            insurancePolicyService.createInsurancePolicy(newEntity);
        });
    }

    @Test
    void testGetInsurancePoliciesValid() {
        List<InsurancePolicyEntity> list = insurancePolicyService.getInsurancePolicies();
        assertEquals(insurancePolicyList.size(), list.size());
        for (InsurancePolicyEntity entity : list) {
            boolean found = insurancePolicyList.stream().anyMatch(stored -> stored.getId().equals(entity.getId()));
            assertTrue(found);
        }
    }
    
    @Test
    void testGetInsurancePolicyValid() throws EntityNotFoundException {
        InsurancePolicyEntity entity = insurancePolicyList.get(0);
        InsurancePolicyEntity resultEntity = insurancePolicyService.getInsurancePolicy(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getPrice(), resultEntity.getPrice());
        assertEquals(entity.getDuration(), resultEntity.getDuration());
        assertEquals(entity.getPremiumRate(), resultEntity.getPremiumRate());
        assertEquals(entity.getInsuranceCompany(), resultEntity.getInsuranceCompany());
    }

    @Test
    void testGetInsurancePolicyInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            insurancePolicyService.getInsurancePolicy(0L);
        });
    }

    @Test
    void testUpdateInsurancePolicyValid() throws EntityNotFoundException, IllegalOperationException {
        InsurancePolicyEntity entity = insurancePolicyList.get(0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2999);
        Date duration = calendar.getTime();
        InsurancePolicyEntity pojoEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
        pojoEntity.setId(entity.getId());
        pojoEntity.setDuration(duration);
        insurancePolicyService.updateInsurancePolicy(entity.getId(), pojoEntity);

        InsurancePolicyEntity resp = entityManager.find(InsurancePolicyEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getPrice(), resp.getPrice());
        assertEquals(pojoEntity.getDuration(), resp.getDuration());
        assertEquals(pojoEntity.getPremiumRate(), resp.getPremiumRate());
        assertEquals(pojoEntity.getInsuranceCompany(), resp.getInsuranceCompany());
    }

    @Test
    void testUpdateInsurancePolicyInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> {
            InsurancePolicyEntity pojoEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            pojoEntity.setId(0L);
            insurancePolicyService.updateInsurancePolicy(0L, pojoEntity);
        });
    }

    @Test
    void testUpdateInsurancePolicyWithInvalidPriceNull() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setPrice(null);
            insurancePolicyService.updateInsurancePolicy(insurancePolicyList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateInsurancePolicyWithInvalidPriceNaN() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setPrice(Double.NaN);
            insurancePolicyService.updateInsurancePolicy(insurancePolicyList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateInsurancePolicyWithInvalidPricePositiveInfinite() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setPrice(Double.POSITIVE_INFINITY);
            insurancePolicyService.updateInsurancePolicy(insurancePolicyList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateInsurancePolicyWithInvalidPriceNegativeInfinite() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setPrice(Double.NEGATIVE_INFINITY);
            insurancePolicyService.updateInsurancePolicy(insurancePolicyList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateInsurancePolicyWithInvalidDurationNull() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setDuration(null);
            insurancePolicyService.updateInsurancePolicy(insurancePolicyList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateInsurancePolicyWithInvalidDurationBefore() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 1910);
            Date duration = calendar.getTime();
            newEntity.setDuration(duration);
            insurancePolicyService.updateInsurancePolicy(insurancePolicyList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateInsurancePolicyWithInvalidPremiumRateNull() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setPremiumRate(null);
            insurancePolicyService.updateInsurancePolicy(insurancePolicyList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateInsurancePolicyWithInvalidPremiumRateNaN() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setPremiumRate(Double.NaN);
            insurancePolicyService.updateInsurancePolicy(insurancePolicyList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateInsurancePolicyWithInvalidPremiumRatePositiveInfinite() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setPremiumRate(Double.POSITIVE_INFINITY);
            insurancePolicyService.updateInsurancePolicy(insurancePolicyList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateInsurancePolicyWithInvalidPremiumRateNegativeInfinite() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setPremiumRate(Double.NEGATIVE_INFINITY);
            insurancePolicyService.updateInsurancePolicy(insurancePolicyList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateInsurancePolicyWithInvalidInsuranceCompanyNull() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setInsuranceCompany(null);
            insurancePolicyService.updateInsurancePolicy(insurancePolicyList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateInsurancePolicyWithInvalidInsuranceCompanyEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            InsurancePolicyEntity newEntity = factory.manufacturePojo(InsurancePolicyEntity.class);
            newEntity.setInsuranceCompany("");
            insurancePolicyService.updateInsurancePolicy(insurancePolicyList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testDeleteInsurancePolicyValid() throws EntityNotFoundException, IllegalOperationException {
        InsurancePolicyEntity entity = insurancePolicyList.get(1);
        insurancePolicyList.clear();
        insurancePolicyService.deleteInsurancePolicy(entity.getId());
        InsurancePolicyEntity deleted = entityManager.find(InsurancePolicyEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteInsurancePolicyInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            insurancePolicyService.deleteInsurancePolicy(0L);
        });
    }
}