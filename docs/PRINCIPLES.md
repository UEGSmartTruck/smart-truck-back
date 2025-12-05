# SmartTruck Backend - Princípios de Desenvolvimento

## Resumo Executivo

O SmartTruck é um sistema backend Java 21 construído com **Clean Architecture** e **Spring Boot 3.5**, focado em **qualidade de código**, **testabilidade**, e **segurança**. Este documento define **10 princípios não-negociáveis** que guiam todas as decisões técnicas, desde a estrutura de pacotes até a entrega em produção.

**Por que estes princípios importam:**
- Reduzem débito técnico através de regras claras e automáveis
- Facilitam onboarding de desenvolvedores juniores com padrões consistentes
- Permitem evolução do sistema sem quebrar contratos de API
- Garantem segurança e conformidade através de gates automatizados

**Status do Projeto:**
- ✅ **23+ classes de teste** cobrindo todas as camadas (controller → repository)
- ✅ **Clean Architecture pura**: domínio sem dependências externas
- ✅ **JWT com blacklist**: logout seguro implementado
- ⚠️ **Gaps identificados**: exceções customizadas, value objects, @ControllerAdvice

---

## Top 10 Princípios Essenciais

### 1️⃣ Clean Architecture Compliance (NON-NEGOTIABLE)

**O QUE**: Separação rígida de camadas — Domain, Application, Infrastructure, Presentation.

**POR QUE**: Evita acoplamento entre regras de negócio e frameworks. Facilita testes sem Spring Context.

**COMO VERIFICAR**:
```bash
# Domain models não devem ter anotações JPA/Spring
grep -r "@Entity\|@Component" src/main/java/com/smarttruck/domain/model/
# Deve retornar zero resultados
```

**EXEMPLO DO CÓDIGO**:
- ✅ `domain/model/User.java`: POJO puro, apenas Java core
- ✅ `infrastructure/persistence/JpaUser.java`: Entidade JPA separada
- ✅ `application/service/CreateTicketService.java`: Orquestra domínio sem lógica de negócio

---

### 2️⃣ Immutability and Domain Purity

**O QUE**: Campos `final` no domínio; mutações via métodos com lógica de negócio.

**POR QUE**: Imutabilidade previne bugs de concorrência e torna estado explícito.

**DO** ✅:
```java
public class User {
    private final String id;
    private final String email; // final = não pode mudar após construção

    public User(String email) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
    }
}
```

**DON'T** ❌:
```java
public class User {
    private String id; // mutable sem motivo

    public void setId(String id) { // setter sem validação
        this.id = id;
    }
}
```

**REGRA AUTOMATIZÁVEL** (PMD):
```xml
<rule ref="category/java/design.xml/ImmutableField"/>
```

---

### 3️⃣ Test Coverage and TDD Discipline (NON-NEGOTIABLE)

**O QUE**: Mínimo **75% line coverage**, **60% branch coverage**. TDD: Red → Green → Refactor.

**POR QUE**: Testes documentam intenção, previnem regressões, e permitem refatoração segura.

**TIPOS DE TESTE REQUERIDOS**:
| Tipo | Quando Usar | Exemplo no Código |
|------|-------------|-------------------|
| **Unit Test** | Serviços, mappers, lógica | `LoginControllerTest.java` (mocks) |
| **@DataJpaTest** | Repositories JPA | `JpaUserRepositoryTest.java` |
| **@SpringBootTest** | Fluxos críticos end-to-end | `LoginControllerIntegrationTest.java` |
| **Contract Test** | Contratos de API REST | Validação de DTOs com MockMvc |

**NOMENCLATURA**:
```java
@Test
void shouldReturnToken_whenCredentialsAreValid() {
    // Arrange
    LoginRequest request = new LoginRequest("user@test.com", "pass");
    when(useCase.execute(email, password)).thenReturn("token");

    // Act
    ResponseEntity<?> response = controller.login(request);

    // Assert
    assertEquals(200, response.getStatusCode().value());
    assertTrue(response.getBody() instanceof TokenResponse);
}
```

**CI GATE**:
```bash
mvn clean verify
# Falha se coverage < 75% (configurar JaCoCo plugin)
```

---

### 4️⃣ DTO-Based API Boundaries

**O QUE**: Controllers só aceitam/retornam DTOs. Domain models nunca vazam para API.

**POR QUE**: Desacopla evolução do domínio de contratos de API. Previne Jackson de expor campos internos.

**DO** ✅:
```java
@PostMapping("/auth/login")
public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
    String token = authenticateUserUseCase.execute(request.email(), request.password());
    return ResponseEntity.ok(new TokenResponse(token, 3600));
}

// DTOs como records (Java 21)
public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
public record TokenResponse(String accessToken, long expiresIn) {}
```

