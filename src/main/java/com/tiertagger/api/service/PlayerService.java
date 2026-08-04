package com.tiertagger.api.service;

import com.tiertagger.api.dto.PlayerResponse;
import com.tiertagger.api.dto.PlayerTierDto;
import com.tiertagger.api.entity.Player;
import com.tiertagger.api.entity.PlayerTier;
import com.tiertagger.api.exception.BusinessException;
import com.tiertagger.api.repository.PlayerRepository;
import com.tiertagger.api.repository.PlayerTierRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço de jogadores.
 */
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerTierRepository playerTierRepository;

    public PlayerService(PlayerRepository playerRepository, PlayerTierRepository playerTierRepository) {
        this.playerRepository = playerRepository;
        this.playerTierRepository = playerTierRepository;
    }

    @Transactional
    public Player findOrCreate(String name) {
        String normalized = name.trim();
        if (normalized.length() > 16) {
            normalized = normalized.substring(0, 16);
        }
final String finalName = normalized;
        return playerRepository.findByNameIgnoreCase(finalName)
                .orElseGet(() -> {
                    Player p = playerRepository.save(new Player(finalName, null));
                    evictPlayerCache(finalName);
                    return p;
                });
    }

    @Cacheable(cacheNames = "players", key = "#name.toLowerCase()")
    public PlayerResponse getPlayer(String name) {
        Player player = playerRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Jogador não encontrado: " + name));

        List<PlayerTier> tiers = playerTierRepository.findByPlayerId(player.getId());
        List<PlayerTierDto> dtos = tiers.stream()
                .map(pt -> new PlayerTierDto(
                        pt.getMode().getKey(),
                        pt.getMode().getDisplayName(),
                        pt.getTier().getKey(),
                        pt.getTier().getDisplayName(),
                        pt.getTier().getWeight(),
                        pt.getTier().getColor(),
                        pt.getUpdatedAt().toString()))
                .toList();

        return new PlayerResponse(player.getName(), player.getUuid(), dtos);
    }

    @CacheEvict(cacheNames = "players", key = "#name.toLowerCase()")
    public void evictPlayerCache(String name) {
        // método só para anotação de evict
    }

    public List<PlayerResponse> listPlayers(int limit) {
        int safe = Math.min(Math.max(limit, 1), 500);
        return playerRepository.findAll().stream()
                .limit(safe)
                .map(p -> getPlayer(p.getName()))
                .toList();
    }
}
