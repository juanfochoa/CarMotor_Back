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
import co.edu.uniandes.dse.carmotor.services.VehicleMaintenanceHistoryService;

@RestController
@RequestMapping("/vehicles/{vehicleId}/maintenancehistory")
public class VehicleMaintenanceHistoryController {
    @Autowired
    private VehicleMaintenanceHistoryService vehicleMaintenanceHistoryService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{maintenanceHistoryId}")
    @ResponseStatus(HttpStatus.CREATED)
    public MaintenanceHistoryDTO addMaintenanceHistoryToVehicle(@PathVariable Long vehicleId, @PathVariable Long maintenanceHistoryId)
            throws EntityNotFoundException, IllegalOperationException {
        MaintenanceHistoryEntity maintenanceHistoryEntity = vehicleMaintenanceHistoryService.addMaintenanceHistoryToVehicle(vehicleId, maintenanceHistoryId);
        return modelMapper.map(maintenanceHistoryEntity, MaintenanceHistoryDTO.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MaintenanceHistoryDTO> getMaintenanceHistoriesFromVehicle(@PathVariable Long vehicleId)
            throws EntityNotFoundException {
        List<MaintenanceHistoryEntity> maintenanceHistories = vehicleMaintenanceHistoryService.getMaintenanceHistoriesFromVehicle(vehicleId);
        return modelMapper.map(maintenanceHistories, new TypeToken<List<MaintenanceHistoryDTO>>() {}.getType());
    }

    @GetMapping(value = "/{maintenanceHistoryId}")
    @ResponseStatus(HttpStatus.OK)
    public MaintenanceHistoryDTO getMaintenanceHistoryFromVehicle(@PathVariable Long vehicleId, @PathVariable Long maintenanceHistoryId)
            throws EntityNotFoundException, IllegalOperationException {
        MaintenanceHistoryEntity maintenanceHistoryEntity = vehicleMaintenanceHistoryService.getMaintenanceHistoryFromVehicle(vehicleId, maintenanceHistoryId);
        return modelMapper.map(maintenanceHistoryEntity, MaintenanceHistoryDTO.class);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MaintenanceHistoryDTO> updateMaintenanceHistoriesFromVehicle(@PathVariable Long vehicleId,
            @RequestBody List<MaintenanceHistoryDTO> maintenanceHistories)
            throws EntityNotFoundException, IllegalOperationException {
        List<MaintenanceHistoryEntity> updatedMaintenanceHistories = vehicleMaintenanceHistoryService.updateMaintenanceHistoriesFromVehicle(
                vehicleId, modelMapper.map(maintenanceHistories, new TypeToken<List<MaintenanceHistoryEntity>>() {}.getType()));
        return modelMapper.map(updatedMaintenanceHistories, new TypeToken<List<MaintenanceHistoryDTO>>() {}.getType());
    }

    @DeleteMapping(value = "/{maintenanceHistoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMaintenanceHistoryFromVehicle(@PathVariable Long vehicleId, @PathVariable Long maintenanceHistoryId)
            throws EntityNotFoundException, IllegalOperationException {
        vehicleMaintenanceHistoryService.deleteMaintenanceHistoryFromVehicle(vehicleId, maintenanceHistoryId);
    }
}