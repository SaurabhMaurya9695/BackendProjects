package com.backend.helpdesk.repository;

import com.backend.helpdesk.models.Ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {

    Optional<Ticket> findTicketByTicketId(String ticketId);

    Optional<Ticket> findTicketByUsername(String username);
}
