package nl.miwnn.ch19.binarybros.brobook.service;

import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import nl.miwnn.ch19.binarybros.brobook.repository.CohortRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Paul Rademaker
 */

@Service
public class  CohortService {

    private final CohortRepository cohortRepository;

    public CohortService(CohortRepository cohortRepository) {
        this.cohortRepository = cohortRepository;
    }

    public List<Cohort> findAll() {
        return cohortRepository.findAll();
    }

    public List<Cohort> findAllById(List<Long> ids) {
        return cohortRepository.findAllById(ids);
    }

    public Cohort getCohortById(Long id) {
        return cohortRepository.findById(id).orElseThrow();
    }

    public void save(Cohort cohort){
        cohortRepository.save(cohort);
    }
}