**DON'T** ❌:
```java
@PostMapping("/users")
public ResponseEntity<User> createUser(@RequestBody User user) { // ❌ Domain no controller
    return ResponseEntity.ok(userRepository.save(user));
}
```

**TESTE AUTOMATIZÁVEL** (ArchUnit):
```java
@Test
void controllers_shouldOnlyReturnDTOs() {
    classes().that().resideInPackage("..presentation.controller..")
        .should().onlyAccessClassesThat().resideInPackage("..presentation.dto..")
        .check(importedClasses);
}
```

---

### 5️⃣ JWT Security Standards

**O QUE**: Token com expiry 1h, blacklist no logout, secret via variável de ambiente.

**POR QUE**: Previne replay attacks e vazamento de credenciais.

**CHECKLIST DE SEGURANÇA**:
- ✅ Token contém claims mínimos (userId, email, name)
- ✅ Validação verifica assinatura + expiry + blacklist
- ✅ Logout adiciona token à blacklist
- ✅ Secret NUNCA commitado (`.env` ignorado no Git)
- ⚠️ HS256 em MVP (migrar para RS256 em multi-service)

**TESTE DE SEGURANÇA**:
```java
@Test
void shouldRejectBlacklistedToken() {
    String token = jwtProvider.generateToken("user123", "user@test.com", "User");
    jwtProvider.invalidateToken(token);

    assertFalse(jwtProvider.validateToken(token)); // ✅ Token rejeitado
}
```

**CI CHECK**:
```bash
# Falha se secret commitado
git grep "jwt.secret.*=" src/main/resources/*.yml && exit 1 || echo "OK"
```

---

### 6️⃣ Exception Handling and Error Contracts

**O QUE**: Exceções customizadas no domínio; @ControllerAdvice traduz para HTTP status + ErrorResponse.

**POR QUE**: Erros consistentes melhoram DX (developer experience) e debugabilidade.

**ESTRUTURA PADRONIZADA**:
```java
// Exceção customizada
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String email) {
        super("Invalid credentials for: " + email);
    }
}

// DTO de erro
public record ErrorResponse(String message, String timestamp, Map<String, String> details) {
    public ErrorResponse(String message) {
        this(message, Instant.now().toString(), Map.of());
    }
}

// Global handler
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(401)
            .body(new ErrorResponse(ex.getMessage()));
    }
}
```

**CURRENT GAP**: LoginController usa `RuntimeException` genérico → substituir por exceções específicas.

---

### 7️⃣ Code Simplicity (Object Calisthenics)

**O QUE**: Limites mensuráveis de complexidade.

**REGRAS**:
- Max 2 níveis de indentação por método
- Max 20 linhas por método (exceto getters)
- Max 3 parâmetros (usar parameter object se mais)
- Sem `else` (preferir early return)
- Sem primitive obsession (email → `Email` value object)

**DO** ✅:
```java
public String authenticate(String email, String password) {
    User user = findUserByEmail(email); // early return implícito
    if (user == null) return null;

    if (!passwordMatches(user, password)) return null;

    return generateToken(user);
}
```

**DON'T** ❌:
```java
public String authenticate(String email, String password) {
    User user = findUserByEmail(email);
    if (user != null) {
        if (passwordMatches(user, password)) { // nested ifs
            return generateToken(user);
        } else { // else desnecessário
            return null;
        }
    }
    return null;
}
```

**CHECKSTYLE SNIPPET**:
```xml
<module name="MethodLength">
    <property name="max" value="20"/>
</module>
<module name="ParameterNumber">
    <property name="max" value="3"/>
</module>
<module name="CyclomaticComplexity">
    <property name="max" value="10"/>
</module>
```

---

### 8️⃣ Documentation and Javadoc

**O QUE**: Javadoc obrigatório em classes públicas, métodos, e domain models.

**POR QUE**: Reduz onboarding time e clarifica intenção sem ler implementação.

**ESTRUTURA REQUERIDA**:
```java
/**
 * Serviço responsável pela autenticação de usuários via credenciais.
 * <p>
 * Este serviço valida email/senha, gera token JWT, e atualiza timestamp de login.
 * </p>
 *
 * @see JwtTokenProvider para geração de token
 * @see UserRepository para persistência
 */
@Service
public class AuthenticateUserService implements AuthenticateUserUseCase {

    /**
     * Autentica usuário e retorna token JWT.
     *
     * @param email email do usuário (deve existir no banco)
     * @param password senha em texto plano (será comparada com hash)
     * @return token JWT válido por 1 hora
     * @throws InvalidCredentialsException se credenciais inválidas
     */
    @Override
    public String execute(String email, String password) {
        // implementação
    }
}
```

