package nl.miwnn.ch19.binarybros.brobook.service.mapper;

/*
 * @author Mart Stukje
 * */

import nl.miwnn.ch19.binarybros.brobook.dto.BaseUserFormDTO;
import nl.miwnn.ch19.binarybros.brobook.dto.UserAccountFormDTO;
import nl.miwnn.ch19.binarybros.brobook.dto.UserInfoFormDTO;
import nl.miwnn.ch19.binarybros.brobook.model.BroBookUser;
import nl.miwnn.ch19.binarybros.brobook.model.Cohort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BroBookUserMapper {

    public BroBookUser toBroBookUser(UserAccountFormDTO dto, BroBookUser existing) {
        existing.setUsername(dto.getUsername());
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setRole(dto.getRole());
        existing.setBirthDate(dto.getBirthDate());
        existing.setFutureEmployer(dto.getFutureEmployer());
        return existing;
    }

    public BroBookUser applyInfoToBroBookUser(UserInfoFormDTO dto, BroBookUser existing) {
        existing.setBio(dto.getBio());
        existing.setResidence(dto.getResidence());
        existing.setFutureEmployer(dto.getFutureEmployer());
        existing.setBirthDate(dto.getBirthDate());
        return existing;
    }

    public UserAccountFormDTO toUserAccountFormDTO(BroBookUser user) {
        UserAccountFormDTO dto = new UserAccountFormDTO();
        setBaseUserFormInfo(dto, user);
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        return dto;
    }

    public UserInfoFormDTO toUserInfoFormDTO(BroBookUser user) {
        UserInfoFormDTO dto = new UserInfoFormDTO();
        setBaseUserFormInfo(dto, user);
        dto.setBio(user.getBio());
        dto.setResidence(user.getResidence());
        return dto;
    }

    private void setBaseUserFormInfo(BaseUserFormDTO dto, BroBookUser user) {
        dto.setId(user.getId());
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
