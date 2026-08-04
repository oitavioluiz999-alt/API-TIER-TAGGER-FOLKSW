package com.tiertagger.api.service;

import com.tiertagger.api.dto.LeaderboardEntry;
import com.tiertagger.api.dto.LeaderboardResponse;
import com.tiertagger.api.entity.Mode;
import com.tiertagger.api.entity.PlayerTier;
import com.tiertagger.api.exception.BusinessException;
import com.tiertagger.api.repository.ModeRepository;
import com.tiertagger.api.repository.PlayerTierRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço de leaderboard.
 */
@Service
public class LeaderboardService {

    private final PlayerTierRepository playerTierRepository;
    private final ModeRepository modeRepository;

    public LeaderboardService(PlayerTierRepository playerTierRepository, ModeRepository modeRepository) {
        this.playerTierRepository = playerTierRepository;
        this.modeRepository = modeRepository;
    }

    @Cacheable(cacheNames = "leaderboards", key = "#mode.toLowerCase()")
    public LeaderboardResponse getLeaderboard(String mode) {
        Mode m = modeRepository.findByKeyIgnoreCase(mode)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Modalidade não encontrada: " + mode));

        List<PlayerTier> entries = playerTierRepository.findLeaderboardByMode(m.getId());
        List<LeaderboardEntry> players = entries.stream()
                .map(pt -> new LeaderboardEntry(
                        pt.getPlayer().getName(),
                        pt.getTier().getKey(),
                        pt.getTier().getDisplayName(),
                        pt.getTier().getWeight(),
                        pt.getTier().getColor()))
                .toList();

        return new LeaderboardResponse(m.getKey(), players.size(), players);
    }
}
