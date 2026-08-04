package com.tiertagger.api.service;

import com.tiertagger.api.dto.HistoryEntryDto;
import com.tiertagger.api.entity.HistoryEntry;
import com.tiertagger.api.entity.Player;
import com.tiertagger.api.exception.BusinessException;
import com.tiertagger.api.repository.HistoryRepository;
import com.tiertagger.api.repository.PlayerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço de histórico de alterações de tier.
 */
@Service
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final PlayerRepository playerRepository;

    public HistoryService(HistoryRepository historyRepository, PlayerRepository playerRepository) {
        this.historyRepository = historyRepository;
        this.playerRepository = playerRepository;
    }

    public List<HistoryEntryDto> getHistory(String playerName, int limit) {
        Player player = playerRepository.findByNameIgnoreCase(playerName)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Jogador não encontrado: " + playerName));

        int safe = Math.min(Math.max(limit, 1), 100);
        return historyRepository.findByPlayerIdOrderByCreatedAtDesc(player.getId(), PageRequest.of(0, safe))
                .stream()
                .map(this::toDto)
                .toList();
    }

    private HistoryEntryDto toDto(HistoryEntry h) {
        return new HistoryEntryDto(
                h.getId(),
                h.getPlayer().getName(),
                h.getMode().getKey(),
                h.getAction(),
                h.getOldTierKey(),
                h.getNewTierKey(),
                h.getStaffName(),
                h.getReason(),
                h.getCreatedAt().toString());
    }
}
