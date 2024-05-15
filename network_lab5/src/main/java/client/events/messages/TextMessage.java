package client.events.messages;

import client.events.Event;

import java.io.Serializable;
import java.util.List;

public class TextMessage extends Message implements Serializable, Event {
    private List<String> text;
    private String singleMessage;
    public TextMessage(List<String> text){
        this.text = text;
    }

    public TextMessage(String text){
        singleMessage = text;
    }

    @Override
    public String toString() {
        if (singleMessage == null) {
            StringBuilder builder = new StringBuilder();
            for (String text : text) {
                builder.append(text);
            }
            return builder.toString();
        } else {
            return singleMessage;
        }
    }
}
