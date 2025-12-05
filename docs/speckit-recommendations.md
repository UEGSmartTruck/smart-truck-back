# SPECKIT - Recomendações Detalhadas para SmartTruck Backend

**Gerado por**: SPECKIT (Automated Quality Principles Generator)
**Data**: 2025-12-03
**Projeto**: SmartTruck Backend (Java 21 + Spring Boot 3.5)
**Repositório**: `smart-truck-back`

---

## Índice

1. [Análise do Código Existente](#análise-do-código-existente)
2. [Princípios Detalhados (com Do/Don't)](#princípios-detalhados)
3. [Testes para Cada Princípio](#testes-para-cada-princípio)
4. [Regras Automatizáveis](#regras-automatizáveis)
5. [Checklist de Pull Request](#checklist-de-pull-request)
6. [Integração CI/CD](#integração-cicd)
7. [Smells Detectados e Priorização](#smells-detectados-e-priorização)
8. [Migração Incremental](#migração-incremental)

---

## Análise do Código Existente

### Estrutura de Pacotes Detectada

```
src/main/java/com/smarttruck/
├── application/
│   ├── service/           # ✅ Services implementam use cases
│   │   ├── AuthenticateUserService.java
│   │   ├── CreateTicketService.java
│   │   ├── CreateUserService.java
│   │   └── ListAllUserService.java
│   └── usecase/           # ✅ Interfaces de casos de uso
│       ├── AuthenticateUserUseCase.java
│       ├── CreateTicketUseCase.java
│       └── CreateUserUseCase.java
├── config/
│   └── SecurityConfig.java
├── domain/
│   ├── factory/           # ✅ Factories para criação de entidades
│   │   ├── AiSolvedTicketFactory.java
│   │   ├── EscalatedTicketFactory.java
│   │   └── TicketFactory.java
│   ├── model/             # ✅ Domain models sem dependências externas
│   │   ├── Ticket.java
│   │   ├── TicketStatus.java
│   │   └── User.java
│   └── repository/        # ✅ Interfaces de repositório no domínio
│       ├── TicketRepository.java
│       └── UserRepository.java
├── infrastructure/
│   └── persistence/       # ✅ Adapters JPA separados
│       ├── JpaTicket.java
│       ├── JpaTicketMapper.java
│       ├── JpaTicketRepository.java
│       ├── JpaUser.java
│       ├── JpaUserMapper.java
│       ├── JpaUserRepository.java
│       ├── SpringDataJpaTicketRepository.java
│       └── SpringDataJpaUserRepository.java
├── presentation/
│   ├── controller/        # ✅ Controllers REST
│   │   ├── LoginController.java
│   │   ├── TicketController.java
│   │   └── UserController.java
│   ├── dto/               # ✅ DTOs (usando records)
│   │   ├── CreateTicketRequest.java
│   │   ├── CreateTicketResponse.java
│   │   ├── CreateUserRequest.java
│   │   ├── CreateUserResponse.java
│   │   ├── ErrorResponse.java
│   │   ├── LoginRequest.java
│   │   ├── MessageResponse.java
│   │   └── TokenResponse.java
│   └── mapper/            # ✅ Mappers DTO ↔ Domain
│       ├── TicketMapper.java
│       └── UserMapper.java
└── shared/
    ├── security/
    │   ├── CustomUserDetailsService.java
    │   ├── JwtAuthenticationFilter.java
    │   └── JwtTokenProvider.java
    └── token/
        └── TokenBlacklist.java
```

### Testes Detectados (23 classes)

| Tipo | Quantidade | Exemplos |
|------|------------|----------|
| **Unit Tests** | 10 | `LoginControllerTest`, `CreateUserServiceTest`, `UserMapperTest` |
| **Integration Tests** | 6 | `LoginControllerIntegrationTest`, `TicketControllerIntegrationTest` |
| **@DataJpaTest** | 4 | `JpaUserRepositoryTest`, `JpaTicketRepositoryTest` |
| **Domain Tests** | 3 | `TicketTest`, `UserTest`, `EscalatedTicketFactoryTest` |

### Pontos Fortes Identificados ✅

1. **Clean Architecture pura**: Domain sem anotações Spring/JPA
2. **Separation of Concerns**: Camadas bem definidas
3. **Testes abrangentes**: Controllers, services, repositories testados
4. **Records para DTOs**: Imutabilidade por padrão (Java 21)
5. **JWT com blacklist**: Logout seguro implementado
6. **Factory pattern**: `TicketFactory` abstrai criação de entidades
7. **Repository pattern**: Interface no domínio, implementação em infra

### Gaps e Oportunidades de Melhoria ⚠️

| Gap | Impacto | Prioridade | Effort |
|-----|---------|------------|--------|
| Sem exceções customizadas | ❌ Erros genéricos difíceis de debugar | 🔴 ALTA | 2h |
| Sem @ControllerAdvice | ❌ Try-catch repetitivo | 🔴 ALTA | 1h |
| User sem Javadoc | ⚠️ Dificulta onboarding | 🟡 MÉDIA | 30min |
| Email/phone como String | ⚠️ Validação espalhada | 🟡 MÉDIA | 4h |
| Sem JaCoCo configurado | ⚠️ Sem gate de coverage | 🟡 MÉDIA | 1h |
| Sem ArchUnit tests | ⚠️ Violações de arquitetura não detectadas | 🟢 BAIXA | 2h |

---

## Princípios Detalhados

### Princípio 1: Clean Architecture Compliance

#### ❌ DON'T: Anotações JPA no domínio

```java
// ❌ ERRADO: Domain model com @Entity
package com.smarttruck.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity // ❌ Dependência de JPA no domínio
public class User {
    @Id // ❌ Vazamento de infraestrutura
    private String id;
    private String email;
}
```

**Problemas**:
- Domínio acoplado ao JPA
- Impossível testar sem Spring Context
- Mudança de banco requer refactor do domínio

#### ✅ DO: Domain puro + JPA adapter separado

```java
// ✅ CORRETO: Domain model puro
package com.smarttruck.domain.model;

import java.time.Instant;
import java.util.UUID;

public class User {
    private final String id;
    private final String email;
    private final Instant createdAt;

    public User(String email) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.createdAt = Instant.now();
    }

    // Constructor for reconstruction (usado por mapper)
    public User(String id, String email, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
    }

    // Apenas getters, sem setters
    public String getId() { return id; }
    public String getEmail() { return email; }
    public Instant getCreatedAt() { return createdAt; }
}

// ✅ CORRETO: JPA entity separada em infrastructure
package com.smarttruck.infrastructure.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.Instant;

@Entity(name = "users")
public class JpaUser {
    @Id
    private String id;
    private String email;
    private Instant createdAt;

    // Getters e setters para JPA
}
```

**Benefícios**:
- Domínio testável sem database
- Troca de JPA para MongoDB sem tocar no domínio
- Business logic isolada

#### Testes para Este Princípio

**Unit Test: Domain sem Spring**
```java
// ✅ Testa domínio sem @SpringBootTest
@Test
void user_shouldBeCreatableWithoutSpring() {
    // Arrange & Act
    User user = new User("test@example.com");

    // Assert
    assertNotNull(user.getId());
    assertEquals("test@example.com", user.getEmail());
    assertNotNull(user.getCreatedAt());
}
```

**ArchUnit Test: Detecta violações**
```java
@Test
void domainLayer_shouldNotDependOnInfrastructure() {
    noClasses()
        .that().resideInPackage("..domain..")
        .should().dependOnClassesThat().resideInAnyPackage(
            "..infrastructure..",
            "jakarta.persistence..",
            "org.springframework.."
        )
        .check(importedClasses);
}
```

#### Regra Automatizável (Checkstyle)

```xml
<!-- Proíbe imports de JPA/Spring no domínio -->
<module name="IllegalImport">
    <property name="illegalPkgs" value="org.springframework, jakarta.persistence"/>
    <message key="import.illegal"
             value="Domain layer must not depend on framework packages"/>
</module>
```

---

### Princípio 2: Immutability and Domain Purity

#### ❌ DON'T: Setters sem validação

```java
// ❌ ERRADO: Mutabilidade sem controle
public class Ticket {
    private String id;
    private TicketStatus status;
    private Instant updatedAt;

    public void setStatus(TicketStatus status) {
        this.status = status; // ❌ Não atualiza updatedAt!
    }

    public void setId(String id) {
        this.id = id; // ❌ ID deve ser imutável
    }
}
```

**Problemas**:
- Invariantes não garantidas (`updatedAt` desatualizado)
- ID pode mudar após construção
- Estado inconsistente possível

#### ✅ DO: Campos finais + métodos com lógica de negócio

```java
// ✅ CORRETO: Imutabilidade controlada
public class Ticket {
    private final String id; // ✅ Imutável
    private final String customerId; // ✅ Imutável
    private final Instant createdAt; // ✅ Imutável

    private TicketStatus status; // Mutável, mas controlado
    private Instant updatedAt; // Atualizado junto com status

    public Ticket(String customerId, String description) {
        this.id = UUID.randomUUID().toString();
        this.customerId = customerId;
        this.status = TicketStatus.OPEN;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Atualiza status e timestamp automaticamente.
     * Garante invariante: updatedAt sempre reflete última mudança.
     */
    public void setStatus(TicketStatus status) {
        this.status = status;
        this.updatedAt = Instant.now(); // ✅ Invariante garantida
    }

    // Apenas getters para campos imutáveis
    public String getId() { return id; }
}
```

#### Testes para Este Princípio

```java
@Test
void setStatus_shouldUpdateTimestamp() {
    // Arrange
    Ticket ticket = new Ticket("customer123", "Engine error");
    Instant initialTime = ticket.getUpdatedAt();

    // Act
    Thread.sleep(10); // Simula passagem de tempo
    ticket.setStatus(TicketStatus.RESOLVED);

    // Assert
    assertEquals(TicketStatus.RESOLVED, ticket.getStatus());
    assertTrue(ticket.getUpdatedAt().isAfter(initialTime)); // ✅ Timestamp atualizado
}

@Test
void ticketId_shouldBeImmutable() {
    Ticket ticket = new Ticket("customer123", "Problem");
    String originalId = ticket.getId();

    // ✅ Não há método setId(), compilação impede mudança
    assertEquals(originalId, ticket.getId());
}
```

#### Regra Automatizável (PMD)

```xml
<rule ref="category/java/design.xml/ImmutableField">
    <properties>
        <!-- Detecta campos que poderiam ser final -->
        <property name="ignoredAnnotations" value="lombok.Setter"/>
    </properties>
</rule>
```

---

### Princípio 3: Test Coverage and TDD Discipline

#### ❌ DON'T: Implementar antes de testar

```java
// ❌ FLUXO ERRADO: Código → Teste depois (ou nunca)

// 1️⃣ Implementa primeiro
@Service
public class CreateUserService {
    public User execute(String name, String email, String password) {
        // ... implementação complexa sem teste
    }
}

// 2️⃣ Depois escreve teste (se sobrar tempo)
// 3️⃣ Teste passa de primeira (não detecta bugs)
```

**Problemas**:
- Teste não guia design
- Código difícil de testar (acoplamento alto)
- Casos extremos esquecidos

#### ✅ DO: TDD Red-Green-Refactor

```java
// ✅ FLUXO CORRETO: Teste → Implementação → Refactor

// 1️⃣ RED: Escreve teste que falha
@Test
void execute_shouldCreateUserWithHashedPassword() {
    // Arrange
    UserRepository mockRepo = mock(UserRepository.class);
    PasswordHasher mockHasher = mock(PasswordHasher.class);
    when(mockHasher.hash("password")).thenReturn("hashed_pw");
    when(mockRepo.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

    CreateUserService service = new CreateUserService(mockRepo, mockHasher);

    // Act
    User user = service.execute("John", "john@test.com", "password");

    // Assert
    assertNotNull(user.getId());
    assertEquals("hashed_pw", user.getPasswordHash());
    verify(mockRepo).save(any(User.class));
}

// 2️⃣ GREEN: Implementa o mínimo para passar
@Service
public class CreateUserService {
    private final UserRepository repository;
    private final PasswordHasher hasher;

    public User execute(String name, String email, String password) {
        String hash = hasher.hash(password);
        User user = new User(name, email, hash);
        return repository.save(user);
    }
}

// 3️⃣ REFACTOR: Melhora código mantendo testes verdes
// (ex.: extrair validação, adicionar logging)
```

#### Matriz de Tipos de Teste

| Tipo | Escopo | Dependências | Exemplo |
|------|--------|--------------|---------|
| **Unit** | Classe isolada | Mocks | `CreateUserServiceTest` |
| **Integration** | Múltiplas camadas | Spring Context real | `LoginControllerIntegrationTest` |
| **@DataJpaTest** | Repository | Database H2 | `JpaUserRepositoryTest` |
| **Contract** | API endpoints | MockMvc | Validação de DTOs |

#### Exemplo: Contract Test

```java
@WebMvcTest(LoginController.class)
class LoginControllerContractTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticateUserUseCase useCase;

    @Test
    void login_shouldReturnTokenResponseWithCorrectStructure() throws Exception {
        // Arrange
        when(useCase.execute(any(), any())).thenReturn("jwt.token.here");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"email": "user@test.com", "password": "pass"}
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("jwt.token.here"))
            .andExpect(jsonPath("$.expiresIn").value(3600));
    }

    @Test
    void login_shouldReturn401OnInvalidCredentials() throws Exception {
        // Arrange
        when(useCase.execute(any(), any())).thenThrow(new RuntimeException());

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"email": "invalid@test.com", "password": "wrong"}
                    """))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").exists());
    }
}
```

#### Regra Automatizável (JaCoCo)

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <id>check</id>
            <goals><goal>check</goal></goals>
            <configuration>
                <rules>
                    <rule>
                        <element>BUNDLE</element>
                        <limits>
                            <limit>
                                <counter>LINE</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.75</minimum> <!-- 75% obrigatório -->
                            </limit>
                            <limit>
                                <counter>BRANCH</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.60</minimum> <!-- 60% obrigatório -->
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

---

### Princípio 4-10: [Similar detalhamento para cada princípio]

*[Reduzido por brevidade - estrutura similar para cada princípio]*

---

## Checklist de Pull Request

### 🔍 Pre-Submit Checklist (Desenvolvedor)

```markdown
## Automated Checks
- [ ] `mvn clean verify` passa localmente
- [ ] Tests executados e passando (nenhum ignored/skipped)
- [ ] Coverage ≥75% nas classes modificadas (verificar relatório JaCoCo)

## Code Quality
- [ ] Métodos com ≤20 linhas (exceto getters)
- [ ] Máx 2 níveis de indentação
- [ ] Sem `else` (preferir early return)
- [ ] Máx 3 parâmetros por método

## Architecture
- [ ] Domain models sem anotações Spring/JPA
- [ ] Controllers usam apenas DTOs (não domain models)
- [ ] Repositories retornam domain models (não JPA entities)
- [ ] Exceções customizadas lançadas (não RuntimeException genérico)

## Testing
- [ ] Testes unitários para nova lógica de negócio
- [ ] Testes de integração para novos endpoints
- [ ] Testes falham antes da implementação (TDD)
- [ ] Casos extremos cobertos (null, empty, invalid input)

## Documentation
- [ ] Javadoc em novas classes públicas
- [ ] Javadoc em novos métodos públicos com @param/@return/@throws
- [ ] README atualizado se houver mudança de setup
- [ ] Comentários explicam "por quê", não "o quê"

## Security
- [ ] Sem secrets commitados (verificar .env, yml)
- [ ] Validação de input em DTOs (@NotNull, @Email, etc.)
- [ ] Logs não expõem dados sensíveis (password, token)

## Database
- [ ] Migrations Flyway incluídas (se mudou schema)
- [ ] Rollback plan documentado (como reverter mudança)
- [ ] Índices adicionados se nova query N+1

## Performance
- [ ] Sem N+1 queries (verificar logs SQL)
- [ ] Paginação implementada se retorna listas grandes
- [ ] Cache considerado para operações repetitivas

## Rollback Plan
- [ ] Feature flag? (se mudança grande)
- [ ] Como reverter migration? (incluir down script)
- [ ] Deploy pode ser revertido sem perda de dados?
```

### 👥 Reviewer Checklist

```markdown
## Architecture Review
- [ ] Clean Architecture respeitada (domain puro, infra isolada)
- [ ] SOLID principles seguidos (SRP, OCP, LSP, ISP, DIP)
- [ ] Não há lógica de negócio em controllers
- [ ] Repository pattern correto (interface no domain)

## Code Quality Review
- [ ] Nomes de variáveis/métodos descritivos (sem `data`, `info`, `temp`)
- [ ] Complexidade aceitável (sem métodos >50 linhas)
- [ ] Sem código comentado (remover ou justificar)
- [ ] Sem duplicação (DRY principle)

## Testing Review
- [ ] Testes cobrem happy path E edge cases
- [ ] Mocks usados corretamente (não testar implementação, testar comportamento)
- [ ] Assertions claras (preferir assertEquals(expected, actual))
- [ ] Testes independentes (não dependem de ordem de execução)

## Security Review
- [ ] Input validation adequada
- [ ] Autorização verificada (endpoints protegidos?)
- [ ] Dados sensíveis não logados
- [ ] SQL injection prevenido (usar prepared statements)

## Documentation Review
- [ ] Javadoc descreve "o quê" e "por quê" (não "como")
- [ ] Casos extremos documentados
- [ ] Exceções lançadas documentadas com @throws

## Final Approval
- [ ] CI passou (build, tests, linters)
- [ ] Sem code smells críticos (SonarQube grade A)
- [ ] PR description clara (problema, solução, teste)
- [ ] Commits squashados se necessário (evitar "fix typo", "wip")
```

---

## Integração CI/CD

### Pipeline YAML Completo

```yaml
# .github/workflows/ci.yml

name: SmartTruck CI

on:
  push:
    branches: [main, dev]
  pull_request:
    branches: [main, dev]

env:
  JAVA_VERSION: '21'
  MAVEN_OPTS: -Xmx1024m

jobs:
  build-and-test:
    name: Build & Unit Tests
    runs-on: ubuntu-latest
    timeout-minutes: 10

    steps:
      - name: Checkout code
        uses: actions/checkout@v4
        with:
          fetch-depth: 0 # SonarQube needs full history

      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: ${{ env.JAVA_VERSION }}
          distribution: 'temurin'
          cache: 'maven'

      - name: Build with Maven
        run: mvn clean compile -DskipTests

      - name: Run unit tests
        run: mvn test -Dgroups="unit"

      - name: Upload test results
        if: always()
        uses: actions/upload-artifact@v4
        with:
          name: test-results
          path: target/surefire-reports/

  integration-tests:
    name: Integration Tests
    runs-on: ubuntu-latest
    needs: build-and-test
    timeout-minutes: 15

    services:
      postgres:
        image: postgres:15
        env:
          POSTGRES_DB: smarttruck_test
          POSTGRES_USER: test
          POSTGRES_PASSWORD: test
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
        ports:
          - 5432:5432

    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: ${{ env.JAVA_VERSION }}
          distribution: 'temurin'
          cache: 'maven'

      - name: Run integration tests
        env:
          SPRING_DATASOURCE_URL: jdbc:postgresql://localhost:5432/smarttruck_test
          SPRING_DATASOURCE_USERNAME: test
          SPRING_DATASOURCE_PASSWORD: test
          JWT_SECRET: test_secret_for_ci
        run: mvn verify -Dgroups="integration"

  code-quality:
    name: Code Quality Scan
    runs-on: ubuntu-latest
    needs: build-and-test

    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: ${{ env.JAVA_VERSION }}
          distribution: 'temurin'
          cache: 'maven'

      - name: Run Checkstyle
        run: mvn checkstyle:check

      - name: Run PMD
        run: mvn pmd:check

      - name: Run SpotBugs
        run: mvn spotbugs:check

      - name: Check test coverage
        run: mvn jacoco:check

      - name: SonarQube Scan
        env:
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
        run: |
          mvn sonar:sonar \
            -Dsonar.projectKey=smarttruck-backend \
            -Dsonar.host.url=https://sonarcloud.io \
            -Dsonar.organization=your-org

  security-scan:
    name: Security Vulnerability Scan
    runs-on: ubuntu-latest
    needs: build-and-test

    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: ${{ env.JAVA_VERSION }}
          distribution: 'temurin'
          cache: 'maven'

      - name: Run Snyk security scan
        uses: snyk/actions/maven@master
        env:
          SNYK_TOKEN: ${{ secrets.SNYK_TOKEN }}
        with:
          args: --severity-threshold=high

      - name: Check for secrets in code
        uses: trufflesecurity/trufflehog@main
        with:
          path: ./
          base: ${{ github.event.repository.default_branch }}
          head: HEAD

  build-docker:
    name: Build Docker Image
    runs-on: ubuntu-latest
    needs: [integration-tests, code-quality]
    if: github.ref == 'refs/heads/main' || github.ref == 'refs/heads/dev'

    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          java-version: ${{ env.JAVA_VERSION }}
          distribution: 'temurin'
          cache: 'maven'

      - name: Build JAR
        run: mvn package -DskipTests

      - name: Build Docker image
        run: |
          docker build -t smarttruck-backend:${{ github.sha }} .
          docker tag smarttruck-backend:${{ github.sha }} smarttruck-backend:latest

      - name: Push to registry
        if: github.ref == 'refs/heads/main'
        run: |
          echo ${{ secrets.DOCKER_PASSWORD }} | docker login -u ${{ secrets.DOCKER_USERNAME }} --password-stdin
          docker push smarttruck-backend:${{ github.sha }}
          docker push smarttruck-backend:latest
```

### Gates de Qualidade (SonarQube)

```properties
# sonar-project.properties

sonar.projectKey=smarttruck-backend
sonar.projectName=SmartTruck Backend
sonar.projectVersion=1.0

sonar.sources=src/main/java
sonar.tests=src/test/java
sonar.java.binaries=target/classes

# Coverage
sonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
sonar.coverage.exclusions=**/*Config.java,**/*Application.java,**/dto/**

# Quality Gates
sonar.qualitygate.wait=true
sonar.qualitygate.timeout=300

# Thresholds (falha se não atingir)
sonar.coverage.overall.line.minimum=75
sonar.coverage.overall.branch.minimum=60
sonar.duplicated_lines_density.maximum=3
sonar.complexity.method.maximum=10
```

---

## Smells Detectados e Priorização

### 🔴 Crítico (Fix em até 1 semana)

| # | Smell | Localização | Impacto | Solução |
|---|-------|-------------|---------|---------|
| 1 | **Exceções genéricas** | `LoginController:36` | ❌ Debugging difícil, erros não diferenciados | Criar `InvalidCredentialsException`, `UserNotFoundException` |
| 2 | **Sem @ControllerAdvice** | Global | ❌ Try-catch repetitivo em todos os controllers | Implementar `GlobalExceptionHandler` |
| 3 | **Secrets em yml** | `application-dev.yml` | 🔐 Vazamento potencial se commitado | Mover para variáveis de ambiente |

### 🟡 Importante (Fix em 1 sprint)

| # | Smell | Localização | Impacto | Solução |
|---|-------|-------------|---------|---------|
| 4 | **Primitive obsession** | `User.java:9-12` | ⚠️ Validação espalhada | Criar `Email`, `PhoneNumber` value objects |
| 5 | **Javadoc ausente** | `User.java`, múltiplas classes | ⚠️ Onboarding lento | Adicionar Javadoc em classes públicas |
| 6 | **Sem coverage gate** | `pom.xml` | ⚠️ Coverage pode cair sem aviso | Configurar JaCoCo com threshold 75% |

### 🟢 Desejável (Backlog)

| # | Smell | Localização | Impacto | Solução |
|---|-------|-------------|---------|---------|
| 7 | **Setters públicos desnecessários** | `Ticket.java:89` | 🤔 Possível quebra de encapsulamento | Avaliar se `setUpdatedAt` é realmente necessário |
| 8 | **Sem ArchUnit tests** | `src/test/` | 🤔 Violações de arquitetura não automatizadas | Adicionar testes de arquitetura |
| 9 | **API sem versioning** | Controllers | 🤔 Breaking changes difíceis de gerenciar | Implementar `/api/v1/...` |

---

## Migração Incremental

### Sprint 1: Exceções e Error Handling (5h total)

**Objetivo**: Padronizar tratamento de erros em toda API.

#### Passo 1.1: Criar exceções customizadas (1h)

```java
// src/main/java/com/smarttruck/shared/exceptions/InvalidCredentialsException.java
package com.smarttruck.shared.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String email) {
        super("Invalid credentials for: " + email);
    }
}

// UserNotFoundException.java
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("User not found: " + userId);
    }
}

// TicketNotFoundException.java
public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(String ticketId) {
        super("Ticket not found: " + ticketId);
    }
}
```

#### Passo 1.2: Implementar @ControllerAdvice (2h)

```java
// src/main/java/com/smarttruck/presentation/exception/GlobalExceptionHandler.java
package com.smarttruck.presentation.exception;

import com.smarttruck.presentation.dto.ErrorResponse;
import com.smarttruck.shared.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        logger.warn("Authentication failed: {}", ex.getMessage());
        return ResponseEntity.status(401)
            .body(new ErrorResponse(
                "Usuário ou senha inválidos",
                Instant.now().toString(),
                Map.of("code", "INVALID_CREDENTIALS")
            ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        logger.warn("User lookup failed: {}", ex.getMessage());
        return ResponseEntity.status(404)
            .body(new ErrorResponse(
                "Usuário não encontrado",
                Instant.now().toString(),
                Map.of("code", "USER_NOT_FOUND")
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Unexpected error", ex);
        return ResponseEntity.status(500)
            .body(new ErrorResponse(
                "Erro interno do servidor",
                Instant.now().toString(),
                Map.of("code", "INTERNAL_ERROR")
            ));
    }
}
```

#### Passo 1.3: Refatorar LoginController (1h)

```java
// ANTES
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
        String token = authenticateUserUseCase.execute(request.email(), request.password());
        return ResponseEntity.ok(new TokenResponse(token, 3600));
    } catch (RuntimeException e) { // ❌ Genérico
        return ResponseEntity.status(401).body(new ErrorResponse("Usuário ou senha inválidos"));
    }
}

// DEPOIS
@PostMapping("/login")
public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
    // ✅ Sem try-catch, @ControllerAdvice cuida dos erros
    String token = authenticateUserUseCase.execute(request.email(), request.password());
    return ResponseEntity.ok(new TokenResponse(token, 3600));
}
```

#### Passo 1.4: Adicionar testes de contract (1h)

```java
@WebMvcTest(LoginController.class)
class LoginControllerErrorHandlingTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticateUserUseCase useCase;

    @Test
    void login_shouldReturn401WithStandardErrorFormat_whenCredentialsInvalid() throws Exception {
        // Arrange
        when(useCase.execute(any(), any()))
            .thenThrow(new InvalidCredentialsException("test@test.com"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {"email": "test@test.com", "password": "wrong"}
                    """))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Usuário ou senha inválidos"))
            .andExpect(jsonPath("$.timestamp").exists())
            .andExpect(jsonPath("$.details.code").value("INVALID_CREDENTIALS"));
    }
}
```

**Checklist de Conclusão Sprint 1**:
- [ ] 3 exceções criadas
- [ ] GlobalExceptionHandler implementado
- [ ] LoginController refatorado (sem try-catch)
- [ ] 3+ testes de contract validando error responses
- [ ] CI passa (build + tests)

---

### Sprint 2-4: [Similar para documentação, code quality, value objects]

*[Estrutura similar para próximos sprints]*

---

## Template de Mensagem de Erro JSON

```json
{
  "message": "Descrição legível do erro para o usuário",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "ERROR_CODE_CONSTANT",
    "field": "campo específico (se validação)",
    "rejectedValue": "valor inválido (se aplicável)",
    "traceId": "correlation-id-for-logging"
  }
}
```

### Exemplos por Tipo de Erro

**401 Unauthorized**:
```json
{
  "message": "Usuário ou senha inválidos",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "INVALID_CREDENTIALS"
  }
}
```

**404 Not Found**:
```json
{
  "message": "Recurso não encontrado",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "RESOURCE_NOT_FOUND",
    "resource": "User",
    "identifier": "user-123"
  }
}
```

**400 Bad Request (Validation)**:
```json
{
  "message": "Dados inválidos na requisição",
  "timestamp": "2025-12-03T10:15:30.123Z",
  "details": {
    "code": "VALIDATION_ERROR",
    "errors": [
      {"field": "email", "message": "Email inválido"},
      {"field": "password", "message": "Senha muito curta (mín 8 caracteres)"}
    ]
  }
}
```

---

**Fim do Documento**

**Versão**: 1.0.0
**Gerado**: 2025-12-03
**Baseado em**: Análise de 66 arquivos Java, 23 classes de teste, pom.xml, estrutura de pacotes
