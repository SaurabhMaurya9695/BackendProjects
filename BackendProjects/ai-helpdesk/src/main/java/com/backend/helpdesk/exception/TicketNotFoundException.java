package com.backend.helpdesk.exception;

public class TicketNotFoundException extends Exception {

    public TicketNotFoundException(String msg) {
        super(msg);
    }
}