package com.shangame.fiction.ui.listen;

import com.shangame.fiction.ui.listen.palyer.Song;
import com.shangame.fiction.ui.listen.timing.TimingBean;

public class PlayerSong {
    private static PlayerSong instance = new PlayerSong();
    private Song playerSong;
    private TimingBean bean;
    private boolean isRunning;
    private boolean isPlay;

    private PlayerSong() {
    }

    public static PlayerSong getInstance() {
        return instance;
    }

    public Song getPlayerSong() {
        return playerSong;
    }

    public void setPlayerSong(Song playerSong) {
        this.playerSong = playerSong;
    }

    public TimingBean getTimingBean() {
        return bean;
    }

    public void setTimingBean(TimingBean bean) {
        this.bean = bean;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }
}
