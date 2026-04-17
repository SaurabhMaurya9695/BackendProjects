package com.backend.helpdesk.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import com.backend.helpdesk.enums.Priority;
import com.backend.helpdesk.enums.Status;

import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
public class Ticket {

    @Id
    private String ticketId;

    @Lob
    private String summary;

    @Enumerated(EnumType.STRING) // in database, it stored as a String
    private Priority priority;

    @Enumerated(EnumType.STRING) // in database, it stored as a String
    private Status status;

    @Column(unique = true)
    private String username;

    private LocalDateTime createdOn;
    private LocalDateTime updateOn;

    @PrePersist
    void preSave() {
        if (this.createdOn == null) {
            this.createdOn = LocalDateTime.now();
        }
        this.updateOn = LocalDateTime.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updateOn = LocalDateTime.now();
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdateOn() {
        return updateOn;
    }

    public void setUpdateOn(LocalDateTime updateOn) {
        this.updateOn = updateOn;
    }
}