**CHECKSTYLE ENFORCEMENT**:
```xml
<module name="MissingJavadocMethod">
    <property name="scope" value="public"/>
</module>
```

---

### 9️⃣ Environment-Based Configuration

**O QUE**: Perfis Spring (dev/prod), config via env vars, Flyway em prod.

**POR QUE**: 12-factor app compliance, deploy em múltiplos ambientes sem rebuild.

**PROFILES**:
| Perfil | Database | Migrations | Secrets |
|--------|----------|------------|---------|
| **dev** | H2 in-memory | Hibernate auto-ddl | `.env` local |
| **prod** | PostgreSQL | Flyway | Azure Key Vault |

**ENV VARS REQUERIDAS**:
```bash
# Dev (.env file)
JWT_SECRET=dev_secret_change_me
SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb

# Prod (secrets manager)
JWT_SECRET=<secret-from-key-vault>
SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/smarttruck
SPRING_DATASOURCE_USERNAME=<from-vault>
SPRING_DATASOURCE_PASSWORD=<from-vault>
```

**STARTUP VALIDATION**:
```java
@Component
public class ConfigValidator implements ApplicationListener<ApplicationReadyEvent> {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if ("DEV_EXAMPLE_JWT_SECRET_CHANGE_ME".equals(jwtSecret)) {
            throw new IllegalStateException("JWT_SECRET not configured!");
        }
    }
}
```

---

### 🔟 Repository Pattern and Persistence Isolation

**O QUE**: Domain repository = interface; JPA adapter = implementação em infra.

**POR QUE**: Permite testes com in-memory fake, troca de banco sem mudar domínio.

**ARQUITETURA**:
```
domain/repository/UserRepository.java (interface)
    ↑
    | usa
    |
application/service/CreateUserService.java
    ↓
    | implementado por
    |
infrastructure/persistence/JpaUserRepository.java (adapter)
    ↓
    | usa
    |
infrastructure/persistence/SpringDataJpaUserRepository.java (Spring Data)
```

**MAPPER PATTERN**:
```java
// Adapter traduz entre domain e JPA
@Repository
public class JpaUserRepository implements UserRepository {
    @Autowired
    private SpringDataJpaUserRepository springRepo;

    @Autowired
    private JpaUserMapper mapper;

    @Override
    public User save(User domainUser) {
        JpaUser jpaUser = mapper.toJpa(domainUser);
        JpaUser saved = springRepo.save(jpaUser);
        return mapper.toDomain(saved);
    }
}
```

**TESTE SEM DATABASE**:
```java
@Test
void shouldCreateUser() {
    UserRepository fakeRepo = new InMemoryUserRepository(); // sem JPA
    CreateUserService service = new CreateUserService(fakeRepo);

    User user = service.execute("Test", "test@example.com", "pass");

    assertNotNull(user.getId());
}
```

---

## Recomendações Específicas e Acionáveis

### 🔴 Prioridade ALTA (Fix imediato)

1. **Criar exceções customizadas** (`shared/exceptions/`)
   ```java
   InvalidCredentialsException.java
   UserNotFoundException.java
   TicketNotFoundException.java
   ```
   **Onde usar**: `AuthenticateUserService.execute()`, `LoginController`

2. **Implementar @ControllerAdvice**
   ```java
   @ControllerAdvice
   public class GlobalExceptionHandler {
       @ExceptionHandler(InvalidCredentialsException.class)
       public ResponseEntity<ErrorResponse> handleInvalidCredentials(ex) { ... }
   }
   ```
   **Impacto**: Remove try-catch repetitivo em controllers

3. **Adicionar Javadoc em User.java**
   ```java
   /**
    * Representa um usuário do sistema SmartTruck.
    * ...
    */
   public class User { ... }
   ```

### 🟡 Prioridade MÉDIA (Próximo sprint)

4. **Criar value objects para email/phone**
   ```java
   public record Email(String value) {
       public Email {
           if (!value.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
               throw new IllegalArgumentException("Invalid email");
           }
       }
   }
   ```
   **Refactor**: `User` usa `Email email` em vez de `String email`

5. **Configurar JaCoCo coverage report**
   ```xml
   <plugin>
       <groupId>org.jacoco</groupId>
       <artifactId>jacoco-maven-plugin</artifactId>
       <executions>
           <execution><goals><goal>check</goal></goals></execution>
       </executions>
       <configuration>
           <rules>
               <rule><limits>
                   <limit><minimum>0.75</minimum></limit>
               </limits></rule>
           </rules>
       </configuration>
   </plugin>
   ```

6. **Adicionar ArchUnit tests**
   ```java
   @Test
   void domainLayer_shouldNotDependOnInfrastructure() {
       noClasses().that().resideInPackage("..domain..")
           .should().dependOnClassesThat().resideInPackage("..infrastructure..")
           .check(importedClasses);
   }
   ```

