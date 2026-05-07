
/*
 * @author Mart Stukje
 * Handles all business logic regarding broBook users
 * */

package nl.miwnn.ch19.binarybros.brobook.service;

import com.opencsv.bean.CsvToBeanBuilder;
import nl.miwnn.ch19.binarybros.brobook.dto.UserAccountFormDTO;
import nl.miwnn.ch19.binarybros.brobook.dto.UserInfoFormDTO;
import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import nl.miwnn.ch19.binarybros.brobook.model.Role;
import nl.miwnn.ch19.binarybros.brobook.model.UserActivation;
import nl.miwnn.ch19.binarybros.brobook.repository.BroBookUserRepository;
import nl.miwnn.ch19.binarybros.brobook.repository.CohortRepository;
import nl.miwnn.ch19.binarybros.brobook.service.mapper.BroBookUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

@Service
public class BroBookUserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(BroBookUserService.class);

    private final BroBookUserRepository userRepository;
    private final ImageService imageService;
    private final BroBookUserMapper broBookUserMapper;
    private final CohortService cohortService;
    private final CohortRepository cohortRepository;
    private final UserActivationService userActivationService;

    public BroBookUserService(BroBookUserRepository userRepository,
                              ImageService imageService,
                              BroBookUserMapper broBookUserMapper,
                              CohortService cohortService, CohortRepository cohortRepository,
                              UserActivationService userActivationService) {
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.broBookUserMapper = broBookUserMapper;
        this.cohortService = cohortService;
        this.cohortRepository = cohortRepository;
        this.userActivationService = userActivationService;
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
        if (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.TEACHER) {
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

    public List<BroBookUser> getParticipantsByRole(Cohort cohort, Role role) {
        return cohort.getParticipants()
                .stream()
                .filter(user -> user.getRole() == role)
                .toList();
    }

    public UserAccountFormDTO getUserAccountFormDTO(Long id) {
        BroBookUser user = getUserById(id);
        return broBookUserMapper.toUserAccountFormDTO(user);
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

    @Transactional
    public UserActivation saveUserAccount(UserAccountFormDTO dto) {
        boolean isNewUser = dto.getId() == null;
        BroBookUser user;

        if (!isNewUser) {
            user = getUserById(dto.getId());
        } else {
            user = new BroBookUser();
        }

        user = broBookUserMapper.toBroBookUser(dto, user);
        List<Cohort> cohorts = cohortService.findAllById(dto.getCohortIds());
        user.setCohorts(cohorts);
        userRepository.save(user);

        return isNewUser ? userActivationService.generateActivation(user) : null;
    }

    public boolean usernameAlreadyInUse(String name, Long userId) {
        Optional<BroBookUser> existingUser = userRepository.findByUsername(name);
        if (existingUser.isEmpty()) {
            return false;
        }
        if (userId == null) {
            return true;
        }
        return !existingUser.get().getId().equals(userId);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public void importUsersFromCsv(MultipartFile file, Long cohortId) throws Exception {
        Cohort targetCohort = cohortRepository.findById(cohortId)
                .orElseThrow(() -> new Exception("Geselecteerd cohort niet gevonden."));

        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            List<BroBookUser> users = new CsvToBeanBuilder<BroBookUser>(reader)
                    .withType(BroBookUser.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(',')
                    .build()
                    .parse();

            for (BroBookUser user : users) {
                if (user.getRole() == null) user.setRole(Role.STUDENT);
                user.getCohorts().add(targetCohort);
                userRepository.save(user);
            }
        }
    }
}