package com.brigada.is.repository;

import com.brigada.is.domain.Nomination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NominationRepository extends JpaRepository<Nomination, Long> {
}
