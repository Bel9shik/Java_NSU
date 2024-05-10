package serialization.client.event.clientEvents;

import serialization.client.event.Event;

public class UpdateChat implements Event {
    private String message;

    public UpdateChat(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
