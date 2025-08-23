package com.backend.design.pattern.behavioural.command.Command;

// This is the class which is controlled by remote
public interface ICommand {

    void executeCommand();

    void undoCommand();
}
