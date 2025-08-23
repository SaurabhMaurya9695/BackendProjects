package com.backend.design.pattern.behavioural.command.Command.ConcreateCommands;

import com.backend.design.pattern.behavioural.command.Command.ICommand;;
import com.backend.design.pattern.behavioural.command.Receiver.Fan;

public class FanCommand implements ICommand {

    private final Fan _fan;

    public FanCommand(Fan fan) {
        _fan = fan;
    }

    @Override
    public void executeCommand() {
        _fan.On();
    }

    @Override
    public void undoCommand() {
        _fan.Off();
    }
}
