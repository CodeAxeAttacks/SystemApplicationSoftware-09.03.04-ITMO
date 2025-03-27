package com.brigada.is.security.repository;

import com.brigada.is.security.entity.AdminApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminApplicationRepository extends JpaRepository<AdminApplication, Long> {
}
