package client;


import client.events.Event;

public interface Observer {
    void update(Event event);
}
