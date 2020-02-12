package com.shangame.fiction.net.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Speedy on 2019/7/24
 */
public class ClassAllFigResponse implements Parcelable {

    public List<SuperDataBean> superdata;

    public static class SuperDataBean implements Parcelable {
        /**
         * classid : 1
         * classname : sample string 2
         * bookcover : sample string 3
         * subdata : [{"classid":1,"classname":"sample string 2"},{"classid":1,"classname":"sample string 2"}]
         */

        public int classid;
        public String classname;
        public String bookcover;
        public List<SubDataBean> subdata;

        public static class SubDataBean implements Parcelable {
            /**
             * classid : 1
             * classname : sample string 2
             */

            public int classid;
            public String classname;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.classid);
                dest.writeString(this.classname);
            }

            public SubDataBean() {
            }

            protected SubDataBean(Parcel in) {
                this.classid = in.readInt();
                this.classname = in.readString();
            }

            public static final Creator<SubDataBean> CREATOR = new Creator<SubDataBean>() {
                @Override
                public SubDataBean createFromParcel(Parcel source) {
                    return new SubDataBean(source);
                }

                @Override
                public SubDataBean[] newArray(int size) {
                    return new SubDataBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.classid);
            dest.writeString(this.classname);
            dest.writeString(this.bookcover);
            dest.writeList(this.subdata);
        }

        public SuperDataBean() {
        }

        protected SuperDataBean(Parcel in) {
            this.classid = in.readInt();
            this.classname = in.readString();
            this.bookcover = in.readString();
            this.subdata = new ArrayList<SubDataBean>();
            in.readList(this.subdata, SubDataBean.class.getClassLoader());
        }

        public static final Creator<SuperDataBean> CREATOR = new Creator<SuperDataBean>() {
            @Override
            public SuperDataBean createFromParcel(Parcel source) {
                return new SuperDataBean(source);
            }

            @Override
            public SuperDataBean[] newArray(int size) {
                return new SuperDataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.superdata);
    }

    public ClassAllFigResponse() {
    }

    protected ClassAllFigResponse(Parcel in) {
        this.superdata = new ArrayList<SuperDataBean>();
        in.readList(this.superdata, SuperDataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ClassAllFigResponse> CREATOR = new Parcelable.Creator<ClassAllFigResponse>() {
        @Override
        public ClassAllFigResponse createFromParcel(Parcel source) {
            return new ClassAllFigResponse(source);
        }

        @Override
        public ClassAllFigResponse[] newArray(int size) {
            return new ClassAllFigResponse[size];
        }
    };
}
