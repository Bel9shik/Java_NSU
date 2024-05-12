package xml.server;

import org.w3c.dom.Document;

import java.util.LinkedList;

public class Story {
    private final LinkedList<Document> storyList;

    public Story() {
        storyList = new LinkedList<>();
    }

    public synchronized void addStory(Document document) {
        if (storyList.size() >= 10) {
            storyList.removeFirst();
        }
        storyList.add(document);
    }

    public LinkedList<Document> getStory() {
        if (!storyList.isEmpty()) {
            return storyList;
        } else return null;
    }
}
