package com.brigada.is.audit.repository;

import com.brigada.is.audit.entity.MusicBandLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicBandLogRepository extends JpaRepository<MusicBandLog, Long> {
}
