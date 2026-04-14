
/*
 * @author Mart Stukje
 * */

package nl.miwnn.ch19.binarybros.brobook.service;

import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.repository.BroBookUserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BroBookUserService {

    private final BroBookUserRepository userRepository;

    public BroBookUserService(BroBookUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<BroBookUser> findAll() {
        return userRepository.findAll();
    }

    public void save(BroBookUser user) {
        userRepository.save(user);
    }
}