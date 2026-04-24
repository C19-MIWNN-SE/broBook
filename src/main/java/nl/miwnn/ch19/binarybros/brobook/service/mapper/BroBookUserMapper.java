package nl.miwnn.ch19.binarybros.brobook.service.mapper;

/*
 * @author Mart Stukje
 * */

import nl.miwnn.ch19.binarybros.brobook.dto.BaseUserFormDTO;
import nl.miwnn.ch19.binarybros.brobook.dto.NewUserFormDTO;
import nl.miwnn.ch19.binarybros.brobook.dto.UserInfoFormDTO;
import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import org.jspecify.annotations.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BroBookUserMapper {

    public BroBookUser toBroBookUser(NewUserFormDTO dto) {
        BroBookUser user = new BroBookUser();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole());
        user.setBirthDate(dto.getBirthDate());
        user.setFutureEmployer(dto.getFutureEmployer());
        return user;
    }

    public BroBookUser applyInfoToBroBookUser(UserInfoFormDTO dto, BroBookUser existing) {
        existing.setBio(dto.getBio());
        existing.setResidence(dto.getResidence());
        existing.setFutureEmployer(dto.getFutureEmployer());
        existing.setBirthDate(dto.getBirthDate());
        return existing;
    }

    public NewUserFormDTO toNewUserFormDTO(BroBookUser user) {
        NewUserFormDTO dto = new NewUserFormDTO();
        setBaseUserFormInfo(dto, user);
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        return dto;
    }

    public UserInfoFormDTO toUserInfoFormDTO(BroBookUser user) {
        UserInfoFormDTO dto = new UserInfoFormDTO();
        setBaseUserFormInfo(dto, user);
        dto.setId(user.getId());
        dto.setBio(user.getBio());
        dto.setResidence(user.getResidence());
        return dto;
    }

    private void setBaseUserFormInfo(BaseUserFormDTO dto, BroBookUser user) {
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setFutureEmployer(user.getFutureEmployer());
        dto.setBirthDate(user.getBirthDate());
        dto.setCohortIds(getCohortIds(user));
    }

    private static List<Long> getCohortIds(BroBookUser user) {
        List<Long> cohortIds = new ArrayList<>();
        List<Cohort> cohorts = user.getCohorts();
        for (Cohort cohort : cohorts) {
            cohortIds.add(cohort.getId());
        }
        return cohortIds;
    }

}
