# Store Manager API

## Índice

- [Descrição](#descrição)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Pré-requisitos](#pré-requisitos)
- [Instalação e Configuração](#instalação-e-configuração)
- [Executando o Projeto](#executando-o-projeto)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Modelos de Dados](#modelos-de-dados)
- [Endpoints da API](#endpoints-da-api)
- [Autenticação e Autorização](#autenticação-e-autorização)
- [Configuração do Kafka](#configuração-do-kafka)
- [Banco de Dados](#banco-de-dados)
- [Testes](#testes)
- [Padrões de Código](#padrões-de-código)
- [Contribuindo](#contribuindo)

---

## Descrição

**Store Manager API** é uma API REST desenvolvida para gerenciamento de inventário e precificação de Trading Card Games (TCG). O sistema permite o controle completo de:

- **Inventário de produtos** (cartas de Pokémon, Yu-Gi-Oh, Magic: The Gathering, etc.)
- **Precificação dinâmica** de produtos
- **Gestão de lojas** e múltiplos pontos de venda
- **Controle de usuários** com diferentes níveis de permissão
- **Dashboard** com métricas e relatórios
- **Auditoria** de movimentações de estoque
- **Eventos assíncronos** via Apache Kafka

---

## Tecnologias

### Backend
- **Kotlin** 1.9.25
- **Spring Boot** 3.5.5
- **Spring Data JPA** - ORM e persistência de dados
- **Spring Web** - APIs RESTful
- **Spring Validation** - Validação de dados
- **Spring Kafka** - Mensageria assíncrona
- **Spring Security** - Segurança e criptografia

### Banco de Dados
- **MySQL** 8.x
- **Hibernate** - Framework ORM

### Mensageria
- **Apache Kafka** 7.6.1
- **Apache Zookeeper** 7.6.1

### Segurança
- **BCrypt** - Hash de senhas

### Build e Gerenciamento
- **Maven** - Gerenciamento de dependências
- **Docker Compose** - Orquestração de containers

### Versionamento
- **Java** 21
- **Kotlin** 1.9.25

---

## Arquitetura

O projeto segue uma **arquitetura em camadas** (Layered Architecture) com as seguintes responsabilidades:

```
┌─────────────────────────────────────┐
│         Controllers                 │  ← Camada de Apresentação (REST)
├─────────────────────────────────────┤
│         Services                    │  ← Camada de Lógica de Negócio
├─────────────────────────────────────┤
│         Repositories                │  ← Camada de Acesso a Dados
├─────────────────────────────────────┤
│         Entities                    │  ← Camada de Domínio
└─────────────────────────────────────┘
```

### Padrões Utilizados
- **DTO (Data Transfer Object)** - Transferência de dados entre camadas
- **Repository Pattern** - Abstração de acesso a dados
- **Service Layer** - Encapsulamento de lógica de negócio
- **Event-Driven Architecture** - Comunicação assíncrona via Kafka
- **Soft Delete** - Deleção lógica de registros

---

## Pré-requisitos

Antes de iniciar, certifique-se de ter instalado:

- [Java JDK 21+](https://www.oracle.com/java/technologies/downloads/)
- [Maven 3.8+](https://maven.apache.org/download.cgi)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)
- [MySQL 8+](https://dev.mysql.com/downloads/) (ou use Docker)
- [Git](https://git-scm.com/downloads)

---

## Instalação e Configuração

### 1. Clone o repositório

```bash
git clone https://github.com/RTR-RapazesTechReformed/store-manager-api.git
cd store-manager-api
```

### 2. Configure o banco de dados

Crie um banco de dados MySQL:

```sql
CREATE DATABASE rtr CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Configure as variáveis de ambiente

Edite o arquivo `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/rtr
spring.datasource.username=root
spring.datasource.password=SUA_SENHA_AQUI

# Kafka Configuration
spring.kafka.bootstrap-servers=localhost:9092
```

### 4. Inicie o Kafka e Zookeeper (via Docker)

```bash
docker-compose up -d
```

Isso iniciará:
- **Zookeeper** na porta `2181`
- **Kafka** nas portas `9092` e `9093`
- **Kafka UI** na porta `8081` (interface web)

### 5. Compile o projeto

```bash
mvn clean install
```

---

## Executando o Projeto

### Usando Maven

```bash
mvn spring-boot:run
```

### Usando o JAR gerado

```bash
java -jar target/store-manager-api-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: **http://localhost:8080**

---

## Estrutura do Projeto

```
store-manager-api/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── com/rtr/store_manager_api/
│   │   │       ├── StoreManagerApiApplication.kt      # Classe principal
│   │   │       ├── configuration/                     # Configurações
│   │   │       │   ├── CorsConfiguration.kt
│   │   │       │   └── SecurityConfig.kt
│   │   │       ├── controller/                        # Controladores REST
│   │   │       │   ├── AuthController.kt
│   │   │       │   ├── CardController.kt
│   │   │       │   ├── CollectionController.kt
│   │   │       │   ├── DashController.kt
│   │   │       │   ├── InventoryController.kt
│   │   │       │   ├── InventoryMovementController.kt
│   │   │       │   ├── OtherProductController.kt
│   │   │       │   ├── PermissionController.kt
│   │   │       │   ├── ProductController.kt
│   │   │       │   ├── StoreController.kt
│   │   │       │   ├── UserController.kt
│   │   │       │   └── UserRoleController.kt
│   │   │       ├── domain/                            # Modelos de domínio
│   │   │       │   ├── entity/                        # Entidades JPA
│   │   │       │   │   ├── BaseEntity.kt
│   │   │       │   │   ├── Card.kt
│   │   │       │   │   ├── Collection.kt
│   │   │       │   │   ├── Inventory.kt
│   │   │       │   │   ├── InventoryAudit.kt
│   │   │       │   │   ├── InventoryMovement.kt
│   │   │       │   │   ├── OtherProduct.kt
│   │   │       │   │   ├── Permission.kt
│   │   │       │   │   ├── Product.kt
│   │   │       │   │   ├── Store.kt
│   │   │       │   │   ├── User.kt
│   │   │       │   │   ├── UserRole.kt
│   │   │       │   │   └── UserSession.kt
│   │   │       │   └── enum/                          # Enumerações
│   │   │       │       ├── AuditStatus.kt
│   │   │       │       ├── CardRarity.kt
│   │   │       │       ├── MovementType.kt
│   │   │       │       ├── Operation.kt
│   │   │       │       ├── OtherProductType.kt
│   │   │       │       ├── PokemonType.kt
│   │   │       │       ├── ProductCondition.kt
│   │   │       │       └── ProductType.kt
│   │   │       ├── dto/                               # Data Transfer Objects
│   │   │       │   ├── CardRequestDTO.kt
│   │   │       │   ├── CardResponseDTO.kt
│   │   │       │   ├── CollectionRequestDTO.kt
│   │   │       │   ├── CollectionResponseDTO.kt
│   │   │       │   └── ...
│   │   │       ├── event/                             # Eventos Kafka
│   │   │       ├── exception/                         # Tratamento de exceções
│   │   │       ├── repository/                        # Repositórios JPA
│   │   │       │   ├── CardRepository.kt
│   │   │       │   ├── CollectionRepository.kt
│   │   │       │   ├── DashRepository.kt
│   │   │       │   ├── InventoryAuditRepository.kt
│   │   │       │   ├── InventoryMovementRepository.kt
│   │   │       │   ├── InventoryRepository.kt
│   │   │       │   ├── OtherProductRepository.kt
│   │   │       │   ├── PermissionRepository.kt
│   │   │       │   ├── ProductRepository.kt
│   │   │       │   ├── StoreRepository.kt
│   │   │       │   ├── UserRepository.kt
│   │   │       │   ├── UserRoleRepository.kt
│   │   │       │   └── UserSessionRepository.kt
│   │   │       ├── service/                           # Serviços
│   │   │       │   ├── impl/                          # Implementações
│   │   │       │   ├── AuthService.kt
│   │   │       │   ├── AuditService.kt
│   │   │       │   ├── CardService.kt
│   │   │       │   ├── CollectionService.kt
│   │   │       │   ├── DashService.kt
│   │   │       │   ├── InventoryMovementService.kt
│   │   │       │   ├── InventoryService.kt
│   │   │       │   ├── OtherProductService.kt
│   │   │       │   ├── PermissionService.kt
│   │   │       │   ├── ProductService.kt
│   │   │       │   ├── StoreService.kt
│   │   │       │   ├── UserRoleService.kt
│   │   │       │   └── UserService.kt
│   │   │       └── util/                              # Utilitários
│   │   └── resources/
│   │       ├── application.properties                 # Configurações da aplicação
│   │       ├── static/                                # Recursos estáticos
│   │       └── templates/                             # Templates
│   └── test/                                          # Testes
├── target/                                            # Arquivos compilados
├── docker-compose.yml                                 # Configuração Docker
├── pom.xml                                            # Configuração Maven
└── README.md                                          # Documentação
```

---

## Modelos de Dados

### Entidades Principais

#### 1. **Product** (Produto)
Representa um produto disponível para venda.

```kotlin
@Entity
@Table(name = "product")
data class Product(
    val id: String,
    var name: String,
    var description: String?,
    val card: Card?,
    val otherProduct: OtherProduct?,
    var price: BigDecimal,
    var store: Store,
    var condition: ProductCondition
)
```

**Campos:**
- `id` - UUID do produto
- `name` - Nome do produto
- `description` - Descrição opcional
- `card` - Referência à carta (se for TCG)
- `otherProduct` - Referência a outros produtos
- `price` - Preço (decimal com 2 casas)
- `store` - Loja associada
- `condition` - Estado do produto (MINT, NEAR_MINT, etc.)

#### 2. **Card** (Carta TCG)
Representa uma carta de jogo colecionável.

```kotlin
@Entity
@Table(name = "card")
data class Card(
    val id: String,
    var title: String,
    var season: String?,
    var nationality: String?,
    var pokemonType: PokemonType,
    var collection: Collection,
    var code: String,
    var rarity: CardRarity
)
```

**Campos:**
- `title` - Nome da carta
- `season` - Temporada/edição
- `nationality` - Nacionalidade da carta
- `pokemonType` - Tipo de Pokémon (FIRE, WATER, etc.)
- `collection` - Coleção à qual pertence
- `code` - Código único da carta
- `rarity` - Raridade (COMMON, RARE, ULTRA_RARE, etc.)

#### 3. **Inventory** (Estoque)
Controla a quantidade de produtos em estoque.

```kotlin
@Entity
@Table(name = "inventory")
data class Inventory(
    val id: String,
    var product: Product,
    var quantity: Int,
    var createdBy: String,
    var updatedBy: String
)
```

#### 4. **InventoryMovement** (Movimentação de Estoque)
Registra todas as movimentações de entrada e saída.

```kotlin
@Entity
@Table(name = "inventory_movement")
data class InventoryMovement(
    val id: String,
    var product: Product,
    var movementType: MovementType, // ENTRADA, SAIDA
    var quantity: Int,
    var reason: String?,
    var performedBy: String
)
```

#### 5. **User** (Usuário)
Usuários do sistema com autenticação.

```kotlin
@Entity
@Table(name = "user")
data class User(
    val id: String,
    var name: String,
    var email: String,
    var password: String,
    var store: Store?,
    var role: UserRole
)
```

**Métodos:**
- `encodePassword()` - Criptografa a senha com BCrypt
- `checkPassword()` - Valida a senha informada

#### 6. **Store** (Loja)
Representa uma loja física ou online.

```kotlin
@Entity
@Table(name = "store")
data class Store(
    val id: String,
    var name: String,
    var address: String?,
    var phone: String?,
    var email: String?
)
```

#### 7. **Collection** (Coleção)
Agrupa cartas de uma mesma coleção/expansão.

```kotlin
@Entity
@Table(name = "collection")
data class Collection(
    val id: String,
    var name: String,
    var releaseDate: LocalDate?,
    var code: String
)
```

### Enumerações

#### ProductCondition
```kotlin
enum class ProductCondition {
    MINT,           // Perfeito
    NEAR_MINT,      // Quase perfeito
    EXCELLENT,      // Excelente
    GOOD,           // Bom
    LIGHT_PLAYED,   // Pouco usado
    PLAYED,         // Usado
    POOR            // Desgastado
}
```

#### CardRarity
```kotlin
enum class CardRarity {
    COMMON,         // Comum
    UNCOMMON,       // Incomum
    RARE,           // Raro
    ULTRA_RARE,     // Ultra Raro
    SECRET_RARE,    // Secreto Raro
    PROMO           // Promocional
}
```

#### PokemonType
```kotlin
enum class PokemonType {
    FIRE, WATER, GRASS, ELECTRIC, PSYCHIC, 
    FIGHTING, DARKNESS, METAL, FAIRY, DRAGON, 
    COLORLESS, NORMAL
}
```

#### MovementType
```kotlin
enum class MovementType {
    ENTRADA,        // Entrada de estoque
    SAIDA           // Saída de estoque
}
```

---

## Endpoints da API

Base URL: `http://localhost:8080/store-manager-api`

### Autenticação

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "senha123"
}
```

**Resposta:**
```json
{
  "session_id": "uuid-da-sessao",
  "user_id": "uuid-do-usuario",
  "user_name": "Nome do Usuário",
  "role": "ADMIN",
  "message": "Login realizado com sucesso"
}
```

#### Logout
```http
POST /auth/logout/{sessionId}
```

---

### Products (Produtos)

#### Listar todos os produtos
```http
GET /products
```

#### Buscar produto por ID
```http
GET /products/{productId}
```

#### Criar produto
```http
POST /products
Content-Type: application/json

{
  "name": "Charizard VMAX",
  "description": "Carta holográfica rara",
  "card_id": "uuid-da-carta",
  "price": 299.99,
  "store_id": "uuid-da-loja",
  "condition": "NEAR_MINT"
}
```

#### Atualizar produto
```http
PUT /products/{productId}
```

#### Deletar produto (soft delete)
```http
DELETE /products/{productId}
```

---

### Cards (Cartas)

#### Listar todas as cartas
```http
GET /cards
```

#### Buscar carta por ID
```http
GET /cards/{cardId}
```

#### Criar carta
```http
POST /cards
Content-Type: application/json

{
  "title": "Pikachu EX",
  "season": "Scarlet & Violet",
  "nationality": "EN",
  "pokemon_type": "ELECTRIC",
  "collection_id": "uuid-da-colecao",
  "code": "SV01-123",
  "rarity": "ULTRA_RARE"
}
```

#### Atualizar carta
```http
PUT /cards/{cardId}
```

#### Deletar carta
```http
DELETE /cards/{cardId}
```

---

### Inventory (Estoque)

#### Listar todo o estoque
```http
GET /inventory
```

#### Buscar estoque por produto
```http
GET /inventory/product/{productId}
```

#### Criar registro de estoque
```http
POST /inventory
Content-Type: application/json

{
  "product_id": "uuid-do-produto",
  "quantity": 50
}
```

#### Atualizar estoque
```http
PUT /inventory/{productId}
Content-Type: application/json

{
  "quantity": 30
}
```

#### Deletar estoque
```http
DELETE /inventory/{productId}
```

---

### Inventory Movements (Movimentações)

#### Listar movimentações
```http
GET /inventory-movements
```

#### Criar movimentação
```http
POST /inventory-movements
Content-Type: application/json

{
  "product_id": "uuid-do-produto",
  "movement_type": "ENTRADA",
  "quantity": 20,
  "reason": "Compra de fornecedor"
}
```

---

### Collections (Coleções)

#### Listar coleções
```http
GET /collections
```

#### Criar coleção
```http
POST /collections
Content-Type: application/json

{
  "name": "Temporal Forces",
  "release_date": "2024-03-22",
  "code": "SV05"
}
```

---

### Stores (Lojas)

#### Listar lojas
```http
GET /stores
```

#### Criar loja
```http
POST /stores
Content-Type: application/json

{
  "name": "Loja Centro",
  "address": "Rua Principal, 123",
  "phone": "(11) 98765-4321",
  "email": "loja@example.com"
}
```

---

### Users (Usuários)

#### Listar usuários
```http
GET /users
```

#### Criar usuário
```http
POST /users
Content-Type: application/json

{
  "name": "João Silva",
  "email": "joao@example.com",
  "password": "senha123",
  "store_id": "uuid-da-loja",
  "role_id": "uuid-do-role"
}
```

---

### Dashboard

#### Obter métricas do dashboard
```http
GET /dashboard/metrics
```

**Resposta:**
```json
{
  "total_products": 1250,
  "total_inventory_value": 45000.00,
  "low_stock_products": 15,
  "recent_movements": [...]
}
```

---

## Autenticação e Autorização

### Sistema de Autenticação

O sistema utiliza **sessões baseadas em UUID** armazenadas no banco de dados.

#### Fluxo de Autenticação:

1. **Login**: Usuário envia email e senha
2. **Validação**: Sistema verifica credenciais com BCrypt
3. **Sessão**: Cria `UserSession` com UUID único
4. **Token**: Retorna `session_id` para o cliente
5. **Requisições**: Cliente envia `session_id` no header ou parâmetro

### Níveis de Permissão

O sistema possui diferentes **roles** (funções) com permissões específicas:

| Role | Permissões |
|------|-----------|
| **ADMIN** | Acesso total ao sistema |
| **MANAGER** | Gerenciar produtos, estoque e relatórios |
| **SELLER** | Consultar produtos e registrar vendas |
| **VIEWER** | Apenas visualização |

### Proteção de Endpoints

Todos os endpoints (exceto `/auth/login`) requerem autenticação válida.

---

## Configuração do Kafka

### Tópicos Kafka

O sistema utiliza Apache Kafka para eventos assíncronos:

#### Tópicos Principais:
- `inventory-audit` - Auditoria de alterações no estoque
- `product-updates` - Atualizações de produtos
- `price-changes` - Mudanças de preço

### Configuração no `application.properties`

```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.producer.acks=all
```

### Produzindo Eventos

```kotlin
@Service
class AuditService(private val kafkaTemplate: KafkaTemplate<String, InventoryAuditEvent>) {
    
    fun publishAudit(event: InventoryAuditEvent) {
        kafkaTemplate.send("inventory-audit", event)
    }
}
```

### Interface Web do Kafka

Acesse o **Kafka UI** em: `http://localhost:8081`

---

## Banco de Dados

### Configuração MySQL

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rtr
spring.datasource.username=root
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
```

### Migrações de Esquema

O projeto utiliza **Hibernate DDL** com modo `validate`, portanto o schema deve ser criado manualmente ou via ferramentas de migração como Flyway/Liquibase.

### Estrutura das Tabelas Principais

```sql
-- Tabela de usuários
CREATE TABLE user (
    id CHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    store_id CHAR(36),
    role_id CHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE
);

-- Tabela de produtos
CREATE TABLE product (
    id CHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    card_id CHAR(36),
    other_product_id CHAR(36),
    price DECIMAL(15,2) NOT NULL,
    store_id CHAR(36) NOT NULL,
    product_condition VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE
);

-- Tabela de estoque
CREATE TABLE inventory (
    id CHAR(36) PRIMARY KEY,
    product_id CHAR(36) NOT NULL UNIQUE,
    quantity INT NOT NULL DEFAULT 0,
    created_by CHAR(36),
    updated_by CHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE
);
```

---

## Testes

### Executando os Testes

```bash
mvn test
```

### Estrutura de Testes

```
src/test/kotlin/
└── com/rtr/store_manager_api/
    └── StoreManagerApiApplicationTests.kt
```

### Cobertura de Testes

Para gerar relatório de cobertura:

```bash
mvn clean test jacoco:report
```

---

## Padrões de Código

### Convenções de Nomenclatura

- **Classes**: PascalCase (`ProductService`, `CardController`)
- **Funções**: camelCase (`createProduct`, `getAllInventory`)
- **Constantes**: UPPER_SNAKE_CASE (`MAX_QUANTITY`, `DEFAULT_PRICE`)
- **Pacotes**: lowercase (`com.rtr.store_manager_api`)

### Formatação JSON (Snake Case)

O sistema utiliza **snake_case** para JSON:

```json
{
  "product_id": "123",
  "product_name": "Charizard",
  "created_at": "2024-01-01T10:00:00"
}
```

### Soft Delete

Todas as entidades herdam de `BaseEntity` e possuem o campo `deleted`:

```kotlin
abstract class BaseEntity {
    var deleted: Boolean = false
    var createdAt: LocalDateTime = LocalDateTime.now()
    var updatedAt: LocalDateTime = LocalDateTime.now()
    var createdBy: String? = null
    var updatedBy: String? = null
}
```

### Tratamento de Erros

```json
{
  "timestamp": "2024-01-01T10:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Produto não encontrado",
  "path": "/store-manager-api/products/123"
}
```

---

## Contribuindo

### Como Contribuir

1. **Fork** o repositório
2. Crie uma **branch** para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. **Commit** suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. **Push** para a branch (`git push origin feature/nova-funcionalidade`)
5. Abra um **Pull Request**

### Padrão de Commits

```
feat: adiciona endpoint de relatórios
fix: corrige cálculo de preço médio
docs: atualiza documentação da API
refactor: refatora serviço de inventário
test: adiciona testes para ProductService
```

---

## Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---

## Autores

**RTR - Rapazes Tech Reformed**

- GitHub: [@RTR-RapazesTechReformed](https://github.com/RTR-RapazesTechReformed)

---

## Suporte

Para dúvidas e suporte:

- Email: suporte@rtr.com.br
- Issues: [GitHub Issues](https://github.com/RTR-RapazesTechReformed/store-manager-api/issues)

---

## Changelog

### Versão 0.0.1-SNAPSHOT
- Implementação inicial da API
- CRUD completo de produtos, cartas e coleções
- Sistema de autenticação e autorização
- Dashboard com métricas básicas
- Integração com Apache Kafka
- Auditoria de movimentações de estoque

---

## Roadmap

- [ ] Implementar refresh tokens
- [ ] Adicionar testes de integração
- [ ] Criar documentação Swagger/OpenAPI
- [ ] Implementar cache com Redis
- [ ] Adicionar suporte a múltiplos idiomas
- [ ] Desenvolver interface administrativa
- [ ] Integração com APIs externas de precificação
- [ ] Sistema de notificações em tempo real
- [ ] Relatórios avançados em PDF
- [ ] App mobile com React Native

---

**Desenvolvido por RTR - Rapazes Tech Reformed**
