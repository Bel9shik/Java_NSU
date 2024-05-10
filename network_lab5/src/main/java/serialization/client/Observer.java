package serialization.client;

import serialization.client.event.Event;

public interface Observer {
    void update(Event event);
}
