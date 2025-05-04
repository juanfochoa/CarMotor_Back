package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.uniandes.dse.carmotor.entities.PhotoEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.PhotoRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PhotoService {
    @Autowired
    private PhotoRepository photoRepository;

    @Transactional
    public PhotoEntity createPhoto(PhotoEntity photoEntity) throws IllegalOperationException {
        log.info("The photo creation process begins");
    
        if (photoEntity.getUri() == null || photoEntity.getUri().length() == 0) {
            throw new IllegalOperationException("Photo uri is not valid");
        }
        if (photoEntity.getArea() == null || photoEntity.getArea().length() == 0) {
            throw new IllegalOperationException("Photo area is not valid");
        }
    
        log.info("The photo creation process ends");
        return photoRepository.save(photoEntity);
    }
    
    @Transactional
    public List<PhotoEntity> getPhotos() {
        log.info("The process of getting all photos begins");
        return photoRepository.findAll();
    }
    
    @Transactional
    public PhotoEntity getPhoto(Long photoId) throws EntityNotFoundException {
        log.info("The process of getting the photo with ID = {} begins", photoId);
        
        Optional<PhotoEntity> photoEntity = photoRepository.findById(photoId);
        if (photoEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.PHOTO_NOT_FOUND);
        }

        log.info("The process of getting the photo with ID = {} ends", photoId);
        return photoEntity.get();
    }
    
    @Transactional
    public PhotoEntity updatePhoto(Long photoId, PhotoEntity photo) throws EntityNotFoundException, IllegalOperationException {
        log.info("The process of updating the photo with ID = {} begins", photoId);

        Optional<PhotoEntity> photoEntity = photoRepository.findById(photoId);
        if (photoEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.PHOTO_NOT_FOUND);
        }

        if (photo.getUri() == null || photo.getUri().length() == 0) {
            throw new IllegalOperationException("Photo uri is not valid");
        }
        if (photo.getArea() == null || photo.getArea().length() == 0) {
            throw new IllegalOperationException("Photo area is not valid");
        }
    
        photo.setId(photoId);

        log.info("The process of updating the photo with ID = {} ends", photoId);
        return photoRepository.save(photo);
    }
    
    @Transactional
    public void deletePhoto(Long photoId) throws EntityNotFoundException {
        log.info("The process of deleting the photo with ID = {} begins", photoId);

        Optional<PhotoEntity> photoEntity = photoRepository.findById(photoId);
        if (photoEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.PHOTO_NOT_FOUND);
        }
    
        photoRepository.deleteById(photoId);

        log.info("The process of deleting the photo with ID = {} ends", photoId);
    }    
}