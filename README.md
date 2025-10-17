# SmartTruck - Sistema de Chatbot para Diagnóstico de Caminhões

## 📋 Sobre o Projeto
O SmartTruck é um sistema de chatbot que ajuda a diagnosticar problemas em caminhões através de códigos de erro. Usando WhatsApp ou Telegram, os usuários podem enviar códigos de erro e receber soluções automaticamente através de Inteligência Artificial.

## 🎯 Objetivo do Projeto
Este projeto serve como exemplo prático de uma aplicação Java moderna, utilizando:
- Spring Boot para criar APIs REST
- Chatbots (WhatsApp e Telegram)
- Inteligência Artificial para respostas automáticas
- Arquitetura Limpa (Clean Architecture)

## 📁 Estrutura de Pastas

O projeto segue a organização orientada à Arquitetura Limpa (Clean Architecture). A seguir está a estrutura principal do código e a responsabilidade de cada pasta/pacote.

### Código-fonte Java

`src/main/java/com/smarttruck`

- `application` — Camada de aplicação: coordena casos de uso e integração entre apresentação e domínio.
   - `service` — Implementações que executam a lógica orquestrada pelos casos de uso.
   - `usecase` — Contratos / interfaces que representam operações (casos de uso) da aplicação.

- `config` — Configurações do Spring e integrações (beans, propriedades, configurações dos bots e security).

- `domain` — Modelo de domínio (regras de negócio puras):
   - `model` — Entidades e value objects do domínio.
   - `factory` — Fábricas para criar entidades com estados iniciais.
   - `repository` — Interfaces que definem como acessar/armazenar entidades (sem dependência de tecnologia).

- `infrastructure` — Implementações concretas (adapters/ports):
   - `persistence` — Entidades JPA, repositórios Spring Data e implementações in-memory utilizadas em testes.
   - `restclient` — Clientes HTTP para serviços externos (se aplicável).

- `presentation` — Camada de entrada/saída (API):
   - `controller` — Endpoints REST (Controllers)
   - `dto` — Objetos de transferência de dados (requests/responses)
   - `mapper` — Conversores entre DTOs e modelos de domínio

- `shared` — Componentes utilitários e compartilhados (exceções, constantes, utilitários de teste)

### Recursos e Configurações

`src/main/resources`

- `application.yml`, `application-dev.yml`, `application-prod.yml` — Configurações por perfil.
- `db/migration` — Migrations do Flyway (ex.: V1__create_tickets_table.sql).
- `sql/`, `templates/` — Arquivos auxiliares e templates usados pela aplicação.

### Testes

`src/test/java`

- A estrutura de testes espelha a estrutura do código principal para facilitar testes unitários e de integração.
- Inclui: testes unitários, `@DataJpaTest` para adapters JPA, testes de controller (MockMvc) e testes de integração com Testcontainers (quando Docker está disponível).

---
Estas convenções ajudam a manter as regras de negócio isoladas em `domain` e a colocar dependências de infraestrutura dentro de `infra`, reduzindo acoplamento e facilitando testes.

## 🔄 Fluxo de Dados na Arquitetura Limpa
1. O usuário envia mensagem pelo WhatsApp/Telegram
2. A mensagem chega na camada de Presentation (Controllers)
3. O Controller converte a requisição em um caso de uso
4. O caso de uso coordena a lógica usando os serviços
5. Os serviços usam as entidades do domínio e repositórios
6. Os repositórios salvam/recuperam dados usando a infraestrutura
7. A resposta volta pelo mesmo caminho até o usuário

## 🚀 Como Executar o Projeto
1. Clone o repositório
2. Configure as variáveis de ambiente para os bots:
   ```properties
   TELEGRAM_BOT_USERNAME=seu_bot
   TELEGRAM_BOT_TOKEN=seu_token
   WHATSAPP_VERIFY_TOKEN=seu_token
   WHATSAPP_ACCESS_TOKEN=seu_token
   WHATSAPP_APP_SECRET=seu_secret
   ```
