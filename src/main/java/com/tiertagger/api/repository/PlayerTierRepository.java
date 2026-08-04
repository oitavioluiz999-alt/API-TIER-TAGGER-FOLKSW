package com.tiertagger.api.repository;

import com.tiertagger.api.entity.PlayerTier;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerTierRepository extends JpaRepository<PlayerTier, Long> {

    @EntityGraph(attributePaths = {"player", "mode", "tier"})
    Optional<PlayerTier> findByPlayerIdAndModeId(Long playerId, Long modeId);

    @EntityGraph(attributePaths = {"mode", "tier"})
    List<PlayerTier> findByPlayerId(Long playerId);

    @EntityGraph(attributePaths = {"player", "tier"})
    List<PlayerTier> findByModeId(Long modeId);

    /**
     * Leaderboard: lista players de uma modalidade ordenada por tier weight.
     */
    @Query("""
            SELECT pt FROM PlayerTier pt
            JOIN pt.player p
            JOIN pt.tier t
            WHERE pt.mode.id = :modeId AND t.enabled = true
            ORDER BY t.weight DESC, p.name ASC
            """)
    List<PlayerTier> findLeaderboardByMode(@Param("modeId") Long modeId);

    void deleteByPlayerIdAndModeId(Long playerId, Long modeId);
}

