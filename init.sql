-- =====================================================
-- BANCO DE DADOS MERURU TCG - POC SIMPLES
-- =====================================================

DROP DATABASE rtr;
CREATE DATABASE IF NOT EXISTS rtr CHARACTER SET = 'utf8mb4' COLLATE = 'utf8mb4_unicode_ci';
USE rtr;

-- =====================================================
-- MERURU TCG DATABASE - SIMPLE POC
-- =====================================================

-- -----------------------------------------------------
-- TABLE: permission
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS permission (
  id CHAR(36) PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE,
  description VARCHAR(255) NOT NULL,
  created_by CHAR(36) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_by CHAR(36) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- TABLE: user_role
-- -----------------------------------------------------
DROP TABLE IF EXISTS user_role;
CREATE TABLE IF NOT EXISTS user_role (
  id CHAR(3) PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE,
  description VARCHAR(100) NOT NULL,
  created_by CHAR(36) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_by CHAR(36) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- TABELA: STORE
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS store (
  id CHAR(36) PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  cep CHAR(8) NOT NULL,	
  number VARCHAR(10) NOT NULL,
  complement VARCHAR(100) NULL,
  created_by CHAR(36) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_by CHAR(36) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- TABLE: role_permission
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS role_permission (
  role_id CHAR(3) NOT NULL,
  permission_id CHAR(36) NOT NULL,
  created_by CHAR(36) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_by CHAR(36) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (role_id, permission_id),
  FOREIGN KEY (role_id) REFERENCES user_role(id),
  FOREIGN KEY (permission_id) REFERENCES permission(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- TABLE: user
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS user (
  id CHAR(36) PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  email VARCHAR(200) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role_id CHAR(3) NOT NULL,
  store_id CHAR(36) NULL,
  created_by CHAR(36) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_by CHAR(36) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (role_id) REFERENCES user_role(id),
  FOREIGN KEY (store_id) REFERENCES store(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- TABLE: collection
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS collection (
  id CHAR(36) PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  abbreviation VARCHAR(50),
  release_date DATE,
  generation VARCHAR(50) NULL,
  created_by CHAR(36) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_by CHAR(36) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- TABLE: card
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS card (
  id CHAR(36) PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  season VARCHAR(50),
  pokemon_type ENUM(
	'GRASS','FIRE','WATER','LIGHTNING','PSYCHIC','FIGHTING',
    'DARKNESS','METAL','FAIRY','DRAGON','COLORLESS'
  ) NULL,
  collection_id CHAR(36) NOT NULL,
  code VARCHAR(10) NOT NULL,
  rarity ENUM(
    'COMMON','UNCOMMON','RARE','RARE_HOLO','RARE_REVERSE_HOLO','RARE_HOLO_EX',
    'RARE_HOLO_GX','RARE_HOLO_V','RARE_HOLO_VMAX','RARE_HOLO_VSTAR',
    'RARE_PRIME','RARE_LEGEND','RARE_BREAK','RARE_ULTRA','RARE_SECRET',
    'RARE_PROMO'
  ) NOT NULL DEFAULT 'COMMON',
  nationality VARCHAR(3) NULL,
  created_by CHAR(36) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_by CHAR(36) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (collection_id) REFERENCES collection(id),
  UNIQUE (code, collection_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- TABLE: other_product
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS other_product (
  id CHAR(36) PRIMARY KEY,
  type ENUM('BOOSTER_BOX','ACCESSORY','OTHER') NOT NULL,
  nationality VARCHAR(50) NULL,
  package_contents VARCHAR(255) NULL,
  extra_info VARCHAR(255) NULL,
  created_by CHAR(36) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_by CHAR(36) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- TABLE: product
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS product (
  id CHAR(36) PRIMARY KEY,
  name VARCHAR(200) NOT NULL,
  description VARCHAR(500),
  card_id CHAR(36) NULL,
  other_product_id CHAR(36) NULL,
  store_id CHAR(36) NULL,
  price DECIMAL(10,2) NOT NULL DEFAULT 0,
  product_condition ENUM(
    'MINT','LIGHTLY_PLAYED','MODERATELY_PLAYED','HEAVILY_PLAYED',
    'DAMAGED','SEALED','OPENED','USED'
  ) NOT NULL DEFAULT 'MINT',
  created_by CHAR(36) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_by CHAR(36) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (card_id) REFERENCES card(id),
  FOREIGN KEY (other_product_id) REFERENCES other_product(id),
  FOREIGN KEY (store_id) REFERENCES store(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- TABLE: inventory
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventory (
  product_id CHAR(36) PRIMARY KEY,
  quantity INT NOT NULL DEFAULT 0,
  created_by CHAR(36) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_by CHAR(36) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (product_id) REFERENCES product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- TABLE: inventory_movement
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventory_movement (
  id CHAR(36) PRIMARY KEY,
  product_id CHAR(36) NOT NULL,
  user_id CHAR(36) NOT NULL,
  quantity INT NOT NULL,
  unit_purchase_price DECIMAL(10,2) NULL, 
  unit_sale_price DECIMAL(10,2) NULL,
  type ENUM('IN','OUT','ADJUST') NOT NULL,
  description VARCHAR(255) NULL,
  created_by CHAR(36) NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_by CHAR(36) NULL,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  deleted BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (product_id) REFERENCES product(id),
  FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- TABLE: inventory_audit
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS inventory_audit (
    id CHAR(36) PRIMARY KEY,
    product_id CHAR(36) NOT NULL,
    movement_id CHAR(36) NOT NULL,
    user_id CHAR(36) NOT NULL,
    movement_type VARCHAR(50) NOT NULL,
    operation ENUM('CREATE','UPDATE','DELETE') NOT NULL,
    quantity INT NOT NULL,
    quantity_before INT NOT NULL,
    quantity_after INT NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(500) NULL,
    error_message VARCHAR(500) NULL,
    status ENUM('PROCESSED','FAILED','PENDING') NOT NULL DEFAULT 'PROCESSED',
    CONSTRAINT fk_inventory_audit_product FOREIGN KEY (product_id) REFERENCES product(id),
    CONSTRAINT fk_inventory_audit_movement FOREIGN KEY (movement_id) REFERENCES inventory_movement(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------
-- TABELA: user_session
-- ----------------------------------------------------
CREATE TABLE IF NOT EXISTS user_session (
  id CHAR(36) PRIMARY KEY,
  user_id CHAR(36) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  ended_at TIMESTAMP NULL,
  active BOOLEAN DEFAULT TRUE,
  FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- =====================================================
-- INSERTS INICIAIS
-- =====================================================

-- ROLES
INSERT INTO user_role (id, name, description) VALUES
('001', 'admin', 'Acesso total ao sistema'),
('002', 'manager', 'Gerencia estoque e relatórios'),
('003', 'staff', 'Cadastro de cartas e vendas');

update user_role set deleted = false where id IN ('001','002','003');

-- PERMISSIONS
INSERT INTO permission (id, name, description) VALUES
(UUID(), 'card.create', 'Cadastrar novas cartas'),
(UUID(), 'card.update', 'Editar cartas existentes'),
(UUID(), 'card.delete', 'Remover cartas'),
(UUID(), 'inventory.view', 'Visualizar estoque'),
(UUID(), 'inventory.update', 'Alterar quantidades do estoque'),
(UUID(), 'pricing.update', 'Atualizar precificação'),
(UUID(), 'report.view', 'Visualizar dashboards e relatórios'),
(UUID(), 'user.manage', 'Gerenciar usuários e cargos'),
(UUID(), 'order.create', 'Realizar compras'),
(UUID(), 'order.view', 'Visualizar histórico de pedidos');
-- ROLE_PERMISSIONS
INSERT INTO role_permission (role_id, permission_id)
SELECT '001', id FROM permission;

INSERT INTO role_permission (role_id, permission_id)
SELECT '002', id FROM permission WHERE name IN (
  'card.create','card.update','inventory.view','inventory.update',
  'pricing.update','report.view','order.view'
);

INSERT INTO role_permission (role_id, permission_id)
SELECT '003', id FROM permission WHERE name IN (
  'card.create','card.update','inventory.view','order.create'
);

-- USERS AND STORE
SET @admin = UUID();
SET @manager = UUID();
SET @staff = UUID();
SET @store_id = UUID();

INSERT INTO store (id, name, cep, number, complement, created_by)
VALUES (@store_id, 'Meruru TCG Store', '01310940', '25', 'Loja 01', @manager),
('5efbc0f7-fda8-442b-bf22-feb205663bb4', 'Meruru TCG Store Curitiba', '02410840', '25', 'Loja 02', @manager);

INSERT INTO user (id, name, email, password_hash, role_id, store_id, created_by)
VALUES
(@admin, 'Administrador', 'admin@meruru.com', '$2a$12$nOljEmh0qzaubb8hwtWe8O0L0cQvB5XzHunJ2og7Itae5TRD/UzzO', '001', @store_id, @manager),
(@manager, 'Gerente', 'manager@meruru.com', '$2a$12$nOljEmh0qzaubb8hwtWe8O0L0cQvB5XzHunJ2og7Itae5TRD/UzzO', '002', @store_id, @manager),
(@staff, 'Funcionário', 'staff@meruru.com', '$2a$12$nOljEmh0qzaubb8hwtWe8O0L0cQvB5XzHunJ2og7Itae5TRD/UzzO', '003', @store_id, @manager);

select * from user;
-- COLLECTIONS
SET @scarlet_violet_id = 'e8f9a0b1-c2d3-4e4f-5a6b-7c8d9e0f1a2b';
SET @scarlet_violet_promo_id = 'aabbccdd-1122-3344-5566-778899aabbcc';

INSERT INTO collection (id, name, abbreviation, release_date, generation, created_by) VALUES
('983ca278-b230-4c92-801b-59d443b0534b', 'Base Set', 'BS', '1999-01-09', 'GEN1', @manager),
('c8f1f35b-8e8c-46cb-9f83-0e4c71b7c984', 'Jungle', 'JU', '1999-06-16', 'GEN1', @manager),
('d5e6f7a8-b9c0-4d1e-2f3a-4b5c6d7e8f9a', 'Fossil', 'FO', '1999-10-10', 'GEN1', @manager),
('e6f7a8b9-c0d1-4e2f-3a4b-5c6d7e8f9a0b', 'Team Rocket', 'TR', '2000-04-24', 'GEN1', @manager),
('f7a8b9c0-d1e2-4f3a-4b5c-6d7e8f9a0b1c', 'Gym Heroes', 'GH', '2000-08-14', 'GEN1', @manager),
('a8b9c0d1-e2f3-4a4b-5c6d-7e8f9a0b1c2d', 'Neo Genesis', 'NG', '2000-12-16', 'GEN2', @manager),
('b9c0d1e2-f3a4-4b5c-6d7e-8f9a0b1c2d3e', 'Neo Discovery', 'ND', '2001-06-01', 'GEN2', @manager),
('c0d1e2f3-a4b5-4c6d-7e8f-9a0b1c2d3e4f', 'EX Ruby & Sapphire', 'RS', '2003-06-18', 'GEN3', @manager),
('d1e2f3a4-b5c6-4d7e-8f9a-0b1c2d3e4f5a', 'EX FireRed & LeafGreen', 'FRLG', '2004-09-01', 'GEN3', @manager),
('e2f3a4b5-c6d7-4e8f-9a0b-1c2d3e4f5a6b', 'Diamond & Pearl', 'DP', '2007-05-01', 'GEN4', @manager),
('f3a4b5c6-d7e8-4f9a-0b1c-2d3e4f5a6b7c', 'Platinum', 'PL', '2009-02-11', 'GEN4', @manager),
('a4b5c6d7-e8f9-4a0b-1c2d-3e4f5a6b7c8d', 'Black & White', 'BW', '2011-04-25', 'GEN5', @manager),
('b5c6d7e8-f9a0-4b1c-2d3e-4f5a6b7c8d9e', 'XY Base Set', 'XY', '2014-02-05', 'GEN6', @manager),
('c6d7e8f9-a0b1-4c2d-3e4f-5a6b7c8d9e0f', 'Sun & Moon', 'SM', '2017-02-03', 'GEN7', @manager),
('d7e8f9a0-b1c2-4d3e-4f5a-6b7c8d9e0f1a', 'Sword & Shield', 'SWSH', '2020-02-07', 'GEN8', @manager),
(@scarlet_violet_id, 'Scarlet & Violet', 'SV1', '2023-03-31', 'GEN9', @manager),
(@scarlet_violet_promo_id, 'Scarlet & Violet promo', 'SVP', '2022-11-18', 'GEN9', @manager);

-- =====================================================
-- CARTAS SV1 E SVP (SCARLET & VIOLET)
-- =====================================================

INSERT INTO card (id, title, season, pokemon_type, collection_id, code, rarity, nationality, created_by) VALUES
(UUID(), 'Pineco', '9', 'GRASS', @scarlet_violet_id, 'SV1-001', 'COMMON', 'EN', @manager),
(UUID(), 'Heracross', '9', 'GRASS', @scarlet_violet_id, 'SV1-002', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Shroomish', '9', 'GRASS', @scarlet_violet_id, 'SV1-003', 'COMMON', 'EN', @manager),
(UUID(), 'Breloom', '9', 'GRASS', @scarlet_violet_id, 'SV1-004', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Cacnea', '9', 'GRASS', @scarlet_violet_id, 'SV1-005', 'COMMON', 'EN', @manager),
(UUID(), 'Cacturne', '9', 'GRASS', @scarlet_violet_id, 'SV1-006', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Tropius', '9', 'GRASS', @scarlet_violet_id, 'SV1-007', 'COMMON', 'EN', @manager),
(UUID(), 'Scatterbug', '9', 'GRASS', @scarlet_violet_id, 'SV1-008', 'COMMON', 'EN', @manager),
(UUID(), 'Spewpa', '9', 'GRASS', @scarlet_violet_id, 'SV1-009', 'COMMON', 'EN', @manager),
(UUID(), 'Vivillon', '9', 'GRASS', @scarlet_violet_id, 'SV1-010', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Skiddo', '9', 'GRASS', @scarlet_violet_id, 'SV1-011', 'COMMON', 'EN', @manager),
(UUID(), 'Gogoat', '9', 'GRASS', @scarlet_violet_id, 'SV1-012', 'COMMON', 'EN', @manager),
(UUID(), 'Sprigatito', '9', 'GRASS', @scarlet_violet_id, 'SV1-013', 'COMMON', 'EN', @manager),
(UUID(), 'Floragato', '9', 'GRASS', @scarlet_violet_id, 'SV1-014', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Meowscarada', '9', 'GRASS', @scarlet_violet_id, 'SV1-015', 'RARE', 'EN', @manager),
(UUID(), 'Tarountula', '9', 'GRASS', @scarlet_violet_id, 'SV1-016', 'COMMON', 'EN', @manager),
(UUID(), 'Tarountula', '9', 'GRASS', @scarlet_violet_id, 'SV1-017', 'COMMON', 'EN', @manager),
(UUID(), 'Tarountula', '9', 'GRASS', @scarlet_violet_id, 'SV1-018', 'COMMON', 'EN', @manager),
(UUID(), 'Spidops ex', '9', 'GRASS', @scarlet_violet_id, 'SV1-019', 'RARE_ULTRA', 'EN', @manager),
(UUID(), 'Smoliv', '9', 'GRASS', @scarlet_violet_id, 'SV1-020', 'COMMON', 'EN', @manager),
(UUID(), 'Smoliv', '9', 'GRASS', @scarlet_violet_id, 'SV1-021', 'COMMON', 'EN', @manager),
(UUID(), 'Dolliv', '9', 'GRASS', @scarlet_violet_id, 'SV1-022', 'COMMON', 'EN', @manager),
(UUID(), 'Arboliva', '9', 'GRASS', @scarlet_violet_id, 'SV1-023', 'RARE', 'EN', @manager),
(UUID(), 'Toedscool', '9', 'GRASS', @scarlet_violet_id, 'SV1-024', 'COMMON', 'EN', @manager),
(UUID(), 'Toedscool', '9', 'GRASS', @scarlet_violet_id, 'SV1-025', 'COMMON', 'EN', @manager),
(UUID(), 'Toedscruel', '9', 'GRASS', @scarlet_violet_id, 'SV1-026', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Capsakid', '9', 'GRASS', @scarlet_violet_id, 'SV1-027', 'COMMON', 'EN', @manager),
(UUID(), 'Capsakid', '9', 'GRASS', @scarlet_violet_id, 'SV1-028', 'COMMON', 'EN', @manager),
(UUID(), 'Scovillain', '9', 'GRASS', @scarlet_violet_id, 'SV1-029', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Growlithe', '9', 'FIRE', @scarlet_violet_id, 'SV1-030', 'COMMON', 'EN', @manager),
(UUID(), 'Growlithe', '9', 'FIRE', @scarlet_violet_id, 'SV1-031', 'COMMON', 'EN', @manager),
(UUID(), 'Arcanine ex', '9', 'FIRE', @scarlet_violet_id, 'SV1-032', 'RARE_ULTRA', 'EN', @manager),
(UUID(), 'Houndour', '9', 'FIRE', @scarlet_violet_id, 'SV1-033', 'COMMON', 'EN', @manager),
(UUID(), 'Houndoom', '9', 'FIRE', @scarlet_violet_id, 'SV1-034', 'COMMON', 'EN', @manager),
(UUID(), 'Torkoal', '9', 'FIRE', @scarlet_violet_id, 'SV1-035', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Fuecoco', '9', 'FIRE', @scarlet_violet_id, 'SV1-036', 'COMMON', 'EN', @manager),
(UUID(), 'Crocalor', '9', 'FIRE', @scarlet_violet_id, 'SV1-037', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Skeledirge', '9', 'FIRE', @scarlet_violet_id, 'SV1-038', 'RARE', 'EN', @manager),
(UUID(), 'Charcadet', '9', 'FIRE', @scarlet_violet_id, 'SV1-039', 'COMMON', 'EN', @manager),
(UUID(), 'Charcadet', '9', 'FIRE', @scarlet_violet_id, 'SV1-040', 'COMMON', 'EN', @manager),
(UUID(), 'Armarouge', '9', 'FIRE', @scarlet_violet_id, 'SV1-041', 'RARE', 'EN', @manager),
(UUID(), 'Ceruledge', '9', 'FIRE', @scarlet_violet_id, 'SV1-042', 'RARE', 'EN', @manager),
(UUID(), 'Buizel', '9', 'WATER', @scarlet_violet_id, 'SV1-043', 'COMMON', 'EN', @manager),
(UUID(), 'Floatzel', '9', 'WATER', @scarlet_violet_id, 'SV1-044', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Magikarp', '9', 'WATER', @scarlet_violet_id, 'SV1-045', 'COMMON', 'EN', @manager),
(UUID(), 'Gyarados ex', '9', 'WATER', @scarlet_violet_id, 'SV1-046', 'RARE_ULTRA', 'EN', @manager),
(UUID(), 'Wiglett', '9', 'WATER', @scarlet_violet_id, 'SV1-047', 'COMMON', 'EN', @manager),
(UUID(), 'Wiglett', '9', 'WATER', @scarlet_violet_id, 'SV1-048', 'COMMON', 'EN', @manager),
(UUID(), 'Wugtrio', '9', 'WATER', @scarlet_violet_id, 'SV1-049', 'RARE', 'EN', @manager),
(UUID(), 'Psyduck', '9', 'WATER', @scarlet_violet_id, 'SV1-050', 'COMMON', 'EN', @manager),
(UUID(), 'Golduck', '9', 'WATER', @scarlet_violet_id, 'SV1-051', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Quaxly', '9', 'WATER', @scarlet_violet_id, 'SV1-052', 'COMMON', 'EN', @manager),
(UUID(), 'Quaxwell', '9', 'WATER', @scarlet_violet_id, 'SV1-053', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Quaquaval', '9', 'WATER', @scarlet_violet_id, 'SV1-054', 'RARE', 'EN', @manager),
(UUID(), 'Basculin', '9', 'WATER', @scarlet_violet_id, 'SV1-055', 'COMMON', 'EN', @manager),
(UUID(), 'Dondozo', '9', 'WATER', @scarlet_violet_id, 'SV1-056', 'RARE', 'EN', @manager),
(UUID(), 'Tatsugiri', '9', 'WATER', @scarlet_violet_id, 'SV1-057', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Tatsugiri', '9', 'WATER', @scarlet_violet_id, 'SV1-058', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Tatsugiri', '9', 'WATER', @scarlet_violet_id, 'SV1-059', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Cetoddle', '9', 'WATER', @scarlet_violet_id, 'SV1-060', 'COMMON', 'EN', @manager),
(UUID(), 'Cetoddle', '9', 'WATER', @scarlet_violet_id, 'SV1-061', 'COMMON', 'EN', @manager),
(UUID(), 'Cetitan', '9', 'WATER', @scarlet_violet_id, 'SV1-062', 'RARE', 'EN', @manager),
(UUID(), 'Gulpin', '9', 'WATER', @scarlet_violet_id, 'SV1-063', 'COMMON', 'EN', @manager),
(UUID(), 'Swalot', '9', 'WATER', @scarlet_violet_id, 'SV1-064', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Clauncher', '9', 'WATER', @scarlet_violet_id, 'SV1-065', 'COMMON', 'EN', @manager),
(UUID(), 'Clauncher', '9', 'WATER', @scarlet_violet_id, 'SV1-066', 'COMMON', 'EN', @manager),
(UUID(), 'Clawitzer', '9', 'WATER', @scarlet_violet_id, 'SV1-067', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Slowpoke', '9', 'WATER', @scarlet_violet_id, 'SV1-068', 'COMMON', 'EN', @manager),
(UUID(), 'Slowbro', '9', 'WATER', @scarlet_violet_id, 'SV1-069', 'RARE', 'EN', @manager),
(UUID(), 'Kilowattrel', '9', 'LIGHTNING', @scarlet_violet_id, 'SV1-070', 'RARE', 'EN', @manager),
(UUID(), 'Pikachu', '9', 'LIGHTNING', @scarlet_violet_id, 'SV1-071', 'COMMON', 'EN', @manager),
(UUID(), 'Pikachu', '9', 'LIGHTNING', @scarlet_violet_id, 'SV1-072', 'COMMON', 'EN', @manager),
(UUID(), 'Pachirisu', '9', 'LIGHTNING', @scarlet_violet_id, 'SV1-073', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Pawmi', '9', 'LIGHTNING', @scarlet_violet_id, 'SV1-074', 'COMMON', 'EN', @manager),
(UUID(), 'Pawmi', '9', 'LIGHTNING', @scarlet_violet_id, 'SV1-075', 'COMMON', 'EN', @manager),
(UUID(), 'Pawmo', '9', 'LIGHTNING', @scarlet_violet_id, 'SV1-076', 'UNCOMMON', 'EN', @manager),
(UUID(), 'Pawmot', '9', 'LIGHTNING', @scarlet_violet_id, 'SV1-077', 'RARE', 'EN', @manager),
(UUID(), 'Amped Up Toxel', '9', 'LIGHTNING', @scarlet_violet_id, 'SV1-078', 'COMMON', 'EN', @manager),
(UUID(), 'Low Key Toxel', '9', 'LIGHTNING', @scarlet_violet_id, 'SV1-079', 'COMMON', 'EN', @manager),
(UUID(), 'Amped Up Toxtricity', '9', 'LIGHTNING', @scarlet_violet_id, 'SV1-080', 'UNCOMMON', 'EN', @manager),
(UUID(), 'SPRIGATITO', '9', 'GRASS', @scarlet_violet_promo_id, 'SVP-001', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'FUECOCO', '9', 'FIRE', @scarlet_violet_promo_id, 'SVP-002', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'QUAXLY', '9', 'WATER', @scarlet_violet_promo_id, 'SVP-003', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'MIMIKYU EX', '9', 'PSYCHIC', @scarlet_violet_promo_id, 'SVP-004', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'QUAQUAVAL', '9', 'WATER', @scarlet_violet_promo_id, 'SVP-005', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'PAWMOT', '9', 'LIGHTNING', @scarlet_violet_promo_id, 'SVP-006', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'HAWLUCHA', '9', 'FIGHTING', @scarlet_violet_promo_id, 'SVP-007', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'REVAVROOM', '9', 'METAL', @scarlet_violet_promo_id, 'SVP-008', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'SPIDOPS', '9', 'GRASS', @scarlet_violet_promo_id, 'SVP-009', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'ESPATHRA', '9', 'PSYCHIC', @scarlet_violet_promo_id, 'SVP-010', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'ARCANINE', '9', 'FIRE', @scarlet_violet_promo_id, 'SVP-011', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'DONDOZO', '9', 'WATER', @scarlet_violet_promo_id, 'SVP-012', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'MIRAIDON', '9', 'LIGHTNING', @scarlet_violet_promo_id, 'SVP-013', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'KORAIDON', '9', 'FIGHTING', @scarlet_violet_promo_id, 'SVP-014', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'FLAAFFY', '9', 'LIGHTNING', @scarlet_violet_promo_id, 'SVP-015', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'AMPHAROS EX', '9', 'LIGHTNING', @scarlet_violet_promo_id, 'SVP-016', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'LUCARIO EX', '9', 'FIGHTING', @scarlet_violet_promo_id, 'SVP-017', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'CYCLIZAR EX', '9', 'COLORLESS', @scarlet_violet_promo_id, 'SVP-018', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'BAXCALIBUR', '9', 'WATER', @scarlet_violet_promo_id, 'SVP-019', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'TINKATON', '9', 'PSYCHIC', @scarlet_violet_promo_id, 'SVP-020', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'MURKROW', '9', 'DARKNESS', @scarlet_violet_promo_id, 'SVP-021', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'PELIPPER', '9', 'COLORLESS', @scarlet_violet_promo_id, 'SVP-022', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'SMOLIV', '9', 'GRASS', @scarlet_violet_promo_id, 'SVP-023', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'GROWLITHE', '9', 'FIRE', @scarlet_violet_promo_id, 'SVP-024', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'TINKATINK', '9', 'PSYCHIC', @scarlet_violet_promo_id, 'SVP-025', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'VAROOM', '9', 'METAL', @scarlet_violet_promo_id, 'SVP-026', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'PIKACHU', '9', 'LIGHTNING', @scarlet_violet_promo_id, 'SVP-027', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'MIRAIDON EX', '9', 'LIGHTNING', @scarlet_violet_promo_id, 'SVP-028', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'KORAIDON EX', '9', 'FIGHTING', @scarlet_violet_promo_id, 'SVP-029', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'CHIEN-PAO EX', '9', 'WATER', @scarlet_violet_promo_id, 'SVP-030', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'TINKATON EX', '9', 'PSYCHIC', @scarlet_violet_promo_id, 'SVP-031', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'ANNIHILAPE EX', '9', 'FIGHTING', @scarlet_violet_promo_id, 'SVP-032', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'MEOWSCARADA EX', '9', 'GRASS', @scarlet_violet_promo_id, 'SVP-033', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'SKELEDIRGE EX', '9', 'FIRE', @scarlet_violet_promo_id, 'SVP-034', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'QUAQUAVAL EX', '9', 'WATER', @scarlet_violet_promo_id, 'SVP-035', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'PALAFIN', '9', 'WATER', @scarlet_violet_promo_id, 'SVP-036', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'CLEFFA', '9', 'PSYCHIC', @scarlet_violet_promo_id, 'SVP-037', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'TOGEKISS', '9', 'PSYCHIC', @scarlet_violet_promo_id, 'SVP-038', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'MAWILE', '9', 'METAL', @scarlet_violet_promo_id, 'SVP-039', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'PAWMI', '9', 'LIGHTNING', @scarlet_violet_promo_id, 'SVP-040', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'PALDEAN WOOPER', '9', 'DARKNESS', @scarlet_violet_promo_id, 'SVP-041', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'HOUNDSTONE', '9', 'PSYCHIC', @scarlet_violet_promo_id, 'SVP-042', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'EEVEE', '9', 'COLORLESS', @scarlet_violet_promo_id, 'SVP-043', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'CHARMANDER', '9', 'FIRE', @scarlet_violet_promo_id, 'SVP-044', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'BULBASAUR', '9', 'GRASS', @scarlet_violet_promo_id, 'SVP-046', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'CHARMANDER', '9', 'FIRE', @scarlet_violet_promo_id, 'SVP-047', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'SQUIRTLE', '9', 'WATER', @scarlet_violet_promo_id, 'SVP-048', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'ZAPDOS EX', '9', 'LIGHTNING', @scarlet_violet_promo_id, 'SVP-049', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'ALAKAZAM EX', '9', 'PSYCHIC', @scarlet_violet_promo_id, 'SVP-050', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'SNORLAX', '9', 'COLORLESS', @scarlet_violet_promo_id, 'SVP-051', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'MEWTWO', '9', 'PSYCHIC', @scarlet_violet_promo_id, 'SVP-052', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'MEW EX', '9', 'PSYCHIC', @scarlet_violet_promo_id, 'SVP-053', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'GRENINJA EX', '9', 'WATER', @scarlet_violet_promo_id, 'SVP-054', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'KANGASKHAN EX', '9', 'COLORLESS', @scarlet_violet_promo_id, 'SVP-055', 'RARE_PROMO', 'EN', @manager),
(UUID(), 'PIKACHU', '9', 'LIGHTNING', @scarlet_violet_promo_id, 'SVP-101', 'RARE_PROMO', 'EN', @manager);

-- BASE SET
INSERT INTO card (id, title, season, pokemon_type, collection_id, code, rarity, nationality, created_by) VALUES
('f7ce6241-8e1c-4c8c-9d36-9b4b7d0dc9bb', 'Pikachu', '1', 'LIGHTNING', '983ca278-b230-4c92-801b-59d443b0534b', 'BS-025', 'COMMON', 'US', @manager),
('4256be5e-7fd6-4497-918d-395cfe404a9b', 'Charizard', '1', 'FIRE', '983ca278-b230-4c92-801b-59d443b0534b', 'BS-004', 'RARE_HOLO', 'JP', @manager),
('3c30fb24-e9a3-4d2f-88ff-71e857fcd2cd', 'Blastoise', '1', 'WATER', '983ca278-b230-4c92-801b-59d443b0534b', 'BS-002', 'RARE_HOLO', 'US', @manager),
('a1a2a3a4-b5b6-4c7d-8e9f-0a1b2c3d4e5f', 'Venusaur', '1', 'GRASS', '983ca278-b230-4c92-801b-59d443b0534b', 'BS-015', 'RARE_HOLO', 'US', @manager),
('b2b3b4b5-c6c7-4d8e-9f0a-1b2c3d4e5f6a', 'Alakazam', '1', 'PSYCHIC', '983ca278-b230-4c92-801b-59d443b0534b', 'BS-001', 'RARE_HOLO', 'US', @manager),
('c3c4c5c6-d7d8-4e9f-0a1b-2c3d4e5f6a7b', 'Mewtwo', '1', 'PSYCHIC', '983ca278-b230-4c92-801b-59d443b0534b', 'BS-010', 'RARE_HOLO', 'US', @manager),
('d4d5d6d7-e8e9-4f0a-1b2c-3d4e5f6a7b8c', 'Raichu', '1', 'LIGHTNING', '983ca278-b230-4c92-801b-59d443b0534b', 'BS-014', 'RARE_HOLO', 'US', @manager),
('e5e6e7e8-f9f0-4a1b-2c3d-4e5f6a7b8c9d', 'Machamp', '1', 'FIGHTING', '983ca278-b230-4c92-801b-59d443b0534b', 'BS-008', 'RARE_HOLO', 'US', @manager);

-- JUNGLE
INSERT INTO card (id, title, season, pokemon_type, collection_id, code, rarity, nationality, created_by) VALUES
('c055e02a-fad8-4be8-8e43-edad513d8e44', 'Snorlax', '1', 'COLORLESS', 'c8f1f35b-8e8c-46cb-9f83-0e4c71b7c984', 'JU-011', 'RARE_HOLO', 'US', @manager),
('7b9e41b5-73b0-4684-9c72-d1e9016da41a', 'Scyther', '1', 'GRASS', 'c8f1f35b-8e8c-46cb-9f83-0e4c71b7c984', 'JU-010', 'RARE_HOLO', 'US', @manager),
('f6f7f8f9-a0a1-4b2c-3d4e-5f6a7b8c9d0e', 'Wigglytuff', '1', 'COLORLESS', 'c8f1f35b-8e8c-46cb-9f83-0e4c71b7c984', 'JU-016', 'RARE_HOLO', 'US', @manager),
('a7a8a9a0-b1b2-4c3d-4e5f-6a7b8c9d0e1f', 'Kangaskhan', '1', 'COLORLESS', 'c8f1f35b-8e8c-46cb-9f83-0e4c71b7c984', 'JU-005', 'RARE_HOLO', 'US', @manager),
('b8b9b0b1-c2c3-4d4e-5f6a-7b8c9d0e1f2a', 'Pinsir', '1', 'GRASS', 'c8f1f35b-8e8c-46cb-9f83-0e4c71b7c984', 'JU-009', 'RARE_HOLO', 'US', @manager),
('c9c0c1c2-d3d4-4e5f-6a7b-8c9d0e1f2a3b', 'Vaporeon', '1', 'WATER', 'c8f1f35b-8e8c-46cb-9f83-0e4c71b7c984', 'JU-012', 'RARE_HOLO', 'US', @manager);

-- FOSSIL
INSERT INTO card (id, title, season, pokemon_type, collection_id, code, rarity, nationality, created_by) VALUES
('d0d1d2d3-e4e5-4f6a-7b8c-9d0e1f2a3b4c', 'Aerodactyl', '1', 'COLORLESS', 'd5e6f7a8-b9c0-4d1e-2f3a-4b5c6d7e8f9a', 'FO-001', 'RARE_HOLO', 'US', @manager),
('e1e2e3e4-f5f6-4a7b-8c9d-0e1f2a3b4c5d', 'Kabutops', '1', 'FIGHTING', 'd5e6f7a8-b9c0-4d1e-2f3a-4b5c6d7e8f9a', 'FO-009', 'RARE_HOLO', 'US', @manager),
('f2f3f4f5-a6a7-4b8c-9d0e-1f2a3b4c5d6e', 'Zapdos', '1', 'LIGHTNING', 'd5e6f7a8-b9c0-4d1e-2f3a-4b5c6d7e8f9a', 'FO-015', 'RARE_HOLO', 'US', @manager),
('a3a4a5a6-b7b8-4c9d-0e1f-2a3b4c5d6e7f', 'Moltres', '1', 'FIRE', 'd5e6f7a8-b9c0-4d1e-2f3a-4b5c6d7e8f9a', 'FO-012', 'RARE_HOLO', 'US', @manager),
('b4b5b6b7-c8c9-4d0e-1f2a-3b4c5d6e7f8a', 'Articuno', '1', 'WATER', 'd5e6f7a8-b9c0-4d1e-2f3a-4b5c6d7e8f9a', 'FO-002', 'RARE_HOLO', 'US', @manager),
('c5c6c7c8-d9d0-4e1f-2a3b-4c5d6e7f8a9b', 'Ditto', '1', 'COLORLESS', 'd5e6f7a8-b9c0-4d1e-2f3a-4b5c6d7e8f9a', 'FO-003', 'RARE_HOLO', 'US', @manager);

-- TEAM ROCKET
INSERT INTO card (id, title, season, pokemon_type, collection_id, code, rarity, nationality, created_by) VALUES
('d6d7d8d9-e0e1-4f2a-3b4c-5d6e7f8a9b0c', 'Dark Charizard', '1', 'FIRE', 'e6f7a8b9-c0d1-4e2f-3a4b-5c6d7e8f9a0b', 'TR-004', 'RARE_HOLO', 'US', @manager),
('e7e8e9e0-f1f2-4a3b-4c5d-6e7f8a9b0c1d', 'Dark Blastoise', '1', 'WATER', 'e6f7a8b9-c0d1-4e2f-3a4b-5c6d7e8f9a0b', 'TR-003', 'RARE_HOLO', 'US', @manager),
('f8f9f0f1-a2a3-4b4c-5d6e-7f8a9b0c1d2e', 'Dark Alakazam', '1', 'PSYCHIC', 'e6f7a8b9-c0d1-4e2f-3a4b-5c6d7e8f9a0b', 'TR-001', 'RARE_HOLO', 'US', @manager),
('a9a0a1a2-b3b4-4c5d-6e7f-8a9b0c1d2e3f', 'Dark Dragonite', '1', 'COLORLESS', 'e6f7a8b9-c0d1-4e2f-3a4b-5c6d7e8f9a0b', 'TR-005', 'RARE_HOLO', 'US', @manager);

-- NEO GENESIS
INSERT INTO card (id, title, season, pokemon_type, collection_id, code, rarity, nationality, created_by) VALUES
('b0b1b2b3-c4c5-4d6e-7f8a-9b0c1d2e3f4a', 'Lugia', '2', 'COLORLESS', 'a8b9c0d1-e2f3-4a4b-5c6d-7e8f9a0b1c2d', 'NG-009', 'RARE_HOLO', 'US', @manager),
('c1c2c3c4-d5d6-4e7f-8a9b-0c1d2e3f4a5b', 'Ampharos', '2', 'LIGHTNING', 'a8b9c0d1-e2f3-4a4b-5c6d-7e8f9a0b1c2d', 'NG-001', 'RARE_HOLO', 'US', @manager),
('d2d3d4d5-e6e7-4f8a-9b0c-1d2e3f4a5b6c', 'Typhlosion', '2', 'FIRE', 'a8b9c0d1-e2f3-4a4b-5c6d-7e8f9a0b1c2d', 'NG-017', 'RARE_HOLO', 'US', @manager),
('e3e4e5e6-f7f8-4a9b-0c1d-2e3f4a5b6c7d', 'Feraligatr', '2', 'WATER', 'a8b9c0d1-e2f3-4a4b-5c6d-7e8f9a0b1c2d', 'NG-004', 'RARE_HOLO', 'US', @manager),
('f4f5f6f7-a8a9-4b0c-1d2e-3f4a5b6c7d8e', 'Meganium', '2', 'GRASS', 'a8b9c0d1-e2f3-4a4b-5c6d-7e8f9a0b1c2d', 'NG-010', 'RARE_HOLO', 'US', @manager);

-- EX RUBY & SAPPHIRE
INSERT INTO card (id, title, season, pokemon_type, collection_id, code, rarity, nationality, created_by) VALUES
('a5a6a7a8-b9b0-4c1d-2e3f-4a5b6c7d8e9f', 'Blaziken', '3', 'FIRE', 'c0d1e2f3-a4b5-4c6d-7e8f-9a0b1c2d3e4f', 'RS-003', 'RARE_HOLO', 'US', @manager),
('b6b7b8b9-c0c1-4d2e-3f4a-5b6c7d8e9f0a', 'Gardevoir', '3', 'PSYCHIC', 'c0d1e2f3-a4b5-4c6d-7e8f-9a0b1c2d3e4f', 'RS-007', 'RARE_HOLO', 'US', @manager),
('c7c8c9c0-d1d2-4e3f-4a5b-6c7d8e9f0a1b', 'Swampert', '3', 'WATER', 'c0d1e2f3-a4b5-4c6d-7e8f-9a0b1c2d3e4f', 'RS-013', 'RARE_HOLO', 'US', @manager),
('d8d9d0d1-e2e3-4f4a-5b6c-7d8e9f0a1b2c', 'Sceptile', '3', 'GRASS', 'c0d1e2f3-a4b5-4c6d-7e8f-9a0b1c2d3e4f', 'RS-010', 'RARE_HOLO', 'US', @manager);

-- DIAMOND & PEARL
INSERT INTO card (id, title, season, pokemon_type, collection_id, code, rarity, nationality, created_by) VALUES
('e9e0e1e2-f3f4-4a5b-6c7d-8e9f0a1b2c3d', 'Dialga', '4', 'METAL', 'e2f3a4b5-c6d7-4e8f-9a0b-1c2d3e4f5a6b', 'DP-001', 'RARE_HOLO', 'US', @manager),
('f0f1f2f3-a4a5-4b6c-7d8e-9f0a1b2c3d4e', 'Palkia', '4', 'WATER', 'e2f3a4b5-c6d7-4e8f-9a0b-1c2d3e4f5a6b', 'DP-011', 'RARE_HOLO', 'US', @manager),
('11223344-5566-4778-8990-aabbccddeeff', 'Empoleon', '4', 'WATER', 'e2f3a4b5-c6d7-4e8f-9a0b-1c2d3e4f5a6b', 'DP-004', 'RARE_HOLO', 'US', @manager),
('22334455-6677-4889-9aa0-bbccddeeeff0', 'Infernape', '4', 'FIRE', 'e2f3a4b5-c6d7-4e8f-9a0b-1c2d3e4f5a6b', 'DP-005', 'RARE_HOLO', 'US', @manager),
('33445566-7788-4990-aabb-bccddeeff001', 'Torterra', '4', 'GRASS', 'e2f3a4b5-c6d7-4e8f-9a0b-1c2d3e4f5a6b', 'DP-012', 'RARE_HOLO', 'US', @manager);

-- BLACK & WHITE
INSERT INTO card (id, title, season, pokemon_type, collection_id, code, rarity, nationality, created_by) VALUES
('44556677-8899-4aa0-bbc0-cddeeff00112', 'Reshiram', '5', 'FIRE', 'a4b5c6d7-e8f9-4a0b-1c2d-3e4f5a6b7c8d', 'BW-026', 'RARE_HOLO', 'US', @manager),
('55667788-99aa-4bb0-ccd0-deeff0011223', 'Zekrom', '5', 'LIGHTNING', 'a4b5c6d7-e8f9-4a0b-1c2d-3e4f5a6b7c8d', 'BW-047', 'RARE_HOLO', 'US', @manager),
('66778899-aa00-4cc1-dde0-eff001122334', 'Serperior', '5', 'GRASS', 'a4b5c6d7-e8f9-4a0b-1c2d-3e4f5a6b7c8d', 'BW-006', 'RARE_HOLO', 'US', @manager),
('77889900-bb11-4dd2-2ee3-3ff445566778', 'Samurott', '5', 'WATER', 'a4b5c6d7-e8f9-4a0b-1c2d-3e4f5a6b7c8d', 'BW-032', 'RARE_HOLO', 'US', @manager);

-- XY BASE SET
INSERT INTO card (id, title, season, pokemon_type, collection_id, code, rarity, nationality, created_by) VALUES
('88990011-cc22-4ee3-3ff4-456677889900', 'Xerneas', '6', 'FAIRY', 'b5c6d7e8-f9a0-4b1c-2d3e-4f5a6b7c8d9e', 'XY-146', 'RARE_HOLO', 'US', @manager),
('99001122-dd33-4ff4-4aa5-567788990011', 'Yveltal', '6', 'DARKNESS', 'b5c6d7e8-f9a0-4b1c-2d3e-4f5a6b7c8d9e', 'XY-144', 'RARE_HOLO', 'US', @manager),
('aa112233-ee44-4aa5-5bb6-678899001122', 'Greninja', '6', 'WATER', 'b5c6d7e8-f9a0-4b1c-2d3e-4f5a6b7c8d9e', 'XY-041', 'RARE_HOLO', 'US', @manager);

-- SUN & MOON
INSERT INTO card (id, title, season, pokemon_type, collection_id, code, rarity, nationality, created_by) VALUES
('bb223344-ff55-4cc6-6dd7-789900112233', 'Solgaleo GX', '7', 'METAL', 'c6d7e8f9-a0b1-4c2d-3e4f-5a6b7c8d9e0f', 'SM-089', 'RARE_HOLO_GX', 'US', @manager),
('cc334455-aa66-4dd7-7ee8-890011223344', 'Lunala GX', '7', 'PSYCHIC', 'c6d7e8f9-a0b1-4c2d-3e4f-5a6b7c8d9e0f', 'SM-066', 'RARE_HOLO_GX', 'US', @manager),
('dd445566-bb77-4ee8-8ff9-901122334455', 'Decidueye GX', '7', 'GRASS', 'c6d7e8f9-a0b1-4c2d-3e4f-5a6b7c8d9e0f', 'SM-012', 'RARE_HOLO_GX', 'US', @manager);

-- SWORD & SHIELD
INSERT INTO card (id, title, season, pokemon_type, collection_id, code, rarity, nationality, created_by) VALUES
('ee556677-cc88-4ff9-9aa0-012233445566', 'Zacian V', '8', 'METAL', 'd7e8f9a0-b1c2-4d3e-4f5a-6b7c8d9e0f1a', 'SWSH-138', 'RARE_HOLO_V', 'US', @manager),
('ff667788-dd99-4aa0-0bb1-123344556677', 'Zamazenta V', '8', 'METAL', 'd7e8f9a0-b1c2-4d3e-4f5a-6b7c8d9e0f1a', 'SWSH-139', 'RARE_HOLO_V', 'US', @manager),
('00778899-ee00-4bb1-1cc2-234455667788', 'Cinderace VMAX', '8', 'FIRE', 'd7e8f9a0-b1c2-4d3e-4f5a-6b7c8d9e0f1a', 'SWSH-036', 'RARE_HOLO_VMAX', 'US', @manager);


-- BOOSTER BOXES
INSERT INTO other_product (id, type, nationality, package_contents, extra_info, created_by) VALUES
('bf6c9e4b-fa7f-4fbd-b2ac-8d4ab7578a40', 'BOOSTER_BOX', 'US',
 '36 boosters com 10 cartas cada', 'Selo original Wizards of the Coast', @manager),

('1c21ae86-d868-4f4f-8ccb-75802b7ec76c', 'BOOSTER_BOX', 'JP',
 '30 boosters japoneses de 5 cartas', 'Impressão japonesa de alta qualidade', @manager),

('d3cc4bce-64fa-4e42-a648-7b2ea5446a19', 'BOOSTER_BOX', 'US',
 '36 boosters SV1', 'Primeiro set da linha Scarlet & Violet', @manager),

('1a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d', 'BOOSTER_BOX', 'US',
 '36 boosters Fossil', 'Set com fósseis e birds lendários', @manager),

('2b3c4d5e-6f7a-4b8c-9d0e-1f2a3b4c5d6e', 'BOOSTER_BOX', 'US',
 '36 boosters Team Rocket', 'Edição especial Dark Pokémon', @manager),

('3c4d5e6f-7a8b-4c9d-0e1f-2a3b4c5d6e7f', 'BOOSTER_BOX', 'US',
 '36 boosters Neo Genesis', 'Primeira coleção Gen 2', @manager),

('4d5e6f7a-8b9c-4d0e-1f2a-3b4c5d6e7f8a', 'BOOSTER_BOX', 'US',
 '36 boosters Diamond & Pearl', 'Início da Gen 4', @manager),

('5e6f7a8b-9c0d-4e1f-2a3b-4c5d6e7f8a9b', 'BOOSTER_BOX', 'US',
 '36 boosters Sun & Moon', 'GX cards Gen 7', @manager);

-- ACCESSORIES
INSERT INTO other_product (id, type, nationality, package_contents, extra_info, created_by) VALUES
('82786ffd-9936-4ece-8c81-2b88f62a61f8', 'ACCESSORY', 'US',
 'Pacote com 60 sleeves premium', 'Dragon Shield Matte', @manager),

('a2ae25e2-0886-47ea-b1d5-f79b0c25283d', 'ACCESSORY', 'US',
 'Binder 9-pocket, 20 páginas', 'Ultra PRO - Charizard Edition', @manager),

('e4cb676a-05ac-486d-b5b0-e042fe3e232d', 'ACCESSORY', 'US',
 'Playmat neoprene 60x35cm', 'Mewtwo Galaxy Edition', @manager);
 
 -- OTHER (produtos diversos)
INSERT INTO other_product (id, type, nationality, package_contents, extra_info, created_by) VALUES
('cd88a9aa-2c79-47e6-ba50-e7f46c9fe43c', 'OTHER', 'US',
 'Caixa temática Pikachu (deck + marcadores)', 'Item de coleção', @manager),

('bb2f7281-1d70-4c71-a1e7-8cbda6ae048e', 'OTHER', 'US',
 'Lata colecionável Snorlax', 'Inclui 4 boosters + carta promocional Snorlax', @manager);

INSERT INTO product (id, name, description, card_id, price, product_condition, store_id, created_by) VALUES
('e0a3bc33-904a-42ed-8221-b7231e70352b', 'Pikachu - Condição Mint', 'Carta Pokemon TCG colecionável', 'f7ce6241-8e1c-4c8c-9d36-9b4b7d0dc9bb', 12.90, 'MINT', @store_id, @manager),
('bc35ae69-387e-4bff-bbd5-5d23994e94c4', 'Charizard - Condição Lightly Played', 'Carta Pokemon TCG colecionável', '4256be5e-7fd6-4497-918d-395cfe404a9b', 450.00, 'LIGHTLY_PLAYED', @store_id, @manager),
('95f91aed-1ee2-4f7d-a830-23906438377a', 'Charizard - Condição Mint', 'Carta Pokemon TCG colecionável', '4256be5e-7fd6-4497-918d-395cfe404a9b', 599.00, 'MINT', @store_id, @manager),
('f472ae78-4a51-4abe-89ad-1ae7297e0ae7', 'Blastoise - Condição Lightly Played', 'Carta Pokemon TCG colecionável', '3c30fb24-e9a3-4d2f-88ff-71e857fcd2cd', 150.00, 'LIGHTLY_PLAYED', @store_id, @manager),
('85978edf-111a-481a-9d03-b615fa468d00', 'Blastoise - Condição Moderately Played', 'Carta Pokemon TCG colecionável', '3c30fb24-e9a3-4d2f-88ff-71e857fcd2cd', 120.00, 'MODERATELY_PLAYED', @store_id, @manager),
('0160abc5-be4e-448f-a2f2-4ec370dd48b5', 'Venusaur - Condição Mint', 'Carta Pokemon TCG colecionável', 'a1a2a3a4-b5b6-4c7d-8e9f-0a1b2c3d4e5f', 180.00, 'MINT', @store_id, @manager),
('d5fb1c62-defd-48d7-bd14-f327c32eed54', 'Venusaur - Condição Moderately Played', 'Carta Pokemon TCG colecionável', 'a1a2a3a4-b5b6-4c7d-8e9f-0a1b2c3d4e5f', 110.00, 'MODERATELY_PLAYED', @store_id, @manager),
('af11995d-438b-475c-b981-4b8599e1da32', 'Alakazam - Condição Near Mint', 'Carta Pokemon TCG colecionável', 'b2b3b4b5-c6c7-4d8e-9f0a-1b2c3d4e5f6a', 85.00, 'MINT', @store_id, @manager),
('a0e16e17-71b6-4ab9-b744-2ee1432bf875', 'Alakazam - Condição Mint', 'Carta Pokemon TCG colecionável', 'b2b3b4b5-c6c7-4d8e-9f0a-1b2c3d4e5f6a', 95.00, 'MINT', @store_id, @manager),
('265de878-5387-4941-a461-e6678a1cd9de', 'Mewtwo - Condição Near Mint', 'Carta Pokemon TCG colecionável', 'c3c4c5c6-d7d8-4e9f-0a1b-2c3d4e5f6a7b', 120.00, 'MINT', @store_id, @manager),
('05bf81da-9d0f-4f08-a96e-5b9886def0a0', 'Mewtwo - Condição Moderately Played', 'Carta Pokemon TCG colecionável', 'c3c4c5c6-d7d8-4e9f-0a1b-2c3d4e5f6a7b', 80.00, 'MODERATELY_PLAYED', @store_id, @manager),
('be682af0-a41f-4689-9ea4-8c2704ae6fb6', 'Raichu - Condição Lightly Played', 'Carta Pokemon TCG colecionável', 'd4d5d6d7-e8e9-4f0a-1b2c-3d4e5f6a7b8c', 45.00, 'LIGHTLY_PLAYED', @store_id, @manager),
('30157d8e-8af8-447f-8eb0-6b014954ead7', 'Machamp - Condição Moderately Played', 'Carta Pokemon TCG colecionável', 'e5e6e7e8-f9f0-4a1b-2c3d-4e5f6a7b8c9d', 28.00, 'MODERATELY_PLAYED', @store_id, @manager),
('62c00e3e-255e-4f4b-b34b-77b9dc83ad3c', 'Snorlax - Condição Near Mint', 'Carta Pokemon TCG colecionável', 'c055e02a-fad8-4be8-8e43-edad513d8e44', 50.00, 'MINT', @store_id, @manager),
('44bef7e1-78cf-4aab-ad93-38adc83a9fd9', 'Scyther - Condição Mint', 'Carta Pokemon TCG colecionável', '7b9e41b5-73b0-4684-9c72-d1e9016da41a', 48.00, 'MINT', @store_id, @manager),
('9a791da8-1e79-4aa9-98da-5ea350c69670', 'Wigglytuff - Condição Moderately Played', 'Carta Pokemon TCG colecionável', 'f6f7f8f9-a0a1-4b2c-3d4e-5f6a7b8c9d0e', 24.00, 'MODERATELY_PLAYED', @store_id, @manager),
('00e24eaa-9766-4056-a49c-77f9457e41b9', 'Dark Charizard - Condição Mint', 'Carta Pokemon TCG colecionável', 'd6d7d8d9-e0e1-4f2a-3b4c-5d6e7f8a9b0c', 170.00, 'MINT', @store_id, @manager),
('70c80142-3437-497d-acde-09f645374b54', 'Lugia - Condição Moderately Played', 'Carta Pokemon TCG colecionável', 'b0b1b2b3-c4c5-4d6e-7f8a-9b0c1d2e3f4a', 65.00, 'MODERATELY_PLAYED', @store_id, @manager),
('dd32d1cc-7210-4662-a74e-1c20856f9749', 'Xerneas - Condição Mint', 'Carta Pokemon TCG colecionável', '88990011-cc22-4ee3-3ff4-456677889900', 35.00, 'MINT', @store_id, @manager),
('2bf51512-1baa-4283-a24e-5898a3678468', 'Greninja - Condição Near Mint', 'Carta Pokemon TCG colecionável', 'aa112233-ee44-4aa5-5bb6-678899001122', 40.00, 'MINT', @store_id, @manager);

-- BOOSTER BOXES PRODUCTS
INSERT INTO product (id, name, description, other_product_id, price, product_condition, store_id, created_by) VALUES
('bb01-0001-0001-0001-000000000001', 'Booster Box Base Set', 'Caixa lacrada com 36 boosters Base Set', 'bf6c9e4b-fa7f-4fbd-b2ac-8d4ab7578a40', 8999.00, 'SEALED', @store_id, @manager),
('bb01-0002-0002-0002-000000000002', 'Booster Box Jungle JP', 'Caixa lacrada japonesa com 30 boosters Jungle', '1c21ae86-d868-4f4f-8ccb-75802b7ec76c', 1299.00, 'SEALED', @store_id, @manager),
('bb01-0003-0003-0003-000000000003', 'Booster Box Scarlet & Violet Base', 'Caixa lacrada com 36 boosters SV1', 'd3cc4bce-64fa-4e42-a648-7b2ea5446a19', 799.00, 'SEALED', @store_id, @manager),
('bb01-0004-0004-0004-000000000004', 'Booster Box Fossil', 'Caixa lacrada com 36 boosters Fossil', '1a2b3c4d-5e6f-4a7b-8c9d-0e1f2a3b4c5d', 4500.00, 'SEALED', @store_id, @manager),
('bb01-0005-0005-0005-000000000005', 'Booster Box Team Rocket', 'Caixa lacrada com 36 boosters Team Rocket', '2b3c4d5e-6f7a-4b8c-9d0e-1f2a3b4c5d6e', 3200.00, 'SEALED', @store_id, @manager),
('bb01-0006-0006-0006-000000000006', 'Booster Box Neo Genesis', 'Caixa lacrada com 36 boosters Neo Genesis', '3c4d5e6f-7a8b-4c9d-0e1f-2a3b4c5d6e7f', 2800.00, 'SEALED', @store_id, @manager),
('bb01-0007-0007-0007-000000000007', 'Booster Box Diamond & Pearl', 'Caixa lacrada com 36 boosters Diamond & Pearl', '4d5e6f7a-8b9c-4d0e-1f2a-3b4c5d6e7f8a', 1500.00, 'SEALED', @store_id, @manager),
('bb01-0008-0008-0008-000000000008', 'Booster Box Sun & Moon', 'Caixa lacrada com 36 boosters Sun & Moon', '5e6f7a8b-9c0d-4e1f-2a3b-4c5d6e7f8a9b', 950.00, 'SEALED', @store_id, @manager);

-- ACCESSORY PRODUCTS
INSERT INTO product (id, name, description, other_product_id, price, product_condition, store_id, created_by) VALUES
('ac01-0001-0001-0001-000000000001', 'Dragon Shield Sleeves Premium', 'Pacote com 60 sleeves premium Dragon Shield Matte', '82786ffd-9936-4ece-8c81-2b88f62a61f8', 45.00, 'SEALED', @store_id, @manager),
('ac01-0002-0002-0002-000000000002', 'Ultra PRO Binder Charizard Edition', 'Binder 9-pocket com 20 páginas - Edição Charizard', 'a2ae25e2-0886-47ea-b1d5-f79b0c25283d', 120.00, 'SEALED', @store_id, @manager),
('ac01-0003-0003-0003-000000000003', 'Playmat Mewtwo Galaxy Edition', 'Playmat neoprene 60x35cm - Mewtwo Galaxy Edition', 'e4cb676a-05ac-486d-b5b0-e042fe3e232d', 85.00, 'SEALED', @store_id, @manager);

-- OTHER PRODUCTS
INSERT INTO product (id, name, description, other_product_id, price, product_condition, store_id, created_by) VALUES
('ot01-0001-0001-0001-000000000001', 'Pikachu Collection Box', 'Caixa temática Pikachu com deck e marcadores', 'cd88a9aa-2c79-47e6-ba50-e7f46c9fe43c', 150.00, 'SEALED', @store_id, @manager),
('ot01-0002-0002-0002-000000000002', 'Snorlax Collector Tin', 'Lata colecionável Snorlax com 4 boosters e carta promo', 'bb2f7281-1d70-4c71-a1e7-8cbda6ae048e', 95.00, 'SEALED', @store_id, @manager);


-- =====================================================
-- INVENTÁRIO DOS PRODUTOS
-- =====================================================

INSERT INTO inventory (product_id, quantity, created_by) VALUES
('e0a3bc33-904a-42ed-8221-b7231e70352b', 40, @manager),
('bc35ae69-387e-4bff-bbd5-5d23994e94c4', 5, @manager),
('95f91aed-1ee2-4f7d-a830-23906438377a', 3, @manager),
('f472ae78-4a51-4abe-89ad-1ae7297e0ae7', 8, @manager),
('85978edf-111a-481a-9d03-b615fa468d00', 12, @manager),
('0160abc5-be4e-448f-a2f2-4ec370dd48b5', 15, @manager),
('d5fb1c62-defd-48d7-bd14-f327c32eed54', 20, @manager),
('af11995d-438b-475c-b981-4b8599e1da32', 10, @manager),
('a0e16e17-71b6-4ab9-b744-2ee1432bf875', 25, @manager),
('265de878-5387-4941-a461-e6678a1cd9de', 18, @manager),
('05bf81da-9d0f-4f08-a96e-5b9886def0a0', 14, @manager),
('be682af0-a41f-4689-9ea4-8c2704ae6fb6', 22, @manager),
('30157d8e-8af8-447f-8eb0-6b014954ead7', 30, @manager),
('62c00e3e-255e-4f4b-b34b-77b9dc83ad3c', 16, @manager),
('44bef7e1-78cf-4aab-ad93-38adc83a9fd9', 19, @manager),
('9a791da8-1e79-4aa9-98da-5ea350c69670', 28, @manager),
('00e24eaa-9766-4056-a49c-77f9457e41b9', 7, @manager),
('70c80142-3437-497d-acde-09f645374b54', 11, @manager),
('dd32d1cc-7210-4662-a74e-1c20856f9749', 24, @manager),
('2bf51512-1baa-4283-a24e-5898a3678468', 33, @manager);

-- BOOSTER BOXES INVENTORY
INSERT INTO inventory (product_id, quantity, created_by) VALUES
('bb01-0001-0001-0001-000000000001', 2, @manager),  -- Base Set (raro, estoque baixo)
('bb01-0002-0002-0002-000000000002', 5, @manager),  -- Jungle JP
('bb01-0003-0003-0003-000000000003', 14, @manager), -- Scarlet & Violet (atual, estoque bom)
('bb01-0004-0004-0004-000000000004', 3, @manager),  -- Fossil (vintage, estoque baixo)
('bb01-0005-0005-0005-000000000005', 4, @manager),  -- Team Rocket
('bb01-0006-0006-0006-000000000006', 6, @manager),  -- Neo Genesis
('bb01-0007-0007-0007-000000000007', 8, @manager),  -- Diamond & Pearl
('bb01-0008-0008-0008-000000000008', 12, @manager); -- Sun & Moon

-- ACCESSORIES INVENTORY
INSERT INTO inventory (product_id, quantity, created_by) VALUES
('ac01-0001-0001-0001-000000000001', 45, @manager),  -- Dragon Shield Sleeves
('ac01-0002-0002-0002-000000000002', 32, @manager),  -- Ultra PRO Binder
('ac01-0003-0003-0003-000000000003', 27, @manager);  -- Playmat Mewtwo

-- OTHER PRODUCTS INVENTORY
INSERT INTO inventory (product_id, quantity, created_by) VALUES
('ot01-0001-0001-0001-000000000001', 18, @manager),  -- Pikachu Box
('ot01-0002-0002-0002-000000000002', 24, @manager);  -- Snorlax Tin


-- =====================================================
-- MOVIMENTAÇÕES DE ESTOQUE (90 DIAS)
-- =====================================================

-- ENTRADAS (COMPRAS E REPOSIÇÕES)
INSERT INTO inventory_movement
(id, product_id, user_id, quantity, unit_purchase_price, type, description, created_at, created_by)
VALUES
('70b7af7b-89c0-436e-adfe-f999d387b6d6', 'e0a3bc33-904a-42ed-8221-b7231e70352b', @manager, 20, 5.00, 'IN', 'Compra Pikachu', '2025-11-22 10:30:00', @manager),
('263959da-5e0a-4825-aa14-b37d80868bba', 'a0e16e17-71b6-4ab9-b744-2ee1432bf875', @manager, 15, 45.00, 'IN', 'Compra Alakazam', '2025-09-30 14:20:00', @manager),
('d2fdd377-69c8-451b-983b-1c6daf0bcbfd', '95f91aed-1ee2-4f7d-a830-23906438377a', @manager, 2, 300.00, 'IN', 'Compra Charizard', '2025-10-14 09:45:00', @manager),
('04d6e639-7517-4c64-b62c-839b5edb6f86', 'f472ae78-4a51-4abe-89ad-1ae7297e0ae7', @manager, 5, 75.00, 'IN', 'Compra Blastoise', '2025-12-04 11:15:00', @manager),
('2708ae57-62b0-4384-9f68-8d9c140a791d', '0160abc5-be4e-448f-a2f2-4ec370dd48b5', @manager, 8, 90.00, 'IN', 'Compra Venusaur', '2025-11-02 13:30:00', @manager),
('92114791-4f4b-4650-85b8-c2f848c0fdfc', 'be682af0-a41f-4689-9ea4-8c2704ae6fb6', @manager, 12, 22.00, 'IN', 'Compra Raichu', '2025-10-23 15:45:00', @manager),
('4d346bf4-229a-4485-8036-4745306d3322', '44bef7e1-78cf-4aab-ad93-38adc83a9fd9', @manager, 10, 24.00, 'IN', 'Compra Scyther', '2025-11-15 08:20:00', @manager),
('0aa09c10-b6a0-40dd-b39f-e667c15c47d4', '9a791da8-1e79-4aa9-98da-5ea350c69670', @manager, 15, 12.00, 'IN', 'Compra Wigglytuff', '2025-11-24 16:00:00', @manager),
('93d1e58c-07ae-4c6b-a192-c35ee5e206bc', '00e24eaa-9766-4056-a49c-77f9457e41b9', @manager, 5, 85.00, 'IN', 'Compra Dark Charizard', '2025-09-19 12:30:00', @manager),
('d73c970d-3aec-4f02-9d07-1f9c69c10573', 'dd32d1cc-7210-4662-a74e-1c20856f9749', @manager, 15, 18.00, 'IN', 'Compra Xerneas', '2025-09-20 10:15:00', @manager),
('0d8ce175-bce5-4da0-9d8a-35ea50ef6561', 'dd32d1cc-7210-4662-a74e-1c20856f9749', @manager, 10, 20.00, 'IN', 'Reposição Xerneas', '2025-11-16 14:45:00', @manager),
('e7af3d96-e533-453a-87d6-990d4360b221', '30157d8e-8af8-447f-8eb0-6b014954ead7', @manager, 20, 15.00, 'IN', 'Compra Machamp', '2025-11-02 09:00:00', @manager),
('caf51118-43c1-4dfa-8993-375b136dff5a', '85978edf-111a-481a-9d03-b615fa468d00', @manager, 8, 60.00, 'IN', 'Compra Blastoise MP', '2025-10-17 11:30:00', @manager),
('818eeda9-e30c-4093-820a-fa54ad4b1229', '2bf51512-1baa-4283-a24e-5898a3678468', @manager, 20, 20.00, 'IN', 'Compra Greninja', '2025-09-12 15:20:00', @manager),
('d16b0792-1631-462c-91ff-612dab08648f', '0160abc5-be4e-448f-a2f2-4ec370dd48b5', @manager, 7, 90.00, 'IN', 'Reposição Venusaur', '2025-09-22 13:45:00', @manager),
('406a6921-2454-4f70-bad7-1baba0f65bfb', '62c00e3e-255e-4f4b-b34b-77b9dc83ad3c', @manager, 10, 25.00, 'IN', 'Compra Snorlax', '2025-11-27 10:00:00', @manager),
('fc40da3a-bee6-4156-9c66-818890e766f7', '70c80142-3437-497d-acde-09f645374b54', @manager, 8, 32.00, 'IN', 'Compra Lugia', '2025-10-15 12:15:00', @manager),
('a81224b9-0c3c-4861-b986-fc154097933b', 'dd32d1cc-7210-4662-a74e-1c20856f9749', @manager, 5, 18.00, 'IN', 'Ajuste Xerneas', '2025-12-06 16:30:00', @manager),
('04e842bd-a8d8-41d3-a85d-2d9169cc145c', 'd5fb1c62-defd-48d7-bd14-f327c32eed54', @manager, 12, 55.00, 'IN', 'Compra Venusaur MP', '2025-09-17 08:45:00', @manager),
('e06f993a-57d4-4234-84bf-e7869c74c034', '2bf51512-1baa-4283-a24e-5898a3678468', @manager, 13, 25.00, 'IN', 'Reposição Greninja', '2025-10-09 14:00:00', @manager);


-- SAÍDAS (VENDAS)
INSERT INTO inventory_movement
(id, product_id, user_id, quantity, unit_sale_price, type, description, created_at, created_by)
VALUES
('58299ce9-ab1b-4de6-bd55-c8d7de7fd4cd', '95f91aed-1ee2-4f7d-a830-23906438377a', @staff, 1, 599.00, 'OUT', 'Venda online Charizard Mint', '2025-09-24 21:00:44', @staff),
('71756729-c2f9-4448-9e6e-72b0fb604f0c', '95f91aed-1ee2-4f7d-a830-23906438377a', @staff, 1, 599.00, 'OUT', 'Venda online Charizard Mint', '2025-11-07 21:00:44', @staff),
('7d69d810-04f1-48d5-a40b-bfe3dc156c00', '85978edf-111a-481a-9d03-b615fa468d00', @staff, 1, 120.00, 'OUT', 'Venda física Blastoise MP', '2025-11-01 21:00:44', @staff),
('ba5aefcc-8b3e-4730-b5be-def7f029d71e', 'f472ae78-4a51-4abe-89ad-1ae7297e0ae7', @staff, 2, 150.00, 'OUT', 'Venda online Blastoise LP', '2025-11-05 21:00:44', @staff),
('de9b5bda-773b-4372-aa7a-dc944f62ff64', '0160abc5-be4e-448f-a2f2-4ec370dd48b5', @staff, 2, 180.00, 'OUT', 'Venda física Venusaur Mint', '2025-10-19 21:00:44', @staff),
('9471c81a-9779-43ed-859e-d720eabc5f96', 'd5fb1c62-defd-48d7-bd14-f327c32eed54', @staff, 2, 110.00, 'OUT', 'Venda reserva Venusaur MP', '2025-10-28 21:00:44', @staff),
('63a86116-fe49-44a1-a3f6-040a93a405f9', '05bf81da-9d0f-4f08-a96e-5b9886def0a0', @staff, 2, 80.00, 'OUT', 'Venda reserva Mewtwo MP', '2025-10-06 21:00:44', @staff),
('7d9cbdc9-5df9-472e-b634-fda8c9eca672', '70c80142-3437-497d-acde-09f645374b54', @staff, 4, 65.00, 'OUT', 'Venda online Lugia', '2025-10-02 21:00:44', @staff),
('aea900c3-42c2-4f72-a13d-5c29a2c43281', '70c80142-3437-497d-acde-09f645374b54', @staff, 8, 65.00, 'OUT', 'Venda física Lugia', '2025-10-12 21:00:44', @staff),
('a26a36fd-3fdf-4507-8463-ad3d1de39978', 'dd32d1cc-7210-4662-a74e-1c20856f9749', @staff, 8, 35.00, 'OUT', 'Venda física Xerneas', '2025-10-19 21:00:44', @staff),
('61247500-6591-4b92-85e1-5b4ba86157d0', '2bf51512-1baa-4283-a24e-5898a3678468', @staff, 9, 40.00, 'OUT', 'Venda física Greninja', '2025-10-20 21:00:44', @staff),
('48b1ac44-a02a-46a3-b962-7946db1bce3e', '2bf51512-1baa-4283-a24e-5898a3678468', @staff, 8, 40.00, 'OUT', 'Venda reserva Greninja', '2025-10-22 21:00:44', @staff),
('99552b85-754b-41fa-9af1-4fcad0a40c3c', '44bef7e1-78cf-4aab-ad93-38adc83a9fd9', @staff, 10, 48.00, 'OUT', 'Venda online Scyther', '2025-10-13 21:00:44', @staff),
('bc446724-6946-4f14-89cd-a49d62e614bd', '30157d8e-8af8-447f-8eb0-6b014954ead7', @staff, 2, 28.00, 'OUT', 'Venda física Machamp', '2025-09-20 21:00:44', @staff),
('01b1ec92-7943-434c-a2aa-d9301b818f38', '62c00e3e-255e-4f4b-b34b-77b9dc83ad3c', @staff, 1, 50.00, 'OUT', 'Venda reserva Snorlax', '2025-11-02 21:00:44', @staff),
('93845bd9-95ce-4bd5-8f96-df52e5c4beaa', '9a791da8-1e79-4aa9-98da-5ea350c69670', @staff, 8, 24.00, 'OUT', 'Venda online Wigglytuff', '2025-09-29 21:00:44', @staff),
('bbafaf0b-2cca-45e6-9c7d-e90c3753b8ce', '9a791da8-1e79-4aa9-98da-5ea350c69670', @staff, 18, 24.00, 'OUT', 'Venda reserva Wigglytuff', '2025-09-14 21:00:44', @staff),
('f9591ac8-ba53-4b71-ae3a-55f8e75e9f03', 'a0e16e17-71b6-4ab9-b744-2ee1432bf875', @staff, 1, 95.00, 'OUT', 'Venda online Alakazam Mint', '2025-10-06 21:00:44', @staff),
('fb899818-164a-4a38-9580-6caba6da5d7d', '00e24eaa-9766-4056-a49c-77f9457e41b9', @staff, 2, 170.00, 'OUT', 'Venda reserva Dark Charizard', '2025-11-07 21:00:44', @staff),
('059ce9dd-4e06-4310-801b-e07e8f5d7a80', 'bc35ae69-387e-4bff-bbd5-5d23994e94c4', @staff, 2, 450.00, 'OUT', 'Venda online Charizard LP', '2025-10-11 21:00:44', @staff);

-- ENTRADAS - ACCESSORIES & OTHER PRODUCTS
INSERT INTO inventory_movement
(id, product_id, user_id, quantity, unit_purchase_price, type, description, created_at, created_by)
VALUES
('acc-in-0001-0001-0001-000000000001', 'ac01-0001-0001-0001-000000000001', @manager, 50, 30.00, 'IN', 'Compra Dragon Shield Sleeves', '2025-10-05 10:00:00', @manager),
('acc-in-0002-0002-0002-000000000002', 'ac01-0002-0002-0002-000000000002', @manager, 35, 80.00, 'IN', 'Compra Ultra  PRO Binder', '2025-10-12 14:30:00', @manager),
('acc-in-0003-0003-0003-000000000003', 'ac01-0003-0003-0003-000000000003', @manager, 30, 60.00, 'IN', 'Compra Playmat Mewtwo', '2025-10-18 09:15:00', @manager),
('oth-in-0001-0001-0001-000000000001', 'ot01-0001-0001-0001-000000000001', @manager, 25, 110.00, 'IN', 'Compra Pikachu Box', '2025-09-28 11:45:00', @manager),
('oth-in-0002-0002-0002-000000000002', 'ot01-0002-0002-0002-000000000002', @manager, 30, 70.00, 'IN', 'Compra Snorlax Tin', '2025-10-08 15:20:00', @manager);

-- SAÍDAS - ACCESSORIES & OTHER PRODUCTS
INSERT INTO inventory_movement
(id, product_id, user_id, quantity, unit_sale_price, type, description, created_at, created_by)
VALUES
('acc-out-0001-0001-0001-000000000001', 'ac01-0001-0001-0001-000000000001', @staff, 5, 45.00, 'OUT', 'Venda online Sleeves', '2025-10-20 16:30:00', @staff),
('acc-out-0002-0002-0002-000000000002', 'ac01-0002-0002-0002-000000000002', @staff, 3, 120.00, 'OUT', 'Venda física Binder', '2025-10-25 10:45:00', @staff),
('acc-out-0003-0003-0003-000000000003', 'ac01-0003-0003-0003-000000000003', @staff, 3, 85.00, 'OUT', 'Venda reserva Playmat', '2025-11-01 14:20:00', @staff),
('oth-out-0001-0001-0001-000000000001', 'ot01-0001-0001-0001-000000000001', @staff, 1, 150.00, 'OUT', 'Venda online Pikachu Box', '2025-10-15 12:00:00', @staff),
('oth-out-0002-0002-0002-000000000002', 'ot01-0002-0002-0002-000000000002', @staff, 1, 95.00, 'OUT', 'Venda física Snorlax Tin', '2025-10-22 09:30:00', @staff);


-- AJUSTES (DANOS, CORREÇÕES, PROMOÇÕES)
INSERT INTO inventory_movement
(id, product_id, user_id, quantity, type, description, created_at, created_by)
VALUES
('6a31f442-06ba-45b4-a009-13df78d907df', 'e0a3bc33-904a-42ed-8221-b7231e70352b', @manager, 1, 'ADJUST', 'Carta danificada durante manuseio', '2025-10-02 21:00:44', @manager),
('8a68861f-57ba-45aa-ae30-668acd88969b', '95f91aed-1ee2-4f7d-a830-23906438377a', @manager, 3, 'ADJUST', 'Ajuste de contagem física', '2025-09-15 21:00:44', @manager),
('85b0b6e8-d9a3-460c-a41d-0eb69e9837ca', 'f472ae78-4a51-4abe-89ad-1ae7297e0ae7', @manager, 1, 'ADJUST', 'Produto defeituoso devolvido', '2025-09-24 21:00:44', @manager),
('9a933427-d49d-4936-9fe4-267c999d7b5b', '0160abc5-be4e-448f-a2f2-4ec370dd48b5', @manager, 2, 'ADJUST', 'Produto defeituoso devolvido', '2025-11-05 21:00:44', @manager),
('073517dc-4c3f-4520-ba34-1fd1d3d78b4d', 'd5fb1c62-defd-48d7-bd14-f327c32eed54', @manager, 2, 'ADJUST', 'Dano de transporte', '2025-10-06 21:00:44', @manager),
('a42b152e-fe94-4274-9fc3-b5dc6b90f18f', 'dd32d1cc-7210-4662-a74e-1c20856f9749', @manager, 2, 'ADJUST', 'Doação promocional', '2025-10-11 21:00:44', @manager),
('de5e60fb-5b41-4c67-8414-71776df487dc', 'be682af0-a41f-4689-9ea4-8c2704ae6fb6', @manager, 1, 'ADJUST', 'Produto perdido', '2025-11-05 21:00:44', @manager),
('133c2723-197a-414d-a197-cb59a8886184', '30157d8e-8af8-447f-8eb0-6b014954ead7', @manager, 2, 'ADJUST', 'Carta danificada durante manuseio', '2025-10-25 21:00:44', @manager),
('bb16221f-ef1a-4e52-b0f6-b674fa99e485', '44bef7e1-78cf-4aab-ad93-38adc83a9fd9', @manager, 2, 'ADJUST', 'Devolução de produto', '2025-09-20 21:00:44', @manager),
('4ece2a5e-4e38-442f-8ae5-213b3c15d5a6', 'bc35ae69-387e-4bff-bbd5-5d23994e94c4', @manager, 1, 'ADJUST', 'Ajuste de contagem física', '2025-11-01 21:00:44', @manager);

-- Total de produtos: 20
-- Total de movimentações IN: 20
-- Total de movimentações OUT: 20
-- Total de ajustes: 10
-- Total geral de movimentações: 50

-- =====================================================
-- AUDITORIAS DE INVENTÁRIO (10 REGISTROS)
-- =====================================================

INSERT INTO inventory_audit
(id, product_id, movement_id, user_id, movement_type, operation, quantity, quantity_before, quantity_after, timestamp, description, status)
VALUES
(UUID(), '95f91aed-1ee2-4f7d-a830-23906438377a', '58299ce9-ab1b-4de6-bd55-c8d7de7fd4cd', @staff, 'OUT', 'CREATE',  1, 3, 2, '2025-09-24 21:00:44', 'Venda online Charizard Mint', 'PROCESSED'),
(UUID(), 'f472ae78-4a51-4abe-89ad-1ae7297e0ae7', 'ba5aefcc-8b3e-4730-b5be-def7f029d71e', @staff, 'OUT', 'CREATE',  2, 10, 8, '2025-11-05 21:00:44', 'Venda online Blastoise LP', 'PROCESSED'),
(UUID(), '0160abc5-be4e-448f-a2f2-4ec370dd48b5', 'de9b5bda-773b-4372-aa7a-dc944f62ff64', @staff, 'OUT', 'CREATE',  2, 17, 15, '2025-10-19 21:00:44', 'Venda física Venusaur Mint', 'PROCESSED'),
(UUID(), 'dd32d1cc-7210-4662-a74e-1c20856f9749', 'a26a36fd-3fdf-4507-8463-ad3d1de39978', @staff, 'OUT', 'CREATE',  8, 32, 24, '2025-10-19 21:00:44', 'Venda física Xerneas', 'PROCESSED'),
(UUID(), '2bf51512-1baa-4283-a24e-5898a3678468', '61247500-6591-4b92-85e1-5b4ba86157d0', @staff, 'OUT', 'CREATE',  9, 42, 33, '2025-10-20 21:00:44', 'Venda física Greninja', 'PROCESSED'),
(UUID(), '44bef7e1-78cf-4aab-ad93-38adc83a9fd9', '99552b85-754b-41fa-9af1-4fcad0a40c3c', @staff, 'OUT', 'CREATE',  10, 29, 19, '2025-10-13 21:00:44', 'Venda online Scyther', 'PROCESSED'),
(UUID(), 'e0a3bc33-904a-42ed-8221-b7231e70352b', '70b7af7b-89c0-436e-adfe-f999d387b6d6', @manager, 'IN', 'CREATE', 20, 20, 40, '2025-11-22 10:30:00', 'Compra Pikachu', 'PROCESSED'),
(UUID(), 'a0e16e17-71b6-4ab9-b744-2ee1432bf875', '263959da-5e0a-4825-aa14-b37d80868bba', @manager, 'IN', 'CREATE', 15, 10, 25, '2025-09-30 14:20:00', 'Compra Alakazam', 'PROCESSED'),
(UUID(), '95f91aed-1ee2-4f7d-a830-23906438377a', '8a68861f-57ba-45aa-ae30-668acd88969b', @manager, 'ADJUST', 'CREATE', 3, 6, 3, '2025-09-15 21:00:44', 'Ajuste de contagem física', 'PROCESSED'),
(UUID(), 'dd32d1cc-7210-4662-a74e-1c20856f9749', 'a42b152e-fe94-4274-9fc3-b5dc6b90f18f', @manager, 'ADJUST', 'CREATE', 2, 26, 24, '2025-10-11 21:00:44', 'Doação promocional', 'PROCESSED');


SELECT
            p.id AS product_id,
            p.name AS product_name,
            p.description AS product_description,
            ROUND(AVG(CASE WHEN im.type = 'OUT' THEN im.unit_sale_price END), 2) AS avg_sale_price,
            MAX(CASE WHEN im.type = 'OUT' THEN im.unit_sale_price END) AS max_sale_price,
            MIN(CASE WHEN im.type = 'OUT' THEN im.unit_sale_price END) AS min_sale_price,
            p.price AS current_sale_price,
            ROUND(p.price - AVG(CASE WHEN im.type = 'OUT' THEN im.unit_sale_price END), 2) AS difference_from_avg,
            ROUND(((p.price - AVG(CASE WHEN im.type = 'OUT' THEN im.unit_sale_price END)) /
                   AVG(CASE WHEN im.type = 'OUT' THEN im.unit_sale_price END)) * 100, 2) AS percentage_change,
            COALESCE(i.quantity, 0) AS current_stock,
            MAX(im.created_at) AS last_sale
        FROM product p
        LEFT JOIN inventory i ON i.product_id = p.id
        LEFT JOIN inventory_movement im ON im.product_id = p.id
        WHERE im.type = 'OUT'
        GROUP BY 
            p.id, p.name, p.description, p.price, i.quantity
        HAVING p.price != ROUND(AVG(CASE WHEN im.type = 'OUT' THEN im.unit_sale_price END), 2)
        ORDER BY percentage_change DESC;
        
        -- ====================================================================================================================================================
        -- ====================================================================================================================================================

-- Cadastrando nova sessão para usuário
UPDATE user
SET id = '44d7f605-d3e4-11f0-8fdc-001a7dda7111'
WHERE email = 'admin@meruru.com';

INSERT INTO user_session (
    id,
    user_id,
    created_at,
    ended_at,
    active
)
VALUES 
(
    'aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee',
    '44d7f605-d3e4-11f0-8fdc-001a7dda7111', -- ADMIN REAL
    NOW(),
    NOW(), -- NOT NULL
    1
);



-- cadastrando novos modelos de cartas por conta do lançamento de coleção;
INSERT INTO card (
    id, title, season, pokemon_type, collection_id, code, rarity, nationality, created_at
) VALUES
-- 1
('c9c15fa3-1c87-4171-bc05-7fcfdf3a8ce8', 'Pineco', 'Ninth', 'GRASS',
 'e8f9a0b1-c2d3-4e4f-5a6b-7c8d9e0f1a2b', 'sv1-1', 'COMMON', 'USA', '2025-01-01 10:00:00'),

-- 2
('02c69d1b-42c5-4f7d-9db9-0760d3869d1f', 'Meowscarada', 'Ninth', 'GRASS',
 'e8f9a0b1-c2d3-4e4f-5a6b-7c8d9e0f1a2b', 'sv1-15', 'RARE', 'USA', '2025-01-01 10:01:00'),

-- 3
('c8637cf5-ff3d-4319-8036-5e76d950917d', 'Arcanine ex', 'Ninth', 'FIRE',
 'e8f9a0b1-c2d3-4e4f-5a6b-7c8d9e0f1a2b', 'sv1-32', 'RARE_HOLO_EX', 'USA', '2025-01-01 10:02:00'),

-- 4
('d7eea8c8-5b53-44f9-b0d0-2acbae0c9423', 'Gyarados ex', 'Ninth', 'WATER',
 'e8f9a0b1-c2d3-4e4f-5a6b-7c8d9e0f1a2b', 'sv1-45', 'RARE_HOLO_EX', 'USA', '2025-01-01 10:03:00'),

-- 5
('ae58bc42-5832-4454-9bbc-eae35a4354e7', 'Magnezone ex', 'Ninth', 'LIGHTNING',
 'e8f9a0b1-c2d3-4e4f-5a6b-7c8d9e0f1a2b', 'sv1-65', 'RARE_HOLO_EX', 'USA', '2025-01-01 10:04:00'),

-- 6
('3c55080d-8564-4908-9590-a85e0c4a96d5', 'Gardevoir ex', 'Ninth', 'PSYCHIC',
 'e8f9a0b1-c2d3-4e4f-5a6b-7c8d9e0f1a2b', 'sv1-86', 'RARE_HOLO_EX', 'USA', '2025-01-01 10:05:00'),

-- 7
('3a8e09cd-d601-429e-aaa9-32be17851ef1', 'Annihilape', 'Ninth', 'FIGHTING',
 'e8f9a0b1-c2d3-4e4f-5a6b-7c8d9e0f1a2b', 'sv1-109', 'RARE', 'USA', '2025-01-01 10:06:00'),

-- 8
('db13fce0-6598-40e2-9280-002062b6312f', 'Toxicroak ex', 'Ninth', 'DARKNESS',
 'e8f9a0b1-c2d3-4e4f-5a6b-7c8d9e0f1a2b', 'sv1-131', 'RARE_HOLO_EX', 'USA', '2025-01-01 10:07:00'),

-- 9
('7a910576-249a-468b-a3be-5b244edac18f', 'Iron Treads ex', 'Ninth', 'METAL',
 'e8f9a0b1-c2d3-4e4f-5a6b-7c8d9e0f1a2b', 'sv1-143', 'RARE_HOLO_EX', 'USA', '2025-01-01 10:08:00'),

-- 10
('17394d74-7c98-4daf-8bbe-4316348f8ab9', 'Oinkologne ex', 'Ninth', 'COLORLESS',
 'e8f9a0b1-c2d3-4e4f-5a6b-7c8d9e0f1a2b', 'sv1-158', 'RARE_HOLO_EX', 'USA', '2025-01-01 10:09:00'),

-- 11
('8b08715f-78a1-4f55-b4b8-45c12e44489a', 'Tarountula', 'Ninth', 'GRASS',
 'e8f9a0b1-c2d3-4e4f-5a6b-7c8d9e0f1a2b', 'sv1-199', 'COMMON', 'USA', '2025-01-01 10:10:00'),

-- 12
('fb9b624a-3638-41f9-bdb5-408c864df2d5', 'Slowpoke', 'Ninth', 'WATER',
 'e8f9a0b1-c2d3-4e4f-5a6b-7c8d9e0f1a2b', 'sv1-204', 'COMMON', 'USA', '2025-01-01 10:11:00'),

-- 13
('58de8193-6694-46bf-89c3-988eb1c89c2e', 'Pachirisu', 'Ninth', 'LIGHTNING',
 'e8f9a0b1-c2d3-4e4f-5a6b-7c8d9e0f1a2b', 'sv1-208', 'UNCOMMON', 'USA', '2025-01-01 10:12:00'),

-- 14
('93ad26c5-5e79-430c-a76e-fc99294077ba', 'Riolu', 'Ninth', 'FIGHTING',
 'e8f9a0b1-c2d3-4e4f-5a6b-7c8d9e0f1a2b', 'sv1-215', 'COMMON', 'USA', '2025-01-01 10:13:00'),

-- 15
('9f81c993-be1b-4eed-9504-6c3887896b83', 'Armarouge', 'Ninth', 'FIRE',
 'e8f9a0b1-c2d3-4e4f-5a6b-7c8d9e0f1a2b', 'sv1-203', 'RARE', 'USA', '2025-01-01 10:14:00');
 
 
 -- ==========================================
-- OTHER PRODUCTS (BOOSTER BOX, BOOSTER, ACCESSORY, OTHER)
-- ==========================================

INSERT INTO other_product (
    id, type, nationality, package_contents, extra_info,
    created_by, created_at, updated_by, updated_at, deleted
)
VALUES
-- BOOSTER BOXES ------------------------------
('decf12ff-357a-4dd2-a070-cc765a8f98c1', 'BOOSTER_BOX', 'USA', '36 Boosters (SV1)', 'Primeira edição', NULL, NOW(), NULL, NOW(), 0),
('37d0b374-2e53-4503-a473-44aba7f2dbf5', 'BOOSTER_BOX', 'JPN', '30 Boosters (SV2)', 'Versão japonesa', NULL, NOW(), NULL, NOW(), 0),

-- BOOSTERS ----------------------------------
('df366752-0585-4927-be24-4f09d5bb07b0', 'OTHER', 'USA', '1 Booster SV1', 'Contém 10 cartas', NULL, NOW(), NULL, NOW(), 0),
('80c5452e-9391-4046-a8e9-7d87083054cc', 'OTHER', 'JPN', '1 Booster SV2', 'Versão japonesa, 5 cartas', NULL, NOW(), NULL, NOW(), 0),

-- ACCESSORIES -------------------------------
('a0c57655-17d8-4863-aee6-b8814162c364', 'ACCESSORY', 'USA', 'Pokémon Sleeves (65)', 'Tema Miraidon', NULL, NOW(), NULL, NOW(), 0),
('6cad7236-f5db-41de-a448-dd458979ce26', 'ACCESSORY', 'USA', 'Deck Box', 'Tema Koraidon', NULL, NOW(), NULL, NOW(), 0),
('01f0e8c7-0fec-48c2-88d4-e93157c72fe1', 'ACCESSORY', 'USA', 'Playmat Oficial Pokémon', 'Scarlet & Violet', NULL, NOW(), NULL, NOW(), 0),

-- OUTROS PRODUTOS ---------------------------
('d0ce8e68-cbdb-4e4e-971f-7c645e08d725', 'OTHER', 'USA', 'Pokémon Pin Collection', 'Pin exclusivo Pikachu', NULL, NOW(), NULL, NOW(), 0),
('1fd9d985-a5f0-46bc-8451-345877b931d3', 'OTHER', 'USA', 'Pokémon Mini Figure', 'Figura Gyarados', NULL, NOW(), NULL, NOW(), 0);

 
 
 -- Novos produtos chegando na loja após aquisição
 INSERT INTO product (
    id, name, description, card_id, other_product_id, store_id, price,
    product_condition, created_at, deleted
) VALUES
('36461c9c-d495-11f0-a8b3-325096b39f47', 'Pineco', 'Carta SV1', 'c9c15fa3-1c87-4171-bc05-7fcfdf3a8ce8', NULL, NULL, 1.00, 'MINT', '2025-01-02', 0),
('3b09864c-d495-11f0-82ae-325096b39f47', 'Meowscarada', 'Carta SV1', '02c69d1b-42c5-4f7d-9db9-0760d3869d1f', NULL, NULL, 3.50, 'MINT', '2025-01-02', 0),
('3dc7e0a4-d495-11f0-9386-325096b39f47', 'Arcanine ex', 'Carta SV1', 'c8637cf5-ff3d-4319-8036-5e76d950917d', NULL, NULL, 12.00, 'MINT', '2025-01-02', 0),
('44ad0a66-d495-11f0-940c-325096b39f47', 'Gyarados ex', 'Carta SV1', 'd7eea8c8-5b53-44f9-b0d0-2acbae0c9423', NULL, NULL, 11.00, 'MINT', '2025-01-02', 0),
('5911154c-39a3-43d0-9cd3-1ffc56b944e3', 'Magnezone ex', 'Carta SV1', 'ae58bc42-5832-4454-9bbc-eae35a4354e7', NULL, NULL, 10.00, 'MINT', '2025-01-02', 0),
('3f029c2c-ef94-46da-9d50-a8e20d61eb1b', 'Gardevoir ex', 'Carta SV1', '3c55080d-8564-4908-9590-a85e0c4a96d5', NULL, NULL, 14.00, 'MINT', '2025-01-02', 0),
('5fcc2746-8012-4c0a-a1f4-f3a3c4dce288', 'Annihilape', 'Carta SV1', '3a8e09cd-d601-429e-aaa9-32be17851ef1', NULL, NULL, 4.00, 'MINT', '2025-01-02', 0),
('645a2d87-4b53-4b74-ab35-fcf8ca43aff1', 'Toxicroak ex', 'Carta SV1', 'db13fce0-6598-40e2-9280-002062b6312f', NULL, NULL, 8.00, 'MINT', '2025-01-02', 0),
('62b330a1-e6c6-4781-925b-30bae7c902ae', 'Iron Treads ex', 'Carta SV1', '7a910576-249a-468b-a3be-5b244edac18f', NULL, NULL, 7.00, 'MINT', '2025-01-02', 0),
('8e54f508-ca69-431e-873a-3dc99c7090c2', 'Oinkologne ex', 'Carta SV1', '17394d74-7c98-4daf-8bbe-4316348f8ab9', NULL, NULL, 6.00, 'MINT', '2025-01-02', 0),
('882d70e1-3a6c-4df4-af4a-ce11bc8dc882', 'Tarountula', 'Carta SV1', '8b08715f-78a1-4f55-b4b8-45c12e44489a', NULL, NULL, 1.00, 'MINT', '2025-01-02', 0),
('8aa44554-5fef-4204-8c23-b727f2e2833c', 'Slowpoke', 'Carta SV1', 'fb9b624a-3638-41f9-bdb5-408c864df2d5', NULL, NULL, 1.00, 'MINT', '2025-01-02', 0),
('791f157d-c65c-4f53-8af7-3eb9d878b39c', 'Pachirisu', 'Carta SV1', '58de8193-6694-46bf-89c3-988eb1c89c2e', NULL, NULL, 2.00, 'MINT', '2025-01-02', 0),
('d2487f63-60cd-4081-823e-c0ef176d80e1', 'Riolu', 'Carta SV1', '93ad26c5-5e79-430c-a76e-fc99294077ba', NULL, NULL, 1.50, 'MINT', '2025-01-02', 0),
('1a26197d-8b1a-4f8a-8819-9500afec8f81', 'Armarouge', 'Carta SV1', '9f81c993-be1b-4eed-9504-6c3887896b83', NULL, NULL, 3.00, 'MINT', '2025-01-02', 0);

-- ==========================================
-- PRODUCTS QUE NÃO SÃO CARTAS
-- ==========================================

INSERT INTO product (
    id, name, description, card_id, other_product_id, store_id,
    price, product_condition,
    created_by, created_at, updated_by, updated_at, deleted
)
VALUES
-- BOOSTER BOXES --------------------------------
('5ccc7f94-003d-49a0-8ea0-5560fc74cf53', 'Booster Box SV1 (Scarlet & Violet)', 'Box com 36 boosters SV1', 
 NULL, 'decf12ff-357a-4dd2-a070-cc765a8f98c1', NULL,
 850.00, 'SEALED',
 NULL, NOW(), NULL, NOW(), 0),

('a8a22009-9e5c-457a-8622-9e88e7bde048', 'Booster Box SV2', 'Box japonês com 30 boosters', 
 NULL, '37d0b374-2e53-4503-a473-44aba7f2dbf5', NULL,
 720.00, 'SEALED',
 NULL, NOW(), NULL, NOW(), 0),

-- BOOSTERS --------------------------------------
('64b4a974-bda5-46e8-87ec-47cba75e72f3', 'Booster SV1', 'Booster americano – 10 cartas',
 NULL, 'df366752-0585-4927-be24-4f09d5bb07b0', NULL,
 18.00, 'SEALED',
 NULL, NOW(), NULL, NOW(), 0),

('1e6d23b6-ce51-4f82-8c0b-a947c02e9b67', 'Booster SV2 (JPN)', 'Booster japonês – 5 cartas',
 NULL, '80c5452e-9391-4046-a8e9-7d87083054cc', NULL,
 12.00, 'SEALED',
 NULL, NOW(), NULL, NOW(), 0),

-- ACCESSORIES -----------------------------------
('4364bfdd-7c7b-436b-8c89-a1cd4cfe6815', 'Sleeves Pokémon — Miraidon', 'Pacote com 65 sleeves oficiais',
 NULL, 'a0c57655-17d8-4863-aee6-b8814162c364', NULL,
 45.00, 'SEALED',
 NULL, NOW(), NULL, NOW(), 0),

('1d78603a-d495-11f0-80a0-325096b39f47', 'Deck Box — Koraidon', 'Deck box rígido tema SV1',
 NULL, '6cad7236-f5db-41de-a448-dd458979ce26', NULL,
 35.00, 'USED',
 NULL, NOW(), NULL, NOW(), 0),

('21d19e9e-d495-11f0-a21f-325096b39f47', 'Playmat Pokémon SV1', 'Playmat oficial Scarlet & Violet',
 NULL, '01f0e8c7-0fec-48c2-88d4-e93157c72fe1', NULL,
 110.00, 'USED',
 NULL, NOW(), NULL, NOW(), 0),

-- OUTROS -----------------------------------------
('26a5866a-d495-11f0-808b-325096b39f47', 'Pokémon Pin Collection — Pikachu', 'Kit com pin metálico + display',
 NULL, 'd0ce8e68-cbdb-4e4e-971f-7c645e08d725', NULL,
 99.00, 'SEALED',
 NULL, NOW(), NULL, NOW(), 0),

('2fb2353c-d495-11f0-be6e-325096b39f47', 'Pokémon Mini Figure — Gyarados', 'Mini figura oficial Pokémon Center',
 NULL, '1fd9d985-a5f0-46bc-8451-345877b931d3', NULL,
 120.00, 'MINT',
 NULL, NOW(), NULL, NOW(), 0);


INSERT INTO inventory_movement
(id, product_id, type, quantity, created_at, user_id, deleted)
VALUES

-- ======================================
-- 📅 JANEIRO 2025 — Entradas iniciais
-- ======================================
('IMOV-001', '36461c9c-d495-11f0-a8b3-325096b39f47', 'IN', 12, '2025-01-03 10:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-002', '3b09864c-d495-11f0-82ae-325096b39f47', 'IN',  6, '2025-01-03 10:05:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-003', '3dc7e0a4-d495-11f0-9386-325096b39f47', 'IN',  4, '2025-01-03 10:10:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-004', '44ad0a66-d495-11f0-940c-325096b39f47', 'IN',  5, '2025-01-03 10:15:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-005', '5911154c-39a3-43d0-9cd3-1ffc56b944e3', 'IN',  6, '2025-01-03 10:20:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-006', '3f029c2c-ef94-46da-9d50-a8e20d61eb1b', 'IN',  3, '2025-01-03 10:25:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-007', '5fcc2746-8012-4c0a-a1f4-f3a3c4dce288', 'IN',  5, '2025-01-03 10:30:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-008', '645a2d87-4b53-4b74-ab35-fcf8ca43aff1', 'IN',  4, '2025-01-03 10:35:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-009', '62b330a1-e6c6-4781-925b-30bae7c902ae', 'IN',  3, '2025-01-03 10:40:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-010', '8e54f508-ca69-431e-873a-3dc99c7090c2', 'IN',  6, '2025-01-03 10:45:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),

('IMOV-011', '882d70e1-3a6c-4df4-af4a-ce11bc8dc882', 'IN', 15, '2025-01-03 10:50:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-012', '8aa44554-5fef-4204-8c23-b727f2e2833c', 'IN', 10, '2025-01-03 10:55:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-013', '791f157d-c65c-4f53-8af7-3eb9d878b39c', 'IN',  8, '2025-01-03 11:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-014', 'd2487f63-60cd-4081-823e-c0ef176d80e1', 'IN',  9, '2025-01-03 11:05:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-015', '1a26197d-8b1a-4f8a-8819-9500afec8f81', 'IN',  5, '2025-01-03 11:10:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),

-- Pequenas vendas do fim de janeiro
('IMOV-016', '3dc7e0a4-d495-11f0-9386-325096b39f47', 'OUT', 1, '2025-01-28 14:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-017', '5911154c-39a3-43d0-9cd3-1ffc56b944e3', 'OUT', 1, '2025-01-29 15:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-018', '8e54f508-ca69-431e-873a-3dc99c7090c2', 'OUT', 1, '2025-01-30 12:30:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),

-- ======================================
-- 📅 FEVEREIRO 2025 — Vendas + Ajustes
-- ======================================
('IMOV-019', '36461c9c-d495-11f0-a8b3-325096b39f47', 'OUT', 2, '2025-02-10 13:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-020', '8aa44554-5fef-4204-8c23-b727f2e2833c', 'OUT', 1, '2025-02-12 09:30:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-021', '882d70e1-3a6c-4df4-af4a-ce11bc8dc882', 'OUT', 3, '2025-02-15 16:20:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),

-- Ajuste negativo
('IMOV-022', '3b09864c-d495-11f0-82ae-325096b39f47', 'ADJUST', -1, '2025-02-20 11:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),

-- Reabastecimento
('IMOV-023', '3f029c2c-ef94-46da-9d50-a8e20d61eb1b', 'IN', 2, '2025-02-25 10:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),

-- ======================================
-- 📅 MARÇO 2025 — Mês forte de vendas
-- ======================================
('IMOV-024', '3dc7e0a4-d495-11f0-9386-325096b39f47', 'OUT', 2, '2025-03-05 14:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-025', '44ad0a66-d495-11f0-940c-325096b39f47', 'OUT', 2, '2025-03-08 11:50:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-026', '5911154c-39a3-43d0-9cd3-1ffc56b944e3', 'OUT', 1, '2025-03-10 10:30:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-027', '62b330a1-e6c6-4781-925b-30bae7c902ae', 'OUT', 1, '2025-03-12 15:15:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-028', '791f157d-c65c-4f53-8af7-3eb9d878b39c', 'OUT', 2, '2025-03-18 16:45:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-029', 'd2487f63-60cd-4081-823e-c0ef176d80e1', 'OUT', 1, '2025-03-22 18:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),

-- Ajuste positivo
('IMOV-030', '882d70e1-3a6c-4df4-af4a-ce11bc8dc882', 'ADJUST', 1, '2025-03-28 09:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),

-- ======================================
-- 📅 ABRIL 2025 — Reposição e vendas leves
-- ======================================
('IMOV-031', '36461c9c-d495-11f0-a8b3-325096b39f47', 'IN', 5, '2025-04-02 10:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-032', '1a26197d-8b1a-4f8a-8819-9500afec8f81', 'IN', 3, '2025-04-02 10:10:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),

('IMOV-033', '645a2d87-4b53-4b74-ab35-fcf8ca43aff1', 'OUT', 1, '2025-04-09 11:40:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-034', '882d70e1-3a6c-4df4-af4a-ce11bc8dc882', 'OUT', 2, '2025-04-15 13:50:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),

('IMOV-035', '3f029c2c-ef94-46da-9d50-a8e20d61eb1b', 'ADJUST', -1, '2025-04-20 17:30:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),

-- ======================================
-- 📅 MAIO 2025 — Movimento misto
-- ======================================
('IMOV-036', '5911154c-39a3-43d0-9cd3-1ffc56b944e3', 'IN', 3, '2025-05-03 09:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-037', '8e54f508-ca69-431e-873a-3dc99c7090c2', 'IN', 4, '2025-05-03 09:10:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),

('IMOV-038', '44ad0a66-d495-11f0-940c-325096b39f47', 'OUT', 1, '2025-05-10 16:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-039', '3b09864c-d495-11f0-82ae-325096b39f47', 'OUT', 1, '2025-05-12 10:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-040', '36461c9c-d495-11f0-a8b3-325096b39f47', 'OUT', 3, '2025-05-14 14:30:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IMOV-041', '1a26197d-8b1a-4f8a-8819-9500afec8f81', 'OUT', 1, '2025-05-18 11:15:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),

('IMOV-042', '8aa44554-5fef-4204-8c23-b727f2e2833c', 'ADJUST', 1, '2025-05-25 12:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0);

-- ------------------------------------------------- Inserts diferenciados

INSERT INTO inventory_movement
(id, product_id, type, quantity, created_at, user_id, deleted)
VALUES

-- =====================================================================
-- 📅 JANEIRO — Estoque inicial + primeiras vendas leves
-- =====================================================================
('IM-2025-001', '36461c9c-d495-11f0-a8b3-325096b39f47', 'IN', 15, '2025-01-05 10:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IM-2025-002', '3b09864c-d495-11f0-82ae-325096b39f47', 'IN', 10, '2025-01-05 10:05:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IM-2025-003', '3dc7e0a4-d495-11f0-9386-325096b39f47', 'IN', 8,  '2025-01-05 10:10:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IM-2025-004', '5ccc7f94-003d-49a0-8ea0-5560fc74cf53', 'IN', 3, '2025-01-06 09:00:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),
('IM-2025-005', '64b4a974-bda5-46e8-87ec-47cba75e72f3', 'IN', 40,'2025-01-06 09:10:00', '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 0),

('IM-2025-006', '36461c9c-d495-11f0-a8b3-325096b39f47', 'OUT', 2,'2025-01-20 13:30:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-007', '64b4a974-bda5-46e8-87ec-47cba75e72f3','OUT',5,'2025-01-22 16:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-008', '3dc7e0a4-d495-11f0-9386-325096b39f47','OUT',1,'2025-01-25 17:10:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

-- =====================================================================
-- 📅 FEVEREIRO — Vendas moderadas + reposição
-- =====================================================================
('IM-2025-020','44ad0a66-d495-11f0-940c-325096b39f47','IN',5,'2025-02-03 10:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-021','4364bfdd-7c7b-436b-8c89-a1cd4cfe6815','IN',10,'2025-02-03 10:10:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

('IM-2025-022','3b09864c-d495-11f0-82ae-325096b39f47','OUT',2,'2025-02-12 14:20:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-023','64b4a974-bda5-46e8-87ec-47cba75e72f3','OUT',8,'2025-02-15 19:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-024','4364bfdd-7c7b-436b-8c89-a1cd4cfe6815','OUT',2,'2025-02-18 11:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

('IM-2025-025','3dc7e0a4-d495-11f0-9386-325096b39f47','ADJUST',-1,'2025-02-25 18:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

-- =====================================================================
-- 📅 MARÇO — Começa a girar mais o estoque
-- =====================================================================
('IM-2025-040','62b330a1-e6c6-4781-925b-30bae7c902ae','IN',6,'2025-03-04 10:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-041','1e6d23b6-ce51-4f82-8c0b-a947c02e9b67','IN',30,'2025-03-05 13:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

('IM-2025-042','62b330a1-e6c6-4781-925b-30bae7c902ae','OUT',2,'2025-03-10 12:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-043','1e6d23b6-ce51-4f82-8c0b-a947c02e9b67','OUT',10,'2025-03-12 17:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-044','645a2d87-4b53-4b74-ab35-fcf8ca43aff1','OUT',1,'2025-03-14 11:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

-- =====================================================================
-- 📅 ABRIL — Mercado estável
-- =====================================================================
('IM-2025-060','21d19e9e-d495-11f0-a21f-325096b39f47','IN',5,'2025-04-01 09:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

('IM-2025-061','21d19e9e-d495-11f0-a21f-325096b39f47','OUT',1,'2025-04-06 15:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-062','36461c9c-d495-11f0-a8b3-325096b39f47','OUT',3,'2025-04-14 14:30:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

-- =====================================================================
-- 📅 MAIO — Pré-férias
-- =====================================================================
('IM-2025-080','8e54f508-ca69-431e-873a-3dc99c7090c2','IN',5,'2025-05-02 10:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

('IM-2025-081','8e54f508-ca69-431e-873a-3dc99c7090c2','OUT',2,'2025-05-10 16:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-082','64b4a974-bda5-46e8-87ec-47cba75e72f3','OUT',15,'2025-05-12 18:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-083','44ad0a66-d495-11f0-940c-325096b39f47','OUT',2,'2025-05-22 12:40:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

-- =====================================================================
-- 📅 JUNHO — Mês mais parado
-- =====================================================================
('IM-2025-100','26a5866a-d495-11f0-808b-325096b39f47','IN',6,'2025-06-01 10:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

('IM-2025-101','26a5866a-d495-11f0-808b-325096b39f47','OUT',1,'2025-06-14 13:20:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-102','3dc7e0a4-d495-11f0-9386-325096b39f47','OUT',1,'2025-06-20 17:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

-- =====================================================================
-- 📅 JULHO — Crescimento do hype
-- =====================================================================
('IM-2025-120','5ccc7f94-003d-49a0-8ea0-5560fc74cf53','IN',4,'2025-07-05 09:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-121','64b4a974-bda5-46e8-87ec-47cba75e72f3','IN',60,'2025-07-06 11:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

('IM-2025-122','64b4a974-bda5-46e8-87ec-47cba75e72f3','OUT',20,'2025-07-20 15:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-123','36461c9c-d495-11f0-a8b3-325096b39f47','OUT',3,'2025-07-22 18:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

-- =====================================================================
-- 📅 AGOSTO — LANÇAMENTO (VENDA INSANA)
-- =====================================================================
('IM-2025-140','a8a22009-9e5c-457a-8622-9e88e7bde048','IN',6,'2025-08-01 09:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-141','1e6d23b6-ce51-4f82-8c0b-a947c02e9b67','IN',80,'2025-08-01 09:10:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

('IM-2025-142','1e6d23b6-ce51-4f82-8c0b-a947c02e9b67','OUT',40,'2025-08-05 16:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-143','a8a22009-9e5c-457a-8622-9e88e7bde048','OUT',3,'2025-08-07 12:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-144','8e54f508-ca69-431e-873a-3dc99c7090c2','OUT',4,'2025-08-10 14:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-145','5911154c-39a3-43d0-9cd3-1ffc56b944e3','OUT',2,'2025-08-16 19:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

-- =====================================================================
-- 📅 SETEMBRO — Mercado estabiliza
-- =====================================================================
('IM-2025-160','1d78603a-d495-11f0-80a0-325096b39f47','IN',8,'2025-09-02 10:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

('IM-2025-161','1d78603a-d495-11f0-80a0-325096b39f47','OUT',3,'2025-09-10 15:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-162','2fb2353c-d495-11f0-be6e-325096b39f47','OUT',1,'2025-09-18 17:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

-- =====================================================================
-- 📅 OUTUBRO — DIA DAS CRIANÇAS (MEGA VENDA!)
-- =====================================================================
('IM-2025-180','64b4a974-bda5-46e8-87ec-47cba75e72f3','OUT',35,'2025-10-10 14:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-181','1e6d23b6-ce51-4f82-8c0b-a947c02e9b67','OUT',30,'2025-10-11 16:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-182','5ccc7f94-003d-49a0-8ea0-5560fc74cf53','OUT',3,'2025-10-12 12:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-183','5fcc2746-8012-4c0a-a1f4-f3a3c4dce288','OUT',2,'2025-10-13 11:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

-- =====================================================================
-- 📅 NOVEMBRO — Black Friday
-- =====================================================================
('IM-2025-200','26a5866a-d495-11f0-808b-325096b39f47','OUT',2,'2025-11-20 18:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-201','21d19e9e-d495-11f0-a21f-325096b39f47','OUT',1,'2025-11-25 16:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),

-- =====================================================================
-- 📅 DEZEMBRO — Natal
-- =====================================================================
('IM-2025-220','36461c9c-d495-11f0-a8b3-325096b39f47','OUT',4,'2025-12-05 14:30:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-221','3b09864c-d495-11f0-82ae-325096b39f47','OUT',2,'2025-12-10 18:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-222','64b4a974-bda5-46e8-87ec-47cba75e72f3','OUT',20,'2025-12-15 17:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0),
('IM-2025-223','1e6d23b6-ce51-4f82-8c0b-a947c02e9b67','OUT',10,'2025-12-18 19:00:00','44d7f605-d3e4-11f0-8fdc-001a7dda7111',0);


-- ================================================ AQUISIÇÕES AO LONGO DO ANO



-- LIMPAR A TABELA (opcional, mas recomendado dado o erro de PK)
-- TRUNCATE TABLE inventory_movement;
-- ou:
-- DELETE FROM inventory_movement;

INSERT INTO inventory_movement (
    id,
    product_id,
    user_id,
    quantity,
    unit_purchase_price,
    unit_sale_price,
    type,
    description,
    created_by,
    created_at,
    updated_by,
    updated_at,
    deleted
) VALUES

-- =====================================================================
-- 📅 JANEIRO 2025 – ENTRADA INICIAL DE ESTOQUE
-- =====================================================================

('11111111-aaaa-4bbb-8ccc-000000000001', '36461c9c-d495-11f0-a8b3-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 80, 0.40, 1.00, 'IN',
 'Compra inicial Pineco (bulk)',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-05 10:00:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-05 10:00:00', 0),

('11111111-aaaa-4bbb-8ccc-000000000002', '3b09864c-d495-11f0-82ae-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 40, 1.80, 3.50, 'IN',
 'Compra inicial Meowscarada',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-05 10:10:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-05 10:10:00', 0),

('11111111-aaaa-4bbb-8ccc-000000000003', '3dc7e0a4-d495-11f0-9386-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 24, 7.00, 12.00, 'IN',
 'Compra inicial Arcanine ex',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-05 10:20:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-05 10:20:00', 0),

('11111111-aaaa-4bbb-8ccc-000000000004', '44ad0a66-d495-11f0-940c-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 24, 6.50, 11.00, 'IN',
 'Compra inicial Gyarados ex',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-05 10:30:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-05 10:30:00', 0),

('11111111-aaaa-4bbb-8ccc-000000000005', '5911154c-39a3-43d0-9cd3-1ffc56b944e3',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 24, 5.50, 10.00, 'IN',
 'Compra inicial Magnezone ex',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-05 10:40:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-05 10:40:00', 0),

('11111111-aaaa-4bbb-8ccc-000000000006', '3f029c2c-ef94-46da-9d50-a8e20d61eb1b',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 20, 8.00, 14.00, 'IN',
 'Compra inicial Gardevoir ex',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-05 10:50:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-05 10:50:00', 0),

('11111111-aaaa-4bbb-8ccc-000000000007', '5fcc2746-8012-4c0a-a1f4-f3a3c4dce288',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 18, 2.20, 4.00, 'IN',
 'Compra inicial Annihilape',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-05 11:00:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-05 11:00:00', 0),

('11111111-aaaa-4bbb-8ccc-000000000008', '645a2d87-4b53-4b74-ab35-fcf8ca43aff1',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 18, 4.50, 8.00, 'IN',
 'Compra inicial Toxicroak ex',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-05 11:10:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-05 11:10:00', 0),

('11111111-aaaa-4bbb-8ccc-000000000009', '62b330a1-e6c6-4781-925b-30bae7c902ae',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 18, 4.00, 7.00, 'IN',
 'Compra inicial Iron Treads ex',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-05 11:20:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-05 11:20:00', 0),

('11111111-aaaa-4bbb-8ccc-00000000000a', '8e54f508-ca69-431e-873a-3dc99c7090c2',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 24, 3.50, 6.00, 'IN',
 'Compra inicial Oinkologne ex',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-05 11:30:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-05 11:30:00', 0),

-- Singles premium
('22222222-bbbb-4ccc-8ddd-000000000001', '95f91aed-1ee2-4f7d-a830-23906438377a',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 3, 380.00, 599.00, 'IN',
 'Compra Charizard Mint (alta prateleira)',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-06 10:00:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-06 10:00:00', 0),

('22222222-bbbb-4ccc-8ddd-000000000002', '00e24eaa-9766-4056-a49c-77f9457e41b9',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 3, 110.00, 170.00, 'IN',
 'Compra Dark Charizard Mint',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-06 10:10:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-06 10:10:00', 0),

('22222222-bbbb-4ccc-8ddd-000000000003', 'e0a3bc33-904a-42ed-8221-b7231e70352b',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 30, 6.50, 12.90, 'IN',
 'Compra Pikachu Mint (alta rotatividade)',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-06 10:30:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-06 10:30:00', 0),

-- Boosters, boxes, acessórios e merch
('33333333-cccc-4ddd-8eee-000000000001', '5ccc7f94-003d-49a0-8ea0-5560fc74cf53',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 8, 650.00, 850.00, 'IN',
 'Lote inicial Booster Box SV1',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-07 09:00:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-07 09:00:00', 0),

('33333333-cccc-4ddd-8eee-000000000002', '64b4a974-bda5-46e8-87ec-47cba75e72f3',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 120, 11.00, 18.00, 'IN',
 'Lote inicial boosters SV1 (loja física + online)',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-07 09:10:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-07 09:10:00', 0),

('33333333-cccc-4ddd-8eee-000000000003', '4364bfdd-7c7b-436b-8c89-a1cd4cfe6815',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 50, 28.00, 45.00, 'IN',
 'Compra sleeves Miraidon',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-07 09:20:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-07 09:20:00', 0),

('33333333-cccc-4ddd-8eee-000000000004', '1d78603a-d495-11f0-80a0-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 30, 20.00, 35.00, 'IN',
 'Compra deck box Koraidon',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-07 09:30:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-07 09:30:00', 0),

('33333333-cccc-4ddd-8eee-000000000005', '21d19e9e-d495-11f0-a21f-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 20, 75.00, 110.00, 'IN',
 'Compra playmat oficial SV1',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-07 09:40:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-07 09:40:00', 0),

('33333333-cccc-4ddd-8eee-000000000006', '26a5866a-d495-11f0-808b-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 15, 65.00, 99.00, 'IN',
 'Compra pin collection Pikachu',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-07 09:50:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-07 09:50:00', 0),

('33333333-cccc-4ddd-8eee-000000000007', '2fb2353c-d495-11f0-be6e-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 10, 80.00, 120.00, 'IN',
 'Compra mini figure Gyarados',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-01-07 10:00:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-01-07 10:00:00', 0),

-- =====================================================================
-- 📅 FEVEREIRO–JUNHO 2025 – VENDAS REGULARES + REPOSIÇÕES
-- (OUT sempre com quantity positiva!)
-- =====================================================================

-- Fevereiro: boosters, singles e acessórios
('44444444-dddd-4eee-8fff-000000000001', '64b4a974-bda5-46e8-87ec-47cba75e72f3',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 18, 11.00, 18.00, 'OUT',
 'Venda boosters SV1 – mês de testes',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-02-10 16:00:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-02-10 16:00:00', 0),

('44444444-dddd-4eee-8fff-000000000002', '3b09864c-d495-11f0-82ae-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 6, 1.80, 3.50, 'OUT',
 'Vendas Meowscarada para deck competitivo',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-02-14 18:10:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-02-14 18:10:00', 0),

('44444444-dddd-4eee-8fff-000000000003', '4364bfdd-7c7b-436b-8c89-a1cd4cfe6815',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 10, 28.00, 45.00, 'OUT',
 'Sleeves vendidos em torneio local',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-02-17 20:00:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-02-17 20:00:00', 0),

-- Março: aumento leve + ajuste
('44444444-dddd-4eee-8fff-000000000004', '3dc7e0a4-d495-11f0-9386-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 4, 7.00, 12.00, 'OUT',
 'Venda Arcanine ex (jogadores competitivos)',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-03-05 15:30:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-03-05 15:30:00', 0),

('44444444-dddd-4eee-8fff-000000000005', '44ad0a66-d495-11f0-940c-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 3, 6.50, 11.00, 'OUT',
 'Venda Gyarados ex para colecionadores',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-03-12 17:45:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-03-12 17:45:00', 0),

('44444444-dddd-4eee-8fff-000000000006', '36461c9c-d495-11f0-a8b3-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 20, 0.40, 1.00, 'OUT',
 'Bulk Pineco vendido em lote online',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-03-20 11:10:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-03-20 11:10:00', 0),

('44444444-dddd-4eee-8fff-000000000007', '36461c9c-d495-11f0-a8b3-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 10, 0.35, 1.00, 'ADJUST',
 'Ajuste de contagem Pineco (cartas achadas na gaveta)',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-03-28 09:00:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-03-28 09:00:00', 0),

-- Abril: foco em merch / playmat
('44444444-dddd-4eee-8fff-000000000008', '26a5866a-d495-11f0-808b-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 4, 65.00, 99.00, 'OUT',
 'Pin collection Pikachu em kits presente',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-04-06 16:00:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-04-06 16:00:00', 0),

('44444444-dddd-4eee-8fff-000000000009', '21d19e9e-d495-11f0-a21f-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 5, 75.00, 110.00, 'OUT',
 'Playmats vendidos em campeonato local',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-04-14 19:20:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-04-14 19:20:00', 0),

-- Maio: boosters pré-férias + reposição
('44444444-dddd-4eee-8fff-00000000000a', '64b4a974-bda5-46e8-87ec-47cba75e72f3',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 24, 11.00, 18.00, 'OUT',
 'Venda boosters SV1 – mês pré-férias',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-05-10 15:45:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-05-10 15:45:00', 0),

('44444444-dddd-4eee-8fff-00000000000b', '64b4a974-bda5-46e8-87ec-47cba75e72f3',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 36, 10.50, 18.00, 'IN',
 'Reposição moderada boosters SV1',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-05-18 10:00:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-05-18 10:00:00', 0),

('44444444-dddd-4eee-8fff-00000000000c', 'e0a3bc33-904a-42ed-8221-b7231e70352b',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 8, 6.50, 12.90, 'OUT',
 'Pikachu Mint vendido para iniciantes e colecionadores',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-05-23 17:30:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-05-23 17:30:00', 0),

-- Junho: giro mais baixo
('44444444-dddd-4eee-8fff-00000000000d', '3dc7e0a4-d495-11f0-9386-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 2, 7.00, 12.00, 'OUT',
 'Vendas pontuais Arcanine ex',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-06-08 16:10:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-06-08 16:10:00', 0),

('44444444-dddd-4eee-8fff-00000000000e', '36461c9c-d495-11f0-a8b3-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 10, 0.40, 1.00, 'OUT',
 'Bulk Pineco vendido como extra',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-06-21 11:00:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-06-21 11:00:00', 0),

-- =====================================================================
-- 📅 JULHO 2025 – AQUECIMENTO PRÉ LANÇAMENTO
-- =====================================================================

('55555555-eeee-4fff-8000-000000000001', '5ccc7f94-003d-49a0-8ea0-5560fc74cf53',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 6, 640.00, 850.00, 'IN',
 'Reposição Booster Box SV1 para férias',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-07-03 09:30:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-07-03 09:30:00', 0),

('55555555-eeee-4fff-8000-000000000002', '64b4a974-bda5-46e8-87ec-47cba75e72f3',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 60, 10.50, 18.00, 'IN',
 'Compra extra boosters SV1 para torneios de férias',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-07-03 09:40:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-07-03 09:40:00', 0),

('55555555-eeee-4fff-8000-000000000003', '4364bfdd-7c7b-436b-8c89-a1cd4cfe6815',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 12, 28.00, 45.00, 'OUT',
 'Sleeves vendidos em eventos de férias',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-07-15 18:20:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-07-15 18:20:00', 0),

('55555555-eeee-4fff-8000-000000000004', '95f91aed-1ee2-4f7d-a830-23906438377a',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 1, 380.00, 599.00, 'OUT',
 'Charizard Mint vendido com alta margem',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-07-19 16:45:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-07-19 16:45:00', 0),

-- =====================================================================
-- 📅 AGOSTO 2025 – LANÇAMENTO IMPORTANTE (EXPLOSÃO)
-- =====================================================================

-- Chegada SV2
('66666666-ffff-4000-8000-000000000001', 'a8a22009-9e5c-457a-8622-9e88e7bde048',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 10, 520.00, 720.00, 'IN',
 'Chegada Booster Box SV2 (lançamento)',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-08-01 09:00:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-08-01 09:00:00', 0),

('66666666-ffff-4000-8000-000000000002', '1e6d23b6-ce51-4f82-8c0b-a947c02e9b67',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 150, 7.00, 12.00, 'IN',
 'Chegada boosters SV2 (estoque de lançamento)',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-08-01 09:10:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-08-01 09:10:00', 0),

-- Vendas fortes no lançamento
('66666666-ffff-4000-8000-000000000003', '1e6d23b6-ce51-4f82-8c0b-a947c02e9b67',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 60, 7.00, 12.00, 'OUT',
 'Venda intensa boosters SV2 fim de semana lançamento',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-08-03 17:20:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-08-03 17:20:00', 0),

('66666666-ffff-4000-8000-000000000004', 'a8a22009-9e5c-457a-8622-9e88e7bde048',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 4, 520.00, 720.00, 'OUT',
 'Venda Box SV2 para colecionadores hardcore',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-08-05 19:00:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-08-05 19:00:00', 0),

('66666666-ffff-4000-8000-000000000005', '64b4a974-bda5-46e8-87ec-47cba75e72f3',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 30, 10.50, 18.00, 'OUT',
 'Players ainda abrindo SV1 junto com SV2',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-08-09 16:30:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-08-09 16:30:00', 0),

('66666666-ffff-4000-8000-000000000006', 'e0a3bc33-904a-42ed-8221-b7231e70352b',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 10, 6.50, 12.90, 'OUT',
 'Pikachu Mint surfando hype de lançamento',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-08-12 18:10:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-08-12 18:10:00', 0),

('66666666-ffff-4000-8000-000000000007', '4364bfdd-7c7b-436b-8c89-a1cd4cfe6815',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 15, 28.00, 45.00, 'OUT',
 'Sleeves vendidos em massa com decks novos',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-08-16 17:45:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-08-16 17:45:00', 0),

('66666666-ffff-4000-8000-000000000008', '1e6d23b6-ce51-4f82-8c0b-a947c02e9b67',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 3, 7.00, 12.00, 'ADJUST',
 'Ajuste negativo boosters SV2 (embalagens danificadas)',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-08-25 10:15:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-08-25 10:15:00', 0),

-- =====================================================================
-- 📅 SETEMBRO 2025 – ESTABILIZAÇÃO PÓS LANÇAMENTO
-- =====================================================================

('77777777-0000-4000-8000-000000000001', '1e6d23b6-ce51-4f82-8c0b-a947c02e9b67',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 25, 7.00, 12.00, 'OUT',
 'Vendas constantes boosters SV2 (meta estabilizou)',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-09-08 18:40:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-09-08 18:40:00', 0),

('77777777-0000-4000-8000-000000000002', '3f029c2c-ef94-46da-9d50-a8e20d61eb1b',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 4, 8.00, 14.00, 'OUT',
 'Gardevoir ex forte no meta – vendas sólidas',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-09-15 17:15:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-09-15 17:15:00', 0),

('77777777-0000-4000-8000-000000000003', '1d78603a-d495-11f0-80a0-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 6, 20.00, 35.00, 'OUT',
 'Deck box vendida em campeonato local',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-09-21 14:05:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-09-21 14:05:00', 0),

-- =====================================================================
-- OUTUBRO 2025 – DIA DAS CRIANÇAS (PICO INSANO)
-- =====================================================================

-- Reposição forte antes do pico
('88888888-1111-4000-8000-000000000001', '64b4a974-bda5-46e8-87ec-47cba75e72f3',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 80, 10.50, 18.00, 'IN',
 'Reposição pesada boosters SV1 p/ Dia das Crianças',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-10-01 10:00:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-10-01 10:00:00', 0),

('88888888-1111-4000-8000-000000000002', '1e6d23b6-ce51-4f82-8c0b-a947c02e9b67',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 60, 7.00, 12.00, 'IN',
 'Reposição boosters SV2 para kits infantis',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-10-01 10:10:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-10-01 10:10:00', 0),

('88888888-1111-4000-8000-000000000003', '4364bfdd-7c7b-436b-8c89-a1cd4cfe6815',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 40, 28.00, 45.00, 'IN',
 'Reposição de sleeves para presentes infantis',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-10-01 10:20:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-10-01 10:20:00', 0),

-- Pico de vendas (Dia das Crianças)
('88888888-1111-4000-8000-000000000004', '64b4a974-bda5-46e8-87ec-47cba75e72f3',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 70, 10.50, 18.00, 'OUT',
 'Explosão de boosters SV1 no Dia das Crianças',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-10-11 15:30:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-10-11 15:30:00', 0),

('88888888-1111-4000-8000-000000000005', '1e6d23b6-ce51-4f82-8c0b-a947c02e9b67',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 50, 7.00, 12.00, 'OUT',
 'Boosters SV2 em kits presente infantis',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-10-12 14:10:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-10-12 14:10:00', 0),

('88888888-1111-4000-8000-000000000006', '26a5866a-d495-11f0-808b-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 6, 65.00, 99.00, 'OUT',
 'Pin collection Pikachu como presente de Dia das Crianças',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-10-12 16:00:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-10-12 16:00:00', 0),

('88888888-1111-4000-8000-000000000007', 'e0a3bc33-904a-42ed-8221-b7231e70352b',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 12, 6.50, 12.90, 'OUT',
 'Pikachu Mint vendido em massa para crianças iniciantes',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-10-13 18:20:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-10-13 18:20:00', 0),

-- =====================================================================
-- NOVEMBRO 2025 – PRÉ-BLACK FRIDAY
-- =====================================================================

('99999999-2222-4000-8000-000000000001', '5ccc7f94-003d-49a0-8ea0-5560fc74cf53',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 4, 640.00, 850.00, 'IN',
 'Reposição leve Box SV1 para Black Friday',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-11-05 11:00:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-11-05 11:00:00', 0),

('99999999-2222-4000-8000-000000000002', '5ccc7f94-003d-49a0-8ea0-5560fc74cf53',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 3, 640.00, 820.00, 'OUT',
 'Venda Box SV1 em promo pré-Black Friday',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-11-23 16:40:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-11-23 16:40:00', 0),

('99999999-2222-4000-8000-000000000003', '21d19e9e-d495-11f0-a21f-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 4, 75.00, 105.00, 'OUT',
 'Playmat em combo Black Friday',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-11-25 19:10:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-11-25 19:10:00', 0),

-- =====================================================================
-- DEZEMBRO 2025 – NATAL (BOOST FINAL)
-- =====================================================================

('aaaa0000-3333-4000-8000-000000000001', '64b4a974-bda5-46e8-87ec-47cba75e72f3',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 40, 10.50, 18.00, 'OUT',
 'Boosters SV1 para presentes de Natal',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-12-15 17:30:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-12-15 17:30:00', 0),

('aaaa0000-3333-4000-8000-000000000002', '1e6d23b6-ce51-4f82-8c0b-a947c02e9b67',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 28, 7.00, 12.00, 'OUT',
 'Boosters SV2 em kits de Natal',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-12-16 18:10:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-12-16 18:10:00', 0),

('aaaa0000-3333-4000-8000-000000000003', '95f91aed-1ee2-4f7d-a830-23906438377a',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 1, 380.00, 580.00, 'OUT',
 'Charizard Mint vendido como presente de luxo de Natal',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-12-20 19:45:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-12-20 19:45:00', 0),

('aaaa0000-3333-4000-8000-000000000004', '2fb2353c-d495-11f0-be6e-325096b39f47',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', 4, 80.00, 120.00, 'OUT',
 'Mini figure Gyarados vendido em combos de Natal',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111',
 '2025-12-22 16:20:00',
 '44d7f605-d3e4-11f0-8fdc-001a7dda7111', '2025-12-22 16:20:00', 0);
 
 select * from user;
 select * from inventory_audit order by timestamp desc	;
