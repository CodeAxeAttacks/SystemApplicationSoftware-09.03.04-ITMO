package com.brigada.is.security.repository;

import com.brigada.is.security.entity.Role;
import com.brigada.is.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByPassword(String encodedPassword);

    @Query("SELECT COUNT(u) > 0 FROM User u JOIN u.roles r WHERE r = :role")
    boolean existsUserWithRole(@Param("role") Role role);
}
