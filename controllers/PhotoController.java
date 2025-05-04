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
import co.edu.uniandes.dse.carmotor.services.PhotoService;

@RestController
@RequestMapping("/photos")
public class PhotoController {
    @Autowired
    private PhotoService photoService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<PhotoDTO> findAll() {
        List<PhotoEntity> photos = photoService.getPhotos();
        return modelMapper.map(photos, new TypeToken<List<PhotoDTO>>() {}.getType());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PhotoDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        PhotoEntity photoEntity = photoService.getPhoto(id);
        return modelMapper.map(photoEntity, PhotoDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public PhotoDTO create(@RequestBody PhotoDTO photoDTO)
            throws IllegalOperationException, EntityNotFoundException {
        PhotoEntity photoEntity = photoService.createPhoto(modelMapper.map(photoDTO, PhotoEntity.class));
        return modelMapper.map(photoEntity, PhotoDTO.class);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public PhotoDTO update(@PathVariable Long id, @RequestBody PhotoDTO photoDTO)
            throws EntityNotFoundException, IllegalOperationException {
        PhotoEntity photoEntity = photoService.updatePhoto(id, modelMapper.map(photoDTO, PhotoEntity.class));
        return modelMapper.map(photoEntity, PhotoDTO.class);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        photoService.deletePhoto(id);
    }
}