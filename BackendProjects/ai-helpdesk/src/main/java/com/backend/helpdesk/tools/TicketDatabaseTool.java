package com.backend.helpdesk.tools;

import com.backend.helpdesk.exception.TicketNotFoundException;
import com.backend.helpdesk.models.Ticket;
import com.backend.helpdesk.service.TicketService;
import com.backend.helpdesk.enums.Status;
import com.backend.helpdesk.enums.Priority;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.logging.Logger;

@Component
public class TicketDatabaseTool {

    private final Logger LOG = Logger.getLogger(getClass().getName());

    private final TicketService _ticketService;

    public TicketDatabaseTool(TicketService ticketService) {
        _ticketService = ticketService;
    }

    @Tool(description = "Create a new ticket in the database with summary, priority, and username")
    public String createTicketTool(@ToolParam(description = "Summary or description of the ticket") String summary,
            @ToolParam(description = "Priority level: HIGH, MEDIUM, or LOW") String priority,
            @ToolParam(description = "Username of the person creating the ticket") String username) {
        LOG.info("Create Ticket Tool has been called");
        try {
            Ticket ticket = new Ticket();
            ticket.setTicketId(UUID.randomUUID().toString());
            ticket.setSummary(summary);
            ticket.setPriority(Priority.valueOf(priority.toUpperCase()));
            ticket.setUsername(username);
            ticket.setStatus(Status.OPEN);

            Ticket saved = this._ticketService.createTicket(ticket);
            return "Ticket created successfully!\nTicket ID: " + saved.getTicketId() + "\nSummary: "
                    + saved.getSummary() + "\nPriority: " + saved.getPriority() + "\nStatus: " + saved.getStatus();
        } catch (IllegalArgumentException e) {
            return "Error: Invalid priority. Use HIGH, MEDIUM, or LOW";
        } catch (Exception e) {
            return "Error creating ticket: " + e.getMessage();
        }
    }

    @Tool(description = "Get ticket details by username")
    public String getTicketByUsernameTool(@ToolParam(description = "Username to retrieve ticket for") String username) {
        LOG.info("Get Ticket By Username Tool has been called");
        try {
            Ticket ticket = this._ticketService.getTicketByUsername(username);
            return "Ticket Details:\nTicket ID: " + ticket.getTicketId() + "\nSummary: " + ticket.getSummary()
                    + "\nPriority: " + ticket.getPriority() + "\nStatus: " + ticket.getStatus() + "\nCreated: "
                    + ticket.getCreatedOn();
        } catch (TicketNotFoundException e) {
            return e.getMessage();
        }
    }

    @Tool(description = "Update an existing ticket with new summary, priority, and/or status")
    public String updateTicketTool(@ToolParam(description = "Ticket ID to update") String ticketId,
            @ToolParam(description = "New summary (optional, leave empty to keep existing)") String summary,
            @ToolParam(description = "New priority: HIGH, MEDIUM, LOW (optional)") String priority,
            @ToolParam(description = "New status: OPEN, CLOSED, RESOLVED (optional)") String status) {
        LOG.info("Update Ticket Tool has been called for ticket: " + ticketId);
        try {
            Priority priorityEnum = (priority != null && !priority.isEmpty()) ? Priority.valueOf(priority.toUpperCase())
                    : null;
            Status statusEnum = (status != null && !status.isEmpty()) ? Status.valueOf(status.toUpperCase()) : null;

            Ticket updated = this._ticketService.updateTicket(ticketId,
                    (summary != null && !summary.isEmpty()) ? summary : null, priorityEnum, statusEnum);

            return "Ticket updated successfully!\nTicket ID: " + updated.getTicketId() + "\nSummary: "
                    + updated.getSummary() + "\nPriority: " + updated.getPriority() + "\nStatus: "
                    + updated.getStatus();
        } catch (IllegalArgumentException e) {
            return "Error: Invalid priority or status value";
        } catch (TicketNotFoundException e) {
            return e.getMessage();
        }
    }

    @Tool(description = "Delete a ticket by its ID")
    public String deleteTicketTool(@ToolParam(description = "Ticket ID to delete") String ticketId) {
        LOG.info("Delete Ticket Tool has been called for ticket: " + ticketId);
        try {
            boolean deleted = this._ticketService.deleteTicketById(ticketId);
            if (deleted) {
                return "Ticket deleted successfully! Ticket ID: " + ticketId;
            } else {
                return "Failed to delete ticket";
            }
        } catch (TicketNotFoundException e) {
            return e.getMessage();
        }
    }

    @Tool(description = "Check the current status of a ticket by its ID")
    public String checkTicketStatusTool(@ToolParam(description = "Ticket ID to check status for") String ticketId) {
        LOG.info("Check Ticket Status Tool has been called for ticket: " + ticketId);
        try {
            Status currentStatus = this._ticketService.getTicketStatus(ticketId);
            return "Current Status for Ticket " + ticketId + ": " + currentStatus;
        } catch (TicketNotFoundException e) {
            return e.getMessage();
        }
    }
}
