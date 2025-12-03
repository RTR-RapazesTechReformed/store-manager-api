-- =====================================================
-- BANCO DE DADOS MERURU TCG - POC SIMPLES
-- =====================================================

DROP DATABASE IF EXISTS RTR;
CREATE DATABASE IF NOT EXISTS RTR CHARACTER SET = 'utf8mb4' COLLATE = 'utf8mb4_unicode_ci';
USE RTR;

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
  ),
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
  ended_at TIMESTAMP NOT NULL,
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

-- STORE
SET @store_id = UUID();
INSERT INTO store (id, name, cep, number, complement, created_by)
VALUES (@store_id, 'Meruru TCG Store', '01310940', '25', 'Loja 02', 'system');

-- USERS
SET @admin = UUID();
SET @manager = UUID();
SET @staff = UUID();

INSERT INTO user (id, name, email, password_hash, role_id, store_id, created_by)
VALUES
(@admin, 'Administrador', 'admin@meruru.com', '123456', '001', @store_id, 'system'),
(@manager, 'Gerente', 'manager@meruru.com', '123456', '002', @store_id, 'system'),
(@staff, 'Funcionário', 'staff@meruru.com', '123456', '003', @store_id, 'system');


-- COLEÇÕES
SET @base_set_id = UUID();
SET @jungle_id = UUID();
SET @fossil_id = UUID();

INSERT INTO collection (id, name, abbreviation, release_date, generation, created_by)
VALUES
  (@base_set_id, 'Base Set', 'BS', '1999-01-09', 'GEN1', 'system'),
  (@jungle_id, 'Jungle', 'JU', '1999-06-16', 'GEN1', 'system'),
  (@fossil_id, 'Fossil', 'FO', '1999-10-10', 'GEN1', 'system');


-- ================================
-- BASE SET CARDS
-- ================================
INSERT INTO card (id, title, season, pokemon_type, collection_id, code, rarity, nationality, created_by)
VALUES
(@pikachu := UUID(), 'Pikachu', '1', 'LIGHTNING', @base_set_id, 'BS-025', 'COMMON', 'JP', 'system'),
(@charizard := UUID(), 'Charizard', '1', 'FIRE', @base_set_id, 'BS-004', 'RARE_HOLO', 'JP', 'system'),
(@blastoise := UUID(), 'Blastoise', '1', 'WATER', @base_set_id, 'BS-002', 'RARE_HOLO', 'JP', 'system'),
(@venusaur := UUID(), 'Venusaur', '1', 'GRASS', @base_set_id, 'BS-003', 'RARE_HOLO', 'JP', 'system'),
(@machamp := UUID(), 'Machamp', '1', 'FIGHTING', @base_set_id, 'BS-008', 'RARE_HOLO', 'JP', 'system');

-- ================================
-- JUNGLE CARDS
-- ================================
INSERT INTO card (id, title, season, pokemon_type, collection_id, code, rarity, nationality, created_by)
VALUES
(@kangaskhan := UUID(), 'Kangaskhan', '1', 'COLORLESS', @jungle_id, 'JU-005', 'RARE_HOLO', 'JP', 'system'),
(@scyther := UUID(), 'Scyther', '1', 'GRASS', @jungle_id, 'JU-010', 'RARE_HOLO', 'JP', 'system'),
(@snorlax := UUID(), 'Snorlax', '1', 'COLORLESS', @jungle_id, 'JU-011', 'RARE_HOLO', 'JP', 'system'),
(@wigglytuff := UUID(), 'Wigglytuff', '1', 'COLORLESS', @jungle_id, 'JU-016', 'RARE_HOLO', 'JP', 'system'),
(@electrode := UUID(), 'Electrode', '1', 'LIGHTNING', @jungle_id, 'JU-002', 'RARE_HOLO', 'JP', 'system');


-- =====================================================
-- FOSSIL (CONTINUAÇÃO)
-- =====================================================

INSERT INTO card (id, title, season, pokemon_type, collection_id, code, rarity, nationality, created_by)
VALUES
(@aerodactyl := UUID(), 'Aerodactyl', '1', 'FIGHTING', @fossil_id, 'FO-001', 'RARE_HOLO', 'JP', 'system'),
(@articuno := UUID(), 'Articuno', '1', 'WATER', @fossil_id, 'FO-002', 'RARE_HOLO', 'JP', 'system'),
(@ditto := UUID(), 'Ditto', '1', 'COLORLESS', @fossil_id, 'FO-003', 'RARE_HOLO', 'JP', 'system'),
(@dragonite := UUID(), 'Dragonite', '1', 'COLORLESS', @fossil_id, 'FO-004', 'RARE_HOLO', 'JP', 'system'),
(@gengar := UUID(), 'Gengar', '1', 'PSYCHIC', @fossil_id, 'FO-005', 'RARE_HOLO', 'JP', 'system');


