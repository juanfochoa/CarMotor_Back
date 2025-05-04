package co.edu.uniandes.dse.carmotor.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.PhotoEntity;
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.PhotoRepository;
import co.edu.uniandes.dse.carmotor.repositories.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VehiclePhotoService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Transactional
    public PhotoEntity addPhotoToVehicle(Long vehicleId, Long photoId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Adding photo {} to vehicle {}", photoId, vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        Optional<PhotoEntity> photoEntity = photoRepository.findById(photoId);
        if (photoEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.PHOTO_NOT_FOUND);
        }
        PhotoEntity photo = photoEntity.get();

        vehicle.getPhotos().add(photo);
        vehicleRepository.save(vehicle);
        log.info("Photo {} added to vehicle {}", photoId, vehicleId);
        return photo;
    }

    @Transactional
    public List<PhotoEntity> getPhotosFromVehicle(Long vehicleId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Getting photos from vehicle {}", vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();
        List<PhotoEntity> photos = vehicle.getPhotos();
        log.info("Photos retrieved from vehicle {}", vehicleId);
        return photos;
    }

    @Transactional
    public PhotoEntity getPhotoFromVehicle(Long vehicleId, Long photoId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Getting photo {} from vehicle {}", photoId, vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        Optional<PhotoEntity> photoEntity = photoRepository.findById(photoId);
        if (photoEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.PHOTO_NOT_FOUND);
        }
        PhotoEntity photo = photoEntity.get();

        if (!vehicle.getPhotos().contains(photo)) {
            throw new IllegalOperationException("The photo is not associated with the vehicle");
        }
        log.info("Photo {} retrieved from vehicle {}", photoId, vehicleId);
        return photo;
    }

    @Transactional
    public List<PhotoEntity> updatePhotosFromVehicle(Long vehicleId, List<PhotoEntity> photoList) throws EntityNotFoundException, IllegalOperationException {
        log.info("Updating photos for vehicle {}", vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();
    
        List<PhotoEntity> validatedPhotos = new ArrayList<>();
        for (PhotoEntity photo : photoList) {
            if (photo == null || photo.getId() == null) {
                throw new IllegalOperationException("Photo or Photo ID cannot be null.");
            }
            Optional<PhotoEntity> photoEntity = photoRepository.findById(photo.getId());
            if (photoEntity.isEmpty()) {
                throw new EntityNotFoundException(ErrorMessage.PHOTO_NOT_FOUND);
            }
            validatedPhotos.add(photoEntity.get());
        }
    
        vehicle.getPhotos().clear();
        vehicle.getPhotos().addAll(validatedPhotos);
        vehicleRepository.save(vehicle);
        log.info("Photos updated for vehicle {}", vehicleId);
        return validatedPhotos;
    }
    
    @Transactional
    public void deletePhotoFromVehicle(Long vehicleId, Long photoId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Deleting photo {} from vehicle {}", photoId, vehicleId);
        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        Optional<PhotoEntity> photoEntity = photoRepository.findById(photoId);
        if (photoEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.PHOTO_NOT_FOUND);
        }
        PhotoEntity photo = photoEntity.get();

        vehicle.getPhotos().remove(photo);
        photoRepository.deleteById(photoId);
        log.info("Photo {} deleted from vehicle {}", photoId, vehicleId);
    }
}