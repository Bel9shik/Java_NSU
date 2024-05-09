package client;

import client.event.Event;

public interface Observer {
    void update(Event event);
}
