package com.backend.design.pattern.behavioural.command;

import com.backend.design.pattern.behavioural.command.Command.ICommand;

public class RemoteController {

    private static final int NUM_BUTTONS = 4;

    private ICommand[] buttons = new ICommand[NUM_BUTTONS];
    private boolean[] isButtonPressed = new boolean[NUM_BUTTONS];

    public RemoteController() {
        for (int i = 0; i < NUM_BUTTONS; i++) { // FIX: use < instead of <=
            buttons[i] = null;
            isButtonPressed[i] = false; // false means OFF & true means ON
        }
    }

    public void setCommand(int idx, ICommand command) {
        if (idx >= 0 && idx < NUM_BUTTONS) {
            buttons[idx] = command;
            isButtonPressed[idx] = false;
        }
    }

    public void pressButton(int idx) {
        if (idx >= 0 && idx < NUM_BUTTONS && buttons[idx] != null) {
            if (!isButtonPressed[idx]) {
                buttons[idx].executeCommand();
            } else {
                buttons[idx].undoCommand();
            }
            isButtonPressed[idx] = !isButtonPressed[idx];
        } else {
            System.out.println("No Command Assign at this Button");
        }
    }

    public void releaseButton(int idx) {
        if (idx >= 0 && idx < NUM_BUTTONS && buttons[idx] != null && isButtonPressed[idx]) {
            buttons[idx].undoCommand();
            isButtonPressed[idx] = false;
        }
    }
}
