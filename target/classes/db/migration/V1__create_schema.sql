-- ============================================================
-- TierTagger API - V1: Schema inicial
-- ============================================================

CREATE TABLE players (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(16) NOT NULL UNIQUE,
    uuid        VARCHAR(36),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_players_name ON players(name);

CREATE TABLE modes (
    id           BIGSERIAL PRIMARY KEY,
    mode_key     VARCHAR(50) NOT NULL UNIQUE,
    display_name VARCHAR(50) NOT NULL,
    icon         VARCHAR(100),
    sort_order   INTEGER NOT NULL DEFAULT 0,
    enabled      BOOLEAN NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_modes_key ON modes(mode_key);

CREATE TABLE tiers (
    id           BIGSERIAL PRIMARY KEY,
    tier_key     VARCHAR(20) NOT NULL UNIQUE,
    weight       INTEGER NOT NULL,
    display_name VARCHAR(20) NOT NULL,
    icon         VARCHAR(100),
    color        VARCHAR(20),
    enabled      BOOLEAN NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_tiers_key ON tiers(tier_key);

CREATE TABLE player_tiers (
    id          BIGSERIAL PRIMARY KEY,
    player_id   BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    mode_id     BIGINT NOT NULL REFERENCES modes(id) ON DELETE CASCADE,
    tier_id     BIGINT NOT NULL REFERENCES tiers(id) ON DELETE RESTRICT,
    assigned_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
    assigned_by VARCHAR(50),
    CONSTRAINT uq_player_mode UNIQUE (player_id, mode_id)
);

CREATE INDEX idx_pt_player ON player_tiers(player_id);
CREATE INDEX idx_pt_mode ON player_tiers(mode_id);
CREATE INDEX idx_pt_tier ON player_tiers(tier_id);

CREATE TABLE history (
    id              BIGSERIAL PRIMARY KEY,
    player_id       BIGINT NOT NULL REFERENCES players(id) ON DELETE CASCADE,
    mode_id         BIGINT NOT NULL REFERENCES modes(id) ON DELETE CASCADE,
    old_tier_key    VARCHAR(20),
    new_tier_key    VARCHAR(20),
    old_tier_weight INTEGER,
    new_tier_weight INTEGER,
    action          VARCHAR(20) NOT NULL,
    staff_name      VARCHAR(50),
    reason          VARCHAR(500),
    ip_address      VARCHAR(100),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_history_player ON history(player_id);
CREATE INDEX idx_history_created ON history(created_at);
CREATE INDEX idx_history_mode ON history(mode_id);

CREATE TABLE app_users (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    display_name  VARCHAR(50),
    role          VARCHAR(20) NOT NULL,
    enabled       BOOLEAN NOT NULL DEFAULT TRUE,
    last_login_at TIMESTAMPTZ,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_users_username ON app_users(username);

CREATE TABLE api_keys (
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    api_key      VARCHAR(128) NOT NULL UNIQUE,
    role         VARCHAR(20) NOT NULL,
    enabled      BOOLEAN NOT NULL DEFAULT TRUE,
    last_used_at TIMESTAMPTZ,
    expires_at   TIMESTAMPTZ,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_apikeys_key ON api_keys(api_key);

CREATE TABLE sessions (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT NOT NULL REFERENCES app_users(id) ON DELETE CASCADE,
    refresh_token VARCHAR(512) NOT NULL UNIQUE,
    expires_at    TIMESTAMPTZ NOT NULL,
    ip_address    VARCHAR(100),
    user_agent    VARCHAR(500),
    revoked       BOOLEAN NOT NULL DEFAULT FALSE,
    created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_sessions_token ON sessions(refresh_token);
CREATE INDEX idx_sessions_user ON sessions(user_id);
CREATE INDEX idx_sessions_expires ON sessions(expires_at);

