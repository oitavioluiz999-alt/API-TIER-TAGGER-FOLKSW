package com.tiertagger.api.service;

import com.tiertagger.api.dto.TierRemoveRequest;
import com.tiertagger.api.dto.TierSetRequest;
import com.tiertagger.api.entity.HistoryEntry;
import com.tiertagger.api.entity.Mode;
import com.tiertagger.api.entity.Player;
import com.tiertagger.api.entity.PlayerTier;
import com.tiertagger.api.entity.Tier;
import com.tiertagger.api.exception.BusinessException;
import com.tiertagger.api.repository.HistoryRepository;
import com.tiertagger.api.repository.ModeRepository;
import com.tiertagger.api.repository.PlayerTierRepository;
import com.tiertagger.api.repository.TierRepository;
import com.tiertagger.api.websocket.WebSocketBroadcastService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

/**
 * Serviço que define/remove tiers e dispara o fluxo:
 * Salvar banco -> Atualizar cache -> Broadcast WebSocket.
 */
@Service
public class TierService {

    private static final Logger log = LoggerFactory.getLogger(TierService.class);

    private final PlayerService playerService;
    private final ModeRepository modeRepository;
    private final TierRepository tierRepository;
    private final PlayerTierRepository playerTierRepository;
    private final HistoryRepository historyRepository;
    private final WebSocketBroadcastService broadcastService;
    private final CacheService cacheService;

    public TierService(PlayerService playerService,
                       ModeRepository modeRepository,
                       TierRepository tierRepository,
                       PlayerTierRepository playerTierRepository,
                       HistoryRepository historyRepository,
                       WebSocketBroadcastService broadcastService,
                       CacheService cacheService) {
        this.playerService = playerService;
        this.modeRepository = modeRepository;
        this.tierRepository = tierRepository;
        this.playerTierRepository = playerTierRepository;
        this.historyRepository = historyRepository;
        this.broadcastService = broadcastService;
        this.cacheService = cacheService;
    }

    @Transactional
    public void setTier(TierSetRequest request, String staff, String ip) {
        Player player = playerService.findOrCreate(request.player());
        Mode mode = modeRepository.findByKeyIgnoreCase(request.mode())
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Modalidade inválida: " + request.mode()));
        Tier newTier = tierRepository.findByKeyIgnoreCase(request.tier())
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Tier inválido: " + request.tier()));

        PlayerTier existing = playerTierRepository.findByPlayerIdAndModeId(player.getId(), mode.getId())
                .orElse(null);

        String oldTierKey = existing != null ? existing.getTier().getKey() : null;
        Integer oldTierWeight = existing != null ? existing.getTier().getWeight() : null;

        if (existing != null) {
            existing.setTier(newTier);
            existing.setAssignedBy(staff);
            existing.setUpdatedAt(OffsetDateTime.now());
            playerTierRepository.save(existing);
        } else {
            PlayerTier newPt = new PlayerTier(player, mode, newTier, staff);
            playerTierRepository.save(newPt);
        }

        saveHistory(player, mode, "SET", oldTierKey, newTier.getKey(),
                oldTierWeight, newTier.getWeight(), staff, request.reason(), ip);

        // Atualizar cache
        playerService.evictPlayerCache(player.getName());
        cacheService.evictLeaderboard(mode.getKey());

        // Broadcast para todos os clientes
        broadcastService.broadcastTierUpdate("SET", player.getName(), mode.getKey(),
                oldTierKey, newTier.getKey(), staff);

        log.info("Tier set {} -> {} em {} por {} (IP {})", player.getName(), newTier.getKey(),
                mode.getKey(), staff, ip);
    }

    @Transactional
    public void removeTier(TierRemoveRequest request, String staff, String ip) {
        Player player = playerService.findOrCreate(request.player());
        Mode mode = modeRepository.findByKeyIgnoreCase(request.mode())
                .orElseThrow(() -> new BusinessException(HttpStatus.BAD_REQUEST, "Modalidade inválida: " + request.mode()));

        PlayerTier existing = playerTierRepository.findByPlayerIdAndModeId(player.getId(), mode.getId())
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,
                        "Jogador não possui tier em " + mode.getKey()));

        String oldTierKey = existing.getTier().getKey();
        Integer oldTierWeight = existing.getTier().getWeight();

        saveHistory(player, mode, "REMOVE", oldTierKey, null,
                oldTierWeight, null, staff, request.reason(), ip);

        playerTierRepository.deleteByPlayerIdAndModeId(player.getId(), mode.getId());

        playerService.evictPlayerCache(player.getName());
        cacheService.evictLeaderboard(mode.getKey());

        broadcastService.broadcastTierUpdate("REMOVE", player.getName(), mode.getKey(),
                oldTierKey, null, staff);

        log.info("Tier removido {} de {} por {} (IP {})", player.getName(), mode.getKey(), staff, ip);
    }

    private void saveHistory(Player player, Mode mode, String action,
                             String oldKey, String newKey,
                             Integer oldWeight, Integer newWeight,
                             String staff, String reason, String ip) {
        HistoryEntry entry = new HistoryEntry();
        entry.setPlayer(player);
        entry.setMode(mode);
        entry.setAction(action);
        entry.setOldTierKey(oldKey);
        entry.setNewTierKey(newKey);
        entry.setOldTierWeight(oldWeight);
        entry.setNewTierWeight(newWeight);
        entry.setStaffName(staff);
        entry.setReason(reason);
        entry.setIpAddress(ip);
        historyRepository.save(entry);
    }
}
