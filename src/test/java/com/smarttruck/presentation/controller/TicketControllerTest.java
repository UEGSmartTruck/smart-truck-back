package com.smarttruck.presentation.controller;

import com.smarttruck.application.service.CreateTicketUseCase;
import com.smarttruck.domain.model.Ticket;
import com.smarttruck.domain.model.TicketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TicketController.class)
public class TicketControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreateTicketUseCase createTicketUseCase;

    private Ticket sample;

    @BeforeEach
    void setup() {
        sample = new Ticket("c-1", "descr");
        sample.setStatus(TicketStatus.IN_PROGRESS);
    }

    @Test
    public void create_should_return_200_and_body_when_valid() throws Exception {
        Mockito.when(createTicketUseCase.create(Mockito.eq("c-1"), Mockito.eq("descr"), Mockito.eq(true)))
                .thenReturn(sample);

        String body = "{\"customerId\":\"c-1\",\"description\":\"descr\",\"aiSolved\":true}";

        mvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("c-1"))
                .andExpect(jsonPath("$.description").value("descr"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    public void create_should_treat_missing_aiSolved_as_false() throws Exception {
        Mockito.when(createTicketUseCase.create(Mockito.eq("c-2"), Mockito.eq("descr2"), Mockito.eq(false)))
                .thenReturn(new Ticket("c-2", "descr2"));

        String body = "{\"customerId\":\"c-2\",\"description\":\"descr2\"}";

        mvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("c-2"));
    }
}
