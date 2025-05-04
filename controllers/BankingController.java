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
import co.edu.uniandes.dse.carmotor.services.BankingService;

@RestController
@RequestMapping("/bankings")
public class BankingController {
    @Autowired
    private BankingService bankingService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BankingDTO> findAll() {
        List<BankingEntity> bankings = bankingService.getBankings();
        return modelMapper.map(bankings, new TypeToken<List<BankingDTO>>() {}.getType());
    }
    
    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BankingDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        BankingEntity bankingEntity = bankingService.getBanking(id);
        return modelMapper.map(bankingEntity, BankingDTO.class);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BankingDTO create(@RequestBody BankingDTO bankingDTO) throws IllegalOperationException, EntityNotFoundException {
        BankingEntity bankingEntity = bankingService.createBanking(modelMapper.map(bankingDTO, BankingEntity.class));
        return modelMapper.map(bankingEntity, BankingDTO.class);
    }
    
    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BankingDTO update(@PathVariable Long id, @RequestBody BankingDTO bankingDTO)
                        throws EntityNotFoundException, IllegalOperationException {
        BankingEntity bankingEntity = bankingService.updateBanking(id, modelMapper.map(bankingDTO, BankingEntity.class));
        return modelMapper.map(bankingEntity, BankingDTO.class);
    }
    
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        bankingService.deleteBanking(id);
    }
}