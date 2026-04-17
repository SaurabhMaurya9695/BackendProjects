package com.backend.helpdesk.service.impl;

import com.backend.helpdesk.exception.TicketNotFoundException;
import com.backend.helpdesk.models.Ticket;
import com.backend.helpdesk.repository.TicketRepository;
import com.backend.helpdesk.service.TicketService;
import com.backend.helpdesk.enums.Status;
import com.backend.helpdesk.enums.Priority;

import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger LOG = Logger.getLogger(TicketServiceImpl.class.getName());

    private final TicketRepository _ticketRepository;

    public TicketServiceImpl(TicketRepository _ticketRepository) {
        this._ticketRepository = _ticketRepository;
    }

    @Override
    public Ticket createTicket(Ticket ticket) {
        Ticket saved = this._ticketRepository.save(ticket);
        LOG.info("TICKET SAVED SUCCESSFULLY");
        return saved;
    }

    @Override
    public Ticket getTicketByUsername(String username) throws TicketNotFoundException {
        return this._ticketRepository.findTicketByUsername(username).orElseThrow(
                () -> new TicketNotFoundException("Ticket Not Found For This User"));
    }

    @Override
    public Ticket getTicketById(String ticketId) throws TicketNotFoundException {
        return this._ticketRepository.findTicketByTicketId(ticketId).orElseThrow(
                () -> new TicketNotFoundException("Ticket Id Not found"));
    }

    @Override
    public Ticket updateTicket(String ticketId, String summary, Priority priority, Status status)
            throws TicketNotFoundException {
        Ticket ticket = getTicketById(ticketId);
        if (summary != null && !summary.isEmpty()) {
            ticket.setSummary(summary);
        }
        if (priority != null) {
            ticket.setPriority(priority);
        }
        if (status != null) {
            ticket.setStatus(status);
        }
        Ticket updated = this._ticketRepository.save(ticket);
        LOG.info("TICKET UPDATED SUCCESSFULLY: " + ticketId);
        return updated;
    }

    @Override
    public boolean deleteTicketById(String ticketId) throws TicketNotFoundException {
        Ticket ticket = getTicketById(ticketId);
        this._ticketRepository.delete(ticket);
        LOG.info("TICKET DELETED SUCCESSFULLY: " + ticketId);
        return true;
    }

    @Override
    public Status getTicketStatus(String ticketId) throws TicketNotFoundException {
        Ticket ticket = getTicketById(ticketId);
        LOG.info("TICKET STATUS RETRIEVED: " + ticketId + " - " + ticket.getStatus());
        return ticket.getStatus();
    }
}
