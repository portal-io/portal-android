package com.whaleyvr.biz.danmu.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2017/5/8.
 */

public class SocketModel implements Parcelable {
    /**
     * serid : 1
     * livemax : 1000
     * status : 1
     * title : 开发测试服务器
     * host : 106.75.72.2
     * lhost : 10.19.130.59
     * port : 3000
     */

    private String serid;
    private String livemax;
    private String status;
    private String title;
    private String host;
    private String lhost;
    private String port;

    public void setSerid(String serid) {
        this.serid = serid;
    }

    public void setLivemax(String livemax) {
        this.livemax = livemax;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setLhost(String lhost) {
        this.lhost = lhost;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSerid() {
        return serid;
    }

    public String getLivemax() {
        return livemax;
    }

    public String getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public String getHost() {
        return host;
    }

    public String getLhost() {
        return lhost;
    }

    public String getPort() {
        return port;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.serid);
        dest.writeString(this.livemax);
        dest.writeString(this.status);
        dest.writeString(this.title);
        dest.writeString(this.host);
        dest.writeString(this.lhost);
        dest.writeString(this.port);
    }

    public SocketModel() {
    }

    protected SocketModel(Parcel in) {
        this.serid = in.readString();
        this.livemax = in.readString();
        this.status = in.readString();
        this.title = in.readString();
        this.host = in.readString();
        this.lhost = in.readString();
        this.port = in.readString();
    }

    public static final Creator<SocketModel> CREATOR = new Creator<SocketModel>() {
        public SocketModel createFromParcel(Parcel source) {
            return new SocketModel(source);
        }

        public SocketModel[] newArray(int size) {
            return new SocketModel[size];
        }
    };
}
