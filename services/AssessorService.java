package co.edu.uniandes.dse.carmotor.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.edu.uniandes.dse.carmotor.entities.AssessorEntity;
import co.edu.uniandes.dse.carmotor.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.carmotor.exceptions.ErrorMessage;
import co.edu.uniandes.dse.carmotor.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.carmotor.repositories.AssessorRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssessorService {
    @Autowired
    private AssessorRepository assessorRepository;

    @Transactional
    public AssessorEntity createAssessor(AssessorEntity assessorEntity) throws IllegalOperationException {
        log.info("The assessor creation process begins");

        if (assessorEntity.getName() == null || assessorEntity.getName().length() == 0) {
            throw new IllegalOperationException("Assessor name is not valid");
        }
        if (assessorEntity.getUriPhoto() == null || assessorEntity.getUriPhoto().length() == 0) {
            throw new IllegalOperationException("Assessor uri photo is not valid");
        }
        if (assessorEntity.getContactInfo() == null || assessorEntity.getContactInfo().length() == 0) {
            throw new IllegalOperationException("Assessor contact information is not valid");
        }

        log.info("The assessor creation process ends");
        if (assessorEntity.getName() == null || assessorEntity.getName().trim().isEmpty()) {
            throw new IllegalOperationException("The assessor name cannot be null or empty.");
        }
        if (assessorEntity.getContactInfo() == null || assessorEntity.getContactInfo().trim().isEmpty()) {
            throw new IllegalOperationException("The assessor contact info cannot be null or empty.");
        }
        if (assessorEntity.getUriPhoto() == null || assessorEntity.getUriPhoto().trim().isEmpty()) {
            throw new IllegalOperationException("The assessor photo URI cannot be null or empty.");
        }
        
        return assessorRepository.save(assessorEntity);
    }

    @Transactional
    public List<AssessorEntity> getAssessors() {
        log.info("The process of getting all assessors begins");
        return assessorRepository.findAll();
    }

    @Transactional
    public AssessorEntity getAssessor(Long assessorId) throws EntityNotFoundException {
        log.info("The process of getting the assessor with ID = {} begins", assessorId);

        Optional<AssessorEntity> assessorEntity = assessorRepository.findById(assessorId);
        if (assessorEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.ASSESSOR_NOT_FOUND);
        
        log.info("The process of getting the assessor with ID = {} ends", assessorId);
        return assessorEntity.get();
    }

    @Transactional
    public AssessorEntity updateAssessor(Long assessorId, AssessorEntity assessor) throws EntityNotFoundException, IllegalOperationException {
        log.info("The process of updating the assessor with ID = {} begins", assessorId);

        Optional<AssessorEntity> assessorEntity = assessorRepository.findById(assessorId);
        if (assessorEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.ASSESSOR_NOT_FOUND);

        if (assessor.getName() == null || assessor.getName().length() == 0) {
            throw new IllegalOperationException("Assessor name is not valid");
        }
        if (assessor.getUriPhoto() == null || assessor.getUriPhoto().length() == 0) {
            throw new IllegalOperationException("Assessor uri photo is not valid");
        }
        if (assessor.getContactInfo() == null || assessor.getContactInfo().length() == 0) {
            throw new IllegalOperationException("Assessor contact information is not valid");
        }
        
        assessor.setId(assessorId);
        
        log.info("The process of updating the assessor with ID = {} ends", assessorId);
        return assessorRepository.save(assessor);
    }

    @Transactional
    public void deleteAssessor(Long assessorId) throws EntityNotFoundException, IllegalOperationException {
        log.info("The process of deleting the assessor with ID = {} begins", assessorId);

        Optional<AssessorEntity> assessorEntity = assessorRepository.findById(assessorId);
        if (assessorEntity.isEmpty())
            throw new EntityNotFoundException(ErrorMessage.ASSESSOR_NOT_FOUND);
        
        assessorRepository.deleteById(assessorId);

        log.info("The process of deleting the assessor with ID = {} ends", assessorId);
    }
}