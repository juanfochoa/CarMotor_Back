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
import co.edu.uniandes.dse.carmotor.entities.VehicleEntity;
import co.edu.uniandes.dse.carmotor.entities.BankingEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(VehicleBankingService.class)
public class VehicleBankingServiceTest {
    @Autowired
    private VehicleBankingService vehicleBankingService;

    @Autowired 
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    
    private List<VehicleEntity> vehicleList = new ArrayList<>();
    private List<BankingEntity> bankingList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from VehicleEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from BankingEntity").executeUpdate();
        vehicleList.clear();
        bankingList.clear();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            BankingEntity banking = factory.manufacturePojo(BankingEntity.class);
            entityManager.persist(banking);
            bankingList.add(banking);
        }
        for (int i = 0; i < 3; i++) {
            VehicleEntity vehicle = factory.manufacturePojo(VehicleEntity.class);
            entityManager.persist(vehicle);
            vehicleList.add(vehicle);
            if (i == 2) {
                vehicle.getBanks().add(bankingList.get(i));
            }
        }
    }

    @Test
    void testAddBankingToVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicle = vehicleList.get(0);
        BankingEntity banking = bankingList.get(0);
        BankingEntity result = vehicleBankingService.addBankingToVehicle(vehicle.getId(), banking.getId());
        assertNotNull(result);
        assertEquals(banking.getId(), result.getId());
    }

    @Test
    void testAddBankingToVehicleInvalidVehicle() {
        BankingEntity banking = bankingList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleBankingService.addBankingToVehicle(0L, banking.getId());
        });
    }

    @Test
    void testAddBankingToVehicleInvalidBanking() {
        VehicleEntity vehicle = vehicleList.get(0);
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleBankingService.addBankingToVehicle(vehicle.getId(), 0L);
        });
    }
    
    @Test
    void testGetBankingsFromVehicleValid() throws EntityNotFoundException {
        VehicleEntity vehicle = vehicleList.get(2);
        List<BankingEntity> banks = vehicleBankingService.getBankingsFromVehicle(vehicle.getId());
        assertNotNull(banks);
        assertEquals(vehicle.getBanks().size(), banks.size());
        for (BankingEntity bank : banks) {
            assertTrue(vehicle.getBanks().contains(bank));
        }
    }

    @Test
    void testGetBankingsFromVehicleInvalidVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleBankingService.getBankingsFromVehicle(0L);
        });
    }

    @Test
    void testGetBankingFromVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicle = vehicleList.get(2);
        BankingEntity banking = bankingList.get(2);
        BankingEntity result = vehicleBankingService.getBankingFromVehicle(vehicle.getId(), banking.getId());
        assertNotNull(result);
        assertEquals(banking.getId(), result.getId());
    }

    @Test
    void testGetBankingFromVehicleInvalidVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleBankingService.getBankingFromVehicle(0L, bankingList.get(2).getId());
        });
    }

    @Test
    void testGetBankingFromVehicleInvalidBanking() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleBankingService.getBankingFromVehicle(vehicleList.get(2).getId(), 0L);
        });
    }

    @Test
    void testUpdateBankingsFromVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicle = vehicleList.get(0);
        List<BankingEntity> newBankingList = new ArrayList<>();
        newBankingList.add(bankingList.get(0));
        newBankingList.add(bankingList.get(1));
        List<BankingEntity> updatedBanks = vehicleBankingService.updateBankingsFromVehicle(vehicle.getId(), newBankingList);
        assertNotNull(updatedBanks);
        assertEquals(newBankingList.size(), updatedBanks.size());
        for (BankingEntity bank : newBankingList) {
            assertTrue(updatedBanks.contains(bank));
        }
    }

    @Test
    void testUpdateBankingsFromVehicleInvalidVehicle() {
        List<BankingEntity> newBankingList = new ArrayList<>();
        newBankingList.add(bankingList.get(0));
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleBankingService.updateBankingsFromVehicle(0L, newBankingList);
        });
    }

    @Test
    void testUpdateBankingsFromVehicleInvalidBanking() {
        VehicleEntity vehicle = vehicleList.get(0);
        BankingEntity invalidBanking = factory.manufacturePojo(BankingEntity.class);
        invalidBanking.setId(0L);
        List<BankingEntity> newBankingList = new ArrayList<>();
        newBankingList.add(invalidBanking);
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleBankingService.updateBankingsFromVehicle(vehicle.getId(), newBankingList);
        });
    }

    @Test
    void testDeleteBankingFromVehicleValid() throws EntityNotFoundException, IllegalOperationException {
        VehicleEntity vehicle = vehicleList.get(2);
        BankingEntity banking = bankingList.get(2);
        assertTrue(vehicle.getBanks().contains(banking));
        vehicleBankingService.deleteBankingFromVehicle(vehicle.getId(), banking.getId());
        assertFalse(vehicle.getBanks().contains(banking));
    }

    @Test
    void testDeleteBankingFromVehicleInvalidVehicle() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleBankingService.deleteBankingFromVehicle(0L, bankingList.get(2).getId());
        });
    }

    @Test
    void testDeleteBankingFromVehicleInvalidBanking() {
        assertThrows(EntityNotFoundException.class, () -> {
            vehicleBankingService.deleteBankingFromVehicle(vehicleList.get(2).getId(), 0L);
        });
    }
}