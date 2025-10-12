package com.smarttruck.infra.repository;

import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Import({ SpringDataTicketRepository.class })
public class SpringDataTicketRepositoryTest {

    @Autowired
    private SpringDataTicketRepository repo;

    @Test
    public void save_should_persist_and_map_back() {
        Ticket t = new Ticket("c-pg", "descr-pg");
        t.setStatus(TicketStatus.ESCALATED);

        Ticket saved = repo.save(t);

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("c-pg", saved.getCustomerId());
        Assertions.assertEquals(TicketStatus.ESCALATED, saved.getStatus());
        Assertions.assertNotNull(saved.getCreatedAt());
    }
}
