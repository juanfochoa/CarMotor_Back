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
import co.edu.uniandes.dse.carmotor.entities.PhotoEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(VehiclePhotoService.class)
public class VehiclePhotoServiceTest {
    @Autowired
    private VehiclePhotoService vehiclePhotoService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<VehicleEntity> vehicleList = new ArrayList<>();
    private List<PhotoEntity> photoList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from VehicleEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PhotoEntity").executeUpdate();
        vehicleList.clear();
        photoList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            PhotoEntity photoEntity = factory.manufacturePojo(PhotoEntity.class);
            entityManager.persist(photoEntity);
            photoList.add(photoEntity);
        }
        for (int i = 0; i < 3; i++) {
            VehicleEntity vehicleEntity = factory.manufacturePojo(VehicleEntity.class);
            entityManager.persist(vehicleEntity);
            vehicleList.add(vehicleEntity);
            if (i == 2) {
                vehicleEntity.getPhotos().add(photoList.get(i));
            }
        }
    }

    @Test
    void testAddPhotoToVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicleEntity = vehicleList.get(0);
        PhotoEntity photoEntity = photoList.get(0);
        PhotoEntity result = vehiclePhotoService.addPhotoToVehicle(vehicleEntity.getId(), photoEntity.getId());
        assertNotNull(result);
        assertEquals(photoEntity.getId(), result.getId());
    }

    @Test
    void testAddPhotoToVehicleInvalidVehicle() {
        PhotoEntity photoEntity = photoList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            vehiclePhotoService.addPhotoToVehicle(0L, photoEntity.getId());
        });
    }

    @Test
    void testAddPhotoToVehicleInvalidPhoto() {
        VehicleEntity vehicleEntity = vehicleList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            vehiclePhotoService.addPhotoToVehicle(vehicleEntity.getId(), 0L);
        });
    }

    @Test
    void testGetPhotosFromVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicle = vehicleList.get(2);
        List<PhotoEntity> photos = vehiclePhotoService.getPhotosFromVehicle(vehicle.getId());

        assertNotNull(photos);
        assertEquals(1, photos.size());
        assertEquals(photoList.get(2).getId(), photos.get(0).getId());
    }

    @Test
    void testGetPhotosFromVehicleEmpty() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicle = vehicleList.get(0);
        List<PhotoEntity> photos = vehiclePhotoService.getPhotosFromVehicle(vehicle.getId());

        assertNotNull(photos);
        assertTrue(photos.isEmpty());
    }

    @Test
    void testGetPhotosFromNonExistentVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehiclePhotoService.getPhotosFromVehicle(999L);
        });
    }

    @Test
    void testGetPhotoFromVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicleEntity = vehicleList.get(2);
        PhotoEntity photoEntity = photoList.get(2);
        PhotoEntity result = vehiclePhotoService.getPhotoFromVehicle(vehicleEntity.getId(), photoEntity.getId());
        assertNotNull(result);
        assertEquals(photoEntity.getId(), result.getId());
    }

    @Test
    void testGetPhotoFromVehicleInvalidVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehiclePhotoService.getPhotoFromVehicle(0L, photoList.get(2).getId());
        });
    }

    @Test
    void testGetPhotoFromVehicleInvalidPhoto() {
        VehicleEntity vehicleEntity = vehicleList.get(2);
        assertThrows(EntityNotFoundException.class, () -> {
            vehiclePhotoService.getPhotoFromVehicle(vehicleEntity.getId(), 0L);
        });
    }

    @Test
    void testUpdatePhotosFromVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicleEntity = vehicleList.get(0);
        List<PhotoEntity> newPhotoList = new ArrayList<>();
        newPhotoList.add(photoList.get(0));
        newPhotoList.add(photoList.get(1));
        List<PhotoEntity> updatedPhotos = vehiclePhotoService.updatePhotosFromVehicle(vehicleEntity.getId(), newPhotoList);
        assertNotNull(updatedPhotos);
        assertEquals(newPhotoList.size(), updatedPhotos.size());
        for (PhotoEntity photo : newPhotoList) {
            assertTrue(updatedPhotos.contains(photo));
        }
    }

    @Test
    void testUpdatePhotosFromVehicleInvalidVehicle() {
        List<PhotoEntity> newPhotoList = new ArrayList<>();
        newPhotoList.add(photoList.get(0));
        assertThrows(EntityNotFoundException.class, () -> {
            vehiclePhotoService.updatePhotosFromVehicle(0L, newPhotoList);
        });
    }

    @Test
    void testUpdatePhotosFromVehicleInvalidPhoto() {
        VehicleEntity vehicleEntity = vehicleList.get(0);
        PhotoEntity invalidPhoto = factory.manufacturePojo(PhotoEntity.class);
        invalidPhoto.setId(0L);
        List<PhotoEntity> newPhotoList = new ArrayList<>();
        newPhotoList.add(invalidPhoto);
        assertThrows(EntityNotFoundException.class, () -> {
            vehiclePhotoService.updatePhotosFromVehicle(vehicleEntity.getId(), newPhotoList);
        });
    }

    @Test
    void testDeletePhotoFromVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicleEntity = vehicleList.get(2);
        PhotoEntity photoEntity = photoList.get(2);
        assertTrue(vehicleEntity.getPhotos().contains(photoEntity));
        vehiclePhotoService.deletePhotoFromVehicle(vehicleEntity.getId(), photoEntity.getId());
        assertFalse(vehicleEntity.getPhotos().contains(photoEntity));
    }

    @Test
    void testDeletePhotoFromVehicleInvalidVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehiclePhotoService.deletePhotoFromVehicle(0L, photoList.get(2).getId());
        });
    }

    @Test
    void testDeletePhotoFromVehicleInvalidPhoto() {
        VehicleEntity vehicleEntity = vehicleList.get(2);
        assertThrows(EntityNotFoundException.class, () -> {
            vehiclePhotoService.deletePhotoFromVehicle(vehicleEntity.getId(), 0L);
        });
    }
}