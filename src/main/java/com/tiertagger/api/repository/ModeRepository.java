package com.tiertagger.api.repository;

import com.tiertagger.api.entity.Mode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModeRepository extends JpaRepository<Mode, Long> {

    Optional<Mode> findByKeyIgnoreCase(String key);

    List<Mode> findAllByEnabledTrueOrderBySortOrderAsc();

    boolean existsByKeyIgnoreCase(String key);
}

