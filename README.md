# Desafio Hotel — Full Stack (Frontend + Backend)

Sistema de gestão simples de hotel, com cadastro de hóspedes, check-in e consultas paginadas de pessoas **presentes** e **ausentes**, exibindo também informações como total gasto e última hospedagem.

---

## Sumário
- [Visão geral](#visão-geral)
- [Funcionalidades](#funcionalidades)
- [Regras de negócio](#regras-de-negócio)
- [Arquitetura](#arquitetura)
- [Tecnologias](#tecnologias)
- [Estrutura do repositório](#estrutura-do-repositório)
- [Como rodar](#como-rodar)
  - [Rodar com Docker (recomendado)](#rodar-com-docker-recomendado)
  - [Rodar local (sem-docker)](#rodar-local-sem-docker)
- [Backend](#backend)
  - [Migrations Flyway](#migrations-flyway)
  - [Logs (Log4j2) e rastreabilidade](#logs-log4j2-e-rastreabilidade)
  - [Endpoints](#endpoints)
  - [Padrão de erro (JSON)](#padrão-de-erro-json)
  - [Testes](#testes)
- [Frontend](#frontend)
  - [Proxy /api](#proxy-api)
  - [Paginação](#paginação)

---

## Visão geral

O projeto implementa um fluxo completo:
1. **Cadastro de hóspedes** (CRUD)
2. **Check-in** com busca e seleção de hóspede (nome/documento/telefone)
3. **Consultas paginadas**
   - Pessoas ainda presentes
   - Pessoas que já deixaram o hotel

---

## Funcionalidades

### Frontend
- Cadastro de hóspedes (modal)
- Tela de check-in com busca e seleção de hóspede
- Consulta de presentes/ausentes com paginação + seleção de “por página”
- Mensagens de validação/erros amigáveis no nível do usuário

### Backend
- API REST para hóspedes e check-ins
- Persistência em PostgreSQL
- Migrations automáticas via Flyway
- Logs (Log4j2) em console e arquivo com **requestId** por requisição

---

## Regras de negócio

Valores de diárias:
- Quarto **de segunda à sexta**: R$ 120,00
- Quarto **em finais de semana**: R$ 150,00

Adicional de garagem:
- Garagem **de segunda à sexta**: R$ 15,00
- Garagem **em finais de semana**: R$ 20,00

Check-out após 16:30:
- Caso a saída ocorra após 16:30, cobra-se **uma diária adicional**.

> Observação: essas regras são aplicadas no backend.

---

## Arquitetura

### Visão geral
- **Frontend (Angular)**: UI, consumo da API REST, paginação e filtros
- **Backend (Spring Boot)**: regras de negócio, validações, persistência e endpoints
- **PostgreSQL**: banco de dados
- **Flyway**: migrations (versionamento do schema)

### Padrão em camadas (backend)
- **Controllers (API)**: camada HTTP (request/response)
- **UseCases / Services**: regra de negócio e orquestração
- **Repositories**: persistência (Spring Data JPA)

---

## Tecnologias

### Backend
- **Java 25**
- **Spring Boot 4.0.x**
- **Maven** (build/dependências) + **Maven Wrapper** (`./mvnw`)
- **PostgreSQL** (Docker)
- **Flyway** (migrations)
- **Log4j2** (logs em console e arquivo)

### Frontend
- **Angular 21.x**
- **Node.js + NPM**
- Proxy local para `/api` (dev) e consumo via HTTP Client

---

## Estrutura do repositório

```text
/
├─ backend/
│  ├─ src/main/java/...                 # código do backend
│  ├─ src/main/resources/
│  │  ├─ application.yaml               # config (datasource/flyway)
│  │  ├─ log4j2-spring.xml              # logs
│  │  └─ db/migration/                  # migrations Flyway
│  ├─ mvnw / mvnw.cmd                   # Maven Wrapper
│  └─ pom.xml
│
├─ frontend/
│  ├─ src/app/features/                 # feature folders (hóspedes/check-in/consultas)
│  ├─ proxy.conf.json                   # proxy /api -> backend
│  └─ package.json
│
└─ docker-compose.yml                   # (no repo atual: sobe só o PostgreSQL)
```

---

## Como rodar

### Rodar com Docker (recomendado)

Pré-requisitos:
- Docker e Docker Compose

1) Subir o banco (PostgreSQL):
```bash
docker compose up -d
```

2) Verificar se o container está de pé:
```bash
docker ps
```

3) Rodar o backend (local):
```bash
cd backend
./mvnw spring-boot:run
```

4) Rodar o frontend (local):
```bash
cd frontend
npm install
npm start
```

Acessos:
- Frontend: http://localhost:4200
- Backend:  http://localhost:8080

Reset completo do banco (apaga volume):
```bash
docker compose down -v
```

> Observação: com a configuração atual do projeto, o **Flyway roda automaticamente ao iniciar o backend** (ver seção “Migrations Flyway”).

---

### Rodar local (sem Docker)

Você pode usar um PostgreSQL local instalado, mas precisa ajustar o datasource do backend.

1) Ajustar `backend/src/main/resources/application.yaml`:
- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

2) Rodar backend:
```bash
cd backend
./mvnw spring-boot:run
```

3) Rodar frontend:
```bash
cd frontend
npm install
npm start
```

---

# Backend

## Migrations Flyway

As migrations ficam em:
- `backend/src/main/resources/db/migration`

Ao iniciar a aplicação, o Flyway aplica automaticamente as migrations no PostgreSQL.

Para verificar tabelas no banco via container:
```bash
docker exec -it hotel_db psql -U hotel -d hotel -c "\dt"
```

---

## Logs (Log4j2) e rastreabilidade

### Saídas de log
- **Console**
- **Arquivo**: `logs/desafio-hotel.log` (caminho relativo ao diretório em que a aplicação é executada)

### Log por requisição (requestId)
Cada request recebe um `requestId` (header `X-Request-Id`). Se o cliente não enviar, o backend gera um automaticamente.

Exemplo de log por request:
```text
HTTP GET /api/hospedes -> 200 (127 ms)
```

Exemplo de log com requestId no padrão:
```text
2026-02-07 19:01:12.345 INFO  ... requestId=... - HTTP GET /api/checkins/presentes?page=0&size=10 -> 200 (22 ms)
```

> Dica: ao debugar, copie o `X-Request-Id` do response e filtre no arquivo de log.

---

## Endpoints

> Base URL: `http://localhost:8080`

### Hóspedes

#### Criar hóspede
`POST /api/hospedes`

Body (JSON):
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

Body (JSON):
```json
{
  "idHospede": 1,
  "dataEntrada": "2026-02-04",
  "dataSaida": "2026-02-06",
  "adicionalVeiculo": true
}
```

#### Consultar hóspedes presentes (paginado)
`GET /api/checkins/presentes?page=&size=`

Exemplo:
`GET /api/checkins/presentes?page=0&size=10`

#### Consultar hóspedes ausentes (paginado)
`GET /api/checkins/ausentes?page=&size=`

Exemplo:
`GET /api/checkins/ausentes?page=0&size=10`

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

Exemplo (409):
```json
{
  "timestamp": "2026-02-04T02:24:36.987550400Z",
  "status": 409,
  "erro": "documento de hóspede ja cadastrado"
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

Via Maven Wrapper:
```bash
cd backend
./mvnw clean test
```

Via IDE (NetBeans/IntelliJ/Eclipse):
- Execute os testes em **Test Packages**.

---

# Frontend

## Proxy /api

No desenvolvimento local, o frontend usa proxy para `/api` apontando para o backend.

Arquivo:
- `frontend/proxy.conf.json`

Executar:
```bash
cd frontend
npm start
```

---

## Paginação

As consultas de presentes/ausentes são paginadas por:
- `page`: página (0-based)
- `size`: quantidade de registros por página

Exemplo:
```http
GET /api/checkins/presentes?page=0&size=10
```

## Apresentação

### Cadastro de Hóspede
![CadastroHospede](https://github.com/user-attachments/assets/f08dd068-ad7d-420a-a226-195c95acc119)

### Cadastro de CheckIn
![CheckInHospede](https://github.com/user-attachments/assets/24c64464-a47a-4637-ae76-5a72cdece1c2)

