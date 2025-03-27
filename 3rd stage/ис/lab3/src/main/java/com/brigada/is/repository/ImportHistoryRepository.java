package com.brigada.is.repository;

import com.brigada.is.domain.ImportHistory;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImportHistoryRepository extends JpaRepository<ImportHistory, Long> {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @NotNull ImportHistory save(@NotNull ImportHistory importHistory);
    List<ImportHistory> findAllByUser_Username(String username);
}
