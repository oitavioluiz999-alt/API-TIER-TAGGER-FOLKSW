# TierTagger API

API REST + WebSocket do sistema de Tiers para Minecraft.

## Stack
- Java 21
- Spring Boot 3.3
- PostgreSQL
- Flyway
- JWT (jjwt)
- WebSocket
- Caffeine (cache)
- Springdoc OpenAPI (Swagger)
- Rate Limit (in-memory)
- Docker / Railway

## Subir localmente (H2 em memória)

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Documentação: http://localhost:8080/swagger-ui.html

## Subir com PostgreSQL (Docker)

```bash
docker compose up -d db
export DB_URL=jdbc:postgresql://localhost:5432/tiertagger
export DB_USERNAME=tiertagger
export DB_PASSWORD=tiertagger
export JWT_SECRET=$(openssl rand -base64 64)
mvn spring-boot:run
```

## Endpoints principais

| Método | Rota                         | Descrição                             |
|--------|------------------------------|---------------------------------------|
| POST   | /api/auth/login              | Login (JWT)                           |
| POST   | /api/auth/refresh            | Renova access token                   |
| POST   | /api/auth/logout             | Revoga sessão                         |
| POST   | /api/tier/set                | Define tier (staff)                   |
| POST   | /api/tier/remove             | Remove tier (staff)                   |
| GET    | /api/player/{name}           | Tiers de um jogador                   |
| GET    | /api/players                 | Lista jogadores                       |
| GET    | /api/leaderboard/{mode}      | Leaderboard por modalidade            |
| GET    | /api/history/{player}        | Histórico do jogador                  |
| GET    | /api/modes                   | Modalidades                           |
| GET    | /api/tiers                   | Tiers                                 |
| GET    | /api/search/{query}          | Busca de jogadores                    |
| GET    | /api/version                 | Versão da API e do client             |
| WS     | /ws/updates                  | Recebe atualizações em tempo real     |

## Exemplo /tier set

```bash
curl -X POST http://localhost:8080/api/tier/set \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <ACCESS_TOKEN>" \
  -d '{"player":"Luiz","mode":"crystal","tier":"HT5","reason":"Mudou de tier"}'
```

## Configuração (variáveis de ambiente)

Copie `.env.example` para `.env` e ajuste. Veja a seção [Deploy](../docs/DEPLOY.md).

## Docker

```bash
docker build -t tiertagger-api .
docker run -p 8080:8080 --env-file .env tiertagger-api
