package co.edu.uniandes.dse.carmotor.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.uniandes.dse.carmotor.dto.UserDTO;
import co.edu.uniandes.dse.carmotor.dto.UserDetailDTO;
import co.edu.uniandes.dse.carmotor.entities.UserEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<UserDetailDTO> findAll() {
        List<UserEntity> users = userService.getUsers();
        return modelMapper.map(users, new TypeToken<List<UserDetailDTO>>() {}.getType());
    }
    
    @GetMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserDetailDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        UserEntity UserEntity = userService.getUser(id);
        return modelMapper.map(UserEntity, UserDetailDTO.class);
    }
    
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDTO create(@RequestBody UserDTO UserDTO) throws IllegalOperationException, EntityNotFoundException {
        UserEntity userEntity = userService.createUser(modelMapper.map(UserDTO, UserEntity.class));
        return modelMapper.map(userEntity, UserDTO.class);
    }
    
    @PutMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserDTO update(@PathVariable Long id, @RequestBody UserDTO UserDTO)
            throws EntityNotFoundException, IllegalOperationException {
        UserEntity UserEntity = userService.updateUser(id, modelMapper.map(UserDTO, UserEntity.class));
        return modelMapper.map(UserEntity, UserDTO.class);
    }
    
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        userService.deleteUser(id);
    }
}