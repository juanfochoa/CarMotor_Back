package co.edu.uniandes.dse.carmotor.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.carmotor.dto.PhotoDTO;
import co.edu.uniandes.dse.carmotor.entities.PhotoEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.services.VehiclePhotoService;

@RestController
@RequestMapping("/vehicles/{vehicleId}/photos")
public class VehiclePhotoController {
    @Autowired
    private VehiclePhotoService vehiclePhotoService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{photoId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PhotoDTO addPhotoToVehicle(@PathVariable Long vehicleId, @PathVariable Long photoId)
            throws EntityNotFoundException, IllegalOperationException {
        PhotoEntity photoEntity = vehiclePhotoService.addPhotoToVehicle(vehicleId, photoId);
        return modelMapper.map(photoEntity, PhotoDTO.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PhotoDTO> getPhotosFromVehicle(@PathVariable Long vehicleId)
            throws EntityNotFoundException, IllegalOperationException {
        List<PhotoEntity> photos = vehiclePhotoService.getPhotosFromVehicle(vehicleId);
        return modelMapper.map(photos, new TypeToken<List<PhotoDTO>>() {}.getType());
    }

    @GetMapping(value = "/{photoId}")
    @ResponseStatus(HttpStatus.OK)
    public PhotoDTO getPhotoFromVehicle(@PathVariable Long vehicleId, @PathVariable Long photoId)
            throws EntityNotFoundException, IllegalOperationException {
        PhotoEntity photoEntity = vehiclePhotoService.getPhotoFromVehicle(vehicleId, photoId);
        return modelMapper.map(photoEntity, PhotoDTO.class);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PhotoDTO> updatePhotosFromVehicle(@PathVariable Long vehicleId, @RequestBody List<PhotoDTO> photos)
            throws EntityNotFoundException, IllegalOperationException {
        List<PhotoEntity> photoEntities = modelMapper.map(photos, new TypeToken<List<PhotoEntity>>() {}.getType());
        List<PhotoEntity> updated = vehiclePhotoService.updatePhotosFromVehicle(vehicleId, photoEntities);
        return modelMapper.map(updated, new TypeToken<List<PhotoDTO>>() {}.getType());
    }

    @DeleteMapping(value = "/{photoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePhotoFromVehicle(@PathVariable Long vehicleId, @PathVariable Long photoId)
            throws EntityNotFoundException, IllegalOperationException {
        vehiclePhotoService.deletePhotoFromVehicle(vehicleId, photoId);
    }
}