-- =====================================================
-- PRODUTOS (CARDS → PRODUCTS)
-- =====================================================

INSERT INTO product (id, name, description, card_id, store_id, price, product_condition, created_by)
VALUES
(@prod_pikachu := UUID(), 'Pikachu - Base Set', 'Carta original Base Set', @pikachu, @store_id, 40.00, 'MINT', 'system'),
(@prod_charizard := UUID(), 'Charizard - Base Set', 'Holo rara', @charizard, @store_id, 1900.00, 'LIGHTLY_PLAYED', 'system'),
(@prod_blastoise := UUID(), 'Blastoise - Base Set', 'Holo rara', @blastoise, @store_id, 850.00, 'MINT', 'system'),
(@prod_kangaskhan := UUID(), 'Kangaskhan - Jungle', 'Holo rara', @kangaskhan, @store_id, 350.00, 'MINT', 'system'),
(@prod_aerodactyl := UUID(), 'Aerodactyl - Fossil', 'Holo rara', @aerodactyl, @store_id, 300.00, 'MINT', 'system');


-- =====================================================
-- INVENTORY
-- =====================================================

INSERT INTO inventory (product_id, quantity, created_by)
VALUES
(@prod_pikachu, 50, 'system'),
(@prod_charizard, 5, 'system'),
(@prod_blastoise, 12, 'system'),
(@prod_kangaskhan, 18, 'system'),
(@prod_aerodactyl, 20, 'system');


-- =====================================================
-- INVENTORY_MOVEMENT (MUITOS REGISTROS PARA DASHBOARD)
-- =====================================================

-- ENTRADAS
INSERT INTO inventory_movement (id, product_id, user_id, quantity, unit_purchase_price, type, description, created_by)
VALUES
(UUID(), @prod_pikachu, @manager, 30, 18.00, 'IN', 'Reposição inicial', 'system'),
(UUID(), @prod_pikachu, @manager, 20, 20.00, 'IN', 'Compra adicional', 'system'),
(UUID(), @prod_charizard, @manager, 5, 1000.00, 'IN', 'Compra rara', 'system'),
(UUID(), @prod_blastoise, @manager, 10, 400.00, 'IN', 'Compra', 'system'),
(UUID(), @prod_kangaskhan, @manager, 18, 110.00, 'IN', 'Compra', 'system'),
(UUID(), @prod_aerodactyl, @manager, 20, 90.00, 'IN', 'Compra', 'system');

-- SAÍDAS (VENDAS)
INSERT INTO inventory_movement (id, product_id, user_id, quantity, unit_sale_price, type, description, created_by)
VALUES
(UUID(), @prod_pikachu, @staff, -5, 49.90, 'OUT', 'Venda balcão', 'system'),
(UUID(), @prod_pikachu, @staff, -10, 49.90, 'OUT', 'Venda online', 'system'),
(UUID(), @prod_charizard, @staff, -1, 2599.00, 'OUT', 'Venda rara', 'system'),
(UUID(), @prod_blastoise, @staff, -3, 999.00, 'OUT', 'Venda', 'system'),
(UUID(), @prod_kangaskhan, @staff, -4, 399.00, 'OUT', 'Venda', 'system'),
(UUID(), @prod_aerodactyl, @staff, -6, 450.00, 'OUT', 'Venda', 'system');

-- AJUSTES
INSERT INTO inventory_movement (id, product_id, user_id, quantity, type, description, created_by)
VALUES
(UUID(), @prod_pikachu, @manager, -1, 'ADJUST', 'Carta danificada', 'system'),
(UUID(), @prod_charizard, @manager, -1, 'ADJUST', 'Revisão de estoque', 'system');


-- =====================================================
-- INVENTORY AUDIT (REGISTROS GERADOS)
-- =====================================================

INSERT INTO inventory_audit (
  id, product_id, movement_id, user_id, movement_type,
  operation, quantity, quantity_before, quantity_after, description
)
SELECT
  UUID(),
  product_id,
  id,
  user_id,
  type,
  'CREATE',
  quantity,
  FLOOR(RAND()*50),
  FLOOR(RAND()*50),
  CONCAT('Audit for movement ', id)
FROM inventory_movement LIMIT 20;

SELECT 
            COALESCE(SUM(inv.quantity), 0) AS totalCardsInStock
        FROM inventory inv
        JOIN product p ON p.id = inv.product_id
        WHERE p.card_id IS NOT NULL;