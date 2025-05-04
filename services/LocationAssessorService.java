package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.LocationEntity;
import co.edu.uniandes.dse.carmotor.entities.AssessorEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.LocationRepository;
import co.edu.uniandes.dse.carmotor.repositories.AssessorRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LocationAssessorService {
    @Autowired
    private LocationRepository locationRepository;
    
    @Autowired
    private AssessorRepository assessorRepository;

    @Transactional
    public AssessorEntity addAssessorToLocation(Long locationId, Long assessorId) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Adding assessor {} to location {}", assessorId, locationId);
        Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
        if (locationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }
        LocationEntity location = locationEntity.get();

        Optional<AssessorEntity> assessorEntity = assessorRepository.findById(assessorId);
        if (assessorEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSESSOR_NOT_FOUND);
        }
        AssessorEntity assessor = assessorEntity.get();

        location.getAssessors().add(assessor);
        assessor.setLocation(location);
        log.info("Assessor {} added to location {}", assessorId, locationId);
        return assessor;
    }
    
    @Transactional
    public List<AssessorEntity> getAssessorsFromLocation(Long locationId) 
            throws EntityNotFoundException {
        log.info("Getting assessors from location {}", locationId);
        Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
        if (locationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }
        LocationEntity location = locationEntity.get();

        return location.getAssessors();
    }

    @Transactional
    public AssessorEntity getAssessorFromLocation(Long locationId, Long assessorId) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Getting assessor {} from location {}", assessorId, locationId);
        Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
        if (locationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }
        LocationEntity location = locationEntity.get();

        Optional<AssessorEntity> assessorEntity = assessorRepository.findById(assessorId);
        if (assessorEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSESSOR_NOT_FOUND);
        }
        AssessorEntity assessor = assessorEntity.get();

        if (!location.getAssessors().contains(assessor)) {
            throw new IllegalOperationException("Assessor isn't related to location with ID: " + locationId);
        }

        return assessor;
    }

    @Transactional
    public List<AssessorEntity> updateAssessorsFromLocation(Long locationId, List<AssessorEntity> assessorList) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Updating assessors for location {}", locationId);
        Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
        if (locationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }
        LocationEntity location = locationEntity.get();

        for (AssessorEntity assessor : assessorList) {
            Optional<AssessorEntity> assessorEntity = assessorRepository.findById(assessor.getId());
            if (assessorEntity.isEmpty()) {
                throw new EntityNotFoundException(ErrorMessage.ASSESSOR_NOT_FOUND);
            }
            assessorEntity.get().setLocation(location);
        }
        
        location.setAssessors(assessorList);
        log.info("Assessors updated for location {}", locationId);
        return location.getAssessors();
    }

    @Transactional
    public void deleteAssessorFromLocation(Long locationId, Long assessorId) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Deleting assessor {} from location {}", assessorId, locationId);
        Optional<LocationEntity> locationEntity = locationRepository.findById(locationId);
        if (locationEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND);
        }
        LocationEntity location = locationEntity.get();

        Optional<AssessorEntity> assessorEntity = assessorRepository.findById(assessorId);
        if (assessorEntity.isEmpty()) {
            throw new EntityNotFoundException(ErrorMessage.ASSESSOR_NOT_FOUND);
        }
        AssessorEntity assessor = assessorEntity.get();

        location.getAssessors().remove(assessor);
        assessor.setLocation(null);
        log.info("Assessor {} deleted from location {}", assessorId, locationId);
    }
}