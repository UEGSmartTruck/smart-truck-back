package com.smarttruck.infra.repository;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;

import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ SpringDataTicketRepository.class })
@SuppressWarnings("resource")
public class SpringDataTicketRepositoryIntegrationTest {

    private static PostgreSQLContainer<?> postgres;

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        if (postgres != null) {
            r.add("spring.datasource.url", postgres::getJdbcUrl);
            r.add("spring.datasource.username", postgres::getUsername);
            r.add("spring.datasource.password", postgres::getPassword);
        }
    }

    @BeforeAll
    static void beforeAll() {
        // Skip integration if Docker is not available in the environment
        Assumptions.assumeTrue(DockerClientFactory.instance().isDockerAvailable(),
                "Docker not available, skipping integration test");

        postgres = new PostgreSQLContainer<>("postgres:15.3")
                .withDatabaseName("smarttruck_test")
                .withUsername("test")
                .withPassword("test");
        postgres.start();

        Flyway.configure()
                .dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
                .locations("classpath:db/migration")
                .load()
                .migrate();
    }

    @AfterAll
    static void afterAll() {
        if (postgres != null) {
            postgres.close();
        }
    }

    @Autowired
    private SpringDataTicketRepository repo;

    @Test
    public void save_should_persist_and_map_back_in_postgres() {
        Ticket t = new Ticket("c-pg", "descr-pg");
        t.setStatus(TicketStatus.ESCALATED);

        Ticket saved = repo.save(t);

        assertNotNull(saved.getId());
        assertEquals("c-pg", saved.getCustomerId());
        assertEquals(TicketStatus.ESCALATED, saved.getStatus());
    }
}
