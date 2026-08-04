-- ============================================================
-- TierTagger API - V3: Índices adicionais para performance
-- ============================================================

-- Leaderboard por modo + tier
CREATE INDEX idx_pt_mode_tier ON player_tiers(mode_id, tier_id);

-- Busca de histórico por jogador e data
CREATE INDEX idx_history_player_created ON history(player_id, created_at DESC);

-- Busca de jogador pelo nome (case-insensitive)
CREATE UNIQUE INDEX idx_players_name_lower ON players(LOWER(name));

-- Buscas rápidas de metadata ativa
CREATE INDEX idx_modes_enabled_sort ON modes(enabled, sort_order);
CREATE INDEX idx_tiers_enabled_weight ON tiers(enabled, weight DESC);

