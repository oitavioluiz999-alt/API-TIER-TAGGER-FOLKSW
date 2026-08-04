package com.tiertagger.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.OffsetDateTime;

/**
 * Associação entre Player, Mode e Tier (tier atual do jogador em uma modalidade).
 */
@Entity
@Table(name = "player_tiers", uniqueConstraints = {
        @UniqueConstraint(name = "uq_player_mode", columnNames = {"player_id", "mode_id"})
}, indexes = {
        @Index(name = "idx_pt_player", columnList = "player_id"),
        @Index(name = "idx_pt_mode", columnList = "mode_id"),
        @Index(name = "idx_pt_tier", columnList = "tier_id")
})
public class PlayerTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mode_id", nullable = false)
    private Mode mode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier_id", nullable = false)
    private Tier tier;

    @Column(name = "assigned_at", nullable = false)
    private OffsetDateTime assignedAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "assigned_by", length = 50)
    private String assignedBy;

    public PlayerTier() {
    }

    public PlayerTier(Player player, Mode mode, Tier tier, String assignedBy) {
        this.player = player;
        this.mode = mode;
        this.tier = tier;
        this.assignedBy = assignedBy;
        OffsetDateTime now = OffsetDateTime.now();
        this.assignedAt = now;
        this.updatedAt = now;
    }

    @jakarta.persistence.PrePersist
    public void prePersist() {
        OffsetDateTime now = OffsetDateTime.now();
        this.assignedAt = now;
        this.updatedAt = now;
    }

    @jakarta.persistence.PreUpdate
    public void preUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Tier getTier() {
        return tier;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public OffsetDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(OffsetDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }
}

