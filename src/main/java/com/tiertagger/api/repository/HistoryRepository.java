package com.tiertagger.api.repository;

import com.tiertagger.api.entity.HistoryEntry;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntry, Long> {

    @EntityGraph(attributePaths = {"player", "mode"})
    List<HistoryEntry> findByPlayerIdOrderByCreatedAtDesc(Long playerId, Pageable pageable);
}

