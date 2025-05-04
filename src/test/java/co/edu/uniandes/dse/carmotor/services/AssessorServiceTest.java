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
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(AssessorService.class)
class AssessorServiceTest {
    @Autowired
    private AssessorService assessorService;

    @Autowired 
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<AssessorEntity> assessorList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from AssessorEntity").executeUpdate();
        assessorList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            AssessorEntity assessorEntity = factory.manufacturePojo(AssessorEntity.class);
            entityManager.persist(assessorEntity);
            assessorList.add(assessorEntity);
        }
    }

    @Test
    void testCreateAssessorValid() throws IllegalOperationException {
        AssessorEntity newEntity = factory.manufacturePojo(AssessorEntity.class);
        AssessorEntity result = assessorService.createAssessor(newEntity);
        assertNotNull(result);
        AssessorEntity storedEntity = entityManager.find(AssessorEntity.class, result.getId());
        assertEquals(newEntity.getName(), storedEntity.getName());
        assertEquals(newEntity.getUriPhoto(), storedEntity.getUriPhoto());
        assertEquals(newEntity.getContactInfo(), storedEntity.getContactInfo());
    }

    @Test
    void testCreateAssessorInvalidNameNull() {
        AssessorEntity newEntity = factory.manufacturePojo(AssessorEntity.class);
        newEntity.setName(null);
        assertThrows(IllegalOperationException.class, () -> {
            assessorService.createAssessor(newEntity);
        });
    }

    @Test
    void testCreateAssessorInvalidNameEmpty() {
        AssessorEntity newEntity = factory.manufacturePojo(AssessorEntity.class);
        newEntity.setName("");
        assertThrows(IllegalOperationException.class, () -> {
            assessorService.createAssessor(newEntity);
        });
    }

    @Test
    void testCreateAssessorInvalidUriPhotoNull() {
        AssessorEntity newEntity = factory.manufacturePojo(AssessorEntity.class);
        newEntity.setUriPhoto(null);
        assertThrows(IllegalOperationException.class, () -> {
            assessorService.createAssessor(newEntity);
        });
    }

    @Test
    void testCreateAssessorInvalidUriPhotoEmpty() {
        AssessorEntity newEntity = factory.manufacturePojo(AssessorEntity.class);
        newEntity.setUriPhoto("");
        assertThrows(IllegalOperationException.class, () -> {
            assessorService.createAssessor(newEntity);
        });
    }

    @Test
    void testCreateAssessorInvalidContactInfoNull() {
        AssessorEntity newEntity = factory.manufacturePojo(AssessorEntity.class);
        newEntity.setContactInfo(null);
        assertThrows(IllegalOperationException.class, () -> {
            assessorService.createAssessor(newEntity);
        });
    }

    @Test
    void testCreateAssessorInvalidContactInfoEmpty() {
        AssessorEntity newEntity = factory.manufacturePojo(AssessorEntity.class);
        newEntity.setContactInfo("");
        assertThrows(IllegalOperationException.class, () -> {
            assessorService.createAssessor(newEntity);
        });
    }

    @Test
    void testGetAssessors() {
        List<AssessorEntity> list = assessorService.getAssessors();
        assertEquals(assessorList.size(), list.size());
        for (AssessorEntity entity : list) {
            boolean found = assessorList.stream().anyMatch(stored -> stored.getId().equals(entity.getId()));
            assertTrue(found);
        }
    }
    
    @Test
    void testGetAssessorValid() throws EntityNotFoundException {
        AssessorEntity entity = assessorList.get(0);
        AssessorEntity resultEntity = assessorService.getAssessor(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getName(), resultEntity.getName());
        assertEquals(entity.getUriPhoto(), resultEntity.getUriPhoto());
        assertEquals(entity.getContactInfo(), resultEntity.getContactInfo());
    }

    @Test
    void testGetAssessorInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            assessorService.getAssessor(0L);
        });
    }

    @Test
    void testUpdateAssessorValid() throws EntityNotFoundException, IllegalOperationException {
        AssessorEntity entity = assessorList.get(0);
        AssessorEntity updatedEntity = factory.manufacturePojo(AssessorEntity.class);
        updatedEntity.setId(entity.getId());
        assessorService.updateAssessor(entity.getId(), updatedEntity);

        AssessorEntity storedEntity = entityManager.find(AssessorEntity.class, entity.getId());
        assertEquals(updatedEntity.getName(), storedEntity.getName());
        assertEquals(updatedEntity.getUriPhoto(), storedEntity.getUriPhoto());
        assertEquals(updatedEntity.getContactInfo(), storedEntity.getContactInfo());
    }

    @Test
    void testUpdateAssessorInvalidId() {
        AssessorEntity updatedEntity = factory.manufacturePojo(AssessorEntity.class);
        updatedEntity.setId(0L);
        assertThrows(EntityNotFoundException.class, () -> {
            assessorService.updateAssessor(0L, updatedEntity);
        });
    }

    @Test
    void testUpdateAssessorInvalidNameNull() {
        AssessorEntity newEntity = factory.manufacturePojo(AssessorEntity.class);
        newEntity.setName(null);
        assertThrows(IllegalOperationException.class, () -> {
            assessorService.updateAssessor(assessorList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateAssessorInvalidNameEmpty() {
        AssessorEntity newEntity = factory.manufacturePojo(AssessorEntity.class);
        newEntity.setName("");
        assertThrows(IllegalOperationException.class, () -> {
            assessorService.updateAssessor(assessorList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateAssessorInvalidUriPhotoNull() {
        AssessorEntity newEntity = factory.manufacturePojo(AssessorEntity.class);
        newEntity.setUriPhoto(null);
        assertThrows(IllegalOperationException.class, () -> {
            assessorService.updateAssessor(assessorList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateAssessorInvalidUriPhotoEmpty() {
        AssessorEntity newEntity = factory.manufacturePojo(AssessorEntity.class);
        newEntity.setUriPhoto("");
        assertThrows(IllegalOperationException.class, () -> {
            assessorService.updateAssessor(assessorList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateAssessorInvalidContactInfoNull() {
        AssessorEntity newEntity = factory.manufacturePojo(AssessorEntity.class);
        newEntity.setContactInfo(null);
        assertThrows(IllegalOperationException.class, () -> {
            assessorService.updateAssessor(assessorList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateAssessorInvalidContactInfoEmpty() {
        AssessorEntity newEntity = factory.manufacturePojo(AssessorEntity.class);
        newEntity.setContactInfo("");
        assertThrows(IllegalOperationException.class, () -> {
            assessorService.updateAssessor(assessorList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testDeleteAssessorValid() throws EntityNotFoundException, IllegalOperationException {
        AssessorEntity entity = assessorList.get(1);
        assessorService.deleteAssessor(entity.getId());
        AssessorEntity deleted = entityManager.find(AssessorEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteAssessorInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            assessorService.deleteAssessor(0L);
        });
    }
}