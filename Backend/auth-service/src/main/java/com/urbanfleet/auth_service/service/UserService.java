package com.urbanfleet.auth_service.service;


import com.urbanfleet.auth_service.constants.Roles;
import com.urbanfleet.auth_service.dto.UserReqDto;
import com.urbanfleet.auth_service.dto.UserResDto;
import com.urbanfleet.auth_service.model.Users;
import com.urbanfleet.auth_service.repository.UserRespository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class UserService {

    private UserRespository userRepo;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRespository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResDto registerUser(UserReqDto userReqDto){
        System.out.println(userReqDto.getPasswordHash());
        return usersToUserRes(userRepo.save(userReqDtoToUsers(userReqDto)));
    }

    public Users userReqDtoToUsers(UserReqDto userReqDto){
        Users user = new Users();
        user.setId(UUID.randomUUID());
        user.setName(userReqDto.getName());
        user.setEmail(userReqDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(userReqDto.getPasswordHash()));
        user.setRole(Roles.USER);
        user.setCreatedDate(LocalDate.now());
        user.setUpdatedDate(LocalDate.now());

        return user;
    }

    public UserResDto usersToUserRes(Users user){
        UserResDto userRes = new UserResDto();
        userRes.setName(user.getName());
        userRes.setRole(user.getRole());
        userRes.setEmail(user.getEmail());
        userRes.setUpdatedDate(user.getUpdatedDate());
        return userRes;
    }
}
