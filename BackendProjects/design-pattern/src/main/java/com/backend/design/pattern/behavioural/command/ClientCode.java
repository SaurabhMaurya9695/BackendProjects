package com.backend.design.pattern.behavioural.command;

import com.backend.design.pattern.behavioural.command.Command.ConcreateCommands.FanCommand;
import com.backend.design.pattern.behavioural.command.Command.ConcreateCommands.LightCommand;
import com.backend.design.pattern.behavioural.command.Receiver.Fan;
import com.backend.design.pattern.behavioural.command.Receiver.Light;

public class ClientCode {

    public static void main(String[] args) {
        Light light = new Light();
        Fan fan = new Fan();

        RemoteController remoteController =
                new RemoteController(); // This will assign every button to null and starting button is not pressed

        remoteController.setCommand(0, new LightCommand(light));
        remoteController.setCommand(1, new FanCommand(fan));

        // Simulate button behaviour
        System.out.println("----- Toggling light button with 0 -----");
        remoteController.pressButton(0);
        remoteController.pressButton(0);

        System.out.println("----- Toggling Fan button with 0 -----");
        remoteController.pressButton(1);
        remoteController.pressButton(1);

        System.out.println("----- Pressing Unsigned Button  -----");
        remoteController.pressButton(2);
    }
}
