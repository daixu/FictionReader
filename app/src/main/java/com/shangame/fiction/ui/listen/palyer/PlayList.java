package com.shangame.fiction.ui.listen.palyer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlayList implements Parcelable {

    // Play List: Favorite
    public static final int NO_POSITION = -1;

    private int id;

    private String name;
    private int numOfSongs;

    private Date createdAt;

    private Date updatedAt;
    private List<Song> songs = new ArrayList<>();

    private int playingIndex = -1;

    public PlayList() {
        // EMPTY
    }

    public PlayList(Song song) {
        songs.add(song);
        numOfSongs = 1;
    }

    public void clear() {
        songs.clear();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public int getPlayingIndex() {
        return playingIndex;
    }

    public void setPlayingIndex(int playingIndex) {
        this.playingIndex = playingIndex;
    }

    /**
     * Prepare to play
     */
    public boolean prepare() {
        if (songs.isEmpty()) {
            return false;
        }
        if (playingIndex == NO_POSITION) {
            playingIndex = 0;
        }
        return true;
    }

    /**
     * The current song being played or is playing based on the {@link #playingIndex}
     */
    public Song getCurrentSong() {
        if (playingIndex != NO_POSITION && !songs.isEmpty()) {
            return songs.get(playingIndex);
        }
        return null;
    }

    public boolean hasLast() {
        return songs != null && songs.size() != 0;
    }

    public Song last() {
        if (songs.isEmpty()) {
            return null;
        }
        int newIndex = playingIndex - 1;
        if (newIndex < 0) {
            newIndex = songs.size() - 1;
        }
        playingIndex = newIndex;
        return songs.get(playingIndex);
    }

    public boolean hasNext(boolean fromComplete) {
        if (songs.isEmpty()) {
            return false;
        }
        if (fromComplete) {
            if (playingIndex + 1 >= songs.size()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Move the playingIndex forward depends on the play mode
     *
     * @return The next song to play
     */
    public Song next() {
        if (songs.isEmpty()) {
            return null;
        }
        int newIndex = playingIndex + 1;
        if (newIndex >= songs.size()) {
            newIndex = 0;
        }
        playingIndex = newIndex;
        return songs.get(playingIndex);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.numOfSongs);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
        dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
        dest.writeTypedList(this.songs);
        dest.writeInt(this.playingIndex);
    }

    protected PlayList(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.numOfSongs = in.readInt();
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
        long tmpUpdatedAt = in.readLong();
        this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
        this.songs = in.createTypedArrayList(Song.CREATOR);
        this.playingIndex = in.readInt();
    }

    public static final Parcelable.Creator<PlayList> CREATOR = new Parcelable.Creator<PlayList>() {
        @Override
        public PlayList createFromParcel(Parcel source) {
            return new PlayList(source);
        }

        @Override
        public PlayList[] newArray(int size) {
            return new PlayList[size];
        }
    };
}
