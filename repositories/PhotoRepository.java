package co.edu.uniandes.dse.carmotor.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.uniandes.dse.carmotor.entities.PhotoEntity;

@Repository
public interface PhotoRepository extends JpaRepository<PhotoEntity, Long> {
    
}