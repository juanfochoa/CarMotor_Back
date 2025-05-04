package co.edu.uniandes.dse.carmotor.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import co.edu.uniandes.dse.carmotor.entities.PhotoEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(PhotoService.class)
public class PhotoServiceTest {
    @Autowired
    private PhotoService photoService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<PhotoEntity> photoList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PhotoEntity").executeUpdate();
        photoList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            PhotoEntity photoEntity = factory.manufacturePojo(PhotoEntity.class);
            entityManager.persist(photoEntity);
            photoList.add(photoEntity);
        }
    }

    @Test
    void testCreatePhotoValid() throws IllegalOperationException, EntityNotFoundException {
        PhotoEntity newEntity = factory.manufacturePojo(PhotoEntity.class);
        PhotoEntity result = photoService.createPhoto(newEntity);
        assertNotNull(result);
        PhotoEntity storedEntity = entityManager.find(PhotoEntity.class, result.getId());
        assertEquals(newEntity.getId(), storedEntity.getId());
        assertEquals(newEntity.getUri(), storedEntity.getUri());
        assertEquals(newEntity.getArea(), storedEntity.getArea());
    }

    @Test
    void testCreatePhotoWithInvalidUriNull() {
        assertThrows(IllegalOperationException.class, () -> {
            PhotoEntity newEntity = factory.manufacturePojo(PhotoEntity.class);
            newEntity.setUri(null);
            photoService.createPhoto(newEntity);
        });
    }

    @Test
    void testCreatePhotoWithInvalidUriEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            PhotoEntity newEntity = factory.manufacturePojo(PhotoEntity.class);
            newEntity.setUri("");
            photoService.createPhoto(newEntity);
        });
    }

    @Test
    void testCreatePhotoWithInvalidAreaNull() {
        assertThrows(IllegalOperationException.class, () -> {
            PhotoEntity newEntity = factory.manufacturePojo(PhotoEntity.class);
            newEntity.setArea(null);
            photoService.createPhoto(newEntity);
        });
    }

    @Test
    void testCreatePhotoWithInvalidAreaEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            PhotoEntity newEntity = factory.manufacturePojo(PhotoEntity.class);
            newEntity.setArea("");
            photoService.createPhoto(newEntity);
        });
    }

    @Test
    void testGetPhotosValid() {
        List<PhotoEntity> list = photoService.getPhotos();
        assertEquals(photoList.size(), list.size());
        for (PhotoEntity entity : list) {
            boolean found = photoList.stream().anyMatch(stored -> stored.getId().equals(entity.getId()));
            assertTrue(found);
        }
    }

    @Test
    void testGetPhotoValid() throws EntityNotFoundException {
        PhotoEntity entity = photoList.get(0);
        PhotoEntity resultEntity = photoService.getPhoto(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getUri(), resultEntity.getUri());
        assertEquals(entity.getArea(), resultEntity.getArea());
    }

    @Test
    void testGetPhotoInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> {
            photoService.getPhoto(0L);
        });
    }

    @Test
    void testUpdatePhotoValid() throws EntityNotFoundException, IllegalOperationException {
        PhotoEntity entity = photoList.get(0);
        PhotoEntity updatedEntity = factory.manufacturePojo(PhotoEntity.class);
        updatedEntity.setId(entity.getId());
        photoService.updatePhoto(entity.getId(), updatedEntity);

        PhotoEntity resp = entityManager.find(PhotoEntity.class, entity.getId());
        assertEquals(updatedEntity.getId(), resp.getId());
        assertEquals(updatedEntity.getUri(), resp.getUri());
        assertEquals(updatedEntity.getArea(), resp.getArea());
    }

    @Test
    void testUpdatePhotoInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> {
            PhotoEntity updatedEntity = factory.manufacturePojo(PhotoEntity.class);
            updatedEntity.setId(0L);
            photoService.updatePhoto(0L, updatedEntity);
        });
    }

    @Test
    void testUpdatePhotoWithInvalidUriNull() {
        assertThrows(IllegalOperationException.class, () -> {
            PhotoEntity updatedEntity = factory.manufacturePojo(PhotoEntity.class);
            updatedEntity.setUri(null);
            photoService.updatePhoto(photoList.get(0).getId(), updatedEntity);
        });
    }

    @Test
    void testUpdatePhotoWithInvalidUriEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            PhotoEntity updatedEntity = factory.manufacturePojo(PhotoEntity.class);
            updatedEntity.setUri("");
            photoService.updatePhoto(photoList.get(0).getId(), updatedEntity);
        });
    }

    @Test
    void testUpdatePhotoWithInvalidAreaNull() {
        assertThrows(IllegalOperationException.class, () -> {
            PhotoEntity updatedEntity = factory.manufacturePojo(PhotoEntity.class);
            updatedEntity.setArea(null);
            photoService.updatePhoto(photoList.get(0).getId(), updatedEntity);
        });
    }

    @Test
    void testUpdatePhotoWithInvalidAreaEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            PhotoEntity updatedEntity = factory.manufacturePojo(PhotoEntity.class);
            updatedEntity.setArea("");
            photoService.updatePhoto(photoList.get(0).getId(), updatedEntity);
        });
    }

    @Test
    void testDeletePhotoValid() throws EntityNotFoundException, IllegalOperationException {
        PhotoEntity entity = photoList.get(1);
        photoService.deletePhoto(entity.getId());
        PhotoEntity deleted = entityManager.find(PhotoEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeletePhotoInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> {
            photoService.deletePhoto(0L);
        });
    }
}