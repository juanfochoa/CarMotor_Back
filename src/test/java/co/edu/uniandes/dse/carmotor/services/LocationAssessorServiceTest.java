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
import co.edu.uniandes.dse.carmotor.entities.AssessorEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(LocationAssessorService.class)
public class LocationAssessorServiceTest {
    @Autowired
    private LocationAssessorService locationAssessorService;

    @Autowired 
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    
    private List<LocationEntity> locationList = new ArrayList<>();
    private List<AssessorEntity> assessorList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from LocationEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AssessorEntity").executeUpdate();
        locationList.clear();
        assessorList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            AssessorEntity assessor = factory.manufacturePojo(AssessorEntity.class);
            entityManager.persist(assessor);
            assessorList.add(assessor);
        }
        for (int i = 0; i < 3; i++) {
            LocationEntity location = factory.manufacturePojo(LocationEntity.class);
            entityManager.persist(location);
            locationList.add(location);
            if (i == 2) {
                location.getAssessors().add(assessorList.get(i));
                assessorList.get(i).setLocation(location);
            }
        }
    }

    @Test
    void testAddAssessorToLocationValid() throws EntityNotFoundException, IllegalOperationException {
        LocationEntity location = locationList.get(0);
        AssessorEntity assessor = assessorList.get(0);
        AssessorEntity result = locationAssessorService.addAssessorToLocation(location.getId(), assessor.getId());
        assertNotNull(result);
        assertEquals(assessor.getId(), result.getId());
        assertEquals(location, assessor.getLocation());
    }

    @Test
    void testAddAssessorToLocationInvalidLocation() {
        AssessorEntity assessor = assessorList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            locationAssessorService.addAssessorToLocation(0L, assessor.getId());
        });
    }

    @Test
    void testAddAssessorToLocationInvalidAssessor() {
        LocationEntity location = locationList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            locationAssessorService.addAssessorToLocation(location.getId(), 0L);
        });
    }
    
    @Test
    void testGetAssessorsFromLocationValid() throws EntityNotFoundException {
        LocationEntity location = locationList.get(2);
        List<AssessorEntity> assessors = locationAssessorService.getAssessorsFromLocation(location.getId());
        assertNotNull(assessors);
        assertTrue(assessors.contains(assessorList.get(2)));
    }

    @Test
    void testGetAssessorsFromLocationInvalidLocation() {
        assertThrows(EntityNotFoundException.class, () -> {
            locationAssessorService.getAssessorsFromLocation(0L);
        });
    }

    @Test
    void testGetAssessorFromLocationValid() throws EntityNotFoundException, IllegalOperationException {
        LocationEntity location = locationList.get(2);
        AssessorEntity assessor = assessorList.get(2);
        AssessorEntity result = locationAssessorService.getAssessorFromLocation(location.getId(), assessor.getId());
        assertNotNull(result);
        assertEquals(assessor.getId(), result.getId());
        assertEquals(location, assessor.getLocation());
    }

    @Test
    void testGetAssessorFromLocationInvalidLocation() {
        AssessorEntity assessor = assessorList.get(2);
        assertThrows(EntityNotFoundException.class, () -> {
            locationAssessorService.getAssessorFromLocation(0L, assessor.getId());
        });
    }

    @Test
    void testGetAssessorFromLocationInvalidAssessor() {
        LocationEntity location = locationList.get(2);
        assertThrows(EntityNotFoundException.class, () -> {
            locationAssessorService.getAssessorFromLocation(location.getId(), 0L);
        });
    }

    @Test
    void testUpdateAssessorsFromLocationValid() throws EntityNotFoundException, IllegalOperationException {
        LocationEntity location = locationList.get(0);
        List<AssessorEntity> newAssessorList = new ArrayList<>();
        newAssessorList.add(assessorList.get(0));
        newAssessorList.add(assessorList.get(1));
        List<AssessorEntity> result = locationAssessorService.updateAssessorsFromLocation(location.getId(), newAssessorList);
        assertNotNull(result);
        assertEquals(newAssessorList.size(), result.size());
        for (int i = 0; i < newAssessorList.size(); i++) {
            assertEquals(newAssessorList.get(i).getId(), result.get(i).getId());
            assertEquals(location, result.get(i).getLocation());
        }
    }

    @Test
    void testUpdateAssessorsFromLocationInvalidLocation() {
        List<AssessorEntity> newAssessorList = new ArrayList<>();
        newAssessorList.add(assessorList.get(0));
        assertThrows(EntityNotFoundException.class, () -> {
            locationAssessorService.updateAssessorsFromLocation(0L, newAssessorList);
        });
    }

    @Test
    void testUpdateAssessorsFromLocationInvalidAssessor() {
        LocationEntity location = locationList.get(0);
        AssessorEntity invalidAssessor = factory.manufacturePojo(AssessorEntity.class);
        invalidAssessor.setId(0L);
        List<AssessorEntity> newAssessorList = new ArrayList<>();
        newAssessorList.add(invalidAssessor);
        assertThrows(EntityNotFoundException.class, () -> {
            locationAssessorService.updateAssessorsFromLocation(location.getId(), newAssessorList);
        });
    }

    @Test
    void testDeleteAssessorFromLocationValid() throws EntityNotFoundException, IllegalOperationException {
        LocationEntity location = locationList.get(2);
        AssessorEntity assessor = assessorList.get(2);
        assertTrue(location.getAssessors().contains(assessor));
        locationAssessorService.deleteAssessorFromLocation(location.getId(), assessor.getId());
        assertFalse(location.getAssessors().contains(assessor));
        assertNull(assessor.getLocation());
    }

    @Test
    void testDeleteAssessorFromLocationInvalidLocation() {
        AssessorEntity assessor = assessorList.get(2);
        assertThrows(EntityNotFoundException.class, () -> {
            locationAssessorService.deleteAssessorFromLocation(0L, assessor.getId());
        });
    }

    @Test
    void testDeleteAssessorFromLocationInvalidAssessor() {
        LocationEntity location = locationList.get(2);
        assertThrows(EntityNotFoundException.class, () -> {
            locationAssessorService.deleteAssessorFromLocation(location.getId(), 0L);
        });
    }
}