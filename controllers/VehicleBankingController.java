package co.edu.uniandes.dse.carmotor.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.carmotor.dto.BankingDTO;
import co.edu.uniandes.dse.carmotor.entities.BankingEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.services.VehicleBankingService;

@RestController
@RequestMapping("/vehicles/{vehicleId}/bankings")
public class VehicleBankingController {
    @Autowired 
    private VehicleBankingService vehicleBankingService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping(value = "/{bankingId}")
    @ResponseStatus(HttpStatus.CREATED)
    public BankingDTO addBankingToVehicle(@PathVariable Long vehicleId, @PathVariable Long bankingId)
            throws EntityNotFoundException, IllegalOperationException {
        BankingEntity bankingEntity = vehicleBankingService.addBankingToVehicle(vehicleId, bankingId);
        return modelMapper.map(bankingEntity, BankingDTO.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BankingDTO> getBankingsFromVehicle(@PathVariable Long vehicleId) throws EntityNotFoundException {
        List<BankingEntity> bankings = vehicleBankingService.getBankingsFromVehicle(vehicleId);
        return modelMapper.map(bankings, new TypeToken<List<BankingDTO>>() {}.getType());
    }

    @GetMapping(value = "/{bankingId}")
    @ResponseStatus(HttpStatus.OK)
    public BankingDTO getBankingFromVehicle(@PathVariable Long vehicleId, @PathVariable Long bankingId)
            throws EntityNotFoundException, IllegalOperationException {
        BankingEntity bankingEntity = vehicleBankingService.getBankingFromVehicle(vehicleId, bankingId);
        return modelMapper.map(bankingEntity, BankingDTO.class);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BankingDTO> updateBankingsFromVehicle(@PathVariable Long vehicleId, @RequestBody List<BankingDTO> bankings)
            throws EntityNotFoundException, IllegalOperationException {
        List<BankingEntity> updatedBankings = vehicleBankingService.updateBankingsFromVehicle(vehicleId,
                modelMapper.map(bankings, new TypeToken<List<BankingEntity>>() {}.getType()));
        return modelMapper.map(updatedBankings, new TypeToken<List<BankingDTO>>() {}.getType());
    }

    @DeleteMapping(value = "/{bankingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBankingFromVehicle(@PathVariable Long vehicleId, @PathVariable Long bankingId)
            throws EntityNotFoundException, IllegalOperationException {
        vehicleBankingService.deleteBankingFromVehicle(vehicleId, bankingId);
    }
}