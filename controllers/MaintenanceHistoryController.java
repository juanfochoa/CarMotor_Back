package co.edu.uniandes.dse.carmotor.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.carmotor.dto.MaintenanceHistoryDTO;
import co.edu.uniandes.dse.carmotor.entities.MaintenanceHistoryEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.services.MaintenanceHistoryService;

@RestController
@RequestMapping("/maintenancehistories")
public class MaintenanceHistoryController {
    @Autowired
    private MaintenanceHistoryService maintenanceHistoryService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MaintenanceHistoryDTO> findAll() {
        List<MaintenanceHistoryEntity> maintenances = maintenanceHistoryService.getMaintenanceHistories();
        return modelMapper.map(maintenances, new TypeToken<List<MaintenanceHistoryDTO>>() {}.getType());
    }
    
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MaintenanceHistoryDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        MaintenanceHistoryEntity maintenance = maintenanceHistoryService.getMaintenanceHistory(id);
        return modelMapper.map(maintenance, MaintenanceHistoryDTO.class);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MaintenanceHistoryDTO create(@RequestBody MaintenanceHistoryDTO maintenanceHistoryDto)
            throws IllegalOperationException, EntityNotFoundException {
        MaintenanceHistoryEntity maintenance = maintenanceHistoryService.createMaintenanceHistory(
                modelMapper.map(maintenanceHistoryDto, MaintenanceHistoryEntity.class));
        return modelMapper.map(maintenance, MaintenanceHistoryDTO.class);
    }
    
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MaintenanceHistoryDTO update(@PathVariable Long id, @RequestBody MaintenanceHistoryDTO maintenanceHistoryDTO)
            throws EntityNotFoundException, IllegalOperationException {
        MaintenanceHistoryEntity maintenance = maintenanceHistoryService.updateMaintenanceHistory(
                id, modelMapper.map(maintenanceHistoryDTO, MaintenanceHistoryEntity.class));
        return modelMapper.map(maintenance, MaintenanceHistoryDTO.class);
    }
    
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        maintenanceHistoryService.deleteMaintenanceHistory(id);
    }
}