# Incidents Management

## 📌 Visão geral
**Incidents Management** é uma aplicação fullstack para gerir ocorrências.  
Conta com backend em **Java 17 + Spring Boot**, frontend em **Angular 16+**, banco de dados **PostgreSQL** e deploy via **Docker Compose**.  
A autenticação é feita com **JWT** e a API está documentada em **Swagger/OpenAPI**.  

---

## 🎯 Objetivo
O projeto tem como objetivo oferecer um sistema de **Gestão de Ocorrências**, evitando duplicação de código tanto no backend quanto no frontend, com lógica reutilizável e padronizada.  

---

## 🚀 Funcionalidades
- **Autenticação e registro** de usuários (JWT).
- **CRUD completo de Incidents**:
  - Campos: título, descrição, prioridade, status, responsável, tags, datas.
- **Comentários** vinculados a incidents.
- **Filtros, ordenação e paginação** em listagens.
- **Estatísticas agregadas** por status e prioridade.
- **Swagger UI** para documentação da API.
- **Cache de leitura com Caffeine** (GETs), com invalidação automática em escritas.
- **Frontend Angular**:
  - Telas de login, registro, listagem, detalhe e edição de incidents.
  - Filtros com query params.
  - Estatísticas e perfil de usuário.
  - Tratamento de erros e feedback visual com Angular Material.

---

## 🛠 Tecnologias
### Backend
- Java 17
- Spring Boot 3+
- Spring Data JPA
- Spring Security + JWT
- springdoc-openapi (Swagger)
- Caffeine Cache
- PostgreSQL

### Frontend
- Angular 16+
- Angular Material
- RxJS

### Infraestrutura
- Docker & Docker Compose
- Flyway/Liquibase (migrations opcionais)

---

## 📂 Estrutura do Repositório

```
/backend
/src/main/java/com/incidents
controller/
service/
repository/
security/
util/
pom.xml
/frontend
src/app/
core/
features/
shared/
package.json
docker-compose.yml
README.md
```

---

## ⚙️ Requisitos
- Docker & Docker Compose  
**ou**
- Java 17 + Maven (backend)  
- Node.js 18+ + npm (frontend)

---

## ▶️ Como executar

### 1. Via Docker
1. Crie um arquivo `.env` na raiz:
   ```env
   POSTGRES_DB=incidents_db
   POSTGRES_USER=incidents_user
   POSTGRES_PASSWORD=incidents_pass
   JWT_SECRET=uma_chave_segura
   JWT_EXPIRATION=3600000
   SERVER_PORT=8080
   ```

2. Suba os serviços:
    ```
    docker compose up --build
    ```

3. Acesse:
    ```
    - Backend: http://localhost:8080

    - Swagger: http://localhost:8080/swagger-ui/index.html

    - Frontend: http://localhost:4200
    ```


## 🔑 Autenticação (JWT)

- Login: POST /api/auth/login

- Registro: POST /api/auth/register

- Header obrigatório nos endpoints protegidos:
    ```
    Authorization: Bearer <TOKEN>
    ```

## 📖 Documentação

Swagger UI disponível em:
    ```
    http://localhost:8080/swagger-ui/index.html
    ```

## 🗃️ Cache

- Configurado com Caffeine.

- Caches nomeados: incidents, incidentById, statsByStatus, statsByPrioridade, commentsByIncident.

- @Cacheable em consultas de leitura.

- @CacheEvict em operações de escrita para garantir consistência.