package com.brigada.is.repository;

import com.brigada.is.domain.MusicBand;
import com.brigada.is.domain.Studio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MusicBandRepository extends JpaRepository<MusicBand, Long> {
    Long countByStudio(Studio studio);

    List<MusicBand> findAllByAlbumsCountGreaterThan(Long albumsCount);
    List<MusicBand> findAllByOrderByIdDesc();

    Optional<MusicBand> findTopByOrderByEstablishmentDateDesc();
}
