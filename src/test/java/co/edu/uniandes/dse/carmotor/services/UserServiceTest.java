package co.edu.uniandes.dse.carmotor.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import co.edu.uniandes.dse.carmotor.entities.UserEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(UserService.class)
class UserServiceTest {
    @Autowired
    private UserService userService;
    
    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<UserEntity> userList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from UserEntity").executeUpdate();
        userList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            UserEntity userEntity = factory.manufacturePojo(UserEntity.class);
            entityManager.persist(userEntity);
            userList.add(userEntity);
        }
    }

    @Test
    void testCreateUserValid() throws EntityNotFoundException, IllegalOperationException {
        UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
        UserEntity result = userService.createUser(newEntity);
        assertNotNull(result);
        UserEntity storedEntity = entityManager.find(UserEntity.class, result.getId());
        assertEquals(newEntity.getId(), storedEntity.getId());
        assertEquals(newEntity.getName(), storedEntity.getName());
        assertEquals(newEntity.getEmail(), storedEntity.getEmail());
        assertEquals(newEntity.getIdentifier(), storedEntity.getIdentifier());
        assertEquals(newEntity.getPhone(), storedEntity.getPhone());
        assertEquals(newEntity.getRole(), storedEntity.getRole());
    }

    @Test
    void testCreateUserWithInvalidNameNull() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setName(null);
            userService.createUser(newEntity);
        });
    }

    @Test
    void testCreateUserWithInvalidNameEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setName("");
            userService.createUser(newEntity);
        });
    }

    @Test
    void testCreateUserWithInvalidEmailNull() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setEmail(null);
            userService.createUser(newEntity);
        });
    }

    @Test
    void testCreateUserWithInvalidEmailEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setEmail("");
            userService.createUser(newEntity);
        });
    }

    @Test
    void testCreateUserWithInvalidIdentifierNull() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setIdentifier(null);
            userService.createUser(newEntity);
        });
    }

    @Test
    void testCreateUserWithInvalidIdentifierEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setIdentifier("");
            userService.createUser(newEntity);
        });
    }

    @Test
    void testCreateUserWithInvalidPhoneNull() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setPhone(null);
            userService.createUser(newEntity);
        });
    }

    @Test
    void testCreateUserWithInvalidPhoneEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setPhone("");
            userService.createUser(newEntity);
        });
    }

    @Test
    void testCreateUserWithInvalidRoleNull() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setRole(null);
            userService.createUser(newEntity);
        });
    }

    @Test
    void testGetUsersValid() {
        List<UserEntity> list = userService.getUsers();
        assertEquals(userList.size(), list.size());
        for (UserEntity entity : list) {
            boolean found = userList.stream().anyMatch(stored -> stored.getId().equals(entity.getId()));
            assertTrue(found);
        }
    }

    @Test
    void testGetUserValid() throws EntityNotFoundException {
        UserEntity entity = userList.get(0);
        UserEntity resultEntity = userService.getUser(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getName(), resultEntity.getName());
        assertEquals(entity.getEmail(), resultEntity.getEmail());
        assertEquals(entity.getIdentifier(), resultEntity.getIdentifier());
        assertEquals(entity.getPhone(), resultEntity.getPhone());
        assertEquals(entity.getRole(), resultEntity.getRole());
    }

    @Test
    void testGetUserInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUser(0L);
        });
    }

    @Test
    void testUpdateUserValid() throws EntityNotFoundException, IllegalOperationException {
        UserEntity entity = userList.get(0);
        UserEntity updatedEntity = factory.manufacturePojo(UserEntity.class);
        updatedEntity.setId(entity.getId());
        userService.updateUser(entity.getId(), updatedEntity);
        UserEntity resp = entityManager.find(UserEntity.class, entity.getId());
        assertEquals(updatedEntity.getId(), resp.getId());
        assertEquals(updatedEntity.getName(), resp.getName());
        assertEquals(updatedEntity.getEmail(), resp.getEmail());
        assertEquals(updatedEntity.getIdentifier(), resp.getIdentifier());
        assertEquals(updatedEntity.getPhone(), resp.getPhone());
        assertEquals(updatedEntity.getRole(), resp.getRole());
    }

    @Test
    void testUpdateUserInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> {
            UserEntity updatedEntity = factory.manufacturePojo(UserEntity.class);
            updatedEntity.setId(0L);
            userService.updateUser(0L, updatedEntity);
        });
    }

    @Test
    void testUpdateUserWithInvalidNameNull() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setName(null);
            userService.updateUser(userList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateUserWithInvalidNameEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setName("");
            userService.updateUser(userList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateUserWithInvalidEmailNull() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setEmail(null);
            userService.updateUser(userList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateUserWithInvalidEmailEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setEmail("");
            userService.updateUser(userList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateUserWithInvalidIdentifierNull() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setIdentifier(null);
            userService.updateUser(userList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateUserWithInvalidIdentifierEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setIdentifier("");
            userService.updateUser(userList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateUserWithInvalidPhoneNull() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setPhone(null);
            userService.updateUser(userList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateUserWithInvalidPhoneEmpty() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setPhone("");
            userService.updateUser(userList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testUpdateUserWithInvalidRoleNull() {
        assertThrows(IllegalOperationException.class, () -> {
            UserEntity newEntity = factory.manufacturePojo(UserEntity.class);
            newEntity.setRole(null);
            userService.updateUser(userList.get(0).getId(), newEntity);
        });
    }

    @Test
    void testDeleteUserValid() throws EntityNotFoundException, IllegalOperationException {
        UserEntity entity = userList.get(1);
        userService.deleteUser(entity.getId());
        UserEntity deleted = entityManager.find(UserEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteUserInvalidId() {
        assertThrows(EntityNotFoundException.class, () -> {
            userService.deleteUser(0L);
        });
    }
}