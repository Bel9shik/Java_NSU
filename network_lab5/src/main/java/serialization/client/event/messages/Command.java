package serialization.client.event.messages;

import java.io.Serializable;

public final class Command extends Message implements Serializable {
    private String command;
    public Command(String command) {
        this.command = command;
    }
    public String getCommand() {
        return command;
    }
}
