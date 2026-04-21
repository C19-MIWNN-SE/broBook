package nl.miwnn.ch19.binarybros.brobook.service.mapper;

/*
 * @author Mart Stukje
 * */

import nl.miwnn.ch19.binarybros.brobook.dto.NewUserFormDTO;
import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BroBookUserMapper {

    private final PasswordEncoder passwordEncoder;

    public BroBookUserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public BroBookUser toBroBookUser(NewUserFormDTO dto) {
        BroBookUser user = new BroBookUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode("welkom"));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole());
        user.setBirthDate(dto.getBirthDate());
        user.setFutureEmployer(dto.getFutureEmployer());
        return user;
    }
}
