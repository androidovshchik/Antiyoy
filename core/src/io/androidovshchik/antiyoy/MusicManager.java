package io.androidovshchik.antiyoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class MusicManager {
    private static MusicManager instance = null;
    public Music music;

    public static void initialize() {
        instance = null;
    }

    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    public void onMusicStatusChanged() {
        if (Settings.musicEnabled && Settings.soundEnabled) {
            if (!this.music.isPlaying()) {
                play();
            }
        } else if (this.music.isPlaying()) {
            stop();
        }
    }

    public void play() {
        if (this.music != null) {
            this.music.play();
            this.music.setLooping(true);
        }
    }

    public void stop() {
        if (this.music != null) {
            this.music.stop();
        }
    }

    public void load() {
        this.music = Gdx.audio.newMusic(Gdx.files.internal("sound/music.ogg"));
    }
}
