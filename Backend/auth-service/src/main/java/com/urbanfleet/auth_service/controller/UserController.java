package com.urbanfleet.auth_service.controller;


import com.urbanfleet.auth_service.dto.UserLoginDto;
import com.urbanfleet.auth_service.dto.UserReqDto;
import com.urbanfleet.auth_service.dto.UserResDto;
import com.urbanfleet.auth_service.model.Users;
import com.urbanfleet.auth_service.repository.UserRespository;
import com.urbanfleet.auth_service.service.UserService;
import com.urbanfleet.auth_service.utility.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private UserService userService;
    private UserRespository userRepo;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    public UserController(UserService userService, UserRespository userRepo, PasswordEncoder passwordEncoder , JwtUtil jwtUtil) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResDto> registerUser(@Valid @RequestBody UserReqDto userReqDto){
        UserResDto registeredUser = userService.registerUser(userReqDto);
        if(registeredUser!=null){
            return ResponseEntity.ok(registeredUser);
        }
        return new ResponseEntity<>(registeredUser, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserLoginDto loginDto){

        Users existUser = userRepo.findByEmail(loginDto.getEmail()).orElseThrow(()-> new RuntimeException("User not found for the given email"));

        if(!passwordEncoder.matches(loginDto.getPasswordHash() ,existUser.getPasswordHash())){
            throw new RuntimeException("Invalid Credentials");
        }

        String token = jwtUtil.generateToken(existUser);
        return new ResponseEntity<>("Token : "+ token , HttpStatus.ACCEPTED);
    }


}