### 🟢 Prioridade BAIXA (Backlog)

7. **Migrar JWT de HS256 para RS256** (quando multi-service)
8. **Adicionar mutation testing** (PIT)
9. **Implementar API versioning** (`/api/v1/...`)
10. **Adicionar distributed tracing** (Micrometer + Zipkin)

---

## Plano de Migração Incremental

### Sprint 1: Exceções e Erros
- [ ] Criar 3 exceções customizadas
- [ ] Implementar GlobalExceptionHandler
- [ ] Refatorar LoginController para usar exceções
- [ ] Adicionar testes de contract (MockMvc validando ErrorResponse)

### Sprint 2: Documentação
- [ ] Adicionar Javadoc em todos os domain models
- [ ] Configurar Checkstyle com JavadocMethod rule
- [ ] Gerar e publicar Javadoc no GitHub Pages

### Sprint 3: Code Quality Automation
- [ ] Configurar JaCoCo coverage gate (75%)
- [ ] Adicionar PMD e SpotBugs ao build
- [ ] Integrar SonarQube no CI

### Sprint 4: Value Objects
- [ ] Criar Email, PhoneNumber value objects
- [ ] Refatorar User para usar value objects
- [ ] Atualizar testes e mappers

---

## Pontos de Atenção Encontrados no Código

| Severidade | Localização | Problema | Sugestão |
|------------|-------------|----------|----------|
| 🔴 **ALTA** | `LoginController.java:36` | Catch genérico `RuntimeException` | Criar `InvalidCredentialsException` |
| 🔴 **ALTA** | Pacote `shared/exceptions/` | Não existe | Criar com 3 exceções básicas |
| 🟡 **MÉDIA** | `User.java` | Sem Javadoc | Adicionar doc de classe |
| 🟡 **MÉDIA** | `User.java:9-12` | Email/phone como String | Criar value objects |
| 🟡 **MÉDIA** | `pom.xml` | Sem JaCoCo configurado | Adicionar plugin com gate 75% |
| 🟢 **BAIXA** | `Ticket.java:89` | Setter público `setUpdatedAt` | Considerar remover (apenas para testes?) |
| 🟢 **BAIXA** | Controllers | Try-catch repetitivo | Usar @ControllerAdvice |

---

## Métricas e Thresholds

### Coverage (JaCoCo)
- **Line Coverage**: ≥75% (current: ~80% estimated)
- **Branch Coverage**: ≥60%
- **Method Coverage**: ≥70%

### Complexity (SonarQube/PMD)
- **Cyclomatic Complexity**: ≤10 por método
- **Cognitive Complexity**: ≤15 por método
- **Lines per Method**: ≤20 (exceto getters)
- **Parameters per Method**: ≤3

### Duplication
- **Duplicated Lines**: <3%
- **Duplicated Blocks**: <5

### Security (Snyk/Dependabot)
- **HIGH/CRITICAL CVEs**: 0
- **MEDIUM CVEs**: ≤3 (com plano de correção)
- **Dependency Age**: ≤6 meses (exceto LTS)

### Performance
- **API Response Time p95**: <200ms (read), <500ms (write)
- **Database Queries per Request**: ≤3 (detectar N+1)
- **Heap Memory**: <512MB (10 usuários concorrentes)
- **Startup Time**: <30 segundos

---

## Como Usar Este Documento

### Para Desenvolvedores
1. Leia os **Top 10 Princípios** antes de qualquer PR
2. Use **Recomendações Acionáveis** como checklist
3. Consulte **Plano de Migração** para priorização
4. Revise **Pontos de Atenção** para áreas de melhoria

### Para Revisores de PR
1. Valide conformidade com **10 princípios**
2. Verifique se testes cobrem novos códigos (75%+)
3. Confirme que DTOs são usados em controllers
4. Valide Clean Architecture (domain sem annotations)

### Para Tech Leads
1. Use **Métricas e Thresholds** para definir CI gates
2. Priorize itens do **Plano de Migração**
3. Revise **Pontos de Atenção** em planning trimestral
4. Atualize Constitution quando houver mudanças arquiteturais

---

## Referências

- **Constitution Completa**: `.specify/memory/constitution.md`
- **Templates de Spec/Tasks**: `.specify/templates/`
- **Guia de Setup**: `README.md`
- **Commit Guidelines**: `.github/commit-guidelines-agents.md`
- **CI/CD Configuration**: `.github/workflows/` (TODO: criar)

---

**Versão**: 1.0.0 | **Data**: 2025-12-03 | **Baseado em**: Análise do código existente (23 classes de teste, Clean Architecture implementada)
