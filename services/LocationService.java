package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.AssessorEntity;
import co.edu.uniandes.dse.carmotor.entities.LocationEntity;
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.LocationRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocationService {
    @Autowired
    private LocationRepository locationRepository;

    @Transactional
    public LocationEntity createLocation(LocationEntity locationEntity) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("The location creation process begins");

        if (locationEntity.getName() == null || locationEntity.getName().isEmpty()) {
            throw new IllegalOperationException("Location name is not valid");
        }
        if (locationEntity.getAddress() == null || locationEntity.getAddress().isEmpty()) {
            throw new IllegalOperationException("Location address is not valid");
        }
        if (locationEntity.getPhoneNumber() == null || locationEntity.getPhoneNumber().isEmpty()) {
            throw new IllegalOperationException("Location phone number is not valid");
        }
        if (locationEntity.getSchedule() == null || locationEntity.getSchedule().isEmpty()) {
            throw new IllegalOperationException("Location schedule is not valid");
        }

        log.info("The location creation process ends");
        return locationRepository.save(locationEntity);
    }

    @Transactional
    public List<LocationEntity> getLocations() {
        log.info("The process of retrieving all locations begins");
        return locationRepository.findAll();
    }

    @Transactional
    public LocationEntity getLocation(Long locationId) throws EntityNotFoundException {
        log.info("The process of retrieving the location with ID = {} begins", locationId);

        Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
        if (locationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }

        log.info("The process of retrieving the location with ID = {} ends", locationId);
        return locationEntity.get();
    }

    @Transactional
    public LocationEntity updateLocation(Long locationId, LocationEntity location) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("The process of updating the location with ID = {} begins", locationId);

        Optional<LocationEntity> existingLocation = locationRepository.findById(locationId);
        if (existingLocation.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }

        if (location.getName() == null || location.getName().isEmpty()) {
            throw new IllegalOperationException("Location name is not valid");
        }
        if (location.getAddress() == null || location.getAddress().isEmpty()) {
            throw new IllegalOperationException("Location address is not valid");
        }
        if (location.getPhoneNumber() == null || location.getPhoneNumber().isEmpty()) {
            throw new IllegalOperationException("Location phone number is not valid");
        }
        if (location.getSchedule() == null || location.getSchedule().isEmpty()) {
            throw new IllegalOperationException("Location schedule is not valid");
        }

        location.setId(locationId);

        log.info("The process of updating the location with ID = {} ends", locationId);
        return locationRepository.save(location);
    }

    @Transactional
    public void deleteLocation(Long locationId) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("The process of deleting the location with ID = {} begins", locationId);

        Optional<LocationEntity> locationOptional = locationRepository.findById(locationId);
        if (locationOptional.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }

        LocationEntity locationEntity = locationOptional.get();

        for (AssessorEntity assessor : locationEntity.getAssessors()) {
            assessor.setLocation(null);
        }

        for (VehicleEntity vehicle : locationEntity.getVehicles()) {
            vehicle.setLocation(null);
        }

        locationRepository.delete(locationEntity);
        log.info("The process of deleting the location with ID = {} ends", locationId);
    }    
}