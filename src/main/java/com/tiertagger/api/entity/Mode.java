package com.tiertagger.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Modalidade do jogo (Crystal, Sword, Mace, Axe, SMP, Pot, ...).
 */
@Entity
@Table(name = "modes", indexes = {
        @Index(name = "idx_modes_key", columnList = "mode_key", unique = true)
})
public class Mode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mode_key", nullable = false, unique = true, length = 50)
    private String key;

    @Column(name = "display_name", nullable = false, length = 50)
    private String displayName;

    @Column(name = "icon", length = 100)
    private String icon;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "mode")
    private List<PlayerTier> playerTiers = new ArrayList<>();

    @OneToMany(mappedBy = "mode")
    private List<HistoryEntry> history = new ArrayList<>();

    public Mode() {
    }

    public Mode(String key, String displayName, String icon, Integer sortOrder) {
        this.key = key;
        this.displayName = displayName;
        this.icon = icon;
        this.sortOrder = sortOrder;
        this.enabled = true;
    }

    @jakarta.persistence.PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
        if (this.enabled == null) {
            this.enabled = true;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<PlayerTier> getPlayerTiers() {
        return playerTiers;
    }

    public void setPlayerTiers(List<PlayerTier> playerTiers) {
        this.playerTiers = playerTiers;
    }

    public List<HistoryEntry> getHistory() {
        return history;
    }

    public void setHistory(List<HistoryEntry> history) {
        this.history = history;
    }
}

