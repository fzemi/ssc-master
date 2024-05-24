package com.fzemi.sscmaster.user.dto;

import com.fzemi.sscmaster.common.Mapper;
import com.fzemi.sscmaster.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserMapperImpl implements Mapper<User, UserRequest> {

    @Override
    public UserRequest mapToDTO(User user) {
        return UserRequest.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .organizationUnit(user.getOrganizationUnit())
                .team(user.getTeam())
                .experienceLevel(user.getExperienceLevel())
                .build();
    }

    @Override
    public User mapFromDTO(UserRequest userRequest) {
        return User.builder()
                .firstName(userRequest.firstName())
                .lastName(userRequest.lastName())
                .dateOfBirth(userRequest.dateOfBirth())
                .organizationUnit(userRequest.organizationUnit())
                .team(userRequest.team())
                .experienceLevel(userRequest.experienceLevel())
                .tasks(new ArrayList<>())
                .build();
    }
}
