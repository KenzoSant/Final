package com.fatec.smart_parking.user;

import com.fatec.smart_parking.core.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndRole(String email, Role role);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

}
