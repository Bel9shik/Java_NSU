package xml.client.event.messages;

import java.io.Serializable;

public class Error extends Message implements Serializable {
    private String error;
    public Error(String error) {
        this.error = error;
    }
    public String getError() {
        return error;
    }
}
