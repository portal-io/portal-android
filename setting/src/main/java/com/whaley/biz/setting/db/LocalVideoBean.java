package com.whaley.biz.setting.db;

import com.whaley.biz.setting.constant.ProgramConstants;
import com.whaley.biz.setting.model.player.DataBuilder;
import com.whaley.biz.setting.model.player.PlayData;
import com.whaley.biz.setting.ui.viewmodel.LocalVideoImportViewModel;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class LocalVideoBean implements Serializable {
    @Id
    public String id;
    public String name;
    public long size;
    public String path;
    public long duration;
    public String picPath;
    public int progress;
    public long progressSize;
    public long lastUpdateTime;
    public boolean isDowloading = false;
    public long speed;

    public static LocalVideoBean create(LocalVideoImportViewModel bean){
        return new LocalVideoBean(String.valueOf(bean.id), bean.name, bean.size, bean.path,
                bean.duration, bean.picPath, 0, 0, 0, false, 0);
    }

    @Generated(hash = 1272979920)
    public LocalVideoBean(String id, String name, long size, String path,
                          long duration, String picPath, int progress, long progressSize,
                          long lastUpdateTime, boolean isDowloading, long speed) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.path = path;
        this.duration = duration;
        this.picPath = picPath;
        this.progress = progress;
        this.progressSize = progressSize;
        this.lastUpdateTime = lastUpdateTime;
        this.isDowloading = isDowloading;
        this.speed = speed;
    }

    @Generated(hash = 1926403013)
    public LocalVideoBean() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getProgressSize() {
        return progressSize;
    }

    public void setProgressSize(long progressSize) {
        this.progressSize = progressSize;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public boolean isDowloading() {
        return isDowloading;
    }

    public void setDowloading(boolean dowloading) {
        isDowloading = dowloading;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public boolean getIsDowloading() {
        return this.isDowloading;
    }

    public void setIsDowloading(boolean isDowloading) {
        this.isDowloading = isDowloading;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LocalVideoBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", path='" + path + '\'' +
                ", duration=" + duration +
                ", picPath='" + picPath + '\'' +
                ", progress=" + progress +
                ", progressSize=" + progressSize +
                ", lastUpdateTime=" + lastUpdateTime +
                ", isDowloading=" + isDowloading +
                ", speed=" + speed +
                '}';
    }

    public PlayData getPlayData() {
        return DataBuilder.createBuilder(getPath(), ProgramConstants.TYPE_PLAY_LOCALVIDEO)
                .setId(getId())
                .setTitle(getName())
                .putCustomData("key_is_Can_change_render",true)
                .setMonocular(true)
                .build();
    }

}
