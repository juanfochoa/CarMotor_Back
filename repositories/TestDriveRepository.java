package co.edu.uniandes.dse.carmotor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.carmotor.entities.TestDriveEntity;

@Repository
public interface TestDriveRepository extends JpaRepository<TestDriveEntity, Long> {

}