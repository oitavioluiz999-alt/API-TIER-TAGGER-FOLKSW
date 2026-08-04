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
 * Nível/rank de habilidade (HT1-Ht5, LT1-LT5, custom).
 */
@Entity
@Table(name = "tiers", indexes = {
        @Index(name = "idx_tiers_key", columnList = "tier_key", unique = true)
})
public class Tier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tier_key", nullable = false, unique = true, length = 20)
    private String key;

    /**
     * Valor numérico usado para ordenação (HT5 = maior, LT5 = menor).
     */
    @Column(name = "weight", nullable = false)
    private Integer weight;

    @Column(name = "display_name", nullable = false, length = 20)
    private String displayName;

    @Column(name = "icon", length = 100)
    private String icon;

    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "tier")
    private List<PlayerTier> playerTiers = new ArrayList<>();

    public Tier() {
    }

    public Tier(String key, Integer weight, String displayName, String icon, String color) {
        this.key = key;
        this.weight = weight;
        this.displayName = displayName;
        this.icon = icon;
        this.color = color;
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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
}

