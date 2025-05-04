package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.UserEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Transactional
    public UserEntity createUser(UserEntity userEntity) throws EntityNotFoundException, IllegalOperationException {
        log.info("Starting user creation process");

        if (userEntity.getName() == null || userEntity.getName().isEmpty()) {
            throw new IllegalOperationException("User name is not valid");
        }
        if (userEntity.getEmail() == null || userEntity.getEmail().isEmpty()) {
            throw new IllegalOperationException("User email is not valid");
        }
        if (userEntity.getIdentifier() == null || userEntity.getIdentifier().isEmpty()) {
            throw new IllegalOperationException("User identifier is not valid");
        }
        if (userEntity.getPhone() == null || userEntity.getPhone().isEmpty()) {
            throw new IllegalOperationException("User phone is not valid");
        }
        if (userEntity.getRole() == null) {
            throw new IllegalOperationException("User role is not valid");
        }
        
        log.info("User creation process finished");
        return userRepository.save(userEntity);
    }

    @Transactional
    public List<UserEntity> getUsers() {
        log.info("Consultation process for all users begins");
        return userRepository.findAll();
    }

    @Transactional
    public UserEntity getUser(Long userId) throws EntityNotFoundException {
        log.info("Start query process for user with id = {}", userId);
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));
    }

    @Transactional
    public UserEntity updateUser(Long userId, UserEntity user) throws EntityNotFoundException, IllegalOperationException {
        log.info("Starting the process of updating the user with id = {}", userId);

        Optional<UserEntity> existingUser = userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalOperationException("User name is not valid");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalOperationException("User email is not valid");
        }
        if (user.getIdentifier() == null || user.getIdentifier().isEmpty()) {
            throw new IllegalOperationException("User identifier is not valid");
        }
        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            throw new IllegalOperationException("User phone is not valid");
        }
        if (user.getRole() == null) {
            throw new IllegalOperationException("User role is not valid");
        }

        user.setId(userId);

        log.info("Finishing the process of updating the user with id = {}", userId);
        return userRepository.save(user);
    }
    
    @Transactional
    public void deleteUser(Long userId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Starting the process of deleting the user with id = {}", userId);

        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        userRepository.delete(userOpt.get());

        log.info("Finishing the process of deleting the user with id = {}", userId);
    }
}