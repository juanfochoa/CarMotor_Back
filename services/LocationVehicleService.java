package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.LocationEntity;
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.LocationRepository;
import co.edu.uniandes.dse.carmotor.repositories.VehicleRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocationVehicleService {
    @Autowired
    private LocationRepository locationRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;

    @Transactional
    public VehicleEntity addVehicleToLocation(Long locationId, Long vehicleId) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Adding vehicle {} to location {}", vehicleId, locationId);
        Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
        if (locationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }
        LocationEntity location = locationEntity.get();

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        location.getVehicles().add(vehicle);
        vehicle.setLocation(location);
        log.info("Vehicle {} added to location {}", vehicleId, locationId);
        return vehicle;
    }
    
    @Transactional
    public List<VehicleEntity> getVehiclesFromLocation(Long locationId) 
            throws EntityNotFoundException {
        log.info("Getting vehicles from location {}", locationId);
        Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
        if (locationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }
        LocationEntity location = locationEntity.get();
        log.info("Vehicles retrieved from location {}", locationId);
        return location.getVehicles();
    }

    @Transactional
    public VehicleEntity getVehicleFromLocation(Long locationId, Long vehicleId) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Getting vehicle {} from location {}", vehicleId, locationId);
        Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
        if (locationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }
        LocationEntity location = locationEntity.get();

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        if (!location.getVehicles().contains(vehicle)) {
            throw new IllegalOperationException("Vehicle isn't related to location with ID: " + locationId);
        }
        log.info("Vehicle {} retrieved from location {}", vehicleId, locationId);
        return vehicle;
    }

    @Transactional
    public List<VehicleEntity> updateVehiclesFromLocation(Long locationId, List<VehicleEntity> vehicleList) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Updating vehicles for location {}", locationId);
        Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
        if (locationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }
        LocationEntity location = locationEntity.get();

        for (VehicleEntity vehicle : vehicleList) {
            Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicle.getId());
            if (vehicleEntity.isEmpty()) {
                throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
            }
            vehicleEntity.get().setLocation(location);
        }
        
        location.setVehicles(vehicleList);
        log.info("Vehicles updated for location {}", locationId);
        return location.getVehicles();
    }

    @Transactional
    public void deleteVehicleFromLocation(Long locationId, Long vehicleId) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Deleting vehicle {} from location {}", vehicleId, locationId);
        Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
        if (locationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }
        LocationEntity location = locationEntity.get();

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }
        VehicleEntity vehicle = vehicleEntity.get();

        location.getVehicles().remove(vehicle);
        vehicle.setLocation(null);
        log.info("Vehicle {} deleted from location {}", vehicleId, locationId);
    }
}