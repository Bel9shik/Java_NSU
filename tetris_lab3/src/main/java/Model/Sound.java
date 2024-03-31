package Model;

import javax.sound.sampled.*;
import java.lang.reflect.Type;
import java.net.URL;

public class Sound {

    Clip musicClip;
    URL url[] = new URL[5];

    public Sound() {
        url[0] = getClass().getResource("src/main/java/RESOURCES/SOUNDS/tetris_main_music.wav");
        url[1] = getClass().getResource("/deleteLine.wav");
        url[2] = getClass().getResource("/GameOver.wav");
        url[3] = getClass().getResource("/rotate.wav");
        url[4] = getClass().getResource("/touchFloor.wav");
    }

    public void play (int i, boolean music) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(url[i]);
            Clip clip = AudioSystem.getClip();

            if (music) {
                musicClip = clip;
            }

            clip.open(ais);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                }
            });

            ais.close();
            clip.start();
        }catch (Exception e) {

        }
    }

    public void loop() {
        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop () {
        musicClip.stop();
        musicClip.close();
    }
}
