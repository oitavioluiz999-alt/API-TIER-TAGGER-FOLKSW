package com.tiertagger.api.service;

import com.tiertagger.api.dto.ModeResponse;
import com.tiertagger.api.dto.TierResponse;
import com.tiertagger.api.entity.Mode;
import com.tiertagger.api.entity.Tier;
import com.tiertagger.api.repository.ModeRepository;
import com.tiertagger.api.repository.TierRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço de metadados (modalidades e tiers).
 */
@Service
public class MetadataService {

    private final ModeRepository modeRepository;
    private final TierRepository tierRepository;

    public MetadataService(ModeRepository modeRepository, TierRepository tierRepository) {
        this.modeRepository = modeRepository;
        this.tierRepository = tierRepository;
    }

    @Cacheable(cacheNames = "metadata", key = "'modes'")
    public List<ModeResponse> getModes() {
        return modeRepository.findAllByEnabledTrueOrderBySortOrderAsc().stream()
                .map(this::toModeDto)
                .toList();
    }

    @Cacheable(cacheNames = "metadata", key = "'tiers'")
    public List<TierResponse> getTiers() {
        return tierRepository.findAllByEnabledTrueOrderByWeightDesc().stream()
                .map(this::toTierDto)
                .toList();
    }

    private ModeResponse toModeDto(Mode m) {
        return new ModeResponse(m.getKey(), m.getDisplayName(), m.getIcon(),
                m.getSortOrder(), m.getEnabled());
    }

    private TierResponse toTierDto(Tier t) {
        return new TierResponse(t.getKey(), t.getDisplayName(), t.getWeight(),
                t.getIcon(), t.getColor(), t.getEnabled());
    }
}
