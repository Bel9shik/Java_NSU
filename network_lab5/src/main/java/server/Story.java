package server;

import messages.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;

public class Story {
    private final LinkedList<Message> storyList;

    public Story() {
        storyList = new LinkedList<>();
    }

    public synchronized void addStory(Message story) {
        if (storyList.size() >= 10) {
            storyList.removeFirst();
        }
        storyList.add(story);
    }

    public synchronized void showStory(ObjectOutputStream out) throws IOException {
        if (!storyList.isEmpty()) {
            for (Message story : storyList) {
                out.writeObject(story);
            }
        }
    }
}
