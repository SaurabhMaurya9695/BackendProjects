package com.backend.design.pattern.behavioural.command.Command.ConcreateCommands;

import com.backend.design.pattern.behavioural.command.Command.ICommand;
import com.backend.design.pattern.behavioural.command.Receiver.Light;

public class LightCommand implements ICommand {

    private final Light _light;

    public LightCommand(Light light) {
        _light = light;
    }

    @Override
    public void executeCommand() {
        _light.On();
    }

    @Override
    public void undoCommand() {
        _light.Off();
    }
}
