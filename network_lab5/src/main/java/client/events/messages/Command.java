package client.events.messages;

import client.events.Event;

import java.io.Serializable;

public final class Command extends Message implements Serializable, Event {
    private String command;
    public Command(String command) {
        this.command = command;
    }
    public String getCommand() {
        return command;
    }
}
