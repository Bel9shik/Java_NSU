package server;

import java.io.PrintWriter;
import java.util.LinkedList;

public class Story {
    private LinkedList<String> storyList;

    public Story() {
        storyList = new LinkedList<>();
    }

    public synchronized void addStory(String story) {
        if (storyList.size() >= 10) {
            storyList.removeFirst();
        }
        storyList.add(story);
    }

    public synchronized void showStory(PrintWriter out) {
        if (!storyList.isEmpty()) {
            for (String story : storyList) {
                out.println(story);
            }
        }
    }
}
