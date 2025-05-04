package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import co.edu.uniandes.dse.carmotor.repositories.PhotoRepository;
import co.edu.uniandes.dse.carmotor.repositories.VehicleRepository;
import co.edu.uniandes.dse.carmotor.entities.PhotoEntity;
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Transactional
    public VehicleEntity createVehicle(VehicleEntity vehicleEntity) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("The vehicle creation process begins");
    
        if (vehicleEntity.getBrand() == null || vehicleEntity.getBrand().length() == 0) {
            throw new IllegalOperationException("Vehicle brand is not valid");
        }
        if (vehicleEntity.getSeries() == null || vehicleEntity.getSeries().length() == 0) {
            throw new IllegalOperationException("Vehicle series is not valid");
        }
        if (vehicleEntity.getLastPlateDigit() == null || vehicleEntity.getLastPlateDigit().length() == 0) {
            throw new IllegalOperationException("Vehicle last plate digit is not valid");
        }
        if (vehicleEntity.getModel() == null || vehicleEntity.getModel().length() == 0) {
            throw new IllegalOperationException("Vehicle model is not valid");
        }
        if (vehicleEntity.getType() == null || vehicleEntity.getType().length() == 0) {
            throw new IllegalOperationException("Vehicle type is not valid");
        }
        if (vehicleEntity.getCapacity() == null || vehicleEntity.getCapacity() <= 0) {
            throw new IllegalOperationException("Vehicle capacity is not valid");
        }
        if (vehicleEntity.getPrice() == null || Double.isNaN(vehicleEntity.getPrice()) || Double.isInfinite(vehicleEntity.getPrice())) {
            throw new IllegalOperationException("Vehicle price is not valid");
        }

        log.info("The vehicle creation process ends");
        return vehicleRepository.save(vehicleEntity);
    }
    
    @Transactional
    public List<VehicleEntity> getVehicles() {
        log.info("The process of getting all vehicles begins");
        return vehicleRepository.findAll();
    }
    
    @Transactional
    public VehicleEntity getVehicle(Long vehicleId) throws EntityNotFoundException {
        log.info("The process of getting the vehicle with ID = {0} begins", vehicleId);

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }

        log.info("The process of getting the vehicle with ID = {0} ends", vehicleId);
        return vehicleEntity.get();
    }
    
    @Transactional
    public VehicleEntity updateVehicle(Long vehicleId, VehicleEntity vehicle) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("The process of updating the vehicle with ID = {0} begins", vehicleId);

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }

        if (vehicle.getBrand() == null || vehicle.getBrand().length() == 0) {
            throw new IllegalOperationException("Vehicle brand is not valid");
        }
        if (vehicle.getSeries() == null || vehicle.getSeries().length() == 0) {
            throw new IllegalOperationException("Vehicle series is not valid");
        }
        if (vehicle.getLastPlateDigit() == null || vehicle.getLastPlateDigit().length() == 0) {
            throw new IllegalOperationException("Vehicle last plate digit is not valid");
        }
        if (vehicle.getModel() == null || vehicle.getModel().length() == 0) {
            throw new IllegalOperationException("Vehicle model is not valid");
        }
        if (vehicle.getType() == null || vehicle.getType().length() == 0) {
            throw new IllegalOperationException("Vehicle type is not valid");
        }
        if (vehicle.getCapacity() == null || vehicle.getCapacity() <= 0) {
            throw new IllegalOperationException("Vehicle capacity is not valid");
        }
        if (vehicle.getPrice() == null || Double.isNaN(vehicle.getPrice()) || Double.isInfinite(vehicle.getPrice())) {
            throw new IllegalOperationException("Vehicle price is not valid");
        }

        vehicle.setId(vehicleId);

        log.info("The process of updating the vehicle with ID = {0} ends", vehicleId);
        return vehicleRepository.save(vehicle);
    }
    
    @Transactional
    public void deleteVehicle(Long vehicleId) throws EntityNotFoundException, IllegalOperationException {
        log.info("The process of deleting the vehicle with ID = {0} begins", vehicleId);

        Optional<VehicleEntity> vehicleEntity = vehicleRepository.findById(vehicleId);
        if (vehicleEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.VEHICLE_NOT_FOUND);
        }

        for (PhotoEntity photo : vehicleEntity.get().getPhotos()) {
            photoRepository.deleteById(photo.getId());
        }
        
        vehicleRepository.deleteById(vehicleId);
        
        log.info("The process of deleting the vehicle with ID = {0} ends", vehicleId);
    }    
}