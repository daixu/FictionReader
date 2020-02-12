package com.shangame.fiction.net.response;

import android.os.Parcel;
import android.os.Parcelable;

public class TaskItem implements Parcelable {
    public double price;
    public String headimgurl;
    public String nickname;

    public int taskid;
    public String taskname;
    public String packname;
    public int receive;
    public int jumptype;

    protected TaskItem(Parcel in) {
        price = in.readDouble();
        headimgurl = in.readString();
        nickname = in.readString();
        taskid = in.readInt();
        taskname = in.readString();
        packname = in.readString();
        receive = in.readInt();
        jumptype = in.readInt();
    }

    public static final Creator<TaskItem> CREATOR = new Creator<TaskItem>() {
        @Override
        public TaskItem createFromParcel(Parcel in) {
            return new TaskItem(in);
        }

        @Override
        public TaskItem[] newArray(int size) {
            return new TaskItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(price);
        dest.writeString(headimgurl);
        dest.writeString(nickname);
        dest.writeInt(taskid);
        dest.writeString(taskname);
        dest.writeString(packname);
        dest.writeInt(receive);
        dest.writeInt(jumptype);
    }
}
