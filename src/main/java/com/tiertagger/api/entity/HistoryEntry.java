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

import java.time.OffsetDateTime;

/**
 * Registro de histórico de alteração de tier. Logs: jogador, staff, modalidade,
 * tier antigo, tier novo, motivo, IP, data/hora.
 */
@Entity
@Table(name = "history", indexes = {
        @Index(name = "idx_history_player", columnList = "player_id"),
        @Index(name = "idx_history_created", columnList = "created_at"),
        @Index(name = "idx_history_mode", columnList = "mode_id")
})
public class HistoryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mode_id", nullable = false)
    private Mode mode;

    @Column(name = "old_tier_key", length = 20)
    private String oldTierKey;

    @Column(name = "new_tier_key", length = 20)
    private String newTierKey;

    @Column(name = "old_tier_weight")
    private Integer oldTierWeight;

    @Column(name = "new_tier_weight")
    private Integer newTierWeight;

    @Column(name = "action", nullable = false, length = 20)
    private String action;

    @Column(name = "staff_name", length = 50)
    private String staffName;

    @Column(name = "reason", length = 500)
    private String reason;

    @Column(name = "ip_address", length = 100)
    private String ipAddress;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public HistoryEntry() {
    }

    @jakarta.persistence.PrePersist
    public void prePersist() {
        this.createdAt = OffsetDateTime.now();
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

    public String getOldTierKey() {
        return oldTierKey;
    }

    public void setOldTierKey(String oldTierKey) {
        this.oldTierKey = oldTierKey;
    }

    public String getNewTierKey() {
        return newTierKey;
    }

    public void setNewTierKey(String newTierKey) {
        this.newTierKey = newTierKey;
    }

    public Integer getOldTierWeight() {
        return oldTierWeight;
    }

    public void setOldTierWeight(Integer oldTierWeight) {
        this.oldTierWeight = oldTierWeight;
    }

    public Integer getNewTierWeight() {
        return newTierWeight;
    }

    public void setNewTierWeight(Integer newTierWeight) {
        this.newTierWeight = newTierWeight;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

