package com.backend.helpdesk.service;

import com.backend.helpdesk.exception.TicketNotFoundException;
import com.backend.helpdesk.models.Ticket;
import com.backend.helpdesk.enums.Status;
import com.backend.helpdesk.enums.Priority;

public interface TicketService {

    Ticket createTicket(Ticket ticket);

    Ticket getTicketByUsername(String username) throws TicketNotFoundException;

    Ticket getTicketById(String ticketId) throws TicketNotFoundException;

    Ticket updateTicket(String ticketId, String summary, Priority priority, Status status)
            throws TicketNotFoundException;

    boolean deleteTicketById(String ticketId) throws TicketNotFoundException;

    Status getTicketStatus(String ticketId) throws TicketNotFoundException;
}
