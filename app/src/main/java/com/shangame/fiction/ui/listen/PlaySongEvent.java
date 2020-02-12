package com.shangame.fiction.ui.listen;

import com.shangame.fiction.ui.listen.palyer.Song;

public class PlaySongEvent {

    public Song song;

    public PlaySongEvent(Song song) {
        this.song = song;
    }
}
