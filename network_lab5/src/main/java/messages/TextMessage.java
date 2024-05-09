package messages;

import java.io.Serializable;
import java.util.List;

public class TextMessage extends Message implements Serializable {
    private List<String> text;
    private String singleMessage;
    public TextMessage(List<String> text){
        this.text = text;
    }

    public TextMessage(String text){
        singleMessage = text;
    }

    public List<String> getText() {
        return text;
    }

    public String getSingleMessage() {
        return singleMessage;
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
