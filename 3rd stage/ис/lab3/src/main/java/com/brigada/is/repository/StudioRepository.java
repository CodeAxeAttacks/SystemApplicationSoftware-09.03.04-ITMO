package com.brigada.is.repository;

import com.brigada.is.domain.Studio;
import com.brigada.is.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudioRepository extends JpaRepository<Studio, Long> {
    List<Studio> findAllByCreatedBy(User user);
}