3. Execute o projeto usando Maven:
   ```bash
   mvn spring-boot:run
   ```

   ### Perfis (dev / prod)

   O projeto agora fornece dois perfis de execução para diferenciar comportamento entre desenvolvimento e produção:

   - `dev` (padrão para desenvolvimento): usa H2 por padrão e habilita o Hibernate `ddl-auto=update`. Flyway fica desabilitado.
   - `prod`: espera-se que o banco (Postgres) e variáveis sensíveis sejam fornecidos por variáveis de ambiente/secret manager. Flyway é habilitado neste perfil e o Hibernate não deve alterar o esquema (`ddl-auto=none`).

   Como executar com um profile específico:

   ```bash
   # Dev (usa `application-dev.yml`)
   set -o allexport; source .env; set +o allexport; mvn -Dspring.profiles.active=dev spring-boot:run

   # Prod (usa `application-prod.yml`)
   set -o allexport; source .env; set +o allexport; mvn -Dspring.profiles.active=prod -DskipTests package
   java -jar target/*.jar --spring.profiles.active=prod
   ```

## �️ Configurando PostgreSQL e variáveis de ambiente

### 1) Criar banco e usuário (psql)
Conecte-se ao PostgreSQL como administrador e execute:

```sql
-- conecte-se: psql -U postgres
CREATE DATABASE smarttruck_db;
CREATE USER smarttruck_user WITH PASSWORD 's3cret';
GRANT ALL PRIVILEGES ON DATABASE smarttruck_db TO smarttruck_user;
\q
```

### 2) Usar `.env` para carregar variáveis (opcional)
Crie um arquivo `.env` na raiz do projeto (existe `.env.example` como modelo). Para carregar as variáveis no seu shell (Git Bash / WSL):

```bash
# Carrega as variáveis do .env para o ambiente atual (bash / WSL)
set -o allexport; source .env; set +o allexport

# Alternativa: export e executar em uma linha
# export $(grep -v '^#' .env | xargs) && mvn spring-boot:run
```

### 3) Propriedades importantes no `.env` / variáveis de ambiente

- SPRING_DATASOURCE_URL (ex: jdbc:postgresql://localhost:5432/smarttruck_db)
- SPRING_DATASOURCE_USERNAME
- SPRING_DATASOURCE_PASSWORD
- SPRING_JPA_DATABASE_PLATFORM (ex: org.hibernate.dialect.PostgreSQLDialect)
- SPRING_JPA_HIBERNATE_DDL_AUTO (recomendado: validate em produção)

### 4) Executar a aplicação com PostgreSQL

Após carregar as variáveis de ambiente (ou configurar no sistema), execute:

```bash
mvn -DskipTests package
mvn spring-boot:run
```

Ao iniciar, o Flyway aplicará automaticamente as migrations localizadas em `src/main/resources/db/migration`.

## 🧭 Observações
- Mantenha `.env` fora do controle de versão. Use `.env.example` para indicar variáveis necessárias.
- Em produção, prefira gerenciadores de segredos (Azure Key Vault, AWS Secrets Manager) em vez de `.env`.

## ✨ Executando as migrations manualmente (Flyway)

Se sua versão do PostgreSQL tem incompatibilidade com a versão do Flyway embarcada no Spring Boot, é possível rodar as migrations manualmente antes de iniciar a aplicação usando o plugin Maven do Flyway.

Exemplo de execução (usando variáveis de ambiente do `.env` ou substituindo na linha de comando):

```bash
# Carregue seu .env (bash)
set -o allexport; source .env; set +o allexport

# Execute as migrations com credenciais do .env
mvn -Dflyway.url=$SPRING_DATASOURCE_URL -Dflyway.user=$SPRING_DATASOURCE_USERNAME -Dflyway.password=$SPRING_DATASOURCE_PASSWORD flyway:migrate

# Ou passe explicitamente (substitua valores)
mvn -Dflyway.url=jdbc:postgresql://localhost:5432/smarttruck_db -Dflyway.user=smarttruck_user -Dflyway.password=your_password flyway:migrate
```

Isso permite aplicar as migrations diretamente no banco sem depender da inicialização automática do Flyway pelo Spring Boot.

## cURL para testar login válido
curl -v -X POST http://localhost:8080/api/login   -H "Content-Type: application/json"   -d '{"email":"admin@smarttruck.com","password":"LouvadoSejaDeus"}'

## cURL para testar login inválido
curl -v -X POST http://localhost:8080/api/login   -H "Content-Type: application/json"   -d '{"email":"invalido@smarttruck.com","password":"LouvadoSejaDeus"}'


## �📚 Links Úteis para Estudo
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Arquitetura Limpa](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [API do Telegram](https://core.telegram.org/bots/api)
- [API do WhatsApp](https://developers.facebook.com/docs/whatsapp/api/overview)
