package client.events.clientEvents;

import client.events.Event;

public class UpdateChat implements Event {
    private String message;

    public UpdateChat(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
