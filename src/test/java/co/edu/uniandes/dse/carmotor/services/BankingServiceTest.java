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
import co.edu.uniandes.dse.carmotor.entities.BankingEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(BankingService.class)
class BankingServiceTest {
    @Autowired
    private BankingService bankingService;

    @Autowired 
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<BankingEntity> bankingList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from BankingEntity").executeUpdate();
        bankingList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            BankingEntity bankingEntity = factory.manufacturePojo(BankingEntity.class);
            entityManager.persist(bankingEntity);
            bankingList.add(bankingEntity);
        }
    }

    @Test
    void testCreateBankingValid() throws IllegalOperationException, EntityNotFoundException {
        BankingEntity newEntity = factory.manufacturePojo(BankingEntity.class);
        BankingEntity result = bankingService.createBanking(newEntity);
        assertNotNull(result);
        BankingEntity entity = entityManager.find(BankingEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getName(), entity.getName());
        assertEquals(newEntity.getUriLogo(), entity.getUriLogo());
        assertEquals(newEntity.getAssessorPhone(), entity.getAssessorPhone());
    }

    @Test
    void testCreateBankingWithInvalidNameNull() {
        assertThrows(IllegalOperationException.class, () -> {
            BankingEntity newEntity = factory.manufacturePojo(BankingEntity.class);
            newEntity.setName(null);
            bankingService.createBanking(newEntity);
        });
    }

    @Test
    void testCreateBankingWithInvalidNameEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            BankingEntity newEntity = factory.manufacturePojo(BankingEntity.class);
            newEntity.setName("");
            bankingService.createBanking(newEntity);
        });
    }

    @Test
    void testCreateBankingWithInvalidUriLogoNull() {
        assertThrows(IllegalOperationException.class, () -> {
            BankingEntity newEntity = factory.manufacturePojo(BankingEntity.class);
            newEntity.setUriLogo(null);
            bankingService.createBanking(newEntity);
        });
    }

    @Test
    void testCreateBankingWithInvalidUriLogoEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            BankingEntity newEntity = factory.manufacturePojo(BankingEntity.class);
            newEntity.setUriLogo("");
            bankingService.createBanking(newEntity);
        });
    }

    @Test
    void testCreateBankingWithInvalidAssessorPhoneNull() {
        assertThrows(IllegalOperationException.class, () -> {
            BankingEntity newEntity = factory.manufacturePojo(BankingEntity.class);
            newEntity.setAssessorPhone(null);
            bankingService.createBanking(newEntity);
        });
    }

    @Test
    void testCreateBankingWithInvalidAssessorPhoneEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            BankingEntity newEntity = factory.manufacturePojo(BankingEntity.class);
            newEntity.setAssessorPhone("");
            bankingService.createBanking(newEntity);
        });
    }

    @Test
    void testGetBankingsValid() {
        List<BankingEntity> list = bankingService.getBankings();
        assertEquals(bankingList.size(), list.size());
        for (BankingEntity entity : list) {
            boolean found = false;
            for (BankingEntity storedEntity : bankingList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testGetBankingValid() throws EntityNotFoundException {
        BankingEntity entity = bankingList.get(0);
        BankingEntity resultEntity = bankingService.getBanking(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getName(), resultEntity.getName());
        assertEquals(entity.getUriLogo(), resultEntity.getUriLogo());
        assertEquals(entity.getAssessorPhone(), resultEntity.getAssessorPhone());
    }

    @Test
    void testGetBankingInvalid() {
        assertThrows(EntityNotFoundException.class, ()->{
            bankingService.getBanking(0L);
        });
    }

    @Test
    void testUpdateBankingValid() throws EntityNotFoundException, IllegalOperationException {
        BankingEntity entity = bankingList.get(0);
        BankingEntity pojoEntity = factory.manufacturePojo(BankingEntity.class);
        pojoEntity.setId(entity.getId());
        bankingService.updateBanking(entity.getId(), pojoEntity);

        BankingEntity resp = entityManager.find(BankingEntity.class, entity.getId());
        assertEquals(pojoEntity.getId(), resp.getId());
        assertEquals(pojoEntity.getName(), resp.getName());
        assertEquals(pojoEntity.getUriLogo(), resp.getUriLogo());
        assertEquals(pojoEntity.getAssessorPhone(), resp.getAssessorPhone());
    }

    @Test
    void testUpdateBankingInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> {
            BankingEntity pojoEntity = factory.manufacturePojo(BankingEntity.class);
            pojoEntity.setId(0L);
            bankingService.updateBanking(0L, pojoEntity);
        });
    }

    @Test
    void testUpdateBankingWithInvalidNameNull() {
        assertThrows(IllegalOperationException.class, () -> {
            BankingEntity newEntity = factory.manufacturePojo(BankingEntity.class);
            newEntity.setName(null);
            bankingService.updateBanking(bankingList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateBankingWithInvalidNameEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            BankingEntity newEntity = factory.manufacturePojo(BankingEntity.class);
            newEntity.setName("");
            bankingService.updateBanking(bankingList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateBankingWithInvalidUriLogoNull() {
        assertThrows(IllegalOperationException.class, () -> {
            BankingEntity newEntity = factory.manufacturePojo(BankingEntity.class);
            newEntity.setUriLogo(null);
            bankingService.updateBanking(bankingList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateBankingWithInvalidUriLogoEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            BankingEntity newEntity = factory.manufacturePojo(BankingEntity.class);
            newEntity.setUriLogo("");
            bankingService.updateBanking(bankingList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateBankingWithInvalidAssessorPhoneNull() {
        assertThrows(IllegalOperationException.class, () -> {
            BankingEntity newEntity = factory.manufacturePojo(BankingEntity.class);
            newEntity.setAssessorPhone(null);
            bankingService.updateBanking(bankingList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateBankingWithInvalidAssessorPhoneEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            BankingEntity newEntity = factory.manufacturePojo(BankingEntity.class);
            newEntity.setAssessorPhone("");
            bankingService.updateBanking(bankingList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testDeleteBankingValid() throws EntityNotFoundException, IllegalOperationException {
        BankingEntity entity = bankingList.get(1);
        bankingService.deleteBanking(entity.getId());
        BankingEntity deleted = entityManager.find(BankingEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteBankingInvalid() {
        assertThrows(EntityNotFoundException.class, ()->{
            bankingService.deleteBanking(0L);
        });
    }
}