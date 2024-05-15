package server;

import java.util.LinkedList;

public class Story {
    private final LinkedList<String> storyList;

    public Story() {
        storyList = new LinkedList<>();
    }

    public synchronized void addStory(String string) {
        if (storyList.size() >= 10) {
            storyList.removeFirst();
        }
        storyList.add(string);
    }

    public LinkedList<String> getStory() {
        if (!storyList.isEmpty()) {
            return storyList;
        } else return null;
    }
}
