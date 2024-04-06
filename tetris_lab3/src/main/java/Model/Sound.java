package Model;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

public class Sound {

    Clip musicClip;
    URL url[] = new URL[5];

    public Sound() {
        url[0] = getClass().getResource("/sounds/tetris_main_music.wav");
        url[1] = getClass().getResource("/sounds/deleteLine.wav");
        url[2] = getClass().getResource("/sounds/GameOver.wav");
        url[3] = getClass().getResource("/sounds/rotate.wav");
        url[4] = getClass().getResource("/sounds/touchFloor.wav");
        System.out.println(Arrays.toString(url));
    }

    public void startPlay(int i, boolean music) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResourceAsStream("/sounds/tetrisMainMusic.mp3")));
            Clip clip = AudioSystem.getClip();

            clip.open(ais);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

            if (music) {
                System.out.println("Playing music");
                musicClip = clip;
            }

            System.out.println(musicClip);

            ais.close();
            clip.start();

        } catch (Exception ignored) {

        }

    }

    public void loop() {
        System.out.println(musicClip.toString());
        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        musicClip.stop();
        musicClip.close();
    }
}
