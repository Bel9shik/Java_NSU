package xml.client;

import xml.client.event.Event;

public interface Observer {
    void update(Event event);
}
