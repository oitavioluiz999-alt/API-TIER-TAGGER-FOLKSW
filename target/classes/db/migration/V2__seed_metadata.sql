-- ============================================================
-- TierTagger API - V2: Seed de modalidades e tiers
-- ============================================================

-- Modalidades (15)
INSERT INTO modes (mode_key, display_name, icon, sort_order, enabled) VALUES
  ('crystal',    'Crystal',    'textures/modes/crystal.png',    1,  TRUE),
  ('sword',      'Sword',      'textures/modes/sword.png',      2,  TRUE),
  ('mace',       'Mace',       'textures/modes/mace.png',       3,  TRUE),
  ('axe',        'Axe',        'textures/modes/axe.png',        4,  TRUE),
  ('smp',        'SMP',        'textures/modes/smp.png',        5,  TRUE),
  ('pot',        'Pot',        'textures/modes/pot.png',        6,  TRUE),
  ('nethpot',    'NethPot',    'textures/modes/nethpot.png',    7,  TRUE),
  ('uhc',        'UHC',        'textures/modes/uhc.png',        8,  TRUE),
  ('shield',     'Shield',     'textures/modes/shield.png',     9,  TRUE),
  ('trident',    'Trident',    'textures/modes/trident.png',    10, TRUE),
  ('elytra',     'Elytra',     'textures/modes/elytra.png',     11, TRUE),
  ('vanilla',    'Vanilla',    'textures/modes/vanilla.png',    12, TRUE),
  ('netheriteop','NetheriteOP','textures/modes/netheriteop.png',13, TRUE),
  ('custom',     'Custom',     'textures/modes/custom.png',     14, TRUE);

-- Tiers (10)
INSERT INTO tiers (tier_key, weight, display_name, icon, color, enabled) VALUES
  ('HT5', 1000, 'HT5', 'textures/tiers/ht5.png', '#FF5555',   TRUE),
  ('HT4',  900, 'HT4', 'textures/tiers/ht4.png', '#FF8C42',   TRUE),
  ('HT3',  800, 'HT3', 'textures/tiers/ht3.png', '#FFD700',   TRUE),
  ('HT2',  700, 'HT2', 'textures/tiers/ht2.png', '#A8FF60',   TRUE),
  ('HT1',  600, 'HT1', 'textures/tiers/ht1.png', '#57DB39',   TRUE),
  ('LT1',  500, 'LT1', 'textures/tiers/lt1.png', '#55B8FF',   TRUE),
  ('LT2',  400, 'LT2', 'textures/tiers/lt2.png', '#3E6CFF',   TRUE),
  ('LT3',  300, 'LT3', 'textures/tiers/lt3.png', '#B45AFF',   TRUE),
  ('LT4',  200, 'LT4', 'textures/tiers/lt4.png', '#FF55B8',   TRUE),
  ('LT5',  100, 'LT5', 'textures/tiers/lt5.png', '#AAAAAA',   TRUE);

