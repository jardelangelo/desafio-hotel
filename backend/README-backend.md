# Desafio Hotel — Backend

API REST para gerenciamento de **hóspedes** e **check-ins** (presentes/ausentes), com persistência em **PostgreSQL**, migrations via **Flyway** e logs via **Log4j2**.

---

## Tecnologias
- **Java 25**
- **Spring Boot**
- **Maven**
- **PostgreSQL** (via Docker)
- **Flyway** (migrations)
- **Log4j2** (logs em console e arquivo)

---

## Pré-requisitos
- **JDK 25**
- **Docker + Docker Compose**
- **NetBeans** (ou outra IDE)

---

## Como rodar (banco + API)

### 1) Subir o PostgreSQL (Docker)
Na pasta onde está o `docker-compose.yml`:

```bash
docker compose up -d
```

Verificar se o container está de pé:

```bash
docker ps
```

### 2) Rodar o backend (NetBeans)
No NetBeans:
1. Abra o projeto Maven (`backend/`)
2. Execute **Clean and Build**
3. Execute **Run Project** (configurado para `spring-boot:run`)

A API deve subir em:

- `http://localhost:8080`

---

## Migrations (Flyway)
As migrations ficam em:

- `src/main/resources/db/migration`

Ao iniciar a aplicação, o Flyway aplica automaticamente as migrations no PostgreSQL.

Para verificar tabelas no banco via container:

```bash
docker exec -it hotel_db psql -U hotel -d hotel -c "\dt"
```

---

## Logs (Log4j2)

### Saídas de log
- **Console** (Output do NetBeans)
- **Arquivo**: `backend/logs/desafio-hotel.log`

### Log por requisição
Cada request gera uma linha semelhante a:

```
HTTP GET /api/hospedes -> 200 (127 ms)
```

---

## Endpoints

> Base URL: `http://localhost:8080`

### Hóspedes

#### Criar hóspede
`POST /api/hospedes`

Exemplo de body (JSON):
```json
{
  "nome": "Fulano de Tal",
  "documento": "12345678900",
  "telefone": "11999990000"
}
```

#### Listar hóspedes
`GET /api/hospedes`

#### Buscar hóspede por ID
`GET /api/hospedes/{id}`

Exemplo:
`GET /api/hospedes/1`

#### Atualizar hóspede (nome/telefone)
`PUT /api/hospedes/{id}`

Exemplo:
```json
{
  "nome": "Fulano Atualizado",
  "telefone": "11988887777"
}
```

> Observação: campos não enviados (ou vazios) são mantidos com o valor atual.

#### Excluir hóspede
`DELETE /api/hospedes/{id}`

#### Buscar por termo (nome/documento/telefone)
`GET /api/hospedes/buscar?termo=...`

Exemplo:
`GET /api/hospedes/buscar?termo=Fulano`

---

### Check-ins

#### Realizar check-in
`POST /api/checkins`

Exemplo de body (JSON):
```json
{
  "idHospede": 1,
  "dataEntrada": "2026-02-04",
  "dataSaida": "2026-02-06",
  "adicionalVeiculo": true
}
```

> Observação: as regras de cálculo de hospedagem e consulta de presentes/ausentes seguem a implementação do domínio/casos de uso.

#### Consultar hóspedes presentes
`GET /api/checkins/presentes`

#### Consultar hóspedes ausentes
`GET /api/checkins/ausentes`

---

## Padrão de erro (JSON)
A API retorna erros padronizados.

Exemplo (404):
```json
{
  "timestamp": "2026-02-04T02:24:36.987550400Z",
  "status": 404,
  "erro": "hospede nao encontrado"
}
```

Exemplo (400):
```json
{
  "timestamp": "2026-02-04T02:24:36.987550400Z",
  "status": 400,
  "erro": "mensagem de validacao/regra"
}
```

Exemplo (500):
```json
{
  "timestamp": "2026-02-04T02:24:36.987550400Z",
  "status": 500,
  "erro": "erro interno"
}
```

---

## Testes
Para executar testes via IDE, use os testes em **Test Packages**.

Via Maven (opcional):
```bash
./mvnw clean test
```

---

## Observações
- O banco é inicializado por migrations (Flyway).
- Logs são gravados em arquivo para facilitar depuração e rastreabilidade.
- O projeto segue organização em camadas (domínio, aplicação, infraestrutura, API) buscando princípios de Clean Code / SOLID.
