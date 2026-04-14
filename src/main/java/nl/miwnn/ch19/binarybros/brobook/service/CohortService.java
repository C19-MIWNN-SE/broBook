package nl.miwnn.ch19.binarybros.brobook.service;

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import nl.miwnn.ch19.binarybros.brobook.repository.CohortRepository;
import org.springframework.stereotype.Service;

/**
 * @author Paul Rademaker
 * ---- VERVANG MIJ ----
 */

@Service
public class CohortService {

    private final CohortRepository cohortRepository;

    public CohortService(CohortRepository cohortRepository) {
        this.cohortRepository = cohortRepository;
    }

    public void save(Cohort cohort){
        cohortRepository.save(cohort);
    }
}
