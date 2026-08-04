package com.tiertagger.api.repository;

import com.tiertagger.api.entity.Tier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TierRepository extends JpaRepository<Tier, Long> {

    Optional<Tier> findByKeyIgnoreCase(String key);

    List<Tier> findAllByEnabledTrueOrderByWeightDesc();

    boolean existsByKeyIgnoreCase(String key);
}

