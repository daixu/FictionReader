package com.shangame.fiction.ui.listen.palyer;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class Song implements Parcelable {

    public int id;
    public int albumid;
    public int lastcid;
    public int nextcid;
    public int buyStatus;
    public String albumName;
    public String showCover;
    public String showName;
    public String url;
    public int duration;
    public int bookShelves;
    public BigDecimal readMoney;
    public int chargingMode;
    public BigDecimal chapterPrice;
    public int isVip;
    public int autoRenew;
    public int chapterNumber;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.albumid);
        dest.writeInt(this.lastcid);
        dest.writeInt(this.nextcid);
        dest.writeInt(this.buyStatus);
        dest.writeString(this.albumName);
        dest.writeString(this.showCover);
        dest.writeString(this.showName);
        dest.writeString(this.url);
        dest.writeInt(this.duration);
        dest.writeInt(this.bookShelves);
        dest.writeSerializable(this.readMoney);
        dest.writeInt(this.chargingMode);
        dest.writeSerializable(this.chapterPrice);
        dest.writeInt(this.isVip);
        dest.writeInt(this.autoRenew);
        dest.writeInt(this.chapterNumber);
    }

    public Song() {
    }

    protected Song(Parcel in) {
        this.id = in.readInt();
        this.albumid = in.readInt();
        this.lastcid = in.readInt();
        this.nextcid = in.readInt();
        this.buyStatus = in.readInt();
        this.albumName = in.readString();
        this.showCover = in.readString();
        this.showName = in.readString();
        this.url = in.readString();
        this.duration = in.readInt();
        this.bookShelves = in.readInt();
        this.readMoney = (BigDecimal) in.readSerializable();
        this.chargingMode = in.readInt();
        this.chapterPrice = (BigDecimal) in.readSerializable();
        this.isVip = in.readInt();
        this.autoRenew = in.readInt();
        this.chapterNumber = in.readInt();
    }

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel source) {
            return new Song(source);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}
