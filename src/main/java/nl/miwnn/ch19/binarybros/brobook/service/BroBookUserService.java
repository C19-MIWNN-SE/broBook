
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

import java.io.IOException;
import java.util.*;

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
                .orElseThrow(() -> new UsernameNotFoundException("Gebruiker niet gevonden: " + username));
    }

    public List<BroBookUser> findAll() {
        return userRepository.findAll();
    }

    public BroBookUser getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public BroBookUser getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<BroBookUser> findVisibleUsers(BroBookUser currentUser) {
        if (currentUser.getRole() != null &&
                ("ADMIN".equalsIgnoreCase(currentUser.getRole()) || "Teacher".equalsIgnoreCase(currentUser.getRole()))) {
            return userRepository.findAll();
        }

        Set<BroBookUser> visibleUsers = new HashSet<>();

        visibleUsers.addAll(userRepository.findByRole("Teacher"));

        visibleUsers.add(currentUser);

        if (currentUser.getCohorts() != null) {
            for (Cohort cohort : currentUser.getCohorts()) {
                visibleUsers.addAll(cohort.getParticipants());
            }
        }

        List<BroBookUser> result = new ArrayList<>(visibleUsers);
        result.sort(Comparator.comparing(BroBookUser::getLastName, Comparator.nullsLast(Comparator.naturalOrder())));

        return result;
    }
    public void save(BroBookUser user, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Image profilePicture = user.getProfilePicture();

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

        userRepository.save(user);
    }

    public void saveNewUser(NewUserFormDTO dto) {
        BroBookUser newUser = broBookUserMapper.toBroBookUser(dto);
        List<Cohort> cohorts = cohortService.findAllById(dto.getCohortIds());
        newUser.setCohorts(cohorts);
        userRepository.save(newUser);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}