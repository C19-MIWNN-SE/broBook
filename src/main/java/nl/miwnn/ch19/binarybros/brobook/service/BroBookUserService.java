
/*
 * @author Mart Stukje
 * Handles all business logic regarding broBook users
 * */

package nl.miwnn.ch19.binarybros.brobook.service;

import nl.miwnn.ch19.binarybros.brobook.dto.NewUserFormDTO;
import nl.miwnn.ch19.binarybros.brobook.dto.UserInfoFormDTO;
import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import nl.miwnn.ch19.binarybros.brobook.repository.BroBookUserRepository;
import nl.miwnn.ch19.binarybros.brobook.service.mapper.BroBookUserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class BroBookUserService implements UserDetailsService {

    private final BroBookUserRepository userRepository;
    private final ImageService imageService;
    private final BroBookUserMapper broBookUserMapper;
    private final CohortService cohortService;

    public BroBookUserService(BroBookUserRepository userRepository,
                              ImageService imageService,
                              BroBookUserMapper broBookUserMapper,
                              CohortService cohortService) {
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.broBookUserMapper = broBookUserMapper;
        this.cohortService = cohortService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Gebruiker niet gevonden: " + username));
    }

    public List<BroBookUser> findAll() {
        return userRepository.findAll();
    }

    public BroBookUser getUserById(Long id) {
        return userRepository.getReferenceById(id);
    }

    public UserInfoFormDTO getUserInfoFormDTO(Long id) {
        BroBookUser user = getUserById(id);
        return broBookUserMapper.toUserInfoFormDTO(user);
    }

    public void saveUserInformation(UserInfoFormDTO dto, MultipartFile imageFile) {
        BroBookUser user;
        if (dto.getId() != null) {
            user = getUserById(dto.getId());
        } else {
            user = new BroBookUser();
        }

        user = broBookUserMapper.applyInfoToBroBookUser(dto, user);

        if (imageFile != null && !imageFile.isEmpty()) {
            user.setProfilePicture(imageService.saveImage(imageFile));
        }
        userRepository.save(user);
    }

    public void saveNewUser(NewUserFormDTO dto) {
        BroBookUser newUser = broBookUserMapper.toBroBookUser(dto);
        List<Cohort> cohorts = cohortService.findAllById(dto.getCohortIds());
        newUser.setCohorts(cohorts);
        userRepository.save(newUser);
    }
}