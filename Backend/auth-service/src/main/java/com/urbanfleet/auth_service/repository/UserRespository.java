package com.urbanfleet.auth_service.repository;

import com.urbanfleet.auth_service.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRespository extends JpaRepository<Users, UUID> {
    Optional<Users> findByEmail(String email);
}
